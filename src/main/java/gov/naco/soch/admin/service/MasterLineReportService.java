package gov.naco.soch.admin.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.projection.MasterLineProjection;
import gov.naco.soch.repository.BeneficiaryRepository;

@Service
@Transactional
public class MasterLineReportService {

	@Autowired
	BeneficiaryRepository beneficiaryRepository;
	@Autowired
	MasterLineReportExportService masterLineReportExport;

	public List<MasterLineProjection> findByFacility(Long facilityId) {
		List<MasterLineProjection> objectsList = null;
		objectsList = beneficiaryRepository.findBeneficiariesByFacility(facilityId);

		return objectsList;

	}

	public ByteArrayInputStream exportExcelData(Long facilityId) {
		List<MasterLineProjection> masterLineProjectionList = findByFacility(facilityId);

		ByteArrayInputStream reportStream = masterLineReportExport.exportExcelData(masterLineProjectionList);

		return reportStream;
	}

}
