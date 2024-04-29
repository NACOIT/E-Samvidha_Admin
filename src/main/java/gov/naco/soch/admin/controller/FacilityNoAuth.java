package gov.naco.soch.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.FacilityService;
import gov.naco.soch.dto.SacsFacilityDto;

@RestController
@RequestMapping("/xapi")
@CrossOrigin(origins = "http://127.0.0.1:3000/", allowedHeaders = "*")
public class FacilityNoAuth {
	
	private static final Logger logger = LoggerFactory.getLogger(FacilityController.class);

	@Autowired
	private FacilityService facilityService;
	
	@GetMapping("list/{sacsId}")
	public @ResponseBody List<SacsFacilityDto> getAllFacilityBySacs(@PathVariable("sacsId") Long sacsId,@RequestParam String searchText,
			@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortType) {
		logger.debug("getAllFacilityBySacs method called with parameters->{}", sacsId);
		List<SacsFacilityDto> sacsFacilityDtoList = new ArrayList<SacsFacilityDto>();
		sacsFacilityDtoList = facilityService.getAllFacilityBySacsAPI(sacsId,searchText,false, pageNumber, pageSize, sortBy, sortType);
		return sacsFacilityDtoList;
	}
}
