/**
 * 
 */
package gov.naco.soch.admin.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import gov.naco.soch.admin.dto.SocialWelfareDto;
import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.entity.MasterMaritalStatus;
import gov.naco.soch.entity.MasterSocialWelfare;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.projection.SocialWelfareProjection;
import gov.naco.soch.repository.MasterSocialWelfareRepository;

/**
 * @author Pranav MS (144958)
 * @email pranav.sasi@ust-global.com
 * @date 2020-Dec-01 8:24:26 pm
 * 
 */

@Service
@Transactional
public class SocialWelfareService {
	private static final Logger logger = LoggerFactory.getLogger(SocialWelfareService.class);

	@Autowired
	MasterSocialWelfareRepository masterSocialWelfareRepository;

	/**
	 * @return
	 */
	public List<SocialWelfareDto> getSocialWelfareList() {
		List<SocialWelfareDto> socialWelfareDtos = new ArrayList<SocialWelfareDto>();
		List<SocialWelfareProjection> welfareProjections = new ArrayList<SocialWelfareProjection>();
		welfareProjections = masterSocialWelfareRepository.findAllSocialWelfareList();
		if (!CollectionUtils.isEmpty(welfareProjections)) {
			welfareProjections.forEach(row -> {
				SocialWelfareDto tempDto = new SocialWelfareDto();
				tempDto.setId(row.getId());
				tempDto.setName(row.getName());
				tempDto.setCode(row.getCode());
				tempDto.setDescription(row.getDescription());
				tempDto.setStateId(row.getStateId());
				tempDto.setStateName(row.getStateName());
				socialWelfareDtos.add(tempDto);
			});
		}
		return socialWelfareDtos;
	}

	/**
	 * @param socialWelfareDto
	 * @return
	 */
	public SocialWelfareDto saveSocialWelfare(SocialWelfareDto socialWelfareDto) {
		int count = 0;
		MasterSocialWelfare masterSocialWelfare = new MasterSocialWelfare();

		if (socialWelfareDto.getId() != null && socialWelfareDto.getId() > 0) {// update duplicate checking 
			count = masterSocialWelfareRepository.existsByOtherNameAndState(socialWelfareDto.getName(),
					socialWelfareDto.getStateId(), socialWelfareDto.getId());
			masterSocialWelfare.setId(socialWelfareDto.getId());
		} else {// insert duplicate checking
			count = masterSocialWelfareRepository.existsByOtherName(socialWelfareDto.getName(),
					socialWelfareDto.getStateId());
		}

		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "title";
			logger.debug("call throwError if duplicate found");
			throwError(errorfield, socialWelfareDto.getName());
		}

		masterSocialWelfare.setName(socialWelfareDto.getName());
		masterSocialWelfare.setCode(socialWelfareDto.getCode());
		masterSocialWelfare.setDescription(socialWelfareDto.getDescription());
		masterSocialWelfare.setStateId(socialWelfareDto.getStateId());
		masterSocialWelfare.setIsActive(true);
		masterSocialWelfare.setIsDelete(false);
		
		masterSocialWelfare = masterSocialWelfareRepository.save(masterSocialWelfare);
		
		SocialWelfareDto tempSocialWelfareDto=new SocialWelfareDto();
		tempSocialWelfareDto.setId(masterSocialWelfare.getId());
		tempSocialWelfareDto.setName(masterSocialWelfare.getName());
		tempSocialWelfareDto.setCode(masterSocialWelfare.getCode());
		tempSocialWelfareDto.setDescription(masterSocialWelfare.getDescription());
		tempSocialWelfareDto.setStateId(masterSocialWelfare.getStateId());
		//tempSocialWelfareDto.setStateName(masterSocialWelfare.getStateName());
		return tempSocialWelfareDto;
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
