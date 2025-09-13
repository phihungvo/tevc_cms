package carevn.luv2code.cms.tevc_cms_api.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {
    Optional<Contract> findByEmployeeIdAndStatus(Integer employeeId, String status);

    List<Contract> findByEmployeeId(Integer employeeId);

    List<Contract> findByContractType(String contractType);

    List<Contract> findByEndDateBefore(Date date);
}
