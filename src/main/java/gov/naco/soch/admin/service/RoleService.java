package gov.naco.soch.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.dto.MasterDto;
import gov.naco.soch.dto.RoleDto;
import gov.naco.soch.entity.Role;
import gov.naco.soch.enums.FacilityTypeEnum;
import gov.naco.soch.enums.RoleEnum;
import gov.naco.soch.exception.ServiceException;
import gov.naco.soch.mapper.Constants;
import gov.naco.soch.mapper.RoleMapperUtil;
import gov.naco.soch.repository.RoleRepository;

@Transactional
@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	UserLdapService userLdapService;

	private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

	// function to add/update role details
	public RoleDto addRole(RoleDto roleDto) {

		Role role = new Role();
		roleDto.setIsDelete(false);
		int count = 0;
		boolean isEdit = false;

		if (roleDto.getId() != null && roleDto.getId() != 0) {
			// method to check whether the role name is already exist for particular
			// facility type in edit
			count = roleRepository.foundDuplicateInEdit(roleDto.getName(), roleDto.getFacilityTypeId(),
					roleDto.getId());
			isEdit = true;
		} else {
			// method to check whether the role name is already exist for particular
			// facility type in add
			count = roleRepository.foundDuplicateRoleNameInAdd(roleDto.getName(), roleDto.getFacilityTypeId());
		}

		// to get all role details by id in edit
		if (isEdit) {
			role = roleRepository.findById(roleDto.getId()).get();
		}

		logger.debug("mapToRole method called with parameters->{}", roleDto, role);
		role = RoleMapperUtil.mapToRole(roleDto, role);

		// invoking throw error method if role name found duplicate
		if (count != 0) {
			logger.error(Constants.DUPLICATE_FOUND);
			String errorfield = "name";
			throwError(errorfield, roleDto.getName());
		} else {
			// to save role details into soch.role
			role = roleRepository.save(role);
			roleDto.setId(role.getId());
			try {
				userLdapService.saveRoleToLdapServer(roleDto);
			} catch (Exception e) {
				logger.error("---------LDAP Role Exception---------------- role name :  " + roleDto.getName(), e);
			}
		}
		if (!isEdit)
			roleDto.setId(null);
		return roleDto;
	}

	// function to select all roles details from database
	public List<RoleDto> getAllRoles() {
		List<Object[]> roles = roleRepository.findByIsDeleteOrderByIdDesc();
		logger.debug("mapObjectListToRoleDTOList method called with parameters->{}", roles);
		List<RoleDto> roleDtoList = RoleMapperUtil.mapObjectListToRoleDTOList(roles);
		return roleDtoList;
	}

	// function to delete role details from database
	public boolean deleteRole(Long id) {
		int count = roleRepository.findDeleteUser(id);
		if (count == 0) {
			Role role = roleRepository.findById(id).get();
			role.setIsDelete(true);
			role = roleRepository.save(role);
			try {
				userLdapService.deleteRoleFromLdap(role);
			} catch (Exception e) {
				logger.error("-----LDAP Role delete---------" + role.getName(), e);
			}
			logger.debug("mapToRoleDTO method called with parameters->{}", role);
			return true;
		} else {
			throw new ServiceException("Role already in use!", null, HttpStatus.BAD_REQUEST);
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

	public List<MasterDto> getBasicRoleList() {
		// Role list except super admin role
		List<Object[]> roles = roleRepository.findRoleBasicList(RoleEnum.SUPER_ADMIN.getRole());
		List<MasterDto> roleList = RoleMapperUtil.mapBasicObjToRoleMasterDto(roles);
		return roleList;
	}

	public List<RoleDto> getFacilityTypeRoles(Long facilityTypeId) {
		List<Object[]> roles = roleRepository.findRoleByFacilityType(facilityTypeId);
		logger.debug("getFacilityTypeRoles method called with parameters->{}", facilityTypeId);
		List<RoleDto> roleDtoList = RoleMapperUtil.mapObjToRoleDto(roles);
		if (roleDtoList != null && !roleDtoList.isEmpty()
				&& facilityTypeId == FacilityTypeEnum.NACO.getFacilityType()) {
			roleDtoList = roleDtoList.stream().filter(r -> r.getId() != RoleEnum.SUPER_ADMIN.getRole())
					.collect(Collectors.toList());
		}
		return roleDtoList;
	}

	public List<RoleDto> getFacilityTypePrimaryRoles(Long facilityTypeId) {
		List<Role> roleList = new ArrayList<Role>();
		List<RoleDto> roleDtoList = new ArrayList<RoleDto>();
		roleList = roleRepository.findByFacilityTypeIdAndIsDeleteAndIsPrimaryOrderByNameAsc(facilityTypeId,
				Boolean.FALSE, Boolean.TRUE);
		roleDtoList = RoleMapperUtil.mapToRoleDTOList(roleList);
		roleDtoList = roleDtoList.stream().filter(r -> r.getId() != RoleEnum.SUPER_ADMIN.getRole())
				.collect(Collectors.toList());
		return roleDtoList;

	}

	public List<RoleDto> getFacilityTypeNonPrimaryRoles(Long facilityTypeId) {
		List<Role> roleList = new ArrayList<Role>();
		List<RoleDto> roleDtoList = new ArrayList<RoleDto>();
		roleList = roleRepository.findByFacilityTypeIdAndIsDeleteAndIsPrimaryOrderByNameAsc(facilityTypeId,
				Boolean.FALSE, Boolean.FALSE);
		roleDtoList = RoleMapperUtil.mapToRoleDTOList(roleList);
		roleDtoList = roleDtoList.stream().filter(r -> r.getId() != RoleEnum.SUPER_ADMIN.getRole())
				.collect(Collectors.toList());
		return roleDtoList;

	}
	
	/**
	 * To get all role based on facility types
	 */
	public List<RoleDto> getRoleListByFacilityType(Long facilityTypeId) {
		List<Object[]> roles = roleRepository.findRoleByFacilityType(facilityTypeId);
		logger.debug("getRoleListByFacilityType method called with parameters->{}", facilityTypeId);
		List<RoleDto> roleDtoList = RoleMapperUtil.mapObjToRoleDto(roles);
		if (roleDtoList != null && !roleDtoList.isEmpty()) {
			roleDtoList = roleDtoList.stream().filter(r -> r.getId() != RoleEnum.SUPER_ADMIN.getRole())
					.collect(Collectors.toList());
		}
		return roleDtoList;
	}

	/**
	 * role advance search with criteria: facilityType, name, primary
	 * 
	 * @param searchValue
	 * @return
	 */
	public List<RoleDto> roleAdvanceSearch(Map<String, String> searchValue) {
		String searchQuery = RoleMapperUtil.advanceSearchQueryCreator(searchValue);
		List<Object[]> roles = roleRepository.roleAdvanceSearch(searchQuery, searchValue);
		logger.debug("mapObjectListToRoleDTOList method called with parameters->{}", roles);
		List<RoleDto> roleDtoList = RoleMapperUtil.mapObjectListToRoleDTOList(roles);
		return roleDtoList;
	}

}
