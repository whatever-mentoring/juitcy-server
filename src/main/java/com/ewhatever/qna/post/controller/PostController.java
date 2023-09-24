package com.ewhatever.qna.post.controller;

import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.Base.BaseResponse;
import com.ewhatever.qna.login.JwtIssuer;
import com.ewhatever.qna.post.dto.GetPostRes;
import com.ewhatever.qna.post.dto.GetPostsRes;
import com.ewhatever.qna.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping(value = "/posts", produces = "application/json; charset=UTF8")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;


    /**
     * [GET] 쥬시글 목록 조회
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<Page<GetPostsRes>> getPosts(HttpServletRequest request, Pageable page, @RequestParam(required = false) String category, @RequestParam(required = false) String searchWord) {
        try {
            postService.validate(request.getHeader("Authorization"));
            if (category.isBlank()) { // 카테고리 X
                if (searchWord.isBlank()) return new BaseResponse<>(postService.getPosts(page)); // 전체 목록 조회
                else return new BaseResponse<>(postService.getPostsBySearchWord(searchWord, page));
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
    public BaseResponse<GetPostRes> getPost(HttpServletRequest request, @PathVariable Long postIdx) {
        try {
            return new BaseResponse<>(postService.getPost(request.getHeader("Authorization"), postIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * [POST] 스크랩/취소
     */
    @ResponseBody
    @PostMapping("/{postIdx}")
    public BaseResponse<String> scrapPost(HttpServletRequest request, @PathVariable Long postIdx) {
        try {
            postService.scrapPost(request.getHeader("Authorization"), postIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}