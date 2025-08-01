package carevn.luv2code.cms.tevc_cms_api.controller;

import carevn.luv2code.cms.tevc_cms_api.dto.requests.AuthenticationRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.LogoutRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.RegisterRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.response.AuthenticationResponse;
import carevn.luv2code.cms.tevc_cms_api.dto.response.LogoutResponse;
import carevn.luv2code.cms.tevc_cms_api.entity.User;
import carevn.luv2code.cms.tevc_cms_api.repository.UserRepository;
import carevn.luv2code.cms.tevc_cms_api.security.AuthService;
import carevn.luv2code.cms.tevc_cms_api.security.JwtService;
import carevn.luv2code.cms.tevc_cms_api.security.TokenBlacklist;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final TokenBlacklist tokenBlacklist;

    @GetMapping("/user/profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String getUserProfile() {
        return "User Profile Access";
    }

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminDashboard() {
        return "Admin Dashboard";
    }

    @GetMapping("/moderator/content")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String getModeratorContent() {
        return "Moderator Content";
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        return userRepository.findByEmail(request.getEmail())
                .filter(User::isEnabled)
                .map(user -> {
                    String token = jwtService.generateToken(user);
                    return ResponseEntity.ok(new AuthenticationResponse(token));
                })
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        String token = authService.register(request);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LogoutResponse> logout(@RequestBody LogoutRequest request) {
        tokenBlacklist.addToBlacklist(request.getToken());

        return ResponseEntity.ok(new LogoutResponse("Đăng xuất thành công"));
    }
}

