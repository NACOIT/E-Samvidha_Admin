package gov.naco.soch.admin.service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import gov.naco.soch.constructordto.FacilityPerformanceDto;
import gov.naco.soch.dto.FacilityPerformanceData;
import gov.naco.soch.dto.FacilityPerformanceResultDto;

@Service
public class FacilityPerformanceExcelReport {

	public ByteArrayInputStream exportExcelData(List<FacilityPerformanceResultDto> performanceResultDtos,
			FacilityPerformanceData facilityPerformanceData) {

		ByteArrayOutputStream reportStream = new ByteArrayOutputStream();
		XSSFRow row0;
		String[] columns = { "S.No.", "Description", "Source", "Male", "Female", "Total" };
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Facility Performance Report");
			int rowNum = 0;
			/* Top static two header */
			String[] topHeadersList = { "DEPARTMENT OF AIDS CONTROL", "OPIOID SUBSTITUTION THERAPY PROGRAMME",
					"  MONTHLY REPORTING FORMAT FOR NGO OST CENTRE" };
			String[] staticHeaderFirstCells = { "State:", "Name of Center:", "Address:", "Type Of OST Center:",
					"Current NABH Accreditation:", "", "Name of Reporting In-Charge:" };
			for (int i = 0; i < topHeadersList.length; i++) {
				row0 = sheet.createRow(rowNum++);
				row0.setHeight((short) 400);// Set row height
				// Creates the cell
				XSSFCell cell = (XSSFCell) CellUtil.createCell(row0, 0, topHeadersList[i]);
				short cellColor = i == 2 ? IndexedColors.YELLOW.getIndex() : IndexedColors.WHITE.getIndex();
				FacilityPerformanceExcelReport.setCellStyles(cell, workbook, cellColor, HorizontalAlignment.CENTER,
						true);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cell, sheet, workbook, i, i, 0, 8,
						HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());
			}

			/*
			 * -----------------------------------------------------------------------------
			 * 
			 */

			XSSFCell cells;
			int tempRowNum = rowNum;
			if (tempRowNum == 3) {
				row0 = sheet.createRow(rowNum++);
				/* 1st */
				cells = (XSSFCell) CellUtil.createCell(row0, 0, staticHeaderFirstCells[0]);
				// sheet.setColumnWidth(0, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						0, 1, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());

				/* 2nd */
				cells = (XSSFCell) CellUtil.createCell(row0, 2, facilityPerformanceData.getStateName());
				// sheet.setColumnWidth(2, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.CENTER, false);

				/* 3rd */
				cells = (XSSFCell) CellUtil.createCell(row0, 3, "District:");
				// sheet.setColumnWidth(3, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);
				/* 4th */
				cells = (XSSFCell) CellUtil.createCell(row0, 4, facilityPerformanceData.getFacilityDistrictName());
				// sheet.setColumnWidth(4, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.CENTER, false);
				/* 5th */
				cells = (XSSFCell) CellUtil.createCell(row0, 5, "Block:");
				// sheet.setColumnWidth(5, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);

				/* 6th */
				cells = (XSSFCell) CellUtil.createCell(row0, 6, facilityPerformanceData.getBlock());
				// sheet.setColumnWidth(6, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.CENTER, false);

				/* 7th */
				cells = (XSSFCell) CellUtil.createCell(row0, 7, "City / Town:");
				// sheet.setColumnWidth(7, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);

				/* 8th */
				cells = (XSSFCell) CellUtil.createCell(row0, 8, facilityPerformanceData.getCity());
				// sheet.setColumnWidth(8, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.CENTER, false);

			}

			/*
			 * -----------------------------------------------------------------------------
			 * --
			 */

			tempRowNum = rowNum;
			if (tempRowNum == 4) {
				row0 = sheet.createRow(rowNum++);
				/* 1st */
				cells = (XSSFCell) CellUtil.createCell(row0, 0, staticHeaderFirstCells[1]);
				// sheet.setColumnWidth(0, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						0, 1, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());

				/* 2nd */
				cells = (XSSFCell) CellUtil.createCell(row0, 2, facilityPerformanceData.getFacilityCenterName());
				// sheet.setColumnWidth(2, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.CENTER, false);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						2, 4, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());
				/* 5th */
				cells = (XSSFCell) CellUtil.createCell(row0, 5, "OST Centre code:");
				// sheet.setColumnWidth(5, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);

				/* 6th */
				cells = (XSSFCell) CellUtil.createCell(row0, 6, facilityPerformanceData.getOstCode());
				// sheet.setColumnWidth(6, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.CENTER, false);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						6, 8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());

			}

