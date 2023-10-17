package gov.naco.soch.admin.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.util.DateUtil;
import gov.naco.soch.dto.DummyDetailsDto;
import gov.naco.soch.dto.DummyDto;
import gov.naco.soch.entity.DummyDetail;
import gov.naco.soch.entity.DummyMaster;
import gov.naco.soch.repository.DummyMasterRepository;

@Service
@Transactional
public class DummyService {

	@Autowired
	DummyMasterRepository dummyRepository;

	public DummyDto addUserDummy(DummyDto dummyDto) {
		dummyDto.setId(null);
		DummyMaster dummyMaster = new DummyMaster();
		dummyMaster.setName(dummyDto.getFirstName());
		dummyMaster.setEmail(dummyDto.getEmail());
		dummyMaster.setDateofbirth(dummyDto.getDateOfBirth());
		dummyMaster.setSubmittedTime(dummyDto.getSubmittedTime());

		dummyMaster.setCreatedDate(DateUtil.getCurrentUTCDateTime());

		DummyDetail dummyDetails = new DummyDetail();
		dummyDetails.setDummyMaster(dummyMaster);
		dummyDetails.setAddress("testaddress1");
		dummyDetails.setMobile("91");
		
		Set<DummyDetail> dummyDetailSet = new HashSet<DummyDetail>();
		dummyMaster.setDummyDetails(dummyDetailSet);

		dummyMaster.getDummyDetails().add(dummyDetails);

		dummyDetails = new DummyDetail();
		dummyDetails.setDummyMaster(dummyMaster);
		dummyDetails.setAddress("testaddress2");
		dummyDetails.setMobile("92");

		dummyMaster.getDummyDetails().add(dummyDetails);

		// dummyMaster.setCreatedDate(instant);
		dummyMaster = dummyRepository.save(dummyMaster);
		dummyDto.setId(dummyMaster.getId());
		return dummyDto;
	}

	public DummyDto getUserDummy(Long userId) {
		DummyMaster dummyMaster = null;
		dummyMaster = dummyRepository.findById(userId).get();
		DummyDto dummyDto = new DummyDto();
		dummyDto.setId(dummyMaster.getId());
		dummyDto.setCreatedDate(dummyMaster.getCreatedDate());
		dummyDto.setDateOfBirth(dummyMaster.getDateofbirth());
		dummyDto.setEmail(dummyMaster.getEmail());
		dummyDto.setName(dummyMaster.getName());
		dummyDto.setSubmittedTime(dummyMaster.getSubmittedTime());

		List<DummyDetailsDto> dummyDetailsDtoList = new ArrayList<DummyDetailsDto>();

		Set<DummyDetail> dummyDetailsList = dummyMaster.getDummyDetails();
		for (DummyDetail dummyDetail : dummyDetailsList) {
			DummyDetailsDto dummyDetailsDto = new DummyDetailsDto();
			dummyDetailsDto.setId(dummyDetail.getId());
			dummyDetailsDto.setAddress(dummyDetail.getAddress());
			dummyDetailsDto.setMobile(dummyDetail.getMobile());
			dummyDetailsDtoList.add(dummyDetailsDto);
		}
		dummyDto.setDummyDetailsList(dummyDetailsDtoList);

		return dummyDto;
	}

}
