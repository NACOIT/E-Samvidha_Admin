package gov.naco.soch.admin.service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.exception.ServiceException;

@Service
@Transactional
public class FileUploadService {

	private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

	private String fileStorageLocation = null;
	private Path fileLocation = null;
	private String baseFileUrl = "/ngo/frontend/src/assets/uploadDocuments";
	private String uploadUrlName = "";
	//private String uploadUrlName = "/assets/uploadDocuments";


	public String setLocation(Environment env, String folderName) {
		this.uploadUrlName = this.baseFileUrl+"/"+folderName;
		this.fileLocation = Paths.get(env.getProperty("app.file.upload-dir", this.uploadUrlName)).toAbsolutePath().normalize();
				try {
					Files.createDirectories(this.fileLocation);
					this.fileStorageLocation = this.fileLocation.toString();
				} catch (Exception ex) {
					throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
				}
				this.uploadUrlName = "";
		return this.fileStorageLocation;
	}
	
	public void storeFile(MultipartFile file, String fileName) {
	    // Normalize file name
	  //  String fileName =      new Date().getTime() + "-file." + getFileExtension(file.getOriginalFilename());

	    try {
	      // Check if the filename contains invalid characters
	      if (fileName.contains("..")) {
	        throw new RuntimeException(
	            "Sorry! Filename contains invalid path sequence " + fileName);
	      }

	      Path targetLocation = this.fileLocation.resolve(fileName);
	      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
	     	      
	     // Files.isDirectory(targetLocation);
     //    outputStream.close();
	     // return fileName;
	    } catch (IOException ex) {
	      throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
	    }
	  }

	  private String getFileExtension(String fileName) {
	    if (fileName == null) {
	      return null;
	    }
	    String[] fileNameParts = fileName.split("\\.");

	    return fileNameParts[fileNameParts.length - 1];
	  }
	
	/**
	 * To through By passing String
	 * 
	 * @param errorString
	 * @param errorType
	 */
	private void throwErrorManually(String errorString, String errorType) {
		List<ErrorDto> errorDtoList = new ArrayList<>();
		List<String> detailsSimplified = new ArrayList<String>();
		ErrorDto errorDto = new ErrorDto();
		errorDto.setField(errorType);
		errorDto.setDescription(errorString);
		errorDtoList.add(errorDto);
		detailsSimplified.add(errorDto.getDescription());
		ErrorResponse errorResponse = new ErrorResponse(errorDtoList.toString(), errorDtoList, detailsSimplified);
		throw new ServiceException(errorString, errorResponse, HttpStatus.BAD_REQUEST);
	}
}
