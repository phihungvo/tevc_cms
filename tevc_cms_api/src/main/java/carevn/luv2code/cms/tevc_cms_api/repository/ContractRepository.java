package carevn.luv2code.cms.tevc_cms_api.repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {
    Optional<Contract> findByEmployeeIdAndStatus(UUID employeeId, String status);

    List<Contract> findByEmployeeId(UUID employeeId);

    List<Contract> findByContractType(String contractType);

    List<Contract> findByEndDateBefore(Date date);
}

