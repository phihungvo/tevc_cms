package carevn.luv2code.cms.tevc_cms_api.service.impl;

import carevn.luv2code.cms.tevc_cms_api.dto.PermissionDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Permission;
import carevn.luv2code.cms.tevc_cms_api.entity.Role;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.repository.PermissionRepository;
import carevn.luv2code.cms.tevc_cms_api.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        if (permissionRepository.findByResourceAndAction(
                permissionDTO.getResource(), permissionDTO.getAction()).isPresent()) {
            throw new AppException(ErrorCode.PERMISSION_ALREADY_EXISTS);
        }

        Permission permission = new Permission();
        permission.setResource(permissionDTO.getResource());
        permission.setAction(permissionDTO.getAction());

        Permission savedPermission = permissionRepository.save(permission);
        return convertToDTO(savedPermission);
    }

    @Override
    @Transactional  
    public PermissionDTO updatePermission(UUID id, PermissionDTO permissionDTO) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        // Check if new resource:action combination exists
        if ((!permission.getResource().equals(permissionDTO.getResource()) ||
             !permission.getAction().equals(permissionDTO.getAction())) && 
            permissionRepository.findByResourceAndAction(
                permissionDTO.getResource(), 
                permissionDTO.getAction()).isPresent()) {
            throw new AppException(ErrorCode.PERMISSION_ALREADY_EXISTS);
        }

        permission.setResource(permissionDTO.getResource());
        permission.setAction(permissionDTO.getAction());

        Permission updatedPermission = permissionRepository.save(permission);
        return convertToDTO(updatedPermission);
    }

    @Override
    @Transactional
    public void deletePermission(UUID id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        // Remove permission from all roles first
        permission.getRoles().forEach(role -> {
            role.getPermissions().remove(permission);
        });

        // Remove permission from all users that have it directly
        permission.getUsers().forEach(user -> {
            user.getPermissions().remove(permission);
        });
        
        permissionRepository.delete(permission);
    }

    @Override
    public PermissionDTO getPermission(UUID id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        return convertToDTO(permission);
    }

    @Override
    public Page<PermissionDTO> getAllPermissions(int page, int size) {
        return permissionRepository.findAll(PageRequest.of(page, size))
                .map(this::convertToDTO);
    }

    @Override
    public List<PermissionDTO> getAllPermissionsNoPaging() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PermissionDTO convertToDTO(Permission permission) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(permission.getId());
        dto.setResource(permission.getResource());
        dto.setAction(permission.getAction());
        dto.setDescription(permission.getDescription());
        
        return dto;
    }
}
