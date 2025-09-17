package carevn.luv2code.cms.tevc_cms_api.security;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import carevn.luv2code.cms.tevc_cms_api.entity.InvalidatedToken;
import carevn.luv2code.cms.tevc_cms_api.repository.InvalidatedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklist {

    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final JwtService jwtService;

    @Transactional
    public void addToBlacklist(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        try {
            if (invalidatedTokenRepository.findByToken(token).isPresent()) {
                return;
            }

            Date expiryDate = jwtService.extractClaim(token, Claims::getExpiration);
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .token(token)
                    .expiryTime(expiryDate)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return invalidatedTokenRepository.findByToken(token).isPresent();
    }

    //    @Scheduled(cron = "0 0 0 * * ?")
    //    @Transactional
    //    public void cleanupExpiredTokens() {
    //        log.info("Bắt đầu chạy job cleanupExpiredTokens tại thời điểm: {}", new Date());
    //        try {
    //            invalidatedTokenRepository.deleteByExpiryTimeBefore(new Date());
    //            log.info("Hoàn thành job cleanupExpiredTokens - Đã xóa {} token hết hạn");
    //        } catch (Exception e) {
    //            log.error("Lỗi khi chạy job cleanupExpiredTokens: {}", e.getMessage(), e);
    //        }
    //    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("🔄 Bắt đầu job cleanupExpiredTokens tại: {}", new Date());

        try {
            int deletedCount = invalidatedTokenRepository.deleteByExpiryTimeBefore(new Date());
            log.info("✅ Đã xoá {} token hết hạn", deletedCount);
        } catch (Exception e) {
            log.error("❌ Lỗi khi xoá token: {}", e.getMessage(), e);
        }
    }
}
