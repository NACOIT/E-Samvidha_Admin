package gov.naco.soch.admin.service;

import gov.naco.soch.dto.ChoiceDetailsDto;
import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.dto.QuestionDto;
import gov.naco.soch.dto.QuestionnaireDto;
import gov.naco.soch.dto.SurveyResponseDto;
import gov.naco.soch.entity.Question;
import gov.naco.soch.entity.Questionnaire;
import gov.naco.soch.entity.Survey;
import gov.naco.soch.entity.SurveyAnswer;
import gov.naco.soch.entity.Question.QstnType;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.mapper.QuestionnaireMapperUtil;
import gov.naco.soch.repository.QuestionRepository;
import gov.naco.soch.repository.QuestionnaireRepository;
import gov.naco.soch.repository.SurveyAnswerRepository;
import gov.naco.soch.repository.SurveyRepository;
import gov.naco.soch.util.CommonConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

@Service
@Transactional
public class QuestionnaireService {

	@Autowired
	private QuestionnaireRepository questionnaireRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private SurveyRepository surveyRepository;
	
	@Autowired
	private SurveyAnswerRepository surveyAnswerRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(QuestionnaireService.class);
	
	public Integer saveSurveyAnswer( SurveyResponseDto surveyDto, Integer id ) {
//		if(surveyRepository.findById ( surveyDto.getId()).isPresent()) {
//			Survey survey = surveyRepository.findById ( surveyDto.getId()).get();
//			resetSurveyAnswersFromDto( surveyDto, survey );
//			survey = surveyRepository.save(survey);
//		}else {
		
		Integer surveyId=null;
			if(surveyDto.getId()==null) {
				
				Survey survey = convertSurveyAnswersFromDto(surveyDto, id);
				survey = surveyRepository.save(survey);
				surveyId=survey.getId();
			}
			else {
				Survey survey = surveyRepository.findById(surveyDto.getId()).get();
				resetSurveyAnswersFromDto( surveyDto, survey );
					//Survey survey = convertSurveyAnswersFromDto(surveyDto, id);
					survey = surveyRepository.save(survey);
					surveyId=survey.getId();
			}
			
			return surveyId;
//		}
		
	}
	public Integer updateSurveyAnswer( SurveyResponseDto surveyDto, Integer id ) {
//		if(surveyRepository.findById ( surveyDto.getId()).isPresent()) {
//			Survey survey = surveyRepository.findById ( surveyDto.getId()).get();
//			resetSurveyAnswersFromDto( surveyDto, survey );
//			survey = surveyRepository.save(survey);
//		}else {
		Survey survey = surveyRepository.findById(surveyDto.getId()).get();
		resetSurveyAnswersFromDto( surveyDto, survey );
			//Survey survey = convertSurveyAnswersFromDto(surveyDto, id);
			survey = surveyRepository.save(survey);
			return survey.getId();
//		}
		
	}
	
	private Survey resetSurveyAnswersFromDto( SurveyResponseDto surveyDto, Survey survey  ) {
	
		survey.removeAllAnswers();
		for ( Question question : survey.getQuestionnaire().getQuestions() ) {
			SurveyAnswer answer = new SurveyAnswer();
			answer.setQuestion(question);
			answer.setAnswer(surveyDto.getAnswerForQuestionId(question.getId()));
			survey.addAnswer(answer);
		}
		
		return survey;
	}
	private Survey convertSurveyAnswersFromDto( SurveyResponseDto surveyDto, Integer id) {
		
		Survey survey  =new Survey();
		survey.setQuestionnaire(questionnaireRepository.findById(id));
		survey.removeAllAnswers();
		for ( Question question : questionnaireRepository.findById(id).getQuestions() ) {
			SurveyAnswer answer = new SurveyAnswer();
			answer.setQuestion(question);
			answer.setAnswer(surveyDto.getAnswerForQuestionId(question.getId()));
			survey.addAnswer(answer);
		}
		
		return survey;
	}
	
