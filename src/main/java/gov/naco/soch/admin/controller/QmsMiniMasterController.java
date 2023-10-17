/**
 * 
 */
package gov.naco.soch.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.dto.QmsPtEnterDataDto;
import gov.naco.soch.admin.service.QmsMiniMasterService;

/**
 * @author Pranav MS (144958)
 * @email pranav.sasi@ust-global.com
 * @date 2020-Nov-17 11:25:14 pm 
 * 
 */
@RestController
@RequestMapping("/qmsminimaster")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class QmsMiniMasterController {
	private static final Logger logger = LoggerFactory.getLogger(QmsMiniMasterController.class);
	
	@Autowired
	QmsMiniMasterService qmsMiniMasterService;
	
	@GetMapping("/ptenterdata")
	public @ResponseBody QmsPtEnterDataDto getMiniMasterForPtEnterData() {
		logger.debug("Entering into method getMiniMasterForPtEnterData");
		return qmsMiniMasterService.getMiniMasterForPtEnterData();
	}
}
