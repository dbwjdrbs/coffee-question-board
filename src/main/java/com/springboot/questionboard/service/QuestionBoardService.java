package com.springboot.questionboard.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.like.entity.Like;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import com.springboot.questionboard.entity.QuestionBoard;
import com.springboot.questionboard.repository.QuestionBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class QuestionBoardService {
    private final QuestionBoardRepository questionBoardRepository;
    private final MemberRepository memberRepository;

    public QuestionBoardService(QuestionBoardRepository questionBoardRepository, MemberRepository memberRepository) {
        this.questionBoardRepository = questionBoardRepository;
        this.memberRepository = memberRepository;
    }

    public QuestionBoard createQuestionBoard(QuestionBoard questionBoard) {
        Member member = memberRepository.findById(1L).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        questionBoard.setMember(member);
        questionBoardRepository.save(questionBoard);

        return questionBoard;
    }

    public QuestionBoard updateLikeCount(Like like) {
        QuestionBoard findQuestionBoard = findQuestionBoard(like.getQuestionBoard().getQuestionBoardId());
        findQuestionBoard.addLikeCount();

        questionBoardRepository.save(findQuestionBoard);
        return findQuestionBoard;
    }

    public QuestionBoard findQuestionBoard(long questionBoardId) {
        QuestionBoard questionBoard = verifyExistsQuestionBoard(questionBoardId);
        alreadyDeletedBoard(questionBoard);
        questionBoard.addViewCount();
        return questionBoard;
    }

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

    public void deleteQuestionBoard(long questionBoardId) {
        memberRepository.findById(1L).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        QuestionBoard questionBoard = verifyExistsQuestionBoard(questionBoardId);
        questionBoard.setQuestionStatus(QuestionBoard.QuestionStatus.QUESTION_DELETED);
    }

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
}
