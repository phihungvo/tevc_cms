package carevn.luv2code.cms.tevc_cms_api.repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.PayrollDetail;

@Repository
public interface PayrollDetailRepository extends JpaRepository<PayrollDetail, UUID> {
    List<PayrollDetail> findByPayrollId(UUID payrollId);

    List<PayrollDetail> findByType(String type);

    List<PayrollDetail> findByDateBetween(Date startDate, Date endDate);
}
