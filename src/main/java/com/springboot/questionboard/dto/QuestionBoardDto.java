package com.springboot.questionboard.dto;

import com.springboot.member.entity.Member;
import com.springboot.questionboard.entity.QuestionBoard;
import com.springboot.validator.ValidEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.thymeleaf.spring5.processor.SpringObjectTagProcessor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class QuestionBoardDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotBlank(message = "타이틀을 입력해주세요.")
        @Size(min = 2, max = 20, message = "타이틀은 2글자에서 최대 20글자 까지 허용됩니다.")
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(min = 10, max = 500, message = "내용은 10자에서 최대 500자 까지만 허용됩니다.")
        private String content;

        @ValidEnum(enumClass = QuestionBoard.ReadPermission.class)
        private QuestionBoard.ReadPermission readPermission;
    }

    @AllArgsConstructor
    @Getter
    public static class Response {
        private long questionBoardId;
        private String title;
        private String content;
        private int viewCount;
        private int likeCount;
        private QuestionBoard.QuestionStatus questionStatus;
        private QuestionBoard.ReadPermission readPermission;
        private long memberId;

        public String getQuestionStatus() {
            return questionStatus.getStatus();
        }

        public String getReadPermission() {
            return readPermission.getStatus();
        }
    }
}
