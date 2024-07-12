package com.springboot.like.dto;

import com.springboot.member.entity.Member;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

public class LikeDto {
    @Getter
    public static class Post {
        @Positive
        private long memberId;

        @Positive
        private long questionBoardId;

        public Member getMember() {
            Member member = new Member();
            member.setMemberId(memberId);
            return member;
        }
    }
}
