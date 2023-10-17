package gov.naco.soch.admin.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import gov.naco.soch.constant.FileUploadConstants;
import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.dto.LoginResponseDto;
import gov.naco.soch.dto.NgoDocumentsDto;
import gov.naco.soch.dto.NgoProjectsDto;
import gov.naco.soch.entity.ActivityReportEntity;
import gov.naco.soch.entity.AnnualReportEntity;
import gov.naco.soch.entity.AuditReportEntity;
import gov.naco.soch.entity.Facility;
import gov.naco.soch.entity.FacilityType;
import gov.naco.soch.entity.FacilityTypeDivisionMapping;
import gov.naco.soch.entity.FcraRegistrationEntity;
import gov.naco.soch.entity.NgoContractCertEntity;
import gov.naco.soch.entity.NgoDocumentsEntity;
import gov.naco.soch.entity.NgoProjectTypologyMapping;
import gov.naco.soch.entity.NgoProjectsEntity;
import gov.naco.soch.entity.ReturnFileEntity;
import gov.naco.soch.entity.SacsAnnualReportEntity;
import gov.naco.soch.entity.SacsAuditReportEntity;
import gov.naco.soch.entity.SoeDocumentEntity;
import gov.naco.soch.entity.TaxRegistrationEntity;
import gov.naco.soch.entity.UcDocumentEntity;
import gov.naco.soch.entity.otherDocumentsEntity;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.DivisionMapperUtil;
import gov.naco.soch.mapper.FacilityMapperUtil;
import gov.naco.soch.projection.NgoMemberListProjection;
import gov.naco.soch.repository.ActivityReportRepository;
import gov.naco.soch.repository.AddressRepository;
import gov.naco.soch.repository.AnnualReportRepository;
import gov.naco.soch.repository.AuditReportRepository;
import gov.naco.soch.repository.SacsAnnualReportRepository;
import gov.naco.soch.repository.SacsAuditReportRepository;
import gov.naco.soch.repository.FacilityRepository;
import gov.naco.soch.repository.FcraRegistrationRepository;
import gov.naco.soch.repository.NgoContractRepository;
import gov.naco.soch.repository.NgoDocumentRepository;
import gov.naco.soch.repository.NgoProjectTypologyMappingRepository;
import gov.naco.soch.repository.NgoProjectsRepository;
import gov.naco.soch.repository.OtherDocumentsRepository;
import gov.naco.soch.repository.ReturnFileRepository;
import gov.naco.soch.repository.SoeDocumentRepository;
import gov.naco.soch.repository.StateRepository;
import gov.naco.soch.repository.TaxRegistrationRepository;
import gov.naco.soch.repository.UcDocumentRepository;
import gov.naco.soch.util.UserUtils;
import gov.naco.soch.util.CommonConstants;


//service class interact data with database

@Transactional
@Service
public class NgoDocumentService {

	private static final Logger logger = LoggerFactory.getLogger(NgoDocumentService.class);

	@Value("${exportRecordsLimit}")
	private Integer exportRecordsLimit;

	@Autowired
	FacilityRepository facilityRepository;
	
	@Autowired
	AddressRepository addressRepository;

	@Autowired
	StateRepository stateRepository;
	
	@Autowired
	private FileUploadService fileUploadService;
		
	@Autowired
	private NgoDocumentRepository ngoDocumentRepository;
	
	@Autowired
	private NgoContractRepository ngoContractRepository;
	
	@Autowired
	private ActivityReportRepository activityReportRepository;
	
	@Autowired
	private AuditReportRepository auditReportRepository;
	
	@Autowired
	private SacsAuditReportRepository sacsAuditReportRepository;
	
	@Autowired
	private FcraRegistrationRepository fcraRegistrationRepository;
	
	@Autowired
	private OtherDocumentsRepository otherDocumentRepository;
	
	@Autowired
	private AnnualReportRepository annualReportRepository;
	
	@Autowired
	private SacsAnnualReportRepository sacsAnnualReportRepository;
	
	@Autowired
	private SoeDocumentRepository soeDocumentRepository;
	
	@Autowired
	private UcDocumentRepository ucDocumentRepository;
	
	@Autowired
	private ReturnFileRepository returnFileRepository;
	
	@Autowired
	private TaxRegistrationRepository taxRegistrationRepository;
	
	@Autowired
	private NgoProjectsRepository ngoProjectsRepository;
	
	@Autowired
	private NgoProjectTypologyMappingRepository ngoProjectTypologyMappingRepository;
	
	@Autowired
	private Environment env;

	private String fileStorageLocation = null;	
	
