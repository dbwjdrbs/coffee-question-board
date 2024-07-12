package com.springboot.like.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.like.entity.Like;
import com.springboot.like.repository.LikeRepository;
import com.springboot.member.service.MemberService;
import com.springboot.questionboard.entity.QuestionBoard;
import com.springboot.questionboard.service.QuestionBoardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class LikeService {
    private final MemberService memberService;
    private final LikeRepository likeRepository;
    private final QuestionBoardService questionBoardService;

    public LikeService(MemberService memberService, LikeRepository likeRepository, QuestionBoardService questionBoardService) {
        this.memberService = memberService;
        this.likeRepository = likeRepository;
        this.questionBoardService = questionBoardService;
    }

    public Like createLike(Like like) {
        // 1. 유효한 멤버인지 체크
        memberService.verifyExistsEmail(like.getMember().getEmail());

        // 2. 유효한 게시물인지 체크
        questionBoardService.verifyExistsQuestionBoard(like.getQuestionBoard().getQuestionBoardId());

        // 3. 이미 좋아요를 누른 게시판인지 체크
        verifyExistedLike(like);

        return likeRepository.save(like);
    }

    // 3. 이미 좋아요를 누른 게시판인지 체크
    private void verifyExistedLike(Like like) {
        List<Like> likes = likeRepository.findAll();

        for (Like compareLike : likes) {
            if (compareLike.getQuestionBoard().getQuestionBoardId().equals(like.getQuestionBoard().getQuestionBoardId()) && compareLike.getMember().getMemberId().equals(like.getMember().getMemberId())) {
                throw new BusinessLogicException(ExceptionCode.LIKE_EXISTS);
            }
        }
    }
}
