package gov.naco.soch.admin.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import gov.naco.soch.admin.dto.BigPicMiniMasterDto;
import gov.naco.soch.dto.DesignationDto;
import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.dto.LocationDto;
import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.dto.NacoProfileDto;
import gov.naco.soch.dto.StateDto;
import gov.naco.soch.entity.*;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.mapper.MasterDataMapper;
import gov.naco.soch.repository.*;

@Service
@Transactional
public class MasterDataService {

	@Autowired
	MasterDataRepository masterDataRepository;

	@Autowired
	MasterOccupationRepository masterOccupationRepository;

	@Autowired
	LabTypesMasterRepository labTypesMasterRepository;

	@Autowired
	private MasterSampleStatusRepository masterSampleStatusRepository;

	@Autowired
	private MasterBatchStatusRepository masterBatchStatusRepository;

	@Autowired
	private MasterResultStatusRepository masterResultStatusRepository;

	@Autowired
	private MasterRemarkRepository masterRemarkRepository;

	@Autowired
	private MasterResultTypeRepository masterResultTypeRepository;

	@Autowired
	private MasterTestTypeRepository masterTestTypeRepository;

	@Autowired
	private IndentReasonsMasterRepository indentReasonsMasterRepository;

	@Autowired
	private IndentRequestStatusMasterRepository indentRequestStatusMasterRepository;

	@Autowired
	private TestRepository testRepository;

	@Autowired
	private MasterClientStatusRepository masterClientStatusRepository;

	@Autowired
	private MasterMaritalStatusRepository masterMaritalStatusRepository;

	@Autowired DesignationRepository designationRepository;
	// @Autowired
	// private MasterEmploymentStatusRepository masterEmploymentStatusRepository;

	@Autowired
	private MasterEducationLevelRepository masterEducationLevelRepository;

	@Autowired
	private MasterBeneficiaryCategoryRepository masterBeneficiaryCategoryRepository;

	@Autowired
	private MasterWeightBandRepository masterWeightBandRepository;

	@Autowired
	private ProductCommodityTypeMasterRepository productCommodityTypeMasterRepository;

	@Autowired
	private ProductDrugLineMasterRepository productDrugLineMasterRepository;

	@Autowired
	private ProductRecipientTypeMasterRepository productRecipientTypeMasterRepository;

	@Autowired
	private MasterVaccineTypeRepository masterVaccineTypeRepository;

	@Autowired
	private ProductDosageCategoryMasterRepository productDosageCategoryMasterRepository;

	@Autowired
	private MasterEntryPointRepository masterEntryPointRepository;

	@Autowired
	private MasterRiskFactorRepository masterRiskfactorRepository;

	@Autowired
	private MasterMonthlyIncomeRepository masterMonthlyIncomeRepository;

	@Autowired
	private MasterGenderRepository masterGenderRepository;

	@Autowired
	private MasterBeneficiaryActivityStatusRepository masterBeneficiaryActivityStatusRepository;

	@Autowired
	private MasterTbRegimenRepository masterTbRegimenRepository;

	@Autowired
	private MasterRxOutcomeRepository masterRxOutcomeRepository;

	@Autowired
	private MasterOpportunisticInfectionsRepository masterOpportunisticInfectionsRepository;

	@Autowired
	private MasterSideEffectsRepository masterSideEffectsRepository;

	@Autowired
	private MasterSubstitueReasonsRepository masterSubstitueReasonsRepository;

	@Autowired
	private MasterStopReasonsRepository masterStopReasonsRepository;

	@Autowired
	private MasterPepPrescriptionRepository masterPepPrescriptionRepository;

	@Autowired
	private MasterBaselineTestsRepository masterBaselineTestsRepository;

	@Autowired
	private MasterDiseaseClassRepository masterDiseaseClassRepository;

	@Autowired
	private MasterFunctionalStatusRepository masterFunctionalStatusRepository;

	@Autowired
	private MasterClinicalStageRepository masterClinicalStageRepository;

	@Autowired
	private MasterPlaceOfArtRepository masterPlaceOfArtRepository;

	@Autowired
	private MasterHrgPrimaryRepository masterHrgPrimaryRepository;

	@Autowired
	private MasterHrgSecondaryRepository masterHrgSecondaryRepository;

	@Autowired
	private MasterSubcategoryRepository masterSubcategoryRepository;

	@Autowired
	private MasterOstStatusBeneficiaryRepository masterOstStatusBeneficiaryRepository;

	@Autowired
	private MasterRelationsRepository masterRelationsRepository;

	@Autowired
	private MasterHivStatusRepository masterHivStatusRepository;

	@Autowired
	private MasterBeneficiaryArtTransferredFromRepository masterBeneficiaryArtTransferredFromRepository;

	@Autowired
	private MasterHabitsAlcoholUseRepository masterHabitsAlcoholUseRepository;

	@Autowired
	private MasterHabitsSmokingRepository masterHabitsSmokingRepository;

	@Autowired
	private MasterTobaccoUseRepository masterTobaccoUseRepository;

	@Autowired
	private MasterCounsellingTypeRepository masterCounsellingTypeRepository;

	@Autowired
	private MasterHealthStatusRepository masterHealthStatusRepository;

	@Autowired
	private MasterDrugsRepository masterDrugsRepository;

	@Autowired
	private MasterSyphilisStatusRepository masterSyphilisStatusRepository;

	@Autowired
	private MasterScreeningStatusRepository masterScreeningStatusRepository;

	@Autowired
	private MasterOstAssementStatusRepository masterOstAssementStatusRepository;

	@Autowired
	private MasterDiagnosisTypeRepository masterDiagnosisTypeRepository;

	@Autowired
	private MasterKitTypeRepository masterKitTypeRepository;

	@Autowired
	private MasterTreatmentTypeRepository masterTreatmentTypeRepository;

	@Autowired
	private MasterReferralStatusRepository masterReferralStatusRepository;

	@Autowired
	private MasterYesOrNoRepository masterYesOrNoRepository;

	@Autowired
	private MasterStatusRepository masterStatusRepository;

	@Autowired
	private MasterHbvStatusRepository masterHbvStatusRepository;

	@Autowired
	private MasterOtherAilmentsRepository masterOtherAilmentsRepository;

	@Autowired
	private MasterContraceptionRepository masterContraceptionRepository;

	@Autowired
	private MasterObstetricHistoryRepository masterObstetricHistoryRepository;

	@Autowired
	private MasterPurposesRepository masterPurposesRepository;

	@Autowired
	private MasterExposureSeverityRepository masterExposureSeverityRepository;

	@Autowired
	private MasterTbTreatmentStatusRepository masterTbTreatmentStatusRepository;

	@Autowired
	private MasterInjuryExposureModeRepository masterInjuryExposureModeRepository;

	@Autowired
	private MasterHivTypeRepository masterHivTypeRepository;

	@Autowired
	private MasterTiOstTypeRepository masterTiOstTypeRepository;

	@Autowired
	private MasterPrimaryDrugRepository masterPrimaryDrugRepository;

	@Autowired
	private MasterDrugComplicationsRepository masterDrugComplicationsRepository;

	@Autowired
	private MasterContactTypeRepository masterContactTypeRepository;

	@Autowired
	private MasterInfectionTypeRepository masterInfectionTypeRepository;

	@Autowired
	private MasterTbScreeningStatusRepository masterTbScreeningStatusRepository;

	@Autowired
	private MasterFollowUpTypeRepository masterFollowUpTypeRepository;

	@Autowired
	private MasterRegistrationDoneAtRepository masterRegistrationDoneAtRepository;

	@Autowired
	private MasterStayingWithRepository masterStayingWithRepository;

	@Autowired
	private MasterMobilityTypeRepository masterMobilityTypeRepository;

	@Autowired
	private MasterDestinationDurationRepository masterDestinationDurationRepository;

	@Autowired
	private MasterReferredfromRepository masterReferredfromRepository;

	@Autowired
	private MasterReferredbyRepository masterReferredbyRepository;

	@Autowired
	private MasterPregnancyTypeCaseRepository masterPregnancyTypeCaseRepository;

	@Autowired
	private MasterArvProphylaxisRepository masterArvProphylaxisRepository;

	@Autowired
	private MasterInfantBreastFeedRepository masterInfantBreastFeedRepository;

	@Autowired
	private MasterRouteOfTransmissionRepository masterRouteOfTransmissionRepository;

	@Autowired
	private MasterTbResultRepository masterTbResultRepository;

	@Autowired
	private MasterNotTestingReasonRepository masterNotTestingReasonRepository;

	@Autowired
	private MasterIctcBeneficiaryStatusRepository masterIctcBeneficiaryStatusRepository;

	@Autowired
	private MasterIctcFollowUpTypeRepository masterIctcFollowUpTypeRepository;

	@Autowired
	private MasterPartnerStatusRepository masterPartnerStatusRepository;

	@Autowired
	private MasterDeliveryOutcomeRepository masterDeliveryOutcomeRepository;

	@Autowired
	private MasterSampleCollectionStatusRepository masterSampleCollectionStatusRepository;

	@Autowired
	private MasterSwitchReasonsRepository masterSwitchReasonsRepository;

	@Autowired
	private MasterArtBeneficiaryStatusRepository masterArtBeneficiaryStatusRepository;

	@Autowired
	private MasterTreatmentLineRepository masterTreatmentLineRepository;

	@Autowired
	private MasterFourSScreeningRepository masterFourSScreeningRepository;

	@Autowired
	private MasterOrganisationTypeRepository masterOrganisationTypeRepository;

	@Autowired
	private MasterArtStayingWithRepository masterArtStayingWithRepository;

	@Autowired
	private MasterBirthHistoryRepository masterBirthHistoryRepository;

	@Autowired
	private MasterInfantFeedingRepository masterInfantFeedingRepository;

	@Autowired
	private MasterVaccineStageRepository masterVaccineStageRepository;

	@Autowired
	private MasterVitaminAAgeRepository masterVitaminAAgeRepository;

	@Autowired
	private MasterInvestigationRepository masterInvestigationRepository;

	@Autowired
	private MasterArtTreatmentStatusRepository masterArtTreatmentStatusRepository;

	@Autowired
	private MasterIptStatusRepository masterIptStatusRepository;

	@Autowired
	private MasterFourSsymptomRepository masterFourSsymptomRepository;

	@Autowired
	private MasterHcvStatusRepository masterHcvStatusRepository;

	@Autowired
	private MasterHivExposureCodeRepository masterHivExposureCodeRepository;

	@Autowired
	private MasterHivSourceStatusRepository masterHivSourceStatusRepository;

	@Autowired
	private MasterDiagnosedByRepository masterDiagnosedByRepository;

	@Autowired
	private MasterTreatmentUnderRepository masterTreatmentUnderRepository;

	@Autowired
	private MasterDnaPcrResultRepository masterDnaPcrResultRepository;

	@Autowired
	private MasterTrainingTypeRepository masterTrainingTypeRepository;

	@Autowired
	private MasterGuardianCaregiverRepository masterGuardianCaregiverRepository;

	@Autowired
	private MasterDnaPcrTestRepository masterDnaPcrTestRepository;

	@Autowired
	private MasterTbTypeRepository masterTbTypeRepository;

	@Autowired
	private MasterTbTestTypeRepository masterTbTestTypeRepository;
	@Autowired
	private MasterSocialWelfareRepository masterSocialWelfareRepository;

	@Autowired
	private FacilityRepository facilityRepository;

	@Autowired
	private MasterTreatmentOutcomeRepository masterTreatmentOutcomeRepository;

	@Autowired
	private MasterDeleteReasonRepository deleteReasonRepository;

	@Autowired
	private MasterArtBeneficiaryDeleteReasonRepository masterArtBeneficiaryDeleteReasonRepository;

	@Autowired
	private MasterFSWSubCategoryRepository masterFSWSubCategoryRepository;

	@Autowired
	private MasterMSMSubCategoryRepository masterMSMSubCategoryRepository;

	@Autowired
	private MasterTGSubCategoryRepository masterTGSubCategoryRepository;

	@Autowired
	private MasterIDUSubCategoryRepository masterIDUSubCategoryRepository;

	@Autowired
	private MasterProductTypesRepository masterProductTypesRepository;

	@Autowired
	private MachineRepository machineRepository;

	@Autowired
	private MasterRelationTypeRepository masterRelationTypeRepository;

	@Autowired
	private FacilityTypeRepository facilityTypeRepository;

	@Autowired
	private ProductUomRepository productUomRepository;

	@Autowired
	private MasterArtRegimenActionRepository masterArtRegimenActionRepository;

	@Autowired
	private MasterOccupationTruckerRepository masterOccupationTruckerRepository;

	@Autowired
	private MasterMigrantOccupationRepository masterMigrantOccupationRepository;

	@Autowired
	private MasterHivScreeningStatusRepository masterHivScreeningStatusRepository;

