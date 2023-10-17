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

import gov.naco.soch.admin.dto.SocialWelfareDto;
import gov.naco.soch.admin.service.CommonAdminService;
import gov.naco.soch.admin.service.SocialWelfareService;
import gov.naco.soch.dto.MasterDto;

/**
 * @author Pranav MS (144958)
 * @email pranav.sasi@ust-global.com
 * @date 2020-Dec-01 8:18:33 pm 
 * 
 */
@RestController
@RequestMapping("/socialwelfare")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SocialWelfareController {

	private static final Logger logger = LoggerFactory.getLogger(SocialWelfareController.class);
	
	@Autowired
	SocialWelfareService socialWelfareService;
	
	@Autowired
	private CommonAdminService commonService;

	@GetMapping("/list")
	public @ResponseBody List<SocialWelfareDto> getSocialWelfareList() {
		logger.debug("Entering into method getSocialWelfareList");
		return socialWelfareService.getSocialWelfareList();
	}

	@PostMapping("/insert")
	public @ResponseBody SocialWelfareDto saveSocialWelfare(@RequestBody SocialWelfareDto socialWelfareDto) {
		logger.info("saveSocialWelfare method called with parameters->{}", socialWelfareDto);
		SocialWelfareDto temSocialWelfareDto=new SocialWelfareDto();
		temSocialWelfareDto = socialWelfareService.saveSocialWelfare(socialWelfareDto);
		try {
			commonService.clearCache("SocialWelfareMasterForFacilityCache", null);
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return temSocialWelfareDto;
	}
}
