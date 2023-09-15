package com.ewhatever.qna.post.service;

import com.ewhatever.qna.answer.entity.Answer;
import com.ewhatever.qna.answer.repository.AnswerRepository;
import com.ewhatever.qna.common.Base.BaseException;
import com.ewhatever.qna.common.enums.Category;
import com.ewhatever.qna.post.dto.GetPostsRes;
import com.ewhatever.qna.post.entity.Post;
import com.ewhatever.qna.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ewhatever.qna.common.Base.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AnswerRepository answerRepository;

    /**
     * 주씨글 전체 목록 조회
     */
    public Page<GetPostsRes> getPosts(Pageable page) throws BaseException {
        try {
            Page<Post> postPage = postRepository.findAllByIsJuicy(page, true); //주씨글 여부 컬럼으로 찾기
            return postPage.map(post -> new GetPostsRes(
                    post.getCategory().name(),
                    post.getLastModifiedDate(),
                    post.getScrapCount(),
                    post.getCommentCount(),
                    getCardList(post) // 질문제목, 질문상세, 답변(0-3개)
            ));
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private List<String> getCardList(Post post) {
        List<String> cardList = new ArrayList<>();

        cardList.add(post.getTitle());
        cardList.add(post.getContent());
        List<Answer> answers = answerRepository.findAllByPost(post);
        for (Answer answer : answers) {
           cardList.add(answer.getContent());
        }
        return cardList;
    }

    /**
     * 주씨글 카테고리 기반 목록 조회
     */
    public Page<GetPostsRes> getPostsByCategory(String category, Pageable page) throws BaseException {
        try {
            Category categoryName = Category.valueOf(category.toUpperCase());
            if (categoryName != null) {
                boolean postExists = postRepository.existsByCategory(categoryName);
                if (postExists) {
                    Page<Post> postPage = postRepository.findByCategory(categoryName, page);
                    return postPage.map(post -> new GetPostsRes(
                            post.getCategory().name(),
                            post.getLastModifiedDate(),
                            post.getScrapCount(),
                            post.getCommentCount(),
                            getCardList(post) // 질문제목, 질문상세, 답변(0-3개)
                    ));
                } else throw new BaseException(NULL_POST);
            } else throw new BaseException(INVALID_CATEGORY);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
