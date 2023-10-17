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
import gov.naco.soch.dto.UserMobileMasterDto;
import gov.naco.soch.entity.Role;
import gov.naco.soch.entity.UserMaster;
import gov.naco.soch.mapper.AccessSettingsMapper;
import gov.naco.soch.mapper.UserMapperUtil;
import gov.naco.soch.projection.OrwProjection;
import gov.naco.soch.repository.RoleRepository;
import gov.naco.soch.repository.UserMasterRepository;
import gov.naco.soch.util.UserUtils;


@Transactional
@Service
public class OrwPeMobileService {
	
	@Autowired
	private UserMasterRepository userRepository;


	@Autowired
	private RoleRepository roleRepository;
	
	// Logger Method
		private static final Logger logger = LoggerFactory.getLogger(OrwPeMobileService.class);

	

	
	
		public List<UserMobileMasterDto> getOrwMobileUsersForTypology(Integer typologyId) {
			logger.debug("getOrwUsersForTypology method called to fetch orw list");
			List<Integer> typologyIdList = new ArrayList<Integer>();
			typologyIdList.add(typologyId);
			LoginResponseDto currentUser = UserUtils.getLoggedInUserDetails();
			List<OrwProjection> userList = userRepository
					.findUsersListByFacilityIdAndTypologyId(currentUser.getFacilityId(),typologyIdList);

			List<UserMobileMasterDto> userMasterDtos = new ArrayList<UserMobileMasterDto>();
			for (OrwProjection user : userList) {
				logger.info("userID :"+ user.getFirstname());
				logger.info("userorw : " + user.getOrwCode());
				Optional<Role> role = roleRepository.findById(user.getRoleId());
				Boolean isOrw = false;
				isOrw = AccessSettingsMapper.findIsOrwFromAccessSettings(role.get(), isOrw);
				if (isOrw == true) {
					UserMobileMasterDto userMasterDto = new UserMobileMasterDto();
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
	
	public List<UserMobileMasterDto> getPeUsersMobileListBasedOnOrw(Long orwId) {
		logger.debug("getPeUsersListBasedOnOrw method called with paramete->{}", orwId);
		List<UserMaster> peUserList = userRepository.findPeByOrwForMobile(orwId);
		logger.info("orwId:"+orwId);
		List<UserMobileMasterDto> userMasterDtos = UserMapperUtil.mapToMobileUserMasterDtoSet(peUserList).stream()
				.collect(Collectors.toList());
		Collections.sort(userMasterDtos);
		logger.debug("getPeUsersListBasedOnOrw method returns ->{}", userMasterDtos);
		return userMasterDtos;
	}


}
