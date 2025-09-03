package carevn.luv2code.cms.tevc_cms_api.service;

import carevn.luv2code.cms.tevc_cms_api.dto.requests.LoginRequest;
import carevn.luv2code.cms.tevc_cms_api.entity.model.Token;

/**
 * Service interface named {@link LoginService} for handling user login operations.
 */
public interface LoginService {

    /**
     * Performs user login based on the provided login request.
     *
     * @param loginRequest The login request containing user credentials.
     * @return The token representing the user's session.
     */
    Token login(final LoginRequest loginRequest);
}
