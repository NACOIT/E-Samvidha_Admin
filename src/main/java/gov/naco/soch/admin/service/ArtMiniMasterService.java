/**
 * 
 */
package gov.naco.soch.admin.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import gov.naco.soch.dto.FacilityBasicListDto;
import gov.naco.soch.dto.FacilityUserDto;
import gov.naco.soch.dto.LocationDto;
import gov.naco.soch.dto.LoginResponseDto;
import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.dto.RegimenDto;
import gov.naco.soch.enums.FacilityTypeEnum;
import gov.naco.soch.util.UserUtils;

/**
 * @author Pranav MS (144958)
 * @email pranav.sasi@ust-global.com
 * @date 2020-Jul-20 12:05:09 pm
 * 
 */

@Service
@Transactional
public class ArtMiniMasterService {

	@Autowired
	MasterDataService masterDataService;

	@Autowired
	private FacilityService facilityService;

	@Autowired
	AddressService addressService;

	@Autowired
	private UserService userService;

	@Autowired
	RegimenService regimenService;

	private static final Logger logger = LoggerFactory.getLogger(ArtMiniMasterService.class);

	/**
	 * @return
	 */
	// @Cacheable(value = "AllMiniMasterForArtAddBenficiaryCache")
	public ArtMiniMasterDto getMiniMasterForAddBenficiary() {
		ArtMiniMasterDto artMiniMasterDto = new ArtMiniMasterDto();
		List<LocationDto> masterState = addressService.getStateList();
		artMiniMasterDto.setMasterState(masterState);

		List<MasterDto> masterRelationType = masterDataService.getRelationType();
		artMiniMasterDto.setMasterRelationType(masterRelationType);

		List<MasterDto> masterPurposes = masterDataService.getPurposes();
		artMiniMasterDto.setMasterPurposes(masterPurposes);

		List<MasterDto> masterOrganisationType = masterDataService.getOrganisationType();
		artMiniMasterDto.setMasterOrganisationType(masterOrganisationType);

		/*
		 * List<FacilityBasicListDto> allArtFacility =
		 * facilityService.getFacilities(null, null,
		 * FacilityTypeEnum.ART_FACILITY.getFacilityType(), null, null, null);
		 * artMiniMasterDto.setAllArtFacility(allArtFacility);
		 */

		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		List<MasterDto> masterSocialwelfare = masterDataService.getSocialWelfare(currentUser.getFacilityId());
		artMiniMasterDto.setMasterSocialwelfare(masterSocialwelfare);

		List<MasterDto> masterReferralStatus = masterDataService.getReferralStatus();
		artMiniMasterDto.setMasterReferralStatus(masterReferralStatus);

		// List<MasterDto>allFacilityTypeList // new api will provide by anees
		List<FacilityBasicListDto> artLacList = facilityService
				.getAllOptimizedFacilityByCurrentFacilityAsParent(FacilityTypeEnum.LAC_FACILITY.getFacilityType()); // new
																													// api
																													// will
																													// provide
																													// by
																													// anees
		artMiniMasterDto.setArtLacList(artLacList);

		List<MasterDto> masterIptstatus = masterDataService.getIptStatus();
		artMiniMasterDto.setMasterIptstatus(masterIptstatus);

		List<MasterDto> masterArtTreatmentStatus = masterDataService.getArtTreatmentStatus();
		artMiniMasterDto.setMasterArtTreatmentStatus(masterArtTreatmentStatus);

		List<MasterDto> beneficiaryArtTransferredFrom = masterDataService.getBeneficiaryArtTransferredFrom();
		artMiniMasterDto.setBeneficiaryArtTransferredFrom(beneficiaryArtTransferredFrom);

		List<FacilityUserDto> artFacilityUserList = userService.getUserListForCurrentFacility();
		artMiniMasterDto.setArtFacilityUserList(artFacilityUserList);

		List<MasterDto> masterPregnancyTypeCase = masterDataService.getPregnancyTypeCase();
		artMiniMasterDto.setMasterPregnancyTypeCase(masterPregnancyTypeCase);

		/*
		 * List<Long> divisoinIds = new ArrayList<Long>();
		 * divisoinIds.add(DivisionEnum.BSD.getDivision());// ICTC division =6
		 * List<FacilityBasicListDto> allICTCcentreList =
		 * facilityService.getFacilities(divisoinIds, null, null, null, null, null);
		 * artMiniMasterDto.setAllICTCcentreList(allICTCcentreList);
		 */

		List<MasterDto> masterTestType = masterDataService.getTestType(null, null);
		artMiniMasterDto.setMasterTestType(masterTestType);

		List<MasterDto> masterTbRegimen = masterDataService.getTbRegimen();
		artMiniMasterDto.setMasterTbRegimen(masterTbRegimen);

		List<MasterDto> tbTreatmentStatus = masterDataService.getTbTreatmentStatus();
		artMiniMasterDto.setTbTreatmentStatus(tbTreatmentStatus);

		List<MasterDto> fourSScreening = masterDataService.getFourSScreening();
		artMiniMasterDto.setFourSScreening(fourSScreening);

		List<MasterDto> beneficiaryActivityStatus = masterDataService.getBeneficiaryActivityStatus();
		artMiniMasterDto.setBeneficiaryActivityStatus(beneficiaryActivityStatus);

		List<MasterDto> beneficiaryCategory = masterDataService.getMasterBeneficiaryCategory();
		artMiniMasterDto.setBeneficiaryCategory(beneficiaryCategory);

		List<MasterDto> masterGender = masterDataService.getGender();
		artMiniMasterDto.setMasterGender(masterGender);

		List<MasterDto> maritalStatus = masterDataService.getMaritalStatus();
		artMiniMasterDto.setMaritalStatus(maritalStatus);

		List<MasterDto> occupation = masterDataService.getMasterOccupation();
		artMiniMasterDto.setOccupation(occupation);

		List<MasterDto> educationLevel = masterDataService.getMasterEducationLevel();
		artMiniMasterDto.setEducationLevel(educationLevel);

		List<RegimenDto> regimenList = regimenService.getRegimens();
		artMiniMasterDto.setRegimenList(regimenList);

		List<MasterDto> entryPoint = masterDataService.getEntryPoint();
		artMiniMasterDto.setEntryPoint(entryPoint);

		List<MasterDto> riskFactor = masterDataService.getRiskFactor();
		artMiniMasterDto.setRiskFactor(riskFactor);

		List<MasterDto> monthlyIncome = masterDataService.getMonthlyIncome();
		artMiniMasterDto.setMonthlyIncome(monthlyIncome);

		List<MasterDto> hivType = masterDataService.getHivType();
		artMiniMasterDto.setHivType(hivType);

		List<MasterDto> treatmentLine = masterDataService.getTreatmentLine();
		artMiniMasterDto.setTreatmentLine(treatmentLine);

		List<MasterDto> deliveryOutcome = masterDataService.getDeliveryOutcome();
		artMiniMasterDto.setDeliveryOutcome(deliveryOutcome);

		List<MasterDto> treatmentOutcome = masterDataService.getTreatmentOutcome();
		artMiniMasterDto.setTreatmentOutcome(treatmentOutcome);

		List<MasterDto> masterMultimonthDispensation = masterDataService.getMultimonthDispensation();
		artMiniMasterDto.setMasterMultimonthDispensation(masterMultimonthDispensation);

		return artMiniMasterDto;
	}

