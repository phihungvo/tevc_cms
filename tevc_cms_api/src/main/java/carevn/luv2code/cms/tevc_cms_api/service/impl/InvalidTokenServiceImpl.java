package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import carevn.luv2code.cms.tevc_cms_api.entity.InvalidatedToken;
import carevn.luv2code.cms.tevc_cms_api.exception.TokenAlreadyInvalidatedException;
import carevn.luv2code.cms.tevc_cms_api.repository.InvalidTokenRepository;
import carevn.luv2code.cms.tevc_cms_api.service.InvalidTokenService;
import lombok.RequiredArgsConstructor;

/**
 * Service implementation named {@link InvalidTokenServiceImpl} for managing invalid tokens.
 */
@Service
@RequiredArgsConstructor
public class InvalidTokenServiceImpl implements InvalidTokenService {

    private final InvalidTokenRepository invalidTokenRepository;

    /**
     * Invalidates multiple tokens identified by their IDs.
     *
     * @param tokenIds The set of token IDs to invalidate.
     */
    @Override
    public void invalidateTokens(Set<String> tokenIds) {

        final Set<InvalidatedToken> invalidTokenEntities = tokenIds.stream()
                .map(tokenId -> InvalidatedToken.builder().tokenId(tokenId).build())
                .collect(Collectors.toSet());

        invalidTokenRepository.saveAll(invalidTokenEntities);
    }

    /**
     * Checks if a token identified by its ID is invalid.
     *
     * @param tokenId The ID of the token to check for invalidity.
     * @throws TokenAlreadyInvalidatedException If the token is already invalidated.
     */
    @Override
    public void checkForInvalidityOfToken(String tokenId) {

        final boolean isTokenInvalid =
                invalidTokenRepository.findByTokenId(tokenId).isPresent();

        if (isTokenInvalid) {
            throw new TokenAlreadyInvalidatedException(tokenId);
        }
    }
}
