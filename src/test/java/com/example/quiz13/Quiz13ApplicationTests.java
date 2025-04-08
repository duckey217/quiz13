package com.example.quiz13;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.quiz13.dao.QuizDao;
import com.example.quiz13.entity.Question;
import com.example.quiz13.service.ifs.QuizService;
import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.CreateReq;
import com.example.quiz13.vo.SearchReq;
import com.example.quiz13.vo.SearchRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class Quiz13ApplicationTests {

	@Autowired
	private QuizDao quizDao;
	private QuizService quizService;

	
	@Test
	void contextLoadS() {
		int res = quizDao.selectMaxQuizId();
		System.out.println(res);
	}
	
	@Test
	public void insert() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String str = mapper.writeValueAsString(List.of("AAA","BBB","CCC"));
		List<Question>list = new ArrayList<>();
		list.add(new Question(1,1,"A","single",true,str));
		LocalDate date = LocalDate.of(2025,3,27);
		LocalDate endDate = LocalDate.of(2025, 3, 28);
		CreateReq req = new CreateReq("AA","AAA",date,endDate,true,list);
		BasicRes res = quizService.create(req);
		System.out.println(res.getCode()+res.getMessage());
	}
	
	@Test
	public void searchTest() {
		SearchReq req = new SearchReq(null,null,null);
		SearchRes res = quizService.getAll(req);
		System.out.println(res.getQuizList().size());
	}
	
	@Test
	public void listTest(){
		List<String> list = new ArrayList<>();
		List<String> strList = List.of("1:AAA","2:BBB");
		for(String item : strList) {
			String[] strArray = item.split(":");
			list.addAll(List.of(strArray));
		}
	}
}