	public ArtMiniMasterMedicalOfficerDto getMedicalOfficerMiniprofile() {

		ArtMiniMasterMedicalOfficerDto minimasterDto = new ArtMiniMasterMedicalOfficerDto();

		List<MasterDto> masterGender = masterDataService.getGender();
		minimasterDto.setMasterGender(masterGender);

		List<RegimenDto> regimen = regimenService.getRegimens();
		minimasterDto.setRegimen(regimen);

		List<MasterDto> artSideEffects = masterDataService.getSideEffects();
		minimasterDto.setArtSideEffects(artSideEffects);

		List<MasterDto> opportunisticInfections = masterDataService.getOpportunisticInfections();
		minimasterDto.setOpportunisticInfections(opportunisticInfections);

		List<MasterDto> functionalStatus = masterDataService.getFunctionalStatus();
		minimasterDto.setFunctionalStatus(functionalStatus);

		List<MasterDto> whoClinicalStage = masterDataService.getClinicalStage();
		minimasterDto.setWhoClinicalStage(whoClinicalStage);

		List<MasterDto> tbRegimen = masterDataService.getTbRegimen();
		minimasterDto.setTbRegimen(tbRegimen);

		List<FacilityUserDto> userList = userService.getUserListForCurrentFacility();
		minimasterDto.setUserList(userList);

		List<MasterDto> concurrentConditions = masterDataService.getOtherAilments();
		minimasterDto.setConcurrentConditions(concurrentConditions);

		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		List<MasterDto> socialWelfareScheme = masterDataService.getSocialWelfare(currentUser.getFacilityId());
		minimasterDto.setSocialWelfareScheme(socialWelfareScheme);

		List<MasterDto> tbType = masterDataService.getTbType();
		minimasterDto.setTbType(tbType);

		List<MasterDto> changeRegimen = masterDataService.getAllMasterArtRegimenAction();
		minimasterDto.setChangeRegimen(changeRegimen);
		
		List<MasterDto> facilityType = masterDataService.getFacilityType();
		minimasterDto.setFacilityType(facilityType);

		return minimasterDto;
	}