			/*
			 * -----------------------------------------------------------------------------
			 * --
			 */

			tempRowNum = rowNum;
			if (tempRowNum == 5) {
				row0 = sheet.createRow(rowNum++);
				/* 1st */
				cells = (XSSFCell) CellUtil.createCell(row0, 0, staticHeaderFirstCells[2]);
				// sheet.setColumnWidth(0, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);

				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum,
						tempRowNum + 1, 0, 1, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER,
						BorderStyle.THIN.getCode());

				/* 2nd */
				cells = (XSSFCell) CellUtil.createCell(row0, 2, facilityPerformanceData.getAddress());
				// sheet.setColumnWidth(2, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.CENTER, false);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						2, 4, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());
				/* 5th */
				cells = (XSSFCell) CellUtil.createCell(row0, 5, "Phone No (Centre):");
				// sheet.setColumnWidth(5, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);

				/* 6th */
				cells = (XSSFCell) CellUtil.createCell(row0, 6, facilityPerformanceData.getCenterPnNo());
				// sheet.setColumnWidth(6, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.CENTER, false);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						6, 8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());

			}

			/*
			 * -----------------------------------------------------------------------------
			 * 
			 */

			tempRowNum = rowNum;
			if (tempRowNum == 6) {
				row0 = sheet.createRow(rowNum++);
				/* 1st */
				cells = (XSSFCell) CellUtil.createCell(row0, 0, "");
				cells = (XSSFCell) CellUtil.createCell(row0, 1, "");
				/* 3rd */
				cells = (XSSFCell) CellUtil.createCell(row0, 2, "");
				// sheet.setColumnWidth(2, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.CENTER, false);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						2, 4, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());

				/* 5th */
				cells = (XSSFCell) CellUtil.createCell(row0, 5, "E-mail ID (Centre):");
				// sheet.setColumnWidth(5, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);
				/* 6th */
				cells = (XSSFCell) CellUtil.createCell(row0, 6, facilityPerformanceData.getCenterEmailId());
				// sheet.setColumnWidth(6, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.CENTER, false);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						6, 8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());

			}

			/*
			 * -----------------------------------------------------------------------------
			 * 
			 */

			tempRowNum = rowNum;
			if (tempRowNum == 7) {
				row0 = sheet.createRow(rowNum++);
				/* 1ST */
				cells = (XSSFCell) CellUtil.createCell(row0, 0, staticHeaderFirstCells[3]);
				// sheet.setColumnWidth(0, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						0, 1, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());

				/* 3rd */
				cells = (XSSFCell) CellUtil.createCell(row0, 2, facilityPerformanceData.getfTypeName());
				// sheet.setColumnWidth(2, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.CENTER, false);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						2, 4, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());
				/* 6th */
				cells = (XSSFCell) CellUtil.createCell(row0, 5, "Type of Facility:");
				// sheet.setColumnWidth(6, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);

				/* 7th */
				cells = (XSSFCell) CellUtil.createCell(row0, 6, facilityPerformanceData.getfTypeName());
				// sheet.setColumnWidth(6, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.CENTER, false);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						6, 8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());
			}

			/*
			 * -----------------------------------------------------------------------------
			 * 
			 */

			tempRowNum = rowNum;
			if (tempRowNum == 8) {
				row0 = sheet.createRow(rowNum++);
				row0.setHeight((short) 500);
				/* 1ST */
				cells = (XSSFCell) CellUtil.createCell(row0, 0, staticHeaderFirstCells[4]);
				// sheet.setColumnWidth(0, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						0, 1, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());
				/* 3rd */
				cells = (XSSFCell) CellUtil.createCell(row0, 2, "From:");
				// sheet.setColumnWidth(3, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);
				/* 4th */
				cells = (XSSFCell) CellUtil.createCell(row0, 3, String.valueOf(new Date()));
				// sheet.setColumnWidth(3, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, false);
				/* 5th */
				cells = (XSSFCell) CellUtil.createCell(row0, 4, "");
				// sheet.setColumnWidth(5, 15 * 256);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum,
						tempRowNum + 1, 4, 4, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER,
						BorderStyle.THIN.getCode());
				/* 6th */
				cells = (XSSFCell) CellUtil.createCell(row0, 5, "Month & Year of Roll-out:");
				// sheet.setColumnWidth(5, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						5, 6, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());

				/* 7th */
				cells = (XSSFCell) CellUtil.createCell(row0, 7, String.valueOf(new Date()));
				//// sheet.setColumnWidth(5, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, false);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						7, 8, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());
			}

			/*
			 * -----------------------------------------------------------------------------
			 * 
			 */

			tempRowNum = rowNum;
			if (tempRowNum == 9) {
				row0 = sheet.createRow(rowNum++);
				row0.setHeight((short) 500);
				/* 1ST */
				cells = (XSSFCell) CellUtil.createCell(row0, 0, staticHeaderFirstCells[5]);
				// sheet.setColumnWidth(0, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						0, 1, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());
				/* 3rd */
				cells = (XSSFCell) CellUtil.createCell(row0, 2, "To:");
				// sheet.setColumnWidth(3, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);
				/* 4th */
				cells = (XSSFCell) CellUtil.createCell(row0, 3, String.valueOf(new Date()));
				// sheet.setColumnWidth(3, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, false);

				/* 6th */
				cells = (XSSFCell) CellUtil.createCell(row0, 5, "Month & Year of Reporting:");
				// sheet.setColumnWidth(5, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						5, 6, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());

				/* 7th */
				cells = (XSSFCell) CellUtil.createCell(row0, 7, String.valueOf(new Date()));
				// sheet.setColumnWidth(5, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.CENTER, false);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						7, 8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());
			}
			/*
			 * -----------------------------------------------------------------------------
			 * 
			 */

			tempRowNum = rowNum;
			if (tempRowNum == 10) {
				row0 = sheet.createRow(rowNum++);
				row0.setHeight((short) 500);
				/* 1ST */
				cells = (XSSFCell) CellUtil.createCell(row0, 0, staticHeaderFirstCells[6]);
				// sheet.setColumnWidth(0, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						0, 1, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());
				/* 3rd */
				cells = (XSSFCell) CellUtil.createCell(row0, 2,
						facilityPerformanceData.getFirstName() + " " + facilityPerformanceData.getLastName());
				// sheet.setColumnWidth(0, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.CENTER, false);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						2, 3, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());
				/* 5th */
				cells = (XSSFCell) CellUtil.createCell(row0, 4, "Phone No:");
				// sheet.setColumnWidth(0, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);
				/* 6th */
				cells = (XSSFCell) CellUtil.createCell(row0, 5, facilityPerformanceData.getMobileNumber());
				// sheet.setColumnWidth(5, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.CENTER, false);
				/* 7th */
				cells = (XSSFCell) CellUtil.createCell(row0, 6, "Email-ID:");
				// sheet.setColumnWidth(6, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.RIGHT, true);

				/* 8th */
				cells = (XSSFCell) CellUtil.createCell(row0, 7, facilityPerformanceData.getEmail());
				// sheet.setColumnWidth(7, 15 * 256);
				FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
						HorizontalAlignment.CENTER, false);
				FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum,
						7, 8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());
			}
			/*
			 * -----------------------------------------------------------------------------
			 * 
			 */

			tempRowNum = rowNum;
			row0 = sheet.createRow(rowNum++);

			cells = (XSSFCell) CellUtil.createCell(row0, 0, "INDICATORS FOR REPORTING");
			// sheet.setColumnWidth(0, 15 * 256);
			FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.YELLOW.getIndex(),
					HorizontalAlignment.CENTER, true);
			FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum, tempRowNum, 0,
					8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, BorderStyle.THIN.getCode());

			/*
			 * -----------------------------------------------------------------------------
			 * DYNAMIC DATA - [START]
			 */
			tempRowNum = rowNum;
			row0 = sheet.createRow(rowNum++);
			if (performanceResultDtos != null && performanceResultDtos.size() > 0) {

				for (int i = 0; i < performanceResultDtos.size(); i++) {
					FacilityPerformanceResultDto resultDto = performanceResultDtos.get(i);
					cells = (XSSFCell) CellUtil.createCell(row0, 0, resultDto.getSectionDesc());
					// sheet.setColumnWidth(0, 15 * 256);
					FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.WHITE.getIndex(),
							HorizontalAlignment.CENTER, true);
					FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum,
							tempRowNum, 0, 8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER,
							BorderStyle.THIN.getCode());
					tempRowNum = rowNum;
					row0 = sheet.createRow(rowNum++);
					if (tempRowNum == 13) {
						cells = (XSSFCell) CellUtil.createCell(row0, 0, "");
						// sheet.setColumnWidth(0, 15 * 256);
						FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
								HorizontalAlignment.RIGHT, true);
						FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum,
								tempRowNum, 0, 5, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER,
								BorderStyle.THIN.getCode());
						/* 3rd */
						cells = (XSSFCell) CellUtil.createCell(row0, 6, "Value");
						// sheet.setColumnWidth(0, 15 * 256);
						FacilityPerformanceExcelReport.setCellStyles(cells, workbook, IndexedColors.BLACK.getIndex(),
								HorizontalAlignment.CENTER, true);
						FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum,
								tempRowNum, 6, 8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER,
								BorderStyle.THIN.getCode());
					}
					tempRowNum = rowNum;
					row0 = sheet.createRow(rowNum++);
					// Create cells
					for (int j = 0; j < columns.length; j++) {
						if (j == 0) {
							cells = (XSSFCell) CellUtil.createCell(row0, 0, columns[j]);
							// sheet.setColumnWidth(0, 15 * 256);
							FacilityPerformanceExcelReport.setCellStyles(cells, workbook,
									IndexedColors.BLACK.getIndex(), HorizontalAlignment.CENTER, true);
							FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum,
									tempRowNum, 0, 1, HorizontalAlignment.CENTER, VerticalAlignment.CENTER,
									BorderStyle.THIN.getCode());
						} else if (j == 1) {
							cells = (XSSFCell) CellUtil.createCell(row0, 2, columns[j]);
							// sheet.setColumnWidth(2, 15 * 256);
							FacilityPerformanceExcelReport.setCellStyles(cells, workbook,
									IndexedColors.BLACK.getIndex(), HorizontalAlignment.CENTER, true);
							FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook, tempRowNum,
									tempRowNum, 2, 4, HorizontalAlignment.CENTER, VerticalAlignment.CENTER,
									BorderStyle.THIN.getCode());
						} else if (j == 2) {
							cells = (XSSFCell) CellUtil.createCell(row0, 5, columns[j]);
							// sheet.setColumnWidth(5, 15 * 256);
							FacilityPerformanceExcelReport.setCellStyles(cells, workbook,
									IndexedColors.BLACK.getIndex(), HorizontalAlignment.CENTER, true);
						} else if (j == 3) {
							cells = (XSSFCell) CellUtil.createCell(row0, 6, columns[j]);
							// sheet.setColumnWidth(6, 15 * 256);
							FacilityPerformanceExcelReport.setCellStyles(cells, workbook,
									IndexedColors.BLACK.getIndex(), HorizontalAlignment.CENTER, true);
						} else if (j == 4) {
							cells = (XSSFCell) CellUtil.createCell(row0, 7, columns[j]);
							// sheet.setColumnWidth(7, 15 * 256);
							FacilityPerformanceExcelReport.setCellStyles(cells, workbook,
									IndexedColors.BLACK.getIndex(), HorizontalAlignment.CENTER, true);
						} else {
							cells = (XSSFCell) CellUtil.createCell(row0, 8, columns[j]);
							// sheet.setColumnWidth(8, 15 * 256);
							FacilityPerformanceExcelReport.setCellStyles(cells, workbook,
									IndexedColors.BLACK.getIndex(), HorizontalAlignment.CENTER, true);
						}
					}

					// [DYNAMIC DATA INTEGRATION
					// ITERATION]-START-----------------------------------
					if (resultDto.getIndicatorList() != null && resultDto.getIndicatorList().size() > 0) {
						for (FacilityPerformanceDto performanceDto : resultDto.getIndicatorList()) {
							tempRowNum = rowNum;
							row0 = sheet.createRow(rowNum++);

							for (int j = 0; j < columns.length; j++) {
								if (j == 0) {
									cells = (XSSFCell) CellUtil.createCell(row0, 0, performanceDto.getSlNo());
									// sheet.setColumnWidth(0, 15 * 256);
									FacilityPerformanceExcelReport.setCellStyles(cells, workbook,
											IndexedColors.BLACK.getIndex(), HorizontalAlignment.CENTER, true);
									FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook,
											tempRowNum, tempRowNum, 0, 1, HorizontalAlignment.CENTER,
											VerticalAlignment.CENTER, BorderStyle.THIN.getCode());
								} else if (j == 1) {
									cells = (XSSFCell) CellUtil.createCell(row0, 2, performanceDto.getIdicatorDesc());
									// sheet.setColumnWidth(2, 15 * 256);
									row0.setHeight((short) 600);
									FacilityPerformanceExcelReport.setCellStyles(cells, workbook,
											IndexedColors.BLACK.getIndex(), HorizontalAlignment.CENTER, true);
									FacilityPerformanceExcelReport.setMergeProeprtyForCells(cells, sheet, workbook,
											tempRowNum, tempRowNum, 2, 4, HorizontalAlignment.CENTER,
											VerticalAlignment.CENTER, BorderStyle.THIN.getCode());
								} else if (j == 2) {
									cells = (XSSFCell) CellUtil.createCell(row0, 5, performanceDto.getSourceDesc());
									// sheet.setColumnWidth(5, 15 * 256);
									FacilityPerformanceExcelReport.setCellStyles(cells, workbook,
											IndexedColors.BLACK.getIndex(), HorizontalAlignment.CENTER, true);
								} else if (j == 3) {
									cells = (XSSFCell) CellUtil.createCell(row0, 6,
											String.valueOf(performanceDto.getMaleCount()));
									// sheet.setColumnWidth(6, 15 * 256);
									FacilityPerformanceExcelReport.setCellStyles(cells, workbook,
											IndexedColors.BLACK.getIndex(), HorizontalAlignment.CENTER, true);
								} else if (j == 4) {
									cells = (XSSFCell) CellUtil.createCell(row0, 7,
											String.valueOf(performanceDto.getFemaleCount()));
									// sheet.setColumnWidth(7, 15 * 256);
									FacilityPerformanceExcelReport.setCellStyles(cells, workbook,
											IndexedColors.BLACK.getIndex(), HorizontalAlignment.CENTER, true);
								} else {
									cells = (XSSFCell) CellUtil.createCell(row0, 8,
											String.valueOf(performanceDto.getTotalCount()));
									// sheet.setColumnWidth(8, 15 * 256);
									FacilityPerformanceExcelReport.setCellStyles(cells, workbook,
											IndexedColors.BLACK.getIndex(), HorizontalAlignment.CENTER, true);
								}
							}

						}
					}
				}
			}

			// [DYNAMIC DATA INTEGRATION -
			// ITERATION]-END-------------------------------------
			/*
			 * -----------------------------------------------------------------------------
			 * DYNAMIC DATA - [END]
			 */

			for (int rw = 0; rw < 3; rw++) {
				XSSFRow xssfRow = sheet.getRow(rw);
				if (rw != 2) {
					for (int c = 0; c < 9; c++) {

						XSSFCellStyle xssfCellStyle = xssfRow.getCell(c).getCellStyle();
						xssfCellStyle.setFillForegroundColor(new XSSFColor(new Color(198, 89, 17)));
						FillPatternType fillPatternType = FillPatternType.SOLID_FOREGROUND;
						xssfCellStyle.setFillPattern(fillPatternType);
					}
				} else {
					for (int c = 0; c < 9; c++) {

						XSSFCellStyle xssfCellStyle = xssfRow.getCell(c).getCellStyle();
						xssfCellStyle.setFillForegroundColor(new XSSFColor(new Color(128, 128, 128)));
						FillPatternType fillPatternType = FillPatternType.SOLID_FOREGROUND;
						xssfCellStyle.setFillPattern(fillPatternType);
					}
				}

			}
			try {
				XSSFRow xssfRow;
				for (int r = 3; r <= 14; r++) {
					xssfRow = sheet.getRow(r);
					for (int c = 0; c < 9; c++) {
						if (xssfRow.getCell(c) != null) {
							XSSFCellStyle xssfCellStyle = xssfRow.getCell(c).getCellStyle();
							if (r == 11 || r == 12) {
								xssfCellStyle.setFillForegroundColor(new XSSFColor(new Color(128, 128, 128)));

							} else {
								xssfCellStyle.setFillForegroundColor(new XSSFColor(new Color(192, 192, 192)));
							}

							FillPatternType fillPatternType = FillPatternType.SOLID_FOREGROUND;
							xssfCellStyle.setFillPattern(fillPatternType);
						}
					}
				}

				int lastRowIndex = sheet.getLastRowNum();
				for (int r = 15; r <= lastRowIndex; r++) {
					xssfRow = sheet.getRow(r);
					for (int c = 0; c <= 8; c++) {
						if (c != 6 && c != 7) {
							if (c != 8) {
								if (xssfRow.getCell(c) != null) {
									XSSFCellStyle xssfCellStyle = xssfRow.getCell(c).getCellStyle();
									xssfCellStyle.setFillForegroundColor(new XSSFColor(new Color(255, 255, 153)));

									FillPatternType fillPatternType = FillPatternType.SOLID_FOREGROUND;
									xssfCellStyle.setFillPattern(fillPatternType);
								}
							} else {
								XSSFCellStyle xssfCellStyle = xssfRow.getCell(c).getCellStyle();
								xssfCellStyle.setFillForegroundColor(new XSSFColor(new Color(217, 217, 217)));

								FillPatternType fillPatternType = FillPatternType.SOLID_FOREGROUND;
								xssfCellStyle.setFillPattern(fillPatternType);
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Resize all columns to fit the content size
			for (int i = 0; i < 9; i++) {
				sheet.autoSizeColumn(i);
			}
			workbook.write(reportStream);
			workbook.close();
		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ByteArrayInputStream(reportStream.toByteArray());
	}

	public static void setStyles(int borderStyle, CellRangeAddress cellRangeAddress, XSSFSheet sheet,
			XSSFWorkbook workbook) {
		// BorderStyle.MEDIUM.getCode()
		RegionUtil.setBorderTop(borderStyle, cellRangeAddress, sheet);
		RegionUtil.setBorderBottom(borderStyle, cellRangeAddress, sheet);
		RegionUtil.setBorderLeft(borderStyle, cellRangeAddress, sheet);
		RegionUtil.setBorderRight(borderStyle, cellRangeAddress, sheet);
	}

	public static void setMergeProeprtyForCells(XSSFCell cell, XSSFSheet sheet, XSSFWorkbook workbook, int startRow,
			int lastRow, int startColumn, int lastColumn, HorizontalAlignment alignmentH, VerticalAlignment alignmentV,
			int borderStyle) {

		CellUtil.setAlignment(cell, alignmentH);
		CellUtil.setVerticalAlignment(cell, alignmentV);

		CellRangeAddress mergedTopHeader = new CellRangeAddress(startRow, lastRow, startColumn, lastColumn);
		sheet.addMergedRegion(mergedTopHeader);

		FacilityPerformanceExcelReport.setStyles(borderStyle, mergedTopHeader, sheet, workbook);
	}

	public static void setCellStyles(XSSFCell cell, XSSFWorkbook workbook, short cellColor,
			HorizontalAlignment horizontalAlignment, boolean bold) {

		CellStyle cellStyle = cell.getCellStyle();
		XSSFFont font = workbook.createFont();
		cellStyle.setWrapText(true);
		font.setColor(cellColor);
		font.setBold(bold);
		font.setFontHeightInPoints((short) 9);
		cellStyle.setFont(font);
		cellStyle.setBorderTop(BorderStyle.MEDIUM);
		cellStyle.setBorderBottom(BorderStyle.MEDIUM);
		cellStyle.setBorderLeft(BorderStyle.MEDIUM);
		cellStyle.setBorderRight(BorderStyle.MEDIUM);
		CellUtil.setAlignment(cell, horizontalAlignment);

	}

}
