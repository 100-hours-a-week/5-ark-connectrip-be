package connectripbe.connectrip_be.auth.service;

import connectripbe.connectrip_be.auth.dto.SignInDto;
import connectripbe.connectrip_be.auth.dto.SignUpDto;
import connectripbe.connectrip_be.auth.jwt.dto.TokenDto;
import org.springframework.web.multipart.MultipartFile;


public interface AuthService {

    SignUpDto signUp(SignUpDto request, MultipartFile multipartFile);

    TokenDto signIn(SignInDto request);

    TokenDto generateToken(long memberId);

    TokenDto generateKaKaoTempToken(String memberEmail, String memberProfileImagePath);
}