	public QuestionnaireDto getSurveyAnswersById(Integer id, boolean isSuperAdmin) {
		Optional<Survey> survey = surveyRepository.findById(id);
		Map<String, String> editAnswer=new HashMap<String, String>();
		//Tweak - Same DTO is used. Questionnaire and Survey are same for UI. Id is replaced.
		QuestionnaireDto qnreDto = mapQuestionnaireToDto(survey.get().getQuestionnaire(),true, isSuperAdmin);
		int i=0;
		for(QuestionDto questionDto:qnreDto.getQuestions()) {
			String eAns="";
			for(SurveyAnswer response:survey.get().getAnswers()) {
				
				if(response.getQuestion().getId().equals(questionDto.getId())) {
					eAns=response.getAnswer();
					qnreDto.getQuestions().get(i).setAnswers(convertToJson(questionDto.getChoices(), response.getAnswer(),questionDto.getChoiceType()));
				}
			}
			editAnswer.put(questionDto.getName(), eAns);	
			i++;
		}
		qnreDto.setEditAnswer(editAnswer);
		//qnreDto.setId(survey.get().getId());
		return qnreDto;
		
	}
	private String convertToJson(String choice, String answer, String choice_type) {
		List<String> ans = new ArrayList<>();
		 JsonArray jsonChoiceArray=new JsonParser().parse(choice).getAsJsonArray();
//		if(choice_type.equals("Checkbox")) {
			if(answer.startsWith("[")) {
		 JsonArray jsonArray=new JsonParser().parse(answer).getAsJsonArray();
			 for (int i=0; i < jsonArray.size(); i++) {
				 for (int j=0; j < jsonChoiceArray.size(); j++) {
					 if(jsonArray.get(i).equals(jsonChoiceArray.get(j).getAsJsonObject().get("value"))) {
							ans.add(jsonChoiceArray.get(j).getAsJsonObject().get("text").getAsString());
					}
				 }
			}
			}
			else {
				
				answer=answer.substring(1, answer.length()-1);
				for (int i=0; i < jsonChoiceArray.size(); i++) {
					
					if(answer.equals(jsonChoiceArray.get(i).getAsJsonObject().get("value").getAsString())) {
						 ans.add(jsonChoiceArray.get(i).getAsJsonObject().get("text").getAsString());
					 }
				
					
				}
			}
				
//		} else {
//			answer=answer.substring(1, answer.length()-1);
//			for (int i=0; i < jsonChoiceArray.size(); i++) {
//				
//				if(answer.equals(jsonChoiceArray.get(i).getAsJsonObject().get("value").getAsString())) {
//					 ans.add(jsonChoiceArray.get(i).getAsJsonObject().get("text").getAsString());
//				 }
//			}
//		}
		
		return ans.toString();
	}
	public QuestionnaireDto getSurveyById( Integer id, boolean isSuperAdmin ) {
//		Optional<Survey> survey = surveyRepository.findById(id);
//		
//		//Tweak - Same DTO is used. Questionnaire and Survey are same for UI. Id is replaced.
//		QuestionnaireDto qnreDto = mapQuestionnaireToDto(survey.get().getQuestionnaire(),true);
//		qnreDto.setId(survey.get().getId());
//		return qnreDto;
		
		Questionnaire qnre = questionnaireRepository.findById(id);
		//		Questionnaire qnre = questionnaireRepository.findById(id);
		QuestionnaireDto qnreDto = mapQuestionnaireToDto(qnre,true, isSuperAdmin);
		qnreDto.setId(qnre.getId());
		return qnreDto;
	}
	public QuestionnaireDto getSurveyByName( String surveyName, boolean isSuperAdmin) {
//		Optional<Survey> survey = surveyRepository.findById(id);
//		
//		//Tweak - Same DTO is used. Questionnaire and Survey are same for UI. Id is replaced.
//		QuestionnaireDto qnreDto = mapQuestionnaireToDto(survey.get().getQuestionnaire(),true);
//		qnreDto.setId(survey.get().getId());
//		return qnreDto;
		
		QuestionnaireDto qnreDto =new QuestionnaireDto();
		List<Questionnaire> qnres=questionnaireRepository.findBySurveyName(surveyName);
		if(!qnres.isEmpty()) {
		Questionnaire qnre = qnres.get(0);
//				Questionnaire qnre = questionnaireRepository.findById(id);
		qnreDto = mapQuestionnaireToDto(qnre,true, isSuperAdmin);
		qnreDto.setId(qnre.getId());
		
		}
		else {
			logger.error(Constants.NO_RECORDS_FOUND);
			String errorfield = "surveyName";
			throwErrorNoRecord(errorfield, surveyName);
		}
		return qnreDto;
	}
	public QuestionnaireDto getQuestionnaireById( Integer id, boolean isSuperAdmin ) {
//		Questionnaire qnre = questionnaireRepository.findById(id).get();
		Questionnaire qnre = questionnaireRepository.findById(id);
		QuestionnaireDto qnreDto = mapQuestionnaireToDto(qnre,true, isSuperAdmin);
		return qnreDto;
	}
	
