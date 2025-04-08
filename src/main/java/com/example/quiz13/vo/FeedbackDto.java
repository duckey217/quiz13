package com.example.quiz13.vo;

import java.time.LocalDate;

// DTO  data transfer object
/// dto 命名為dto的原因是這些資料主要是從資料庫來，且無法只用一個entity裝載，一般是用join取多張表的欄位資料
/// vo 也可以命名為vo value object 用來裝資料容器的統稱
///  entity 也是裝資料的型態 但entity 可以mapping 到資料表及其資料欄位
public class FeedbackDto {

	private int quizId;

	private String quizName;

	private String description;

	private String userName;
	private String phone;
	private String email;
	private int age;
	
	private int quesId;
	
	private String quesName ; 
	
	private String answer ;
	
	private LocalDate fillinDate ;

	public FeedbackDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FeedbackDto(int quizId, String quizName, String description, String userName, String phone, String email,
			int age, int quesId, String quesName, String answer, LocalDate fillinDate) {
		super();
		this.quizId = quizId;
		this.quizName = quizName;
		this.description = description;
		this.userName = userName;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.quesId = quesId;
		this.quesName = quesName;
		this.answer = answer;
		this.fillinDate = fillinDate;
	}

	public int getQuizId() {
		return quizId;
	}

	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}

	public String getQuizName() {
		return quizName;
	}

	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getQuesId() {
		return quesId;
	}

	public void setQuesId(int quesId) {
		this.quesId = quesId;
	}

	public String getQuesName() {
		return quesName;
	}

	public void setQuesName(String quesName) {
		this.quesName = quesName;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public LocalDate getFillinDate() {
		return fillinDate;
	}

	public void setFillinDate(LocalDate fillinDate) {
		this.fillinDate = fillinDate;
	}
	
	
	
}
