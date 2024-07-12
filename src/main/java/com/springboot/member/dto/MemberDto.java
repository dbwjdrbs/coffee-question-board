package com.springboot.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class MemberDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotBlank
        @Email
        private String email;

        @NotBlank(message = "공백 금지")
        private String name;

        @NotBlank
        private String password;

        @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$", message = "올바른 휴대번호 패턴이 아님")
        private String phone;
    }
}
