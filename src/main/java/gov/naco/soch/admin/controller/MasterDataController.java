package gov.naco.soch.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.dto.BigPicMiniMasterDto;
import gov.naco.soch.admin.service.CommonAdminService;
import gov.naco.soch.admin.service.MasterDataService;
import gov.naco.soch.dto.DesignationDto;
import gov.naco.soch.dto.LoginResponseDto;
import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.dto.NacoProfileDto;
import gov.naco.soch.dto.SacsFacilityDto;
import gov.naco.soch.util.UserUtils;
import gov.naco.soch.admin.service.FacilityService;

//Controller class for API call

@RestController
@RequestMapping("/master")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MasterDataController {

	@Autowired
	MasterDataService masterDataService;
	
	@Autowired
	private CommonAdminService commonService;
	
	@Autowired
	private FacilityService facilityService;
	
	private static final Logger logger = LoggerFactory.getLogger(MasterDataController.class);
	
	public MasterDataController() {
	}

	@GetMapping("/labTypes")
	public @ResponseBody List<MasterDto> getAllLabTypes() {
		logger.debug("Entering into method getAllLabTypes");
		return masterDataService.getAllLabTypes();
	}

	@GetMapping("/batchNoOptions")
	public @ResponseBody List<MasterDto> getAllBatchNoOptions() {
		logger.debug("Entering into method getAllBatchNoOptions");
		return masterDataService.getAllBatchNoOptions();
	}

	@GetMapping("/regimenAdultPedOptions")
	public @ResponseBody List<MasterDto> getRegimenAdultPedOptions() {
		logger.debug("Entering into method getRegimenAdultPedOptions");
		return masterDataService.getRegimenAdultPedOptions();
	}

	@GetMapping("/masterDataOptions")
	public @ResponseBody List<MasterDto> getMasterDataOptions(@RequestParam String masterType) {
		logger.debug("Entering into method getMasterDataOptions");
		return masterDataService.getMasterDataOptions(masterType.trim());
	}

	@GetMapping("/regimenLineOptions")
	public @ResponseBody List<MasterDto> getRegimenLineOptions() {
		logger.debug("Entering into method getRegimenLineOptions");
		return masterDataService.getRegimenLineOptions();
	}

	@GetMapping("/sampleStatus")
	public @ResponseBody List<MasterDto> getSampleStatus() {
		logger.debug("Entering into method getSampleStatus");
		return masterDataService.getSampleStatus();
	}

	@GetMapping("/batchStatus")
	public @ResponseBody List<MasterDto> getBatchStatus() {
		logger.debug("Entering into method getBatchStatus");
		return masterDataService.getBatchStatus();
	}

	@GetMapping("/resultStatus")
	public @ResponseBody List<MasterDto> getResultStatus() {
		logger.debug("Entering into method getResultStatus");
		return masterDataService.getResultStatus();
	}

	@GetMapping("/sampleRemark")
	public @ResponseBody List<MasterDto> getSampleRemark() {
		logger.debug("Entering into method getSampleRemark");
		return masterDataService.getSampleRemark();
	}

	@GetMapping("/resultType")
	public @ResponseBody List<MasterDto> getResultType(@RequestParam(required = false) String labType) {
		logger.debug("Entering into method getResultType");
		return masterDataService.getResultType(labType);
	}

	@GetMapping("/test")
	public @ResponseBody List<MasterDto> getTests() {
		logger.debug("Entering into method getTests");
		return masterDataService.getTests();
	}

	@GetMapping("/testType")
	public @ResponseBody List<MasterDto> getTestType(@RequestParam(required = false) String labType,
			@RequestParam(required = false) String test) {
		logger.debug("Entering into method getTestType");
		return masterDataService.getTestType(labType, test);
	}

	@GetMapping("/indentreasons")
	public @ResponseBody List<MasterDto> getIndentReasons() {
		logger.debug("Entering into method getIndentReasons");
		return masterDataService.getIndentReasons();
	}

	@GetMapping("/indentstatus")
	public @ResponseBody List<MasterDto> getIndentStatus() {
		logger.debug("Entering into method getIndentStatus");
		return masterDataService.getIndentStatus();
	}

	@GetMapping("/clientstatus")
	public @ResponseBody List<MasterDto> getClientStatus() {
		logger.debug("Entering into method getClientStatus");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getClientStatus();
		return masterDtoList;

	}
/*
	@GetMapping("/clientstatus/ticore")
	public @ResponseBody List<MasterDto> getClientStatusTiCore() {
		logger.debug("Entering into method getClientStatusTiCore");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		Long facilityTypeId = (long) 7;
		masterDtoList = masterDataService.getClientStatusByFacilityTypeId(facilityTypeId);
		return masterDtoList;
	}*/
	
	@GetMapping("/clientstatus/ticore")
	public @ResponseBody List<MasterDto> getClientStatusTiCore() {
		logger.debug("Entering into method getClientStatusTiCore");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getClientStatusTiCore();
		return masterDtoList;
	}
	
	@PostMapping("/insert/clientstatus/ticore")
	public @ResponseBody MasterDto saveClientStatusTiCore(@RequestBody MasterDto masterDto) {
		logger.info("saveClientStatusTiCore method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterClientStatusTiCoreCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveClientStatusTiCore(masterDto);
	}
	

	@GetMapping("/clientstatus/tiost")
	public @ResponseBody List<MasterDto> getClientStatusTiOst() {
		logger.debug("Entering into method getClientStatusTiOst");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		Long facilityTypeId = (long) 10;
		masterDtoList = masterDataService.getClientStatusByFacilityTypeId(facilityTypeId);
		return masterDtoList;
	}

	@GetMapping("/maritalstatus")
	public @ResponseBody List<MasterDto> getMaritalStatus() {
		logger.debug("Entering into method getMaritalStatus");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getMaritalStatus();
		return masterDtoList;

	}

	@PostMapping("/insert/maritalstatus")
	public @ResponseBody MasterDto saveMaritalStatus(@RequestBody MasterDto masterDto) {
		logger.info("saveMaritalStatus method called with parameters->{}", masterDto);
		masterDto = masterDataService.saveMaritalStatus(masterDto);
		try {
			commonService.clearCache("AllMaritalStatusCache", null);
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDto;

	}

	@GetMapping("/occupation")
	public @ResponseBody List<MasterDto> getMasterOccupation() {
		logger.debug("Entering into method getMasterOccupation");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getMasterOccupation();
		return masterDtoList;
	}

	@PostMapping("/insert/occupation")
	public @ResponseBody MasterDto saveOccupation(@RequestBody MasterDto masterDto) {
		logger.info("saveOccupation method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterOccupationCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveOccupation(masterDto);
	}

//	@GetMapping("/employmentstatus")
//	public @ResponseBody List<MasterDto> getMasterEmploymentStatus() {
//		logger.debug("Entering into method getMasterEmploymentStatus");
//		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
//		masterDtoList = masterDataService.getMasterEmploymentStatus();
//		return masterDtoList;
//	}
//
//	@PostMapping("/insert/empstatus")
//	public @ResponseBody MasterDto saveEmploymentStatus(@RequestBody MasterDto masterDto) {
//		logger.info("saveEmploymentStatus method called with parameters->{}", masterDto);
//		try {
//			commonService.clearCache("AllMasterEmploymentStatusCache", null);	
//		} catch (Exception e) {
//			logger.error("Exception in clearing cache !",e);
//		}
//		return masterDataService.saveEmploymentStatus(masterDto);
//	}

	@GetMapping("/educationlevel")
	public @ResponseBody List<MasterDto> getMasterEducationLevel() {
		logger.debug("Entering into method getMastereducationlevel");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getMasterEducationLevel();
		return masterDtoList;
	}

	@PostMapping("/insert/educationlevel")
	public @ResponseBody MasterDto saveEducationlevel(@RequestBody MasterDto masterDto) {
		logger.info("saveEducationLevel method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterEducationLevelCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveEducationLevel(masterDto);

	}

	@GetMapping("/beneficiarycategory")
	public @ResponseBody List<MasterDto> getMasterBeneficiaryCategory() {
		logger.debug("Entering into method getMasterBeneficiaryCategory");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getMasterBeneficiaryCategory();
		return masterDtoList;
	}

	@PostMapping("/insert/beneficiarycategory")
	public @ResponseBody MasterDto saveBeneficiaryCategory(@RequestBody MasterDto masterDto) {
		logger.info("saveBeneficiaryCategory method called with parameters->{}", masterDto);
		return masterDataService.saveBeneficiaryCategory(masterDto);

	}

	@GetMapping("/weightband")
	public @ResponseBody List<MasterDto> getMasterWeightBand() {
		logger.debug("Entering into method getMasterWeightBand");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getMasterWeightBand();
		return masterDtoList;
	}

	@GetMapping("/commoditytype")
	public @ResponseBody List<MasterDto> getCommodityType() {
		logger.debug("Entering into method getCommodityType");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getCommodityType();
		return masterDtoList;
	}
	
	@PostMapping("/insert/commoditytype")
	public @ResponseBody MasterDto saveCommodityType(@RequestBody MasterDto masterDto) {
		logger.info("saveCommodityType method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterCommodityTypeCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveCommodityType(masterDto);

	}

	@GetMapping("/drugline")
	public @ResponseBody List<MasterDto> getDrugLine() {
		logger.debug("Entering into method getDrugLine");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getDrugLine();
		return masterDtoList;
	}

	@GetMapping("/recepienttype")
	public @ResponseBody List<MasterDto> getRecepientType() {
		logger.debug("Entering into method getRecepientType");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getRecepientType();
		return masterDtoList;
	}

	@GetMapping("/vaccinetype")
	public @ResponseBody List<MasterDto> getVaccineType() {
		logger.debug("Entering into method getVaccineType");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getVaccineType();
		return masterDtoList;
	}

	@GetMapping("/productdosagecategory")
	public @ResponseBody List<MasterDto> getProductDosageCategory() {
		logger.debug("Entering into method getVaccineType");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getProductDosageCategory();
		return masterDtoList;
	}

	@GetMapping("/entrypoint")
	public @ResponseBody List<MasterDto> getEntryPoint() {
		logger.debug("Entering into method getEntryPoint");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getEntryPoint();
		return masterDtoList;
	}

	@GetMapping("/riskfactor")
	public @ResponseBody List<MasterDto> getRiskFactor() {
		logger.debug("Entering into method getRiskFactor");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getRiskFactor();
		return masterDtoList;
	}

	@GetMapping("/monthlyincome")
	public @ResponseBody List<MasterDto> getMonthlyIncome() {
		logger.debug("Entering into method getRiskFactor");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getMonthlyIncome();
		return masterDtoList;
	}

	@GetMapping("/gender")
	public @ResponseBody List<MasterDto> getGender() {
		logger.debug("Entering into method getGender");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getGender();
		return masterDtoList;
	}

	@GetMapping("/beneficiaryactivitystatus")
	public @ResponseBody List<MasterDto> getBeneficiaryActivityStatus() {
		logger.debug("Entering into method getGender");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getBeneficiaryActivityStatus();
		return masterDtoList;
	}

	@GetMapping("/tbregimen")
	public @ResponseBody List<MasterDto> getTbRegimen() {
		logger.debug("Entering into method getTbRegimen");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getTbRegimen();
		return masterDtoList;
	}

	@GetMapping("/rxoutcome")
	public @ResponseBody List<MasterDto> getRxOutcome() {
		logger.debug("Entering into method getRxOutcome");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getRxOutcome();
		return masterDtoList;
	}

	@GetMapping("/opportunisticinfections")
	public @ResponseBody List<MasterDto> getOpportunisticInfections() {
		logger.debug("Entering into method getOpportunisticInfections");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getOpportunisticInfections();
		return masterDtoList;
	}

	@GetMapping("/sideeffects")
	public @ResponseBody List<MasterDto> getSideEffects() {
		logger.debug("Entering into method getSideEffects");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getSideEffects();
		return masterDtoList;
	}

	@GetMapping("/substitutereasons")
	public @ResponseBody List<MasterDto> getSubstituteReasons() {
		logger.debug("Entering into method getSubstituteReasons");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getSubstituteReasons();
		return masterDtoList;
	}

	@GetMapping("/stopreasons")
	public @ResponseBody List<MasterDto> getStopReasons() {
		logger.debug("Entering into method getStopReasons");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getStopReasons();
		return masterDtoList;
	}

	@GetMapping("/pepprescription")
	public @ResponseBody List<MasterDto> getPepPrescription() {
		logger.debug("Entering into method getPepPrescription");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getPepPrescription();
		return masterDtoList;
	}

	@GetMapping("/baselinetests")
	public @ResponseBody List<MasterDto> getBaselineTests() {
		logger.debug("Entering into method getBaselineTests");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getBaselineTests();
		return masterDtoList;
	}

	@GetMapping("/diseaseclass")
	public @ResponseBody List<MasterDto> getDiseaseClass() {
		logger.debug("Entering into method getDiseaseClass");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getDiseaseClass();
		return masterDtoList;
	}

	@GetMapping("/functionalstatus")
	public @ResponseBody List<MasterDto> getFunctionalStatus() {
		logger.debug("Entering into method getFunctionalStatus");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getFunctionalStatus();
		return masterDtoList;
	}

	@GetMapping("/clinicalstage")
	public @ResponseBody List<MasterDto> getClinicalStage() {
		logger.debug("Entering into method getClinicalStage");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getClinicalStage();
		return masterDtoList;
	}

	@GetMapping("/placeofart")
	public @ResponseBody List<MasterDto> getPlaceOfArt() {
		logger.debug("Entering into method getPlaceOfArt");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getPlaceOfArt();
		return masterDtoList;
	}

	@GetMapping("/hrgprimary")
	public @ResponseBody List<MasterDto> getHrgPrimary() {
		logger.debug("Entering into method getHrgPrimary");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getHrgPrimary();
		return masterDtoList;
	}

	@GetMapping("/hrgsecondary")
	public @ResponseBody List<MasterDto> getHrgSecondary() {
		logger.debug("Entering into method getHrgPrimary");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getHrgSecondary();
		return masterDtoList;
	}

	@PostMapping("/insert/hrgsecondary")
	public @ResponseBody MasterDto saveHrgSecondary(@RequestBody MasterDto masterDto) {
		logger.info("saveHrgSecondary method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterHrgSecondaryCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveHrgSecondary(masterDto);
	}
	
	
	@GetMapping("/subcategory")
	public @ResponseBody List<MasterDto> getSubcategory() {
		logger.debug("Entering into method getSubcategory");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getSubcategory();
		return masterDtoList;
	}

	@GetMapping("/oststatusbeneficiary")
	public @ResponseBody List<MasterDto> getOstStatusBeneficiary() {
		logger.debug("Entering into method getOstStatusBeneficiary");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getOstStatusBeneficiary();
		return masterDtoList;
	}

	@GetMapping("/relations")
	public @ResponseBody List<MasterDto> getRelations() {
		logger.debug("Entering into method getRelations");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getRelations();
		return masterDtoList;
	}

	@GetMapping("/hivstatus")
	public @ResponseBody List<MasterDto> getHivStatus() {
		logger.debug("Entering into method getHivStatus");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getHivStatus();
		return masterDtoList;
	}

	@GetMapping("/beneficiaryarttransferredfrom")
	public @ResponseBody List<MasterDto> getBeneficiaryArtTransferredFrom() {
		logger.debug("Entering into method getBeneficiaryArtTransferredFrom");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getBeneficiaryArtTransferredFrom();
		return masterDtoList;
	}

	@GetMapping("/habitsalcoholuse")
	public @ResponseBody List<MasterDto> getHabitsAlcoholUse() {
		logger.debug("Entering into method getHabitsAlcoholUse");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getHabitsAlcoholUse();
		return masterDtoList;
	}

	@GetMapping("/habitssmoking")
	public @ResponseBody List<MasterDto> getHabitsSmoking() {
		logger.debug("Entering into method getHabitsSmoking");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getHabitsSmoking();
		return masterDtoList;
	}

	@GetMapping("/tobaccouse")
	public @ResponseBody List<MasterDto> getTobaccoUse() {
		logger.debug("Entering into method getTobaccoUse");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getTobaccoUse();
		return masterDtoList;
	}

	@GetMapping("/counsellingtype")
	public @ResponseBody List<MasterDto> getCounsellingType() {
		logger.debug("Entering into method getMasterCounsellingType");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getCounsellingType();
		return masterDtoList;
	}
	
	@PostMapping("/insert/counsellingtype")
	public @ResponseBody MasterDto saveCounsellingType(@RequestBody MasterDto masterDto) {
		logger.info("saveCounsellingType method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterCounsellingTypeCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveCounsellingType(masterDto);
	}


	@GetMapping("/healthstatus")
	public @ResponseBody List<MasterDto> getHealthStatus() {
		logger.debug("Entering into method getHealthStatus");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getHealthStatus();
		return masterDtoList;
	}

	@GetMapping("/drugs")
	public @ResponseBody List<MasterDto> getDrugs() {
		logger.debug("Entering into method getDrugs");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getDrugs();
		return masterDtoList;
	}

	@GetMapping("/syphilisstatus")
	public @ResponseBody List<MasterDto> getSyphilisStatus() {
		logger.debug("Entering into method getSyphilisStatus");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getSyphilisStatus();
		return masterDtoList;
	}

	@GetMapping("/screeningstatus")
	public @ResponseBody List<MasterDto> getScreeningStatus() {
		logger.debug("Entering into method getScreeningStatus");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getScreeningStatus();
		return masterDtoList;
	}

	@GetMapping("/ostassementstatus")
	public @ResponseBody List<MasterDto> getOstAssementStatus() {
		logger.debug("Entering into method getOstAssementStatus");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getOstAssementStatus();
		return masterDtoList;
	}

	@GetMapping("/diagnosistype")
	public @ResponseBody List<MasterDto> getDiagnosisType() {
		logger.debug("Entering into method getDiagnosisType");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getDiagnosisType();
		return masterDtoList;
	}

	@GetMapping("/kittype")
	public @ResponseBody List<MasterDto> getKitType() {
		logger.debug("Entering into method getKitType");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getKitType();
		return masterDtoList;
	}

	@GetMapping("/kittype/{diagnosisId}")
	public @ResponseBody List<MasterDto> getKitTypeBydiagnosisId(@PathVariable("diagnosisId") Long diagnosisId) {
		logger.debug("Entering into method getKitType");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getKitTypeByDiagnosisId(diagnosisId);
		return masterDtoList;
	}

	@GetMapping("/treatmenttype")
	public @ResponseBody List<MasterDto> getTreatmentType() {
		logger.debug("Entering into method getTreatmentType");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getTreatmentType();
		return masterDtoList;
	}

	@GetMapping("/referralstatus")
	public @ResponseBody List<MasterDto> getReferralStatus() {
		logger.debug("Entering into method getReferralStatus");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getReferralStatus();
		return masterDtoList;
	}

	@GetMapping("/yesorno")
	public @ResponseBody List<MasterDto> getYesOrNo() {
		logger.debug("Entering into method getYesOrNo");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getYesOrNo();
		return masterDtoList;
	}

	@GetMapping("/status")
	public @ResponseBody List<MasterDto> getStatus() {
		logger.debug("Entering into method getStatus");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getStatus();
		return masterDtoList;
	}

	@GetMapping("/hbvstatus")
	public @ResponseBody List<MasterDto> getHbvStatus() {
		logger.debug("Entering into method getHbvStatus");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getHbvStatus();
		return masterDtoList;
	}

	@GetMapping("/otherailments")
	public @ResponseBody List<MasterDto> getOtherAilments() {
		logger.debug("Entering into method getOtherAilments");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getOtherAilments();
		return masterDtoList;
	}

	@GetMapping("/contraception")
	public @ResponseBody List<MasterDto> getContraception() {
		logger.debug("Entering into method getContraception");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getContraception();
		return masterDtoList;
	}

	@GetMapping("/obstetrichistory")
	public @ResponseBody List<MasterDto> getObstetricHistory() {
		logger.debug("Entering into method getObstetricHistory");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getObstetricHistory();
		return masterDtoList;
	}

	@GetMapping("/purposes")
	public @ResponseBody List<MasterDto> getPurposes() {
		logger.debug("Entering into method getPurposes()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getPurposes();
		return masterDtoList;
	}

	@GetMapping("/exposureseverity")
	public @ResponseBody List<MasterDto> getExposureSeverity() {
		logger.debug("Entering into method getExposureSeverity()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getExposureSeverity();
		return masterDtoList;
	}

	@GetMapping("/injuryexposuremode")
	public @ResponseBody List<MasterDto> getInjuryExposureMode() {
		logger.debug("Entering into method getInjuryExposureMode()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getInjuryExposureMode();
		return masterDtoList;
	}

	@GetMapping("/tbtreatmentstatus")
	public @ResponseBody List<MasterDto> getTbTreatmentStatus() {
		logger.debug("Entering into method getTbTreatmentStatus()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getTbTreatmentStatus();
		return masterDtoList;
	}

	@GetMapping("/hivtype")
	public @ResponseBody List<MasterDto> getHivType() {
		logger.debug("Entering into method getHivType()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getHivType();
		return masterDtoList;
	}

	@GetMapping("/tiosttype")
	public @ResponseBody List<MasterDto> getTiOstType() {
		logger.debug("Entering into method getTiOstType()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getTiOstType();
		return masterDtoList;
	}

	@GetMapping("/primarydrug")
	public @ResponseBody List<MasterDto> PrimaryDrug() {
		logger.debug("Entering into method getPrimaryDrug()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getPrimaryDrug();
		return masterDtoList;
	}

	@PostMapping("/insert/primarydrug")
	public @ResponseBody MasterDto savePrimaryDrug(@RequestBody MasterDto masterDto) {
		logger.info("savePrimaryDrug method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterPrimaryDrugCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.savePrimaryDrug(masterDto);
	}
	
	@GetMapping("/drugcomplications")
	public @ResponseBody List<MasterDto> getDrugComplications() {
		logger.debug("Entering into method getDrugComplications()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getDrugComplications();
		return masterDtoList;
	}

	@GetMapping("/contacttype")
	public @ResponseBody List<MasterDto> getContactType() {
		logger.debug("Entering into method getContactType()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getContactType();
		return masterDtoList;
	}

	@PostMapping("/insert/contacttype")
	public @ResponseBody MasterDto saveContactType(@RequestBody MasterDto masterDto) {
		logger.info("saveContactType method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterContactTypeCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveContactType(masterDto);
	}

	@GetMapping("/infectiontype")
	public @ResponseBody List<MasterDto> getInfectionType() {
		logger.debug("Entering into method getInfectionType()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getInfectionType();
		return masterDtoList;
	}

	@GetMapping("/tbscreeningstatus")
	public @ResponseBody List<MasterDto> getTbScreeningStatus() {
		logger.debug("Entering into method getTbScreeningStatus()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getTbScreeningStatus();
		return masterDtoList;
	}

	@GetMapping("/followuptype")
	public @ResponseBody List<MasterDto> getFollowUpType() {
		logger.debug("Entering into method getFollowUpType()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getFollowUpType();
		return masterDtoList;
	}

	@GetMapping("/registrationdoneat")
	public @ResponseBody List<MasterDto> getRegistrationDoneAt() {
		logger.debug("Entering into method getRegistrationDoneAt()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getRegistrationDoneAt();
		return masterDtoList;
	}

	@GetMapping("/stayingwith")
	public @ResponseBody List<MasterDto> getStayingWith() {
		logger.debug("Entering into method getStayingWith()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getStayingWith();
		return masterDtoList;
	}

	@GetMapping("/mobilitytype")
	public @ResponseBody List<MasterDto> getMobilityType() {
		logger.debug("Entering into method getMobilityType(()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getMobilityType();
		return masterDtoList;
	}

	@GetMapping("/destinationduration")
	public @ResponseBody List<MasterDto> getDestinationDuration() {
		logger.debug("Entering into method getDestinationDuration()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getDestinationDuration();
		return masterDtoList;
	}

	@GetMapping("/referredfrom")
	public @ResponseBody List<MasterDto> getReferredfrom() {
		logger.debug("Entering into method getReferredfrom()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getReferredfrom();
		return masterDtoList;
	}

	@GetMapping("/referredby")
	public @ResponseBody List<MasterDto> getReferredby() {
		logger.debug("Entering into method getReferredby()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getReferredby();
		return masterDtoList;
	}

	@GetMapping("/pregnancytypecase")
	public @ResponseBody List<MasterDto> getPregnancyTypeCase() {
		logger.debug("Entering into method getPregnancyTypeCase()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getPregnancyTypeCase();
		return masterDtoList;
	}

	@GetMapping("/arvprophylaxis")
	public @ResponseBody List<MasterDto> getArvProphylaxis() {
		logger.debug("Entering into method getArvProphylaxis()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getArvProphylaxis();
		return masterDtoList;
	}

	@GetMapping("/infantbreastfeed")
	public @ResponseBody List<MasterDto> getInfantBreastFeed() {
		logger.debug("Entering into method getInfantBreastFeed()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getInfantBreastFeed();
		return masterDtoList;
	}

	@GetMapping("/routeoftransmission")
	public @ResponseBody List<MasterDto> getRouteOfTransmission() {
		logger.debug("Entering into method getRouteOfTransmission()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getRouteOfTransmission();
		return masterDtoList;
	}

	@GetMapping("/tbresult")
	public @ResponseBody List<MasterDto> getTbResult() {
		logger.debug("Entering into method getTbResult()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getTbResult();
		return masterDtoList;
	}

	@GetMapping("/nottestingreason")
	public @ResponseBody List<MasterDto> getNotTestingReason() {
		logger.debug("Entering into method getNotTestingReason()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getNotTestingReason();
		return masterDtoList;
	}

	@GetMapping("/ictcbeneficiarystatus")
	public @ResponseBody List<MasterDto> getIctcBeneficiaryStatus() {
		logger.debug("Entering into method getIctcBeneficiaryStatus()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getIctcBeneficiaryStatus();
		return masterDtoList;
	}

	@GetMapping("/ictcfollowuptype")
	public @ResponseBody List<MasterDto> getictcFollowUpType() {
		logger.debug("Entering into method getictcFollowUpType()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getictcFollowUpType();
		return masterDtoList;
	}

	@GetMapping("/partnerstatus")
	public @ResponseBody List<MasterDto> getPartnerStatus() {
		logger.debug("Entering into method getPartnerStatus()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getPartnerStatus();
		return masterDtoList;
	}

	@GetMapping("/deliveryoutcome")
	public @ResponseBody List<MasterDto> getDeliveryOutcome() {
		logger.debug("Entering into method getDeliveryOutcome()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getDeliveryOutcome();
		return masterDtoList;
	}

	@GetMapping("/samplecollectionstatus")
	public @ResponseBody List<MasterDto> getSampleCollectionStatus() {
		logger.debug("Entering into method getSampleCollectionStatus()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getSampleCollectionStatus();
		return masterDtoList;
	}

	@GetMapping("/switchreasons")
	public @ResponseBody List<MasterDto> getSwitchReasons() {
		logger.debug("Entering into method getSwitchReasons()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getSwitchReasons();
		return masterDtoList;
	}

	@GetMapping("/artbeneficiarystatus")
	public @ResponseBody List<MasterDto> getArtBeneficiaryStatus() {
		logger.debug("Entering into method getArtBeneficiaryStatus()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getArtBeneficiaryStatus();
		return masterDtoList;
	}

	@GetMapping("/treatmentline")
	public @ResponseBody List<MasterDto> getTreatmentLine() {
		logger.debug("Entering into method getTreatmentLine()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getTreatmentLine();
		return masterDtoList;
	}

	@GetMapping("/foursscreening")
	public @ResponseBody List<MasterDto> getFourSScreening() {
		logger.debug("Entering into method getFourSScreening()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getFourSScreening();
		return masterDtoList;
	}

	@GetMapping("/organisationtype")
	public @ResponseBody List<MasterDto> getOrganisationType() {
		logger.debug("Entering into method getOrganisationType()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getOrganisationType();
		return masterDtoList;
	}

	@GetMapping("/artstayingwith")
	public @ResponseBody List<MasterDto> getArtStayingWith() {
		logger.debug("Entering into method getArtStayingWith()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getArtStayingWith();
		return masterDtoList;
	}

	@GetMapping("/birthhistory")
	public @ResponseBody List<MasterDto> getBirthHistory() {
		logger.debug("Entering into method getBirthHistory()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getBirthHistory();
		return masterDtoList;
	}

	@GetMapping("/infantfeeding")
	public @ResponseBody List<MasterDto> getInfantFeeding() {
		logger.debug("Entering into method getInfantFeeding()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getInfantFeeding();
		return masterDtoList;
	}

	@GetMapping("/vaccinestage")
	public @ResponseBody List<MasterDto> getVaccineStage() {
		logger.debug("Entering into method getVaccineStage()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getVaccineStage();
		return masterDtoList;
	}

	@GetMapping("/vitaminaage")
	public @ResponseBody List<MasterDto> getVitaminAAge() {
		logger.debug("Entering into method getVitaminAAge()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getVitaminAAge();
		return masterDtoList;
	}

	@GetMapping("/investigation")
	public @ResponseBody List<MasterDto> getInvestigation() {
		logger.debug("Entering into method getInvestigation()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getInvestigation();
		return masterDtoList;
	}

	@GetMapping("/arttreatmentstatus")
	public @ResponseBody List<MasterDto> getArtTreatmentStatus() {
		logger.debug("Entering into method getArtTreatmentStatus()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getArtTreatmentStatus();
		return masterDtoList;
	}

	@GetMapping("/iptstatus")
	public @ResponseBody List<MasterDto> getIptStatus() {
		logger.debug("Entering into method getIptStatus()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getIptStatus();
		return masterDtoList;
	}

	@GetMapping("/fourssymptom")
	public @ResponseBody List<MasterDto> getFoursSymptom() {
		logger.debug("Entering into method getFoursSymptom()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getFoursSymptom();
		return masterDtoList;
	}

	@GetMapping("/hcvstatus")
	public @ResponseBody List<MasterDto> getHcvStatus() {
		logger.debug("Entering into method getHcvStatus()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getHcvStatus();
		return masterDtoList;
	}

	@GetMapping("/hivexposurecode")
	public @ResponseBody List<MasterDto> getHivExposureCode() {
		logger.debug("Entering into method getHivExposureCode()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getHivExposureCode();
		return masterDtoList;
	}

	@GetMapping("/hivsourcestatus")
	public @ResponseBody List<MasterDto> getHivSourceStatus() {
		logger.debug("Entering into method getHivSourceStatus()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getHivSourceStatus();
		return masterDtoList;
	}

	@GetMapping("/diagnosedby")
	public @ResponseBody List<MasterDto> getDiagnosedBy() {
		logger.debug("Entering into method getDiagnosedBy()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getDiagnosedBy();
		return masterDtoList;
	}

	@GetMapping("/treatmentunder")
	public @ResponseBody List<MasterDto> getTreatmentUnder() {
		logger.debug("Entering into method getTreatmentUnder()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getTreatmentUnder();
		return masterDtoList;
	}

	@GetMapping("/dnapcrresult")
	public @ResponseBody List<MasterDto> getDnaPcrResult() {
		logger.debug("Entering into method getDnaPcrResult()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getDnaPcrResult();
		return masterDtoList;
	}

	@GetMapping("/trainingtype")
	public @ResponseBody List<MasterDto> getTrainingType() {
		logger.debug("Entering into method getTrainingType()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getTrainingType();
		return masterDtoList;
	}
	
	@PostMapping("/insert/trainingtype")
	public @ResponseBody MasterDto saveTrainingType(@RequestBody MasterDto masterDto) {
		logger.info("saveTrainingType method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterTrainingTypeCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveTrainingType(masterDto);

	}
	

	@GetMapping("/guardiancaregiver")
	public @ResponseBody List<MasterDto> getGuardianCaregiver() {
		logger.debug("Entering into method getGuardianCaregiver()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getGuardianCaregiver();
		return masterDtoList;
	}

	@GetMapping("/vaccinetypebystage/{stageId}")
	public @ResponseBody List<MasterDto> getVaccineTypeByStage(@PathVariable("stageId") Long stageId) {
		logger.debug("Entering into method getVaccineTypeByStage()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getVaccineTypeByStage(stageId);
		return masterDtoList;
	}

	@GetMapping("/dnapcrtest")
	public @ResponseBody List<MasterDto> getDnaPcrTest() {
		logger.debug("Entering into method getDnaPcrTest()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getDnaPcrTest();
		return masterDtoList;
	}

	@GetMapping("/tbtype")
	public @ResponseBody List<MasterDto> getTbType() {
		logger.debug("Entering into method getTbType()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getTbType();
		return masterDtoList;
	}

	@GetMapping("/tbtesttype")
	public @ResponseBody List<MasterDto> getTbTestType() {
		logger.debug("Entering into method getTbTestType()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getTbTestType();
		return masterDtoList;
	}

	@GetMapping("/socialwelfare")
	public @ResponseBody List<MasterDto> getSocialWelfare() {
		logger.debug("Entering into method getSocialWelfare()");
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getSocialWelfare(currentUser.getFacilityId());
		return masterDtoList;
	}

	@GetMapping("/treatmentoutcome")
	public @ResponseBody List<MasterDto> getTreatmentOutcome() {
		logger.debug("Entering into method getTreatmentOutcome()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getTreatmentOutcome();
		return masterDtoList;
	}

	@GetMapping("/deletereason")
	public @ResponseBody List<MasterDto> getDeleteReason() {
		logger.debug("Entering into method getDeleteReason()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getMasterDeleteReason();
		return masterDtoList;
	}

	@GetMapping("/art-delete-reason")
	public @ResponseBody List<MasterDto> getArtDeleteReason() {
		logger.debug("Entering into method getDeleteReason()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getMasterArtDeleteReason();
		return masterDtoList;
	}

	@GetMapping("/fswsubcategory")
	public @ResponseBody List<MasterDto> getFswSubCategory() {
		logger.debug("Entering into method getFswSubCategory()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getFswSubCategory();
		return masterDtoList;
	}
	
	@PostMapping("/insert/fswsubcategory")
	public @ResponseBody MasterDto saveFswSubCategory(@RequestBody MasterDto masterDto) {
		logger.info("saveFswSubCategory method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterFswSubCategoryCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveFswSubCategory(masterDto);
	}


	@GetMapping("/msmsubcategory")
	public @ResponseBody List<MasterDto> getMsmSubCategory() {
		logger.debug("Entering into method getMsmSubCategory()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getMsmSubCategory();
		return masterDtoList;
	}

	@PostMapping("/insert/msmsubcategory")
	public @ResponseBody MasterDto saveMsmSubCategory(@RequestBody MasterDto masterDto) {
		logger.info("saveMsmSubCategory method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterMsmSubCategoryCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveMsmSubCategory(masterDto);
	}
	
	@GetMapping("/tgsubcategory")
	public @ResponseBody List<MasterDto> getTgSubCategory() {
		logger.debug("Entering into method getTgSubCategory()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getTgSubCategory();
		return masterDtoList;
	}
	
	@PostMapping("/insert/tgsubcategory")
	public @ResponseBody MasterDto saveTgSubCategory(@RequestBody MasterDto masterDto) {
		logger.info("saveTgSubCategory method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterTgSubCategoryCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveTgSubCategory(masterDto);
	}

	@GetMapping("/idusubcategory")
	public @ResponseBody List<MasterDto> getIduSubCategory() {
		logger.debug("Entering into method getIduSubCategory()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getIduSubCategory();
		return masterDtoList;
	}
	
	
	@PostMapping("/insert/idusubcategory")
	public @ResponseBody MasterDto saveIduSubCategory(@RequestBody MasterDto masterDto) {
		logger.info("saveIduSubCategory method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterIduSubCategoryCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveIduSubCategory(masterDto);
	}

	@GetMapping("/producttype")
	public @ResponseBody List<MasterDto> getProductType() {
		logger.debug("Entering into method getProductType()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getProductType();
		return masterDtoList;
	}
	
	@PostMapping("/insert/producttype")
	public @ResponseBody MasterDto saveProductType(@RequestBody MasterDto masterDto) {
		logger.info("saveProductType method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterProductTypeCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveProductType(masterDto);

	}
	
	@GetMapping("/machine")
	public @ResponseBody List<MasterDto> getMachine() {
		logger.debug("Entering into method getMachine()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getMachine();
		return masterDtoList;
	}

	@PostMapping("/insert/machine")
	public @ResponseBody MasterDto saveMachine(@RequestBody MasterDto masterDto) {
		logger.info("saveMachine method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterMachineCache", null);
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveMachine(masterDto);

	}
	
	@GetMapping("/facilitytype")
	public @ResponseBody List<MasterDto> getFacilityType() {
		logger.debug("Entering into method getFacilityType()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getFacilityType();
		return masterDtoList;
	}
	

	@PostMapping("/insert/facilitytype")
	public @ResponseBody MasterDto saveFacilityType(@RequestBody MasterDto masterDto) {
		logger.info("saveFacilityType method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterFacilityTypeCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveFacilityType(masterDto);

	}
	
	@GetMapping("/productuom")
	public @ResponseBody List<MasterDto> getProductUom() {
		logger.debug("Entering into method getProductUom()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getProductUom();
		return masterDtoList;
	}
	
	@GetMapping("/relationtype")
	public @ResponseBody List<MasterDto> getRelationType() {
		logger.debug("Entering into method getRelationType()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getRelationType();
		return masterDtoList;
	}
	
	@GetMapping("/occupationtrucker")
	public @ResponseBody List<MasterDto> getOccupationTrucker() {
		logger.debug("Entering into method getOccupationTrucker()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getOccupationTrucker();
		return masterDtoList;
	}
	
	@GetMapping("/migrantoccupation")
	public @ResponseBody List<MasterDto> getMigrantOccupation() {
		logger.debug("Entering into method getMigrantOccupation()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getMigrantOccupation();
		return masterDtoList;
	}
	
	
	@GetMapping("/hivscreeningstatus")
	public @ResponseBody List<MasterDto> getHivScreeningStatus() {
		logger.debug("Entering into method getHivScreeningStatus()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getHivScreeningStatus();
		return masterDtoList;
	}
	
	@GetMapping("/migrationpattern")
	public @ResponseBody List<MasterDto> getMigrationPattern() {
		logger.debug("Entering into method getMigrationPattern()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getMigrationPattern();
		return masterDtoList;
	}
	
	@GetMapping("/ipctopic")
	public @ResponseBody List<MasterDto> getIPCTopic() {
		logger.debug("Entering into method getIPCTopic()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getIPCTopic();
		return masterDtoList;
	}
	
	@GetMapping("/takehomedays")
	public @ResponseBody List<MasterDto> getTakeHomeDays() {
		logger.debug("Entering into method getTakeHomeDays()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getTakeHomeDays();
		return masterDtoList;
	}
	
	@PostMapping("/insert/takehomedays")
	public @ResponseBody MasterDto saveTakeHomeDays(@RequestBody MasterDto masterDto) {
		logger.info("saveTakeHomeDays method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterTakeHomeDaysCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveTakeHomeDays(masterDto);
	}
	
	@GetMapping("/relationship")
	public @ResponseBody List<MasterDto> getRelationship() {
		logger.debug("Entering into method getRelationship()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getRelationship();
		return masterDtoList;
	}
	
	@GetMapping("/ostassessmentreason")
	public @ResponseBody List<MasterDto> getOstAssessmentReason() {
		logger.debug("Entering into method getOstAssessmentReason()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getOstAssessmentReason();
		return masterDtoList;
	}
	
	@PostMapping("/insert/ostassessmentreason")
	public @ResponseBody MasterDto saveOstAssessmentReason(@RequestBody MasterDto masterDto) {
		logger.info("saveOstAssessmentReason method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterOstAssessmentReasonCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveOstAssessmentReason(masterDto);
	}
	
	
	@GetMapping("/followupby")
	public @ResponseBody List<MasterDto> getFollowupBy() {
		logger.debug("Entering into method getFollowupBy()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getFollowupBy();
		return masterDtoList;
	}
	
	@GetMapping("/bloodbankownedby")
	public @ResponseBody List<MasterDto> getBloodBankOwnedBy() {
		logger.debug("Entering into method getBloodBankOwnedBy()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getBloodBankOwnedBy();
		return masterDtoList;
	}
	
	@GetMapping("/bloodbanktype")
	public @ResponseBody List<MasterDto> getBloodbankType() {
		logger.debug("Entering into method getBloodbankType()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getBloodbankType();
		return masterDtoList;
	}
	
	
	
	/*
	 * private void clearCache(String cacheName) { LoginResponseDto currentUser =
	 * UserUtils.getLoggedInUserDetails(); RestTemplate restTemplate = new
	 * RestTemplate(); HttpHeaders headers = new HttpHeaders();
	 * headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	 * headers.setBearerAuth(currentUser.getToken()); HttpEntity<String> entity =
	 * new HttpEntity<>("body", headers); restTemplate.exchange(CACHE_CLEAR_ENDPOINT
	 * + "?cacheName=" + cacheName, HttpMethod.GET, entity, String.class); }
	 */
	@GetMapping("/artregimenaction")
	public @ResponseBody List<MasterDto> getAllMasterArtRegimenAction() {
		return masterDataService.getAllMasterArtRegimenAction();
	}
	
	//INVENTORY STATUS MASTERS
	
	@GetMapping("/indentstatusmaster")
	public @ResponseBody List<MasterDto> getIndentStatusMaster() {
		logger.debug("Entering into method getAllIndentStatus");
		return masterDataService.getAllIndentStatus();
	}
	
	@GetMapping("/contractstatus")
	public @ResponseBody List<MasterDto> getAllContractStatus() {
		logger.debug("Entering into method getAllContractStatus");
		return masterDataService.getAllContractStatus();
	}
	
	@GetMapping("/consignmentstatus")
	public @ResponseBody List<MasterDto> getAllConsignmentStatus() {
		logger.debug("Entering into method getAllConsignmentStatus");
		return masterDataService.getAllConsignmentStatus();
	}
	
	@GetMapping("/receiptstatus")
	public @ResponseBody List<MasterDto> getAllReceiptStatus() {
		logger.debug("Entering into method getAllReceiptStatus");
		return masterDataService.getAllReceiptStatus();
	}
	
	@GetMapping("/facilityconsignmentstatus")
	public @ResponseBody List<MasterDto> getAllFacilityConsignmentStatus() {
		logger.debug("Entering into method getAllFacilityConsignmentStatus");
		return masterDataService.getAllFacilityConsignmentStatus();
	}
	
	@GetMapping("/facilityreceiptstatus")
	public @ResponseBody List<MasterDto> getAllFacilityReceiptStatus() {
		logger.debug("Entering into method getAllFacilityReceiptStatus");
		return masterDataService.getAllFacilityReceiptStatus();
	}
	
	@GetMapping("/relocationrequeststatus")
	public @ResponseBody List<MasterDto> getAllRelocationRequestStatus() {
		logger.debug("Entering into method getAllRelocationRequestStatus");
		return masterDataService.getAllRelocationRequestStatus();
	}
	
	@GetMapping("/reconciliationstatus")
	public @ResponseBody List<MasterDto> getAllReconciliationStatus() {
		logger.debug("Entering into method getAllReconciliationStatus");
		return masterDataService.getAllReconciliationStatus();
	}
	
	@GetMapping("/opioidoverdosereasons")
	public @ResponseBody List<MasterDto> getOpioidOverdoseReasons() {
		logger.debug("Entering into method getOpioidOverdoseReasons");
		return masterDataService.getOpioidOverdoseReasons();
	}

	@PostMapping("/insert/opioidoverdosereasons")
	public @ResponseBody MasterDto saveOpioidOverdoseReasons(@RequestBody MasterDto masterDto) {
		logger.info("saveOpioidOverdoseReasons method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterOpioidOverdoseReasonsCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveOpioidOverdoseReasons(masterDto);
	}
	
	
//	@GetMapping("/stirtidiagnosistype")
//	public @ResponseBody List<MasterDto> getStiRtiDiagnosisType() {
//		logger.debug("Entering into method getStiRtiDiagnosisType");
//		return masterDataService.getStiRtiDiagnosisType();
//	}
//	
	@GetMapping("/clinicaltreatmenttype")
	public @ResponseBody List<MasterDto> getClinicalTreatmentType() {
		logger.debug("Entering into method getClinicalTreatmentType");
		return masterDataService.getClinicalTreatmentType();
	}
	
	@GetMapping("/treatmentabssesssites")
	public @ResponseBody List<MasterDto> getTreatmentAbssessSites() {
		logger.debug("Entering into method getTreatmentAbssessSites");
		return masterDataService.getTreatmentAbssessSites();
	}
	
	@GetMapping("/treatmentabssesstype")
	public @ResponseBody List<MasterDto> getTreatmentAbssessType() {
		logger.debug("Entering into method getTreatmentAbssessType");
		return masterDataService.getTreatmentAbssessType();
	}
	
	@GetMapping("/diagnosisrecordedat")
	public @ResponseBody List<MasterDto> getDiagnosisRecordedAt() {
		logger.debug("Entering into method getDiagnosisRecordedAt");
		return masterDataService.getDiagnosisRecordedAt();
	}
	
	@GetMapping("/multimonthdispensation")
	public @ResponseBody List<MasterDto> getMultimonthDispensation() {
		logger.debug("Entering into method getDiagnosisRecordedAt");
		return masterDataService.getMultimonthDispensation();
	}

	
	@GetMapping("/qmsteststatus")
	public @ResponseBody List<MasterDto> getQmsTestStatus() {
		logger.debug("Entering into method getQmsTestStatus");
		return masterDataService.getQmsTestStatus();
	}
	
	@GetMapping("/lactype")
	public @ResponseBody List<MasterDto> getLacType() {
		logger.debug("Entering into method getDiagnosisRecordedAt");
		return masterDataService.getLacType();
	}
	@GetMapping("/ostfollowupreason")
	public @ResponseBody List<MasterDto> getOstFollowupReason() {
		logger.debug("Entering into method getOstFollowupReason()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getOstFollowupReason();
		return masterDtoList;
	}
	
	@PostMapping("/insert/ostfollowupreason")
	public @ResponseBody MasterDto saveOstFollowupReason(@RequestBody MasterDto masterDto) {
		logger.info("saveOstFollowupReason method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterOstFollowupReasonCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveOstFollowupReason(masterDto);
	}
	
	@GetMapping("/ostfollowupsideeffect")
	public @ResponseBody List<MasterDto> getOstFollowupSideEffect() {
		logger.debug("Entering into method getOstFollowupSideEffect()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getOstFollowupSideEffect();
		return masterDtoList;
	}
	
	@PostMapping("/insert/ostfollowupsideeffect")
	public @ResponseBody MasterDto saveOstFollowupSideEffect(@RequestBody MasterDto masterDto) {
		logger.info("saveOstFollowupSideEffect method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllMasterOstFollowupSideEffectCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveOstFollowupSideEffect(masterDto);
	}
	
	@GetMapping("/mobile/gender-and-pregnancy-type")
	public @ResponseBody Map<String, Object> getGenderAndPregnancyType() {
		logger.debug("Entering into method getGenderAndPregnancyType");
		Map<String, Object> response = null;;
		response = masterDataService.getGenderAndPregnancyType();
		return response;
	}
	
	@GetMapping("/mobile/purpose-of-visit-list/{facilityTypeId}")
	public @ResponseBody List<MasterDto> getPurposeOfVisit(@PathVariable Integer facilityTypeId) {
		logger.debug("Entering into method getPurposeOfVisit");
		return masterDataService.getPurposeOfVisit(facilityTypeId);
	}
	@GetMapping("/mobile/appointment-status-list")
	public @ResponseBody List<MasterDto> getAppointmentStatus() {
		logger.debug("Entering into method getAppointmentStatus");
		return masterDataService.getAppointmentStatusList();
	}
	@GetMapping("/mobile/naco-profile")
	public @ResponseBody List<NacoProfileDto> getNacoProfileData() {
		logger.debug("Entering into method getNacoProfileData");
		return masterDataService.getNacoProfileData();
	}

	@GetMapping("/adherenceremarks")
	public @ResponseBody List<MasterDto> getAdherenceRemarks() {
		logger.debug("Entering into method getAdherenceRemarks");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getAdherenceRemarks();
		return masterDtoList;
	}
	
	@GetMapping("/sacepreviewtype")
	public @ResponseBody List<MasterDto> getSacepReviewTypes() {
		logger.debug("Entering into method getSacepReviewTypes");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getSacepReviewTypes();
		return masterDtoList;
	}
	
	@PostMapping("/insert/sacepreviewtype")
	public @ResponseBody MasterDto saveSacepReviewTypes(@RequestBody MasterDto masterDto) {
		logger.info("saveSacepReviewTypes method called with parameters->{}", masterDto);
		try {
			commonService.clearCache("AllSacepReviewTypesCache", null);	
		} catch (Exception e) {
			logger.error("Exception in clearing cache !",e);
		}
		return masterDataService.saveSacepReviewTypes(masterDto);
	}		
	
	@GetMapping("/bigpictureminimasters")
	public @ResponseBody BigPicMiniMasterDto getMiniMasterForBigPic() {
		logger.debug("Entering into method getAllLabTypes");
		return masterDataService.getMiniMasterValues();
	}
	
	@GetMapping("/artcounsellingtype")
	public @ResponseBody List<MasterDto> getArtCounsellingType() {
		logger.debug("Entering into method getArtCounsellingType()");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList = masterDataService.getArtCounsellingType();
		return masterDtoList;
	}
	
	@GetMapping("/mobile/tb-symptoms")
	public @ResponseBody List<MasterDto> getTbSymptoms() {
		logger.debug("Entering into method getTbSymptoms");
		return masterDataService.getTbSymptoms();
	}
	
	@GetMapping("/mobile/sti-symptoms")
	public @ResponseBody List<MasterDto> getStiSymptoms() {
		logger.debug("Entering into method getStiSymptoms");
		return masterDataService.getStiSymptoms();
	}
	
	@GetMapping("/questionlist")
	public @ResponseBody List<MasterDto> getQuestionList() {
		logger.debug("Entering into method getQuestionList");
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		masterDtoList =  masterDataService.getQuestionList();
		return masterDtoList;
	}
	
	@GetMapping("/sacslist")
	public @ResponseBody List<SacsFacilityDto> getAllSacsList() {
		logger.debug("Entering into method getAllSacsList");
		List<Long> facilityTypeId = new ArrayList<Long>();
		facilityTypeId.add((long) 2); // SACS facility Type Id
		String sortType = "asc";
		String sortBy = "name";
		List<SacsFacilityDto> masterDtoList = new ArrayList<SacsFacilityDto>();
		masterDtoList = facilityService.getFacilityByFacilityTypeOptimized(facilityTypeId, 0, 10000,
				sortBy,sortType);
		return masterDtoList;
	}
	
	/** 
	 * Get Governance Body Designation
	 * */
	@GetMapping("/gb_designation")
	public @ResponseBody List<DesignationDto> getGBDesignation() {
		logger.debug("Entering into method getGBDesignation");
		List<DesignationDto> designationDtoList = new ArrayList<DesignationDto>();
		designationDtoList = masterDataService.getGBDesignation();
		return designationDtoList;

	}
	
	
}
