package com.ewhatever.qna.user.service;

import com.ewhatever.qna.answer.repository.AnswerRepository;
import com.ewhatever.qna.comment.repository.CommentRepository;
import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponse;
import com.ewhatever.qna.common.enums.Role;
import com.ewhatever.qna.login.CustomUnauthorizedException;
import com.ewhatever.qna.login.JwtIssuer;
import com.ewhatever.qna.login.dto.AuthService;
import com.ewhatever.qna.post.repository.PostRepository;
import com.ewhatever.qna.scrap.repository.ScrapRepository;
import com.ewhatever.qna.user.dto.*;
import com.ewhatever.qna.user.entity.User;
import com.ewhatever.qna.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final AnswerRepository answerRepository;
    private final ScrapRepository scrapRepository;

    private final AuthService authService;

    private final JwtIssuer jwtIssuer;

    // TODO : 이거 BaseResponse 말고 객체 반환하도록 수정
    public BaseResponse<?> getProfile(String token) throws BaseException {
        if(!jwtIssuer.validateToken(token)) throw new CustomUnauthorizedException(INVALID_TOKEN.getMessage());
        User user = userRepository.findById(authService.getUserIdx(token)).orElseThrow(()-> new BaseException(INVALID_USER));
        if(user.getRole().equals(Role.Cyni)) return getSinyProfile(user);
        else return getJunyProfile(user);
    }

    // TODO : code duplication 해결
    // TODO : getPageable 메소드 호출
    public Page<GetSinyAnswerResponse> getMyAnswers(String token, String status, int requestPageNum) throws BaseException {
        if(!jwtIssuer.validateToken(token)) throw new CustomUnauthorizedException(INVALID_TOKEN.getMessage());
        log.info("*** status : [{}], requestPageNum : [{}]", status, requestPageNum);
        User user = userRepository.findById(authService.getUserIdx(token)).orElseThrow(()-> new BaseException(INVALID_USER));
        checkSinyRole(user.getRole());
        List<Sort.Order> sorts = new ArrayList<>();
        if(status.equals("completed")) {//쥬시 완료
            sorts.add(Sort.Order.desc("post_JuicyDate"));
            Pageable pageable = PageRequest.of(Math.max(requestPageNum-1,  0), 10, Sort.by(sorts));
            return answerRepository.findByAnswerer_UserIdxAndPost_IsJuicyTrueAndStatusEquals(user.getUserIdx(), "active", pageable).map(answer ->
                    GetSinyAnswerResponse.fromAnswer(answer, 3L, true));
        }
        else {//쥬시 대기
            sorts.add(Sort.Order.asc("createdDate"));
            Pageable pageable = PageRequest.of(Math.max(requestPageNum-1,  0), 10, Sort.by(sorts));
            return answerRepository.findByAnswerer_UserIdxAndPost_IsJuicyFalseAndStatusEquals(user.getUserIdx(), "active", pageable).map(answer ->
                    GetSinyAnswerResponse.fromAnswer(answer, answerRepository.countByPost_PostIdxAndStatusEquals(answer.getPost().getPostIdx(), "active"), false));
        }

    }

    public Page<GetJunyQuestionResponse> getMyQuestions(String token, String status, int requestPageNum) throws BaseException {
        if(!jwtIssuer.validateToken(token)) throw new CustomUnauthorizedException(INVALID_TOKEN.getMessage());
        User user = userRepository.findById(authService.getUserIdx(token)).orElseThrow(()-> new BaseException(INVALID_USER));
        checkJunyRole(user.getRole());
        List<Sort.Order> sorts = new ArrayList<>();
        if(status.equals("completed")) {//쥬시 완료
            sorts.add(Sort.Order.desc("juicyDate"));
            Pageable pageable = PageRequest.of(Math.max(requestPageNum-1,  0), 10, Sort.by(sorts));
            return postRepository.findByQuestioner_UserIdxAndIsJuicyTrueAndStatusEquals(user.getUserIdx(),"active", pageable).map(post ->
                    GetJunyQuestionResponse.fromPost(post, 3L, true));
        }
        else {//쥬시 대기
            sorts.add(Sort.Order.asc("createdDate"));
            Pageable pageable = PageRequest.of(Math.max(requestPageNum-1,  0), 10, Sort.by(sorts));
            return postRepository.findByQuestioner_UserIdxAndIsJuicyFalseAndStatusEquals(user.getUserIdx(), "active", pageable).map(post ->
                    GetJunyQuestionResponse.fromPost(post, answerRepository.countByPost_PostIdxAndStatusEquals(post.getPostIdx(), "active"), false));
        }
    }

    public Page<GetCommentResponse> getMyComments(String token, int requestPageNum) throws BaseException {
        if(!jwtIssuer.validateToken(token)) throw new CustomUnauthorizedException(INVALID_TOKEN.getMessage());
        User user = userRepository.findById(authService.getUserIdx(token)).orElseThrow(()-> new BaseException(INVALID_USER));
        Pageable pageable = getPageable(requestPageNum, 10, "createdDate");
        return commentRepository.findByWriter_UserIdxAndStatusEquals(user.getUserIdx(), "active", pageable).map(GetCommentResponse::fromComment);
    }

    public Page<GetScrapResponse> getMyScraps(String token, int requestPageNum) throws BaseException {
        if(!jwtIssuer.validateToken(token)) throw new CustomUnauthorizedException(INVALID_TOKEN.getMessage());
        User user = userRepository.findById(authService.getUserIdx(token)).orElseThrow(()-> new BaseException(INVALID_USER));
        Pageable pageable = getPageable(requestPageNum, 10, "createdDate");
        return scrapRepository.findByUser_UserIdxAndStatusEquals(user.getUserIdx(), "active", pageable).map(GetScrapResponse::fromScrap);

    }

    private Pageable getPageable(int requestPageNum, int pageSize, String property) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc(property));
        return PageRequest.of(Math.max(requestPageNum-1,  0), pageSize, Sort.by(sorts));
    }

    private BaseResponse<GetJunyProfileResponse> getJunyProfile(User user) {
        return new BaseResponse<>(GetJunyProfileResponse.builder().name(user.getName())
                .questionCount(postRepository.countByQuestioner_UserIdxAndStatusEquals(user.getUserIdx(), "active"))
                .commentCount(commentRepository.countByWriter_UserIdxAndStatusEquals(user.getUserIdx(), "active"))
                .scrapCount(scrapRepository.countByUser_UserIdxAndStatusEquals(user.getUserIdx(), "active"))
                .build());
    }

    private BaseResponse<GetSinyProfileResponse> getSinyProfile(User user) {
        return new BaseResponse<>(GetSinyProfileResponse.builder().name(user.getName())
                .answerCount(answerRepository.countByAnswerer_UserIdxAndStatusEquals(user.getUserIdx(), "active"))
                .commentCount(commentRepository.countByWriter_UserIdxAndStatusEquals(user.getUserIdx(), "active"))
                .scrapCount(scrapRepository.countByUser_UserIdxAndStatusEquals(user.getUserIdx(), "active"))
                .build());
    }

    private void checkJunyRole(Role role) throws BaseException {
        if(role.equals(Role.Juni)) return;
        else throw new BaseException(NO_JUNIOR_ROLE);
    }

    private void checkSinyRole(Role role) throws BaseException {
        if(role.equals(Role.Cyni)) return;
        else throw new BaseException(NO_SENIOR_ROLE);
    }
}