	// method to find all questionnaires
	public List<QuestionnaireDto> getAllQuestionnaires() {
		List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
		List<QuestionnaireDto> questionnaireDtosList = new ArrayList<QuestionnaireDto>();
		logger.debug("mapToQuestionnaireDtoList method called with parameters->{}", questionnaireDtosList);
		questionnaireDtosList = QuestionnaireMapperUtil.mapToQuestionnaireDtoList(questionnaireList);
		return questionnaireDtosList;
	}

	// method to add/update Questionnaire
	public QuestionnaireDto addQuestionnaire(QuestionnaireDto questionnaireDto) {
		Questionnaire questionnaire = new Questionnaire();
		int count = 0;
		boolean isEdit = false;
		if (questionnaireDto.getId() != null && questionnaireDto.getId() != 0) {
			// To check whether the questionnaire is already exist in table in edit
			String surveyName = questionnaireDto.getSurveyName().trim().toString();
			count = questionnaireRepository.existsBySurveyNameInEdit(surveyName, questionnaireDto.getId());
			isEdit = true;
		} else {
			String surveyName = questionnaireDto.getSurveyName().trim().toString();
			count = questionnaireRepository.existsBySurveyNameIgnoreCase(surveyName);
		}
		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "facilityTypeName";
			throwError(errorfield, questionnaireDto.getSurveyName());
		}
		
		// get all details by questionnaire_id in edit
		if (isEdit) {
//			questionnaire = questionnaireRepository.findById(questionnaireDto.getId()).get();
			questionnaire = questionnaireRepository.findById(questionnaireDto.getId());
		}
				
		questionnaire = QuestionnaireMapperUtil.maptoQuestionnaire(questionnaireDto, questionnaire);
		logger.debug("maptoQuestionnaire method called with parameters->{}", questionnaireDto);

		// save facility_type details into table
		questionnaireRepository.save(questionnaire);
		logger.debug("maptoFacilityTypeDto method called with parameters->{}", questionnaire);
		questionnaireDto = QuestionnaireMapperUtil.mapQuestionnaireToDto(questionnaire, false);

