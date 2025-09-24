package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.dto.UserDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.AssignRoleRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.CreateUserRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.UserUpdateRequest;
import carevn.luv2code.cms.tevc_cms_api.entity.Role;
import carevn.luv2code.cms.tevc_cms_api.entity.User;
import carevn.luv2code.cms.tevc_cms_api.mapper.UserMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.PermissionRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.RoleRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.UserRepository;
import carevn.luv2code.cms.tevc_cms_api.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepository userRepository;
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
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    //    @Override
    //    public User findByUsername(String username) {
    //        return userRepository.findByUserName(username).orElseThrow(() -> new
    // AppException(ErrorCode.USER_NOT_FOUND));
    //    }

    //    @Override
    //    public void save(UserDTO userDTO) {
    //        if (userRepository.existsByEmail(userDTO.getEmail())) {
    //            throw new AppException(ErrorCode.EMAIL_EXISTED);
    //        }
    //        User user = userMapper.toEntity(userDTO);
    //
    //        user.setPassword(passwordEncoder.encode(user.getPassword()));
    //        user.setCreateAt(new Date());
    //        //        user.setRoles(convertToRoleSet(userDTO.getRoles()));
    //        //        user.setPermissions(convertToPermissionSet(userDTO.getPermissions()));
    //
    //        userRepository.save(user);
    //    }

    @CacheEvict(value = "userPermissions", key = "#request.userName")
    public UserDTO createUser(CreateUserRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new RuntimeException("Username already exists");
        }

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (Integer roleId : request.getRoleIds()) {
                Role role = roleRepository
                        .findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
                roles.add(role);
            }
            user.setRoles(roles);
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @CacheEvict(value = "userPermissions", key = "#request.userId")
    public UserDTO assignRoles(AssignRoleRequest request) {
        User user =
                userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        Set<Role> roles = new HashSet<>();
        for (Integer roleId : request.getRoleIds()) {
            Role role = roleRepository
                    .findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
            roles.add(role);
        }

        user.setRoles(roles);
        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    @CacheEvict(value = "userPermissions", allEntries = true)
    public UserDTO updateUser(Integer id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUserFromDto(request, user);

        // update roles nếu có
        if (request.getRoleIds() != null) {
            Set<Role> roles = request.getRoleIds().stream()
                    .map(roleId -> roleRepository
                            .findById(roleId)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleId)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    //    @Override
    //    public void updateUser(UUID userId, UserUpdateRequest request) {
    //        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    //
    //        userMapper.updateUserFromDto(request, user);
    //        user.setUpdateAt(new Date());
    //
    //        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
    //            user.setPassword(passwordEncoder.encode(request.getPassword()));
    //        }
    //
    //        user.setRoles(convertToRoleSet(request.getRoles()));
    //        user.setPermissions(convertToPermissionSet(request.getPermissions()));
    //        user.setEnabled(request.isEnabled());
    //
    //        userRepository.save(user);
    //    }

    @Override
    public Page<UserDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).map(this::convertToDTO);
    }

    //    @Override
    //    public void deleteUser(UUID userId) {
    //        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    //        userRepository.delete(user);
    //    }

    //    @Override
    //    @Transactional
    //    public void assignPermissions(UUID userId, List<String> permissionNames) {
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
    //    }

    //    @Override
    //    @Transactional
    //    public void removePermission(UUID userId, String resource, String action) {
    //        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    //
    //        // Khóa người dùng bằng Pessimistic Locking
    //        entityManager.lock(user, LockModeType.PESSIMISTIC_WRITE);
    //
    //        //        Permission permission = permissionRepository.findByResourceAndAction(resource, action)
    //        //                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
    //        //        user.getPermissions().remove(permission);
    //        userRepository.save(user);
    //        log.info("Đã xóa quyền {}:{} khỏi người dùng ID: {}", resource, action, userId);
    //    }

    //    @Override
    //    public List<String> getUserPermissions(UUID userId) {
    //        User user = userRepository.findById(userId)
    //                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    //
    //        // Get direct permissions
    ////        Set<Permission> allPermissions = new HashSet<>(user.getPermissions());
    //
    //        // Add permissions from roles

    /// /        user.getRoles().forEach(role -> {
    /// /            allPermissions.addAll(role.getPermissions());
    /// /        });
    /// /
    /// /        return allPermissions.stream()
    /// /                .map(p -> p.getResource() + ":" + p.getAction())
    /// /                .collect(Collectors.toList());
    //    }

    //    @Override
    //    public List<Permission> getAllPermissions() {
    //        return permissionRepository.findAll();
    //    }
    //
    //    private Set<Role> convertToRoleSet(List<UUID> roleIds) {
    //        if (roleIds == null) return Collections.emptySet();
    //
    //        return roleIds.stream()
    //                .map(id -> roleRepository.findById(id).orElseThrow(() -> new
    // AppException(ErrorCode.ROLE_NOT_FOUND)))
    //                .collect(Collectors.toSet());
    //    }

    //    private Set<Permission> convertToPermissionSet(List<UUID> permissionIds) {
    //        if (permissionIds == null) return Collections.emptySet();
    //
    //        return permissionIds.stream()
    //                .map(id -> permissionRepository
    //                        .findById(id)
    //                        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND)))
    //                .collect(Collectors.toSet());
    //    }
    //
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUserName(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setEnabled(user.isEnabled());
        dto.setProfilePicture(user.getProfilePicture());

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            Set<Integer> roleIds = user.getRoles().stream().map(Role::getId).collect(Collectors.toSet());

            Set<String> roleNames = user.getRoles().stream()
                    .map(Role::getName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            dto.setRoleIds(roleIds);
            dto.setRoleNames(roleNames);
        }

        return dto;
    }
}
