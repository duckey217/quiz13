package com.example.quiz13.vo;

import java.time.LocalDate;
import java.util.List;

public class FeedbackVo {
	private String userName;
	private String phone;
	private String email;
	private int age;
	
	private List<QuesAnswerVO> quesAnswerList;
	
	private LocalDate fillinDate ;

	public FeedbackVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public FeedbackVo(String userName, String phone, String email, int age, List<QuesAnswerVO> quesAnswerList,
			LocalDate fillinDate) {
		super();
		this.userName = userName;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.quesAnswerList = quesAnswerList;
		this.fillinDate = fillinDate;
	}



	public String getUserName() {
		return userName;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public int getAge() {
		return age;
	}

	public List<QuesAnswerVO> getQuesAnswerList() {
		return quesAnswerList;
	}



	public LocalDate getFillinDate() {
		return fillinDate;
	}



	public void setFillinDate(LocalDate fillinDate) {
		this.fillinDate = fillinDate;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public void setAge(int age) {
		this.age = age;
	}



	public void setQuesAnswerList(List<QuesAnswerVO> quesAnswerList) {
		this.quesAnswerList = quesAnswerList;
	}
	
	
}
