package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetch a single user by id
     *
     * @param userId : to retch details
     * @return User details
     */
    public UserEntity getUserById(final String userId) {
        try {
            return entityManager
                    .createNamedQuery("userByUserId", UserEntity.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * create user in database.
     *
     * @param userEntity : the userEntity body
     * @return User details
     */
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager
                    .createNamedQuery("userByEmail", UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * Method to get user by name
     *
     * @param userName : username for which to be pulled
     * @return user details
     */
    public UserEntity getUserByUserName(final String userName) {
        try {
            return entityManager
                    .createNamedQuery("userByUserName", UserEntity.class)
                    .setParameter("userName", userName)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


    /**
     * Method to update user in db
     *
     * @param updatedUserEntity : UserEntity body
     * @return updated response
     */
    public void updateUserEntity(final UserEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }

}
