package carevn.luv2code.cms.tevc_cms_api.service;

import carevn.luv2code.cms.tevc_cms_api.dto.requests.TokenRefreshRequest;
import carevn.luv2code.cms.tevc_cms_api.entity.model.Token;

/**
 * Service interface named {@link RefreshTokenService} for refreshing authentication tokens.
 */
public interface RefreshTokenService {

    /**
     * Refreshes an authentication token based on the provided refresh token request.
     *
     * @param tokenRefreshRequest The request containing the refresh token.
     * @return The refreshed authentication token.
     */
    Token refreshToken(final TokenRefreshRequest tokenRefreshRequest);
}
