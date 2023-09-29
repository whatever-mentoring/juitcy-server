package com.ewhatever.qna.question.service;

import com.ewhatever.qna.answer.repository.AnswerRepository;
import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.enums.Category;
import com.ewhatever.qna.login.dto.AuthService;
import com.ewhatever.qna.post.entity.Post;
import com.ewhatever.qna.post.repository.PostRepository;
import com.ewhatever.qna.question.dto.GetQuestionsRes;
import com.ewhatever.qna.question.dto.PostQuestionReq;
import com.ewhatever.qna.user.entity.User;
import com.ewhatever.qna.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.*;
import static com.ewhatever.qna.common.Constant.Status.ACTIVE;
import static com.ewhatever.qna.common.enums.Role.Juni;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final AnswerRepository answerRepository;

    /**
     * 질문 등록
     * @param token
     * @param postQuestionReq
     * @throws BaseException
     */
    public void addQuestion(String token, PostQuestionReq postQuestionReq) throws BaseException {
        try {
            User questioner = userRepository.findByUserIdxAndStatusEquals(authService.getUserIdx(token), ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER));
            Category category = Category.valueOf(postQuestionReq.getCategory());
            if (postQuestionReq.getContent().length() < 10) throw new BaseException(SHORT_QUESTION_CONTENT);
            else if (postQuestionReq.getContent().length() > 1000) throw new BaseException(LONG_QUESTION_CONTENT);
            else if (postQuestionReq.getTitle().length() < 5) throw new BaseException(SHORT_QUESTION_TITLE);
            else if (postQuestionReq.getTitle().length() > 50) throw new BaseException(LONG_QUESTION_TITLE);
            else {
                if (questioner.getRole().equals(Juni)) {
                    Post question = Post.builder()
                            .title(postQuestionReq.getTitle())
                            .content(postQuestionReq.getContent())
                            .category(category)
                            .questioner(questioner)
                            .scrapCount(0L)
                            .commentCount(0L)
                            .isJuicy(false)
                            .build();
                    postRepository.save(question);
                } else throw new BaseException(NO_JUNIOR_ROLE);
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 질문 목록 조회
     * @param token
     * @param page
     * @return page
     * @throws BaseException
     */
    public Page<GetQuestionsRes> getQuestions(String token, Pageable page) throws BaseException {
        try {
            User user = userRepository.findByUserIdxAndStatusEquals(authService.getUserIdx(token), ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER));
            Page<Post> postPage = postRepository.findAllByIsJuicyFalseOrderByCreatedDateDesc(page); // 최신순 조회

            // get not answered posts as list
            List<Post> questionList = getQuestionList(user, postPage);

            // question(post entity) -> dto
            List<GetQuestionsRes> questionDtoList = getQuestionDtoList(questionList);

//            int pageCount = 0;
//            int size = 10;
//            PageRequest pageRequest = PageRequest.of(pageCount, size);
//            int start = (int) pageRequest.getOffset();
//            int end = (start + pageRequest.getPageSize()) > questionList.size() ? questionList.size() : (start + pageRequest.getPageSize());

            // list -> page
            return new PageImpl<>(questionDtoList, page, questionDtoList.size());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 카테고리 기반 질문 목록 조회
     * @param token
     * @param category
     * @param page
     * @return page
     * @throws BaseException
     */
    public Page<GetQuestionsRes> getQuestionsByCategory(String token, String category, Pageable page) throws BaseException {
        try {
            User user = userRepository.findByUserIdxAndStatusEquals(authService.getUserIdx(token), ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER));
            Category categoryName = Category.valueOf(category.toUpperCase());
            boolean questionExists = postRepository.existsByCategoryAndIsJuicyFalse(categoryName);
            if (questionExists) {
                Page<Post> postPage = postRepository.findAllByCategoryAndIsJuicyFalseOrderByCreatedDateDesc(categoryName, page); // 최신순 조회

                // get not answered posts as list
                List<Post> questionList = getQuestionList(user, postPage);

                // get question dto list
                List<GetQuestionsRes> questionDtoList = getQuestionDtoList(questionList);

                // list -> page
                return new PageImpl<>(questionDtoList, page, questionDtoList.size());
            } else throw new BaseException(NULL_QUESTION);
        } catch (IllegalArgumentException e) {
            throw new BaseException(INVALID_CATEGORY);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // get question(not answered post) list
    private List<Post> getQuestionList(User user, Page<Post> postPage) {
        // page -> list
        List<Post> postList = new ArrayList<>();
        if (postPage != null && postPage.hasContent()) {
            postList = postPage.getContent();
        }

        // 미답변 상태인 질문만 list로
        List<Post> questionList = new ArrayList<>();
        for (Post post : postList) {
            Boolean isAnswered = answerRepository.existsByPostAndAnswerer(post, user);
            if (!isAnswered) {
                questionList.add(post);
            }
        }
        return questionList;
    }

    // get question dto list
    private static List<GetQuestionsRes> getQuestionDtoList(List<Post> questionList) {
        return questionList.stream().map(question -> new GetQuestionsRes( // question dto list
                        question.getPostIdx(),
                        question.getCategory().getKrName(),
                        question.getTitle(),
                        question.getContent(),
                        question.getCreatedDate()))
                .collect(Collectors.toList());
    }
}
