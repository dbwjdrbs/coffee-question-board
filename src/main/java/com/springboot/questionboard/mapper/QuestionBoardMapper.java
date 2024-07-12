package com.springboot.questionboard.mapper;

import com.springboot.member.entity.Member;
import com.springboot.questionboard.dto.QuestionBoardDto;
import com.springboot.questionboard.entity.QuestionBoard;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionBoardMapper {
    QuestionBoard questionBoardPostToQuestionBoard(QuestionBoardDto.Post requestBody);
    default QuestionBoardDto.Response questionBoardToQuestionBoardResponse(QuestionBoard questionBoard) {
        if ( questionBoard == null ) {
            return null;
        }

        long questionBoardId = 0L;
        String title = null;
        String content = null;
        int viewCount = 0;
        int likeCount = 0;
        QuestionBoard.QuestionStatus questionStatus = null;
        QuestionBoard.ReadPermission readPermission = null;

        if ( questionBoard.getQuestionBoardId() != null ) {
            questionBoardId = questionBoard.getQuestionBoardId();
        }
        title = questionBoard.getTitle();
        content = questionBoard.getContent();
        questionStatus = questionBoard.getQuestionStatus();
        readPermission = questionBoard.getReadPermission();
        viewCount = questionBoard.getViewCount();
        likeCount = questionBoard.getLikeCount();

        long memberId = questionBoard.getMember().getMemberId();

        QuestionBoardDto.Response response = new QuestionBoardDto.Response( questionBoardId, title, content, viewCount, likeCount, questionStatus, readPermission, memberId);

        return response;
    }
    List<QuestionBoardDto.Response> questionBoardToQuestionBoardResponses(List<QuestionBoard> questionBoards);
}
