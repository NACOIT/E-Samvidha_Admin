package gov.naco.soch.admin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.LoginResponseDto;
import gov.naco.soch.dto.OrwPeMappingDto;
import gov.naco.soch.dto.UserBasicDto;
import gov.naco.soch.dto.UserMasterDto;
import gov.naco.soch.entity.OrwPeMapping;
import gov.naco.soch.entity.Role;
import gov.naco.soch.entity.UserMaster;
import gov.naco.soch.enums.AccessCodeEnum;
import gov.naco.soch.mapper.AccessSettingsMapper;
import gov.naco.soch.mapper.OrwPeMapperUtil;
import gov.naco.soch.mapper.UserMapperUtil;
import gov.naco.soch.projection.OrwProjection;
import gov.naco.soch.projection.UserBasicProjection;
import gov.naco.soch.repository.OrwPeMappingRepository;
import gov.naco.soch.repository.RoleRepository;
import gov.naco.soch.repository.UserMasterRepository;
import gov.naco.soch.util.UserUtils;

//service class interact data with database
@Transactional
@Service
public class OrwPeService {

	@Autowired
	private UserMasterRepository userRepository;

	@Autowired
	private OrwPeMappingRepository orwPeMappingRepository;

	@Autowired
	private RoleRepository roleRepository;

	// Logger Method
	private static final Logger logger = LoggerFactory.getLogger(OrwPeService.class);

	/**
	 * @return
	 */
	// public List<UserMasterDto> getOrwUsers() {
	//
	// logger.debug("getOrwUsers method called to fetch orw list");
	// LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
	// List<UserMaster> userList =
	// userRepository.findUsersListByAccessCodeAndFacilityId(
	// AccessCodeEnum.ORW.getAccessCode(), currentUser.getFacilityId());
	// List<UserMasterDto> userMasterDtos =
	// UserMapperUtil.mapToUserMasterDtoSet(userList).stream()
	// .collect(Collectors.toList());
	// Collections.sort(userMasterDtos);
	// logger.debug("getOrwUsers method returns ->{}", userMasterDtos);
	// return userMasterDtos;
	// }

	/**
	 * Optimized list
	 * 
	 * @return
	 */
	public List<UserBasicDto> getOrwUsersForDropdownBasicDetails() {

		logger.debug("getOrwUsersForDropdownBasicDetails method called to fetch orw list");
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		List<UserBasicProjection> userList = userRepository.findBasicUsersListByAccessCodeAndFacilityId(
				AccessCodeEnum.ORW.getAccessCode(), currentUser.getFacilityId());
		logger.debug(" Accesscode "+AccessCodeEnum.ORW.getAccessCode());
		logger.debug(" Facility code "+currentUser.getFacilityId());
		List<UserBasicDto> userBasicDtos = UserMapperUtil.mapBasicUserProjectionToUserBasicDto(userList);
		Collections.sort(userBasicDtos);
		logger.debug("getOrwUsersForDropdownBasicDetails method returns ->{}", userBasicDtos);
		return userBasicDtos;
	}

	/**
	 * @return
	 */
	public List<UserMasterDto> getPeUsersForDropdown() {

		logger.debug("getPeUsersForDropdown method called to fetch pe list");
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		List<UserMaster> userList = userRepository
				.findUsersListByAccessCodeAndFacilityId(AccessCodeEnum.PE.getAccessCode(), currentUser.getFacilityId());
		userList = OrwPeMapperUtil.findPeListForDropdownFromTotalResult(userList);
		List<UserMasterDto> userMasterDtos = UserMapperUtil.mapToUserMasterDtoSet(userList).stream()
				.collect(Collectors.toList());
		Collections.sort(userMasterDtos);
		logger.debug("getPeUsersForDropdown method returns ->{}", userMasterDtos);
		return userMasterDtos;
	}

