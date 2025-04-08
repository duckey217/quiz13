package com.example.quiz13.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz13.service.ifs.QuizService;
import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.CreateReq;
import com.example.quiz13.vo.DeleteReq;
import com.example.quiz13.vo.GetQuestionsRes;
import com.example.quiz13.vo.SearchReq;
import com.example.quiz13.vo.SearchRes;
import com.example.quiz13.vo.UpdateReq;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quiz")
public class QuizServiceController {

	
	
		@Autowired
		private QuizService quizService;
		
		@PostMapping("/create")
		public BasicRes create(@Valid @RequestBody CreateReq req) {
			return quizService.create(req);
			
		}
		
		@GetMapping(value = "/get_all")
		public SearchRes getAll() {
			return quizService.getAll();
		}
		

		@PostMapping("/get_all")
		public SearchRes getAll(@RequestBody SearchReq req) {
			return quizService.getAll(req);
		}
		
		
		
		
		
		// 呼叫此api的路徑 : localhost:8080quiz/get_by_quiz_id?quizId=編號
		@GetMapping("/get_by_quiz_id")
		public GetQuestionsRes getQuestionsByQuizId(@RequestParam(value = "quizId")int quizId) {
			
			return quizService.getQuestionsByQuizId(quizId);
		}
		
		
		@PostMapping( value  = "/update")
		public BasicRes update( @Valid  @RequestBody UpdateReq req) {
			return quizService.update(req);
		}
		
		
		@PostMapping(value = "/delete")
		public BasicRes delete( @Valid  @RequestBody DeleteReq req){
			return quizService.delete(req);
		}
		
}


