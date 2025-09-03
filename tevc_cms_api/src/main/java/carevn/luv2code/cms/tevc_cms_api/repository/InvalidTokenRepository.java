package carevn.luv2code.cms.tevc_cms_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import carevn.luv2code.cms.tevc_cms_api.entity.InvalidatedToken;

/**
 * Repository interface named {@link InvalidTokenRepository} for managing {@link InvalidatedToken} entities.
 */
public interface InvalidTokenRepository extends JpaRepository<InvalidatedToken, String> {

    /**
     * Finds an invalid token entity by its tokenId.
     *
     * @param tokenId The tokenId of the invalid token to find.
     * @return An {@link Optional} containing the found {@link InvalidatedToken}, or empty if not found.
     */
    Optional<InvalidatedToken> findByTokenId(final String tokenId);
}
