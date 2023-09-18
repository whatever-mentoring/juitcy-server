package com.ewhatever.qna.post.service;

import com.ewhatever.qna.answer.entity.Answer;
import com.ewhatever.qna.answer.repository.AnswerRepository;
import com.ewhatever.qna.comment.entity.Comment;
import com.ewhatever.qna.comment.repository.CommentRepository;
import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.enums.Category;
import com.ewhatever.qna.common.enums.Role;
import com.ewhatever.qna.post.dto.GetPostRes;
import com.ewhatever.qna.post.dto.GetPostsRes;
import com.ewhatever.qna.post.entity.Post;
import com.ewhatever.qna.post.repository.PostRepository;
import com.ewhatever.qna.scrap.repository.ScrapRepository;
import com.ewhatever.qna.user.entity.User;
import com.ewhatever.qna.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.*;
import static com.ewhatever.qna.common.Constant.Role.SENIOR;
import static com.ewhatever.qna.common.Constant.Status.ACTIVE;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final CommentRepository commentRepository;

    /**
     * 쥬시글 전체 목록 조회
     */
    public Page<GetPostsRes> getPosts(Pageable page) throws BaseException {
        try {
            Page<Post> postPage = postRepository.findAllByIsJuicy(page, true); //주씨글 여부 컬럼으로 찾기
            return postPage.map(post -> new GetPostsRes(
                    post.getCategory().name(),
                    post.getLastModifiedDate(),
                    post.getScrapCount(),
                    post.getCommentCount(),
                    getCardList(post) // 질문제목, 질문상세, 답변(0-3개)
            ));
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private List<String> getCardList(Post post) {
        List<String> cardList = new ArrayList<>();

        cardList.add(post.getTitle());
        cardList.add(post.getContent());
        List<Answer> answers = answerRepository.findAllByPost(post);
        for (Answer answer : answers) {
           cardList.add(answer.getContent());
        }
        return cardList;
    }

    /**
     * 쥬시글 카테고리 기반 목록 조회
     */
    public Page<GetPostsRes> getPostsByCategory(String category, Pageable page) throws BaseException {
        try {
            Category categoryName = Category.valueOf(category.toUpperCase());
            if (categoryName != null) {
                boolean postExists = postRepository.existsByCategory(categoryName);
                if (postExists) {
                    Page<Post> postPage = postRepository.findByCategory(categoryName, page);
                    return postPage.map(post -> new GetPostsRes(
                            post.getCategory().name(),
                            post.getLastModifiedDate(),
                            post.getScrapCount(),
                            post.getCommentCount(),
                            getCardList(post) // 질문제목, 질문상세, 답변(0-3개)
                    ));
                } else throw new BaseException(NULL_POST);
            } else throw new BaseException(INVALID_CATEGORY);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 쥬시글 상세 조회
     */
    public GetPostRes getPost(Long postIdx, Long userIdx) throws BaseException {
        try {
            Post post = postRepository.findById(postIdx).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE);
            return new GetPostRes(post.getCategory().toString(), getCardList(post), post.getLastModifiedDate(),
                    post.getCommentCount(), post.getScrapCount(), isScrap(user, post), getCommentList(post, user));

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
        List<Comment> comments = commentRepository.findAllByPost(post);

        for (Comment comment : comments) {
            GetPostRes.CommentDto commentDto = new GetPostRes.CommentDto(getWriter(comment.getWriter().getRole()), comment.getCreatedDate(),
                    comment.getContent(), isWriter(user, comment));
            commentList.add(commentDto);
        }
        return commentList;
    }

    // 댓글 작성자 여부
    private Boolean isWriter(User user, Comment comment) {
        if (comment.getWriter().equals(user)) return true;
        else return false;
    }

    // 댓글 작성자
    private String getWriter(Role role) {
        if (role.equals(SENIOR)) return "익명의 시니";
        else return "익명의 쥬니";
    }
}
