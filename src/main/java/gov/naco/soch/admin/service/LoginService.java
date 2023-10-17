/**
 * 
 */
package gov.naco.soch.admin.service;

import org.springframework.stereotype.Service;

/**
 * @author Pranav MS (144958)
 * @email pranav.sasi@ust-global.com
 * @date 2020-Jan-07 6:05:47 pm
 * 
 */
@Service
public class LoginService {

//	@Autowired
//	UserAuthRepository authRepository;
//	Object userObject = null;
//
//	public UserAuthDto checkUser(UserAuthDto userAuthDto) {
//		System.out.println(userAuthDto.toString());
//		String userName = userAuthDto.getUsername();
//		Object[] userAuthObjectArray = (Object[]) authRepository.findByUsername(userName.toString());
//		String dbPassword = (String) userAuthObjectArray[4];
//		String dtoPassword = userAuthDto.getPassword();
//		List<ErrorDto> errorDtoList = new ArrayList<>();
//		List<String> detailsSimplified = new ArrayList<String>();
//
//		if (!dbPassword.equals(dtoPassword) || userAuthObjectArray == null) {
//			ErrorDto errorDto = new ErrorDto();
//			errorDto.setField("username");
//			errorDto.setDescription("Invalid username or password !..");
//			errorDtoList.add(errorDto);
//
//			ErrorResponse errorResponse = new ErrorResponse("Invalid username or password !..", errorDtoList,
//					detailsSimplified);
//			throw new ServiceException("Invalid username or password", errorResponse, HttpStatus.UNAUTHORIZED);
//
//		}
//		return UserMapperUtil.mapToUserAuthDto(userAuthObjectArray);
//
//	}

}
