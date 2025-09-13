package carevn.luv2code.cms.tevc_cms_api.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Performance;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Integer> {
    List<Performance> findByEmployeeId(Integer employeeId);

    List<Performance> findByReviewerId(Integer reviewerId);

    List<Performance> findByReviewPeriod(String period);

    List<Performance> findByReviewDateBetween(Date startDate, Date endDate);
}
