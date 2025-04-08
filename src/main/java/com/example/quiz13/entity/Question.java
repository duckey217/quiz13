package com.example.quiz13.entity;

import com.example.quiz13.constants.ResMessages;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="question")
@IdClass(value=ComplexQuestion.class)
public class Question {
	
		@Min(value = 1,message = ResMessages.ConstantsMessage.PARAM_QUES_ID_ERROR)
		@Id
		@Column(name ="ques_id")
		private int quesid;
		
		
		@Id
		@Column(name ="quiz_id")		
		private int quizid;
		
		@NotBlank(message = ResMessages.ConstantsMessage.PARAM_QUES_NAME_ERROR)
		@Column(name ="name")		
		private String name ;

		@NotBlank(message = ResMessages.ConstantsMessage.PARAM_QUES_TYPE_ERROR)
		@Column(name ="type")		
		private String type;

		



		@Column(name ="is_must")		
		private boolean must;
		
		// 不用檢查，因為當question type是簡答時，沒有選項
		@Column(name ="options")		
		private String options;

		public Question() {
			super();
		}

		public Question(int quizid,int quesid,  String name, String type, boolean ismust , String options) {
			super();
			this.quesid = quesid;
			this.quizid = quizid;
			this.name = name;
			this.type = type;
			this.must = must;
			this.options = options;
		}

		public int getQuesid() {
			return quesid;
		}

		public int getQuizid() {
			return quizid;
		}

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}
		
		
		public boolean isMust() {
			return must;
		}

		
		
		public String getOptions() {
			return options;
		}

		public void setQuizid(int quizid) {
			this.quizid = quizid;
		}
		
		
		
}
