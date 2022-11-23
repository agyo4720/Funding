package com.funding.user;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterValidation {
	
	@Size(min = 3, max = 25)
    @NotEmpty(message = "사용자ID는 필수항목입니다.")
	private String username;
	
	@NotEmpty(message = "비밀번호는 필수항목입니다.")
	private String password1;
	
	@NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
	private String password2;
	
	@NotEmpty(message = "이름은 필수항목입니다.")
	private String nickname;
	
//	@Email
//	@NotEmpty(message = "이메일은 필수항목입니다.")
	private String email;
	private String domain;
	private String domainDirect;
	
	@NotEmpty(message = "전화번호는 필수항목입니다.")
	private String mobile;
	
//	@NotEmpty(message = "주소는 필수항목입니다.")
	private String addr1;
	private String addr2;
	private String addr3;
	private String addr4;
	
	@NotEmpty(message = "성별은 필수항목입니다.")
	private String gender;
	
//	@NotEmpty(message = "생년월일은 필수항목입니다.")
	private String year;
	private String month;
	private String day;

	
	@NotEmpty(message = "권한은 필수항목입니다.")
	private String role;
	
	
	
}