		return questionnaireDto;
	}

	// Method to throw error
	private void throwError(String errorfield, String errorFieldValue) {
		List<ErrorDto> errorDtoList = new ArrayList<>();
		List<String> detailsSimplified = new ArrayList<String>();
		ErrorDto errorDto = new ErrorDto();
		errorDto.setField(errorfield);
		errorDto.setDescription(Constants.DUPLICATE_FOUND + "'" + errorFieldValue + "'");
		errorDtoList.add(errorDto);
		detailsSimplified.add(errorDto.getDescription());
		ErrorResponse errorResponse = new ErrorResponse(CommonConstants.VALIDATION_FAILED, errorDtoList,
				detailsSimplified);
		throw new ServiceException(Constants.DUPLICATE_FOUND + " '" + errorFieldValue + "' ", errorResponse,
				HttpStatus.BAD_REQUEST);
	}
	
	// Method to throw error - no record found
	private void throwErrorNoRecord(String errorfield, String errorFieldValue) {
		List<ErrorDto> errorDtoList = new ArrayList<>();
		List<String> detailsSimplified = new ArrayList<String>();
		ErrorDto errorDto = new ErrorDto();
		errorDto.setField(errorfield);
		errorDto.setDescription(Constants.NO_RECORDS_FOUND + "'" + errorFieldValue + "'");
		errorDtoList.add(errorDto);
		detailsSimplified.add(errorDto.getDescription());
		ErrorResponse errorResponse = new ErrorResponse(CommonConstants.VALIDATION_FAILED, errorDtoList,
				detailsSimplified);
		throw new ServiceException(Constants.NO_RECORDS_FOUND + " '" + errorFieldValue + "' ", errorResponse,
				HttpStatus.BAD_REQUEST);
	}
	
	public QuestionnaireDto saveQuestionnaire( QuestionnaireDto qnreDto, boolean isSuperAdmin ) {
		Questionnaire qnre = questionnaireRepository.save(createQuestionnaireFromDto( qnreDto ));
		return mapQuestionnaireToDto(qnre,true, isSuperAdmin);
	}
	
	public QuestionnaireDto updateQuestionnaireWithQuestions( QuestionnaireDto qnreDto ) throws JsonProcessingException {
		Questionnaire qnree = null;
		List<QuestionDto> qstnDtos = qnreDto.getQuestions();//form
		if(qnreDto.getId()!=null) {
			qnree = questionnaireRepository.findById (qnreDto.getId());
			
			List<Question> qstnss = qnree.getQuestions();// db
			if (!CollectionUtils.isEmpty( qstnDtos )) {
			
				if (!CollectionUtils.isEmpty(qstnss)) {

					// filter out all deleted questions
					List<Question> deletedList = qstnss.stream()
							.filter(qstn -> qstnDtos.stream().filter(dto -> dto.getId()!=null).noneMatch(dto -> dto.getId().equals(qstn.getId())))
							.collect(Collectors.toList());

					for (Question question : deletedList) {
						qnree.removeQuestion(question);
					}
				}
			}
		}

		List<Question> qstns=new ArrayList<>();
		qnree = createQuestionnaireFromDtoUpdateAll( qnreDto );
		for (QuestionDto questionDto : qnreDto.getQuestions()) {
			Question qstn=createQuestionFromDto(questionDto);
			qstn.setQuestionnaire(qnree);
			qstns.add(qstn);
		}
		for  (Question q : qstns) {
			 questionRepository.save(q);
		}
		
		// saveALL not saving new questions without id hence commented 
		// only the question with id are updated so we need above code i.e questionRepository.save(q)
		//List<Question> qstn= questionRepository.saveAll(qstns);
	
		
		//Questionnaire qnre = questionnaireRepository.save(createQuestionnaireFromDtoUpdate( qnreDto ));
		return qnreDto;
	}
	
	public QuestionnaireDto updateQuestionnaire( QuestionnaireDto qnreDto, boolean isSuperAdmin ) throws JsonProcessingException {
		Questionnaire qnre = null;
		if(qnreDto.getId()!=null) {
//		 qnre = questionnaireRepository.findById (qnreDto.getId()).get();
			qnre = questionnaireRepository.findById (qnreDto.getId());
//		mapQuestionnaireFromDto( qnreDto, qnre ); // june 17
		mapQuestionnaireFromDto1( qnreDto, qnre ); // june 17
		}
		else {
			
				String surveyName = qnreDto.getSurveyName().trim().toString();
				int count = questionnaireRepository.existsBySurveyNameIgnoreCase(surveyName);
			
			if (count != 0) {
				logger.error(Constants.DUPLICATE_FOUND);
				String errorfield = "facilityTypeName";
				throwError(errorfield, qnreDto.getSurveyName());
			}else {
			 qnre = questionnaireRepository.save(createQuestionnaireFromDto( qnreDto ));
			}
		}
		qnre = questionnaireRepository.save(qnre);
		return mapQuestionnaireToDto(qnre,true, isSuperAdmin);
	}