	/**
	 * To through By passing String
	 * 
	 * @param errorString
	 * @param errorType
	 */
	private void throwErrorManually(String errorString, String errorType) {
		List<ErrorDto> errorDtoList = new ArrayList<>();
		List<String> detailsSimplified = new ArrayList<String>();
		ErrorDto errorDto = new ErrorDto();
		errorDto.setField(errorType);
		errorDto.setDescription(errorString);
		errorDtoList.add(errorDto);
		detailsSimplified.add(errorDto.getDescription());
		ErrorResponse errorResponse = new ErrorResponse(errorDtoList.toString(), errorDtoList, detailsSimplified);
		throw new ServiceException(errorString, errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	public static Pageable parenthesisEncapsulation(final Pageable pageable) {

		Sort sort = Sort.by(Collections.emptyList());
		for (final Sort.Order order : pageable.getSort()) {
			if (order.getProperty().matches("^\\(.*\\)$")) {
				sort = sort.and(JpaSort.unsafe(order.getDirection(), order.getProperty()));
			} else {
				sort = sort.and(Sort.by(order.getDirection(), order.getProperty()));
			}
		}
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
	}
	
	/*
	 * Save Contract Certificate Details and upload document
	 */
	public NgoDocumentsDto uploadContractLetter(@RequestBody NgoDocumentsDto ngoDocumentsDto, MultipartFile file){
		logger.debug("Entering into method uploadContractLetter with NgoDocumentsDto->{}:", ngoDocumentsDto);
		NgoContractCertEntity	ngoContractCertEntity = new NgoContractCertEntity();
		
		ngoContractCertEntity.setFacilityId(ngoDocumentsDto.getFacilityId());
		ngoContractCertEntity.setSacsId(ngoDocumentsDto.getSacsId());
		ngoContractCertEntity.setFinancialYear(ngoDocumentsDto.getFinancialYear());	   
		ngoContractCertEntity.setContractValiditydate(ngoDocumentsDto.getContractValiditydate());
		ngoContractCertEntity.setRemarks(ngoDocumentsDto.getRemarks());
		ngoContractCertEntity.setIsActive(true);
		ngoContractCertEntity.setIsDelete(false);
		
		if (file != null && !file.isEmpty()) {
			try {
				if (file.getContentType().equals("application/pdf")) {
					 // Save file on system
			        if (!file.getOriginalFilename().isEmpty()) {
						this.fileStorageLocation = fileUploadService.setLocation(env,ngoDocumentsDto.getFolderName());				
						fileUploadService.storeFile(file, ngoDocumentsDto.getFileName());    
			        } else {
			            throw new Exception();
			        }
			        ngoContractCertEntity.setFileName(ngoDocumentsDto.getFileName());
			        ngoContractCertEntity.setFileType(ngoDocumentsDto.getFileType());
			        ngoContractCertEntity.setFilePath(this.fileStorageLocation);
				} else {
					throwErrorManually("Document must be pdf format................!", "Format Error");
				}
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
				throwErrorManually("Document cannot upload!", "Upload Error");
			}
		}	
		ngoContractRepository.save(ngoContractCertEntity);
		ngoDocumentsDto.setId(ngoContractCertEntity.getId());
		logger.debug("Terminate method uploadContractLetter with ngoDocumentsDto->{}:", ngoDocumentsDto);
		 return ngoDocumentsDto;
	}
	/*
	 * Save Social Registration Certificate Details and upload document
	 */
	public NgoDocumentsDto uploadRegCert(@RequestBody NgoDocumentsDto ngoDocumentsDto, MultipartFile file){
		logger.debug("Entering into method uploadRegCert with NgoDocumentsDto->{}:", ngoDocumentsDto);
		NgoDocumentsEntity	ngoDocumentsEntity = new NgoDocumentsEntity();
		
		ngoDocumentsEntity.setFacilityId(ngoDocumentsDto.getFacilityId());
		ngoDocumentsEntity.setSacsId(ngoDocumentsDto.getSacsId());
		ngoDocumentsEntity.setFinancialYear(ngoDocumentsDto.getFinancialYear());	    
		ngoDocumentsEntity.setSocietyValiditydate(ngoDocumentsDto.getSocietyValiditydate());
	    ngoDocumentsEntity.setRemarks(ngoDocumentsDto.getRemarks());
		ngoDocumentsEntity.setIsActive(true);
		ngoDocumentsEntity.setIsDelete(false);
		
		if (file != null && !file.isEmpty()) {
			try {
				if (file.getContentType().equals("application/pdf")) {
					 // Save file on system
			        if (!file.getOriginalFilename().isEmpty()) {
						this.fileStorageLocation = fileUploadService.setLocation(env,ngoDocumentsDto.getFolderName());				
						fileUploadService.storeFile(file, ngoDocumentsDto.getFileName());    
			        } else {
			            throw new Exception();
			        }
			        ngoDocumentsEntity.setFileName(ngoDocumentsDto.getFileName());
			        ngoDocumentsEntity.setFileType(ngoDocumentsDto.getFileType());
			        ngoDocumentsEntity.setFilePath(this.fileStorageLocation);
				} else {
					throwErrorManually("Document must be pdf format................!", "Format Error");
				}
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
				throwErrorManually("Document cannot upload!", "Upload Error");
			}
		}	
		ngoDocumentRepository.save(ngoDocumentsEntity);
		ngoDocumentsDto.setId(ngoDocumentsEntity.getId());
		logger.debug("Terminate method uploadRegCert with ngoDocumentsDto->{}:", ngoDocumentsDto);
		 return ngoDocumentsDto;
	}
	
	/*
	 * Save Return File Details and upload document
	 */
	public NgoDocumentsDto uploadReturnFile(@RequestBody NgoDocumentsDto ngoDocumentsDto, MultipartFile file){
		logger.debug("Entering into method uploadReturnFile with NgoDocumentsDto->{}:", ngoDocumentsDto);
		ReturnFileEntity returnFileEntity = new ReturnFileEntity();
		
		returnFileEntity.setFacilityId(ngoDocumentsDto.getFacilityId());
		returnFileEntity.setSacsId(ngoDocumentsDto.getSacsId());
		returnFileEntity.setFinancialYear(ngoDocumentsDto.getFinancialYear());	    
		returnFileEntity.setRemarks(ngoDocumentsDto.getRemarks());
		returnFileEntity.setIsActive(true);
		returnFileEntity.setIsDelete(false);
		
		if (file != null && !file.isEmpty()) {
			try {
				if (file.getContentType().equals("application/pdf")) {
					 // Save file on system
			        if (!file.getOriginalFilename().isEmpty()) {
						this.fileStorageLocation = fileUploadService.setLocation(env,ngoDocumentsDto.getFolderName());				
						fileUploadService.storeFile(file, ngoDocumentsDto.getFileName());    
			        } else {
			            throw new Exception();
			        }
			        returnFileEntity.setFileName(ngoDocumentsDto.getFileName());
			        returnFileEntity.setFileType(ngoDocumentsDto.getFileType());
			        returnFileEntity.setFilePath(this.fileStorageLocation); 
			     

				} else {
					throwErrorManually("Document must be pdf format................!", "Format Error");
				}
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
				throwErrorManually("Document cannot upload!", "Upload Error");
			}
		}		
		returnFileRepository.save(returnFileEntity);
		ngoDocumentsDto.setId(returnFileEntity.getId());
		logger.debug("Terminate method uploadRegCert with ngoDocumentsDto->{}:", ngoDocumentsDto);
		 return ngoDocumentsDto;
	}
	
	/*
	 * Save FCRA Details and upload document
	 */
	public NgoDocumentsDto uploadFcra(@RequestBody NgoDocumentsDto ngoDocumentsDto, MultipartFile file){
		logger.debug("Entering into method uploadReturnFile with NgoDocumentsDto->{}:", ngoDocumentsDto);
		FcraRegistrationEntity fcraRegistrationEntity = new FcraRegistrationEntity();
		
		fcraRegistrationEntity.setFacilityId(ngoDocumentsDto.getFacilityId());
		fcraRegistrationEntity.setSacsId(ngoDocumentsDto.getSacsId());
		fcraRegistrationEntity.setFinancialYear(ngoDocumentsDto.getFinancialYear());	    
		fcraRegistrationEntity.setRemarks(ngoDocumentsDto.getRemarks());
		fcraRegistrationEntity.setIsActive(true);
		fcraRegistrationEntity.setIsDelete(false);
		
		if (file != null && !file.isEmpty()) {
			try {
				if (file.getContentType().equals("application/pdf")) {
					 // Save file on system
			        if (!file.getOriginalFilename().isEmpty()) {
						this.fileStorageLocation = fileUploadService.setLocation(env,ngoDocumentsDto.getFolderName());				
						fileUploadService.storeFile(file, ngoDocumentsDto.getFileName());    
			        } else {
			            throw new Exception();
			        }
			        fcraRegistrationEntity.setFileName(ngoDocumentsDto.getFileName());
			        fcraRegistrationEntity.setFileType(ngoDocumentsDto.getFileType());
			        fcraRegistrationEntity.setFilePath(this.fileStorageLocation); 
			     

				} else {
					throwErrorManually("Document must be pdf format................!", "Format Error");
				}
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
				throwErrorManually("Document cannot upload!", "Upload Error");
			}
		}	
		fcraRegistrationRepository.save(fcraRegistrationEntity);
		ngoDocumentsDto.setId(fcraRegistrationEntity.getId());
		logger.debug("Terminate method uploadRegCert with ngoDocumentsDto->{}:", ngoDocumentsDto);
		 return ngoDocumentsDto;
	}
	
	/*
	 * Save Activity Report Details and upload document
	 */
	public NgoDocumentsDto uploadActivityReport(@RequestBody NgoDocumentsDto ngoDocumentsDto, MultipartFile file){
		logger.debug("Entering into method uploadReturnFile with NgoDocumentsDto->{}:", ngoDocumentsDto);
		ActivityReportEntity activityReportEntity = new ActivityReportEntity();
		
		activityReportEntity.setFacilityId(ngoDocumentsDto.getFacilityId());
		activityReportEntity.setSacsId(ngoDocumentsDto.getSacsId());
		activityReportEntity.setProjectId(ngoDocumentsDto.getProjectId());
		activityReportEntity.setMonthId(ngoDocumentsDto.getMonthId());	
		activityReportEntity.setFinancialYear(ngoDocumentsDto.getFinancialYear());	    
		activityReportEntity.setRemarks(ngoDocumentsDto.getRemarks());
		activityReportEntity.setIsActive(true);
		activityReportEntity.setIsDelete(false);
		
		if (file != null && !file.isEmpty()) {
			try {
				if (file.getContentType().equals("application/pdf")) {
					 // Save file on system
			        if (!file.getOriginalFilename().isEmpty()) {
						this.fileStorageLocation = fileUploadService.setLocation(env,ngoDocumentsDto.getFolderName());				
						fileUploadService.storeFile(file, ngoDocumentsDto.getFileName());    
			        } else {
			            throw new Exception();
			        }
			        activityReportEntity.setFileName(ngoDocumentsDto.getFileName());
			        activityReportEntity.setFileType(ngoDocumentsDto.getFileType());
			        activityReportEntity.setFilePath(this.fileStorageLocation); 
			     

				} else {
					throwErrorManually("Document must be pdf format................!", "Format Error");
				}
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
				throwErrorManually("Document cannot upload!", "Upload Error");
			}
		}	
		activityReportRepository.save(activityReportEntity);
		ngoDocumentsDto.setId(activityReportEntity.getId());
		logger.debug("Terminate method uploadRegCert with ngoDocumentsDto->{}:", ngoDocumentsDto);
		 return ngoDocumentsDto;
	}
	

	/*
	 * Save Audit Report Details and upload document
	 */
	public NgoDocumentsDto uploadAuditReport(@RequestBody NgoDocumentsDto ngoDocumentsDto, MultipartFile file){
		logger.debug("Entering into method uploadAuditReport with NgoDocumentsDto->{}:", ngoDocumentsDto);
		AuditReportEntity auditReportEntity = new AuditReportEntity();
		
		auditReportEntity.setFacilityId(ngoDocumentsDto.getFacilityId());
		auditReportEntity.setSacsId(ngoDocumentsDto.getSacsId());
		auditReportEntity.setFinancialYear(ngoDocumentsDto.getFinancialYear());	    
		auditReportEntity.setRemarks(ngoDocumentsDto.getRemarks());
		auditReportEntity.setIsActive(true);
		auditReportEntity.setIsDelete(false);
		
		if (file != null && !file.isEmpty()) {
			try {
				if (file.getContentType().equals("application/pdf")) {
					 // Save file on system
			        if (!file.getOriginalFilename().isEmpty()) {
						this.fileStorageLocation = fileUploadService.setLocation(env,ngoDocumentsDto.getFolderName());				
						fileUploadService.storeFile(file, ngoDocumentsDto.getFileName());    
			        } else {
			            throw new Exception();
			        }
			        auditReportEntity.setFileName(ngoDocumentsDto.getFileName());
			        auditReportEntity.setFileType(ngoDocumentsDto.getFileType());
			        auditReportEntity.setFilePath(this.fileStorageLocation); 
			     

				} else {
					throwErrorManually("Document must be pdf format................!", "Format Error");
				}
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
				throwErrorManually("Document cannot upload!", "Upload Error");
			}
		}	
		auditReportRepository.save(auditReportEntity);
		ngoDocumentsDto.setId(auditReportEntity.getId());
		logger.debug("Terminate method uploadRegCert with ngoDocumentsDto->{}:", ngoDocumentsDto);
		 return ngoDocumentsDto;
	}
	
	/*
	 * Save SACS Audit Report Details and upload document
	 */
	public NgoDocumentsDto uploadSacsAuditReport(@RequestBody NgoDocumentsDto ngoDocumentsDto, MultipartFile file){
		logger.debug("Entering into method uploadSacsAuditReport with NgoDocumentsDto->{}:", ngoDocumentsDto);
		SacsAuditReportEntity auditReportEntity = new SacsAuditReportEntity();
		
		auditReportEntity.setFacilityId(ngoDocumentsDto.getFacilityId());
	//	auditReportEntity.setSacsId(ngoDocumentsDto.getSacsId());
		auditReportEntity.setFinancialYear(ngoDocumentsDto.getFinancialYear());	    
		auditReportEntity.setRemarks(ngoDocumentsDto.getRemarks());
		auditReportEntity.setIsActive(true);
		auditReportEntity.setIsDelete(false);
		
		if (file != null && !file.isEmpty()) {
			try {
				if (file.getContentType().equals("application/pdf")) {
					 // Save file on system
			        if (!file.getOriginalFilename().isEmpty()) {
						this.fileStorageLocation = fileUploadService.setLocation(env,ngoDocumentsDto.getFolderName());				
						fileUploadService.storeFile(file, ngoDocumentsDto.getFileName());    
			        } else {
			            throw new Exception();
			        }
			        auditReportEntity.setFileName(ngoDocumentsDto.getFileName());
			        auditReportEntity.setFileType(ngoDocumentsDto.getFileType());
			        auditReportEntity.setFilePath(this.fileStorageLocation); 
			     

				} else {
					throwErrorManually("Document must be pdf format................!", "Format Error");
				}
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
				throwErrorManually("Document cannot upload!", "Upload Error");
			}
		}	
		sacsAuditReportRepository.save(auditReportEntity);
		ngoDocumentsDto.setId(auditReportEntity.getId());
		logger.debug("Terminate method uploadSacsAuditReport with ngoDocumentsDto->{}:", ngoDocumentsDto);
		 return ngoDocumentsDto;
	}
	
	/*
	 * Save SACS Annual Report Details and upload document
	 */
	public NgoDocumentsDto uploadSacsAnnualReport(@RequestBody NgoDocumentsDto ngoDocumentsDto, MultipartFile file){
		logger.debug("Entering into method uploadAnnualReport with NgoDocumentsDto->{}:", ngoDocumentsDto);
		SacsAnnualReportEntity annualReportEntity = new SacsAnnualReportEntity();
		
		annualReportEntity.setFacilityId(ngoDocumentsDto.getFacilityId());
		//annualReportEntity.setSacsId(ngoDocumentsDto.getSacsId());
		annualReportEntity.setFinancialYear(ngoDocumentsDto.getFinancialYear());	    
		annualReportEntity.setRemarks(ngoDocumentsDto.getRemarks());
		annualReportEntity.setIsActive(true);
		annualReportEntity.setIsDelete(false);
		
		if (file != null && !file.isEmpty()) {
			try {
				if (file.getContentType().equals("application/pdf")) {
					 // Save file on system
			        if (!file.getOriginalFilename().isEmpty()) {
						this.fileStorageLocation = fileUploadService.setLocation(env,ngoDocumentsDto.getFolderName());				
						fileUploadService.storeFile(file, ngoDocumentsDto.getFileName());    
			        } else {
			            throw new Exception();
			        }
			        annualReportEntity.setFileName(ngoDocumentsDto.getFileName());
			        annualReportEntity.setFileType(ngoDocumentsDto.getFileType());
			        annualReportEntity.setFilePath(this.fileStorageLocation); 
				} else {
					throwErrorManually("Document must be pdf format................!", "Format Error");
				}
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
				throwErrorManually("Document cannot upload!", "Upload Error");
			}
		}	
		sacsAnnualReportRepository.save(annualReportEntity);
		ngoDocumentsDto.setId(annualReportEntity.getId());
		logger.debug("Terminate method uploadAnnualReport with ngoDocumentsDto->{}:", ngoDocumentsDto);
		 return ngoDocumentsDto;
	}
	
	/*
	 * Save Tax Report Details and upload document
	 */
	public NgoDocumentsDto uploadTaxReport(@RequestBody NgoDocumentsDto ngoDocumentsDto, MultipartFile file){
		logger.debug("Entering into method uploadTaxReport with NgoDocumentsDto->{}:", ngoDocumentsDto);
		TaxRegistrationEntity taxRegistrationEntity = new TaxRegistrationEntity();
		
		taxRegistrationEntity.setFacilityId(ngoDocumentsDto.getFacilityId());
		taxRegistrationEntity.setSacsId(ngoDocumentsDto.getSacsId());
		taxRegistrationEntity.setFinancialYear(ngoDocumentsDto.getFinancialYear());	    
		taxRegistrationEntity.setRemarks(ngoDocumentsDto.getRemarks());
		taxRegistrationEntity.setIsActive(true);
		taxRegistrationEntity.setIsDelete(false);
		
		if (file != null && !file.isEmpty()) {
			try {
				if (file.getContentType().equals("application/pdf")) {
					 // Save file on system
			        if (!file.getOriginalFilename().isEmpty()) {
						this.fileStorageLocation = fileUploadService.setLocation(env,ngoDocumentsDto.getFolderName());				
						fileUploadService.storeFile(file, ngoDocumentsDto.getFileName());    
			        } else {
			            throw new Exception();
			        }
			        taxRegistrationEntity.setFileName(ngoDocumentsDto.getFileName());
			        taxRegistrationEntity.setFileType(ngoDocumentsDto.getFileType());
			        taxRegistrationEntity.setFilePath(this.fileStorageLocation); 
			     

				} else {
					throwErrorManually("Document must be pdf format................!", "Format Error");
				}
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
				throwErrorManually("Document cannot upload!", "Upload Error");
			}
		}	
		taxRegistrationRepository.save(taxRegistrationEntity);
		ngoDocumentsDto.setId(taxRegistrationEntity.getId());
		logger.debug("Terminate method uploadRegCert with ngoDocumentsDto->{}:", ngoDocumentsDto);
		 return ngoDocumentsDto;
	}

	/*
	 * Save Project Details and upload Sanction Letter
	 */
	public NgoProjectsDto uploadSanctionLetter(@RequestBody NgoProjectsDto ngoProjectsDto, MultipartFile file){
		logger.debug("Entering into method uploadTaxReport with NgoDocumentsDto->{}:", ngoProjectsDto);
		NgoProjectsEntity ngoProjectsEntity = new NgoProjectsEntity();
		
		ngoProjectsEntity.setFacilityId(ngoProjectsDto.getFacilityId());
		ngoProjectsEntity.setSacsId(ngoProjectsDto.getSacsId());
		ngoProjectsEntity.setProjectName(ngoProjectsDto.getProjectName());	    
		ngoProjectsEntity.setProjectType(ngoProjectsDto.getProjectType());
		ngoProjectsEntity.setEndDate(ngoProjectsDto.getEndDate());
		ngoProjectsEntity.setStartDate(ngoProjectsDto.getStartDate());
		ngoProjectsEntity.setSanctionAmount(ngoProjectsDto.getSanctionAmount());
		ngoProjectsEntity.setStateId(ngoProjectsDto.getStateId());
		ngoProjectsEntity.setDistrictId(ngoProjectsDto.getDistrictId());
		ngoProjectsEntity.setAddress(ngoProjectsDto.getAddress());
		ngoProjectsEntity.setPincode(ngoProjectsDto.getPincode());
		ngoProjectsEntity.setIsActive(true);
		ngoProjectsEntity.setIsDelete(false);
		
		if (file != null && !file.isEmpty()) {
			try {
				if (file.getContentType().equals("application/pdf")) {
					 // Save file on system
			        if (!file.getOriginalFilename().isEmpty()) {
						this.fileStorageLocation = fileUploadService.setLocation(env,ngoProjectsDto.getFolderName());				
						fileUploadService.storeFile(file, ngoProjectsDto.getFileName());    
			        } else {
			            throw new Exception();
			        }
			        ngoProjectsEntity.setFileName(ngoProjectsDto.getFileName());
			       // ngoProjectsEntity.setFileType(ngoProjectsDto.getFileType());
			        ngoProjectsEntity.setFilePath(this.fileStorageLocation); 
			     

				} else {
					throwErrorManually("Document must be pdf format................!", "Format Error");
				}
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
				throwErrorManually("Document cannot upload!", "Upload Error");
			}
		}	
		ngoProjectsRepository.save(ngoProjectsEntity);
		ngoProjectsDto.setId(ngoProjectsEntity.getId());
		
		
//		if (isEdit) {
//			division = divisionRepository.findById(divisionDto.getId()).get();
//		}
//		divisionDto.setIsDelete(false);
//		division = DivisionMapperUtil.mapToDivision(divisionDto, division);
		
	//	Set<NgoProjectTypologyMapping> ngoProjectTypologyMappingList = new HashSet<>();
		
		Long project_id = ngoProjectsDto.getId();
		for (Long typologyId : ngoProjectsDto.getTypology()) {
			NgoProjectTypologyMapping ngoProjectTypologyMapping = new NgoProjectTypologyMapping();
			ngoProjectTypologyMapping.setTypologyId(typologyId);
			ngoProjectTypologyMapping.setProjectId(project_id);
			ngoProjectTypologyMapping.setIsDelete(false);
			ngoProjectTypologyMapping.setIsActive(true);
			ngoProjectTypologyMappingRepository.save(ngoProjectTypologyMapping);
		//	ngoProjectTypologyMappingList.add(ngoProjectTypologyMapping);

		}
		
		logger.debug("Terminate method uploadRegCert with ngoProjectsDto->{}:", ngoProjectsDto);
		 return ngoProjectsDto;
	}
	
	/*
	 * Save Other Documents Detail and upload document
	 */
	public NgoDocumentsDto uploadOtherDocuments(@RequestBody NgoDocumentsDto ngoDocumentsDto, MultipartFile file){
		logger.debug("Entering into method uploadOtherDocuments with NgoDocumentsDto->{}:", ngoDocumentsDto);
		otherDocumentsEntity otherDocumentEntity = new otherDocumentsEntity();
		
		otherDocumentEntity.setFacilityId(ngoDocumentsDto.getFacilityId());
		otherDocumentEntity.setSacsId(ngoDocumentsDto.getSacsId());
		otherDocumentEntity.setFinancialYear(ngoDocumentsDto.getFinancialYear());	    
		otherDocumentEntity.setRemarks(ngoDocumentsDto.getRemarks());
		otherDocumentEntity.setIsActive(true);
		otherDocumentEntity.setIsDelete(false);
		
		if (file != null && !file.isEmpty()) {
			try {
				if (file.getContentType().equals("application/pdf")) {
					 // Save file on system
			        if (!file.getOriginalFilename().isEmpty()) {
						this.fileStorageLocation = fileUploadService.setLocation(env,ngoDocumentsDto.getFolderName());				
						fileUploadService.storeFile(file, ngoDocumentsDto.getFileName());    
			        } else {
			            throw new Exception();
			        }
			        otherDocumentEntity.setFileName(ngoDocumentsDto.getFileName());
			        otherDocumentEntity.setFileType(ngoDocumentsDto.getFileType());
			        otherDocumentEntity.setFilePath(this.fileStorageLocation); 
			     

				} else {
					throwErrorManually("Document must be pdf format................!", "Format Error");
				}
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
				throwErrorManually("Document cannot upload!", "Upload Error");
			}
		}	
		otherDocumentRepository.save(otherDocumentEntity);
		ngoDocumentsDto.setId(otherDocumentEntity.getId());
		logger.debug("Terminate method uploadOtherDocuments with ngoDocumentsDto->{}:", ngoDocumentsDto);
		 return ngoDocumentsDto;
	}
	
	/*
	 * Save Other Documents Detail and upload document
	 */
	public NgoDocumentsDto uploadAnnualReport(@RequestBody NgoDocumentsDto ngoDocumentsDto, MultipartFile file){
		logger.debug("Entering into method uploadAnnualReport with NgoDocumentsDto->{}:", ngoDocumentsDto);
		AnnualReportEntity annualReportEntity = new AnnualReportEntity();
		
		annualReportEntity.setFacilityId(ngoDocumentsDto.getFacilityId());
		annualReportEntity.setSacsId(ngoDocumentsDto.getSacsId());
		annualReportEntity.setFinancialYear(ngoDocumentsDto.getFinancialYear());	    
		annualReportEntity.setRemarks(ngoDocumentsDto.getRemarks());
		annualReportEntity.setIsActive(true);
		annualReportEntity.setIsDelete(false);
		
		if (file != null && !file.isEmpty()) {
			try {
				if (file.getContentType().equals("application/pdf")) {
					 // Save file on system
			        if (!file.getOriginalFilename().isEmpty()) {
						this.fileStorageLocation = fileUploadService.setLocation(env,ngoDocumentsDto.getFolderName());				
						fileUploadService.storeFile(file, ngoDocumentsDto.getFileName());    
			        } else {
			            throw new Exception();
			        }
			        annualReportEntity.setFileName(ngoDocumentsDto.getFileName());
			        annualReportEntity.setFileType(ngoDocumentsDto.getFileType());
			        annualReportEntity.setFilePath(this.fileStorageLocation); 
				} else {
					throwErrorManually("Document must be pdf format................!", "Format Error");
				}
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
				throwErrorManually("Document cannot upload!", "Upload Error");
			}
		}	
		annualReportRepository.save(annualReportEntity);
		ngoDocumentsDto.setId(annualReportEntity.getId());
		logger.debug("Terminate method uploadAnnualReport with ngoDocumentsDto->{}:", ngoDocumentsDto);
		 return ngoDocumentsDto;
	}
	/*
	 * Update Darpan Certificate Details and upload document
	 */
	public NgoDocumentsDto uploadDarpanCertificate(@RequestBody NgoDocumentsDto ngoDocumentsDto, MultipartFile file){
		logger.debug("Entering into method uploadTaxReport with NgoDocumentsDto->{}:", ngoDocumentsDto);
		Facility facilityEntity = new Facility();
		
		facilityEntity.setId(ngoDocumentsDto.getFacilityId());
		
		if (file != null && !file.isEmpty()) {
			try {
				if (file.getContentType().equals("application/pdf")) {
					 // Save file on system
			        if (!file.getOriginalFilename().isEmpty()) {
						this.fileStorageLocation = fileUploadService.setLocation(env,ngoDocumentsDto.getFolderName());				
						fileUploadService.storeFile(file, ngoDocumentsDto.getFileName());    
			        } else {
			            throw new Exception();
			        }
			        facilityEntity.setFileName(ngoDocumentsDto.getFileName());
			        facilityEntity.setFilePath(this.fileStorageLocation);
				} else {
					throwErrorManually("Document must be pdf format................!", "Format Error");
				}
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
				throwErrorManually("Document cannot upload!", "Upload Error");
			}
		}
		facilityRepository.updateFacility(ngoDocumentsDto.getFacilityId(),facilityEntity.getFileName(),facilityEntity.getFilePath());
		ngoDocumentsDto.setId(facilityEntity.getId());
		logger.debug("Terminate method uploadRegCert with ngoDocumentsDto->{}:", ngoDocumentsDto);
		 return ngoDocumentsDto;
	}
	
	/*
	 * Save SOE Documents Detail and upload document
	 */
	public NgoDocumentsDto uploadSoeDocuments(@RequestBody NgoDocumentsDto ngoDocumentsDto, MultipartFile file){
		logger.debug("Entering into method uploadSoeDocuments with NgoDocumentsDto->{}:", ngoDocumentsDto);
		SoeDocumentEntity soeDocumentEntity = new SoeDocumentEntity();
		
		soeDocumentEntity.setFacilityId(ngoDocumentsDto.getFacilityId());		
		soeDocumentEntity.setProjectId(ngoDocumentsDto.getProjectId());
		soeDocumentEntity.setFinancialYear(ngoDocumentsDto.getFinancialYear());	    
		soeDocumentEntity.setMonthId(ngoDocumentsDto.getMonthId());	
		soeDocumentEntity.setSoeAmount(ngoDocumentsDto.getSoeAmount());	
		soeDocumentEntity.setRemarks(ngoDocumentsDto.getRemarks());
		soeDocumentEntity.setIsActive(true);
		soeDocumentEntity.setIsDelete(false);
		
		if (file != null && !file.isEmpty()) {
			try {
				if (file.getContentType().equals("application/pdf")) {
					 // Save file on system
			        if (!file.getOriginalFilename().isEmpty()) {
						this.fileStorageLocation = fileUploadService.setLocation(env,ngoDocumentsDto.getFolderName());				
						fileUploadService.storeFile(file, ngoDocumentsDto.getFileName());    
			        } else {
			            throw new Exception();
			        }
			        soeDocumentEntity.setFileName(ngoDocumentsDto.getFileName());
			      //  soeDocumentEntity.setFileType(ngoDocumentsDto.getFileType());
			        soeDocumentEntity.setFilePath(this.fileStorageLocation); 
				} else {
					throwErrorManually("Document must be pdf format................!", "Format Error");
				}
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
				throwErrorManually("Document cannot upload!", "Upload Error");
			}
		}	
		soeDocumentRepository.save(soeDocumentEntity);
		ngoDocumentsDto.setId(soeDocumentEntity.getId());
		logger.debug("Terminate method uploadSoeDocuments with ngoDocumentsDto->{}:", ngoDocumentsDto);
		 return ngoDocumentsDto;
	}
	
	/*
	 * Save UC Documents Detail and upload document
	 */
	public NgoDocumentsDto uploadUcDocuments(@RequestBody NgoDocumentsDto ngoDocumentsDto, MultipartFile file){
		logger.debug("Entering into method uploadSoeDocuments with NgoDocumentsDto->{}:", ngoDocumentsDto);
		UcDocumentEntity ucDocumentEntity = new UcDocumentEntity();
		
		ucDocumentEntity.setFacilityId(ngoDocumentsDto.getFacilityId());		
		ucDocumentEntity.setProjectId(ngoDocumentsDto.getProjectId());
		ucDocumentEntity.setFinancialYear(ngoDocumentsDto.getFinancialYear());	    
		ucDocumentEntity.setMonthId(ngoDocumentsDto.getMonthId());	
		ucDocumentEntity.setRemarks(ngoDocumentsDto.getRemarks());
		ucDocumentEntity.setIsActive(true);
		ucDocumentEntity.setIsDelete(false);
		
		if (file != null && !file.isEmpty()) {
			try {
				if (file.getContentType().equals("application/pdf")) {
					 // Save file on system
			        if (!file.getOriginalFilename().isEmpty()) {
						this.fileStorageLocation = fileUploadService.setLocation(env,ngoDocumentsDto.getFolderName());				
						fileUploadService.storeFile(file, ngoDocumentsDto.getFileName());    
			        } else {
			            throw new Exception();
			        }
			        ucDocumentEntity.setFileName(ngoDocumentsDto.getFileName());
			      //  soeDocumentEntity.setFileType(ngoDocumentsDto.getFileType());
			        ucDocumentEntity.setFilePath(this.fileStorageLocation); 
				} else {
					throwErrorManually("Document must be pdf format................!", "Format Error");
				}
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace();
				throwErrorManually("Document cannot upload!", "Upload Error");
			}
		}	
		ucDocumentRepository.save(ucDocumentEntity);
		ngoDocumentsDto.setId(ucDocumentEntity.getId());
		logger.debug("Terminate method uploadUcDocuments with ngoDocumentsDto->{}:", ngoDocumentsDto);
		 return ngoDocumentsDto;
	}
	/*
	 * List All Uploaded Documents and Details
	 */
	public List<NgoDocumentsDto> getAllUploasedDocumentsByFacility(Long facilityId,String searchText, Integer pageNumber, Integer pageSize, String sortBy,
			String sortType, String docType) {
		logger.debug("getAllRegistrationCertByFacility function invoked for facilityId " + facilityId);
		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
		if (sortType.equalsIgnoreCase("asc")) {
			pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
		}
		pageable = parenthesisEncapsulation(pageable);
		List<NgoMemberListProjection> memberList = new ArrayList<NgoMemberListProjection>();
		List<NgoDocumentsDto> ngoDocumentsDtoList = new ArrayList<NgoDocumentsDto>();
		Page<NgoMemberListProjection> memberPage = null;
		int actualCount = 0;
		Optional<List> memberListOptional = null;
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		
		System.out.println("****************facilityType*********************"+currentUser.getFacilityTypeId());
		
		if(docType.contains(FileUploadConstants.registration_Certificate)){
			System.out.println("==docType===>"+docType);
			if(currentUser.getFacilityTypeId() == 2l) {
				memberPage = ngoDocumentRepository.findAllRegistrationCertBySacsId(facilityId,searchText, pageable);
				actualCount = ngoDocumentRepository.findRegistrationCertCountBySacsId(facilityId,searchText);			
			}else {
				memberPage = ngoDocumentRepository.findAllRegistrationCertByFacilityId(facilityId,searchText, pageable);
				actualCount = ngoDocumentRepository.findRegistrationCertCountByFacilityId(facilityId,searchText);
			}
		}
		else if(docType.contains(FileUploadConstants.return_File_Certificate)){
			System.out.println("==docType===>"+docType);
			if(currentUser.getFacilityTypeId() == 2l) {
				memberPage = returnFileRepository.findAllRegistrationCertBySacsId(facilityId,searchText, pageable);
				actualCount = returnFileRepository.findRegistrationCertCountBySacsId(facilityId,searchText);			
			}else {
				memberPage = returnFileRepository.findAllRegistrationCertByFacilityId(facilityId,searchText, pageable);
				actualCount = returnFileRepository.findRegistrationCertCountByFacilityId(facilityId,searchText);
			}
		}
		else if(docType.contains(FileUploadConstants.activity_Report)){
			System.out.println("==docType===>"+docType);
			// facilityId is projectId for this block(activityReport)
			if(currentUser.getFacilityTypeId() == 2l) {
				memberPage = activityReportRepository.findAllRegistrationCertBySacsId(facilityId,searchText, pageable);
				actualCount = activityReportRepository.findRegistrationCertCountBySacsId(facilityId,searchText);			
			}else {
				memberPage = activityReportRepository.findAllRegistrationCertByFacilityId(facilityId,searchText, pageable);
				actualCount = activityReportRepository.findRegistrationCertCountByFacilityId(facilityId,searchText);
			}
		}
		else if(docType.contains(FileUploadConstants.fcra_Registration)){
			System.out.println("==docType===>"+docType);
			if(currentUser.getFacilityTypeId() == 2l) {
				memberPage = fcraRegistrationRepository.findAllRegistrationCertBySacsId(facilityId,searchText, pageable);
				actualCount = fcraRegistrationRepository.findRegistrationCertCountBySacsId(facilityId,searchText);			
			}else {
				memberPage = fcraRegistrationRepository.findAllRegistrationCertByFacilityId(facilityId,searchText, pageable);
				actualCount = fcraRegistrationRepository.findRegistrationCertCountByFacilityId(facilityId,searchText);
			}
		}
		else if(docType.contains(FileUploadConstants.audit_Report)){
			System.out.println("==docType===>"+docType);
			if(currentUser.getFacilityTypeId() == 2l) {
				memberPage = auditReportRepository.findAllRegistrationCertBySacsId(facilityId,searchText, pageable);
				actualCount = auditReportRepository.findRegistrationCertCountBySacsId(facilityId,searchText);			
			}else {
				memberPage = auditReportRepository.findAllRegistrationCertByFacilityId(facilityId,searchText, pageable);
				actualCount = auditReportRepository.findRegistrationCertCountByFacilityId(facilityId,searchText);
			}
		}
		else if(docType.contains(FileUploadConstants.tax_Registration)){
			System.out.println("==docType===>"+docType);
			if(currentUser.getFacilityTypeId() == 2l) {
				memberPage = taxRegistrationRepository.findAllRegistrationCertBySacsId(facilityId,searchText, pageable);
				actualCount = taxRegistrationRepository.findRegistrationCertCountBySacsId(facilityId,searchText);			
			}else {
				memberPage = taxRegistrationRepository.findAllRegistrationCertByFacilityId(facilityId,searchText, pageable);
				actualCount = taxRegistrationRepository.findRegistrationCertCountByFacilityId(facilityId,searchText);
			}
		}
		else if(docType.contains(FileUploadConstants.other_Document)){
			System.out.println("==docType===>"+docType);
			if(currentUser.getFacilityTypeId() == 2l) {
				memberPage = otherDocumentRepository.findAllRegistrationCertBySacsId(facilityId,searchText, pageable);
				actualCount = otherDocumentRepository.findRegistrationCertCountBySacsId(facilityId,searchText);			
			}else {
				memberPage = otherDocumentRepository.findAllRegistrationCertByFacilityId(facilityId,searchText, pageable);
				actualCount = otherDocumentRepository.findRegistrationCertCountByFacilityId(facilityId,searchText);
			}
		}
		else if(docType.contains(FileUploadConstants.annual_Report)){
			System.out.println("==docType===>"+docType);
			if(currentUser.getFacilityTypeId() == 2l) {
				memberPage = annualReportRepository.findAllRegistrationCertBySacsId(facilityId,searchText, pageable);
				actualCount = annualReportRepository.findRegistrationCertCountBySacsId(facilityId,searchText);			
			}else {
				memberPage = annualReportRepository.findAllRegistrationCertByFacilityId(facilityId,searchText, pageable);
				actualCount = annualReportRepository.findRegistrationCertCountByFacilityId(facilityId,searchText);
			}
		}
		else if(docType.contains(FileUploadConstants.soe_Document)){ 
			// facilityId is projectId for this block(soeDocuments)
			System.out.println("==docType===>"+docType);
			if(currentUser.getFacilityTypeId() == 2l) {
				memberPage = soeDocumentRepository.findAllRegistrationCertBySacsId(facilityId,searchText, pageable);
				actualCount = soeDocumentRepository.findRegistrationCertCountBySacsId(facilityId,searchText);			
			}else {
				memberPage = soeDocumentRepository.findAllRegistrationCertByFacilityId(facilityId,searchText, pageable);
				actualCount = soeDocumentRepository.findRegistrationCertCountByFacilityId(facilityId,searchText);
			}
		}
		else if(docType.contains(FileUploadConstants.utilization_Certificate)){ 
			// facilityId is projectId for this block(UC Details)
			System.out.println("==docType===>"+docType);
			if(currentUser.getFacilityTypeId() == 2l) {
				memberPage = ucDocumentRepository.findAllRegistrationCertBySacsId(facilityId,searchText, pageable);
				actualCount = ucDocumentRepository.findRegistrationCertCountBySacsId(facilityId,searchText);			
			}else {
				memberPage = ucDocumentRepository.findAllRegistrationCertByFacilityId(facilityId,searchText, pageable);
				actualCount = ucDocumentRepository.findRegistrationCertCountByFacilityId(facilityId,searchText);
			}
		}
		else if(docType.contains(FileUploadConstants.sacs_Audit_Report)){
			System.out.println("==docType===>"+docType);
				memberPage = sacsAuditReportRepository.findAllRegistrationCertByFacilityId(facilityId,searchText, pageable);
				actualCount = sacsAuditReportRepository.findRegistrationCertCountByFacilityId(facilityId,searchText);			
		}
		else if(docType.contains(FileUploadConstants.sacs_Annual_Report)){
			System.out.println("==docType===>"+docType);			
				memberPage = sacsAnnualReportRepository.findAllRegistrationCertByFacilityId(facilityId,searchText, pageable);
				actualCount = sacsAnnualReportRepository.findRegistrationCertCountByFacilityId(facilityId,searchText);
		}
				
		System.out.println("actualCount==>"+actualCount);
		memberListOptional = Optional.ofNullable(memberPage.getContent());

		if (memberListOptional != null && memberListOptional.isPresent()) {
			memberList = memberListOptional.get();
		}
		ngoDocumentsDtoList = FacilityMapperUtil.mapDocumentListProjectionToDocumentDto(memberList);
		if (!ngoDocumentsDtoList.isEmpty()) {
			ngoDocumentsDtoList.get(0).setActualRecordCount(actualCount);
		}
		return ngoDocumentsDtoList;
	}

// Method to update Verification Status
	public void updateVerificationStatus(Long recordId, Integer recordStatus,String recordType) {	
		System.out.println("Entering into updateVerificationStatus=>"+recordType);
		logger.debug("Entering into updateVerificationStatus  method  - NgoDocumentService");
		if(recordType.contains("registrationCertificate")) {
			ngoDocumentRepository.changeRegCertStatus(recordId, recordStatus);
		}
		if(recordType.contains("returnFileCertificate")) {
			ngoDocumentRepository.changeReturnFileStatus(recordId, recordStatus);
		}
		if(recordType.contains("activityReport")) {
			ngoDocumentRepository.changeActiveReportStatus(recordId, recordStatus);
		}
		if(recordType.contains("auditReport")) {
			ngoDocumentRepository.changeAuditReportStatus(recordId, recordStatus);
		}
		if(recordType.contains("fcraRegCertificate")) {
			ngoDocumentRepository.changeFCRACertStatus(recordId, recordStatus);
		}
		if(recordType.contains("taxRegistrationCertificate")) {
			ngoDocumentRepository.changeTaxCertStatus(recordId, recordStatus);
		}
		
	}
	
	// Method to update Verification Status
	public void deleteDocuments(Long recordId,String recordType) {	
		System.out.println("Entering into deleteDocuments=>"+recordType);
		logger.debug("Entering into deleteDocuments  method  - NgoDocumentService");
		if(recordType.contains("registrationCertificate") || recordType.contains("socialRegistrationCertificate")) {
			ngoDocumentRepository.deleteRegCertData(recordId, Boolean.TRUE);
		}
		if(recordType.contains("returnFileCertificate")) {
			ngoDocumentRepository.deleteReturnFileData(recordId, Boolean.TRUE);
		}
		if(recordType.contains("activityReport")) {
			ngoDocumentRepository.deleteActiveReportData(recordId, Boolean.TRUE);
		}
		if(recordType.contains("auditReport")) {
			ngoDocumentRepository.deleteAuditReportData(recordId, Boolean.TRUE);
		}
		if(recordType.contains("annualReport")) {
			ngoDocumentRepository.deleteAnnualReportData(recordId, Boolean.TRUE);
		}
		if(recordType.contains("fcraRegCertificate")) {
			ngoDocumentRepository.deleteFCRACertData(recordId, Boolean.TRUE);
		}
		if(recordType.contains("taxRegistrationCertificate")) {
				ngoDocumentRepository.deleteTaxCertData(recordId, Boolean.TRUE);
		}
		if(recordType.contains("sacsAuditReport")) {
			ngoDocumentRepository.deleteSacsAuditRecord(recordId, Boolean.TRUE);
		}
		if(recordType.contains("sacsAnnualReport")) {
			ngoDocumentRepository.deleteSacsAnnualRecord(recordId, Boolean.TRUE);
		}
		if(recordType.contains("otherDocuments")) {
			ngoDocumentRepository.deleteOtherDocumentRecord(recordId, Boolean.TRUE);
		}
		if(recordType.contains("soeDocument")) {
			ngoDocumentRepository.deleteSoeDocumentRecord(recordId, Boolean.TRUE);
		}
		if(recordType.contains("ucDocument")) {
			ngoDocumentRepository.deleteUcDocumentRecord(recordId, Boolean.TRUE);
		}
			
	}
}
