package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class UserProfileService {

    @Autowired
    private UserDao userDao;

    @Autowired private UserAuthDao userAuthDao;

    /**
     * Retrieving the UserEntity based on userId
     *
     * @param userUuid
     * @return UserEntity
     * @throws UserNotFoundException
     */
    public UserEntity getUserByUuid(final String userUuid) throws UserNotFoundException {
        UserEntity userEntity = userDao.getUserById(userUuid);
        if (userEntity == null) {
            // handle exception when null object is returned from userDao
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        } else {
            return userEntity;
        }
    }

    /**
     * Authorize the user who is trying to fetch a user's profile details
     *
     * @param authorization
     * @return UserAuthEntity
     * @throws AuthorizationFailedException
     */
    public UserAuthEntity authorizeUser(final String authorization)
            throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByToken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else {
            // Retrieve logout_at attribute value of UserAuthEntity to check if user has already signed
            // out
            ZonedDateTime logoutAt = userAuthEntity.getLogoutAt();
            if (logoutAt != null) {
                throw new AuthorizationFailedException(
                        "ATHR-002", "User is signed out.Sign in first to get user details");
            } else {
                return userAuthEntity;
            }
        }
    }
}
