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

import gov.naco.soch.admin.service.IndentRequestStatusService;
import gov.naco.soch.dto.IndentRequestStatusDto;

/**
 * @date 2019-Dec-31 10:38:37 AM
 */

@RestController
@RequestMapping("/indentrequeststatus")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IndentRequestStatusController {

	// Logger method
	private static final Logger logger = LoggerFactory.getLogger(IndentRequestStatusController.class);

	public IndentRequestStatusController() {
	}

	@Autowired
	private IndentRequestStatusService indentRequestStatusService;

	// API to get all details from indent_request_status_master table
	@GetMapping("/list")
	public @ResponseBody List<IndentRequestStatusDto> getAllIndentRequestStatuses() {
		logger.debug("getAllIndentRequestStatuses method called");
		return indentRequestStatusService.getAllIndentRequestStatuses();
	}

	// API to save details to indent_request_status_master table
	@PostMapping("/save")
	public @ResponseBody IndentRequestStatusDto saveIndentRequestStatus(
			@RequestBody IndentRequestStatusDto indentRequestStatusDto) {
		logger.debug("addIndentRequestStatus method called with parameters->{}", indentRequestStatusDto);

		// Invoking saveIndentRequestStatus method
		indentRequestStatusService.saveIndentRequestStatus(indentRequestStatusDto);
		logger.debug("addIndentRequestStatus method returns with parameters->{}", indentRequestStatusDto);
		return indentRequestStatusDto;
	}

}
