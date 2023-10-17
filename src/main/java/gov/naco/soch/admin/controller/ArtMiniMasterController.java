/**
 * 
 */
package gov.naco.soch.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.ArtMiniMasterDto;
import gov.naco.soch.admin.dto.ArtMiniMasterFollowUpVisitDto;
import gov.naco.soch.admin.dto.ArtMiniMasterIctcReferralsDto;
import gov.naco.soch.admin.dto.ArtMiniMasterMedicalOfficerDto;
import gov.naco.soch.admin.dto.ArtMiniMasterReferToDto;
import gov.naco.soch.admin.dto.ArtMinimasterAddPepDto;
import gov.naco.soch.admin.dto.ArtMinimasterClinicalDetailsDto;
import gov.naco.soch.admin.dto.ArtMinimasterCouncellingNotesDto;
import gov.naco.soch.admin.dto.ArtMinimasterCounsellorDto;
import gov.naco.soch.admin.dto.ArtMinimasterIptAttDetailsDto;
import gov.naco.soch.admin.dto.ArtMinimasterPaedatricBeneficiaryDto;
import gov.naco.soch.admin.dto.ArtStaffNurseMinimasterDto;
import gov.naco.soch.admin.service.ArtMiniMasterService;

/**
 * @author Pranav MS (144958)
 * @email pranav.sasi@ust-global.com
 * @date 2020-Jul-20 11:58:18 am 
 * 
 */
@RestController
@RequestMapping("/artminimaster")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ArtMiniMasterController {
	private static final Logger logger = LoggerFactory.getLogger(ArtMiniMasterController.class);

	@Autowired
	ArtMiniMasterService artMiniMasterService;
	
	public ArtMiniMasterController() {
	}

	@GetMapping("/addbenficiary")
	public @ResponseBody ArtMiniMasterDto getMiniMasterForAddBenficiary() {
		logger.debug("Entering into method getAllLabTypes");
		return artMiniMasterService.getMiniMasterForAddBenficiary();
	}
	
	@GetMapping("/medicalofficer/miniprofile")
	public @ResponseBody ArtMiniMasterMedicalOfficerDto getMedicalOfficerMiniprofile() {
		logger.debug("Entering into method medical officer mini profile");
		return artMiniMasterService.getMedicalOfficerMiniprofile();
	}
	
	@GetMapping("/staffnurse/miniprofile")
	public @ResponseBody ArtStaffNurseMinimasterDto getStaffNurseMiniprofile() {
		logger.debug("Entering into method staff nurse mini profile");
		return artMiniMasterService.getStaffNurseMiniprofile();
	}
	
	@GetMapping("/staffnurse/tboi")
	public @ResponseBody ArtStaffNurseMinimasterDto getStaffNurseTbOi() {
		logger.debug("Entering into method staff nurse tb oi");
		return artMiniMasterService.getStaffNurseTbOi();
	}
	
	@GetMapping("/followupvisit")
	public @ResponseBody ArtMiniMasterFollowUpVisitDto getFollowUpVisit() {
		logger.debug("Entering into method follow up visit");
		return artMiniMasterService.getFollowUpVisit();
	}
	
	@GetMapping("/clinicaldetails")
	public @ResponseBody ArtMinimasterClinicalDetailsDto getClinicalDetails() {
		logger.debug("Entering into method clinical details");
		return artMiniMasterService.getClinicalDetails();
	}
	
	@GetMapping("/ictcreferrals")
	public @ResponseBody ArtMiniMasterIctcReferralsDto getMinimasterForIctcReferrals() {
		logger.debug("Entering into method ictc referrals");
		return artMiniMasterService.getMinimasterForIctcReferrals();
	}
	
	@GetMapping("/councellingnotes")
	public @ResponseBody ArtMinimasterCouncellingNotesDto getMinimasterForCouncellingNotes() {
		logger.debug("Entering into method councelling notes");
		return artMiniMasterService.getMinimasterForCouncellingNotes();
	}
	
	@GetMapping("/paediatric/beneficiary")
	public @ResponseBody ArtMinimasterPaedatricBeneficiaryDto getMinimasterForPaediatricBeneficiary() {
		logger.debug("Entering into method peadiatric");
		return artMiniMasterService.getMinimasterForPaediatricBeneficiary();
	}
	
	@GetMapping("/addpep")
	public @ResponseBody ArtMinimasterAddPepDto getMinimasterForAddPep() {
		logger.debug("Entering into method add pep");
		return artMiniMasterService.getMinimasterForAddPep();
	}
	
	@GetMapping("/counsellor/miniprofile")
	public @ResponseBody ArtMinimasterCounsellorDto getMinimasterForCounsellorMiniprofile() {
		logger.debug("Entering into method counsellor mini profile");
		return artMiniMasterService.getMinimasterForCounsellorMiniprofile();
	}
	
	@GetMapping("/iptattdetails")
	public @ResponseBody ArtMinimasterIptAttDetailsDto getMinimasterForIptAttDetails() {
		logger.debug("Entering into method ipt att details");
		return artMiniMasterService.getMinimasterForIptAttDetails();
	}
	
	@GetMapping("/referto")
	public @ResponseBody ArtMiniMasterReferToDto getReferToCoeAndArtPlus() {
		logger.debug("Entering into method getReferToCoeAndArtPlus");
		return artMiniMasterService.getReferToCoeAndArtPlus();
	}
}
