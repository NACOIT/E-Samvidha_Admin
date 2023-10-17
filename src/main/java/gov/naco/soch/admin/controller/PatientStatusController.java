/**
 * 
 */
package gov.naco.soch.admin.controller;

import java.util.List;

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

import gov.naco.soch.admin.service.PatientStatusService;

/**
 * @date 2020-Jan-02 12:32:25 PM
 */

@RestController
@RequestMapping("/patientstatus")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PatientStatusController {

	// Logger method
	private static final Logger logger = LoggerFactory.getLogger(PatientStatusController.class);

	public PatientStatusController() {
	}

	@Autowired
	private PatientStatusService patientStatusService;

	// API to get all details from patient_status_master table
	/*@GetMapping("/list")
	public @ResponseBody List<PatientStatusDto> getAllPatientStatus() {
		logger.debug("getAllPatientStatuses method called");
		//return patientStatusService.getAllPatientStatus();
		return null;
	}*/

	// API to save details to patient_status_mastertable
	/* @PostMapping("/save")
	public @ResponseBody PatientStatusDto savePatientStatus(@RequestBody PatientStatusDto patientStatusDto) {
		logger.debug("addPatientStatus method called with parameters->{}", patientStatusDto);

		// Invoking savePatientStatus method
		//patientStatusDto=patientStatusService.savePatientStatus(patientStatusDto);
		logger.debug("addPatientStatus method returns with parameters->{}", patientStatusDto);
		return patientStatusDto;
	} */


}
