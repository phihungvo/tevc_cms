package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.dto.UserDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.UserUpdateRequest;
import carevn.luv2code.cms.tevc_cms_api.entity.Permission;
import carevn.luv2code.cms.tevc_cms_api.entity.Role;
import carevn.luv2code.cms.tevc_cms_api.entity.User;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.UserMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.PermissionRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.RoleRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.UserRepository;
import carevn.luv2code.cms.tevc_cms_api.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUserName(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public void save(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        User user = userMapper.toEntity(userDTO);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateAt(new Date());
        user.setRoles(convertToRoleSet(userDTO.getRoles()));
        //        user.setPermissions(convertToPermissionSet(userDTO.getPermissions()));

        userRepository.save(user);
    }

    @Override
    public void updateUser(UUID userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUserFromDto(request, user);
        user.setUpdateAt(new Date());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        user.setRoles(convertToRoleSet(request.getRoles()));
        //        user.setPermissions(convertToPermissionSet(request.getPermissions()));
        user.setEnabled(request.isEnabled());

        userRepository.save(user);
    }

    @Override
    public Page<UserDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void assignPermissions(UUID userId, List<String> permissionNames) {
        //        User user = userRepository.findById(userId)
        //                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        //
        //        // Khóa người dùng bằng Pessimistic Locking
        //        entityManager.lock(user, LockModeType.PESSIMISTIC_WRITE);
        //
        //        Set<Permission> permissions = convertToPermissionSet(permissionNames);
        //        user.getPermissions().addAll(permissions);
        //        userRepository.save(user);
        //        log.info("Đã gán quyền cho người dùng ID: {}", userId);
    }

    @Override
    @Transactional
    public void removePermission(UUID userId, String resource, String action) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Khóa người dùng bằng Pessimistic Locking
        entityManager.lock(user, LockModeType.PESSIMISTIC_WRITE);

        //        Permission permission = permissionRepository.findByResourceAndAction(resource, action)
        //                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        //        user.getPermissions().remove(permission);
        userRepository.save(user);
        log.info("Đã xóa quyền {}:{} khỏi người dùng ID: {}", resource, action, userId);
    }

    //    @Override
    //    public List<String> getUserPermissions(UUID userId) {
    //        User user = userRepository.findById(userId)
    //                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    //
    //        // Get direct permissions
    ////        Set<Permission> allPermissions = new HashSet<>(user.getPermissions());
    //
    //        // Add permissions from roles
    ////        user.getRoles().forEach(role -> {
    ////            allPermissions.addAll(role.getPermissions());
    ////        });
    ////
    ////        return allPermissions.stream()
    ////                .map(p -> p.getResource() + ":" + p.getAction())
    ////                .collect(Collectors.toList());
    //    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    private Set<Role> convertToRoleSet(List<UUID> roleIds) {
        if (roleIds == null) return Collections.emptySet();

        return roleIds.stream()
                .map(id -> roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)))
                .collect(Collectors.toSet());
    }

    private Set<Permission> convertToPermissionSet(List<UUID> permissionIds) {
        if (permissionIds == null) return Collections.emptySet();

        return permissionIds.stream()
                .map(id -> permissionRepository
                        .findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND)))
                .collect(Collectors.toSet());
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        dto.setBio(user.getBio());
        dto.setAddress(user.getAddress());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setEnabled(user.isEnabled());
        dto.setProfilePicture(user.getProfilePicture());

        if (user.getRoles() != null) {
            List<UUID> roleIds = user.getRoles().stream().map(Role::getId).collect(Collectors.toList());
            dto.setRoles(roleIds);

            dto.setRoleNames(user.getRoles().stream()
                    .map(Role::getName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
        }

        //        if (user.getPermissions() != null) {
        //            List<UUID> permissions =
        //                    user.getPermissions().stream().map(Permission::getId).collect(Collectors.toList());
        //            dto.setPermissions(permissions);
        //        }

        return dto;
    }
}
