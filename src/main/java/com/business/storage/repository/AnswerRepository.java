package com.business.storage.repository;

import com.business.storage.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: mmustafin@context-it.ru
 * @created: 13.05.2020
 */
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    public List<Answer> findAllByQuestionId(Long questionId);
}
