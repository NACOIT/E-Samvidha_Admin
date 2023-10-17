package gov.naco.soch.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.OrwPeMobileService;
import gov.naco.soch.dto.UserMobileMasterDto;


//Controller class for API call
@RestController
@RequestMapping("/orwandpe/mobile")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrwPeMobileController {

	// Logger Method
	private static final Logger logger = LoggerFactory.getLogger(OrwPeMobileController.class);

	@Autowired
	private OrwPeMobileService orwPeMobileService;
	
	@GetMapping("/orw/facilityorwusers/{typologyId}")
	public @ResponseBody List<UserMobileMasterDto> getOrwUsersForTypology(@PathVariable("typologyId") Integer typologyId) {
		logger.info("getOrwUsersForTypology method called");
		logger.info("typologyId :"+typologyId);
		return orwPeMobileService.getOrwMobileUsersForTypology(typologyId);
	}
	
	

	
	@GetMapping("/pe/listbyorw/{orwId}")
	public @ResponseBody List<UserMobileMasterDto> getPeUsersMobileListBasedOnOrw(@PathVariable("orwId") Long orwId) {
		logger.info("getPeUsersListBasedOnOrw method called");
		return orwPeMobileService.getPeUsersMobileListBasedOnOrw(orwId);
	}
}

