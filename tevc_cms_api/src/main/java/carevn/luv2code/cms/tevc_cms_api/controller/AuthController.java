package carevn.luv2code.cms.tevc_cms_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.requests.AuthRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.LogoutRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.RegisterRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.response.AuthResponse;
import carevn.luv2code.cms.tevc_cms_api.dto.response.LogoutResponse;
import carevn.luv2code.cms.tevc_cms_api.repository.UserRepository;
import carevn.luv2code.cms.tevc_cms_api.security.AuthService;
import carevn.luv2code.cms.tevc_cms_api.security.JwtService;
import carevn.luv2code.cms.tevc_cms_api.security.TokenBlacklist;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final TokenBlacklist tokenBlacklist;

    //    @PostMapping("/login")
    //    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
    //        authenticationManager.authenticate(
    //                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    //
    //        return userRepository
    //                .findByEmail(request.getEmail())
    //                .filter(User::isEnabled)
    //                .map(user -> {
    //                    String token = jwtService.generateToken(user);
    //                    return ResponseEntity.ok(new AuthenticationResponse(token));
    //                })
    //                .orElseGet(() -> ResponseEntity.badRequest().build());
    //    }

    //    @PostMapping("/register")
    //    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    //        String token = authService.register(request);
    //        return ResponseEntity.ok(new AuthenticationResponse(token));
    //    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LogoutResponse> logout(@RequestBody LogoutRequest request) {
        tokenBlacklist.addToBlacklist(request.getToken());

        return ResponseEntity.ok(new LogoutResponse("Đăng xuất thành công"));
    }
}
