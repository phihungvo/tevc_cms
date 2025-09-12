package carevn.luv2code.cms.tevc_cms_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Payroll;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Integer> {
    List<Payroll> findByEmployeeId(Integer employeeId);

    List<Payroll> findByPeriod(String period);

    List<Payroll> findByStatus(String status);

    Optional<Payroll> findByEmployeeIdAndPeriod(Integer employeeId, String period);
}
