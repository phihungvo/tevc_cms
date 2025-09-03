package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.PermissionDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.PermissionCreateRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.response.ApiResponse;
import carevn.luv2code.cms.tevc_cms_api.dto.response.CustomResponse;
import carevn.luv2code.cms.tevc_cms_api.entity.Permission;
import carevn.luv2code.cms.tevc_cms_api.repository.PermissionRepository;
import carevn.luv2code.cms.tevc_cms_api.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;
    private final PermissionRepository permissionRepository;

    @PostMapping
    @PreAuthorize("@securityService.hasPermission('PERMISSION', 'CREATE')")
    public CustomResponse<UUID> createPermission(@RequestBody @Valid PermissionCreateRequest request) {
        Permission permission = Permission.builder()
                .name(request.getResource() + ":" + request.getAction())
                .build();
        permission = permissionRepository.save(permission);
        return CustomResponse.successOf(permission.getId());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION:UPDATE')")
    public ResponseEntity<ApiResponse<PermissionDTO>> updatePermission(
            @PathVariable UUID id, @RequestBody PermissionDTO permissionDTO) {
        return ResponseEntity.ok(ApiResponse.<PermissionDTO>builder()
                .code(200)
                .message("Permission updated successfully")
                .result(permissionService.updatePermission(id, permissionDTO))
                .build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    public ResponseEntity<ApiResponse<PermissionDTO>> getPermission(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.<PermissionDTO>builder()
                .code(200)
                .result(permissionService.getPermission(id))
                .build());
    }

    @GetMapping
    //    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    public ResponseEntity<ApiResponse<Page<PermissionDTO>>> getAllPermissions(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.<Page<PermissionDTO>>builder()
                .code(200)
                .result(permissionService.getAllPermissions(page, size))
                .build());
    }

    @GetMapping("/noPaging")
    //    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    public ResponseEntity<ApiResponse<java.util.List<PermissionDTO>>> getAllPermissionsNoPaging() {
        return ResponseEntity.ok(ApiResponse.<java.util.List<PermissionDTO>>builder()
                .code(200)
                .result(permissionService.getAllPermissionsNoPaging())
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISSION:DELETE')")
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable UUID id) {
        permissionService.deletePermission(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(200)
                .message("Permission deleted successfully")
                .build());
    }
}
