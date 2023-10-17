package gov.naco.soch.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.MachineService;
import gov.naco.soch.dto.MachineDto;

@RestController
@RequestMapping("/machine")
public class MachineController {

	@Autowired
	private MachineService machineService;
	// Logger Method
	private static final Logger logger = LoggerFactory.getLogger(MachineController.class);
	
	public MachineController() {
		
	}

	@GetMapping("/list")
	public @ResponseBody List<MachineDto> getAllMachine(){
		
		List<MachineDto> machineDtoList = new ArrayList<MachineDto>();
		machineDtoList = machineService.getAllMachine();
		return machineDtoList;
		
	}
	
}