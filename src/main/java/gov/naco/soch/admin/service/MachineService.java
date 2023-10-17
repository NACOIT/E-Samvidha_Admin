package gov.naco.soch.admin.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.MachineDto;
import gov.naco.soch.entity.Machine;
import gov.naco.soch.mapper.MachineMapperUtil;
import gov.naco.soch.repository.MachineRepository;

@Transactional
@Service
public class MachineService {
	@Autowired
	private MachineRepository machineRepository;
	// Logger Method
	private static final Logger logger = LoggerFactory.getLogger(MachineService.class);

	public List<MachineDto> getAllMachine() {
		List<MachineDto> machineDtoList = new ArrayList<MachineDto>();
		List<Machine> machineList = new ArrayList<Machine>();
		machineList = machineRepository.findByIsDelete(Boolean.FALSE);
		machineDtoList=MachineMapperUtil.mapToMachineDto(machineList);
		return machineDtoList;
	}

}
