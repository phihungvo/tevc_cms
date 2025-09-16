package carevn.luv2code.cms.tevc_cms_api.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.InvalidatedToken;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, Integer> {

    List<InvalidatedToken> findByExpiryTimeBefore(Date expiryTime);

    Optional<InvalidatedToken> findByToken(String token);

    int deleteByExpiryTimeBefore(Date date);
}