	public ArtStaffNurseMinimasterDto getStaffNurseMiniprofile() {

		ArtStaffNurseMinimasterDto minimasterDto = new ArtStaffNurseMinimasterDto();

		List<MasterDto> masterGender = masterDataService.getGender();
		minimasterDto.setMasterGender(masterGender);

		List<RegimenDto> regimen = regimenService.getRegimens();
		minimasterDto.setRegimen(regimen);

		List<MasterDto> opportunisticInfections = masterDataService.getOpportunisticInfections();
		minimasterDto.setOpportunisticInfections(opportunisticInfections);

		List<MasterDto> functionalStatus = masterDataService.getFunctionalStatus();
		minimasterDto.setFunctionalStatus(functionalStatus);

		List<MasterDto> whoClinicalStage = masterDataService.getClinicalStage();
		minimasterDto.setWhoClinicalStage(whoClinicalStage);

		List<MasterDto> tbRegimen = masterDataService.getTbRegimen();
		minimasterDto.setTbRegimen(tbRegimen);

		List<FacilityUserDto> userList = userService.getUserListForCurrentFacility();
		minimasterDto.setUserList(userList);

		List<MasterDto> foursScreening = masterDataService.getFourSScreening();
		minimasterDto.setFoursScreening(foursScreening);

		List<MasterDto> foursSymptom = masterDataService.getFoursSymptom();
		minimasterDto.setFoursSymptom(foursSymptom);

		List<MasterDto> tbType = masterDataService.getTbType();
		minimasterDto.setTbType(tbType);

		List<MasterDto> tbTestType = masterDataService.getTbTestType();
		minimasterDto.setTbTestType(tbTestType);

		List<MasterDto> iptStatus = masterDataService.getIptStatus();
		minimasterDto.setIptStatus(iptStatus);

		List<MasterDto> tbTreatmentStatus = masterDataService.getTbTreatmentStatus();
		minimasterDto.setTbTreatmentStatus(tbTreatmentStatus);

		return minimasterDto;
	}

	public ArtStaffNurseMinimasterDto getStaffNurseTbOi() {

		ArtStaffNurseMinimasterDto minimasterDto = new ArtStaffNurseMinimasterDto();

		List<MasterDto> masterGender = masterDataService.getGender();
		minimasterDto.setMasterGender(masterGender);

		List<MasterDto> beneficiaryCategory = masterDataService.getMasterBeneficiaryCategory();
		minimasterDto.setBeneficiaryCategory(beneficiaryCategory);

		List<MasterDto> opportunisticInfections = masterDataService.getOpportunisticInfections();
		minimasterDto.setOpportunisticInfections(opportunisticInfections);

		List<LocationDto> masterState = addressService.getStateList();
		minimasterDto.setMasterState(masterState);

		return minimasterDto;
	}

	public ArtMiniMasterFollowUpVisitDto getFollowUpVisit() {

		ArtMiniMasterFollowUpVisitDto minimasterDto = new ArtMiniMasterFollowUpVisitDto();

		List<MasterDto> functionalStatus = masterDataService.getFunctionalStatus();
		minimasterDto.setFunctionalStatus(functionalStatus);

		List<MasterDto> whoClinicalStage = masterDataService.getClinicalStage();
		minimasterDto.setWhoClinicalStage(whoClinicalStage);

		List<MasterDto> opportunisticInfections = masterDataService.getOpportunisticInfections();
		minimasterDto.setOpportunisticInfections(opportunisticInfections);

		List<MasterDto> artSideEffects = masterDataService.getSideEffects();
		minimasterDto.setArtSideEffects(artSideEffects);

		List<MasterDto> concurrentConditions = masterDataService.getOtherAilments();
		minimasterDto.setConcurrentConditions(concurrentConditions);

		List<MasterDto> organisationalType = masterDataService.getOrganisationType();
		minimasterDto.setOrganisationalType(organisationalType);

		List<MasterDto> purposes = masterDataService.getPurposes();
		minimasterDto.setPurposes(purposes);

		List<MasterDto> masterGender = masterDataService.getGender();
		minimasterDto.setMasterGender(masterGender);

		List<MasterDto> beneficiaryCategory = masterDataService.getMasterBeneficiaryCategory();
		minimasterDto.setBeneficiaryCategory(beneficiaryCategory);

		List<MasterDto> facilityType = masterDataService.getFacilityType();
		minimasterDto.setFacilityType(facilityType);

		List<RegimenDto> regimen = regimenService.getRegimens();
		minimasterDto.setRegimen(regimen);

		List<FacilityUserDto> userList = userService.getUserListForCurrentFacility();
		minimasterDto.setUserList(userList);

		List<LocationDto> masterState = addressService.getStateList();
		minimasterDto.setMasterState(masterState);

		return minimasterDto;
	}

