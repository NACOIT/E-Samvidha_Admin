package gov.naco.soch.admin.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import gov.naco.soch.dto.StockTransactionDto;
import gov.naco.soch.mapper.StockTransactionMapperUtil;
import gov.naco.soch.projection.StockTransactionProjection;
import gov.naco.soch.repository.FacilityStockTrackingRepository;

@Transactional
@Service
public class StockTransactionReportService {
	
	@Autowired
	private FacilityStockTrackingRepository facilityStockTrackingRepository;

	public List<StockTransactionDto> getStockReport(int facilityId,int productId,Date fromDate ,Date toDate) {

		List<StockTransactionProjection> stockTransactionProjectionList;
		try {
			stockTransactionProjectionList = facilityStockTrackingRepository.getStockReport(facilityId,productId,fromDate,toDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return StockTransactionMapperUtil.mapToStockTransactionSet(stockTransactionProjectionList);
	}

	public ByteArrayInputStream exportToExcel(int facilityId, int productId, Date fromDate, Date toDate) throws IOException {
		// TODO Auto-generated method stub
		List<StockTransactionProjection> stockTransactionProjectionList = 
				          facilityStockTrackingRepository.getStockReport(facilityId,productId,fromDate,toDate);
		ByteArrayInputStream inputStream = exportExcelData(stockTransactionProjectionList,fromDate,toDate);
		return inputStream;
	}

	private ByteArrayInputStream exportExcelData(List<StockTransactionProjection> stockTransactionProjectionList,Date fromDate, Date toDate) throws IOException {
		
		ByteArrayOutputStream reportStream = new ByteArrayOutputStream();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Expose-Headers", "Content-Disposition");
		headers.add("Content-Disposition", "attachment; filename=Facility_Performance.xlsx");

		String[] columns = { "Date", "Transaction Type", "Withdrawal(Tabs)", "Deposit (Tabs)", "Balance (Tabs)",
				"Remarks" };

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Transaction Stock Report");
		int rowNum = 1;
		XSSFRow row0;
		row0 = sheet.createRow(rowNum++);
		row0.setHeight((short) 400);// Set row height
		XSSFCell cell;
		cell = (XSSFCell) CellUtil.createCell(row0, 1, "Date From");
		StockTransactionReportService.setCellFontStyle(cell, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, true,
				workbook);
		cell = (XSSFCell) CellUtil.createCell(row0, 2, String.valueOf(fromDate));
		StockTransactionReportService.setCellFontStyle(cell, HorizontalAlignment.CENTER, VerticalAlignment.CENTER,
				false, workbook);
		StockTransactionReportService.mergeCells(1, 1, 2, 3, sheet, workbook, false);
		cell = (XSSFCell) CellUtil.createCell(row0, 4, "Choose Product");
		StockTransactionReportService.setCellFontStyle(cell, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, true,
				workbook);
		cell = (XSSFCell) CellUtil.createCell(row0, 5, "Burpenorphine 2mgF");
		StockTransactionReportService.mergeCells(1, 1, 5, 6, sheet, workbook, false);
		StockTransactionReportService.setCellFontStyle(cell, HorizontalAlignment.CENTER, VerticalAlignment.CENTER,
				false, workbook);
		/*---------------------------------------------------------------*/
		row0 = sheet.createRow(rowNum++);
		cell = (XSSFCell) CellUtil.createCell(row0, 1, "Date To");
		StockTransactionReportService.setCellFontStyle(cell, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, true,
				workbook);
		cell = (XSSFCell) CellUtil.createCell(row0, 2, String.valueOf(toDate));
		StockTransactionReportService.mergeCells(2, 2, 2, 3, sheet, workbook, false);
		StockTransactionReportService.setCellFontStyle(cell, HorizontalAlignment.CENTER, VerticalAlignment.CENTER,
				false, workbook);
		/*---------------------------------------------------------------*/
		row0 = sheet.createRow(rowNum++);
		cell = (XSSFCell) CellUtil.createCell(row0, 1, "Stock Transactional Report");
		StockTransactionReportService.mergeCells(3, 3, 1, 6, sheet, workbook, false);
		
		StockTransactionReportService.setCellFontStyle(cell, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, true,
				workbook);
		/*---------------------------------------------------------------*/
		row0 = sheet.createRow(rowNum++);
		for (int i = 0; i < columns.length; i++) {
			CellUtil.createCell(row0, i + 1, columns[i]);
		}
		/*---------------------------------------------------------------*/
		
		for (StockTransactionProjection stockTransactionProjection : stockTransactionProjectionList) {
			XSSFRow rowgridData = sheet.createRow(rowNum++);
			rowgridData.createCell(1).setCellValue(stockTransactionProjection.getStrTrnDate());
			rowgridData.createCell(2).setCellValue(stockTransactionProjection.getStrTrnType());
			rowgridData.createCell(3).setCellValue(stockTransactionProjection.getStrWdlTbs());
		    rowgridData.createCell(4).setCellValue(stockTransactionProjection.getStrDepTbs());
			rowgridData.createCell(5).setCellValue(stockTransactionProjection.getStrBalance());
			rowgridData.createCell(6).setCellValue(stockTransactionProjection.getStrTrnRem());
		}

		/*---------------------------------------------------------------*/
		// Resize all columns to fit the content size
		for (int i = 1; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}
		/*---------------------------------------------------------------*/

		StockTransactionReportService.setBordersToRangeOfCells(1, 2, 1, 6, sheet, workbook);
		int lastRowIndex = sheet.getLastRowNum();
		StockTransactionReportService.setBordersToRangeOfCells(3, lastRowIndex, 1, 6, sheet, workbook);

		/*---------------------------------------------------------------*/
		workbook.write(reportStream);
		workbook.close();
		//ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(reportStream.toByteArray());
		return new ByteArrayInputStream(reportStream.toByteArray());
		//return ResponseEntity.ok().headers(headers).body(new InputStreamResource(arrayInputStream));
	}
	
	public static void mergeCells(int sRow, int lRow, int sColumn, int lColumn, XSSFSheet sheet, XSSFWorkbook workbook,
			boolean setRangeBorder) {
		CellRangeAddress cellRangeAddress = new CellRangeAddress(sRow, lRow, sColumn, lColumn);
		sheet.addMergedRegion(cellRangeAddress);
	}

	public static void setBordersToRangeOfCells(int sRow, int lRow, int sColumn, int lColumn, XSSFSheet sheet,
			XSSFWorkbook workbook) {
		XSSFCellStyle styleTop = workbook.createCellStyle();
		XSSFCellStyle styleBottom = workbook.createCellStyle();
		XSSFCellStyle styleLeft = workbook.createCellStyle();
		XSSFCellStyle styleRight = workbook.createCellStyle();
		XSSFCellStyle styleLeftTop = workbook.createCellStyle();
		XSSFCellStyle styleRightTop = workbook.createCellStyle();
		XSSFCellStyle styleRightBottom = workbook.createCellStyle();
		XSSFCellStyle styleLeftBottom = workbook.createCellStyle();

		styleBottom.setBorderBottom(BorderStyle.MEDIUM);
		styleLeft.setBorderLeft(BorderStyle.MEDIUM);
		styleRight.setBorderRight(BorderStyle.MEDIUM);
		styleTop.setBorderTop(BorderStyle.MEDIUM);
		/* ------------------------------------------ */
		styleLeftTop.setBorderLeft(BorderStyle.MEDIUM);
		styleLeftTop.setBorderTop(BorderStyle.MEDIUM);
		/* ------------------------------------------ */
		styleRightTop.setBorderRight(BorderStyle.MEDIUM);
		styleRightTop.setBorderTop(BorderStyle.MEDIUM);
		/* ------------------------------------------ */
		styleRightBottom.setBorderRight(BorderStyle.MEDIUM);
		styleRightBottom.setBorderBottom(BorderStyle.MEDIUM);
		/* ------------------------------------------ */
		styleLeftBottom.setBorderBottom(BorderStyle.MEDIUM);
		styleLeftBottom.setBorderLeft(BorderStyle.MEDIUM);
		/* ------------------------------------------ */
		int startRow = sRow;
		int startColumn = sColumn;
		for (int rw = sRow; rw <= lRow; rw++) {
			XSSFRow xssfRow = sheet.getRow(rw);
			for (int c = sColumn; c <= lColumn; c++) {
				XSSFCell xssfCell = xssfRow.getCell(c);
				if (xssfCell == null) {
					xssfCell = xssfRow.createCell(c);
				}

				if (rw == startRow) {
					if (c == startColumn) {
						xssfCell.setCellStyle(styleLeftTop);

					} else if (c == lColumn) {
						xssfCell.setCellStyle(styleRightTop);
					} else {
						xssfCell.setCellStyle(styleTop);
					}
				} else if (rw == lRow) {
					if (c == startColumn) {
						xssfCell.setCellStyle(styleLeftBottom);

					} else if (c == lColumn) {
						xssfCell.setCellStyle(styleRightBottom);
					} else {
						xssfCell.setCellStyle(styleBottom);
					}
				} else {
					if (c == startColumn) {
						xssfCell.setCellStyle(styleLeft);
					} else if (c == lColumn) {
						xssfCell.setCellStyle(styleRight);
					}
				}
			}
		}

	}

	public static void setCellFontStyle(XSSFCell cell, HorizontalAlignment alignmentH, VerticalAlignment alignmentV,
			boolean fontBold, XSSFWorkbook workbook) {

		// get cell style
		XSSFCellStyle cellStyle = cell.getCellStyle();


		cellStyle.setAlignment(alignmentH);
		cellStyle.setVerticalAlignment(alignmentV);
		if (fontBold) {
			XSSFFont font = workbook.createFont();
			cellStyle.setWrapText(true);
			font.setBold(true);
			font.setColor(IndexedColors.BLACK.getIndex());
			cellStyle.setFont(font);

		}
		cell.setCellStyle(cellStyle);
	}
	


}
