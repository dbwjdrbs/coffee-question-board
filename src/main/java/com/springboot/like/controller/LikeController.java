package com.springboot.like.controller;

import com.springboot.like.dto.LikeDto;
import com.springboot.like.entity.Like;
import com.springboot.like.mapper.LikeMapper;
import com.springboot.like.service.LikeService;
import com.springboot.member.service.MemberService;
import com.springboot.questionboard.service.QuestionBoardService;
import com.springboot.utils.UriCreator;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/v11/likes")
@Validated
public class LikeController {
    private final static String LIKE_DEFAULT_URL = "/v11/likes";
    private final LikeService likeService;
    private final LikeMapper mapper;
    private final QuestionBoardService questionBoardService;

    public LikeController(LikeService likeService, LikeMapper mapper, QuestionBoardService questionBoardService) {
        this.likeService = likeService;
        this.mapper = mapper;
        this.questionBoardService = questionBoardService;
    }

    @PostMapping
    public ResponseEntity postLike(@Valid @RequestBody LikeDto.Post requestBody) {
        // mapper에서 like 객체 생성한 후, Service에서 DB 저장 및 불러오기.
        Like like = likeService.createLike(mapper.likePostToLike(requestBody));

        // QuestionBoard count up
        questionBoardService.updateLikeCount(like);

        URI location = UriCreator.createUri(LIKE_DEFAULT_URL, like.getLikeId());

        return ResponseEntity.created(location).build();
    }
}
