/**
 * 
 */
package gov.naco.soch.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import gov.naco.soch.admin.service.LoginService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoginController {

	// private static final Logger logger =
	// LoggerFactory.getLogger(UserController.class);

	@Autowired
	LoginService loginService;

	// @PostMapping("/authenticate")
	// public @ResponseBody UserAuthDto login(@RequestBody UserAuthDto userAuthDto)
	// {
	// logger.info("login method called with parameters->{}", userAuthDto);
	// userAuthDto resultUserAuthDto = loginService.checkUser(userAuthDto);
	// return resultUserAuthDto;
	// }

}
