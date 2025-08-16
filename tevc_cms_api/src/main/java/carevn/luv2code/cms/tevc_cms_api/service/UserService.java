package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.UserDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.UserUpdateRequest;
import carevn.luv2code.cms.tevc_cms_api.entity.Permission;
import carevn.luv2code.cms.tevc_cms_api.entity.User;

public interface UserService {

    User findByUsername(String username);

    void save(UserDTO userDTO);

    void updateUser(UUID userId, UserUpdateRequest request);

    Page<UserDTO> findAll(int page, int size);

    void deleteUser(UUID userId);

    void assignPermissions(UUID userId, List<String> permissionNames);

    void removePermission(UUID userId, String resource, String action);

    //    List<String> getUserPermissions(UUID userId);

    List<Permission> getAllPermissions();
}
