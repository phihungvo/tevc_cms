package carevn.luv2code.cms.tevc_cms_api.controller;

import carevn.luv2code.cms.tevc_cms_api.dto.RoleDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.response.ApiResponse;
import carevn.luv2code.cms.tevc_cms_api.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
//    @PreAuthorize("hasAuthority('ROLE:CREATE')")
    public ResponseEntity<ApiResponse<RoleDTO>> createRole(@RequestBody RoleDTO roleDTO) {
        RoleDTO created = roleService.createRole(roleDTO);
        return ResponseEntity.ok(ApiResponse.<RoleDTO>builder()
                .code(200)
                .message("Role created successfully")
                .result(created)
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE:UPDATE')")
    public ResponseEntity<ApiResponse<RoleDTO>> updateRole(
            @PathVariable UUID id,
            @RequestBody RoleDTO roleDTO) {
        RoleDTO updated = roleService.updateRole(id, roleDTO);
        return ResponseEntity.ok(ApiResponse.<RoleDTO>builder()
                .code(200)
                .message("Role updated successfully")
                .result(updated)
                .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE:READ')")
    public ResponseEntity<ApiResponse<RoleDTO>> getRole(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.<RoleDTO>builder()
                .code(200)
                .result(roleService.getRole(id))
                .build());
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('ROLE:READ')")
    public ResponseEntity<ApiResponse<Page<RoleDTO>>> getAllRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.<Page<RoleDTO>>builder()
                .code(200)
                .result(roleService.getAllRoles(page, size))
                .build());
    }

    @PostMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('ROLE:UPDATE')")
    public ResponseEntity<ApiResponse<Void>> assignPermissionsToRole(
            @PathVariable UUID roleId,
            @RequestBody List<UUID> permissionIds) {
        roleService.assignPermissionsToRole(roleId, permissionIds);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(200)
                .message("Permissions assigned successfully")
                .build());
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('ROLE:UPDATE')")
    public ResponseEntity<ApiResponse<Void>> removePermissionFromRole(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId) {
        roleService.removePermissionFromRole(roleId, permissionId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(200)
                .message("Permission removed successfully")
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE:DELETE')")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable UUID id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(200)
                .message("Role deleted successfully")
                .build());
    }
}
