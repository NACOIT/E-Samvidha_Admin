package gov.naco.soch.admin.service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
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
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.DailyDosageDto;
import gov.naco.soch.dto.FacilityPerformanceData;

@Service
public class DailyDosageReportExportService {

	public ByteArrayInputStream exportExcelData(FacilityPerformanceData dailyDosageExportDto,
			List<DailyDosageDto> dailyDosageDtosList) {
		ByteArrayOutputStream reportStream = new ByteArrayOutputStream();
		try {

			/* Setting static headers:-[START] */
			String[] columns = new String[36];
			columns[0] = "Sr.";
			columns[1] = "Name of the  Patient";
			columns[2] = "OST Regn. No.";
			int colIndex = 1;
			for (int i = 3; i <= 33; i++) {
				columns[i] = String.valueOf(colIndex);
				colIndex++;
			}
			columns[34] = "Total Days";
			columns[35] = "Total Dosage of the Client";
			System.out.println("columns : " + columns);
			/* Setting static headers:-[END] */
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Daily_Dosage_Report");
			int rowNum = 0;
			// [firt row end-----------------------------------------------------]
			XSSFRow row0 = sheet.createRow(rowNum++);
			row0.setHeight((short) 600);
			// Creates the cell
			XSSFCell cell = (XSSFCell) CellUtil.createCell(row0, 0, "OST Daily Dosage Sheet");
			CellStyle cellStyle = cell.getCellStyle();
			XSSFFont font = workbook.createFont();
			cellStyle.setWrapText(true);
			cellStyle.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
			font.setBold(true);
			cellStyle.setFont(font);
			HorizontalAlignment alignmentH = HorizontalAlignment.CENTER;
			VerticalAlignment alignmentV = VerticalAlignment.CENTER;
			CellUtil.setAlignment(cell, alignmentH);
			CellUtil.setVerticalAlignment(cell, alignmentV);
			CellRangeAddress mergedTopHeader = new CellRangeAddress(0, 0, 0, 35);
			sheet.addMergedRegion(mergedTopHeader);
			MasterLineReportExportService.setStyles(mergedTopHeader, sheet, workbook);
			// [firt row end-----------------------------------------------------]
			/* 1st Merge -START */
			XSSFRow row1 = sheet.createRow(rowNum++);
			row1.setHeight((short) 1000);
			XSSFRichTextString ostRts = new XSSFRichTextString("OST Center Name and District: ");

			XSSFFont fontColor = workbook.createFont();
			fontColor.setColor(IndexedColors.RED.getIndex());

			ostRts.append(dailyDosageExportDto.getFacilityCenterName() + ", "
					+ dailyDosageExportDto.getFacilityDistrictName(), fontColor);

			XSSFCell cell1 = (XSSFCell) CellUtil.createCell(row1, 0, "");
			cell1.setCellValue(ostRts);
			CellStyle cellStyle1 = cell1.getCellStyle();
			XSSFFont font1 = workbook.createFont();
			cellStyle1.setWrapText(true);
			font1.setBold(true);
			cellStyle1.setFont(font1);
			HorizontalAlignment alignmentH1 = HorizontalAlignment.CENTER;
			VerticalAlignment alignmentV1 = VerticalAlignment.CENTER;
			CellUtil.setAlignment(cell1, alignmentH1);
			CellUtil.setVerticalAlignment(cell1, alignmentV1);
			CellRangeAddress mergedTopHeader1 = new CellRangeAddress(1, 1, 0, 3);
			sheet.addMergedRegion(mergedTopHeader1);
			MasterLineReportExportService.setStyles(mergedTopHeader1, sheet, workbook);
			/* 1st Merge -END */
			/* 2nd Merge -START */
			XSSFRichTextString totReg = new XSSFRichTextString("Total Registered: ");
			XSSFFont fontColorTotReg = workbook.createFont();
			fontColorTotReg.setColor(IndexedColors.RED.getIndex());
			totReg.append(String.valueOf(dailyDosageExportDto.getTotalRegisteredCount()), fontColorTotReg);
			XSSFCell cell2 = (XSSFCell) CellUtil.createCell(row1, 4, "");
			cell2.setCellValue(totReg);
			CellStyle cellStyle2 = cell2.getCellStyle();
			XSSFFont font2 = workbook.createFont();
			cellStyle2.setWrapText(true);
			font2.setBold(true);
			cellStyle2.setFont(font2);
			HorizontalAlignment alignmentH2 = HorizontalAlignment.CENTER;
			VerticalAlignment alignmentV2 = VerticalAlignment.CENTER;
			CellUtil.setAlignment(cell2, alignmentH2);
			CellUtil.setVerticalAlignment(cell2, alignmentV2);
			CellRangeAddress mergedTopHeader2 = new CellRangeAddress(1, 1, 4, 10);
			sheet.addMergedRegion(mergedTopHeader2);
			MasterLineReportExportService.setStyles(mergedTopHeader2, sheet, workbook);
			/* 2nd Merge -END */

			/* 3rd Merge -START */
			XSSFCell cell3 = (XSSFCell) CellUtil.createCell(row1, 11, "");
			XSSFRichTextString regThisMonth = new XSSFRichTextString("Client started on OST this month: ");
			XSSFFont fontColorRegThisMonth = workbook.createFont();
			fontColorRegThisMonth.setColor(IndexedColors.RED.getIndex());
			regThisMonth.append(String.valueOf(dailyDosageExportDto.getRegistrationThisMonth()), fontColorRegThisMonth);
			cell3.setCellValue(regThisMonth);
			CellStyle cellStyle3 = cell3.getCellStyle();
			XSSFFont font3 = workbook.createFont();
			cellStyle3.setWrapText(true);
			font3.setBold(true);
			cellStyle3.setFont(font3);
			HorizontalAlignment alignmentH3 = HorizontalAlignment.CENTER;
			VerticalAlignment alignmentV3 = VerticalAlignment.CENTER;
			CellUtil.setAlignment(cell3, alignmentH3);
			CellUtil.setVerticalAlignment(cell3, alignmentV3);
			CellRangeAddress mergedTopHeader3 = new CellRangeAddress(1, 1, 11, 15);
			sheet.addMergedRegion(mergedTopHeader3);
			MasterLineReportExportService.setStyles(mergedTopHeader3, sheet, workbook);
			/* 3rd Merge -END */
			/* 4th Merge -START */
			Calendar cal = Calendar.getInstance(TimeZone.getDefault());
			XSSFCell cell4 = (XSSFCell) CellUtil.createCell(row1, 16, "");
			XSSFRichTextString reportDate = new XSSFRichTextString("Reporting Month & Year: ");
			XSSFFont fontColorReportDate = workbook.createFont();
			fontColorReportDate.setColor(IndexedColors.RED.getIndex());
			reportDate.append(String.valueOf(cal.get(Calendar.MONTH) + ", " + cal.get(Calendar.YEAR)),
					fontColorReportDate);
			cell4.setCellValue(reportDate);
			CellStyle cellStyle4 = cell4.getCellStyle();
			XSSFFont font4 = workbook.createFont();
			cellStyle4.setWrapText(true);
			font4.setBold(true);
			cellStyle4.setFont(font4);
			HorizontalAlignment alignmentH4 = HorizontalAlignment.CENTER;
			VerticalAlignment alignmentV4 = VerticalAlignment.CENTER;
			CellUtil.setAlignment(cell4, alignmentH4);
			CellUtil.setVerticalAlignment(cell4, alignmentV4);
			CellRangeAddress mergedTopHeader4 = new CellRangeAddress(1, 1, 16, 25);
			sheet.addMergedRegion(mergedTopHeader4);
			MasterLineReportExportService.setStyles(mergedTopHeader4, sheet, workbook);
			/* 4th Merge -END */
			/* 5th Merge -START */
			XSSFCell cell5 = (XSSFCell) CellUtil.createCell(row1, 26, "");
			XSSFRichTextString reportPerson = new XSSFRichTextString("Reporting Person: ");
			XSSFFont fontColorReportPerson = workbook.createFont();
			fontColorReportPerson.setColor(IndexedColors.RED.getIndex());
			reportPerson.append(
					String.valueOf(dailyDosageExportDto.getFirstName() + " " + dailyDosageExportDto.getLastName()),
					fontColorReportPerson);
			cell5.setCellValue(reportPerson);
			CellStyle cellStyle5 = cell5.getCellStyle();
			XSSFFont font5 = workbook.createFont();
			cellStyle5.setWrapText(true);
			font5.setBold(true);
			cellStyle5.setFont(font5);
			HorizontalAlignment alignmentH5 = HorizontalAlignment.CENTER;
			VerticalAlignment alignmentV5 = VerticalAlignment.CENTER;
			CellUtil.setAlignment(cell5, alignmentH5);
			CellUtil.setVerticalAlignment(cell5, alignmentV5);
			CellRangeAddress mergedTopHeader5 = new CellRangeAddress(1, 1, 26, 35);
			sheet.addMergedRegion(mergedTopHeader5);
			MasterLineReportExportService.setStyles(mergedTopHeader5, sheet, workbook);
			/* 5th Merge -END */
			// [3RD ROW START]------------------------------------------------
			XSSFRow headerRow = sheet.createRow(rowNum++);
			// Create a Font for styling header cells
			XSSFFont headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 10);
			headerFont.setColor(IndexedColors.BLACK.getIndex());

			// Create a CellStyle with the font
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setWrapText(true);
			headerCellStyle.setFont(headerFont);
			// Create cells
			for (int i = 0; i < columns.length; i++) {
				XSSFCell cellHeader = headerRow.createCell(i);
				cellHeader.setCellValue(String.valueOf(columns[i]));
				cellHeader.setCellStyle(headerCellStyle);
				XSSFCellStyle cellColumnStyle = headerRow.getCell(i).getCellStyle();
				if (i >= 3 && i <= 33) {
					XSSFFont numCellsFont = workbook.createFont();
					numCellsFont.setColor(IndexedColors.RED.getIndex());
					cellColumnStyle.setFont(numCellsFont);
					/*
					 * CellType cellType = CellType.NUMERIC;
					 * headerRow.getCell(i).setCellType(cellType);
					 */
				}

				cellColumnStyle.setBorderTop(BorderStyle.THIN);
				cellColumnStyle.setBorderBottom(BorderStyle.THIN);
				cellColumnStyle.setBorderLeft(BorderStyle.THIN);
				cellColumnStyle.setBorderRight(BorderStyle.THIN);
			}
			// -------------------------------------------------------------------------------------------------------
			for (int rw = 0; rw < 2; rw++) {
				XSSFRow xssfRow = sheet.getRow(rw);
				for (int c = 0; c <= 35; c++) {
					XSSFCellStyle xssfCellStyle = xssfRow.getCell(c).getCellStyle();
					xssfCellStyle.setFillForegroundColor(new XSSFColor(new Color(197, 217, 241)));
					FillPatternType fillPatternType = FillPatternType.SOLID_FOREGROUND;
					xssfCellStyle.setFillPattern(fillPatternType);
				}
			}
			XSSFRow row = sheet.getRow(2);
			for (int c = 0; c <= 35; c++) {
				XSSFCellStyle xssfCellStyle = row.getCell(c).getCellStyle();
				xssfCellStyle.setFillForegroundColor(new XSSFColor(new Color(218, 150, 148)));
				FillPatternType fillPatternType = FillPatternType.SOLID_FOREGROUND;
				xssfCellStyle.setFillPattern(fillPatternType);
			}

			// [DYNAMIC DATA INTEGRATION -
			// ITERATION]-START-----------------------------------
			int slNo = 0;
			CellType cellType = CellType.NUMERIC;
			for (DailyDosageDto dailyDosageDto : dailyDosageDtosList) {
				XSSFRow rowgridData = sheet.createRow(rowNum++);
				rowgridData.createCell(0).setCellValue(++slNo);
				rowgridData.createCell(1).setCellValue(dailyDosageDto.getPatientName());
				rowgridData.createCell(2).setCellValue(dailyDosageDto.getOstRegnNum());
				rowgridData.createCell(3).setCellValue(dailyDosageDto.getDay1());
				rowgridData.createCell(4).setCellValue(dailyDosageDto.getDay2());
				rowgridData.createCell(5).setCellValue(dailyDosageDto.getDay3());
				rowgridData.createCell(6).setCellValue(dailyDosageDto.getDay4());
				rowgridData.createCell(7).setCellValue(dailyDosageDto.getDay5());
				rowgridData.createCell(8).setCellValue(dailyDosageDto.getDay6());
				rowgridData.createCell(9).setCellValue(dailyDosageDto.getDay7());
				rowgridData.createCell(10).setCellValue(dailyDosageDto.getDay8());
				rowgridData.createCell(11).setCellValue(dailyDosageDto.getDay9());
				rowgridData.createCell(12).setCellValue(dailyDosageDto.getDay10());
				rowgridData.createCell(13).setCellValue(dailyDosageDto.getDay11());
				rowgridData.createCell(14).setCellValue(dailyDosageDto.getDay12());
				rowgridData.createCell(15).setCellValue(dailyDosageDto.getDay13());
				rowgridData.createCell(16).setCellValue(dailyDosageDto.getDay14());
				rowgridData.createCell(17).setCellValue(dailyDosageDto.getDay15());
				rowgridData.createCell(18).setCellValue(dailyDosageDto.getDay16());
				rowgridData.createCell(19).setCellValue(dailyDosageDto.getDay17());
				rowgridData.createCell(20).setCellValue(dailyDosageDto.getDay18());
				rowgridData.createCell(21).setCellValue(dailyDosageDto.getDay19());
				rowgridData.createCell(22).setCellValue(dailyDosageDto.getDay20());
				rowgridData.createCell(23).setCellValue(dailyDosageDto.getDay21());
				rowgridData.createCell(24).setCellValue(dailyDosageDto.getDay22());
				rowgridData.createCell(25).setCellValue(dailyDosageDto.getDay23());
				rowgridData.createCell(26).setCellValue(dailyDosageDto.getDay24());
				rowgridData.createCell(27).setCellValue(dailyDosageDto.getDay25());
				rowgridData.createCell(28).setCellValue(dailyDosageDto.getDay26());
				rowgridData.createCell(29).setCellValue(dailyDosageDto.getDay27());
				rowgridData.createCell(30).setCellValue(dailyDosageDto.getDay28());
				rowgridData.createCell(31).setCellValue(dailyDosageDto.getDay29());
				rowgridData.createCell(32).setCellValue(dailyDosageDto.getDay30());
				rowgridData.createCell(33).setCellValue(dailyDosageDto.getDay31());
				rowgridData.createCell(34).setCellValue(dailyDosageDto.getTotalDoseDays());
				rowgridData.createCell(35).setCellValue(dailyDosageDto.getTotalDose());

			}
			// [DYNAMIC DATA INTEGRATION -
			// ITERATION]-END-------------------------------------

			workbook.write(reportStream);
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ByteArrayInputStream(reportStream.toByteArray());
	}

	public static void setStyles(CellRangeAddress cellRangeAddress, XSSFSheet sheet, XSSFWorkbook workbook) {
		RegionUtil.setBorderTop(BorderStyle.MEDIUM.getCode(), cellRangeAddress, sheet);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM.getCode(), cellRangeAddress, sheet);
		RegionUtil.setBorderLeft(BorderStyle.MEDIUM.getCode(), cellRangeAddress, sheet);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM.getCode(), cellRangeAddress, sheet);
	}

}
