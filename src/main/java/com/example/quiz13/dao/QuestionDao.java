package com.example.quiz13.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.quiz13.entity.ComplexQuestion;
import com.example.quiz13.entity.Question;
import com.example.quiz13.entity.Quiz;

import jakarta.transaction.Transactional;
@Repository
public interface QuestionDao extends JpaRepository<Quiz, ComplexQuestion>{
		
	// SQL語法中:name 對應的變數名稱是@Param("name")括號中的字串
		@Transactional	
		@Modifying
		@Query(value ="insert into question ( quiz_id,ques_id ,name , type , is_must , options ) "
				+  "values (:quizId,:quesId,:quesName,:type,:must ,:options) " ,nativeQuery = true)
		public void insert(//
				
				@Param("quizId")int quizId , //
				@Param("quesId")int quesId , //
				@Param("quesName")String quesName , //

				@Param("type")String type , //
				@Param("must")boolean must,//
				@Param("options")String options );//

			@Query(value ="select * from question where quiz_id = ?1", nativeQuery = true)
			public List<Question> getByQuizId(int quizId);
			
			@Transactional	
			@Modifying
			@Query(value ="delete from question where quiz_id = ?1", nativeQuery = true)
			public void deleteByQuizId(int quizId);
			
			
			@Transactional	
			@Modifying
			@Query (value = "  delete from question where quiz_id  in (?1)  " ,nativeQuery = true)
			public void delete(List<Integer> quizIdList) ;
	
}
