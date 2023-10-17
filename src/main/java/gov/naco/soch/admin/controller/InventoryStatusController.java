
package gov.naco.soch.admin.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.InventoryStatusService;
import gov.naco.soch.dto.InventoryStatusDto;

@RestController
@RequestMapping("/inventorystatus")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InventoryStatusController {

	@Autowired
	private InventoryStatusService inventoryStatusService;

	private static final Logger logger = LoggerFactory.getLogger(InventoryStatusController.class);

	public InventoryStatusController() {
	}

	/**
	 * @return inventoryStatusList
	 */
	@GetMapping("/list")
	public @ResponseBody List<InventoryStatusDto> getAllInventoryStatuses() {
		logger.debug("getAllInventoryStatus method called");
		List<InventoryStatusDto> inventoryStatusList=inventoryStatusService.getAllInventoryStatus();
		return inventoryStatusList;
	}

	/**
	 * @param inventoryStatusDto
	 * @return inventoryStatusDto
	 */
	@PostMapping("/add")
	public @ResponseBody InventoryStatusDto addInventoryStatus(
			@Valid @RequestBody InventoryStatusDto inventoryStatusDto) {
		logger.debug("addInventoryStatus method called with parameters->{}", inventoryStatusDto);
		inventoryStatusDto = inventoryStatusService.addInventoryStatus(inventoryStatusDto);
		return inventoryStatusDto;
	}
}
