package com.ewhatever.qna.post.controller;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponse;
import com.ewhatever.qna.post.dto.GetPostRes;
import com.ewhatever.qna.post.dto.GetPostsRes;
import com.ewhatever.qna.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    /**
     * [GET] 쥬시글 목록 조회
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<Page<GetPostsRes>> getPosts(Pageable page,
                                                    @RequestParam(required = false) String category) {
        try {
            if (category.isBlank()) { // 전체 조회
                return new BaseResponse<>(postService.getPosts(page));
            } else return new BaseResponse<>(postService.getPostsByCategory(category, page)); // 카테고리 조회
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * [GET] 쥬시글 상세 조회
     */
    @ResponseBody
    @GetMapping("/{postIdx}")
    public BaseResponse<GetPostRes> getPost(@PathVariable Long postIdx, Long userIdx) { // TODO: 추후 getUserIdx로 수정
        try {
            return new BaseResponse<>(postService.getPost(postIdx, userIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * [POST] 스크랩/취소
     */
    @ResponseBody
    @PostMapping("/{postIdx}")
    public BaseResponse<String> scrapPost(@PathVariable Long postIdx, Long userIdx) { // TODO: 추후 getUserIdx로 수정
        try {
            postService.scrapPost(postIdx, userIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}