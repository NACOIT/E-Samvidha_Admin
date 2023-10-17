
package gov.naco.soch.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.MasterNameListService;
import gov.naco.soch.dto.MasterNameListDto;

/**
 * @date 2020-Jan-03 1:58:02 PM
 */

// Controller class for API call
@RestController
@RequestMapping("/masters")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MasterNameListController {

	@Autowired
	private MasterNameListService masterNameListService;

	// Logger Method
	private static final Logger logger = LoggerFactory.getLogger(MasterNameListController.class);

	public MasterNameListController() {
	}

	/*
	 * API for listing master_name and master_description from table
	 * master_name_list
	 */
	@GetMapping("/list")
	public @ResponseBody List<MasterNameListDto> getAllMasters() {
		logger.debug("getAllMasterNames method called");
		return masterNameListService.getAllMasterNames();
	}

}
