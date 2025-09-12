package carevn.luv2code.cms.tevc_cms_api.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.PayrollDetail;

@Repository
public interface PayrollDetailRepository extends JpaRepository<PayrollDetail, Integer> {
    List<PayrollDetail> findByPayrollId(Integer payrollId);

    List<PayrollDetail> findByType(String type);

    List<PayrollDetail> findByDateBetween(Date startDate, Date endDate);
}
