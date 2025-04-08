package com.example.quiz13.vo;

import java.util.List;

import com.example.quiz13.constants.ResMessages;

import jakarta.validation.constraints.NotEmpty;

public class DeleteReq {
	@NotEmpty(message = ResMessages.ConstantsMessage.PARAM_QUIZ_ID_ERROR)	
	private List<Integer> quizIdList;

	
	public List<Integer> getQuizIdList() {
		return quizIdList;
	}

	public void setQuizIdList(List<Integer> quizIdList) {
		this.quizIdList = quizIdList;
	}
	
}
