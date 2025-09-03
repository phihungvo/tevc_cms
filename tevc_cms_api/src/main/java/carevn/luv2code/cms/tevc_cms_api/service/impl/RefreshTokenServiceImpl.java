package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import carevn.luv2code.cms.tevc_cms_api.dto.requests.TokenRefreshRequest;
import carevn.luv2code.cms.tevc_cms_api.entity.User;
import carevn.luv2code.cms.tevc_cms_api.entity.model.Token;
import carevn.luv2code.cms.tevc_cms_api.enums.TokenClaims;
import carevn.luv2code.cms.tevc_cms_api.enums.UserStatus;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.exception.UserStatusNotValidException;
import carevn.luv2code.cms.tevc_cms_api.repository.UserRepository;
import carevn.luv2code.cms.tevc_cms_api.service.RefreshTokenService;
import carevn.luv2code.cms.tevc_cms_api.service.TokenService;
import lombok.RequiredArgsConstructor;

/**
 * Service implementation named {@link RefreshTokenServiceImpl} for refreshing authentication tokens.
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    /**
     * Refreshes an authentication token based on the provided refresh token request.
     *
     * @param tokenRefreshRequest The request containing the refresh token.
     * @return The refreshed authentication token.
     */
    @Override
    public Token refreshToken(TokenRefreshRequest tokenRefreshRequest) {

        tokenService.verifyAndValidate(tokenRefreshRequest.getRefreshToken());

        final String adminId = tokenService
                .getPayload(tokenRefreshRequest.getRefreshToken())
                .get(TokenClaims.USER_ID.getValue())
                .toString();

        final User userEntityFromDB = userRepository
                .findById(UUID.fromString(adminId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        this.validateAdminStatus(userEntityFromDB);

        return tokenService.generateToken(userEntityFromDB.getClaims(), tokenRefreshRequest.getRefreshToken());
    }

    /**
     * Validates the status of an admin user.
     *
     * @param userEntity The user entity to validate.
     * @throws UserStatusNotValidException If the user status is not valid.
     */
    private void validateAdminStatus(final User userEntity) {
        if (!(UserStatus.ACTIVE.equals(userEntity.getUserStatus()))) {
            throw new UserStatusNotValidException("UserStatus = " + userEntity.getUserStatus());
        }
    }
}
