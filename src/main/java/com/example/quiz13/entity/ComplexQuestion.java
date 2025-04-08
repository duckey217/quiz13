package com.example.quiz13.entity;

import java.io.Serializable;


public class ComplexQuestion implements Serializable {
	
	private int quesid;
	
	private int quizid;

	public int getQuesid() {
		return quesid;
	}

	public void setQuesid(int quesid) {
		this.quesid = quesid;
	}

	public int getQuizid() {
		return quizid;
	}

	public void setQuizid(int quizid) {
		this.quizid = quizid;
	}

}
