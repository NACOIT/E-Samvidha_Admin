package gov.naco.soch.admin.controller;

import gov.naco.soch.admin.service.QuestionnaireService;
import gov.naco.soch.dto.QuestionnaireDto;
import gov.naco.soch.dto.SurveyResponseDto;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/questionnaires")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class QuestionnaireController {
	@Autowired
	private QuestionnaireService questionnaireService;
	
	public QuestionnaireController() {
	}

	private static final Logger logger = LoggerFactory
			.getLogger(QuestionnaireController.class);

	// API to get all details from facilityType table
	/**
	 * @return facilityTypeList
	 */
	@GetMapping("/list")
	public @ResponseBody List<QuestionnaireDto> getFacilityTypes() {
		logger.debug("getFacilityTypes method called");
		List<QuestionnaireDto> questionnaireList = questionnaireService.getAllQuestionnaires();
		return questionnaireList;
	}
	@GetMapping("/qstnres/{id}")
	public @ResponseBody QuestionnaireDto getQuestionnaireById( @PathVariable("id") Integer id ) {
		logger.debug("getQuestionnaireById method is invoked");
		return questionnaireService.getQuestionnaireById(id, true);
	}
	@PostMapping("/add")
	public @ResponseBody QuestionnaireDto addQuestionnaire(@RequestBody QuestionnaireDto questionnaireDto) {

		logger.debug("addQuestionnaire method called with parameters->{}", questionnaireDto);
		return questionnaireService.saveQuestionnaire(questionnaireDto, true);

	}
	@PostMapping("/update-all")
	public @ResponseBody QuestionnaireDto updateQuestionnairewithQuestions(@RequestBody QuestionnaireDto questionnaireDto) throws JsonProcessingException {

		logger.debug("addQuestionnaire method called with parameters->{}", questionnaireDto);
		return questionnaireService.updateQuestionnaireWithQuestions(questionnaireDto);

	}
	@PutMapping("/update")
	public @ResponseBody QuestionnaireDto updateQuestionnaire(@RequestBody QuestionnaireDto questionnaireDto) throws JsonProcessingException {
		logger.debug("addQuestionnaire method called with parameters->{}", questionnaireDto);
		return questionnaireService.updateQuestionnaire(questionnaireDto, true);
	}
	
	@GetMapping("/surveys")
	public @ResponseBody QuestionnaireDto getSurveyById( @RequestParam("surveyName") String surveyName) {
		logger.debug("getSurveyById method is invoked");
		return questionnaireService.getSurveyByName(surveyName, false);
//		return questionnaireService.getQuestionnaireById(id);
	}
	
	@PutMapping("/surveys/{id}")
	public @ResponseBody Integer saveSurveyAnswer(@Valid @RequestBody SurveyResponseDto surveyDto, @PathVariable("id") Integer id ) {
		logger.debug("saveSurveyAnswer method is invoked");
		return questionnaireService.saveSurveyAnswer(surveyDto,id);
	}
	@PutMapping("/surveys-update/{id}")
	public @ResponseBody Integer updateSurveyAnswer(@Valid @RequestBody SurveyResponseDto surveyDto, @PathVariable("id") Integer id ) {
		logger.debug("saveSurveyAnswer method is invoked");
		return questionnaireService.updateSurveyAnswer(surveyDto,id);
	}
	
	@GetMapping("/surveys-view/{id}")
	public @ResponseBody QuestionnaireDto getSurveyAnswersById( @PathVariable("id") Integer id ) {
		logger.debug("getSurveyAnswersById method is invoked");
		return questionnaireService.getSurveyAnswersById(id, false);
	}
}
