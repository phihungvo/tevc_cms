package carevn.luv2code.cms.tevc_cms_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.InvalidatedToken;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, Integer> {

    //    Optional<InvalidatedToken> findByToken(String token);

    //    void deleteByExpiryTimeBefore(Date date);
}
