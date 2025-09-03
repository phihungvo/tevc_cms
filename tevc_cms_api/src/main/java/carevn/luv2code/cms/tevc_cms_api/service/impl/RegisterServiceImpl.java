package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import carevn.luv2code.cms.tevc_cms_api.dto.requests.RegisterRequest;
import carevn.luv2code.cms.tevc_cms_api.entity.Role;
import carevn.luv2code.cms.tevc_cms_api.entity.User;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.RegisterRequestToUserEntityMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.RoleRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.UserRepository;
import carevn.luv2code.cms.tevc_cms_api.service.RegisterService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RegisterRequestToUserEntityMapper registerRequestToUserEntityMapper =
            RegisterRequestToUserEntityMapper.initialize();
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(RegisterRequest registerRequest) {

        if (userRepository.existsUserEntityByEmail(registerRequest.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        final User userEntityToBeSaved = registerRequestToUserEntityMapper.mapForSaving(registerRequest);
        userEntityToBeSaved.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        List<Integer> roleIds = registerRequest.getRoleIds();
        if (roleIds == null || roleIds.isEmpty()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }

        Set<Role> roles = roleIds.stream()
                .map(roleId ->
                        roleRepository.findById(roleId).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)))
                .collect(Collectors.toSet());

        userEntityToBeSaved.setRoles(roles);
        userEntityToBeSaved.setCreatedAt(LocalDateTime.now());

        return userRepository.save(userEntityToBeSaved);
    }
}
