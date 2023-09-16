package com.ewhatever.qna.comment.service;

import com.ewhatever.qna.comment.dto.PostCommentReq;
import com.ewhatever.qna.comment.entity.Comment;
import com.ewhatever.qna.comment.repository.CommentRepository;
import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponseStatus;
import com.ewhatever.qna.post.repository.PostRepository;
import com.ewhatever.qna.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.INVALID_POST_IDX;
import static com.ewhatever.qna.common.Base.BaseResponseStatus.INVALID_USER;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    public Long addComment(PostCommentReq postCommentReq) throws BaseException {//TODO : service를 어디서 호출해야할지..
        Comment comment = Comment.builder().content(postCommentReq.getContent())
                .post(postRepository.findById(postCommentReq.getPostIdx()).orElseThrow(()-> new BaseException(INVALID_POST_IDX)))
                .writer(userRepository.findById(1L).orElseThrow(()-> new BaseException(INVALID_USER))).build();//TODO : getUserIdx로 수정하기
        return commentRepository.save(comment).getCommentIdx();//TODO : Location Header 에 리소스 위치를 알려줄 필요가 있는지 생각해보기
    }
}
