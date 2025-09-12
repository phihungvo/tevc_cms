package carevn.luv2code.cms.tevc_cms_api.service;

import carevn.luv2code.cms.tevc_cms_api.dto.UserDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.AssignRoleRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.CreateUserRequest;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.UpdateUserRequest;

public interface UserService {

    //    User findByUsername(String username);

    //    void save(UserDTO userDTO);

    UserDTO createUser(CreateUserRequest request);

    UserDTO assignRoles(AssignRoleRequest request);

    UserDTO updateUser(Integer id, UpdateUserRequest request);

    //    void updateUser(UUID userId, UserUpdateRequest request);

    //    Page<UserDTO> findAll(int page, int size);

    //    void deleteUser(UUID userId);

    //    void assignPermissions(UUID userId, List<String> permissionNames);

    //    void removePermission(UUID userId, String resource, String action);

    //    List<String> getUserPermissions(UUID userId);

    //    List<Permission> getAllPermissions();
}
