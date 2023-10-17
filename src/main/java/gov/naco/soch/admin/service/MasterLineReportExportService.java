package gov.naco.soch.admin.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import gov.naco.soch.projection.MasterLineProjection;

@Service
public class MasterLineReportExportService {

	public ByteArrayInputStream exportExcelData(List<MasterLineProjection> masterLineProjectionList) {
		ByteArrayOutputStream reportStream = new ByteArrayOutputStream();
		// To get Addess and facility details

		String[] columns = { "Sr.", "UID Number", "OST Regn. No.", "Name of the  Patient", "Registration Date",
				"Gender", "Age", "Mobile Number", "Current Status", "Current Dosage" };

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Master Line List");
		int rowNum = 0;
		try {
			if (masterLineProjectionList.size() > 0) {
				// [First ROW]-[START]
				// Start]-------------------------------------------------------------
				XSSFRow row0 = sheet.createRow(rowNum++);
				row0.setHeight((short) 400);// Set row height
				// Creates the cell
				XSSFCell cell = (XSSFCell) CellUtil.createCell(row0, 0, "Master Line List- OST Number");
				CellStyle cellStyle = cell.getCellStyle();
				XSSFFont font = workbook.createFont();
				cellStyle.setWrapText(true);
				font.setBold(true);
				cellStyle.setFont(font);
				HorizontalAlignment alignmentH = HorizontalAlignment.CENTER;
				VerticalAlignment alignmentV = VerticalAlignment.CENTER;
				CellUtil.setAlignment(cell, alignmentH);
				CellUtil.setVerticalAlignment(cell, alignmentV);
				CellRangeAddress mergedTopHeader = new CellRangeAddress(0, 0, 0, 9);
				sheet.addMergedRegion(mergedTopHeader);
				MasterLineReportExportService.setStyles(mergedTopHeader, sheet, workbook);

				// [firt row end-----------------------------------------------------]

				// [2nd ROW start - 1 MERGED
				// RANGE]-------------------------------------------------------------
				MasterLineProjection lineProjection = masterLineProjectionList.get(0);
				XSSFRow row1 = sheet.createRow(rowNum++);
				row1.setHeight((short) 800);
				XSSFCell cell1 = (XSSFCell) CellUtil.createCell(row1, 0,
						"Centre Name: " + lineProjection.getMllCenName());
				CellStyle cellStyle1 = cell1.getCellStyle();
				XSSFFont font1 = workbook.createFont();
				cellStyle1.setWrapText(true);
				font1.setBold(true);
				cellStyle1.setFont(font1);
				HorizontalAlignment alignmentH1 = HorizontalAlignment.CENTER;
				VerticalAlignment alignmentV1 = VerticalAlignment.CENTER;
				CellUtil.setAlignment(cell1, alignmentH1);
				CellUtil.setVerticalAlignment(cell1, alignmentV1);
				CellRangeAddress mergedChildHeader = new CellRangeAddress(1, 1, 0, 2);
				sheet.addMergedRegion(mergedChildHeader);
				MasterLineReportExportService.setStyles(mergedChildHeader, sheet, workbook);
				// [2ND row end 1 MERGED RANGE
				// START-----------------------------------------------------]
				// [2ND 2 MERGED
				// RANGE]-------------------------------------------------------------
				XSSFCell cell2 = (XSSFCell) CellUtil.createCell(row1, 3,
						"Address: " + lineProjection.getMllAddr() + ", " + lineProjection.getMllCity() + ","
								+ lineProjection.getMllDist() + ", Pin: " + lineProjection.getMllPin());
				CellStyle cellStyle2 = cell2.getCellStyle();
				XSSFFont font2 = workbook.createFont();
				font2.setBold(true);
				cellStyle2.setWrapText(true);
				cellStyle2.setFont(font2);
				HorizontalAlignment alignmentH2 = HorizontalAlignment.CENTER;
				VerticalAlignment alignmentV2 = VerticalAlignment.CENTER;
				CellUtil.setAlignment(cell2, alignmentH2);
				CellUtil.setVerticalAlignment(cell2, alignmentV2);
				CellRangeAddress mergedChildHeader1 = new CellRangeAddress(1, 1, 3, 6);
				sheet.addMergedRegion(mergedChildHeader1);
				MasterLineReportExportService.setStyles(mergedChildHeader1, sheet, workbook);
				// [2ND 1 MERGED RANGE END-----------------------------------------------------]
				// [2ND row end 3 MERGED RANGE START
				// START]-------------------------------------------------------------
				XSSFCell cell3 = (XSSFCell) CellUtil.createCell(row1, 7, "Date:" + new Date());

				CellStyle cellStyle3 = cell3.getCellStyle();
				cellStyle3.setWrapText(true);
				XSSFFont font3 = workbook.createFont();
				font3.setBold(true);
				cellStyle3.setFont(font3);
				HorizontalAlignment alignmentH3 = HorizontalAlignment.CENTER;
				VerticalAlignment alignmentV3 = VerticalAlignment.CENTER;
				CellUtil.setAlignment(cell3, alignmentH3);
				CellUtil.setVerticalAlignment(cell3, alignmentV3);
				CellRangeAddress mergedChildHeader2 = new CellRangeAddress(1, 1, 7, 9);
				sheet.addMergedRegion(mergedChildHeader2);
				MasterLineReportExportService.setStyles(mergedChildHeader2, sheet, workbook);
				// [2ND row end 3 MERGED RANGE END
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
					cellHeader.setCellValue(columns[i]);
					cellHeader.setCellStyle(headerCellStyle);
				}
				// [3RD ROW END]------------------------------------------------
				// [DYNAMIC DATA INTEGRATION -
				// ITERATION]-START-----------------------------------
				int slNo = 0;
				CellType cellType = CellType.NUMERIC;
				for (MasterLineProjection masterLineProjection : masterLineProjectionList) {
					XSSFRow rowgridData = sheet.createRow(rowNum++);
					rowgridData.createCell(0).setCellValue(++slNo);
					rowgridData.createCell(1).setCellValue(masterLineProjection.getMllUid());
					rowgridData.createCell(2).setCellValue(masterLineProjection.getMllRegNum());
					rowgridData.createCell(3).setCellValue(masterLineProjection.getMllPatientName());
					rowgridData.createCell(4).setCellValue(masterLineProjection.getMllRegDate());
					rowgridData.createCell(5).setCellValue(masterLineProjection.getMllPatGender());
					rowgridData.createCell(6).setCellValue(masterLineProjection.getMllPatAge());
					rowgridData.getCell(6).setCellType(cellType);
					rowgridData.createCell(7).setCellValue(masterLineProjection.getMllPatMobile());
					rowgridData.createCell(8).setCellValue(masterLineProjection.getMllCurrStatus());
					rowgridData.createCell(9).setCellValue(masterLineProjection.getMllCurrDosage());
					rowgridData.getCell(9).setCellType(cellType);

				}
				// [DYNAMIC DATA INTEGRATION -
				// ITERATION]-END-------------------------------------
				// Resize all columns to fit the content size
				for (int i = 0; i < columns.length; i++) {
					sheet.autoSizeColumn(i);
				}
			} else {
				// [Display no data found message]
				XSSFRow row0 = sheet.createRow(rowNum++);
				row0.setHeight((short) 400);// Set row height
				// Creates the cell
				XSSFCell cell = (XSSFCell) CellUtil.createCell(row0, 0, "No Records Found");
				CellStyle cellStyle = cell.getCellStyle();
				XSSFFont font = workbook.createFont();
				font.setBold(true);
				cellStyle.setFont(font);
				HorizontalAlignment alignmentH = HorizontalAlignment.CENTER;
				VerticalAlignment alignmentV = VerticalAlignment.CENTER;
				CellUtil.setAlignment(cell, alignmentH);
				CellUtil.setVerticalAlignment(cell, alignmentV);
				CellRangeAddress mergedTopHeader = new CellRangeAddress(0, 0, 0, 9);
				sheet.addMergedRegion(mergedTopHeader);
				MasterLineReportExportService.setStyles(mergedTopHeader, sheet, workbook);
			}
			workbook.write(reportStream);
			workbook.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
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