	/**
	 * @param orwPeMappingDto
	 * @return
	 */
	public OrwPeMappingDto saveOrwPeMapping(OrwPeMappingDto orwPeMappingDto) {
		logger.debug("saveOrwPeMapping method called with paramete->{}", orwPeMappingDto);
		OrwPeMapping orwPeMapping = OrwPeMapperUtil.mapToOrwPeMapping(orwPeMappingDto);
		if (orwPeMapping != null) {
			Optional<OrwPeMapping> orwPeMappingOpt = orwPeMappingRepository
					.findByOrwUserIdAndPeUserId(orwPeMapping.getOrwUser().getId(), orwPeMapping.getPeUser().getId());
			if (orwPeMappingOpt.isPresent()) {
				orwPeMapping = orwPeMappingOpt.get();
				orwPeMapping.setIsDelete(Boolean.FALSE);
			}
			orwPeMappingRepository.save(orwPeMapping);
		}
		return orwPeMappingDto;
	}

	/**
	 * @param orwId
	 * @return
	 */
	public List<UserBasicDto> getPeUsersListBasedOnOrw(Long orwId) {
		logger.debug("getPeUsersListBasedOnOrw method called with paramete->{}", orwId);
		//List<UserMaster> peUserList = userRepository.findPeByOrw(orwId);
		//List<UserMasterDto> userMasterDtos = UserMapperUtil.mapToUserMasterDtoSet(peUserList).stream()
				//.collect(Collectors.toList());
		List<UserBasicProjection> peUserList = userRepository.findPeByOrw(orwId);	
		List<UserBasicDto> userMasterDtos = UserMapperUtil.mapBasicUserProjectionToUserBasicDto(peUserList);
		Collections.sort(userMasterDtos);
		logger.debug("getPeUsersListBasedOnOrw method returns ->{}", userMasterDtos);
		return userMasterDtos;
	}

	/**
	 * @param peId
	 * @return
	 */
	public Boolean deletePeUserMapping(Long peId) {
		logger.debug("deletePeUserMapping method called with paramete->{}", peId);
		//OrwPeMapping peMapping = orwPeMappingRepository.findByPeUserIdAndIsDelete(peId, Boolean.FALSE);
		OrwPeMapping peMapping = orwPeMappingRepository.findByUserId(peId);
		if (peMapping != null) {
			peMapping.setIsDelete(Boolean.TRUE);
			orwPeMappingRepository.save(peMapping);
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * @param typologyId
	 * @param typologyIds 
	 * @return
	 */
	public List<UserMasterDto> getOrwUsersForTypology(Long typologyId, List<Integer> typologyIds) {
		logger.debug("getOrwUsersForTypology method called to fetch orw list");
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		List<OrwProjection> userList = userRepository
				.findUsersListByFacilityIdAndTypologyId(currentUser.getFacilityId(), typologyIds);

		List<UserMasterDto> userMasterDtos = new ArrayList<UserMasterDto>();
		for (OrwProjection user : userList) {
			Optional<Role> role = roleRepository.findById(user.getRoleId());
			Boolean isOrw = false;
			isOrw = AccessSettingsMapper.findIsOrwFromAccessSettings(role.get(), isOrw);
			if (isOrw == true) {
				UserMasterDto userMasterDto = new UserMasterDto();
				userMasterDto.setId(user.getId());
				userMasterDto.setOrwCode("ORW0" + user.getId());
				userMasterDto.setFirstname(user.getFirstname());
				userMasterDtos.add(userMasterDto);
			}

		}
		Collections.sort(userMasterDtos);
		logger.debug("getOrwUsersForTypology method returns ->{}", userMasterDtos);
		return userMasterDtos;
	}

	/**
	 * @param typologyIds 
	 * @return
	 */	
	public List<UserMasterDto> getPeUsersForTypology(Long typologyId, List<Integer> typologyIds) {

		logger.debug("getPeUsersForDropdown method called to fetch pe list");
		LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
		logger.debug(" accesscode "+ AccessCodeEnum.PE.getAccessCode());
		logger.debug(" facilityId "+currentUser.getFacilityId());
		List<UserMaster> userList = userRepository
				.findUsersListByAccessCodeAndFacilityId(AccessCodeEnum.PE.getAccessCode(), currentUser.getFacilityId(),typologyIds);
		userList = OrwPeMapperUtil.findPeListForDropdownFromTotalResult(userList);
		List<UserMasterDto> userMasterDtos = UserMapperUtil.mapToUserMasterDtoSet(userList).stream()
				.collect(Collectors.toList());
		Collections.sort(userMasterDtos);
		logger.debug("getPeUsersForDropdown method returns ->{}", userMasterDtos);
		return userMasterDtos;
	}
}
