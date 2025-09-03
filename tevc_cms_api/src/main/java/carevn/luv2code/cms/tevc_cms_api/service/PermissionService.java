package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.PermissionDTO;

public interface PermissionService {

    PermissionDTO createPermission(PermissionDTO permissionDTO);

    PermissionDTO updatePermission(Integer id, PermissionDTO permissionDTO);

    void deletePermission(Integer id);

    PermissionDTO getPermission(Integer id);

    Page<PermissionDTO> getAllPermissions(int page, int size);

    List<PermissionDTO> getAllPermissionsNoPaging();
}