private Questionnaire createQuestionnaireFromDto(QuestionnaireDto questionnaireDto ) {
	Questionnaire questionnaire = new Questionnaire(questionnaireDto.getSurveyName());
		try {
		
		questionnaire.setId(questionnaireDto.getId());
		questionnaire.setDescription(questionnaireDto.getDescription());
		questionnaire.setSurveyName(questionnaireDto.getSurveyName());
		
		List<QuestionDto> qstnDtos = questionnaireDto.getQuestions();
		if ( !CollectionUtils.isEmpty( qstnDtos )) {
			for (QuestionDto questionDto : qstnDtos) {
				
					questionnaire.addQuestion( createQuestionFromDto( questionDto ) );
				
				
			}
		}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return questionnaire;
	}
private Questionnaire createQuestionnaireFromDtoUpdateAll(QuestionnaireDto questionnaireDto ) {
	Questionnaire questionnaire = new Questionnaire(questionnaireDto.getSurveyName());
		try {
		
		questionnaire.setId(questionnaireDto.getId());
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return questionnaire;
	}
private Questionnaire createQuestionnaireFromDtoUpdate(QuestionnaireDto questionnaireDto ) {
	Questionnaire questionnaire = new Questionnaire(questionnaireDto.getSurveyName());
		try {
		
		questionnaire.setId(questionnaireDto.getId());
		questionnaire.setDescription(questionnaireDto.getDescription());
		questionnaire.setSurveyName(questionnaireDto.getSurveyName());
		
		List<QuestionDto> qstnDtos = questionnaireDto.getQuestions();
		
		if ( !CollectionUtils.isEmpty( qstnDtos )) {
			for (QuestionDto questionDto : qstnDtos) {
				List<SurveyAnswer> survey = surveyAnswerRepository.findByQuestionId(questionDto.getId());	
				if(questionDto.getId()!=null) {
				if(checkQuestionAnswered(survey, questionDto.getId())) {
					questionnaire.addQuestion( createQuestionFromDto( questionDto ) );
				}}
				else
				{
					questionnaire.addQuestion( createQuestionFromDto( questionDto ) );
				}
			}
		}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return questionnaire;
	}
static String convertToValue(String s) 
{ 
	String str="";
    Pattern p = Pattern.compile("\\b[a-zA-Z]"); 
    if(s!=null) {
    	Matcher m = p.matcher(s); 

        while (m.find()) {
        	str=str+m.group();
        }
    }
    
 	return str; 
} 
private Question createQuestionFromDto( QuestionDto questionDto ) throws JsonProcessingException {
	List<Choice> choices=new ArrayList<>();
	for(ChoiceDetailsDto str: questionDto.getChoiceDetailss()) {
		Choice choice=new Choice();
		choice.setText(str.getChoices());
		if (str.getChoices().length()<2) {
			choice.setValue(str.getChoices());
		}else {
			choice.setValue(convertToValue(str.getChoices()).toLowerCase());
		}
		choices.add(choice);
	}
	ObjectMapper Obj = new ObjectMapper();
	String jsonStr = Obj.writeValueAsString(choices);
	
	String visibleOption=null;
	if(questionDto.getVisibleIfQuestion()==null || questionDto.getVisibleIfCondition()==null || questionDto.getVisibleIfOption()==null) {
		visibleOption="{}  ''";
	} else {
		visibleOption="{"+questionDto.getVisibleIfQuestion()+"}"+" "+questionDto.getVisibleIfCondition()+" "+"'"+convertToValue(questionDto.getVisibleIfOption()).toLowerCase()+"'";
	}
	if(questionDto.getId()==null) {
		return new Question(questionDto.getName(), 
				getQstnTypeFromString(questionDto.getChoiceType()),
				questionDto.getTitle(), visibleOption, jsonStr );
	}else {
		return new Question(questionDto.getId(), questionDto.getName(), 
				getQstnTypeFromString(questionDto.getChoiceType()),
				questionDto.getTitle(), visibleOption, jsonStr );
	}

}
private QstnType getQstnTypeFromString( String typeString) {
	return typeString.isEmpty() ? QstnType.Radio : QstnType.valueOf( typeString );
}
private List<QuestionnaireDto> mapQuestionnaireListToDto(List<Questionnaire> questionnaires, boolean isSuperAdmin ) {
	
	List<QuestionnaireDto> questionnaireDtos = new ArrayList<QuestionnaireDto>();
	if (!CollectionUtils.isEmpty(questionnaires)) {
		for (Questionnaire questionnaire: questionnaires) {
			//not load qstns for better performance. Questionnaire List doesn't need details
			questionnaireDtos.add(mapQuestionnaireToDto(questionnaire, false, isSuperAdmin));
		};
	}
	return questionnaireDtos;
}
private QuestionnaireDto mapQuestionnaireToDto(Questionnaire questionnaire,
		boolean isLoadQuestions, boolean isSuperAdmin) {
	
	QuestionnaireDto questionnaireDto = new QuestionnaireDto();
	questionnaireDto.setId( questionnaire.getId() );
	questionnaireDto.setSurveyName(questionnaire.getSurveyName());
	questionnaireDto.setDescription(questionnaire.getDescription());
	
	if ( isLoadQuestions) {
		questionnaireDto.setQuestions(mapQuestionListToDto(questionnaire.getQuestions(), isSuperAdmin));
		
	} else {
		questionnaireDto.setQuestions( new ArrayList<QuestionDto>());
	}
			
	return questionnaireDto;
}
private List<QuestionDto> mapQuestionListToDto(List<Question> questions, boolean isSuperAdmin) {
	List<QuestionDto> questionDtos = new ArrayList<QuestionDto>();
	if (!CollectionUtils.isEmpty(questions)) {
		for ( Question question : questions ) {
			questionDtos.add(mapQuestionToDto(question, isSuperAdmin));
		};	
	}
	return questionDtos;
}

private QuestionDto mapQuestionToDto(Question question, boolean isSuperAdmin) {
	QuestionDto questionDto = new QuestionDto();
	questionDto.setId(question.getId());
	questionDto.setName(question.getName());
	questionDto.setTitle(question.getTitle());
	questionDto.setChoiceType(question.getChoiceType().toString());
	questionDto.setChoices(question.getChoices());
	questionDto.setVisibleIfCondition(question.getVisibleIf());
	if(isSuperAdmin) {
		List<SurveyAnswer> survey = surveyAnswerRepository.findByQuestionId(question.getId());
		questionDto.setEditable(checkQuestionAnswered(survey, question.getId()));
	}
	else {
		questionDto.setEditable(true);
	}
	return questionDto;
}
private boolean checkQuestionAnswered(List<SurveyAnswer> surveyList, Integer id) {
	boolean isEditable=true;
//	for(Survey survey: surveyList) {
//		for(SurveyAnswer surveyAnswer: survey.getAnswers()) {
//			if(surveyAnswer.getQuestion().getId()==id) {
//				isEditable=false;
//			}
//		}
//	}
	if(surveyList.size()>0) {
		isEditable=false;
	}
	return isEditable;
}
private Questionnaire mapQuestionnaireFromDto1(QuestionnaireDto questionnaireDto, Questionnaire questionnaire) throws JsonProcessingException {
	questionnaire.setSurveyName(questionnaireDto.getSurveyName());
	questionnaire.setDescription(questionnaireDto.getDescription());

	return questionnaire;
}
private Questionnaire mapQuestionnaireFromDto(QuestionnaireDto questionnaireDto, Questionnaire questionnaire) throws JsonProcessingException {
	
	questionnaire.setSurveyName(questionnaireDto.getSurveyName());
	questionnaire.setDescription(questionnaireDto.getDescription());
	
	List<QuestionDto> qstnDtos = questionnaireDto.getQuestions();
	List<Question> qstns = questionnaire.getQuestions();
			
	if (!CollectionUtils.isEmpty( qstnDtos )) {
		
		if ( !CollectionUtils.isEmpty( qstns ) ) {
			
			//filter out all deleted questions
			List<Question> deletedList = qstns.stream()
					.filter(qstn -> qstnDtos.stream().noneMatch( dto -> dto.getId().equals(qstn.getId())))
					.collect(Collectors.toList());
			
			for ( Question question : deletedList ) {
				questionnaire.removeQuestion(question);
			}
		}
		
		final List<Question> qstnsRemaining = questionnaire.getQuestions();
					
		if ( !CollectionUtils.isEmpty( qstnsRemaining ) ) {
			//filter out all new questions
			List<QuestionDto> existingList = qstnDtos.stream()
					.filter( dto -> qstnsRemaining.stream().anyMatch( qstn -> qstn.getId().equals(dto.getId() )) )
					.collect(Collectors.toList());
			
			//assuming that order is correct in DTO and DB lists. Need to confirm
			for ( int i=0; i< qstnsRemaining.size(); i++ ) {
				qstnsRemaining.get(i).setName(existingList.get(i).getName());
				qstnsRemaining.get(i).setTitle(existingList.get(i).getTitle());
				qstnsRemaining.get(i).setChoiceType(getQstnTypeFromString(existingList.get(i).getChoiceType()));
//				qstnsRemaining.get(i).setChoices(existingList.get(i).getChoiceDetailss());
				qstnsRemaining.get(i).setVisibleIf(existingList.get(i).getVisibleIfQuestion());
			}
			
			//New questions in dto list - filter all existing (saved ones).
			qstnDtos.removeAll(existingList);
		}
		
		for (QuestionDto questionDto : qstnDtos) {
			questionnaire.addQuestion( createQuestionFromDto( questionDto ) );
		}
		
	} else {
		//Remove all questions - dto is empty; but db has questions
		questionnaire.removeAllQuestions();
	}
	
	return questionnaire;
}
}
class Choice{
	private String value;
	private String text;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
