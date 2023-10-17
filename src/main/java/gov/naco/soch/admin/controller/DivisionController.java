package gov.naco.soch.admin.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.DivisionService;
import gov.naco.soch.constant.AdminAccessCodes;
import gov.naco.soch.dto.DivisionDto;
import gov.naco.soch.dto.MasterDto;

/**
 * Controller class for managing division related APIs
 *
 */
@RestController
@RequestMapping("/division")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DivisionController {

	@Autowired
	private DivisionService divisionService;

	private static final Logger logger = LoggerFactory.getLogger(DivisionController.class);

	/**
	 * API to get all details from division table
	 * 
	 * @return List<DivisionDto>
	 */
	@GetMapping("/list")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.NACO_DIVISION + "') or hasAuthority('"
			+ AdminAccessCodes.NACO_ADMIN_PROGRAM_DIVISION + "')")
	public @ResponseBody List<DivisionDto> getAllDivision() {
		logger.info("getAllDivision method called");
		return divisionService.getAllDivision();
	}
	
	@GetMapping("/findBy/{divisionId}")
	public @ResponseBody DivisionDto getDivisionById(@PathVariable("divisionId") Long divisionId) {
		logger.info("getAllDivision method called");
		return divisionService.getDivisionById(divisionId);
	}
	
	/**
	 * Optimized division List
	 * return only id and name
	 * @return
	 */
	@GetMapping("/basicList")
	public @ResponseBody List<MasterDto> getAllBasicDivisionList() {
		logger.info("getAllBasicDivisionList method called");
		return divisionService.getAllBasicDivisionList();
	}

	/**
	 * API to add division details into division table
	 * 
	 * @param divisionDto
	 * @return DivisionDto
	 */
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('" + AdminAccessCodes.NACO_DIVISION + "') or hasAuthority('"
			+ AdminAccessCodes.NACO_ADMIN_PROGRAM_DIVISION + "')")
	public @ResponseBody DivisionDto addDivision(@Valid @RequestBody DivisionDto divisionDto) {

		logger.info("addDivison method called with parameters->{}", divisionDto);
		divisionService.saveDivision(divisionDto);
		logger.info("addDivison method returns with parameters->{}", divisionDto);
		return divisionDto;

	}

	/**
	 * API to delete details from division table
	 * 
	 * @param divisionDto
	 * @return Boolean
	 */
	@PostMapping("/delete/{divisionId}")
	public Boolean deleteDivision(@PathVariable("divisionId") Long divisionId) {
		logger.info("deleteDivision method called with parameters->{}", divisionId);
		return divisionService.deleteDivision(divisionId);
	}

	/**
	 * Division advance search with criteria: facilityType, name, code
	 * 
	 * @param searchValue
	 * @return
	 */
	@GetMapping("/advancesearch")
	public @ResponseBody List<DivisionDto> divisonAdvanceSearch(@RequestParam Map<String, String> searchValue) {
		logger.info("divisonAdvanceSearch method called");
		return divisionService.divisonAdvanceSearch(searchValue);
	}

}
