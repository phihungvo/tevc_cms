package carevn.luv2code.cms.tevc_cms_api.service.impl;

import carevn.luv2code.cms.tevc_cms_api.dto.RoleDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Permission;
import carevn.luv2code.cms.tevc_cms_api.entity.Role;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.repository.PermissionRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.RoleRepository;
import carevn.luv2code.cms.tevc_cms_api.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public RoleDTO createRole(RoleDTO roleDTO) {
        if (roleRepository.existsByName(roleDTO.getName())) {
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
        }

        Role role = new Role();
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        
        if (roleDTO.getPermissions() != null) {
            role.setPermissions(convertToPermissionSet(roleDTO.getPermissions()));
        }

        Role savedRole = roleRepository.save(role);
        return convertToDTO(savedRole);
    }

    @Override
    @Transactional
    public RoleDTO updateRole(UUID id, RoleDTO roleDTO) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        // Check if new name already exists for different role
        if (!role.getName().equals(roleDTO.getName()) && 
            roleRepository.existsByName(roleDTO.getName())) {
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
        }

        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        if (roleDTO.getPermissions() != null) {
            role.setPermissions(convertToPermissionSet(roleDTO.getPermissions()));
        }

        Role updatedRole = roleRepository.save(role);
        return convertToDTO(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
                
        // Remove role from all users first
//        role.getUsers().forEach(user -> {
//            user.getRoles().remove(role);
//        });
        
        roleRepository.delete(role);
    }

    @Override
    public RoleDTO getRole(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        return convertToDTO(role);
    }

    @Override
    public Page<RoleDTO> getAllRoles(int page, int size) {
        return roleRepository.findAll(PageRequest.of(page, size))
                .map(this::convertToDTO);
    }

    @Override
    public List<RoleDTO> getAllRolesNoPaging() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void assignPermissionsToRole(UUID roleId, List<UUID> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        
        Set<Permission> permissions = permissionIds.stream()
                .map(id -> permissionRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND)))
                .collect(Collectors.toSet());
        
        role.getPermissions().addAll(permissions);
        roleRepository.save(role);
    }

    @Override
    @Transactional
    public void removePermissionFromRole(UUID roleId, UUID permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
                
        role.getPermissions().remove(permission);
        roleRepository.save(role);
    }

    private Set<Permission> convertToPermissionSet(Set<UUID> permissionIds) {
        if (permissionIds == null) return Collections.emptySet();
        
        return permissionIds.stream()
                .map(id -> permissionRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND)))
                .collect(Collectors.toSet());
    }

    private RoleDTO convertToDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        
        if (role.getPermissions() != null) {
            Set<UUID> permissions = role.getPermissions().stream()
                    .map(Permission::getId)
                    .collect(Collectors.toSet());
            dto.setPermissions(permissions);
        }
        
        return dto;
    }
}
