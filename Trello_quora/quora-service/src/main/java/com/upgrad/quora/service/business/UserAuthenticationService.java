package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserAuthenticationService {

    @Autowired private UserDao userDao;

    @Autowired private PasswordCryptographyProvider passwordCryptographyProvider;
    /**
     * check if user is already created add salt and encryption to password
     *
     * @throws SignUpRestrictedException : throw exception if user already exists
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {
        if (isUserNameInUse(userEntity.getUserName())) {
            throw new SignUpRestrictedException(
                    "SGR-001", "Try any other Username, this Username has already been taken");
        }

        if (isEmailInUse(userEntity.getEmail())) {
            throw new SignUpRestrictedException(
                    "SGR-002", "This user has already been registered, try with any other emailId");
        }
        // Assign a UUID to the user that is being created.
        userEntity.setUuid(UUID.randomUUID().toString());
        // Assign encrypted password and salt to the user that is being created.
        String[] encryptedText = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);
        return userDao.createUser(userEntity);
    }

    // checks whether the username exist in the database
    private boolean isUserNameInUse(final String userName) {
        return userDao.getUserByUserName(userName) != null;
    }

    // checks whether the email exist in the database
    private boolean isEmailInUse(final String email) {
        return userDao.getUserByEmail(email) != null;
    }
}
