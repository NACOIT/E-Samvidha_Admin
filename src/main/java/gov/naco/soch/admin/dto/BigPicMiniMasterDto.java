package gov.naco.soch.admin.dto;

import java.util.List;

import gov.naco.soch.dto.BaseDto;
import gov.naco.soch.dto.LocationDto;
import gov.naco.soch.dto.MasterDto;

public class BigPicMiniMasterDto extends BaseDto {
		
		private static final long serialVersionUID = 1L;
				
		private List<LocationDto> masterState;

		private List<MasterDto> masterGender;

		private List<MasterDto> masterAgeGroup;
		
		private List<MasterDto> masterTypology;

		private List<MasterDto> masterTargetIndicator;

		public List<LocationDto> getMasterState() {
			return masterState;
		}

		public void setMasterState(List<LocationDto> masterState) {
			this.masterState = masterState;
		}

		public List<MasterDto> getMasterGender() {
			return masterGender;
		}

		public void setMasterGender(List<MasterDto> masterGender) {
			this.masterGender = masterGender;
		}
		
		public List<MasterDto> getMasterAgeGroup() {
			return masterAgeGroup;
		}

		public void setMasterAgeGroup(List<MasterDto> masterAgeGroup) {
			this.masterAgeGroup = masterAgeGroup;
		}

		public List<MasterDto> getMasterTypology() {
			return masterTypology;
		}

		public void setMasterTypology(List<MasterDto> masterTypology) {
			this.masterTypology = masterTypology;
		}


		public List<MasterDto> getMasterTargetIndicator() {
			return masterTargetIndicator;
		}

		public void setMasterTargetIndicator(List<MasterDto> masterTargetIndicator) {
			this.masterTargetIndicator = masterTargetIndicator;
		}
		
		
}
