package com.example.quiz13.vo;

import java.util.List;

public class QuesAnswerVO  extends QuesIdAnswerVo{
	  private String quesName ;

	public QuesAnswerVO() {
		super();
	}

	public QuesAnswerVO(int quesId,String quesName, List<String> answers) {
		super(quesId, answers);
		this.quesName = quesName;
	}

	

	public String getQuesName() {
		return quesName;
	}

	public void setQuesName(String quesName) {
		this.quesName = quesName;
	} 
	  
}
