package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Controller method to handle PUT request to update question
     *
     * @param questionEditRequest
     * @param questionUuid
     * @param authorization
     * @return QuestionEditResponse
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "/question/edit/{questionId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent(
            QuestionEditRequest questionEditRequest,
            @PathVariable("questionId") final String questionUuid,
            @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, InvalidQuestionException {
        // Set the user typed content as the new content of the Question entity
        String content = questionEditRequest.getContent();

        // Authorize user and edit the question with Uuid passed
        QuestionEntity questionEntity =
                questionService.editQuestionContent(authorization, questionUuid, content);

        // Set the Uuid and status of edited question in response
        QuestionEditResponse questionEditResponse =
                new QuestionEditResponse().id(questionEntity.getUuid()).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
    }

    /**
     * Controller method to handle DELETE request to delete question
     *
     * @param accessToken
     * @param questionId
     * @return
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}")
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(
            @RequestHeader("authorization") final String accessToken,
            @PathVariable("questionId") final String questionId)
            throws AuthorizationFailedException, InvalidQuestionException {

        QuestionEntity questionEntity = questionService.deleteQuestion(accessToken, questionId);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse();
        questionDeleteResponse.setId(questionEntity.getUuid());
        questionDeleteResponse.setStatus("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
    }

    /**
     * Controller method to get all questions posted by a user
     *
     * @param accessToken
     * @param userId
     * @return
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */
    @RequestMapping(
            method = RequestMethod.GET,
            path = "question/all/{userId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getQuestionByUserId(
            @RequestHeader("authorization") final String accessToken,
            @PathVariable("userId") String userId)
            throws AuthorizationFailedException, UserNotFoundException {
        List<QuestionEntity> questions = questionService.getAllQuestionsByUser(userId, accessToken);
        List<QuestionDetailsResponse> questionDetailResponses = new ArrayList<>();
        for (QuestionEntity questionEntity : questions) {
            QuestionDetailsResponse questionDetailResponse = new QuestionDetailsResponse();
            questionDetailResponse.setId(questionEntity.getUuid());
            questionDetailResponse.setContent(questionEntity.getContent());
            questionDetailResponses.add(questionDetailResponse);
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(
                questionDetailResponses, HttpStatus.OK);
    }
}