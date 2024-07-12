package com.springboot.like.entity;

import com.springboot.member.entity.Member;
import com.springboot.questionboard.entity.QuestionBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        if (!member.getLikes().contains(this)) {
            member.getLikes().add(this);
        }
    }

    @OneToOne
    @JoinColumn(name = "QUESTION_BOARD_ID")
    private QuestionBoard questionBoard = new QuestionBoard();
}
