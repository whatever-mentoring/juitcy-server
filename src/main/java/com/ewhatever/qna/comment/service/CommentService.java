package com.ewhatever.qna.comment.service;

import com.ewhatever.qna.comment.dto.PostCommentReq;
import com.ewhatever.qna.comment.dto.UpdateCommentReq;
import com.ewhatever.qna.comment.entity.Comment;
import com.ewhatever.qna.comment.repository.CommentRepository;
import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.post.entity.Post;
import com.ewhatever.qna.post.repository.PostRepository;
import com.ewhatever.qna.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.*;
import static com.ewhatever.qna.common.Constant.Status.INACTIVE;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    @Transactional
    public void addComment(PostCommentReq postCommentReq) throws BaseException {//TODO : service를 어디서 호출해야할지..
        Post post = postRepository.findById(postCommentReq.getPostIdx()).orElseThrow(()-> new BaseException(INVALID_POST_IDX));
        Comment comment = Comment.builder().content(postCommentReq.getContent())
                .post(post)
                .writer(userRepository.findById(1L).orElseThrow(()-> new BaseException(INVALID_USER))).build();//TODO : getUserIdx로 수정하기
        commentRepository.save(comment).getCommentIdx();
        post.setCommentCount(post.getCommentCount()+1L);
    }

    @Transactional
    public void deleteComment(Long commentIdx) throws BaseException {
        //commentRepository.deleteById(commentIdx);
        Comment comment = commentRepository.findById(commentIdx).orElseThrow(()-> new BaseException(INVALID_COMMENT_IDX));
        comment.setStatus(INACTIVE);
        Post post = postRepository.findById(comment.getPost().getPostIdx()).orElseThrow(() -> new BaseException(INVALID_POST_IDX));
        post.setCommentCount(post.getCommentCount()-1L);
    }

    @Transactional
    public void updateComment(Long commentIdx, UpdateCommentReq updateCommentReq) throws BaseException {
        Comment comment = commentRepository.findById(commentIdx).orElseThrow(()-> new BaseException(INVALID_COMMENT_IDX));
        comment.setContent(updateCommentReq.getContent());
    }
}
