/**
 * 
 */
package gov.naco.soch.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.GitStatusService;
import gov.naco.soch.dto.GitStatusDto;

/**
 * @date 2020-Dec-30 2:13:54 PM
 */

@RestController
@RequestMapping("/gitstatus")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GitStatusController {

	// Logger method
	private static final Logger logger = LoggerFactory.getLogger(GitStatusController.class);

	public GitStatusController() {
	}

	@Autowired
	private GitStatusService gitStatusService;

	// API to get all details from git_status_master table
	@GetMapping("/list")
	public @ResponseBody List<GitStatusDto> getAllGitStatuses() {
		logger.debug("getAllGitStatuses method called");
		return gitStatusService.getAllGitStatuses();
	}

	// API to save details to git_status_master table
	@PostMapping("/save")
	public @ResponseBody GitStatusDto saveGitStatus(@RequestBody GitStatusDto gitStatusDto) {
		logger.debug("addGitStatus method called with parameters->{}", gitStatusDto);

		// Invoking saveGitStatus method
		gitStatusDto=gitStatusService.saveGitStatus(gitStatusDto);
		logger.debug("addGitStatus method returns with parameters->{}", gitStatusDto);
		return gitStatusDto;
	}

}
