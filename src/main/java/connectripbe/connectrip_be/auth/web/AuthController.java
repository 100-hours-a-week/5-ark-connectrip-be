package connectripbe.connectrip_be.auth.web;


import connectripbe.connectrip_be.auth.dto.LogoutDto;
import connectripbe.connectrip_be.auth.dto.ReissueDto;
import connectripbe.connectrip_be.auth.dto.SignInDto;
import connectripbe.connectrip_be.auth.dto.SignUpDto;
import connectripbe.connectrip_be.auth.jwt.dto.TokenDto;
import connectripbe.connectrip_be.auth.kakao.service.KakaoService;
import connectripbe.connectrip_be.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    // private final MailService mailService;
    private final KakaoService kakaoService;

    public ResponseEntity<SignUpDto> signUp(@RequestPart("request") SignUpDto request,
                                            @RequestPart(name = "image", required = false) MultipartFile image) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.signUp(request, image));
    }

    @PostMapping(path = "/signin1", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDto> signIn1(@RequestBody SignInDto request) {
        return ResponseEntity.ok(authService.signIn(request));
    }

    @PostMapping(path = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDto> signIn(@RequestBody SignInDto request) {
        return ResponseEntity.ok(authService.signIn(request));
    }

    @PostMapping(path = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout(@RequestBody LogoutDto request) {

        authService.logout(request);
        return ResponseEntity.status(HttpStatus.OK).body("로그아웃 성공");
    }

    @PostMapping(path = "/reissue", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenDto> reissue(@Valid @RequestBody ReissueDto request) {

        return ResponseEntity.ok(authService.reissue(request));
    }

    // noah-Q. 시큐리티를 사용하는데 로그인이 왜 컨트롤러에 있지? 고민 중
    @GetMapping("/redirected/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String code, HttpServletResponse httpServletResponse) {
        // fixme: Object가 아닌 TokenDto 반환으로 변경
        TokenDto tokenDto = (TokenDto) kakaoService.kakaoLogin(code);

        Cookie refreshTokenCookie = new Cookie("access_token", tokenDto.getRefreshToken());
        // fixme: TokenDto int 타입으로 변경
        refreshTokenCookie.setMaxAge(Math.toIntExact(tokenDto.getRefreshTokenExpireTime()));
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");

        httpServletResponse.addCookie(refreshTokenCookie);

        Cookie accesssTokenCookie = new Cookie("refresh_token", tokenDto.getAccessToken());
        accesssTokenCookie.setMaxAge(Math.toIntExact(tokenDto.getAccessTokenExpireTime()));
        accesssTokenCookie.setHttpOnly(true);
        accesssTokenCookie.setSecure(true);
        accesssTokenCookie.setPath("/");

        httpServletResponse.addCookie(accesssTokenCookie);

        return ResponseEntity.ok(kakaoService.kakaoLogin(code));
    }
}