	@Autowired
	private MasterPatternOfMigrationRepository masterPatternOfMigrationRepository;

	@Autowired
	private MasterTakeHomeDaysRepository masterTakeHomeDaysRepository;

	@Autowired
	private MasterRelationshipRepository masterRelationshipRepository;

	@Autowired
	private MasterIpcTopicsRepository masterIpcTopicsRepository;

	@Autowired
	private MasterOstAssessmentReasonRepository masterOstAssessmentReasonRepository;

	@Autowired
	private MasterFollowUpByRepository masterFollowUpByRepository;

	@Autowired
	private IndentStatusMasterRepository indentStatusMasterRepository;

	@Autowired
	private ContractStatusMasterRepository contractStatusMasterRepository;

	@Autowired
	private ConsignmentStatusMasterRepository consignmentStatusMasterRepository;

	@Autowired
	private ReceiptStatusRepository receiptStatusRepository;

	@Autowired
	private FacilityConsignmentStatusMasterRepository facilityConsignmentStatusMasterRepository;

	@Autowired
	private FacilityReceiptStatusMasterRepository facilityReceiptStatusMasterRepository;

	@Autowired
	private FacilityRelocationRequestStatusMasterRepository facilityRelocationRequestStatusMasterRepository;

	@Autowired
	private ReconciliationStatusMasterRepository reconciliationStatusMasterRepository;

	@Autowired
	private MasterOpioidOverdoseReasonsRepository masterOpioidOverdoseReasonsRepository;
	
	@Autowired
	private MasterArtCounsellingTypeRepository masterArtCounsellingTypeRepository;
//	@Autowired
//	private MasterStiRtiDiagnosisTypeRepository masterStiRtiDiagnosisTypeRepository;
	@Autowired
	private MasterClinicalTreatmentTypeRepository masterClinicalTreatmentTypeRepository;
	@Autowired
	private MasterTreatmentAbssessSitesRepository masterTreatmentAbssessSitesRepository;
	@Autowired
	private MasterTreatmentAbssessTypesRepository masterTreatmentAbssessTypesRepository;
	@Autowired
	private MasterDiagnosisRecordedAtRepository masterDiagnosisRecordedAtRepository;

	@Autowired
	private MasterBloodBankOwnedByRepository masterBloodBankOwnedByRepository;

	@Autowired
	private MasterBloodBankTypeRepository masterBloodBankTypeRepository;

	@Autowired
	private MasterMultimonthDispensationRepository masterMultimonthDispensationRepository;

	@Autowired
	private MasterQmsTestStatusRepository masterQmsTestStatusRepository;

	@Autowired
	private MasterLacTypeRepository masterLacTypeRepository;

	@Autowired
	private MasterOstFollowupReasonRepository masterOstFollowupReasonRepository;

	@Autowired
	private MasterOstFollowupSideEffectRepository masterOstFollowupSideEffectRepository;
	
	@Autowired
	private MasterPurposeOfVisitRepository masterPurposeOfVisitRepository;
	@Autowired
	private MasterTbSymptomsRepository masterTbSymptomsRepository;
	@Autowired
	private MasterStiSymptomsRepository masterStiSymptomsRepository;
	@Autowired
	private MasterAppointmentStatusRepository masterAppointmentStatusRepository;
	@Autowired
	private NacoProfileRepository nacoProfileRepository;
	
	@Autowired
	private MasterAdherenceRemarksRepository masterAdherenceRemarksRepository;
	
	@Autowired
	private MasterReferralTypeRepository masterReferralTypeRepository;
	
	@Autowired
	private MasterSacepReferralReasonRepository masterSacepReferralReasonRepository;

	@Autowired
	private MasterClientStatusTicoreRepository masterClientStatusTicoreRepository;
	
	@Autowired
	MasterTypeOfSacepReviewRepository masterTypeOfSacepReviewRepository;
	
	@Autowired
	MasterDashboardGenderRepository masterDashboardGenderRepository;
	
	@Autowired
	MasterDashboardGeographicalLevelRepository masterDashboardGeographicalLevelRepository;
	
	@Autowired
	MasterDashboardTypologyRepository masterDashboardTypologyRepository;
	
	@Autowired
	AddressService addressService;
	
	@Autowired
	MasterAgeGroupRepository masterAgeGroupRepository;
	
	@Autowired
	MasterTargetIndicatorTypeRepository masterTargetIndicatorTypeRepository;
	
	@Autowired
	MasterPrisonQuestionRepository masterPrisonQuestionRepository;
	private static final Logger logger = LoggerFactory.getLogger(MasterDataService.class);

	/*
	 * Get all Lab types
	 */
	@Cacheable(value = "AllMasterAllLabTypesCache")
	public List<MasterDto> getAllLabTypes() {

		List<LabTypesMaster> labTypesMasterList = labTypesMasterRepository.findAll();
		List<MasterDto> masterDtoList = new ArrayList<>();
		MasterDto masterDto = null;
		for (LabTypesMaster labTypesMaster : labTypesMasterList) {
			masterDto = MasterDataMapper.mapLabTypesMasterToMasterDto(labTypesMaster);
			masterDtoList.add(masterDto);
		}
		return masterDtoList;

	}

	/*
	 * Get all Batch number options
	 */
	@Cacheable(value = "AllMasterAllBatchNoOptionsCache")
	public List<MasterDto> getAllBatchNoOptions() {

		List<MasterData> masterDataList = masterDataRepository.findAllBatchNoOptions();
		List<MasterDto> masterDtoList = new ArrayList<>();
		MasterDto masterDto = null;
		for (MasterData masterData : masterDataList) {
			masterDto = MasterDataMapper.mapToMasterDto(masterData);
			masterDtoList.add(masterDto);
		}
		return masterDtoList;

	}

	/*
	 * Get all adult ped options for regimen.
	 */
	@Cacheable(value = "AllMasterRegimenAdultPedOptionsCache")
	public List<MasterDto> getRegimenAdultPedOptions() {

		List<MasterData> masterDataList = masterDataRepository.findRegimenAdultPedOptions();
		List<MasterDto> masterDtoList = new ArrayList<>();
		MasterDto masterDto = null;
		for (MasterData masterData : masterDataList) {
			masterDto = MasterDataMapper.mapToMasterDto(masterData);
			masterDtoList.add(masterDto);
		}
		return masterDtoList;

	}

	/**
	 * Get master data options based on given master type
	 * 
	 * @param masterType
	 * @return
	 */
	public List<MasterDto> getMasterDataOptions(String masterType) {

		List<MasterData> masterDataList = masterDataRepository.findMasterDataOptions(masterType);
		List<MasterDto> masterDtoList = new ArrayList<>();
		MasterDto masterDto = null;
		for (MasterData masterData : masterDataList) {
			masterDto = MasterDataMapper.mapToMasterDto(masterData);
			masterDtoList.add(masterDto);
		}
		return masterDtoList;

	}

	/*
	 * Get all line options for regimen.
	 */
	@Cacheable(value = "AllMasterRegimenLineOptionsCache")
	public List<MasterDto> getRegimenLineOptions() {

		List<MasterData> masterDataList = masterDataRepository.findRegimenLineOptions();
		List<MasterDto> masterDtoList = new ArrayList<>();
		MasterDto masterDto = null;
		for (MasterData masterData : masterDataList) {
			masterDto = MasterDataMapper.mapToMasterDto(masterData);
			masterDtoList.add(masterDto);
		}
		return masterDtoList;

	}

	@Cacheable(value = "AllMasterSampleStatusCache")
	public List<MasterDto> getSampleStatus() {
		List<MasterSampleStatus> statusList = masterSampleStatusRepository.findByIsDelete(Boolean.FALSE);
		if (!CollectionUtils.isEmpty(statusList)) {
			List<MasterDto> masterDtoList = statusList.stream().filter(s -> s.getIsSelectable() == Boolean.TRUE)
					.map(s -> {
						MasterDto dto = new MasterDto();
						dto.setId(s.getId());
						dto.setName(s.getStatus());
						return dto;
					}).sorted(Comparator.comparing(MasterDto::getName)).collect(Collectors.toList());
			return masterDtoList;
		}
		return null;
	}

	@Cacheable(value = "AllMasterBatchStatusCache")
	public List<MasterDto> getBatchStatus() {
		List<MasterBatchStatus> statusList = masterBatchStatusRepository.findByIsDelete(Boolean.FALSE);
		if (!CollectionUtils.isEmpty(statusList)) {
			List<MasterDto> masterDtoList = statusList.stream().map(s -> {
				MasterDto dto = new MasterDto();
				dto.setId(s.getId());
				dto.setName(s.getStatus());
				return dto;
			}).sorted(Comparator.comparing(MasterDto::getName)).collect(Collectors.toList());
			return masterDtoList;
		}
		return null;
	}

	@Cacheable(value = "AllMasterResultStatusCache")
	public List<MasterDto> getResultStatus() {
		List<MasterResultStatus> statusList = masterResultStatusRepository.findByIsDelete(Boolean.FALSE);
		if (!CollectionUtils.isEmpty(statusList)) {
			List<MasterDto> masterDtoList = statusList.stream().map(s -> {
				MasterDto dto = new MasterDto();
				dto.setId(s.getId());
				dto.setName(s.getStatus());
				return dto;
			}).sorted(Comparator.comparing(MasterDto::getName)).collect(Collectors.toList());
			return masterDtoList;
		}
		return null;
	}

	@Cacheable(value = "AllMasterSampleRemarkCache")
	public List<MasterDto> getSampleRemark() {
		List<MasterRemark> remarksList = masterRemarkRepository.findByIsDelete(Boolean.FALSE);
		if (!CollectionUtils.isEmpty(remarksList)) {
			List<MasterDto> masterDtoList = remarksList.stream().map(s -> {
				MasterDto dto = new MasterDto();
				dto.setId(s.getId());
				dto.setName(s.getRemarks());
				return dto;
			}).sorted(Comparator.comparing(MasterDto::getName)).collect(Collectors.toList());
			return masterDtoList;
		}
		return null;
	}

	@Cacheable(value = "AllMasterResultTypeCache")
	public List<MasterDto> getResultType(String labType) {
		List<MasterResultType> resultTypeList = masterResultTypeRepository.findByIsDelete(Boolean.FALSE);
		if (!CollectionUtils.isEmpty(resultTypeList)) {

			if (labType != null) {
				resultTypeList = resultTypeList.stream().filter(r -> r.getLabType().equalsIgnoreCase(labType))
						.collect(Collectors.toList());
			}

			List<MasterDto> masterDtoList = resultTypeList.stream().map(s -> {
				MasterDto dto = new MasterDto();
				dto.setId(s.getId());
				dto.setName(s.getResultType());
				return dto;
			}).sorted(Comparator.comparing(MasterDto::getName)).collect(Collectors.toList());
			return masterDtoList;
		}
		return null;
	}

	@Cacheable(value = "AllMasterTestsCache")
	public List<MasterDto> getTests() {
		List<Test> testList = testRepository.findByIsDelete(Boolean.FALSE);
		if (!CollectionUtils.isEmpty(testList)) {
			List<MasterDto> masterDtoList = testList.stream().map(s -> {
				MasterDto dto = new MasterDto();
				dto.setId(s.getId());
				dto.setName(s.getTypeOfTest());
				return dto;
			}).collect(Collectors.toList());
			return masterDtoList;
		}
		return null;
	}

	@Cacheable(value = "AllMasterTestTypeCache")
	public List<MasterDto> getTestType(String labType, String test) {
		List<MasterTestType> testTypeList = masterTestTypeRepository.findByIsDelete(Boolean.FALSE);

		if (labType != null) {
			testTypeList = testTypeList.stream().filter(r -> r.getLabType().equalsIgnoreCase(labType))
					.collect(Collectors.toList());
		}

		if (test != null) {
			testTypeList = testTypeList.stream().filter(r -> r.getTestGroup().equalsIgnoreCase(test))
					.collect(Collectors.toList());
		}

		if (!CollectionUtils.isEmpty(testTypeList)) {
			List<MasterDto> masterDtoList = testTypeList.stream().map(s -> {
				MasterDto dto = new MasterDto();
				dto.setId(s.getId());
				dto.setName(s.getTestType());
				return dto;
			}).sorted(Comparator.comparing(MasterDto::getName)).collect(Collectors.toList());
			return masterDtoList;
		}
		return null;
	}

	@Cacheable(value = "AllMasterIndentReasonsCache")
	public List<MasterDto> getIndentReasons() {
		List<IndentReasonsMaster> reasonsList = indentReasonsMasterRepository
				.findByIsDeleteOrderByIndentReasonsNameAsc(Boolean.FALSE);
		if (!CollectionUtils.isEmpty(reasonsList)) {
			List<MasterDto> masterDtoList = reasonsList.stream().map(s -> {
				MasterDto dto = new MasterDto();
				dto.setId(s.getId());
				dto.setName(s.getIndentReasonsName());
				return dto;
			}).collect(Collectors.toList());
			return masterDtoList;
		}
		return null;
	}

