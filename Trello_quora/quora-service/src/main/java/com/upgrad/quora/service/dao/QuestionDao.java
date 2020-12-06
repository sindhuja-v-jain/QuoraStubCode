package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
}