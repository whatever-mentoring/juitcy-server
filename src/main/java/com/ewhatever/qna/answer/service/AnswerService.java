package com.ewhatever.qna.answer.service;

import com.ewhatever.qna.answer.dto.PostAnswerReq;
import com.ewhatever.qna.answer.entity.Answer;
import com.ewhatever.qna.answer.repository.AnswerRepository;
import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.post.entity.Post;
import com.ewhatever.qna.post.repository.PostRepository;
import com.ewhatever.qna.user.entity.User;
import com.ewhatever.qna.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.*;
import static com.ewhatever.qna.common.Constant.Status.ACTIVE;
import static com.ewhatever.qna.common.enums.Role.SINY;

@Service
public class AnswerService {
    UserRepository userRepository;
    PostRepository postRepository;
    AnswerRepository answerRepository;

    @Transactional(rollbackFor = Exception.class)
    public void addAnswer(Long userIdx, PostAnswerReq postAnswerReq) throws BaseException {
        try {
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER));
            Post post = postRepository.findById(postAnswerReq.getPostIdx()).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
            Long currentAnswerCount = answerRepository.countByPost(post);
            if (user.getRole().equals(SINY)) {
                if (currentAnswerCount < 3) { // 크론잡 주기 사이에 답변이 등록되어 isJuicy 컬럼값에 아직 반영이 안된 경우를 위해 예외처리
                    Answer answer = Answer.builder()
                            .content(postAnswerReq.getAnswer())
                            .answerer(user)
                            .post(post)
                            .build();
                    answerRepository.save(answer);

                    currentAnswerCount = answerRepository.countByPost(post); //currentAnswerCount 최신화
                    // 3번째 답변이면 쥬시글로 전환
                    if (currentAnswerCount == 3) {
                        post.setIsJuicy(true);
                    }
                    postRepository.save(post);
                } else throw new BaseException(ALREADY_JUICY_POST);
            } else throw new BaseException(NO_SENIOR_ROLE);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
