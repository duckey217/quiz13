package com.example.quiz13.servcie.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.quiz13.constants.QuesType;
import com.example.quiz13.constants.ResMessages;
import com.example.quiz13.dao.QuestionDao;
import com.example.quiz13.dao.QuizDao;
import com.example.quiz13.entity.Question;
import com.example.quiz13.entity.Quiz;
import com.example.quiz13.service.ifs.QuizService;
import com.example.quiz13.vo.BasicRes;
import com.example.quiz13.vo.CreateReq;
import com.example.quiz13.vo.DeleteReq;
import com.example.quiz13.vo.GetQuestionsRes;
import com.example.quiz13.vo.SearchReq;
import com.example.quiz13.vo.SearchRes;
import com.example.quiz13.vo.UpdateReq;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuestionDao questionDao;
	
	
	// 
	@Transactional(rollbackOn = Exception.class)
	@Override
	public BasicRes create(CreateReq req) {

		// 參數檢查:參數已透過@Valid 做基本檢查
		// 開始時間不能比結束時間晚
		if (req.getStartDate().isAfter(req.getEndDate())) {
			return new BasicRes(ResMessages.PARAM_DATE_ERROR.getCode(), //
					ResMessages.PARAM_DATE_ERROR.getMessage());
		}
		// 檢查問題內容
			BasicRes checkRes = checkQuestions(req.getQuestionList());
			if(checkRes != null) {
				return checkRes;
			}
		
		try	{
			// 將資料寫進DB
			// 將資料寫進quiz
				
				quizDao.insert(req.getName(), req.getDescription(), req.getStartDate(), req.getEndDate(), req.isPublished());
			// 取最新一筆資料的 quiz_id
				
			int quizId = quizDao.selectMaxQuizId();
			
			// 將資料寫進question
			
			for(Question item : req.getQuestionList()) {
				questionDao.insert(quizId,item.getQuesid(),  item.getName(), item.getType(), item.isMust(), item.getOptions());
				
			}
		}catch(Exception e) {
			
			// 錯誤要拋出去，@transactional 才會生效(將資料回朔)
			throw e;
		}
		
			
		
			
		return new BasicRes(ResMessages.SUCCESS.getCode(), //
				ResMessages.SUCCESS.getMessage());
			
	
	}

	private BasicRes checkQuestions(List<Question> list) {
		for (Question item :list) {
			if (!QuesType.checkType(item.getType())) {
				return new BasicRes(ResMessages.QUES_TYPE_ERROR.getCode(), //
						ResMessages.QUES_TYPE_ERROR.getMessage());}
				
			
				//排除: type不是text的時候  選項沒有值
				if(!item.getType().equalsIgnoreCase(QuesType.TEXT.getType())&&!StringUtils.hasText(item.getOptions()))	{
		
					return new BasicRes(ResMessages.PARAM_OPTIONS_ERROR.getCode(),//
							ResMessages.PARAM_OPTIONS_ERROR.getMessage());
				}
					
				//  選項有值，type就不能是text
				if (item.getType().equalsIgnoreCase(QuesType.TEXT.getType())//
						&& StringUtils.hasText(item.getOptions())) {
					return new BasicRes(ResMessages.PARAM_OPTIONS_ERROR.getCode(),//
							ResMessages.PARAM_OPTIONS_ERROR.getMessage());
				}
				
				
				
				if(item.getType().equalsIgnoreCase(QuesType.TEXT.getType())) {
					return null;
				}
			
		
		// 將選項字串轉成 List<String>: 要先確定當初創建問卷時，前端的多個選項是陣列，且使用 Stringify 轉成字串型態
		// 前端選項原本格式(陣列): ["aa","bb", "cc"]
		// 前端選項原本格式陣列:["aa","vv"]
		
		ObjectMapper mapper = new ObjectMapper();
		try {
		List<String>optionsList = mapper.readValue(item.getOptions(),new TypeReference<>(){});
			if(item.getType().equalsIgnoreCase(QuesType.SINGLE.getType())||item.getType().equalsIgnoreCase(QuesType.MULTI.getType())) {
				if(optionsList.size()<2) {
					return new BasicRes(ResMessages.OPTIONS_SIZE_ERROR.getCode(),//
							ResMessages.OPTIONS_SIZE_ERROR.getMessage());
				}
			}
		
		
		}catch(Exception e){
			return new BasicRes(ResMessages.OPTIONS_PARSE_ERROR.getCode(),//
					ResMessages.OPTIONS_PARSE_ERROR.getMessage());
		}			
		
		}
		
	
	  return null;
	}

	@Override
	public SearchRes getAll() {
		List<Quiz> list = quizDao.getAll();
		return new SearchRes(ResMessages.SUCCESS.getCode(), //
				ResMessages.SUCCESS.getMessage(),list);
	}

	@Override
	public GetQuestionsRes getQuestionsByQuizId(int quizId) {
		
		if(quizId <= 0) {
			return new GetQuestionsRes(ResMessages.PARAM_QUIZ_ID_ERROR.getCode(), //
					ResMessages.PARAM_QUIZ_ID_ERROR.getMessage());
		}
		
		List<Question> list = questionDao.getByQuizId(quizId);
		return new GetQuestionsRes(ResMessages.SUCCESS.getCode(), //
				ResMessages.SUCCESS.getMessage(),list);
	}
	
	@Transactional( rollbackOn = Exception.class)
	@Override
	public BasicRes update(UpdateReq req) {
		// 要檢查 quiz 的 id 是否存在，因為最後是先將問題的資料刪除後再新增，而不是用更新語法
				// 不使用更新語法的考量原因是，更新前有5題，更新後變成4題，若是用更新語法，原本的第5題還會存在
				// 因 quiz 的 id 不存在，問題刪除時資料不會變更，但接著新增時就會新增資料，且這些資料沒有對應的 quiz_id
		int quizId = req.getId();


		int count = quizDao.selectCountById(quizId);
		if(count != 1) {
			return new BasicRes(ResMessages.ID_NOT_FOUND.getCode(), //
					ResMessages.ID_NOT_FOUND.getMessage());
		}
		
		//  開始時間不能比結束時間晚
		if (req.getStartDate().isAfter(req.getEndDate())) {
			return new BasicRes(ResMessages.PARAM_DATE_ERROR.getCode(), //
					ResMessages.PARAM_DATE_ERROR.getMessage());
		}
		// 檢查問題內容
			BasicRes checkRes = checkQuestions(req.getQuestionList());
			if(checkRes != null) {
				return checkRes;
			}
			
		// 檢查Question 中的quizid 是否與Quiz 的 id值一樣
			
			for(Question item : req.getQuestionList()) {
				if(item.getQuizid() !=quizId) {
					return new BasicRes(ResMessages.QUIZ_ID_MISMATCH.getCode(), //
							ResMessages.QUIZ_ID_MISMATCH.getMessage());
				}
			}
			try {
				// 1.更新問卷
				quizDao.updateById(quizId, req.getName(), req.getDescription(), req.getStartDate(), req.getEndDate(), req.isPublished());
				// 2.刪除同張問卷的所有問題
				questionDao.deleteByQuizId(quizId);
				// 3.新增問題
				for(Question item : req.getQuestionList()) {
					questionDao.insert(quizId,item.getQuesid(),  item.getName(), item.getType(), item.isMust(), item.getOptions());
					
				}
			} catch (Exception e) {
				throw e ;
			}
		 return new BasicRes(ResMessages.SUCCESS.getCode(), //
				ResMessages.SUCCESS.getMessage());
	}

	@Override
	public SearchRes getAll(SearchReq req) {
		// 轉換值
		String quizName = req.getQuizName();
		LocalDate startDate = req.getStartDate();
		LocalDate endDate = req.getEndDate();
		
		if(!StringUtils.hasText(quizName)) {
			// 將quizName = null or 全空白字串時  ，轉成空字串
			
			quizName = "" ;
			}
		// startDate 沒有帶值表示不是條件之一，但因為SQL的語法，可以把startDate 替換成很早的時間
		if(startDate == null) {
			startDate = LocalDate.of(1970, 1, 1);
		}
		
		// endDate 沒有帶值表示不是條件之一，但因為SQL的語法，可以把endDate 替換成很晚的時間
		if(endDate == null) {
			endDate = LocalDate.of(2999, 12, 31);
		}
		
		List<Quiz> list =quizDao.getAll(quizName, startDate, endDate);
		
		return new SearchRes(ResMessages.SUCCESS.getCode(), //
				ResMessages.SUCCESS.getMessage() , list);
	}
	
	// 因為update 的方法是跨表操作 ，所以要將這些Dao的操作當作同一次的行為
	@Transactional(rollbackOn = Exception.class  )
	@Override
	public BasicRes delete(DeleteReq req) {
		try {
			quizDao.delete(req.getQuizIdList());
			questionDao.delete(req.getQuizIdList());
		} catch (Exception e) {
			throw e ;
		}
		
		
		return new BasicRes(ResMessages.SUCCESS.getCode(), //
				ResMessages.SUCCESS.getMessage() );
	}
	}