	public ArtMinimasterClinicalDetailsDto getClinicalDetails() {

		ArtMinimasterClinicalDetailsDto minimasterDto = new ArtMinimasterClinicalDetailsDto();

		List<MasterDto> masterGender = masterDataService.getGender();
		minimasterDto.setMasterGender(masterGender);

		List<MasterDto> beneficiaryCategory = masterDataService.getMasterBeneficiaryCategory();
		minimasterDto.setBeneficiaryCategory(beneficiaryCategory);

		List<MasterDto> obstetricHistory = masterDataService.getObstetricHistory();
		minimasterDto.setObstetricHistory(obstetricHistory);

		List<MasterDto> alcoholUseStatus = masterDataService.getHabitsAlcoholUse();
		minimasterDto.setAlcoholUseStatus(alcoholUseStatus);

		List<MasterDto> tobaccoUseStatus = masterDataService.getTobaccoUse();
		minimasterDto.setTobaccoUseStatus(tobaccoUseStatus);

		List<MasterDto> smokingStatus = masterDataService.getHabitsSmoking();
		minimasterDto.setSmokingStatus(smokingStatus);

		List<MasterDto> hcvStatus = masterDataService.getHcvStatus();
		minimasterDto.setHcvStatus(hcvStatus);

		List<MasterDto> hbvStatus = masterDataService.getHbvStatus();
		minimasterDto.setHbvStatus(hbvStatus);

		List<MasterDto> otherAilments = masterDataService.getOtherAilments();
		minimasterDto.setOtherAilments(otherAilments);

		List<MasterDto> contraception = masterDataService.getContraception();
		minimasterDto.setContraception(contraception);

		List<MasterDto> artBeneficiaryStatus = masterDataService.getArtBeneficiaryStatus();
		minimasterDto.setArtBeneficiaryStatus(artBeneficiaryStatus);

		List<MasterDto> lineTreatmentForRegimen = masterDataService.getRegimenLineOptions();
		minimasterDto.setLineTreatmentForRegimen(lineTreatmentForRegimen);

		List<MasterDto> foursScreening = masterDataService.getFourSScreening();
		minimasterDto.setFoursScreening(foursScreening);

		List<MasterDto> tbTestType = masterDataService.getTbTestType();
		minimasterDto.setTbTestType(tbTestType);

		List<MasterDto> iptStatus = masterDataService.getIptStatus();
		minimasterDto.setIptStatus(iptStatus);

		List<MasterDto> tbResult = masterDataService.getTbResult();
		minimasterDto.setTbResult(tbResult);

		List<MasterDto> tbRegimen = masterDataService.getTbRegimen();
		minimasterDto.setTbRegimen(tbRegimen);

		return minimasterDto;
	}

	public ArtMiniMasterIctcReferralsDto getMinimasterForIctcReferrals() {

		ArtMiniMasterIctcReferralsDto minimasterDto = new ArtMiniMasterIctcReferralsDto();

		List<MasterDto> beneficiaryCategory = masterDataService.getMasterBeneficiaryCategory();
		minimasterDto.setBeneficiaryCategory(beneficiaryCategory);

		List<MasterDto> masterGender = masterDataService.getGender();
		minimasterDto.setMasterGender(masterGender);

		List<MasterDto> hivType = masterDataService.getHivType();
		minimasterDto.setHivType(hivType);

		List<MasterDto> artBeneficiaryStatus = masterDataService.getArtBeneficiaryStatus();
		minimasterDto.setArtBeneficiaryStatus(artBeneficiaryStatus);

		// List<FacilityBasicListDto> ictcCenters =
		// facilityService.getFacilities(divisionIds,0L,0L,divisionIds);
		// minimasterDto.setIctcCenters(ictcCenters);

		return minimasterDto;
	}

