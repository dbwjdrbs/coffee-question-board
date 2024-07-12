package com.springboot.like.mapper;

import com.springboot.like.dto.LikeDto;
import com.springboot.like.entity.Like;
import com.springboot.member.entity.Member;
import com.springboot.questionboard.entity.QuestionBoard;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    // likePost를 하려면 LikeQuestion도 같이 매핑해줘야함.
    default Like likePostToLike(LikeDto.Post requestBody) {
        Like like = new Like();
        Member member = new Member();
        QuestionBoard questionBoard = new QuestionBoard();

        member.setMemberId(requestBody.getMember().getMemberId());
        questionBoard.setQuestionBoardId(requestBody.getQuestionBoardId());

        // member 값을 넣어줌.
        like.setMember(member);
        like.setQuestionBoard(questionBoard);

        return like;
    }
}
