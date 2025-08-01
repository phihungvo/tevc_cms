package carevn.luv2code.cms.tevc_cms_api.service;

import carevn.luv2code.cms.tevc_cms_api.dto.UserDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.UserUpdateRequest;
import carevn.luv2code.cms.tevc_cms_api.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {

    User findByUsername(String username);

    void save(UserDTO userDTO);

    void updateUser(UUID userId, UserUpdateRequest request);

    Page<UserDTO> findAll(int page, int size);

    void deleteUser(UUID userId);

}