	public ArtMinimasterCouncellingNotesDto getMinimasterForCouncellingNotes() {

		ArtMinimasterCouncellingNotesDto minimasterDto = new ArtMinimasterCouncellingNotesDto();

		List<MasterDto> alcoholUseStatus = masterDataService.getHabitsAlcoholUse();
		minimasterDto.setAlcoholUseStatus(alcoholUseStatus);

		List<MasterDto> tobaccoUseStatus = masterDataService.getTobaccoUse();
		minimasterDto.setTobaccoUseStatus(tobaccoUseStatus);

		List<MasterDto> smokingStatus = masterDataService.getHabitsSmoking();
		minimasterDto.setSmokingStatus(smokingStatus);

		List<MasterDto> hcvStatus = masterDataService.getHcvStatus();
		minimasterDto.setHcvStatus(hcvStatus);

		List<MasterDto> hbvStatus = masterDataService.getHbvStatus();
		minimasterDto.setHbvStatus(hbvStatus);

		List<MasterDto> otherAilments = masterDataService.getOtherAilments();
		minimasterDto.setOtherAilments(otherAilments);

		List<MasterDto> contraception = masterDataService.getContraception();
		minimasterDto.setContraception(contraception);

		List<MasterDto> deliveryOutcome = masterDataService.getDeliveryOutcome();
		minimasterDto.setDeliveryOutcome(deliveryOutcome);

		return minimasterDto;
	}

	public ArtMinimasterPaedatricBeneficiaryDto getMinimasterForPaediatricBeneficiary() {

		ArtMinimasterPaedatricBeneficiaryDto minimasterDto = new ArtMinimasterPaedatricBeneficiaryDto();

		List<MasterDto> artStayingWith = masterDataService.getStayingWith();
		minimasterDto.setArtStayingWith(artStayingWith);

		List<MasterDto> masterGender = masterDataService.getGender();
		minimasterDto.setMasterGender(masterGender);

		List<MasterDto> educationalLeval = masterDataService.getMasterEducationLevel();
		minimasterDto.setEducationalLeval(educationalLeval);

		List<MasterDto> infantFeeding = masterDataService.getInfantFeeding();
		minimasterDto.setInfantFeeding(infantFeeding);

		List<MasterDto> birthHistory = masterDataService.getBirthHistory();
		minimasterDto.setBirthHistory(birthHistory);

		List<MasterDto> dnaPcrTest = masterDataService.getDnaPcrTest();
		minimasterDto.setDnaPcrTest(dnaPcrTest);

		List<MasterDto> guardinCaregiver = masterDataService.getGuardianCaregiver();
		minimasterDto.setGuardinCaregiver(guardinCaregiver);

		List<FacilityUserDto> facilityUserlist = userService.getUserListForCurrentFacility();
		minimasterDto.setFacilityUserlist(facilityUserlist);

		return minimasterDto;
	}

	public ArtMinimasterAddPepDto getMinimasterForAddPep() {

		ArtMinimasterAddPepDto minimasterDto = new ArtMinimasterAddPepDto();

		List<MasterDto> masterGender = masterDataService.getGender();
		minimasterDto.setMasterGender(masterGender);

		List<MasterDto> exposureSeverity = masterDataService.getExposureSeverity();
		minimasterDto.setExposureSeverity(exposureSeverity);

		List<MasterDto> hivExposureCode = masterDataService.getHivExposureCode();
		minimasterDto.setHivExposureCode(hivExposureCode);

		List<MasterDto> hivSourceStatus = masterDataService.getHivSourceStatus();
		minimasterDto.setHivSourceStatus(hivSourceStatus);

		List<MasterDto> hivStatus = masterDataService.getHivStatus();
		minimasterDto.setHivStatus(hivStatus);

		List<MasterDto> hcvStatus = masterDataService.getHcvStatus();
		minimasterDto.setHcvStatus(hcvStatus);

		List<MasterDto> hbvStatus = masterDataService.getHbvStatus();
		minimasterDto.setHbvStatus(hbvStatus);

		List<MasterDto> pepPrescription = masterDataService.getPepPrescription();
		minimasterDto.setPepPrescription(pepPrescription);

		List<FacilityUserDto> assignTo = userService.getUserListForCurrentFacility();
		minimasterDto.setAssignTo(assignTo);

		List<LocationDto> masterState = addressService.getStateList();
		minimasterDto.setMasterState(masterState);

		return minimasterDto;
	}

