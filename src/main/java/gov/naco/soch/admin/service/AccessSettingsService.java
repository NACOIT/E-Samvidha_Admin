package gov.naco.soch.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.dto.RoleAccessMappingDto;
import gov.naco.soch.entity.AccessMaster;
import gov.naco.soch.entity.Role;
import gov.naco.soch.entity.RoleAccessMapping;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.AccessSettingsMapper;
import gov.naco.soch.repository.AccessMasterRepository;
import gov.naco.soch.repository.RoleAccessMappingRepository;
import gov.naco.soch.repository.RoleRepository;

@Service
@Transactional
public class AccessSettingsService {

	@Autowired
	RoleAccessMappingRepository roleAccessMappingRepository;

	@Autowired
	AccessMasterRepository accessMasterRepository;

	@Autowired
	RoleRepository roleRepository;

	private static final Logger logger = LoggerFactory.getLogger(AccessSettingsService.class);

	/*
	 * This will save all entries in role_access_mapping table.These entries are
	 * supposed to be existing in the table already and this operation will be just
	 * UPDATE for those.
	 */
	public boolean saveAccessSettings(List<RoleAccessMappingDto> roleAccessMappingDtoList, Long roleId) {
		logger.debug("Entering into method saveAccessSettings with roleAccessMappingDtoList->{}:roleId->{}:",
				roleAccessMappingDtoList, roleId);
		List<RoleAccessMapping> roleAccessMappingList = new ArrayList<RoleAccessMapping>();
		Role role = null;
		Optional<Role> roleOpt = roleRepository.findById(roleId);

		if (roleOpt.isPresent()) {
			role = roleOpt.get();
			/*
			 * if(role.getRoleAccessMappings() != null &&
			 * !role.getRoleAccessMappings().isEmpty()) {
			 * role.getRoleAccessMappings().clear(); }
			 */
			List<RoleAccessMapping> oldMappingList = roleAccessMappingRepository.findByRole(roleId);

			roleAccessMappingRepository.deleteAll(oldMappingList);
			/*
			 * Without below code, save is failing with unique constraint violation
			 * exception as system is not considering that the mapping entries (roleId,
			 * accessCode) has not been deleted with above deleteAll code.
			 */
			List<RoleAccessMapping> oldMappingList1 = roleAccessMappingRepository.findByRole(roleId);

		}
		for (RoleAccessMappingDto roleAccessMappingDto : roleAccessMappingDtoList) {
			RoleAccessMapping roleAccessMapping = null;
			roleAccessMapping = AccessSettingsMapper.mapToRoleAccessMapping(roleAccessMappingDto);
			roleAccessMapping.setRole(role);
			roleAccessMappingList.add(roleAccessMapping);
		}
		try {
			roleAccessMappingRepository.saveAll(roleAccessMappingList);
		} catch (DataIntegrityViolationException e) {
			logger.error("DataIntegrityViolationException in saving access settings->{}", e);
			ErrorResponse errorResponse = new ErrorResponse(e.getLocalizedMessage(), null, null);
			throw new ServiceException(e.getLocalizedMessage(), errorResponse, HttpStatus.BAD_REQUEST);
		}
		logger.debug("Leaving from method saveAccessSettings with roleAccessMappingDtoList->{}:roleId->{}:",
				roleAccessMappingDtoList, roleId);
		return true;
	}


	public List<RoleAccessMappingDto> getRoleAccessSettings(Long roleId) {
		/*
		 * First fetch the entries from role_access_mapping table which is already
		 * inserted for this role.
		 */
		logger.debug("Entering into method getRoleAccessSettings with roleId->{}", roleId);
		List<RoleAccessMapping> roleAccessMappingList = roleAccessMappingRepository.findByRole(roleId);
		RoleAccessMappingDto roleAccessMappingDto = null;
		List<RoleAccessMappingDto> roleAccessMappingDtoList = new ArrayList<>();
		for (RoleAccessMapping roleAccessMapping : roleAccessMappingList) {
			roleAccessMappingDto = AccessSettingsMapper.mapToRoleAccessMappingDto(roleAccessMapping);
			roleAccessMappingDtoList.add(roleAccessMappingDto);
		}
		logger.debug("Leaving from method getRoleAccessSettings with roleId->{}", roleId);
		return roleAccessMappingDtoList;
	}

	public List<RoleAccessMappingDto> getAllAccessSettings() {
		/*
		 * First fetch the entries from role_access_mapping table which is already
		 * inserted for this role.
		 */
		logger.debug("Entering into method getAllAccessSettings with roleId->{}");
		List<AccessMaster> accessMasterList = accessMasterRepository.findAll();
		RoleAccessMappingDto roleAccessMappingDto = null;
		List<RoleAccessMappingDto> roleAccessMappingDtoList = new ArrayList<>();
		for (AccessMaster accessMaster : accessMasterList) {
			roleAccessMappingDto = AccessSettingsMapper.mapToRoleAccessMappingDto(accessMaster);
			roleAccessMappingDtoList.add(roleAccessMappingDto);
		}
		logger.debug("Leaving from method getAllAccessSettings with roleId->{}");
		return roleAccessMappingDtoList;
	}

}
