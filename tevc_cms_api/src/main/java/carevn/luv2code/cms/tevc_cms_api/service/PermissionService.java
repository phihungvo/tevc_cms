package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.PermissionDTO;

public interface PermissionService {

    PermissionDTO createPermission(PermissionDTO permissionDTO);

    PermissionDTO updatePermission(UUID id, PermissionDTO permissionDTO);

    void deletePermission(UUID id);

    PermissionDTO getPermission(UUID id);

    Page<PermissionDTO> getAllPermissions(int page, int size);

    List<PermissionDTO> getAllPermissionsNoPaging();
}
