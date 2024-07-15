package com.springboot.questionboard.mapper;

import com.springboot.questionboard.dto.QuestionBoardDto;
import com.springboot.questionboard.entity.QuestionBoard;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-12T16:36:17+0900",
    comments = "version: 1.5.1.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 11.0.22 (Azul Systems, Inc.)"
)
@Component
public class QuestionBoardMapperImpl implements QuestionBoardMapper {

    @Override
    public QuestionBoard questionBoardPostToQuestionBoard(QuestionBoardDto.Post requestBody) {
        if ( requestBody == null ) {
            return null;
        }

        QuestionBoard questionBoard = new QuestionBoard();

        questionBoard.setTitle( requestBody.getTitle() );
        questionBoard.setContent( requestBody.getContent() );
        questionBoard.setReadPermission( requestBody.getReadPermission() );

        return questionBoard;
    }

    @Override
    public List<QuestionBoardDto.Response> questionBoardToQuestionBoardResponses(List<QuestionBoard> questionBoards) {
        if ( questionBoards == null ) {
            return null;
        }

        List<QuestionBoardDto.Response> list = new ArrayList<QuestionBoardDto.Response>( questionBoards.size() );
        for ( QuestionBoard questionBoard : questionBoards ) {
            list.add( questionBoardToQuestionBoardResponse( questionBoard ) );
        }

        return list;
    }
}
