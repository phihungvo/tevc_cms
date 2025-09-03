package carevn.luv2code.cms.tevc_cms_api.service;

import carevn.luv2code.cms.tevc_cms_api.dto.requests.RegisterRequest;
import carevn.luv2code.cms.tevc_cms_api.entity.User;

/**
 * Service interface named {@link RegisterService} for user registration operations.
 */
public interface RegisterService {

    /**
     * Registers a new user based on the provided registration request.
     *
     * @param registerRequest The registration request containing user details.
     * @return The registered user entity.
     */
    User registerUser(final RegisterRequest registerRequest);
}
