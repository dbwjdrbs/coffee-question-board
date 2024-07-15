package com.springboot.questionboard.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.like.entity.Like;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import com.springboot.member.service.MemberService;
import com.springboot.questionboard.entity.QuestionBoard;
import com.springboot.questionboard.repository.QuestionBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class QuestionBoardService {
    private final QuestionBoardRepository questionBoardRepository;
    private final MemberService memberService;

    public QuestionBoardService(QuestionBoardRepository questionBoardRepository, MemberService memberService) {
        this.questionBoardRepository = questionBoardRepository;
        this.memberService = memberService;
    }

    // ******************************************************* Create *******************************************************
    public QuestionBoard createQuestionBoard(QuestionBoard questionBoard, Authentication authentication) {
        // 1) member 조회
        String username = String.valueOf(authentication.getPrincipal());
        Member member = memberService.verifyExistsMember(username);

        // 2) 제약 조건 1: 게시물 Secret인지?
        isSecretPosting(member, questionBoard);

        // 3) 제약 조건 2: 삭제된 게시물 조회 불가
        isDeletedPosting(questionBoard);

        questionBoard.setMember(member);
        questionBoardRepository.save(questionBoard);

        return questionBoard;
    }

    // ******************************************************* Update *******************************************************
    public QuestionBoard updateLikeCount(Like like) {
        QuestionBoard findQuestionBoard = findQuestionBoard(like.getQuestionBoard().getQuestionBoardId());
        findQuestionBoard.addLikeCount();

        questionBoardRepository.save(findQuestionBoard);
        return findQuestionBoard;
    }

    // ******************************************************* Find *******************************************************
    public QuestionBoard findQuestionBoard(long questionBoardId) {
        QuestionBoard questionBoard = verifyExistsQuestionBoard(questionBoardId);
        alreadyDeletedBoard(questionBoard);
        questionBoard.addViewCount();
        return questionBoard;
    }

    // ******************************************************* Finds *******************************************************
    public Page<QuestionBoard> findQuestionBoards(int page, int size, String criteria) {
        if (criteria.equals("oldPost")) {
            return questionBoardRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "questionBoardId")));
        } else if (criteria.equals("viewCount")) {
            return questionBoardRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "viewCount")));
        } else if (criteria.equals("likeCount")) {
            return questionBoardRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount")));
        }

        return questionBoardRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "questionBoardId")));
    }

    // ******************************************************* Delete *******************************************************
    public void deleteQuestionBoard(long questionBoardId, Authentication authentication) {
        String username = String.valueOf(authentication.getPrincipal());
        // 1) 존재하는 멤버 인지 체크하는김에 맴버정보도 가져오기
        Member member = memberService.verifyExistsMember(username);

        // 2) 게시물이 존재하는지
        QuestionBoard questionBoard = verifyExistsQuestionBoard(questionBoardId);

        // 3) 이미 삭제된 게시물
        alreadyDeletedBoard(questionBoard);

        // 4) 이 게시물의 주인 인지 체크
        verifyPostingOwner(member.getMemberId(), questionBoard);

        questionBoard.setQuestionStatus(QuestionBoard.QuestionStatus.QUESTION_DELETED);
    }

    // ******************************************************* 제약 조건 메서드 *******************************************************

    public QuestionBoard verifyExistsQuestionBoard(long questionBoardId) {
        Optional<QuestionBoard> questionBoard = questionBoardRepository.findById(questionBoardId);
        // null 이면 예외
        return questionBoard.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
    }

    private void alreadyDeletedBoard(QuestionBoard questionBoard) {
        if (questionBoard.getQuestionStatus() == QuestionBoard.QuestionStatus.QUESTION_DELETED) {
            throw new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND);
        }
    }

    private void isSecretPosting(Member member, QuestionBoard questionBoard) {
        if (questionBoard.getReadPermission() != QuestionBoard.ReadPermission.PUBLIC) {
            if (!questionBoard.getMember().getMemberId().equals(member.getMemberId())) {
                throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_USER);
            }
        }
    }

    private void isDeletedPosting(QuestionBoard questionBoard) {
        if(questionBoard.getQuestionStatus().equals(QuestionBoard.QuestionStatus.QUESTION_DELETED)) {
            throw new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND);
        }
    }

    private void verifyPostingOwner(long memberId, QuestionBoard questionBoard) {
        long findMemberId = questionBoard.getMember().getMemberId();

        if (memberId != findMemberId) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED_USER);
        }
    }
}
