package gov.naco.soch.admin.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.DailyDosageDto;
import gov.naco.soch.dto.FacilityPerformanceData;
import gov.naco.soch.entity.Facility;
import gov.naco.soch.entity.UserMaster;
import gov.naco.soch.mapper.FacilityPerformanceDataMapper;
import gov.naco.soch.projection.DailyDosageProjection;
import gov.naco.soch.projection.DailyDosageRegistrationProjection;
import gov.naco.soch.projection.FacilityDetailedProjection;
import gov.naco.soch.projection.FacilityPerformanceDetailProjection;
import gov.naco.soch.projection.UserDetailsProjection;
import gov.naco.soch.repository.DailyDosageReportRepository;
import gov.naco.soch.repository.FacilityRepository;
import gov.naco.soch.repository.UserMasterRepository;

@Service
@Transactional
public class DailyDosageReportService {

	@Autowired
	private DailyDosageReportRepository dailyDosageReportRepository;
	@Autowired
	DailyDosageReportExportService dailyDosageReportExportService;
	@Autowired
	FacilityRepository facilityRepository;
	@Autowired
	UserMasterRepository userMasterRepository;

	public List<DailyDosageDto> getDailyDosageReportList(Long facilityId, String month, int year) {

		List<DailyDosageProjection> report = dailyDosageReportRepository.getDailyDosageReportList(facilityId, month,
				year);

		return FacilityPerformanceDataMapper.mapToDailyDosageDtoSet(report);

	}

	public ByteArrayInputStream exportExcelData(Long facilityId, String month, int year, Long userId) {
		List<DailyDosageDto> dailyDosageProjectionList = getDailyDosageReportList(facilityId, month, year);
		ByteArrayInputStream reportStream;
		try {
			/* Extract static header details-[START] */

			FacilityPerformanceDetailProjection facility = facilityRepository.getFacilityById(facilityId);
			DailyDosageRegistrationProjection registrationDetails = dailyDosageReportRepository
					.getRegistrationCounts(facilityId);
			UserDetailsProjection userData = userMasterRepository.getUserDetailById(userId);
			FacilityPerformanceData facilityPerformanceData = FacilityPerformanceDataMapper
					.mapReportStaticHeaderData(facility, registrationDetails, userData);
			reportStream = dailyDosageReportExportService.exportExcelData(facilityPerformanceData,
					dailyDosageProjectionList);
			/* Extract static header details-[END] */
		} catch (Exception e) {
			return null;
		}
		return reportStream;
	}

}
