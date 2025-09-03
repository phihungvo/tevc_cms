package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.dto.RoleDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Permission;
import carevn.luv2code.cms.tevc_cms_api.entity.Role;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.repository.PermissionRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.RoleRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.UserRepository;
import carevn.luv2code.cms.tevc_cms_api.service.RoleService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        if (roleRepository.existsByName(roleDTO.getName())) {
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
        }

        Role role = new Role();
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());

        //        if (roleDTO.getPermissions() != null) {
        //            role.setPermissions(convertToPermissionSet(roleDTO.getPermissions()));
        //        }

        Role savedRole = roleRepository.save(role);
        return convertToDTO(savedRole);
    }

    @Override
    public RoleDTO updateRole(Integer id, RoleDTO roleDTO) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        // Check if new name already exists for different role
        if (!role.getName().equals(roleDTO.getName()) && roleRepository.existsByName(roleDTO.getName())) {
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
        }

        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        //        if (roleDTO.getPermissions() != null) {
        //            role.setPermissions(convertToPermissionSet(roleDTO.getPermissions()));
        //        }

        Role updatedRole = roleRepository.save(role);
        return convertToDTO(updatedRole);
    }

    @Override
    public void deleteRole(Integer id) {
        //        Role role = roleRepository.findById(id)
        //                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        //
        //        // Gỡ role khỏi tất cả user
        //        for (User user : role.getUsers()) {
        //            user.getRoles().remove(role);
        //        }
        //
        //        // Gỡ permission khỏi role
        //        for (Permission permission : role.getPermissions()) {
        //            permission.getRoles().remove(role);
        //        }
        //
        //        // Xóa toàn bộ liên kết trong tập của role
        //        role.getUsers().clear();
        //        role.getPermissions().clear();
        //
        //        // Lưu thay đổi để xóa dữ liệu ở bảng trung gian trước khi xóa role
        //        roleRepository.save(role);
        //
        //        // Cuối cùng xóa role
        //        roleRepository.delete(role);
        roleRepository.deleteById(id);
    }

    @Override
    public RoleDTO getRole(Integer id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        return convertToDTO(role);
    }

    @Override
    public Page<RoleDTO> getAllRoles(int page, int size) {
        return roleRepository.findAll(PageRequest.of(page, size)).map(this::convertToDTO);
    }

    @Override
    public List<RoleDTO> getAllRolesNoPaging() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void assignPermissionsToRole(Integer roleId, List<Integer> permissionIds) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        Set<Permission> permissions = permissionIds.stream()
                .map(id -> permissionRepository
                        .findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND)))
                .collect(Collectors.toSet());

        role.getPermissions().addAll(permissions);
        roleRepository.save(role);
    }

    @Override
    public void removePermissionFromRole(Integer roleId, Integer permissionId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        Permission permission = permissionRepository
                .findById(permissionId)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        role.getPermissions().remove(permission);
        roleRepository.save(role);
    }

    private Set<Permission> convertToPermissionSet(Set<Integer> permissionIds) {
        if (permissionIds == null) return Collections.emptySet();

        return permissionIds.stream()
                .map(id -> permissionRepository
                        .findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND)))
                .collect(Collectors.toSet());
    }

    private RoleDTO convertToDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());

        if (role.getPermissions() != null) {
            Set<Integer> permissions =
                    role.getPermissions().stream().map(Permission::getId).collect(Collectors.toSet());
            dto.setPermissions(permissions);
        }

        return dto;
    }
}
