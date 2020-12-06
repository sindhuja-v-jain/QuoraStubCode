package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * Controller method to handle createQuestion POST endpoint
     *
     * @param questionRequest
     * @param authorization
     * @return QuestionResponse
     * @throws AuthorizationFailedException
     */
    @RequestMapping(
            method = RequestMethod.POST,
            path = "/question/create",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(
            QuestionRequest questionRequest, @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {
        // Create new Question Entity
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setDate(ZonedDateTime.now());

        // Authorize the user who is trying to create question and create question
        final QuestionEntity createdQuestion =
                questionService.createQuestion(authorization, questionEntity);

        // Create QuestionResponse and return it to user
        QuestionResponse questionResponse =
                new QuestionResponse().id(questionEntity.getUuid()).status("QUESTION CREATED");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.OK);
    }
}