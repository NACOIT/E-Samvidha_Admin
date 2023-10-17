package gov.naco.soch.admin.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.RegimenService;
import gov.naco.soch.dto.RegimenDto;
import org.springframework.security.access.prepost.PreAuthorize;
import gov.naco.soch.constant.AdminAccessCodes;

/*Controller class for API call*/

@RestController
@RequestMapping("/regimen")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RegimenController {

	@Autowired
	RegimenService regimenService;

	private static final Logger logger = LoggerFactory.getLogger(RegimenController.class);

	public RegimenController() {
	}

	/*
	 * Save product.
	 * 
	 */
	@PostMapping("/save")
	public @ResponseBody boolean saveRegimen(@Valid @RequestBody RegimenDto regimenDto) {
		logger.debug("Entering into method saveRegimen with regimenDto->{}:", regimenDto);
		return regimenService.saveRegimen(regimenDto);
	}

	/**
	 * Optimized
	 * @return
	 */
	@GetMapping("/list")
	public @ResponseBody List<RegimenDto> getRegimens() {
		logger.debug("Entering into method getRegimens");
		return regimenService.getRegimens();
	}

	/*
	 * Save product.
	 * 
	 */
	@DeleteMapping("/{regimenId}/delete")
	public @ResponseBody boolean deleteRegimen(@PathVariable("regimenId") Long regimenId) {
		logger.debug("Entering into method deleteProduct with productId->{}:", regimenId);
		return regimenService.deleteRegimen(regimenId);
	}

	/**
	 * Regimen advance search. Search Criteria: name,adultorped,line,constituent
	 * 
	 * @param searchValue
	 * @return
	 */
	@GetMapping("/advancesearch")
	public @ResponseBody List<RegimenDto> regimenAdvanceSearch(@RequestParam Map<String, String> searchValue) {
		logger.debug("Entering into method regimenAdvanceSearch");
		return regimenService.regimenAdvanceSearch(searchValue);
	}

}
