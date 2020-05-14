package com.business.storage.controller;

import com.business.storage.model.Answer;
import com.business.storage.model.Question;
import com.business.storage.repository.AnswerRepository;
import com.business.storage.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: mmustafin@context-it.ru
 * @created: 13.05.2020
 */
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @GetMapping("/getAllQuestions")
    public ResponseEntity<?> getAllQuestions() {
        List<Question> allQuestions = questionRepository.findAll();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(allQuestions);
    }

    @GetMapping(path = "/{questionId}/getAnswersTree")
    public ResponseEntity<?> getAllAnswers(@PathVariable("questionId") String questionId) {
        Long questId = Long.parseLong(questionId);
        List<Answer> allAnswers = answerRepository.findAllByQuestionId(questId);

        List<Answer> result = new ArrayList<>();

        allAnswers.forEach(currentAnswer -> {
            if (currentAnswer.getParentId() == 0) {
                result.add(currentAnswer);
            } else {
                allAnswers.stream()
                        .filter(answer -> answer.getId() == currentAnswer.getParentId())
                        .findAny().ifPresent(parent -> parent.getAnswers().add(currentAnswer));
            }
        });

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }
}
