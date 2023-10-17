/**
 * 
 */
package gov.naco.soch.admin.validator;

import org.springframework.beans.factory.annotation.Autowired;

import gov.naco.soch.dto.FacilityTypeDto;
import gov.naco.soch.repository.FacilityTypeRepository;

/**
* @author Vidhya A V (145144)
* @email vidhya.AV@ust-global.com
* @date 2020-Jan-02 4:13:41 PM 
 * 
*/

public class FacilityTypeValidator {
	
	@Autowired
	FacilityTypeRepository facilityTypeRepository;
	
	
	
	//public Integer isTypeAlreadyExists( FacilityTypeDto facilityTypeDto) {
	
		//return facilityTypeRepository.existsByType(facilityTypeDto.getType());
		
	//}

}
