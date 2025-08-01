package carevn.luv2code.cms.tevc_cms_api.security;

import carevn.luv2code.cms.tevc_cms_api.dto.requests.RegisterRequest;
import carevn.luv2code.cms.tevc_cms_api.entity.Permission;
import carevn.luv2code.cms.tevc_cms_api.entity.Role;
import carevn.luv2code.cms.tevc_cms_api.entity.User;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.repository.PermissionRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.RoleRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        User user = new User();
        user.setUserName(request.getUsername());
        user.setCreateAt(new Date());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Gán vai trò mặc định
        Set<Role> roles = request.getRoles() != null && !request.getRoles().isEmpty()
                ? request.getRoles().stream()
                .map(name -> roleRepository.findByName(String.valueOf(name))
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)))
                .collect(Collectors.toSet())
                : Collections.singleton(roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));
        user.setRoles(roles);

        // Gán quyền mặc định cho USER
        Permission readPermission = permissionRepository.findByResourceAndAction("USER", "READ")
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        user.setPermissions(Collections.singleton(readPermission));

        userRepository.save(user);

        return jwtService.generateToken(user);
    }
}