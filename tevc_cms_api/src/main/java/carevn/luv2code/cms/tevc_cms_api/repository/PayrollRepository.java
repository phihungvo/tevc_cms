package carevn.luv2code.cms.tevc_cms_api.repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, UUID> {
    List<Payroll> findByEmployeeId(UUID employeeId);

    List<Payroll> findByPeriod(String period);

    List<Payroll> findByStatus(String status);

    Optional<Payroll> findByEmployeeIdAndPeriod(UUID employeeId, String period);
}
