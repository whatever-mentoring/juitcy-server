package com.ewhatever.qna.question.service;

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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.*;
import static com.ewhatever.qna.common.Constant.Status.ACTIVE;
import static com.ewhatever.qna.common.enums.Role.JUNY;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    /**
     * 질문 등록
     * @param postQuestionReq
     * @throws BaseException
     */
    public void addQuestion(String token, PostQuestionReq postQuestionReq) throws BaseException {
        try {
            User questioner = userRepository.findByUserIdxAndStatusEquals(authService.getUserIdx(token), ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER));
            Category category = Category.valueOf(postQuestionReq.getCategory());

            if (questioner.getRole().equals(JUNY)) {
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
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 질문 목록 조회
     * @param page
     * @return Page<GetQuestionsRes>
     * @throws BaseException
     */
    public Page<GetQuestionsRes> getQuestions(Pageable page) throws BaseException {
        try {
            Page<Post> postPage = postRepository.findAllByIsJuicyFalseOrderByCreatedDateDesc(page); // 최신순 조회
            return postPage.map(post -> new GetQuestionsRes(
                    post.getPostIdx(),
                    post.getCategory().getKrName(),
                    post.getTitle(),
                    post.getContent(),
                    post.getCreatedDate()
            ));
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 카테고리 기반 질문 목록 조회
     * @param category
     * @param page
     * @return Page<GetQustionsRes>
     * @throws BaseException
     */
    public Page<GetQuestionsRes> getQuestionsByCategory(String category, Pageable page) throws BaseException {
        try {
            Category categoryName = Category.valueOf(category.toUpperCase());
            boolean questionExists = postRepository.existsByCategoryAndIsJuicyFalse(categoryName);
            if (questionExists) {
                Page<Post> postPage = postRepository.findAllByCategoryAndIsJuicyFalseOrderByCreatedDateDesc(categoryName, page); // 최신순 조회
                return postPage.map(post -> new GetQuestionsRes(
                        post.getPostIdx(),
                        post.getCategory().getKrName(),
                        post.getTitle(),
                        post.getContent(),
                        post.getCreatedDate()
                ));
            } else throw new BaseException(NULL_QUESTION);
        } catch (IllegalArgumentException e) {
            throw new BaseException(INVALID_CATEGORY);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