	@Cacheable(value = "AllMasterIndentStatusCache")
	public List<MasterDto> getIndentStatus() {
		List<IndentRequestStatusMaster> indentStatusList = indentRequestStatusMasterRepository
				.findByIsDelete(Boolean.FALSE);
		if (!CollectionUtils.isEmpty(indentStatusList)) {
			List<MasterDto> masterDtoList = indentStatusList.stream().map(s -> {
				MasterDto dto = new MasterDto();
				dto.setId(s.getId());
				dto.setName(s.getIndentRequestStatusName());
				return dto;
			}).collect(Collectors.toList());
			return masterDtoList;
		}
		return null;
	}

	@Cacheable(value = "AllMasterClientStatusCache")
	public List<MasterDto> getClientStatus() {
		logger.debug("Entering into method getClientStatus - In MasterDataService");
		List<MasterClientStatus> masterClientStatusList = new ArrayList<MasterClientStatus>();
		masterClientStatusList = masterClientStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterClientStatusList != null) {
			masterClientStatusList.forEach(masterClientStatus -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(masterClientStatus.getId());
				masterDto.setName(masterClientStatus.getName());
				masterDto.setDescription(masterClientStatus.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "ClientStatusByFacilityTypeIdCache")
	public List<MasterDto> getClientStatusByFacilityTypeId(Long facilityTypeId) {
		logger.debug("Entering into method getClientStatusByFacilityTypeId - In MasterDataService");
		List<MasterClientStatus> masterClientStatusList = new ArrayList<MasterClientStatus>();
		masterClientStatusList = masterClientStatusRepository.findByFacilityTypeIdAndIsDelete(facilityTypeId,
				Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterClientStatusList != null) {
			masterClientStatusList.forEach(masterClientStatus -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(masterClientStatus.getId());
				masterDto.setName(masterClientStatus.getName());
				masterDto.setDescription(masterClientStatus.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}
	
	@Cacheable(value = "AllMasterClientStatusTiCoreCache")
	public List<MasterDto> getClientStatusTiCore() {
		logger.debug("Entering into method getClientStatusTiCore - In MasterDataService");
		List<MasterClientStatusTicore> masterClientStatusTicoreList = new ArrayList<MasterClientStatusTicore>();
		masterClientStatusTicoreList = masterClientStatusTicoreRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterClientStatusTicoreList != null) {
			masterClientStatusTicoreList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}
	
	public MasterDto saveClientStatusTiCore(MasterDto masterDto) {

		int count = 0;
		count = masterClientStatusTicoreRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterClientStatusTicore row = new MasterClientStatusTicore();
		//row.setCode(masterDto.getCode());
		row.setDescription(masterDto.getDescription());
		row.setName(masterDto.getName());
		row.setIsActive(true);
		row.setIsDelete(false);
		row = masterClientStatusTicoreRepository.save(row);

		masterDto.setId(row.getId());
		//masterDto.setCode(row.getCode());
		masterDto.setDescription(row.getName());
		masterDto.setName(row.getName());
		return masterDto;

	}

	@Cacheable(value = "AllMaritalStatusCache")
	public List<MasterDto> getMaritalStatus() {
		logger.debug("Entering into method getMaritalStatus - In MasterDataService");
		List<MasterMaritalStatus> masterMaritalStatusList = new ArrayList<MasterMaritalStatus>();
		masterMaritalStatusList = masterMaritalStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterMaritalStatusList != null) {
			masterMaritalStatusList.forEach(masterClientStatus -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(masterClientStatus.getId());
				masterDto.setName(masterClientStatus.getName());
				masterDto.setDescription(masterClientStatus.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList.stream().sorted(Comparator.comparing(MasterDto::getName)).collect(Collectors.toList());
	}

	public MasterDto saveMaritalStatus(MasterDto masterDto) {

		int count = 0;
		count = masterMaritalStatusRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterMaritalStatus masterMaritalStatus = new MasterMaritalStatus();
		masterMaritalStatus.setCode(masterDto.getCode());
		masterMaritalStatus.setDescription(masterDto.getDescription());
		masterMaritalStatus.setName(masterDto.getName());
		masterMaritalStatus.setIsActive(true);
		masterMaritalStatus.setIsDelete(false);
		masterMaritalStatus = masterMaritalStatusRepository.save(masterMaritalStatus);

		masterDto.setId(masterMaritalStatus.getId());
		masterDto.setCode(masterMaritalStatus.getCode());
		masterDto.setDescription(masterMaritalStatus.getDescription());
		masterDto.setName(masterMaritalStatus.getName());
		return masterDto;

	}

	@Cacheable(value = "AllMasterOccupationCache")
	public List<MasterDto> getMasterOccupation() {
		logger.debug("Entering into method getMasterOccupation - In MasterDataService");
		List<MasterOccupation> masterOccupationList = new ArrayList<MasterOccupation>();
		masterOccupationList = masterOccupationRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterOccupationList != null) {
			masterOccupationList.forEach(masterOccupation -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(masterOccupation.getId());
				masterDto.setName(masterOccupation.getName());
				masterDto.setDescription(masterOccupation.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList.stream().sorted(Comparator.comparing(MasterDto::getName)).collect(Collectors.toList());
	}

	public MasterDto saveOccupation(MasterDto masterDto) {
		int count = 0;
		count = masterOccupationRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterOccupation masterOccupation = new MasterOccupation();
		masterOccupation.setCode(masterDto.getCode());
		masterOccupation.setDescription(masterDto.getDescription());
		masterOccupation.setName(masterDto.getName());
		masterOccupation.setIsActive(true);
		masterOccupation.setIsDelete(false);
		masterOccupation = masterOccupationRepository.save(masterOccupation);

		masterDto.setId(masterOccupation.getId());
		masterDto.setCode(masterOccupation.getCode());
		masterDto.setDescription(masterOccupation.getDescription());
		masterDto.setName(masterOccupation.getName());
		return masterDto;

	}

	// @Cacheable(value = "AllMasterEmploymentStatusCache")
	// public List<MasterDto> getMasterEmploymentStatus() {
	// logger.debug("Entering into method getMasterEmploymentStatus - In
	// MasterDataService");
	// List<MasterEmploymentStatus> masterEmploymentStatusList = new
	// ArrayList<MasterEmploymentStatus>();
	// masterEmploymentStatusList =
	// masterEmploymentStatusRepository.findByIsDelete(Boolean.FALSE);
	// List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
	// if (masterEmploymentStatusList != null) {
	// masterEmploymentStatusList.forEach(masterEmploymentStatus -> {
	// MasterDto masterDto = new MasterDto();
	// masterDto.setId(masterEmploymentStatus.getId());
	// masterDto.setName(masterEmploymentStatus.getName());
	// masterDto.setDescription(masterEmploymentStatus.getDescription());
	// masterDtoList.add(masterDto);
	// });
	// }
	// return masterDtoList;
	// }

	// public MasterDto saveEmploymentStatus(MasterDto masterDto) {
	// int count = 0;
	// count =
	// masterEmploymentStatusRepository.existsByOtherName(masterDto.getName());
	//
	// if (count != 0) {
	// logger.error(Constants.DUPLICATE_FOUND);
	// String errorfield = "title";
	// logger.debug("call throwError if duplicate found");
	// throwError(errorfield, masterDto.getName());
	// }
	//
	// MasterEmploymentStatus masterEmpStatus = new MasterEmploymentStatus();
	// masterEmpStatus.setCode(masterDto.getCode());
	// masterEmpStatus.setDescription(masterDto.getDescription());
	// masterEmpStatus.setName(masterDto.getName());
	// masterEmpStatus.setIsActive(true);
	// masterEmpStatus.setIsDelete(false);
	// masterEmpStatus = masterEmploymentStatusRepository.save(masterEmpStatus);
	//
	// masterDto.setId(masterEmpStatus.getId());
	// masterDto.setCode(masterEmpStatus.getCode());
	// masterDto.setDescription(masterEmpStatus.getDescription());
	// masterDto.setName(masterEmpStatus.getName());
	// return masterDto;
	//
	// }

	@Cacheable(value = "AllMasterEducationLevelCache")
	public List<MasterDto> getMasterEducationLevel() {
		logger.debug("Entering into method getMasterEducationLevel - In MasterDataService");
		List<MasterEducationLevel> masterEducationLevelList = new ArrayList<MasterEducationLevel>();
		masterEducationLevelList = masterEducationLevelRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterEducationLevelList != null) {
			masterEducationLevelList.forEach(masterEducationLevel -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(masterEducationLevel.getId());
				masterDto.setName(masterEducationLevel.getName());
				masterDto.setDescription(masterEducationLevel.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList.stream().sorted(Comparator.comparing(MasterDto::getName)).collect(Collectors.toList());
	}

	public MasterDto saveEducationLevel(MasterDto masterDto) {
		int count = 0;
		count = masterEducationLevelRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterEducationLevel masterEducationLevel = new MasterEducationLevel();
		masterEducationLevel.setCode(masterDto.getCode());
		masterEducationLevel.setDescription(masterDto.getDescription());
		masterEducationLevel.setName(masterDto.getName());
		masterEducationLevel.setIsActive(true);
		masterEducationLevel.setIsDelete(false);
		masterEducationLevel = masterEducationLevelRepository.save(masterEducationLevel);

		masterDto.setId(masterEducationLevel.getId());
		masterDto.setCode(masterEducationLevel.getCode());
		masterDto.setDescription(masterEducationLevel.getName());
		masterDto.setName(masterEducationLevel.getDescription());
		return masterDto;

	}

	@Cacheable(value = "AllBeneficiaryCategoryCache")
	public List<MasterDto> getMasterBeneficiaryCategory() {
		logger.debug("Entering into method getMasterBeneficiaryCategory - In MasterDataService");
		List<MasterBeneficiaryCategory> masterBeneficiaryCategoryList = new ArrayList<MasterBeneficiaryCategory>();
		masterBeneficiaryCategoryList = masterBeneficiaryCategoryRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterBeneficiaryCategoryList != null) {
			masterBeneficiaryCategoryList.forEach(masterBeneficiaryCategory -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(masterBeneficiaryCategory.getId());
				masterDto.setName(masterBeneficiaryCategory.getName());
				masterDto.setDescription(masterBeneficiaryCategory.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveBeneficiaryCategory(@Valid MasterDto masterDto) {

		int count = 0;
		count = masterBeneficiaryCategoryRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "name";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterBeneficiaryCategory masterBeneficiaryCategory = new MasterBeneficiaryCategory();
		masterBeneficiaryCategory.setCode(masterDto.getCode());
		masterBeneficiaryCategory.setDescription(masterDto.getDescription());
		masterBeneficiaryCategory.setName(masterDto.getName());
		masterBeneficiaryCategory.setIsActive(true);
		masterBeneficiaryCategory.setIsDelete(false);
		masterBeneficiaryCategory = masterBeneficiaryCategoryRepository.save(masterBeneficiaryCategory);

		masterDto.setId(masterBeneficiaryCategory.getId());
		masterDto.setCode(masterBeneficiaryCategory.getCode());
		masterDto.setDescription(masterBeneficiaryCategory.getDescription());
		masterDto.setName(masterBeneficiaryCategory.getName());
		return masterDto;
	}

	@Cacheable(value = "AllMasterWeightBandCache")
	public List<MasterDto> getMasterWeightBand() {
		logger.debug("Entering into method getMasterWeightBand - In MasterDataService");
		List<MasterWeightBand> masterWeightBandList = new ArrayList<MasterWeightBand>();
		masterWeightBandList = masterWeightBandRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterWeightBandList != null) {
			masterWeightBandList.forEach(masterWeightBand -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(masterWeightBand.getId());
				masterDto.setName(masterWeightBand.getName());
				masterDto.setDescription(masterWeightBand.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterCommodityTypeCache")
	public List<MasterDto> getCommodityType() {
		logger.debug("Entering into method getCommodityType - In MasterDataService");
		List<ProductCommodityTypeMaster> productCommodityTypeMasterList = new ArrayList<ProductCommodityTypeMaster>();
		productCommodityTypeMasterList = productCommodityTypeMasterRepository.findAllByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (productCommodityTypeMasterList != null) {
			productCommodityTypeMasterList.forEach(productCommodityTypeMaster -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(productCommodityTypeMaster.getId());
				masterDto.setName(productCommodityTypeMaster.getProductCommodityTypeName());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveCommodityType(MasterDto masterDto) {

		int count = 0;
		count = productCommodityTypeMasterRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "name";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		ProductCommodityTypeMaster productCommodityTypeMaster = new ProductCommodityTypeMaster();
		productCommodityTypeMaster.setProductCommodityTypeCode(masterDto.getCode());
		productCommodityTypeMaster.setProductCommodityTypeName(masterDto.getName());
		productCommodityTypeMaster.setIsActive(true);
		productCommodityTypeMaster.setIsDelete(false);
		productCommodityTypeMaster = productCommodityTypeMasterRepository.save(productCommodityTypeMaster);

		masterDto.setId(productCommodityTypeMaster.getId());
		masterDto.setCode(productCommodityTypeMaster.getProductCommodityTypeCode());
		masterDto.setName(productCommodityTypeMaster.getProductCommodityTypeName());
		return masterDto;
	}

	@Cacheable(value = "AllMasterDrugLineCache")
	public List<MasterDto> getDrugLine() {
		logger.debug("Entering into method getDrugLine - In MasterDataService");
		List<ProductDrugLineMaster> productDrugLineMasterList = new ArrayList<ProductDrugLineMaster>();
		productDrugLineMasterList = productDrugLineMasterRepository.findAllByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (productDrugLineMasterList != null) {
			productDrugLineMasterList.forEach(productDrugLineMaster -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(productDrugLineMaster.getId());
				masterDto.setName(productDrugLineMaster.getProductDrugLineName());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterRecepientTypeCache")
	public List<MasterDto> getRecepientType() {
		logger.debug("Entering into method getRecepientType - In MasterDataService");
		List<ProductRecipientTypeMaster> productRecipientTypeMasterList = new ArrayList<ProductRecipientTypeMaster>();
		productRecipientTypeMasterList = productRecipientTypeMasterRepository.findAllByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (productRecipientTypeMasterList != null) {
			productRecipientTypeMasterList.forEach(productRecipientTypeMaster -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(productRecipientTypeMaster.getId());
				masterDto.setName(productRecipientTypeMaster.getProductRecipientTypeName());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterVaccineTypeCache")
	public List<MasterDto> getVaccineType() {
		logger.debug("Entering into method getVaccineType - In MasterDataService");
		List<MasterVaccineType> masterList = new ArrayList<MasterVaccineType>();
		masterList = masterVaccineTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getVaccineTypeByStage(Long stageId) {
		logger.debug("Entering into method getVaccineTypeByStage - In MasterDataService");
		List<MasterVaccineType> masterList = new ArrayList<MasterVaccineType>();
		masterList = masterVaccineTypeRepository.findByIsDeleteAndVaccineStageId(stageId);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getProductDosageCategory() {
		logger.debug("Entering into method getProductDosageCategory - In MasterDataService");
		List<ProductDosageCategoryMaster> productDosageCategoryMasterList = new ArrayList<ProductDosageCategoryMaster>();
		productDosageCategoryMasterList = productDosageCategoryMasterRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (productDosageCategoryMasterList != null) {
			productDosageCategoryMasterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getProductDosageCategoryName());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllEntryPointCache")
	public List<MasterDto> getEntryPoint() {
		logger.debug("Entering into method getEntryPoint - In MasterDataService");
		List<MasterEntryPoint> masterList = new ArrayList<MasterEntryPoint>();
		masterList = masterEntryPointRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList.stream().sorted(Comparator.comparing(MasterDto::getName)).collect(Collectors.toList());
	}

	@Cacheable(value = "AllRiskFactorCache")
	public List<MasterDto> getRiskFactor() {
		logger.debug("Entering into method getRiskFactor - In MasterDataService");
		List<MasterRiskFactor> masterList = new ArrayList<MasterRiskFactor>();
		masterList = masterRiskfactorRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList.stream().sorted(Comparator.comparing(MasterDto::getName)).collect(Collectors.toList());
	}

	@Cacheable(value = "AllMonthlyIncomeCache")
	public List<MasterDto> getMonthlyIncome() {
		logger.debug("Entering into method getMonthlyIncome - In MasterDataService");
		List<MasterMonthlyIncome> masterList = new ArrayList<MasterMonthlyIncome>();
		masterList = masterMonthlyIncomeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllGenderCache")
	public List<MasterDto> getGender() {
		logger.debug("Entering into method getGender - In MasterDataService");
		List<MasterGender> masterList = new ArrayList<MasterGender>();
		masterList = masterGenderRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllBeneficiaryActivityStatusCache")
	public List<MasterDto> getBeneficiaryActivityStatus() {
		logger.debug("Entering into method getBeneficiaryActivityStatus - In MasterDataService");
		List<MasterBeneficiaryActivityStatus> masterList = new ArrayList<MasterBeneficiaryActivityStatus>();
		masterList = masterBeneficiaryActivityStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterTbRegimenCache")
	public List<MasterDto> getTbRegimen() {
		logger.debug("Entering into method getTbRegimen - In MasterDataService");
		List<MasterTbRegimen> masterList = new ArrayList<MasterTbRegimen>();
		masterList = masterTbRegimenRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterRxOutcomeCache")
	public List<MasterDto> getRxOutcome() {
		logger.debug("Entering into method getRxOutcome - In MasterDataService");
		List<MasterRxOutcome> masterList = new ArrayList<MasterRxOutcome>();
		masterList = masterRxOutcomeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterOpportunisticInfectionsCache")
	public List<MasterDto> getOpportunisticInfections() {
		logger.debug("Entering into method getOpportunisticInfections - In MasterDataService");
		List<MasterOpportunisticInfection> masterList = new ArrayList<MasterOpportunisticInfection>();
		masterList = masterOpportunisticInfectionsRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterSideEffectsCache")
	public List<MasterDto> getSideEffects() {
		logger.debug("Entering into method getSideEffects - In MasterDataService");
		List<MasterSideEffect> masterList = new ArrayList<MasterSideEffect>();
		masterList = masterSideEffectsRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterSubstituteReasonsCache")
	public List<MasterDto> getSubstituteReasons() {
		logger.debug("Entering into method getSubstituteReasons - In MasterDataService");
		List<MasterSubstituteReason> masterList = new ArrayList<MasterSubstituteReason>();
		masterList = masterSubstitueReasonsRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterStopReasonsCache")
	public List<MasterDto> getStopReasons() {
		logger.debug("Entering into method getStopReasons - In MasterDataService");
		List<MasterStopReason> masterList = new ArrayList<MasterStopReason>();
		masterList = masterStopReasonsRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterPepPrescriptionCache")
	public List<MasterDto> getPepPrescription() {
		logger.debug("Entering into method getPepPrescription - In MasterDataService");
		List<MasterPepPrescription> masterList = new ArrayList<MasterPepPrescription>();
		masterList = masterPepPrescriptionRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterBaselineTestsCache")
	public List<MasterDto> getBaselineTests() {
		logger.debug("Entering into method getBaselineTests - In MasterDataService");
		List<MasterBaselineTest> masterList = new ArrayList<MasterBaselineTest>();
		masterList = masterBaselineTestsRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;

	}

	@Cacheable(value = "AllMasterDiseaseClassCache")
	public List<MasterDto> getDiseaseClass() {
		logger.debug("Entering into method getDiseaseClass - In MasterDataService");
		List<MasterDiseaseClass> masterList = new ArrayList<MasterDiseaseClass>();
		masterList = masterDiseaseClassRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;

	}

	@Cacheable(value = "AllMasterFunctionalStatusCache")
	public List<MasterDto> getFunctionalStatus() {
		logger.debug("Entering into method getFunctionalStatus - In MasterDataService");
		List<MasterFunctionalStatus> masterList = new ArrayList<MasterFunctionalStatus>();
		masterList = masterFunctionalStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;

	}

	@Cacheable(value = "AllMasterClinicalStageCache")
	public List<MasterDto> getClinicalStage() {
		logger.debug("Entering into method getClinicalStage - In MasterDataService");
		List<MasterClinicalStage> masterList = new ArrayList<MasterClinicalStage>();
		masterList = masterClinicalStageRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;

	}

	@Cacheable(value = "AllMasterPlaceOfArtCache")
	public List<MasterDto> getPlaceOfArt() {
		logger.debug("Entering into method getPlaceOfArt - In MasterDataService");
		List<MasterPlaceOfArt> masterList = new ArrayList<MasterPlaceOfArt>();
		masterList = masterPlaceOfArtRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterHrgPrimaryCache")
	public List<MasterDto> getHrgPrimary() {
		logger.debug("Entering into method getHrgPrimary - In MasterDataService");
		List<MasterHrgPrimary> masterList = new ArrayList<MasterHrgPrimary>();
		masterList = masterHrgPrimaryRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterHrgSecondaryCache")
	public List<MasterDto> getHrgSecondary() {
		logger.debug("Entering into method getHrgSecondary - In MasterDataService");
		List<MasterHrgSecondary> masterList = new ArrayList<MasterHrgSecondary>();
		masterList = masterHrgSecondaryRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveHrgSecondary(MasterDto masterDto) {

		int count = 0;
		count = masterHrgSecondaryRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterHrgSecondary row = new MasterHrgSecondary();
		row.setCode(masterDto.getCode());
		row.setDescription(masterDto.getDescription());
		row.setName(masterDto.getName());
		row.setIsActive(true);
		row.setIsDelete(false);
		row = masterHrgSecondaryRepository.save(row);

		masterDto.setId(row.getId());
		masterDto.setCode(row.getCode());
		masterDto.setDescription(row.getName());
		masterDto.setName(row.getName());
		return masterDto;

	}

	@Cacheable(value = "AllMasterSubcategoryCache")
	public List<MasterDto> getSubcategory() {
		logger.debug("Entering into method getSubcategory - In MasterDataService");
		List<MasterSubcategory> masterList = new ArrayList<MasterSubcategory>();
		masterList = masterSubcategoryRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterOstStatusBeneficiaryCache")
	public List<MasterDto> getOstStatusBeneficiary() {
		logger.debug("Entering into method getOstStatusBeneficiary - In MasterDataService");
		List<MasterOstStatusBeneficiary> masterList = new ArrayList<MasterOstStatusBeneficiary>();
		masterList = masterOstStatusBeneficiaryRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterRelationsCache")
	public List<MasterDto> getRelations() {
		logger.debug("Entering into method getRelations - In MasterDataService");
		List<MasterRelation> masterList = new ArrayList<MasterRelation>();
		masterList = masterRelationsRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllHivStatusCache")
	public List<MasterDto> getHivStatus() {
		logger.debug("Entering into method getHivStatus - In MasterDataService");
		List<MasterHivStatus> masterList = new ArrayList<MasterHivStatus>();
		masterList = masterHivStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "BeneficiaryArtTransferredFromCache")
	public List<MasterDto> getBeneficiaryArtTransferredFrom() {
		logger.debug("Entering into method getBeneficiaryArtTransferredFrom - In MasterDataService");
		List<MasterBeneficiaryArtTransferredFrom> masterList = new ArrayList<MasterBeneficiaryArtTransferredFrom>();
		masterList = masterBeneficiaryArtTransferredFromRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterHabitsAlcoholUseCache")
	public List<MasterDto> getHabitsAlcoholUse() {
		logger.debug("Entering into method getHabitsAlcoholUse - In MasterDataService");
		List<MasterHabitsAlcoholUse> masterList = new ArrayList<MasterHabitsAlcoholUse>();
		masterList = masterHabitsAlcoholUseRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterHabitsSmokingCache")
	public List<MasterDto> getHabitsSmoking() {
		logger.debug("Entering into method getHabitsSmoking - In MasterDataService");
		List<MasterHabitsSmoking> masterList = new ArrayList<MasterHabitsSmoking>();
		masterList = masterHabitsSmokingRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterTobaccoUseCache")
	public List<MasterDto> getTobaccoUse() {
		logger.debug("Entering into method getTobaccoUse - In MasterDataService");
		List<MasterTobaccoUse> masterList = new ArrayList<MasterTobaccoUse>();
		masterList = masterTobaccoUseRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterCounsellingTypeCache")
	public List<MasterDto> getCounsellingType() {
		logger.debug("Entering into method getCounsellingType - In MasterDataService");
		List<MasterCounsellingType> masterList = new ArrayList<MasterCounsellingType>();
		masterList = masterCounsellingTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveCounsellingType(MasterDto masterDto) {

		int count = 0;
		count = masterCounsellingTypeRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterCounsellingType masterCounsellingType = new MasterCounsellingType();
		masterCounsellingType.setCode(masterDto.getCode());
		masterCounsellingType.setDescription(masterDto.getDescription());
		masterCounsellingType.setName(masterDto.getName());
		masterCounsellingType.setIsActive(true);
		masterCounsellingType.setIsDelete(false);

		masterCounsellingType = masterCounsellingTypeRepository.save(masterCounsellingType);

		masterDto.setId(masterCounsellingType.getId());
		masterDto.setCode(masterCounsellingType.getCode());
		masterDto.setDescription(masterCounsellingType.getName());
		masterDto.setName(masterCounsellingType.getDescription());
		return masterDto;
	}

	@Cacheable(value = "AllMasterHealthStatusCache")
	public List<MasterDto> getHealthStatus() {
		logger.debug("Entering into method getHealthStatus - In MasterDataService");
		List<MasterHealthStatus> masterList = new ArrayList<MasterHealthStatus>();
		masterList = masterHealthStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterDrugsCache")
	public List<MasterDto> getDrugs() {
		logger.debug("Entering into method getDrugs - In MasterDataService");
		List<MasterDrug> masterList = new ArrayList<MasterDrug>();
		masterList = masterDrugsRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterSyphilisStatusCache")
	public List<MasterDto> getSyphilisStatus() {
		logger.debug("Entering into method getSyphilisStatus - In MasterDataService");
		List<MasterSyphilisStatus> masterList = new ArrayList<MasterSyphilisStatus>();
		masterList = masterSyphilisStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterScreeningStatusCache")
	public List<MasterDto> getScreeningStatus() {
		logger.debug("Entering into method getScreeningStatus - In MasterDataService");
		List<MasterScreeningStatus> masterList = new ArrayList<MasterScreeningStatus>();
		masterList = masterScreeningStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterOstAssementStatusCache")
	public List<MasterDto> getOstAssementStatus() {
		logger.debug("Entering into method getOstAssementStatus - In MasterDataService");
		List<MasterOstAssementStatus> masterList = new ArrayList<MasterOstAssementStatus>();
		masterList = masterOstAssementStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterDiagnosisTypeCache")
	public List<MasterDto> getDiagnosisType() {
		logger.debug("Entering into method  getDiagnosisType - In MasterDataService");
		List<MasterDiagnosisType> masterList = new ArrayList<MasterDiagnosisType>();
		masterList = masterDiagnosisTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterKitTypeCache")
	public List<MasterDto> getKitType() {
		logger.debug("Entering into method  getKitType - In MasterDataService");
		List<MasterKitType> masterList = new ArrayList<MasterKitType>();
		masterList = masterKitTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getKitTypeByDiagnosisId(Long diagnosisId) {
		logger.debug("Entering into method  getKitType - In MasterDataService");
		List<MasterKitType> masterList = new ArrayList<MasterKitType>();
		masterList = masterKitTypeRepository.findByIsDeleteAndDiagnosisId(Boolean.FALSE, diagnosisId);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterTreatmentTypeCache")
	public List<MasterDto> getTreatmentType() {
		logger.debug("Entering into method  getTreatmentType- In MasterDataService");
		List<MasterTreatmentType> masterList = new ArrayList<MasterTreatmentType>();
		masterList = masterTreatmentTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterReferralStatusCache")
	public List<MasterDto> getReferralStatus() {
		logger.debug("Entering into method  getReferralStatus - In MasterDataService");
		List<MasterReferralStatus> masterList = new ArrayList<MasterReferralStatus>();
		masterList = masterReferralStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterYesOrNoCache")
	public List<MasterDto> getYesOrNo() {
		logger.debug("Entering into method  getYesOrNo - In MasterDataService");
		List<MasterYesOrNo> masterList = new ArrayList<MasterYesOrNo>();
		masterList = masterYesOrNoRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterStatusCache")
	public List<MasterDto> getStatus() {
		logger.debug("Entering into method  getStatus - In MasterDataService");
		List<MasterStatus> masterList = new ArrayList<MasterStatus>();
		masterList = masterStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterHbvStatusCache")
	public List<MasterDto> getHbvStatus() {
		logger.debug("Entering into method  getHbvStatus - In MasterDataService");
		List<MasterHbvStatus> masterList = new ArrayList<MasterHbvStatus>();
		masterList = masterHbvStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterOtherAilmentsCache")
	public List<MasterDto> getOtherAilments() {
		logger.debug("Entering into method  getOtherAilments - In MasterDataService");
		List<MasterOtherAilment> masterList = new ArrayList<MasterOtherAilment>();
		masterList = masterOtherAilmentsRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterContraceptionCache")
	public List<MasterDto> getContraception() {
		logger.debug("Entering into method  getContraception - In MasterDataService");
		List<MasterContraception> masterList = new ArrayList<MasterContraception>();
		masterList = masterContraceptionRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterPurposesCache")
	public List<MasterDto> getPurposes() {
		logger.debug("Entering into method  getPurposes - In MasterDataService");
		List<MasterPurpos> masterList = new ArrayList<MasterPurpos>();
		masterList = masterPurposesRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterObstetricHistoryCache")
	public List<MasterDto> getObstetricHistory() {
		logger.debug("Entering into method  getObstetricHistory - In MasterDataService");
		List<MasterObstetricHistory> masterList = new ArrayList<MasterObstetricHistory>();
		masterList = masterObstetricHistoryRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterExposureSeverityCache")
	public List<MasterDto> getExposureSeverity() {
		logger.debug("Entering into method  getExposureSeverity - In MasterDataService");
		List<MasterExposureSeverity> masterList = new ArrayList<MasterExposureSeverity>();
		masterList = masterExposureSeverityRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllInjuryExposureModeCache")
	public List<MasterDto> getInjuryExposureMode() {
		logger.debug("Entering into method  getInjuryExposureMode - In MasterDataService");
		List<MasterInjuryExposureMode> masterList = new ArrayList<MasterInjuryExposureMode>();
		masterList = masterInjuryExposureModeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterTbTreatmentStatusCache")
	public List<MasterDto> getTbTreatmentStatus() {
		logger.debug("Entering into method  getTbTreatmentStatus - In MasterDataService");
		List<MasterTbTreatmentStatus> masterList = new ArrayList<MasterTbTreatmentStatus>();
		masterList = masterTbTreatmentStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterHivTypeCache")
	public List<MasterDto> getHivType() {
		logger.debug("Entering into method  getHivType - In MasterDataService");
		List<MasterHivType> masterList = new ArrayList<MasterHivType>();
		masterList = masterHivTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getTiOstType() {
		logger.debug("Entering into method  getTiOstType - In MasterDataService");
		List<MasterTiOstType> masterList = new ArrayList<MasterTiOstType>();
		masterList = masterTiOstTypeRepository.findAllByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterPrimaryDrugCache")
	public List<MasterDto> getPrimaryDrug() {
		logger.debug("Entering into method getPrimaryDrug - In MasterDataService");
		List<MasterPrimaryDrug> masterList = new ArrayList<MasterPrimaryDrug>();
		masterList = masterPrimaryDrugRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto savePrimaryDrug(MasterDto masterDto) {

		int count = 0;
		count = masterPrimaryDrugRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterPrimaryDrug row = new MasterPrimaryDrug();
		row.setCode(masterDto.getCode());
		row.setDescription(masterDto.getDescription());
		row.setName(masterDto.getName());
		row.setIsActive(true);
		row.setIsDelete(false);
		row = masterPrimaryDrugRepository.save(row);

		masterDto.setId(row.getId());
		masterDto.setCode(row.getCode());
		masterDto.setDescription(row.getName());
		masterDto.setName(row.getName());
		return masterDto;

	}

	@Cacheable(value = "AllMasterDrugComplicationsCache")
	public List<MasterDto> getDrugComplications() {
		logger.debug("Entering into method getDrugComplications - In MasterDataService");
		List<MasterDrugComplications> masterList = new ArrayList<MasterDrugComplications>();
		masterList = masterDrugComplicationsRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterContactTypeCache")
	public List<MasterDto> getContactType() {
		logger.debug("Entering into method getContactType- In MasterDataService");
		List<MasterContactType> masterList = new ArrayList<MasterContactType>();
		masterList = masterContactTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveContactType(MasterDto masterDto) {

		int count = 0;
		count = masterContactTypeRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterContactType masterContactType = new MasterContactType();
		masterContactType.setCode(masterDto.getCode());
		masterContactType.setDescription(masterDto.getDescription());
		masterContactType.setName(masterDto.getName());
		masterContactType.setIsActive(true);
		masterContactType.setIsDelete(false);

		masterContactType = masterContactTypeRepository.save(masterContactType);

		masterDto.setId(masterContactType.getId());
		masterDto.setCode(masterContactType.getCode());
		masterDto.setDescription(masterContactType.getName());
		masterDto.setName(masterContactType.getDescription());
		return masterDto;

	}

	@Cacheable(value = "AllMasterInfectionTypeCache")
	public List<MasterDto> getInfectionType() {
		logger.debug("Entering into method getInfectionType- In MasterDataService");
		List<MasterInfectionType> masterList = new ArrayList<MasterInfectionType>();
		masterList = masterInfectionTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "TbScreeningStatusCache")
	public List<MasterDto> getTbScreeningStatus() {
		logger.debug("Entering into method getTbScreeningStatus- In MasterDataService");
		List<MasterTbScreeningStatus> masterList = new ArrayList<MasterTbScreeningStatus>();
		masterList = masterTbScreeningStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterFollowUpTypeCache")
	public List<MasterDto> getFollowUpType() {
		logger.debug("Entering into method getFollowUpType- In MasterDataService");
		List<MasterFollowUpType> masterList = new ArrayList<MasterFollowUpType>();
		masterList = masterFollowUpTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterRegistrationDoneAtCache")
	public List<MasterDto> getRegistrationDoneAt() {
		logger.debug("Entering into method getRegistrationDoneAt- In MasterDataService");
		List<MasterRegistrationDoneAt> masterList = new ArrayList<MasterRegistrationDoneAt>();
		masterList = masterRegistrationDoneAtRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterStayingWithCache")
	public List<MasterDto> getStayingWith() {
		logger.debug("Entering into method getStayingWith - In MasterDataService");
		List<MasterStayingWith> masterList = new ArrayList<MasterStayingWith>();
		masterList = masterStayingWithRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterMobilityTypeCache")
	public List<MasterDto> getMobilityType() {
		logger.debug("Entering into method getMobilityType - In MasterDataService");
		List<MasterMobilityType> masterList = new ArrayList<MasterMobilityType>();
		masterList = masterMobilityTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterDestinationDurationCache")
	public List<MasterDto> getDestinationDuration() {
		logger.debug("Entering into method getDestinationDuration - In MasterDataService");
		List<MasterDestinationDuration> masterList = new ArrayList<MasterDestinationDuration>();
		masterList = masterDestinationDurationRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterReferredfromCache")
	public List<MasterDto> getReferredfrom() {
		logger.debug("Entering into method getReferredfrom - In MasterDataService");
		List<MasterReferredfrom> masterList = new ArrayList<MasterReferredfrom>();
		masterList = masterReferredfromRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterReferredbyCache")
	public List<MasterDto> getReferredby() {
		logger.debug("Entering into method getReferredby - In MasterDataService");
		List<MasterReferredby> masterList = new ArrayList<MasterReferredby>();
		masterList = masterReferredbyRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterPregnancyTypeCaseCache")
	public List<MasterDto> getPregnancyTypeCase() {
		logger.debug("Entering into method getPregnancyTypeCase - In MasterDataService");
		List<MasterPregnancyTypeCase> masterList = new ArrayList<MasterPregnancyTypeCase>();
		masterList = masterPregnancyTypeCaseRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterArvProphylaxisCache")
	public List<MasterDto> getArvProphylaxis() {
		logger.debug("Entering into method getArvProphylaxis - In MasterDataService");
		List<MasterArvProphylaxis> masterList = new ArrayList<MasterArvProphylaxis>();
		masterList = masterArvProphylaxisRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterInfantBreastFeedCache")
	public List<MasterDto> getInfantBreastFeed() {
		logger.debug("Entering into method getInfantBreastFeed - In MasterDataService");
		List<MasterInfantBreastFeed> masterList = new ArrayList<MasterInfantBreastFeed>();
		masterList = masterInfantBreastFeedRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterRouteOfTransmissionCache")
	public List<MasterDto> getRouteOfTransmission() {
		logger.debug("Entering into method  getRouteOfTransmission - In MasterDataService");
		List<MasterRouteOfTransmission> masterList = new ArrayList<MasterRouteOfTransmission>();
		masterList = masterRouteOfTransmissionRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterTbResultCache")
	public List<MasterDto> getTbResult() {
		logger.debug("Entering into method  getTbResult - In MasterDataService");
		List<MasterTbResult> masterList = new ArrayList<MasterTbResult>();
		masterList = masterTbResultRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterNotTestingReasonCache")
	public List<MasterDto> getNotTestingReason() {
		logger.debug("Entering into method  getNotTestingReason - In MasterDataService");
		List<MasterNotTestingReason> masterList = new ArrayList<MasterNotTestingReason>();
		masterList = masterNotTestingReasonRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterIctcBeneficiaryStatusCache")
	public List<MasterDto> getIctcBeneficiaryStatus() {
		logger.debug("Entering into method  getIctcBeneficiaryStatus - In MasterDataService");
		List<MasterIctcBeneficiaryStatus> masterList = new ArrayList<MasterIctcBeneficiaryStatus>();
		masterList = masterIctcBeneficiaryStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterictcFollowUpTypeCache")
	public List<MasterDto> getictcFollowUpType() {
		logger.debug("Entering into method  getictcFollowUpType- In MasterDataService");
		List<MasterIctcFollowUpType> masterList = new ArrayList<MasterIctcFollowUpType>();
		masterList = masterIctcFollowUpTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterPartnerStatusCache")
	public List<MasterDto> getPartnerStatus() {
		logger.debug("Entering into method   getPartnerStatus- In MasterDataService");
		List<MasterPartnerStatus> masterList = new ArrayList<MasterPartnerStatus>();
		masterList = masterPartnerStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterDeliveryOutcomeCache")
	public List<MasterDto> getDeliveryOutcome() {
		logger.debug("Entering into method   getDeliveryOutcome -  In MasterDataService");
		List<MasterDeliveryOutcome> masterList = new ArrayList<MasterDeliveryOutcome>();
		masterList = masterDeliveryOutcomeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList.stream().sorted(Comparator.comparing(MasterDto::getName)).collect(Collectors.toList());
	}

	@Cacheable(value = "AllMasterSampleCollectionStatusCache")
	public List<MasterDto> getSampleCollectionStatus() {
		logger.debug("Entering into method   getSampleCollectionStatus -  In MasterDataService");
		List<MasterSampleCollectionStatus> masterList = new ArrayList<MasterSampleCollectionStatus>();
		masterList = masterSampleCollectionStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList.stream().sorted(Comparator.comparing(MasterDto::getName)).collect(Collectors.toList());
	}

	@Cacheable(value = "AllMasterSwitchReasonsCache")
	public List<MasterDto> getSwitchReasons() {
		logger.debug("Entering into method   getSwitchReasons-  In MasterDataService");
		List<MasterSwitchReasons> masterList = new ArrayList<MasterSwitchReasons>();
		masterList = masterSwitchReasonsRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterArtBeneficiaryStatusCache")
	public List<MasterDto> getArtBeneficiaryStatus() {
		logger.debug("Entering into method   getArtBeneficiaryStatus-  In MasterDataService");
		List<MasterArtBeneficiaryStatus> masterList = new ArrayList<MasterArtBeneficiaryStatus>();
		masterList = masterArtBeneficiaryStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterTreatmentLineCache")
	public List<MasterDto> getTreatmentLine() {
		logger.debug("Entering into method   getTreatmentLine-  In MasterDataService");
		List<MasterTreatmentLine> masterList = new ArrayList<MasterTreatmentLine>();
		masterList = masterTreatmentLineRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterFourSScreeningCache")
	public List<MasterDto> getFourSScreening() {
		logger.debug("Entering into method   getFourSScreening -  In MasterDataService");
		List<MasterFourSScreening> masterList = new ArrayList<MasterFourSScreening>();
		masterList = masterFourSScreeningRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterOrganisationTypeCache")
	public List<MasterDto> getOrganisationType() {
		logger.debug("Entering into method   getOrganisationType -  In MasterDataService");
		List<MasterOrganisationType> masterList = new ArrayList<MasterOrganisationType>();
		masterList = masterOrganisationTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterArtStayingWithCache")
	public List<MasterDto> getArtStayingWith() {
		logger.debug("Entering into method   getArtStayingWith -  In MasterDataService");
		List<MasterArtStayingWith> masterList = new ArrayList<MasterArtStayingWith>();
		masterList = masterArtStayingWithRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterBirthHistoryCache")
	public List<MasterDto> getBirthHistory() {
		logger.debug("Entering into method   getBirthHistory -  In MasterDataService");
		List<MasterBirthHistory> masterList = new ArrayList<MasterBirthHistory>();
		masterList = masterBirthHistoryRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterInfantFeedingCache")
	public List<MasterDto> getInfantFeeding() {
		logger.debug("Entering into method   getInfantFeeding -  In MasterDataService");
		List<MasterInfantFeeding> masterList = new ArrayList<MasterInfantFeeding>();
		masterList = masterInfantFeedingRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterVaccineStageCache")
	public List<MasterDto> getVaccineStage() {
		logger.debug("Entering into method   getVaccineStage -  In MasterDataService");
		List<MasterVaccineStage> masterList = new ArrayList<MasterVaccineStage>();
		masterList = masterVaccineStageRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterVitaminAAgeCache")
	public List<MasterDto> getVitaminAAge() {
		logger.debug("Entering into method   getVitaminAAge -  In MasterDataService");
		List<MasterVitaminAAge> masterList = new ArrayList<MasterVitaminAAge>();
		masterList = masterVitaminAAgeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterInvestigationCache")
	public List<MasterDto> getInvestigation() {
		logger.debug("Entering into method   getInvestigation -  In MasterDataService");
		List<MasterInvestigation> masterList = new ArrayList<MasterInvestigation>();
		masterList = masterInvestigationRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterArtTreatmentStatusCache")
	public List<MasterDto> getArtTreatmentStatus() {
		logger.debug("Entering into method   getArtTreatmentStatus -  In MasterDataService");
		List<MasterArtTreatmentStatus> masterList = new ArrayList<MasterArtTreatmentStatus>();
		masterList = masterArtTreatmentStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterIptStatusCache")
	public List<MasterDto> getIptStatus() {
		logger.debug("Entering into method   getIptStatus -  In MasterDataService");
		List<MasterIptStatus> masterList = new ArrayList<MasterIptStatus>();
		masterList = masterIptStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	/**
	 * @return
	 */
	@Cacheable(value = "AllMasterFoursSymptomCache")
	public List<MasterDto> getFoursSymptom() {
		logger.debug("Entering into method   getFoursSymptom -  In MasterDataService");
		List<MasterFourSsymptom> masterList = new ArrayList<MasterFourSsymptom>();
		masterList = masterFourSsymptomRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	/**
	 * @return
	 */
	@Cacheable(value = "AllMasterHcvStatusCache")
	public List<MasterDto> getHcvStatus() {
		logger.debug("Entering into method   getHcvStatus -  In MasterDataService");
		List<MasterHcvStatus> masterList = new ArrayList<MasterHcvStatus>();
		masterList = masterHcvStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	/**
	 * @return
	 */
	@Cacheable(value = "AllMasterHivExposureCodeCache")
	public List<MasterDto> getHivExposureCode() {
		logger.debug("Entering into method   getHivExposureCode -  In MasterDataService");
		List<MasterHivExposureCode> masterList = new ArrayList<MasterHivExposureCode>();
		masterList = masterHivExposureCodeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	/**
	 * @return
	 */
	@Cacheable(value = "AllMasterHivSourceStatusCache")
	public List<MasterDto> getHivSourceStatus() {
		logger.debug("Entering into method   getHivSourceStatus -  In MasterDataService");
		List<MasterHivSourceStatus> masterList = new ArrayList<MasterHivSourceStatus>();
		masterList = masterHivSourceStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	/**
	 * @return
	 */
	@Cacheable(value = "AllMasterDiagnosedByCache")
	public List<MasterDto> getDiagnosedBy() {
		logger.debug("Entering into method   getHivSourceStatus -  In MasterDataService");
		List<MasterDiagnosedBy> masterList = new ArrayList<MasterDiagnosedBy>();
		masterList = masterDiagnosedByRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	/**
	 * @return
	 */
	@Cacheable(value = "AllMasterTreatmentUnderCache")
	public List<MasterDto> getTreatmentUnder() {
		logger.debug("Entering into method   getTreatmentUnder -  In MasterDataService");
		List<MasterTreatmentUnder> masterList = new ArrayList<MasterTreatmentUnder>();
		masterList = masterTreatmentUnderRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	/**
	 * @return
	 */
	@Cacheable(value = "AllMasterDnaPcrResultCache")
	public List<MasterDto> getDnaPcrResult() {
		logger.debug("Entering into method   getDnaPcrResult -  In MasterDataService");
		List<MasterDnaPcrResult> masterList = new ArrayList<MasterDnaPcrResult>();
		masterList = masterDnaPcrResultRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterTrainingTypeCache")
	public List<MasterDto> getTrainingType() {
		logger.debug("Entering into method   getDnaPcrResult -  In MasterDataService");
		List<MasterTrainingType> masterList = new ArrayList<MasterTrainingType>();
		masterList = masterTrainingTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveTrainingType(MasterDto masterDto) {

		int count = 0;
		count = masterTrainingTypeRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterTrainingType masterTrainingTypes = new MasterTrainingType();
		masterTrainingTypes.setCode(masterDto.getCode());
		masterTrainingTypes.setDescription(masterDto.getDescription());
		masterTrainingTypes.setName(masterDto.getName());
		masterTrainingTypes.setIsActive(true);
		masterTrainingTypes.setIsDelete(false);
		masterTrainingTypes = masterTrainingTypeRepository.save(masterTrainingTypes);

		masterDto.setId(masterTrainingTypes.getId());
		masterDto.setCode(masterTrainingTypes.getCode());
		masterDto.setDescription(masterTrainingTypes.getName());
		masterDto.setName(masterTrainingTypes.getName());
		return masterDto;

	}

	/**
	 * @return
	 */
	public List<MasterDto> getGuardianCaregiver() {
		logger.debug("Entering into method   getGuardianCaregiver -  In MasterDataService");
		List<MasterGuardianCaregiver> masterList = new ArrayList<MasterGuardianCaregiver>();
		masterList = masterGuardianCaregiverRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	/**
	 * @return
	 */
	public List<MasterDto> getDnaPcrTest() {
		logger.debug("Entering into method   getDnaPcrTest -  In MasterDataService");
		List<MasterDnaPcrTest> masterList = new ArrayList<MasterDnaPcrTest>();
		masterList = masterDnaPcrTestRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getTbType() {
		logger.debug("Entering into method   getTbType -  In MasterDataService");
		List<MasterTbType> masterList = new ArrayList<MasterTbType>();
		masterList = masterTbTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getTbTestType() {
		List<MasterTbTestType> masterList = new ArrayList<MasterTbTestType>();
		masterList = masterTbTestTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	/**
	 * @return
	 */
	@Cacheable(value = "SocialWelfareMasterForFacilityCache")
	public List<MasterDto> getSocialWelfare(Long facilityId) {
		logger.debug("Entering into method   getSocialWelfare -  In MasterDataService");
		List<MasterSocialWelfare> masterList = new ArrayList<MasterSocialWelfare>();
		Facility facility = facilityRepository.findById(facilityId).get();
		Long stateId = 0l;
		if (facility.getAddress() != null) {
			if (facility.getAddress().getState() != null) {
				stateId = facility.getAddress().getState().getId();
			}
		}
		masterList = masterSocialWelfareRepository.findByIsDeleteAndStateId(stateId);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	/**
	 * @return MasterDto
	 */
	public List<MasterDto> getMasterDeleteReason() {
		logger.debug("Entering into method getMasterDeleteReason - In MasterDataService");
		List<MasterDeleteReason> masterList = new ArrayList<MasterDeleteReason>();
		masterList = deleteReasonRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getTreatmentOutcome() {
		List<MasterTreatmentOutcome> masterList = new ArrayList<MasterTreatmentOutcome>();
		masterList = masterTreatmentOutcomeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getMasterArtDeleteReason() {
		List<MasterArtBeneficiaryDeleteReason> masterList = new ArrayList<MasterArtBeneficiaryDeleteReason>();
		masterList = masterArtBeneficiaryDeleteReasonRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterFswSubCategoryCache")
	public List<MasterDto> getFswSubCategory() {
		List<MasterFSWSubCategory> masterList = new ArrayList<MasterFSWSubCategory>();
		masterList = masterFSWSubCategoryRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveFswSubCategory(MasterDto masterDto) {

		int count = 0;
		count = masterFSWSubCategoryRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterFSWSubCategory row = new MasterFSWSubCategory();
		row.setCode(masterDto.getCode());
		row.setDescription(masterDto.getDescription());
		row.setName(masterDto.getName());
		row.setIsActive(true);
		row.setIsDelete(false);
		row = masterFSWSubCategoryRepository.save(row);

		masterDto.setId(row.getId());
		masterDto.setCode(row.getCode());
		masterDto.setDescription(row.getName());
		masterDto.setName(row.getName());
		return masterDto;

	}

	@Cacheable(value = "AllMasterMsmSubCategoryCache")
	public List<MasterDto> getMsmSubCategory() {
		List<MasterMSMSubCategory> masterList = new ArrayList<MasterMSMSubCategory>();
		masterList = masterMSMSubCategoryRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveMsmSubCategory(MasterDto masterDto) {

		int count = 0;
		count = masterMSMSubCategoryRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterMSMSubCategory row = new MasterMSMSubCategory();
		row.setCode(masterDto.getCode());
		row.setDescription(masterDto.getDescription());
		row.setName(masterDto.getName());
		row.setIsActive(true);
		row.setIsDelete(false);
		row = masterMSMSubCategoryRepository.save(row);

		masterDto.setId(row.getId());
		masterDto.setCode(row.getCode());
		masterDto.setDescription(row.getName());
		masterDto.setName(row.getName());
		return masterDto;

	}

	@Cacheable(value = "AllMasterTgSubCategoryCache")
	public List<MasterDto> getTgSubCategory() {
		List<MasterTGSubCategory> masterList = new ArrayList<MasterTGSubCategory>();
		masterList = masterTGSubCategoryRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveTgSubCategory(MasterDto masterDto) {

		int count = 0;
		count = masterTGSubCategoryRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterTGSubCategory row = new MasterTGSubCategory();
		row.setCode(masterDto.getCode());
		row.setDescription(masterDto.getDescription());
		row.setName(masterDto.getName());
		row.setIsActive(true);
		row.setIsDelete(false);
		row = masterTGSubCategoryRepository.save(row);

		masterDto.setId(row.getId());
		masterDto.setCode(row.getCode());
		masterDto.setDescription(row.getName());
		masterDto.setName(row.getName());
		return masterDto;

	}

	@Cacheable(value = "AllMasterIduSubCategoryCache")
	public List<MasterDto> getIduSubCategory() {
		List<MasterIDUSubCategory> masterList = new ArrayList<MasterIDUSubCategory>();
		masterList = masterIDUSubCategoryRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveIduSubCategory(MasterDto masterDto) {

		int count = 0;
		count = masterIDUSubCategoryRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterIDUSubCategory row = new MasterIDUSubCategory();
		row.setCode(masterDto.getCode());
		row.setDescription(masterDto.getDescription());
		row.setName(masterDto.getName());
		row.setIsActive(true);
		row.setIsDelete(false);
		row = masterIDUSubCategoryRepository.save(row);

		masterDto.setId(row.getId());
		masterDto.setCode(row.getCode());
		masterDto.setDescription(row.getName());
		masterDto.setName(row.getName());
		return masterDto;

	}

	@Cacheable(value = "AllMasterProductTypeCache")
	public List<MasterDto> getProductType() {
		List<ProductTypesMaster> masterList = new ArrayList<ProductTypesMaster>();
		masterList = masterProductTypesRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getProductTypeName());
				// masterDto.setcode(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveProductType(MasterDto masterDto) {

		int count = 0;
		count = masterProductTypesRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		ProductTypesMaster masterProductTypes = new ProductTypesMaster();
		// masterProductTypes.setCode(masterDto.getCode());
		// masterProductTypes.setDescription(masterDto.getDescription());
		masterProductTypes.setProductTypeName(masterDto.getName());
		masterProductTypes.setIsActive(true);
		masterProductTypes.setIsDelete(false);
		masterProductTypes = masterProductTypesRepository.save(masterProductTypes);

		masterDto.setId(masterProductTypes.getId());
		// masterDto.setCode(masterProductTypes.getCode());
		// masterDto.setDescription(masterProductTypes.getName());
		masterDto.setName(masterProductTypes.getProductTypeName());
		return masterDto;

	}

	@Cacheable(value = "AllMasterMachineCache")
	public List<MasterDto> getMachine() {
		List<Machine> masterList = new ArrayList<Machine>();
		masterList = machineRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getMachineName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveMachine(MasterDto masterDto) {
		int count = 0;
		count = machineRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		Machine machine = new Machine();
		machine.setDescription(masterDto.getDescription());
		machine.setMachineName(masterDto.getName());
		machine.setIsActive(true);
		machine.setIsDelete(false);
		machine = machineRepository.save(machine);

		masterDto.setId(machine.getId());
		masterDto.setDescription(machine.getDescription());
		masterDto.setName(machine.getMachineName());
		return masterDto;

	}

	@Cacheable(value = "AllMasterFacilityTypeCache")
	public List<MasterDto> getFacilityType() {
		List<Object[]> masterList = facilityTypeRepository.findFacilityTypeByIsDelete();
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null && !masterList.isEmpty()) {
			masterDtoList = masterList.stream().map(object -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(object[0] != null ? Long.valueOf(object[0].toString()) : null);
				masterDto.setName(object[1] != null ? object[1].toString().trim() : null);
				masterDto.setDescription(object[2] != null ? object[2].toString() : null);
				return masterDto;
			}).collect(Collectors.toList());
		}
		return masterDtoList;
	}

	public MasterDto saveFacilityType(MasterDto masterDto) {
		int count = 0;
		count = facilityTypeRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		FacilityType facilityType = new FacilityType();
		// facilityType.setCode(masterDto.getCode());
		facilityType.setDescription(masterDto.getDescription());
		facilityType.setFacilityTypeName(masterDto.getName());
		facilityType.setIsActive(true);
		facilityType.setIsDelete(false);
		facilityType = facilityTypeRepository.save(facilityType);

		masterDto.setId(facilityType.getId());
		masterDto.setDescription(facilityType.getDescription());
		masterDto.setName(facilityType.getFacilityTypeName());
		return masterDto;
	}

	@Cacheable(value = "AllMasterProductUomCache")
	public List<MasterDto> getProductUom() {
		List<ProductUomMaster> masterList = new ArrayList<ProductUomMaster>();
		masterList = productUomRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getUomName());
				// masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterRelationTypeCache")
	public List<MasterDto> getRelationType() {
		List<MasterRelationType> masterList = new ArrayList<MasterRelationType>();
		masterList = masterRelationTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterOccupationTruckerCache")
	public List<MasterDto> getOccupationTrucker() {
		List<MasterOccupationTrucker> masterList = new ArrayList<MasterOccupationTrucker>();
		masterList = masterOccupationTruckerRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList.stream().sorted(Comparator.comparing(MasterDto::getName)).collect(Collectors.toList());
	}

	public List<MasterDto> getAllMasterArtRegimenAction() {

		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		List<MasterArtRegimenAction> regimenActionList = masterArtRegimenActionRepository.findAll();
		if (regimenActionList != null) {
			for (MasterArtRegimenAction action : regimenActionList) {
				MasterDto master = new MasterDto();
				master.setCode(action.getCode());
				master.setDescription(action.getDescription());
				master.setId(action.getId());
				master.setName(action.getName());
				masterDtoList.add(master);
			}
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterMigrantOccupationCache")
	public List<MasterDto> getMigrantOccupation() {
		List<MasterMigrantOccupation> masterList = new ArrayList<MasterMigrantOccupation>();
		masterList = masterMigrantOccupationRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList.stream().sorted(Comparator.comparing(MasterDto::getName)).collect(Collectors.toList());
	}

	@Cacheable(value = "AllMasterHivScreeningStatusCache")
	public List<MasterDto> getHivScreeningStatus() {
		List<MasterHivScreeningStatus> masterList = new ArrayList<MasterHivScreeningStatus>();
		masterList = masterHivScreeningStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				// masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterMigrationPatternCache")
	public List<MasterDto> getMigrationPattern() {
		List<MasterMigrationPattern> masterList = new ArrayList<MasterMigrationPattern>();
		masterList = masterPatternOfMigrationRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterIpcTopicCache")
	public List<MasterDto> getIPCTopic() {
		List<MasterIPCTopic> masterList = new ArrayList<MasterIPCTopic>();
		masterList = masterIpcTopicsRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterTakeHomeDaysCache")
	public List<MasterDto> getTakeHomeDays() {
		List<MasterTakeHomeDays> masterList = new ArrayList<MasterTakeHomeDays>();
		masterList = masterTakeHomeDaysRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				// masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveTakeHomeDays(MasterDto masterDto) {

		int count = 0;
		count = masterTakeHomeDaysRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterTakeHomeDays row = new MasterTakeHomeDays();
		// row.setCode(masterDto.getCode());
		row.setDescription(masterDto.getDescription());
		row.setName(masterDto.getName());
		row.setIsActive(true);
		row.setIsDelete(false);
		row = masterTakeHomeDaysRepository.save(row);

		masterDto.setId(row.getId());
		// masterDto.setCode(row.getCode());
		masterDto.setDescription(row.getName());
		masterDto.setName(row.getName());
		return masterDto;

	}

	@Cacheable(value = "AllMasterRelationshipCache")
	public List<MasterDto> getRelationship() {
		List<MasterRelationship> masterList = new ArrayList<MasterRelationship>();
		masterList = masterRelationshipRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterOstAssessmentReasonCache")
	public List<MasterDto> getOstAssessmentReason() {
		List<MasterOstAssessmentReason> masterList = new ArrayList<MasterOstAssessmentReason>();
		masterList = masterOstAssessmentReasonRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				// masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveOstAssessmentReason(MasterDto masterDto) {

		int count = 0;
		count = masterOstAssessmentReasonRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterOstAssessmentReason row = new MasterOstAssessmentReason();
		// row.setCode(masterDto.getCode());
		row.setDescription(masterDto.getDescription());
		row.setName(masterDto.getName());
		row.setIsActive(true);
		row.setIsDelete(false);
		row = masterOstAssessmentReasonRepository.save(row);

		masterDto.setId(row.getId());
		// masterDto.setCode(row.getCode());
		masterDto.setDescription(row.getName());
		masterDto.setName(row.getName());
		return masterDto;

	}

	@Cacheable(value = "AllMasterFollowupByCache")
	public List<MasterDto> getFollowupBy() {
		List<MasterFollowUpBy> masterList = new ArrayList<MasterFollowUpBy>();
		masterList = masterFollowUpByRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				// masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getAllIndentStatus() {
		List<IndentStatusMaster> masterList = indentStatusMasterRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getStatus());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getAllContractStatus() {
		List<ContractStatusMaster> masterList = contractStatusMasterRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getStatus());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getAllConsignmentStatus() {
		List<ConsignmentStatusMaster> masterList = consignmentStatusMasterRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getStatus());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getAllReceiptStatus() {
		List<ReceiptStatusMaster> masterList = receiptStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getStatus());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getAllFacilityConsignmentStatus() {
		List<FacilityConsignmentStatusMaster> masterList = facilityConsignmentStatusMasterRepository
				.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getStatus());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getAllFacilityReceiptStatus() {
		List<FacilityReceiptStatusMaster> masterList = facilityReceiptStatusMasterRepository
				.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getStatus());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getAllRelocationRequestStatus() {
		List<FacilityRelocationRequestStatusMaster> masterList = facilityRelocationRequestStatusMasterRepository
				.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getStatus());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getAllReconciliationStatus() {
		List<ReconciliationStatusMaster> masterList = reconciliationStatusMasterRepository
				.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getStatus());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getBloodBankOwnedBy() {
		List<MasterBloodBankOwnedBy> masterList = masterBloodBankOwnedByRepository.findByIsDelete(Boolean.FALSE);

		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getBloodbankType() {
		List<MasterBloodBankType> masterList = masterBloodBankTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterOpioidOverdoseReasonsCache")
	public List<MasterDto> getOpioidOverdoseReasons() {
		List<MasterOpioidOverdoseReasons> masterList = new ArrayList<MasterOpioidOverdoseReasons>();
		masterList = masterOpioidOverdoseReasonsRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveOpioidOverdoseReasons(MasterDto masterDto) {

		int count = 0;
		count = masterOpioidOverdoseReasonsRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterOpioidOverdoseReasons row = new MasterOpioidOverdoseReasons();
		row.setCode(masterDto.getCode());
		row.setDescription(masterDto.getDescription());
		row.setName(masterDto.getName());
		row.setIsActive(true);
		row.setIsDelete(false);
		row = masterOpioidOverdoseReasonsRepository.save(row);

		masterDto.setId(row.getId());
		masterDto.setCode(row.getCode());
		masterDto.setDescription(row.getName());
		masterDto.setName(row.getName());
		return masterDto;

	}

//	@Cacheable(value = "AllMasterStiRtiDiagnosisTypeCache")
//	public List<MasterDto> getStiRtiDiagnosisType() {
//		List<MasterStiRtiDiagnosisType> masterList = new ArrayList<MasterStiRtiDiagnosisType>();
//		masterList = masterStiRtiDiagnosisTypeRepository.findByIsDelete(Boolean.FALSE);
//		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
//		if (!masterList.isEmpty()) {
//			masterList.forEach(row -> {
//				MasterDto masterDto = new MasterDto();
//				masterDto.setId(row.getId());
//				masterDto.setName(row.getName());
//				masterDto.setCode(row.getCode());
//				masterDto.setDescription(row.getDescription());
//				masterDtoList.add(masterDto);
//			});
//		}
//		return masterDtoList;
//	}

	@Cacheable(value = "AllMasterClinicalTreatmentTypeCache")
	public List<MasterDto> getClinicalTreatmentType() {
		List<MasterClinicalTreatmentType> masterList = new ArrayList<MasterClinicalTreatmentType>();
		masterList = masterClinicalTreatmentTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterTreatmentAbssessSitesCache")
	public List<MasterDto> getTreatmentAbssessSites() {
		List<MasterTreatmentAbssessSites> masterList = new ArrayList<MasterTreatmentAbssessSites>();
		masterList = masterTreatmentAbssessSitesRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterTreatmentAbssessTypeCache")
	public List<MasterDto> getTreatmentAbssessType() {
		List<MasterTreatmentAbssessType> masterList = new ArrayList<MasterTreatmentAbssessType>();
		masterList = masterTreatmentAbssessTypesRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterDiagnosisRecordedAtCache")
	public List<MasterDto> getDiagnosisRecordedAt() {
		List<MasterDiagnosisRecordedAt> masterList = new ArrayList<MasterDiagnosisRecordedAt>();
		masterList = masterDiagnosisRecordedAtRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterOstFollowupReasonCache")
	public List<MasterDto> getOstFollowupReason() {
		List<MasterOstFollowUpReason> masterList = new ArrayList<MasterOstFollowUpReason>();
		masterList = masterOstFollowupReasonRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveOstFollowupReason(MasterDto masterDto) {

		int count = 0;
		count = masterOstFollowupReasonRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterOstFollowUpReason row = new MasterOstFollowUpReason();
		row.setCode(masterDto.getCode());
		row.setDescription(masterDto.getDescription());
		row.setName(masterDto.getName());
		row.setIsActive(true);
		row.setIsDelete(false);
		row = masterOstFollowupReasonRepository.save(row);

		masterDto.setId(row.getId());
		masterDto.setCode(row.getCode());
		masterDto.setDescription(row.getName());
		masterDto.setName(row.getName());
		return masterDto;

	}

	@Cacheable(value = "AllMasterOstFollowupSideEffectCache")
	public List<MasterDto> getOstFollowupSideEffect() {
		List<MasterOstFollowupSideEffect> masterList = new ArrayList<MasterOstFollowupSideEffect>();
		masterList = masterOstFollowupSideEffectRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveOstFollowupSideEffect(MasterDto masterDto) {

		int count = 0;
		count = masterOstFollowupSideEffectRepository.existsByOtherName(masterDto.getName());

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, masterDto.getName());
		}

		MasterOstFollowupSideEffect row = new MasterOstFollowupSideEffect();
		row.setCode(masterDto.getCode());
		row.setDescription(masterDto.getDescription());
		row.setName(masterDto.getName());
		row.setIsActive(true);
		row.setIsDelete(false);
		row = masterOstFollowupSideEffectRepository.save(row);

		masterDto.setId(row.getId());
		masterDto.setCode(row.getCode());
		masterDto.setDescription(row.getName());
		masterDto.setName(row.getName());
		return masterDto;

	}

	// Method to throw error
	private void throwError(String errorfield, String errorFieldValue) {
		List<ErrorDto> errorDtoList = new ArrayList<>();
		List<String> detailsSimplified = new ArrayList<String>();
		ErrorDto errorDto = new ErrorDto();
		errorDto.setField(errorfield);
		errorDto.setDescription(Constants.DUPLICATE_FOUND + "'" + errorFieldValue + "'");
		errorDtoList.add(errorDto);
		detailsSimplified.add(errorDto.getDescription());
		ErrorResponse errorResponse = new ErrorResponse(errorDtoList.toString(), errorDtoList, detailsSimplified);
		throw new ServiceException(Constants.DUPLICATE_FOUND + " '" + errorFieldValue + "' ", errorResponse,
				HttpStatus.BAD_REQUEST);
	}

	@Cacheable(value = "AllMasterMultimonthDispensationCache")
	public List<MasterDto> getMultimonthDispensation() {
		List<MasterMultimonthDispensation> masterList = new ArrayList<MasterMultimonthDispensation>();
		masterList = masterMultimonthDispensationRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterQmsTestStatusCache")
	public List<MasterDto> getQmsTestStatus() {
		List<MasterQmsTestStatus> masterList = new ArrayList<MasterQmsTestStatus>();
		masterList = masterQmsTestStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterLacTypeCache")
	public List<MasterDto> getLacType() {
		List<MasterLacType> masterList = new ArrayList<MasterLacType>();
		masterList = masterLacTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}
	
	@Cacheable(value = "GenderAndPregnancyTypeCache")
	public Map<String, Object> getGenderAndPregnancyType() {
		logger.debug("Entering into method getGender - In MasterDataService");
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<MasterGender> genderList = new ArrayList<MasterGender>();
		genderList = masterGenderRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterGenderDtoList = new ArrayList<MasterDto>();
		if (genderList != null) {
			genderList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterGenderDtoList.add(masterDto);
			});
		}
		List<MasterPregnancyTypeCase> pregnancyTypeList = new ArrayList<MasterPregnancyTypeCase>();
		pregnancyTypeList = masterPregnancyTypeCaseRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterPregnancyTypeDtoList = new ArrayList<MasterDto>();
		if (!pregnancyTypeList.isEmpty()) {
			pregnancyTypeList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterPregnancyTypeDtoList.add(masterDto);
			});
		}
		responseMap.put("genders", masterGenderDtoList);
		responseMap.put("pregnancyTypes", masterPregnancyTypeDtoList);
		return responseMap ;
	}
	
	@Cacheable(value = "PurposeOfVisitCache")
	public List<MasterDto> getPurposeOfVisit(Integer facilityTypeId) {
		List<MasterPurposeOfVisit> masterList = new ArrayList<MasterPurposeOfVisit>();
		masterList = masterPurposeOfVisitRepository.findByisDeleteAndFacilityTypeId(Boolean.FALSE, facilityTypeId);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}
	@Cacheable(value = "AppointmentStatusCache")
	public List<MasterDto> getAppointmentStatusList() {
		List<MasterAppointmentStatus> masterList = new ArrayList<MasterAppointmentStatus>();
		masterList = masterAppointmentStatusRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}
	@Cacheable(value = "NacoProfileCache")
	public List<NacoProfileDto> getNacoProfileData() {
		List<NacoProfileEntity> masterList = new ArrayList<NacoProfileEntity>();
		masterList = nacoProfileRepository.findByIsDelete(Boolean.FALSE);
		List<NacoProfileDto> nacoProfileData = new ArrayList<NacoProfileDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				NacoProfileDto nacoProfileDto = new NacoProfileDto();
				nacoProfileDto.setId(row.getId());
				nacoProfileDto.setType(row.getType());
				nacoProfileDto.setValue(row.getValue());
			nacoProfileData.add(nacoProfileDto);
			});
		}
		return nacoProfileData;
	}

	@Cacheable(value = "AllMasterAdherenceRemarksCache")
	public List<MasterDto> getAdherenceRemarks() {
		List<MasterAdherenceRemarks> masterList = new ArrayList<MasterAdherenceRemarks>();
		masterList = masterAdherenceRemarksRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}
	
	@Cacheable(value = "AllReferralTypeCache")
	public List<MasterDto> getReferralType() {
		List<MasterReferralType> masterList = new ArrayList<MasterReferralType>();
		masterList = masterReferralTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}
	
	@Cacheable(value = "AllSacepReferralReasonCache")
	public List<MasterDto> getSacepReferralReason() {
		List<MasterSacepReferralReason> masterList = new ArrayList<MasterSacepReferralReason>();
		masterList = masterSacepReferralReasonRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	/**
	 * @return
	 */
	@Cacheable(value = "AllSacepReviewTypesCache")
	public List<MasterDto> getSacepReviewTypes() {
		List<MasterTypeOfSacepReview> masterList = new ArrayList<MasterTypeOfSacepReview>();
		masterList = masterTypeOfSacepReviewRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public MasterDto saveSacepReviewTypes(MasterDto masterDto) {

			int count = 0;
			count = masterTypeOfSacepReviewRepository.existsByOtherName(masterDto.getName());

			if (count != 0) {
				logger.error(Constants.DUPLICATE_FOUND);
				String errorfield = "title";
				logger.debug("call throwError if duplicate found");
				throwError(errorfield, masterDto.getName());
			}

			MasterTypeOfSacepReview row = new MasterTypeOfSacepReview();
			row.setCode(masterDto.getCode());
			row.setDescription(masterDto.getDescription());
			row.setName(masterDto.getName());
			row.setIsActive(true);
			row.setIsDelete(false);
			row = masterTypeOfSacepReviewRepository.save(row);

			masterDto.setId(row.getId());
			masterDto.setCode(row.getCode());
			masterDto.setDescription(row.getName());
			masterDto.setName(row.getName());
			return masterDto;

		}

	public List<MasterDto> getGeographicalLevels() {
		List<MasterDashboardGeographicalLevel> masterList = new ArrayList<MasterDashboardGeographicalLevel>();
		masterList = masterDashboardGeographicalLevelRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getDashboardGender() {
		List<MasterDashboardGender> masterList = new ArrayList<MasterDashboardGender>();
		masterList = masterDashboardGenderRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getDashboardTypology() {
		List<MasterDashboardTypology> masterList = new ArrayList<MasterDashboardTypology>();
		masterList = masterDashboardTypologyRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	public List<MasterDto> getAgeGroup() {
		List<MasterAgeGroup> masterList = new ArrayList<MasterAgeGroup>();
		masterList = masterAgeGroupRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}
	
	public BigPicMiniMasterDto getMiniMasterValues() {
		
		BigPicMiniMasterDto miniMasterDto = new BigPicMiniMasterDto();
		
		List<LocationDto> masterState = addressService.getStateList();
		miniMasterDto.setMasterState(masterState);
		
		List<MasterDto> masterGender = getDashboardGender();
		miniMasterDto.setMasterGender(masterGender);
		
		List<MasterDto> masterAgeGroup = getAgeGroup();
		miniMasterDto.setMasterAgeGroup(masterAgeGroup);
		
		List<MasterDto> masterTypology = getDashboardTypology();
		miniMasterDto.setMasterTypology(masterTypology);
		
		List<MasterDto> masterTargetIndicator = getTargetIndicator();
		miniMasterDto.setMasterTargetIndicator(masterTargetIndicator);
		
		return miniMasterDto;
	}

	private List<MasterDto> getTargetIndicator() {
		List<MasterTargetIndicatorType> masterList = new ArrayList<MasterTargetIndicatorType>();
		masterList = masterTargetIndicatorTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "AllMasterArtCounsellingTypeCache")
	public List<MasterDto> getArtCounsellingType() {
		List<CounsellingType> masterList = new ArrayList<CounsellingType>();
		masterList = masterArtCounsellingTypeRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getCounsellingType());
				//masterDto.setCode(row.getCode());
				//masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}

	@Cacheable(value = "TbSymptomsCache")
	public List<MasterDto> getTbSymptoms() {
		List<MasterTbSymptoms> masterList = new ArrayList<MasterTbSymptoms>();
		masterList = masterTbSymptomsRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}
	
	@Cacheable(value = "StiSymptomsCache")
	public List<MasterDto> getStiSymptoms() {
		List<MasterStiSymptoms> masterList = new ArrayList<MasterStiSymptoms>();
		masterList = masterStiSymptomsRepository.findByIsDelete(Boolean.FALSE);
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (!masterList.isEmpty()) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setName(row.getName());
				masterDto.setCode(row.getCode());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}
	
	public List<MasterDto> getQuestionList() {
		List<MasterPrisonQuestion> masterList = new ArrayList<MasterPrisonQuestion>();
		// masterList = masterPrisonQuestionRepository.findByIsDelete(Boolean.FALSE);
		masterList = masterPrisonQuestionRepository.findAllByOrderByIdAsc();
		List<MasterDto> masterDtoList = new ArrayList<MasterDto>();
		if (masterList != null) {
			masterList.forEach(row -> {
				MasterDto masterDto = new MasterDto();
				masterDto.setId(row.getId());
				masterDto.setCode(row.getQuestionNumber());
				masterDto.setName(row.getQuestion());
				masterDto.setDescription(row.getDescription());
				masterDtoList.add(masterDto);
			});
		}
		return masterDtoList;
	}
	
	//@Cacheable(value = "AllMaritalStatusCache")
	public List<DesignationDto> getGBDesignation() {
		logger.debug("Entering into method getGBDesignation - In MasterDataService");
		List<Designation> designationList = new ArrayList<Designation>();
		designationList = designationRepository.getAllGBDesignation();
		List<DesignationDto> designationDtoList = new ArrayList<DesignationDto>();
		if (designationList != null) {
			designationList.forEach(masterClientStatus -> {
				DesignationDto designationDto = new DesignationDto();
				designationDto.setId(masterClientStatus.getId());
				designationDto.setTitle(masterClientStatus.getTitle());
				designationDto.setDescription(masterClientStatus.getDescription());
				designationDtoList.add(designationDto);
			});
		}
		return designationDtoList.stream().sorted(Comparator.comparing(DesignationDto::getTitle)).collect(Collectors.toList());
	}
	

}

