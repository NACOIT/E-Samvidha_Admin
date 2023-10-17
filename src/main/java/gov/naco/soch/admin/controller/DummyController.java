package gov.naco.soch.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import gov.naco.soch.admin.service.DummyService;
import gov.naco.soch.admin.service.UserService;
import gov.naco.soch.dto.DummyDto;
import gov.naco.soch.dto.ErrorDto;
import gov.naco.soch.dto.ErrorResponse;
import gov.naco.soch.dto.UserMasterDto;
import gov.naco.soch.exception.ServiceException;

@RestController
@RequestMapping("/dummy")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DummyController {


	@Autowired
	UserService userService;

	@Autowired
	DummyService dummyService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	RestTemplate restTemplate = new RestTemplate();

	public DummyController() {
	}

	// Test API to try sample codes.
	@PostMapping("/addtest")
	public @ResponseBody UserMasterDto addUserTest(@Valid @RequestBody UserMasterDto userDto) {
		logger.info("addUserTest method called with parameters->{}", userDto);
		if ("soch".equalsIgnoreCase(userDto.getFirstname())) {
			List<ErrorDto> errorDtoList = new ArrayList<>();
			List<String> detailsSimplified = new ArrayList<String>();

			ErrorDto errorDto = new ErrorDto();
			errorDto.setField("firstname");
			errorDto.setDescription("Our business does not allow a user named '" + userDto.getFirstname() + "'");
			errorDtoList.add(errorDto);
			detailsSimplified.add(errorDto.getDescription());
			ErrorResponse errorResponse = new ErrorResponse("Validation Failed", errorDtoList, detailsSimplified);
			throw new ServiceException("Our business does not allow a user named '" + userDto.getFirstname() + "'",
					errorResponse, HttpStatus.BAD_REQUEST);
		}
		userDto = new UserMasterDto();
		userDto.setFirstname("created user");
		return userDto;
		// return userService.addUser(userDto);
	}

	@GetMapping("/jbpmhit")
	public @ResponseBody String jbpmHit() {
		String url = "http://localhost:8080/kie-server/services/rest/server/containers/evaluation_1.0.0-SNAPSHOT/processes/evaluation/instances";
		ResponseEntity<String> response = null;
		try {

			String authData = "maciek" + ":" + "maciek";
			String encoded = null;
			//String encoded = new sun.misc.BASE64Encoder().encode(authData.getBytes());

			Map<String, Object> data = new HashMap<>();
			data.put("employee", "maciek");
			data.put("reason", "vim from soch");
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Basic " + encoded);
			HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);
			response = restTemplate.postForEntity(url, request, String.class);
			System.out.println(response);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.getBody();
	}

	@PostMapping("/addUser")
	public @ResponseBody DummyDto addUserDummy(@Valid @RequestBody DummyDto dummyDto) {
		logger.info("addUserTest method called with parameters->{}", dummyDto);
		return dummyService.addUserDummy(dummyDto);
	}

	@GetMapping("/getUser/{id}")
	public @ResponseBody DummyDto getUserDummy(@PathVariable("id") Long userId) {
		logger.info("getUserDummy method called with parameters->{}", userId);
		return dummyService.getUserDummy(userId);
	}


}
