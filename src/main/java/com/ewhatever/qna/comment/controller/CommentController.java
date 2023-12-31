package com.ewhatever.qna.comment.controller;

import com.ewhatever.qna.comment.dto.PostCommentReq;
import com.ewhatever.qna.comment.dto.UpdateCommentReq;
import com.ewhatever.qna.comment.service.CommentService;
import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponse;
import com.ewhatever.qna.common.Base.BaseResponseStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.persister.entity.mutation.UpdateCoordinator;
import org.springframework.web.bind.annotation.*;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @ResponseBody
    @PostMapping
    public BaseResponse<String> addComment(HttpServletRequest request,
                                           @RequestBody PostCommentReq postCommentReq) throws BaseException {
        commentService.addComment(request.getHeader("Authorization"), postCommentReq);
        return new BaseResponse<>(SUCCESS);
    }

    @ResponseBody
    @PatchMapping("/{commentIdx}")
    public BaseResponse<String> updateComment(HttpServletRequest request, @PathVariable Long commentIdx, @RequestBody UpdateCommentReq updateCommentReq) throws BaseException {
        commentService.updateComment(commentIdx, updateCommentReq);
        return new BaseResponse<>(SUCCESS);
    }

    @ResponseBody
    @DeleteMapping("/{commentIdx}")
    public BaseResponse<String> deleteComment(@PathVariable Long commentIdx) throws BaseException {
        commentService.deleteComment(commentIdx);
        return new BaseResponse<>(SUCCESS);
    }
}
