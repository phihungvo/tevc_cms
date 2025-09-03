package carevn.luv2code.cms.tevc_cms_api.security;

import org.springframework.stereotype.Component;

import carevn.luv2code.cms.tevc_cms_api.repository.InvalidatedTokenRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenBlacklist {

    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final JwtService jwtService;

    //    public void addToBlacklist(String token) {
    //        Date expiryDate = jwtService.extractClaim(token, Claims::getExpiration);
    //
    //        InvalidatedToken invalidatedToken =
    //                InvalidatedToken.builder().token(token).expiryTime(expiryDate).build();
    //
    //        invalidatedTokenRepository.save(invalidatedToken);
    //    }

    //    public boolean isTokenBlacklisted(String token) {
    //        return invalidatedTokenRepository.findByToken(token).isPresent();
    //    }
    //
    //    @Scheduled(cron = "0 0 0 * * ?")
    //    public void cleanupExpiredTokens() {
    //        invalidatedTokenRepository.deleteByExpiryTimeBefore(new Date());
    //    }
}
