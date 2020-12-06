package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDetailsResponse;
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
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Controller method to handle GET request to fetch all questions
     *
     * @param authorization
     * @return List of all questions
     * @throws AuthorizationFailedException
     */
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/question/all",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(
            @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {
        // Authorize the user by passing in access-token of the user and Fetch list of all questions
        List<QuestionEntity> allQuestions = questionService.getAllQuestions(authorization);

        // List to add QuestionResponse entities
        final List<QuestionDetailsResponse> questionResponseList = new ArrayList<>();

        // Extract Uuid and content from each QuestionResponse entity
        for (QuestionEntity question : allQuestions) {
            String uuid = question.getUuid();
            String content = question.getContent();
            questionResponseList.add(new QuestionDetailsResponse().id(uuid).content(content));
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionResponseList, HttpStatus.OK);
    }
}