package com.springboot.questionboard.entity;

import com.springboot.audit.Auditable;
import com.springboot.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class QuestionBoard extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionBoardId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int viewCount = 0;

    public void addViewCount() {
        this.viewCount += 1;
    }

    @Column(nullable = false)
    private int likeCount = 0;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private QuestionStatus questionStatus = QuestionStatus.QUESTION_REGISTERED;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ReadPermission readPermission;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void addLikeCount() {
        this.likeCount += 1;
    }

    public enum QuestionStatus {
        QUESTION_REGISTERED("질문 등록 상태"),
        QUESTION_ANSWERED("답변 완료 상태"),
        QUESTION_DELETED("질문 삭제 상태"),
        // 회원 탈퇴 시, 질문 비활성화 상태
        QUESTION_DEACTIVED("질문 비활성화 상태");

        @Getter
        private String status;

        QuestionStatus(String status) {
            this.status = status;
        }
    }

    public enum ReadPermission {
        PUBLIC("공개글"),
        SECRET("비공개글");

        @Getter
        private String status;

        ReadPermission(String status) {
            this.status = status;
        }
    }
}

