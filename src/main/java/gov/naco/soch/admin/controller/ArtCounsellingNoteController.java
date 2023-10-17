package gov.naco.soch.admin.controller;

import java.util.ArrayList;
import java.util.List;

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

import gov.naco.soch.admin.dto.ArtCounsellingNoteDto;
import gov.naco.soch.admin.dto.ArtCounsellingNoteResponseDto;
import gov.naco.soch.admin.service.ArtCounsellingNoteService;
import gov.naco.soch.dto.MasterDto;

//Controller class for API call

@RestController
@RequestMapping("/counsellingnote")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ArtCounsellingNoteController {

	private static final Logger logger = LoggerFactory.getLogger(ArtCounsellingNoteController.class);
	
	@Autowired
	private ArtCounsellingNoteService artCounsellingNoteService;
	
	// NORMAL COUNSELLING NOTE LIST WITH PAGINATION (EXCEL/PDF)
	@GetMapping("/list")
	public @ResponseBody ArtCounsellingNoteResponseDto getAllArtCounsellingNoteList(@RequestParam(required = false) String searchText,@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize) {
		logger.debug("Entering into method getAllArtCounsellingNoteList");
		ArtCounsellingNoteResponseDto artCounsellingNoteResponseDto = new ArtCounsellingNoteResponseDto();
		artCounsellingNoteResponseDto = artCounsellingNoteService.getAllArtCounsellingNoteList(searchText,pageNumber,pageSize);
		return artCounsellingNoteResponseDto;
	}
	
	//LIST - DISTINCT COUNSELLING SECTION  FOR DROP DOWN
	@GetMapping("/counsellingsection/list")
	public @ResponseBody List<MasterDto> getAllArtCounsellingSection() {
		logger.debug("Entering into method getAllArtCounsellingSection");
		List<MasterDto> counsellingsectionList=new ArrayList<MasterDto>();
		counsellingsectionList=artCounsellingNoteService.getAllArtCounsellingSection();
		return counsellingsectionList;
	}

	//COUNT FOR COUNSELLING NOTE BY COUNSELLING SECTION 
	@GetMapping("/count")
	public @ResponseBody Integer getCounsellingNoteCount(@RequestParam(required = false) String counsellingsection) {
		logger.debug("Entering into method getCounsellingNoteCount");
		Integer counsellingNoteCount=0;
		counsellingNoteCount=artCounsellingNoteService.getCounsellingNoteCount(counsellingsection);
		return counsellingNoteCount;
	}
	
	//DELETE COUNSELLING NOTE BY ID
	@DeleteMapping("/delete/{counsellingNoteId}")
	public Boolean deleteCounsellingNote(@PathVariable("counsellingNoteId") Long counsellingNoteId){
		logger.debug("deleteCounsellingNote method called with parameters->{}", counsellingNoteId);
		return artCounsellingNoteService.deleteCounsellingNote(counsellingNoteId);
	}
	
	//SAVE COUNSELLING NOTE
	@PostMapping("/insert")
	public @ResponseBody ArtCounsellingNoteDto saveCounsellingNote(@RequestBody ArtCounsellingNoteDto artCounsellingNoteDto) {
		logger.info("saveCounsellingNote method called with parameters->{}", artCounsellingNoteDto);
		return artCounsellingNoteService.saveCounsellingNote(artCounsellingNoteDto);
	}
	
}
