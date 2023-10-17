/**
 * 
 */
package gov.naco.soch.admin.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.PatientInformationService;
import gov.naco.soch.dto.DatavaluesPatientInformationMasterDto;

/**
 * @date 2020-Jan-02 3:58:21 PM
 */

@RestController
@RequestMapping("/patientinformation")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PatientInformationController {

	@Autowired
	private PatientInformationService patientInformationService;

	// Logger method
	private static final Logger logger = LoggerFactory.getLogger(PatientInformationController.class);

	public PatientInformationController() {
	}

	// API to get all details from datavalues_patient_information_master table
	@GetMapping("/list")
	public @ResponseBody List<DatavaluesPatientInformationMasterDto> getAllPatientInformation() {
		logger.debug("getAllPatientInformation method is invoked");
		//return patientInformationService.getAllPatientInformation();
		return null;
	}

	// API to add details to datavalues_patient_information_master table
	@PostMapping("/save")
	public @ResponseBody DatavaluesPatientInformationMasterDto savePatientInformation(
			@Valid @RequestBody DatavaluesPatientInformationMasterDto patientInformationDto) {
		logger.debug("savePatientInformation method called with parameters->{}", patientInformationDto);

		// Invoking savePatientInformation method
		//patientInformationDto=patientInformationService.savePatientInformation(patientInformationDto);
		logger.debug("savePatientInformation method returns with parameters->{}", patientInformationDto);
		return patientInformationDto;
	}

}
