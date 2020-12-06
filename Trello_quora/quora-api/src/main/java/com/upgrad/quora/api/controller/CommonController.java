package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserProfileService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/")
    public class CommonController {

        @Autowired
        private UserProfileService userProfileService;

        /**
         * Controller method that serves userProfile GET endpoint
         *
         * @param userUuid
         * @param authorization
         * @return User profile of a user
         * @throws AuthorizationFailedException
         * @throws UserNotFoundException
         */
        @RequestMapping(
                method = RequestMethod.GET,
                path = "/userprofile/{userId}",
                produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
        public ResponseEntity<UserDetailsResponse> getUserProfile(
                @PathVariable("userId") final String userUuid,
                @RequestHeader("authorization") final String authorization)
                throws AuthorizationFailedException, UserNotFoundException {

            // Check if user has signed-in or signed-out already by validating the access-token
            UserAuthEntity userAuthEntity = userProfileService.authorizeUser(authorization);

            // Get requested user's details after signed in user is authorized
            UserEntity existingUser = userProfileService.getUserByUuid(userUuid);

            // Creating new UserDetailsResponse object to send user profile details in response
            UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
            userDetailsResponse
                    .firstName(existingUser.getFirstName())
                    .lastName(existingUser.getLastName())
                    .userName(existingUser.getUserName())
                    .emailAddress(existingUser.getEmail())
                    .country(existingUser.getCountry())
                    .aboutMe(existingUser.getAboutMe())
                    .dob(existingUser.getDob())
                    .contactNumber(existingUser.getContactNumber());
            return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
        }
    }

