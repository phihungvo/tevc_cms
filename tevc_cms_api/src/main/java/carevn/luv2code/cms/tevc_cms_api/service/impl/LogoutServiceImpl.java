package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.Set;

import org.springframework.stereotype.Service;

import carevn.luv2code.cms.tevc_cms_api.dto.requests.TokenInvalidateRequest;
import carevn.luv2code.cms.tevc_cms_api.service.InvalidTokenService;
import carevn.luv2code.cms.tevc_cms_api.service.LogoutService;
import carevn.luv2code.cms.tevc_cms_api.service.TokenService;
import lombok.RequiredArgsConstructor;

/**
 * Service implementation named {@link LogoutServiceImpl} for handling user logout operations.
 */
@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final TokenService tokenService;
    private final InvalidTokenService invalidTokenService;

    /**
     * Logs out a user session based on the provided token invalidation request.
     *
     * @param tokenInvalidateRequest The request containing tokens to invalidate for logout.
     */
    @Override
    public void logout(TokenInvalidateRequest tokenInvalidateRequest) {

        tokenService.verifyAndValidate(
                Set.of(tokenInvalidateRequest.getAccessToken(), tokenInvalidateRequest.getRefreshToken()));

        final String accessTokenId =
                tokenService.getPayload(tokenInvalidateRequest.getAccessToken()).getId();

        invalidTokenService.checkForInvalidityOfToken(accessTokenId);

        final String refreshTokenId = tokenService
                .getPayload(tokenInvalidateRequest.getRefreshToken())
                .getId();

        invalidTokenService.checkForInvalidityOfToken(refreshTokenId);

        invalidTokenService.invalidateTokens(Set.of(accessTokenId, refreshTokenId));
    }
}