	public ArtMinimasterCounsellorDto getMinimasterForCounsellorMiniprofile() {

		ArtMinimasterCounsellorDto minimasterDto = new ArtMinimasterCounsellorDto();

		List<MasterDto> masterGender = masterDataService.getGender();
		minimasterDto.setMasterGender(masterGender);

		List<RegimenDto> regimen = regimenService.getRegimens();
		minimasterDto.setRegimen(regimen);

		List<MasterDto> beneficiaryActivityStatus = masterDataService.getBeneficiaryActivityStatus();
		minimasterDto.setBeneficiaryActivityStatus(beneficiaryActivityStatus);

		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		List<MasterDto> socialWelfare = masterDataService.getSocialWelfare(currentUser.getFacilityId());
		minimasterDto.setSocialWelfare(socialWelfare);

		List<MasterDto> otherAilments = masterDataService.getOtherAilments();
		minimasterDto.setOtherAilments(otherAilments);

		List<MasterDto> sideEffects = masterDataService.getSideEffects();
		minimasterDto.setSideEffects(sideEffects);

		List<MasterDto> functionalStatus = masterDataService.getFunctionalStatus();
		minimasterDto.setFunctionalStatus(functionalStatus);

		List<MasterDto> clinicalStage = masterDataService.getClinicalStage();
		minimasterDto.setClinicalStage(clinicalStage);

		List<MasterDto> opportunisticInfections = masterDataService.getOpportunisticInfections();
		minimasterDto.setOpportunisticInfections(opportunisticInfections);

		return minimasterDto;
	}

	public ArtMinimasterIptAttDetailsDto getMinimasterForIptAttDetails() {

		ArtMinimasterIptAttDetailsDto minimasterDto = new ArtMinimasterIptAttDetailsDto();

		List<MasterDto> masterGender = masterDataService.getGender();
		minimasterDto.setMasterGender(masterGender);

		List<LocationDto> masterState = addressService.getStateList();
		minimasterDto.setMasterState(masterState);

		List<MasterDto> beneficiaryCategory = masterDataService.getMasterBeneficiaryCategory();
		minimasterDto.setBeneficiaryCategory(beneficiaryCategory);

		List<MasterDto> foursScreening = masterDataService.getFourSScreening();
		minimasterDto.setFoursScreening(foursScreening);

		List<MasterDto> tbTestType = masterDataService.getTbTestType();
		minimasterDto.setTbTestType(tbTestType);

		List<MasterDto> iptStatus = masterDataService.getIptStatus();
		minimasterDto.setIptStatus(iptStatus);

		List<MasterDto> tbResult = masterDataService.getTbResult();
		minimasterDto.setTbResult(tbResult);

		List<MasterDto> tbRegimen = masterDataService.getTbRegimen();
		minimasterDto.setTbRegimen(tbRegimen);

		List<MasterDto> tbTreatmentStatus = masterDataService.getTbTreatmentStatus();
		minimasterDto.setTbTreatmentStatus(tbTreatmentStatus);

		List<MasterDto> diagnosedBy = masterDataService.getDiagnosedBy();
		minimasterDto.setDiagnosedBy(diagnosedBy);

		List<MasterDto> treatmentUnder = masterDataService.getTreatmentUnder();
		minimasterDto.setTreatmentUnder(treatmentUnder);

		List<MasterDto> treatmentOutcome = masterDataService.getTreatmentOutcome();
		minimasterDto.setTreatmentOutcome(treatmentOutcome);

		return minimasterDto;
	}

	/**
	 * @return
	 */

	public ArtMiniMasterReferToDto getReferToCoeAndArtPlus() {
		ArtMiniMasterReferToDto artMiniMasterDto = new ArtMiniMasterReferToDto();

		List<MasterDto> masterAdherenceRemarks = masterDataService.getAdherenceRemarks();
		artMiniMasterDto.setMasterAdherenceRemarks(masterAdherenceRemarks);

		List<MasterDto> masterReferalType = masterDataService.getReferralType();
		artMiniMasterDto.setMasterReferralType(masterReferalType);

		List<MasterDto> masterSacepReferralReason = masterDataService.getSacepReferralReason();
		artMiniMasterDto.setMasterSACEPReferralReason(masterSacepReferralReason);

		return artMiniMasterDto;
	}
}
