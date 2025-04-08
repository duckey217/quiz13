package com.example.quiz13.servcie.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quiz13.constants.QuesType;
import com.example.quiz13.constants.ResMessages;
import com.example.quiz13.dao.FeedbackDao;
import com.example.quiz13.dao.QuestionDao;
import com.example.quiz13.dao.QuizDao;
import com.example.quiz13.entity.Question;
import com.example.quiz13.service.ifs.FeedBackService;
import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.FeedbackDto;
import com.example.quiz13.vo.FeedbackRes;
import com.example.quiz13.vo.FeedbackVo;
import com.example.quiz13.vo.FillinReq;
import com.example.quiz13.vo.QuesAnswerVO;
import com.example.quiz13.vo.QuesIdAnswerVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FeedBackServiceImpl implements FeedBackService {
	private ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private FeedbackDao feedbackDao;

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;

	@Override
	public BasicRes fillin(FillinReq req) {
		int quizId = req.getQuizId();


		// 1.檢查quiz
		// 1.1 是否已發布以及當前時間是否介於時間內
		int count = quizDao.selectCountById(req.getQuizId(), LocalDate.now());
		if (count != 1) {
			return new BasicRes(ResMessages.OUT_OF_FILLIN_DATE_RANGE.getCode(), //
					ResMessages.OUT_OF_FILLIN_DATE_RANGE.getMessage());
		}

		// 2. 檢查回饋資料
		// 同一個email 只能填寫同一張問卷一次
		count = feedbackDao.selectCount(quizId, req.getEmail());
		if (count != 0) { // 0表示同一個email沒有填寫過同一張問卷
			return new BasicRes(ResMessages.EMAIL_DUPLICATED.getCode(), //
					ResMessages.EMAIL_DUPLICATED.getMessage());
		}

		// 3.檢查答案
		List<QuesIdAnswerVo> quesIdAnswerList = req.getQuesIdAnswerList();
		// 3.1 從DB撈問題
		List<Question> questionList = questionDao.getByQuizId(quizId);
		// 3.2 比對問題選項跟答案
		for (Question quesItem : questionList) {
			int quesId = quesItem.getQuesid();
			String type = quesItem.getType();
			boolean must = quesItem.isMust();
			List<String> optionsList = new ArrayList<>();
			try {
				if (!type.equalsIgnoreCase(QuesType.TEXT.getType())) {
					optionsList = mapper.readValue(quesItem.getOptions(), new TypeReference<List<String>>() {
					});
					;
				}
			} catch (Exception e) {
				return new BasicRes(ResMessages.OPTIONS_PARSE_ERROR.getCode(), //
						ResMessages.OPTIONS_PARSE_ERROR.getMessage());
			}

			for (QuesIdAnswerVo voItem : quesIdAnswerList) {
				List<String> answerList = new ArrayList<>();
				if (quesId == voItem.getQuesId()) {
					answerList = voItem.getAnswers();
					// 經過上面的if 判斷式 ，answerList 可能會有答案，也可能沒有答案(因為沒有填)
					// 排除1:必填但沒有答案
					if (must && answerList.isEmpty()) {// must 等同於 == true
						return new BasicRes(ResMessages.ANSWER_REQUIRED.getCode(), //
								ResMessages.ANSWER_REQUIRED.getMessage());
					}
					// 跳過type 是text 的因為沒有選項可以轉換
					if (type.equalsIgnoreCase(QuesType.TEXT.getType())) {
						continue;
					}
					// 單選時答案不能有多個
					if (type.equalsIgnoreCase(QuesType.SINGLE.getType()) && answerList.size() > 1) {
						return new BasicRes(ResMessages.ONE_OPTION_IS_ALLOWED.getCode(), //
								ResMessages.ONE_OPTION_IS_ALLOWED.getMessage());
					}
					// 比對同一題的答案是否都在選項中
					boolean checkRes = checkAnswer(optionsList, answerList);
					if (!checkRes) {
						return new BasicRes(ResMessages.ANSWER_OPTION_MISMATCH.getCode(), //
								ResMessages.ANSWER_OPTION_MISMATCH.getMessage());
					}
				}
				
			}
		}
		// 存資料

		for (QuesIdAnswerVo voItem : quesIdAnswerList) {
			// 將List<String>answerList 轉換成String
			try {
				String answerStr = mapper.writeValueAsString(voItem.getAnswers());
				feedbackDao.insert(quizId, voItem.getQuesId(), req.getName(), req.getPhone(), //
						req.getEmail(), req.getAge(), answerStr);
			} catch (JsonProcessingException e) {
				return new BasicRes(ResMessages.ANSWER_OPTION_MISMATCH.getCode(), //
						ResMessages.ANSWER_OPTION_MISMATCH.getMessage());
			} catch (Exception e) {
				throw e;
			}
		}

		return new BasicRes(ResMessages.SUCCESS.getCode(), //
				ResMessages.SUCCESS.getMessage());
	}

	private boolean checkAnswer(List<String> optionsList, List<String> answerList) {
		// 切串接的字串
		List<String> newOptionList = new ArrayList<>();
		for (String item : optionsList) {
			String[] strArray = item.split(":");
			newOptionList.addAll(List.of(strArray));
		}
		// 比對選項跟答案
		for (String answer : answerList) {
			// 假設一種情況，optionList =["A","B","C","D"] , arrayList = ["A',"a"]
			// 若下方的if是沒有驚嘆的判斷式，if(optionsList.contains(answer)),且回傳true
			// 當 answerList 中第一個答案 A 判斷後就會回傳 true，後面的的 a 就會沒判斷到
			if (!optionsList.contains(answer)) {
				return false;
			}
		}
		return true;
	}
	
	
	@Override
	public FeedbackRes feedback(int quizId) {
		// 檢查參數
		if(quizId <=0) {
			return new FeedbackRes(ResMessages.PARAM_QUIZ_ERROR.getCode(), //
					ResMessages.PARAM_QUIZ_ERROR.getMessage());
		}
		// 撈資料
		List<FeedbackDto>res = feedbackDao.selectFeedback(quizId);
		// 整理資料:透過相同的email把問題跟回答整理在一起
		// Map<String, List<QuesAnswerVO>> 中的key 是放email
		Map<String, List<QuesAnswerVO>> map = new HashMap<>();
		String quizName = "" ;
		String description = "" ;
		for (FeedbackDto dto  :res) {
			quizName = dto.getQuizName();
			description = dto.getDescription();
			String email = dto.getEmail();
			List<QuesAnswerVO>voList = new ArrayList<>();
			if(map.containsKey(email)) {
				// 若map中已存在相同的key(email)，就從map中把對應的value取出
				voList = map.get(email);
				}
				QuesAnswerVO vo = new QuesAnswerVO();
				vo.setQuesId(dto.getQuesId());
				vo.setQuesName(dto.getQuesName());
				try{
					// 將DB中的answer String 轉成 List<String>
					List<String>answerList = mapper.readValue(dto.getAnswer(), new TypeReference<List<String>>() {
				});
					vo.setAnswers(answerList);
				}catch(Exception e) {
					return new FeedbackRes(ResMessages.ANSWER_PARSE_ERROR.getCode(), //
							ResMessages.ANSWER_PARSE_ERROR.getMessage());
				}
				voList.add(vo);
				map.put(email,voList);
			}
			
			
			
			
			
		
		List<FeedbackVo> feedbackList  =new ArrayList<>() ;
		a:for (FeedbackDto dto  :res) {
			String email = dto.getEmail();
			for(FeedbackVo vo : feedbackList ) {
				if (email.equalsIgnoreCase(vo.getEmail())) {
					continue a ;
				}
			}
			FeedbackVo feedbackVo = new FeedbackVo();
			feedbackVo.setUserName(dto.getUserName());
			feedbackVo.setPhone(dto.getPhone());
			feedbackVo.setEmail(dto.getEmail());
			feedbackVo.setAge(dto.getAge());
			feedbackVo.setFillinDate(dto.getFillinDate());
			feedbackVo.setQuesAnswerList(map.get(dto.getEmail()));
			feedbackList.add(feedbackVo);
		}
		return new FeedbackRes(ResMessages.SUCCESS.getCode(), //
				ResMessages.SUCCESS.getMessage() , quizName , description , feedbackList  );
	}
}
