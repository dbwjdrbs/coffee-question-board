package com.springboot.questionboard.controller;

import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.questionboard.dto.QuestionBoardDto;
import com.springboot.questionboard.entity.QuestionBoard;
import com.springboot.questionboard.mapper.QuestionBoardMapper;
import com.springboot.questionboard.service.QuestionBoardService;
import com.springboot.utils.UriCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v11/questionboard")
@Validated
@Slf4j
public class QuestionBoardController {
    private final static String QUESTION_BOARD_URL = "/v11/questionboard";
    private final QuestionBoardMapper mapper;
    private final QuestionBoardService questionBoardService;

    public QuestionBoardController(QuestionBoardMapper mapper, QuestionBoardService questionBoardService) {
        this.mapper = mapper;
        this.questionBoardService = questionBoardService;
    }

    @PostMapping
    public ResponseEntity postQuestionBoard(@Valid @RequestBody QuestionBoardDto.Post requestBody,
                                            Authentication authentication) {
        QuestionBoard questionBoard = mapper.questionBoardPostToQuestionBoard(requestBody);
        QuestionBoard createdQuestionBoard = questionBoardService.createQuestionBoard(questionBoard, authentication);
        URI location = UriCreator.createUri(QUESTION_BOARD_URL, createdQuestionBoard.getQuestionBoardId());
        log.info("=".repeat(60));
        log.info("게시물 생성 완료 : " + createdQuestionBoard);
        log.info("=".repeat(60));
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{questionBoard-id}")
    public ResponseEntity getQuestionBoard(@PathVariable("questionBoard-id") @Positive long questionBoardId) {
        QuestionBoard questionBoard = questionBoardService.findQuestionBoard(questionBoardId);

        log.info("=".repeat(60));
        log.info("단일 게시물 조회 : " + questionBoard);
        log.info("=".repeat(60));

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.questionBoardToQuestionBoardResponse(questionBoard)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getQuestionBoards(@Positive @RequestParam int page,
                                            @Positive @RequestParam int size,
                                            @RequestParam(defaultValue = "questionBoardId") String criteria) {
        Page<QuestionBoard> pageQuestionBoards = questionBoardService.findQuestionBoards(page - 1, size, criteria);
        List<QuestionBoard> questionBoards = pageQuestionBoards.getContent();
        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.questionBoardToQuestionBoardResponses(questionBoards),
                        pageQuestionBoards),
                HttpStatus.OK);
    }


    @DeleteMapping("/{questionBoard-id}")
    public void deleteQuestionBoard(@PathVariable("questionBoard-id") @Positive long questionBoardId,
                                    Authentication authentication) {
        questionBoardService.deleteQuestionBoard(questionBoardId, authentication);
    }
}
