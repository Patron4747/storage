package com.business.storage.repository;

import com.business.storage.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: mmustafin@context-it.ru
 * @created: 13.05.2020
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
