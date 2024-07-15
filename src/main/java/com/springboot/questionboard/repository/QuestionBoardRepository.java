package com.springboot.questionboard.repository;

import com.springboot.questionboard.entity.QuestionBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionBoardRepository extends JpaRepository<QuestionBoard, Long> {
//    @Query("SELECT q FROM QUESTION_BOARD q WHERE q.QUESTION_STATUS != 'QUESTION_DELETED'")
//    Page<QuestionBoard> findAllActiveBoard(PageRequest pageRequest);
}
