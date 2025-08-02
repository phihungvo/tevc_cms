package carevn.luv2code.cms.tevc_cms_api.repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Contract;
import carevn.luv2code.cms.tevc_cms_api.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, UUID> {
    List<Performance> findByEmployeeId(UUID employeeId);
    List<Performance> findByReviewerId(UUID reviewerId);
    List<Performance> findByReviewPeriod(String period);
    List<Performance> findByReviewDateBetween(Date startDate, Date endDate);
}
