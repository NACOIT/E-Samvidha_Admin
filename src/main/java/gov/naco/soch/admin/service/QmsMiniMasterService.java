/**
 * 
 */
package gov.naco.soch.admin.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.admin.dto.QmsPtEnterDataDto;

/**
 * @author Pranav MS (144958)
 * @email pranav.sasi@ust-global.com
 * @date 2020-Jul-20 12:05:09 pm
 * 
 */

@Service
@Transactional
public class QmsMiniMasterService {

	@Autowired
	MasterDataService masterDataService;

	@Autowired
	private FacilityService facilityService;

	@Autowired
	AddressService addressService;

	@Autowired
	private UserService userService;

	
	private static final Logger logger = LoggerFactory.getLogger(QmsMiniMasterService.class);

	/**
	 * @return
	 */
	public QmsPtEnterDataDto getMiniMasterForPtEnterData() {
		// TODO Auto-generated method stub
		return null;
	}

}
