package com.ewhatever.qna.post.service;

import com.ewhatever.qna.answer.entity.Answer;
import com.ewhatever.qna.answer.repository.AnswerRepository;
import com.ewhatever.qna.comment.entity.Comment;
import com.ewhatever.qna.comment.repository.CommentRepository;
import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.enums.Category;
import com.ewhatever.qna.common.enums.Role;
import com.ewhatever.qna.login.CustomUnauthorizedException;
import com.ewhatever.qna.login.JwtIssuer;
import com.ewhatever.qna.login.dto.AuthService;
import com.ewhatever.qna.post.dto.GetPostRes;
import com.ewhatever.qna.post.dto.GetPostsRes;
import com.ewhatever.qna.post.entity.Post;
import com.ewhatever.qna.post.repository.PostRepository;
import com.ewhatever.qna.scrap.entity.Scrap;
import com.ewhatever.qna.scrap.repository.ScrapRepository;
import com.ewhatever.qna.user.entity.User;
import com.ewhatever.qna.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.*;
import static com.ewhatever.qna.common.Constant.Status.ACTIVE;
import static com.ewhatever.qna.common.Constant.Status.INACTIVE;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final CommentRepository commentRepository;

    private final AuthService authService;

    private final JwtIssuer jwtIssuer;
    //tmp
    public void validate(String token) {
        jwtIssuer.validateToken(token);
    }
    /**
     * 쥬시글 전체 목록 조회
     * @param page
     * @return Page<GetPostsRes>
     * @throws BaseException
     */
    public Page<GetPostsRes> getPosts(Pageable page) throws BaseException {
        try {
            Page<Post> postPage = postRepository.findAllByIsJuicyTrueOrderByLastModifiedDateDesc(page); // 최신순 조회
            return postPage.map(post -> new GetPostsRes(
                    post.getPostIdx(),
                    post.getCategory().getKrName(),
                    post.getLastModifiedDate(),
                    post.getScrapCount(),
                    post.getCommentCount(),
                    getCardList(post) // 질문제목, 질문상세, 답변(0-3개)
            ));
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 카드 리스트 조회
    private List<String> getCardList(Post post) {
        List<String> cardList = new ArrayList<>();

        cardList.add(post.getTitle());
        cardList.add(post.getContent());
        List<Answer> answers = answerRepository.findAllByPostOrderByCreatedDateDesc(post); // 답변 등록 날짜 순 조회
        for (Answer answer : answers) {
            cardList.add(answer.getContent());
        }
        return cardList;
    }

    /**
     * 쥬시글 카테고리 기반 목록 조회
     * @param category
     * @param page
     * @return Page<GetPostsRes>
     * @throws BaseException
     */
    public Page<GetPostsRes> getPostsByCategory(String category, Pageable page) throws BaseException {
        try {
            Category categoryName = Category.valueOf(category.toUpperCase());
            boolean postExists = postRepository.existsByCategoryAndIsJuicyTrue(categoryName);
            if (postExists) {
                Page<Post> postPage = postRepository.findAllByCategoryAndIsJuicyTrueOrderByLastModifiedDateDesc(categoryName, page); // 최신순 조회
                return postPage.map(post -> new GetPostsRes(
                        post.getPostIdx(),
                        post.getCategory().getKrName(),
                        post.getLastModifiedDate(),
                        post.getScrapCount(),
                        post.getCommentCount(),
                        getCardList(post) // 질문제목, 질문상세, 답변(0-3개)
                ));
            } else throw new BaseException(NULL_POST);
        } catch (IllegalArgumentException e) {
            throw new BaseException(INVALID_CATEGORY);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 쥬시글 검색
     * @param searchWord
     * @param page
     * @return Page<GetPostsRes>
     * @throws BaseException
     */
    public Page<GetPostsRes> getPostsBySearchWord(String searchWord, Pageable page) throws BaseException {
        try {
            boolean searchResultExists = postRepository.existsJuicyPosts(searchWord);
            if (searchResultExists) {
                return postRepository.searchJuicyPosts(searchWord, page)
                        .map(searchPost -> new GetPostsRes(
                                searchPost.getPostIdx(),
                                searchPost.getCategory().getKrName(),
                                searchPost.getLastModifiedDate(),
                                searchPost.getScrapCount(),
                                searchPost.getCommentCount(),
                                getCardList(searchPost)
                        ));
            } else throw new BaseException(NULL_POST);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 쥬시글 상세 조회
     * @param postIdx
     * @return GetPostRes
     * @throws BaseException
     */
    public GetPostRes getPost(String token, Long postIdx) throws BaseException {
        if(!jwtIssuer.validateToken(token)) throw new CustomUnauthorizedException(INVALID_TOKEN.getMessage());
        try {
            Post post = postRepository.findById(postIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
            User user = userRepository.findByUserIdxAndStatusEquals(authService.getUserIdx(token), ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER));
            return new GetPostRes(
                    post.getCategory().getKrName(),
                    getCardList(post),
                    post.getLastModifiedDate(),
                    post.getCommentCount(),
                    post.getScrapCount(),
                    isScrap(user, post),
                    getCommentList(post, user)
            );
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 스크랩 여부
    private Boolean isScrap(User user, Post post) {
        return scrapRepository.existsByPostAndUserAndStatusEquals(post, user, ACTIVE);
    }

    // 댓글 list 조회
    private List<GetPostRes.CommentDto> getCommentList(Post post, User user) {
        List<GetPostRes.CommentDto> commentList = new ArrayList<>();
        List<Comment> comments = commentRepository.findAllByPostAndStatusEqualsOrderByCreatedDateDesc(post, ACTIVE); // 최신순

        for (Comment comment : comments) {
            GetPostRes.CommentDto commentDto = new GetPostRes.CommentDto(
                    comment.getCommentIdx(),
                    getWriter(comment.getWriter().getRole().name()),
                    comment.getCreatedDate(),
                    comment.getContent(),
                    isWriter(user, comment)
            );
            commentList.add(commentDto);
        }
        return commentList;
    }

    // 댓글 작성자 여부
    private Boolean isWriter(User user, Comment comment) {
        return comment.getWriter().equals(user);
    }

    // 댓글 작성자
    private String getWriter(String role) {
        if (role.equals(Role.Cyni.name())) return "익명의 시니";
        else return "익명의 쥬니";
    }

    /**
     * 스크랩/취소
     * @param postIdx
     * @throws BaseException
     */
    @Transactional(rollbackFor = Exception.class)
    public void scrapPost(String token, Long postIdx) throws BaseException {
        if(!jwtIssuer.validateToken(token)) throw new CustomUnauthorizedException(INVALID_TOKEN.getMessage());
        try {
            Post post = postRepository.findById(postIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
            User user = userRepository.findByUserIdxAndStatusEquals(authService.getUserIdx(token), ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER));
            Boolean existsByPostAndUser = scrapRepository.existsByPostAndUser(post, user);

            if (existsByPostAndUser) { // 스크랩 존재
                Scrap scrap = scrapRepository.findByPostAndUser(post, user);
                if (scrap.getStatus().equals(ACTIVE)) { // ACTIVE -> INACTIVE
                    long currentScrapCount = Optional.ofNullable(post.getScrapCount()).orElse(0L);
                    if (currentScrapCount > 0) {
                        post.setScrapCount(currentScrapCount - 1L);
                    } else throw new BaseException(ZERO_SCRAP_COUNT);
                    scrap.setStatus(INACTIVE);

                } else { // INACTIVE -> ACTIVE
                    post.setScrapCount(post.getScrapCount() + 1L);
                    scrap.setStatus(ACTIVE);

                }
                postRepository.save(post);
                scrapRepository.save(scrap);
            } else {// 스크랩 X -> 스크랩 생성(ACTIVE)
                Scrap newScrap = Scrap.builder()
                        .post(post)
                        .user(user)
                        .build();
                post.setScrapCount(post.getScrapCount() + 1L);

                scrapRepository.save(newScrap);
                postRepository.save(post);
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 매 시간 쥬시글 여부 확인해서 변환해주는 크론잡
     */
    @Scheduled(cron = "0 0 0/1 * * *")
    @Transactional(rollbackFor = Exception.class)
    public void checkJuicy() {
        List<Post> postList = postRepository.findAllByIsJuicyFalseOrderByCreatedDate(); // 오래된 순으로 조회
        for (Post post : postList) {
            LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
//            System.out.println("현재 시간: " + currentDateTime);
            LocalDateTime questionDateTime = post.getCreatedDate().truncatedTo(ChronoUnit.HOURS);
//            System.out.println("질문 등록 시간: " + questionDateTime);
//            int compareResult = currentDateTime.compareTo(questionDateTime);
//            System.out.println("비교 결과: " + compareResult);
//            Period period = Period.between(questionDate, currentDate);
            long timeGap = ChronoUnit.HOURS.between(questionDateTime, currentDateTime);
            if (timeGap == 72) {
                post.setIsJuicy(true);
                postRepository.save(post);
            }
        }
    }
}