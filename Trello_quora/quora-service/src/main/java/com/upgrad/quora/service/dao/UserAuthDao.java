package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


@Repository
public class UserAuthDao {

    @PersistenceContext
    private EntityManager entityManager;
    /**
     * get User auth by token
     *
     * @param accessToken : access token to authenticate
     * @return single user auth details
     */
    public UserAuthEntity getUserAuthByToken(final String accessToken) {
        try {
            return entityManager
                    .createNamedQuery("userAuthByAccessToken", UserAuthEntity.class)
                    .setParameter("accessToken", accessToken)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    /**
     * Persists user authen entity in database.
     *
     * @param userAuthEntity to be persisted in the DB.
     * @return UserAuthEntity
     */
    public UserAuthEntity createAuthToken(final UserAuthEntity userAuthEntity) {
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }
    /**
     * Update UserAuthEntity in Database
     *
     * @param updatedUserAuthEntity: UserAuthEntity object
     */
    public void updateUserAuth(final UserAuthEntity updatedUserAuthEntity) {
        entityManager.merge(updatedUserAuthEntity);
    }
}

