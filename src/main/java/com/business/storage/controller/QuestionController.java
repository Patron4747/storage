package com.business.storage.controller;

import com.business.storage.model.Answer;
import com.business.storage.model.Question;
import com.business.storage.repository.AnswerRepository;
import com.business.storage.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @PostMapping("/saveQuestion")
    public ResponseEntity<Question> saveQuestion(@RequestBody Question question) {
        Question saveQuestion = questionRepository.save(question);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saveQuestion.getId())
                .toUri();

        return ResponseEntity.created(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(saveQuestion);

    }

    @GetMapping("/getAllQuestions")
    public ResponseEntity<?> getAllQuestions() {
        List<Question> allQuestions = questionRepository.findAll();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(allQuestions);
    }

    @GetMapping("/getQuestion")
    public ResponseEntity<?> getQuestion(@RequestParam String id) {
        Question question = questionRepository.findById(Long.parseLong(id)).orElse(null);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(question);
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
