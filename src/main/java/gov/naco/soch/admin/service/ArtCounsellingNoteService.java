package gov.naco.soch.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.ehcache.core.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import gov.naco.soch.admin.dto.ArtCounsellingNoteDto;
import gov.naco.soch.admin.dto.ArtCounsellingNoteResponseDto;
import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.entity.CounsellingNote;
import gov.naco.soch.entity.CounsellingType;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.projection.ArtCounsellingNoteProjection;
import gov.naco.soch.projection.CounsellingSectionProjection;
import gov.naco.soch.repository.CounsellingNoteRepository;

@Service
@Transactional
public class ArtCounsellingNoteService {

	private static final Logger logger = LoggerFactory.getLogger(ArtCounsellingNoteService.class);

	@Autowired
	private CounsellingNoteRepository counsellingNoteRepository;

	@Value("${exportRecordsLimit}")
	private Integer exportRecordsLimit;

	public ArtCounsellingNoteResponseDto getAllArtCounsellingNoteList(String searchText, Integer pageNumber,
			Integer pageSize) {
		logger.debug("Entering into getAllArtCounsellingNoteList method --- in ArtCounsellingNoteService ");

		if (pageNumber == null || pageSize == null) {
			pageNumber = 0;
			pageSize = exportRecordsLimit;
		}

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		ArtCounsellingNoteResponseDto artCounsellingNoteResponseDto = new ArtCounsellingNoteResponseDto();
		Page<ArtCounsellingNoteProjection> artCounsellingNoteProjectionPage = null;
		List<ArtCounsellingNoteProjection> artCounsellingNoteProjectionList = new ArrayList<ArtCounsellingNoteProjection>();
		List<ArtCounsellingNoteDto> artCounsellingNoteDtoList = new ArrayList<ArtCounsellingNoteDto>();
		Optional<List> counsellingListOptional = null;
		Long actualCount = null;

		if (searchText != null && searchText != "") {
			artCounsellingNoteProjectionPage = counsellingNoteRepository
					.findArtCounsellingNoteListWithSearch(searchText.toLowerCase(), pageable);
			actualCount = counsellingNoteRepository.getActualCountWithSearch(searchText.toLowerCase());
		} else {
			artCounsellingNoteProjectionPage = counsellingNoteRepository
					.findArtCounsellingNoteListWithoutSearch(pageable);
			actualCount = counsellingNoteRepository.getActualCountWithoutSearch();
		}

		counsellingListOptional = Optional.ofNullable(artCounsellingNoteProjectionPage.getContent());
		if (counsellingListOptional != null && counsellingListOptional.isPresent()) {
			artCounsellingNoteProjectionList = counsellingListOptional.get();
		}

		if (!CollectionUtils.isEmpty(artCounsellingNoteProjectionList)) {
			artCounsellingNoteProjectionList.forEach(row -> {
				ArtCounsellingNoteDto artCounsellingNoteDto = new ArtCounsellingNoteDto();
				artCounsellingNoteDto.setArtCounsellingNoteId(row.getArtCounsellingNoteId());
				artCounsellingNoteDto.setCounsellingNoteName(row.getCounsellingNoteName());
				artCounsellingNoteDto.setCounsellingTypeName(row.getCounsellingTypeName());
				artCounsellingNoteDto.setCounsellingSection(row.getCounsellingSection());
				artCounsellingNoteDto.setFirstVisitOnly(row.getFirstVisitOnly());
				artCounsellingNoteDto.setCounsellingNoteIsActive(row.getCounsellingNoteIsActive());
				artCounsellingNoteDtoList.add(artCounsellingNoteDto);
			});
		}
		artCounsellingNoteResponseDto.setArtCounsellingNoteDto(artCounsellingNoteDtoList);
		artCounsellingNoteResponseDto.setCurrentCount(artCounsellingNoteDtoList.size());
		artCounsellingNoteResponseDto.setTotalcount(actualCount);
		artCounsellingNoteResponseDto.setPageSize(pageSize);
		artCounsellingNoteResponseDto.setPageNumber(pageNumber);

		return artCounsellingNoteResponseDto;
	}

	/**
	 * @return
	 */
	public List<MasterDto> getAllArtCounsellingSection() {
		logger.debug("Entering into getAllArtCounsellingSection method --- in ArtCounsellingNoteService ");
		List<CounsellingSectionProjection> counsellingSectionProjectionList = new ArrayList<CounsellingSectionProjection>();
		List<MasterDto> counsellingsectionList = new ArrayList<MasterDto>();
		counsellingSectionProjectionList = counsellingNoteRepository.getAllArtCounsellingSection();

		if (!CollectionUtils.isEmpty(counsellingSectionProjectionList)) {
			counsellingSectionProjectionList.forEach(row -> {
				MasterDto dto = new MasterDto();
				dto.setName(row.getCounsellingSection());
				counsellingsectionList.add(dto);
			});
		}
		return counsellingsectionList;
	}

	/**
	 * @param counsellingsection
	 * @return
	 */
	public Integer getCounsellingNoteCount(String counsellingsection) {
		logger.debug("Entering into getCounsellingNoteCount method --- in getCounsellingNoteCount ");
		Integer counsellingNoteCount = 0;
		if (counsellingsection != null && counsellingsection != "") {
			counsellingNoteCount = counsellingNoteRepository
					.getCounsellingNoteCount(counsellingsection.trim().toLowerCase());
		}
		return counsellingNoteCount;
	}

	public Boolean deleteCounsellingNote(Long counsellingNoteId) {
		Boolean isDeleted = Boolean.FALSE;
		if (counsellingNoteId > 0 && counsellingNoteId != null) {
			counsellingNoteRepository.deleteCounsellingNote(counsellingNoteId);
			isDeleted = Boolean.TRUE;
		}
		return isDeleted;
	}

	public ArtCounsellingNoteDto saveCounsellingNote(ArtCounsellingNoteDto artCounsellingNoteDto) {
		CounsellingNote counsellingNote = new CounsellingNote();
		CounsellingType counsellingType = new CounsellingType();

		if (artCounsellingNoteDto != null) {

			Integer count = 0;
			count = counsellingNoteRepository.existsByOtherName(
					artCounsellingNoteDto.getCounsellingSection().trim().toLowerCase(),
					artCounsellingNoteDto.getCounsellingNoteName().trim().toLowerCase());
			if (count != 0) {
				logger.error(Constants.DUPLICATE_FOUND);
				String errorfield = "title";
				logger.debug("call throwError if duplicate found");
				throwError(errorfield,
						artCounsellingNoteDto.getCounsellingNoteName() + " is present under the counselling section: "
								+ artCounsellingNoteDto.getCounsellingSection());
			}

			counsellingNote.setCounsellingNote(artCounsellingNoteDto.getCounsellingNoteName().trim());
			if (artCounsellingNoteDto.getCounsellingTypeId() != null) {
				counsellingType.setId(artCounsellingNoteDto.getCounsellingTypeId());
				counsellingNote.setCounsellingType(counsellingType);
			}
			counsellingNote.setCounsellingSection(artCounsellingNoteDto.getCounsellingSection());
			counsellingNote.setQuestionKey(artCounsellingNoteDto.getQuestionKey());
			counsellingNote.setFirstVisit(artCounsellingNoteDto.getFirstVisitOnly());
			counsellingNote.setIsActive(Boolean.TRUE);
			counsellingNote.setIsDelete(Boolean.FALSE);

			counsellingNote = counsellingNoteRepository.save(counsellingNote);
		}
		if (counsellingNote.getId() != null) {
			return artCounsellingNoteDto;
		} else {
			return null;
		}
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

}
