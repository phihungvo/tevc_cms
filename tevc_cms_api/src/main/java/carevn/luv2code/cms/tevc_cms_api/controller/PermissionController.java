package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.PermissionDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.response.ApiResponse;
import carevn.luv2code.cms.tevc_cms_api.service.PermissionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    //    @PostMapping
    //    //    @PreAuthorize("hasAuthority('PERMISSION:CREATE')")
    //    public ResponseEntity<ApiResponse<PermissionDTO>> createPermission(@RequestBody PermissionDTO permissionDTO) {
    //        return ResponseEntity.ok(ApiResponse.<PermissionDTO>builder()
    //                .code(200)
    //                .message("Permission created successfully")
    //                .result(permissionService.createPermission(permissionDTO))
    //                .build());
    //    }

    //    @PutMapping("/{id}")
    //    @PreAuthorize("hasAuthority('PERMISSION:UPDATE')")
    //    public ResponseEntity<ApiResponse<PermissionDTO>> updatePermission(
    //            @PathVariable UUID id, @RequestBody PermissionDTO permissionDTO) {
    //        return ResponseEntity.ok(ApiResponse.<PermissionDTO>builder()
    //                .code(200)
    //                .message("Permission updated successfully")
    //                .result(permissionService.updatePermission(id, permissionDTO))
    //                .build());
    //    }

    //    @GetMapping("/{id}")
    //    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    //    public ResponseEntity<ApiResponse<PermissionDTO>> getPermission(@PathVariable UUID id) {
    //        return ResponseEntity.ok(ApiResponse.<PermissionDTO>builder()
    //                .code(200)
    //                .result(permissionService.getPermission(id))
    //                .build());
    //    }

    //    @GetMapping
    //    //    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    //    public ResponseEntity<ApiResponse<Page<PermissionDTO>>> getAllPermissions(
    //            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    //        return ResponseEntity.ok(ApiResponse.<Page<PermissionDTO>>builder()
    //                .code(200)
    //                .result(permissionService.getAllPermissions(page, size))
    //                .build());
    //    }

    @GetMapping("/noPaging")
    //    @PreAuthorize("hasAuthority('PERMISSION:READ')")
    public ResponseEntity<ApiResponse<List<PermissionDTO>>> getAllPermissionsNoPaging() {
        return ResponseEntity.ok(ApiResponse.<java.util.List<PermissionDTO>>builder()
                .code(200)
                .result(permissionService.getAllPermissionsNoPaging())
                .build());
    }

    //    @DeleteMapping("/{id}")
    //    @PreAuthorize("hasAuthority('PERMISSION:DELETE')")
    //    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable UUID id) {
    //        permissionService.deletePermission(id);
    //        return ResponseEntity.ok(ApiResponse.<Void>builder()
    //                .code(200)
    //                .message("Permission deleted successfully")
    //                .build());
    //    }
}
