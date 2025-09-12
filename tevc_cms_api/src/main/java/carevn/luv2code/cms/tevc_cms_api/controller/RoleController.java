package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.RoleDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.AssignPermissionRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.CreateRoleRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.response.ApiResponse;
import carevn.luv2code.cms.tevc_cms_api.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody CreateRoleRequest request) {
        RoleDTO createdRole = roleService.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @PostMapping("/assign-permissions")
    public ResponseEntity<RoleDTO> assignPermissions(@Valid @RequestBody AssignPermissionRequest request) {
        RoleDTO role = roleService.assignPermissions(request);
        return ResponseEntity.ok(role);
    }

    //        @PostMapping
    //        public ResponseEntity<ApiResponse<RoleDTO>> createRole(@RequestBody CreateRoleRequest request) {
    //            RoleDTO created = roleService.createRole(roleDTO);
    //            return ResponseEntity.ok(ApiResponse.<RoleDTO>builder()
    //                    .code(200)
    //                    .message("Role created successfully")
    //                    .result(created)
    //                    .build());
    //        }
    //
    //    @PatchMapping("/{id}")
    //    //    @PreAuthorize("hasAuthority('ROLE:UPDATE')")
    //    public ResponseEntity<ApiResponse<RoleDTO>> updateRole(@PathVariable UUID id, @RequestBody RoleDTO roleDTO) {
    //        RoleDTO updated = roleService.updateRole(id, roleDTO);
    //        return ResponseEntity.ok(ApiResponse.<RoleDTO>builder()
    //                .code(200)
    //                .message("Role updated successfully")
    //                .result(updated)
    //                .build());
    //    }
    //
    //    @GetMapping("/{id}")
    //    @PreAuthorize("hasAuthority('ROLE:READ')")
    //    public ResponseEntity<ApiResponse<RoleDTO>> getRole(@PathVariable UUID id) {
    //        return ResponseEntity.ok(ApiResponse.<RoleDTO>builder()
    //                .code(200)
    //                .result(roleService.getRole(id))
    //                .build());
    //    }
    //
    @GetMapping
    public ResponseEntity<ApiResponse<Page<RoleDTO>>> getAllRoles(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.<Page<RoleDTO>>builder()
                .code(200)
                .result(roleService.getAllRoles(page, size))
                .build());
    }

    @GetMapping("/noPaging")
    public ResponseEntity<ApiResponse<List<RoleDTO>>> getAllRolesNoPaging() {
        return ResponseEntity.ok(ApiResponse.<List<RoleDTO>>builder()
                .code(200)
                .result(roleService.getAllRolesNoPaging())
                .build());
    }
    //
    //    @PostMapping("/{roleId}/permissions")
    //    @PreAuthorize("hasAuthority('ROLE:UPDATE')")
    //    public ResponseEntity<ApiResponse<Void>> assignPermissionsToRole(
    //            @PathVariable UUID roleId, @RequestBody List<UUID> permissionIds) {
    //        roleService.assignPermissionsToRole(roleId, permissionIds);
    //        return ResponseEntity.ok(ApiResponse.<Void>builder()
    //                .code(200)
    //                .message("Permissions assigned successfully")
    //                .build());
    //    }
    //
    //    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    //    @PreAuthorize("hasAuthority('ROLE:UPDATE')")
    //    public ResponseEntity<ApiResponse<Void>> removePermissionFromRole(
    //            @PathVariable UUID roleId, @PathVariable UUID permissionId) {
    //        roleService.removePermissionFromRole(roleId, permissionId);
    //        return ResponseEntity.ok(ApiResponse.<Void>builder()
    //                .code(200)
    //                .message("Permission removed successfully")
    //                .build());
    //    }
    //
    //    @DeleteMapping("/{id}")
    //    //    @PreAuthorize("hasAuthority('ROLE:DELETE')")
    //    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable UUID id) {
    //        roleService.deleteRole(id);
    //        return ResponseEntity.ok(ApiResponse.<Void>builder()
    //                .code(200)
    //                .message("Role deleted successfully")
    //                .build());
    //    }
}
