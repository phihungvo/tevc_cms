package carevn.luv2code.cms.tevc_cms_api.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.UserDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.AssignRoleRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.CreateUserRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.UserUpdateRequest;
import carevn.luv2code.cms.tevc_cms_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //    @GetMapping("/username/{username}")
    //    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
    //        UserDTO user = userService.getUserByUsername(username);
    //        return ResponseEntity.ok(user);
    //    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest request) {
        UserDTO createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/assign-roles")
    public ResponseEntity<UserDTO> assignRoles(@Valid @RequestBody AssignRoleRequest request) {
        UserDTO user = userService.assignRoles(request);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UserUpdateRequest request) {
        UserDTO updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    //    public UserController(UserService userService) {
    //        this.userService = userService;
    //    }

    //    @GetMapping("/getByUsername")
    //    @PreAuthorize("hasAuthority('USER:READ')")
    //    public ApiResponse<User> getByUsername(@RequestParam String username) {
    //        return ApiResponse.<User>builder()
    //                .code(200)
    //                .result(userService.findByUsername(username))
    //                .build();
    //    }
    //
    @GetMapping("/getAll")
    public ResponseEntity<Page<UserDTO>> findAll(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<UserDTO> users = userService.findAll(page, size);
        return ResponseEntity.ok(users);
    }
    //
    //    @PostMapping("/createUser")
    //    //    @PreAuthorize("hasAnyAuthority('ADMIN:MANAGE', 'USER:READ')")
    //    public ResponseEntity<ApiResponse<String>> createUser(@RequestBody UserDTO userDTO) {
    //        userService.save(userDTO);
    //        return ResponseEntity.ok(ApiResponse.<String>builder()
    //                .code(200)
    //                .result("Tạo người dùng thành công")
    //                .build());
    //    }
    //
    //    @PatchMapping("/{userId}/update")
    //    //    @PreAuthorize("hasAnyAuthority('ADMIN:MANAGE', 'USER:READ')")
    //    public ResponseEntity<ApiResponse<String>> updateUser(
    //            @PathVariable UUID userId, @RequestBody UserUpdateRequest request) {
    //        userService.updateUser(userId, request);
    //        return ResponseEntity.ok(ApiResponse.<String>builder()
    //                .code(200)
    //                .result("Cập nhật người dùng thành công")
    //                .build());
    //    }
    //
    //    @DeleteMapping()
    //    //    @PreAuthorize("hasAuthority('USER:DELETE')")
    //    public ResponseEntity<ApiResponse<String>> deleteUser(@RequestBody List<UUID> userIds) {
    //        userIds.forEach(userService::deleteUser);
    //
    //        return ResponseEntity.ok(ApiResponse.<String>builder()
    //                .code(200)
    //                .result("Xóa người dùng thành công")
    //                .build());
    //    }
    //
    //    //    @PreAuthorize("hasAuthority('USER:MANAGE')")
    //    @PostMapping("/assignPermissions")
    //    public ResponseEntity<ApiResponse<String>> assignPermissions(
    //            @RequestParam UUID userId, @RequestBody List<String> permissionNames) {
    //        userService.assignPermissions(userId, permissionNames);
    //        return ResponseEntity.ok(ApiResponse.<String>builder()
    //                .code(200)
    //                .result("Gán quyền thành công")
    //                .build());
    //    }
    //
    //    @DeleteMapping("/removePermission")
    //    @PreAuthorize("hasAuthority('USER:MANAGE')")
    //    public ResponseEntity<ApiResponse<String>> removePermission(
    //            @RequestParam UUID userId, @RequestParam String resource, @RequestParam String action) {
    //        userService.removePermission(userId, resource, action);
    //        return ResponseEntity.ok(ApiResponse.<String>builder()
    //                .code(200)
    //                .result("Xóa quyền thành công")
    //                .build());
    //    }
    //
    //    //    @PreAuthorize("hasAuthority('USER:READ')")
    //    // @GetMapping("/{userId}/permissions")
    //    // public ResponseEntity<ApiResponse<List<String>>> getUserPermissions(@PathVariable UUID userId) {
    //    //        List<String> permissions = userService.getUserPermissions(userId);
    //    //        return ResponseEntity.ok(ApiResponse.<List<String>>builder()
    //    //                .code(200)
    //    //                .result(permissions)
    //    //                .build());
    //    //    }
    //
    //    //    @PreAuthorize("hasAuthority('ADMIN:MANAGE')")
    //    @GetMapping("/permissions")
    //    public ResponseEntity<ApiResponse<List<Permission>>> getAllPermissions() {
    //        List<Permission> permissions = userService.getAllPermissions();
    //        return ResponseEntity.ok(ApiResponse.<List<Permission>>builder()
    //                .code(200)
    //                .result(permissions)
    //                .build());
    //    }
}
