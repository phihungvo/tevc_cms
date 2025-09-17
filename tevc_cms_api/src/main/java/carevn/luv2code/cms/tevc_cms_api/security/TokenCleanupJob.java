package carevn.luv2code.cms.tevc_cms_api.security;

import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.entity.InvalidatedToken;
import carevn.luv2code.cms.tevc_cms_api.repository.InvalidatedTokenRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenCleanupJob {

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    public TokenCleanupJob(InvalidatedTokenRepository invalidatedTokenRepository) {
        this.invalidatedTokenRepository = invalidatedTokenRepository;
    }

    @Scheduled(fixedRate = 60000) // chạy mỗi 1 phút
    @Transactional
    public void cleanupExpiredTokens() {
        Date now = new Date();
        log.info("==============================");
        log.info("🔄 Bắt đầu job cleanupExpiredTokens tại: {}", now);

        // Lấy danh sách token hết hạn
        List<InvalidatedToken> expired = invalidatedTokenRepository.findByExpiryTimeBefore(now);
        log.info("📌 Có {} token hết hạn trong DB", expired.size());

        if (!expired.isEmpty()) {
            expired.forEach(t -> log.info("➡ Xoá token id={} expiry={}", t.getId(), t.getExpiryTime()));
            invalidatedTokenRepository.deleteAll(expired);
            log.info("✅ Đã xoá {} token hết hạn", expired.size());

            log.info("------------------------------");

            int deletedCount = invalidatedTokenRepository.deleteByExpiryTimeBefore(now);
            log.info("✅ Đã xoá {} token ", deletedCount);
        } else {
            log.info("⚡ Không có token nào cần xoá");
        }
    }
}
