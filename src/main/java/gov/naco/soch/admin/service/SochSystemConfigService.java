package gov.naco.soch.admin.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.entity.SochSystemConfig;
import gov.naco.soch.repository.SochSystemConfigRepository;

@Transactional
@Service
public class SochSystemConfigService {
	
	@Autowired
	private SochSystemConfigRepository sochSystemConfigRepository;

	public Boolean isEodJobsEnabled() {
		Boolean isEnabled=true;
		Optional<SochSystemConfig> sochSystemConfigOPtional=sochSystemConfigRepository.findById(1L);
		if(sochSystemConfigOPtional.isPresent()) {
			SochSystemConfig sochSystemConfig=sochSystemConfigOPtional.get();
			isEnabled=sochSystemConfig.getEnabled();
		}
		return isEnabled;
	}

}
