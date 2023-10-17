
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

import gov.naco.soch.admin.service.StatusGlobalService;
import gov.naco.soch.dto.StatusGlobalDto;

@RestController
@RequestMapping("/statusglobal")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StatusGlobalController {

	@Autowired
	private StatusGlobalService statusGlobalService;

	private static final Logger logger = LoggerFactory.getLogger(StatusGlobalController.class);

	public StatusGlobalController() {
	}

	/**
	 * @return statusGlobalList
	 */
	@GetMapping("/list")
	public @ResponseBody List<StatusGlobalDto> getAllStatusGlobals() {
		logger.debug("getAllStatusGlobals method called");
		//List<StatusGlobalDto> statusGlobalList=statusGlobalService.getAllStatusGlobals();
		//return statusGlobalList;
		return null;
	}

	/**
	 * @param statusGlobalDto
	 * @return statusGlobalDto
	 */
	@PostMapping("/add")
	public @ResponseBody StatusGlobalDto addStatusGlobal(@Valid @RequestBody StatusGlobalDto statusGlobalDto) {
		logger.debug("addStatusGlobal method called with parameters->{}", statusGlobalDto);
		//statusGlobalDto = statusGlobalService.addStatusGlobal(statusGlobalDto);
		return statusGlobalDto;
	}

}
