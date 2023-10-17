package gov.naco.soch.admin.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.naco.soch.projection.ProductListProjection;
import gov.naco.soch.repository.ProductRepository;

@Transactional
@Service
public class ProductListReportService {
	
	@Autowired
	private ProductRepository productRepository;
	
	public List<ProductListProjection> getProductsByFacility(Long facilityId) {
		List<ProductListProjection> productListProjectionList = new ArrayList<ProductListProjection>();
		productListProjectionList =  productRepository.getProductsByFacility(facilityId);
		return productListProjectionList;
		
	}


	public ByteArrayInputStream exportToExcel(Long facilityId) {
		// TODO Auto-generated method stub
		List<ProductListProjection> productListProjectionList = new ArrayList<ProductListProjection>();
		productListProjectionList =  productRepository.getProductsByFacility(facilityId);
		ByteArrayInputStream inputStream = exportExcelData(productListProjectionList);
		return inputStream;
	}

	public ByteArrayInputStream exportExcelData(List<ProductListProjection> productListProjectionList) {
		ByteArrayOutputStream reportStream = new ByteArrayOutputStream();
		// To get Addess and facility details

		String[] columns = { "Sr.", "Product Name", "Batch Number", "Manufacture Date", "Expiry Date",
				"Unit of Measurement", "Available Stock", "Stock Consumed Today" };

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Product List");
		int rowNum = 0;
		try {
			if (productListProjectionList.size() > 0) {
				// [First ROW]-[START]
				// Start]-------------------------------------------------------------
				XSSFRow row0 = sheet.createRow(rowNum++);
				row0.setHeight((short) 400);// Set row height
				// Creates the cell
				XSSFCell cell = (XSSFCell) CellUtil.createCell(row0, 0, "Stock Summary - OST Centre");
				CellStyle cellStyle = cell.getCellStyle();
				XSSFFont font = workbook.createFont();
				font.setBold(true);
				cellStyle.setFont(font);
				HorizontalAlignment alignmentH = HorizontalAlignment.CENTER;
				VerticalAlignment alignmentV = VerticalAlignment.CENTER;
				CellUtil.setAlignment(cell, alignmentH);
				CellUtil.setVerticalAlignment(cell, alignmentV);
				CellRangeAddress mergedTopHeader = new CellRangeAddress(0, 0, 0, 7);
				sheet.addMergedRegion(mergedTopHeader);
				ProductListReportService.setStyles(mergedTopHeader, sheet, workbook);

				// [firt row end-----------------------------------------------------]

				// [2nd ROW start - 1 MERGED
				// RANGE]-------------------------------------------------------------
				ProductListProjection lineProjection = productListProjectionList.get(0);
				XSSFRow row1 = sheet.createRow(rowNum++);
				row1.setHeight((short) 400);
				XSSFCell cell1 = (XSSFCell) CellUtil.createCell(row1, 0,
						"Centre Name: " + lineProjection.getPlCenName());
				CellStyle cellStyle1 = cell1.getCellStyle();
				XSSFFont font1 = workbook.createFont();
				font1.setBold(true);
				cellStyle1.setFont(font1);
				HorizontalAlignment alignmentH1 = HorizontalAlignment.CENTER;
				VerticalAlignment alignmentV1 = VerticalAlignment.CENTER;
				CellUtil.setAlignment(cell1, alignmentH1);
				CellUtil.setVerticalAlignment(cell1, alignmentV1);
				CellRangeAddress mergedChildHeader = new CellRangeAddress(1, 1, 0, 2);
				sheet.addMergedRegion(mergedChildHeader);
				ProductListReportService.setStyles(mergedChildHeader, sheet, workbook);
				// [2ND row end 1 MERGED RANGE
				// START-----------------------------------------------------]
				// [2ND 2 MERGED
				// RANGE]-------------------------------------------------------------
				XSSFCell cell2 = (XSSFCell) CellUtil.createCell(row1, 3, "Address: " + lineProjection.getPlAddr()
						+ ", " + lineProjection.getPlCity() + "," + lineProjection.getPlDist() +"," +lineProjection.getPlState()
						+ ", " + lineProjection.getPlPin());
				CellStyle cellStyle2 = cell2.getCellStyle();
				XSSFFont font2 = workbook.createFont();
				font2.setBold(true);
				cellStyle2.setFont(font2);
				cellStyle2.setWrapText(true); 
				HorizontalAlignment alignmentH2 = HorizontalAlignment.CENTER;
				VerticalAlignment alignmentV2 = VerticalAlignment.CENTER;
				CellUtil.setAlignment(cell2, alignmentH2);
				CellUtil.setVerticalAlignment(cell2, alignmentV2);
				CellRangeAddress mergedChildHeader1 = new CellRangeAddress(1, 1, 3, 6);
				sheet.addMergedRegion(mergedChildHeader1);
				ProductListReportService.setStyles(mergedChildHeader1, sheet, workbook);
				// [2ND 1 MERGED RANGE END-----------------------------------------------------]
				// [2ND row end 3 MERGED RANGE START
				// START]-------------------------------------------------------------
				XSSFCell cell3 = (XSSFCell) CellUtil.createCell(row1, 7, "Date:" + new Date());
				CellStyle cellStyle3 = cell3.getCellStyle();
				XSSFFont font3 = workbook.createFont();
				font3.setBold(true);
				cellStyle3.setFont(font3);
				cellStyle3.setWrapText(true); 
				HorizontalAlignment alignmentH3 = HorizontalAlignment.CENTER;
				VerticalAlignment alignmentV3 = VerticalAlignment.CENTER;
				CellUtil.setAlignment(cell3, alignmentH3);
				CellUtil.setVerticalAlignment(cell3, alignmentV3);
				cellStyle3.setBorderBottom(BorderStyle.MEDIUM);
				cellStyle3.setBorderTop(BorderStyle.MEDIUM);
				cellStyle3.setBorderLeft(BorderStyle.MEDIUM);
				cellStyle3.setBorderRight(BorderStyle.MEDIUM);
				cellStyle3.setTopBorderColor(IndexedColors.BLACK.getIndex()); 
				cell3.setCellStyle(cellStyle3);
				//CellRangeAddress mergedChildHeader2 = new CellRangeAddress(1, 1, 7, 8);
				//sheet.addMergedRegion(mergedChildHeader2);
				//ProductListReportService.setStyles(mergedChildHeader2, sheet, workbook);
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
				for (ProductListProjection productListProjection : productListProjectionList) {
					XSSFRow rowgridData = sheet.createRow(rowNum++);
					rowgridData.createCell(0).setCellValue(++slNo);
					rowgridData.createCell(1).setCellValue(productListProjection.getPlProdName());
					rowgridData.createCell(2).setCellValue(productListProjection.getPlBatchNum());
					rowgridData.createCell(3).setCellValue(productListProjection.getPlManfDate());
				    rowgridData.createCell(4).setCellValue(productListProjection.getPlExprDate());
					rowgridData.createCell(5).setCellValue(productListProjection.getPlUom());
					rowgridData.createCell(6).setCellValue(productListProjection.getPlAvailStock());
					rowgridData.createCell(7).setCellValue(productListProjection.getPlStockConsumed());

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
				cellStyle.setWrapText(true); 
				HorizontalAlignment alignmentH = HorizontalAlignment.CENTER;
				VerticalAlignment alignmentV = VerticalAlignment.CENTER;
				CellUtil.setAlignment(cell, alignmentH);
				CellUtil.setVerticalAlignment(cell, alignmentV);
				CellRangeAddress mergedTopHeader = new CellRangeAddress(0, 0, 0, 9);
				sheet.addMergedRegion(mergedTopHeader);
				ProductListReportService.setStyles(mergedTopHeader, sheet, workbook);
			}
			workbook.write(reportStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done");
		return new ByteArrayInputStream(reportStream.toByteArray());
	}

	public static void setStyles(CellRangeAddress cellRangeAddress, XSSFSheet sheet, XSSFWorkbook workbook) {
		RegionUtil.setBorderTop(BorderStyle.MEDIUM.getCode(), cellRangeAddress, sheet);
		RegionUtil.setBorderBottom(BorderStyle.MEDIUM.getCode(), cellRangeAddress, sheet);
		RegionUtil.setBorderLeft(BorderStyle.MEDIUM.getCode(), cellRangeAddress, sheet);
		RegionUtil.setBorderRight(BorderStyle.MEDIUM.getCode(), cellRangeAddress, sheet);
	}

}
