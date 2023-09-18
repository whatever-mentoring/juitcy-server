package com.ewhatever.qna.question.service;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.enums.Category;
import com.ewhatever.qna.post.entity.Post;
import com.ewhatever.qna.post.repository.PostRepository;
import com.ewhatever.qna.question.dto.PostQuestionReq;
import com.ewhatever.qna.user.entity.User;
import com.ewhatever.qna.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.*;
import static com.ewhatever.qna.common.Constant.Status.ACTIVE;
import static com.ewhatever.qna.common.enums.Role.SINY;

@Service
public class QuestionService {
    UserRepository userRepository;
    PostRepository postRepository;

    /**
     * 질문 등록
     * @param userIdx
     * @param postQuestionReq
     * @throws BaseException
     */
    public void addQuestion(Long userIdx, PostQuestionReq postQuestionReq) throws BaseException {
        try {
            User questioner = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER));
            Category category = Category.valueOf(postQuestionReq.getCategory());

            if (questioner.getRole().equals(SINY)) {
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
}
