package com.ewhatever.qna.answer.service;

import com.ewhatever.qna.answer.dto.PostAnswerReq;
import com.ewhatever.qna.answer.entity.Answer;
import com.ewhatever.qna.answer.repository.AnswerRepository;
import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.enums.Role;
import com.ewhatever.qna.post.entity.Post;
import com.ewhatever.qna.post.repository.PostRepository;
import com.ewhatever.qna.user.entity.User;
import com.ewhatever.qna.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.*;
import static com.ewhatever.qna.common.Constant.ACTIVE;

@Service
public class AnswerService {
    UserRepository userRepository;
    PostRepository postRepository;
    AnswerRepository answerRepository;
    /**
     * 답변 등록
     */
    public void addAnswer(Long userIdx, PostAnswerReq postAnswerReq) throws BaseException {
        try {
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE);
            Post post = postRepository.findById(postAnswerReq.getPostIdx()).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
            if (user.getRole().equals(Role.SINY)) {
                Answer answer = Answer.builder()
                        .content(postAnswerReq.getAnswer())
                        .answerer(user)
                        .post(post)
                        .build();
                answerRepository.save(answer);
            } else throw new BaseException(NO_SENIOR_ROLE);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
