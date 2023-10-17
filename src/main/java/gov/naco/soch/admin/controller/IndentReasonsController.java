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

import gov.naco.soch.admin.service.IndentReasonsService;
import gov.naco.soch.dto.IndentReasonsDto;

/**
 * @date 2020-Jan-01 12:51:20 PM
 */

@RestController
@RequestMapping("/indentreasons")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IndentReasonsController {

	// Logger method
	private static final Logger logger = LoggerFactory.getLogger(IndentReasonsController.class);

	public IndentReasonsController() {
	}

	@Autowired
	private IndentReasonsService indentReasonsService;

	// API to get all details from indent_reasons_master table
	@GetMapping("/list")
	public @ResponseBody List<IndentReasonsDto> getAllIndentReasons() {
		logger.debug("getAllIndentReasons method called");
		return indentReasonsService.getAllIndentReasons();
	}

	// API to save details to indent_reasons_master table
	@PostMapping("/save")
	public @ResponseBody IndentReasonsDto saveIndentReasons(@RequestBody IndentReasonsDto indentReasonsDto) {
		logger.debug("addPatientStatus method called with parameters->{}", indentReasonsDto);

		// Invoking saveIndentReasons method
		indentReasonsDto=indentReasonsService.saveIndentReasons(indentReasonsDto);
		logger.debug("addIndentReasons method returns with parameters->{}", indentReasonsDto);
		return indentReasonsDto;
	}

}
