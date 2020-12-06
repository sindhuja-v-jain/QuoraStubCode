package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Persists a new Question entity in the Database
     *
     * @param questionEntity
     * @return QuestionEntity created
     */
    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    /**
     * Fetches a list of Question entities from the Database
     *
     * @return List of all questions
     */
    public List<QuestionEntity> getAllQuestions() {

        List<QuestionEntity> questionsList =
                entityManager.createNamedQuery("getAllQuestions", QuestionEntity.class).getResultList();
        return questionsList;
    }

    /**
     * Fetch Question by question Uuid
     *
     * @param questionUuid
     * @return question by Uuid
     */
    public QuestionEntity getQuestionByUuid(String questionUuid) {
        try {
            return entityManager
                    .createNamedQuery("getQuestionByUuid", QuestionEntity.class)
                    .setParameter("questionId", questionUuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * persist the edited Question in Database
     *
     * @param questionEntity
     * @return
     */
    public QuestionEntity editQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    /**
     * delete a question from Database
     *
     * @param questionEntity
     */
    public void deleteQuestion(QuestionEntity questionEntity) {

        entityManager.remove(questionEntity);
    }

}