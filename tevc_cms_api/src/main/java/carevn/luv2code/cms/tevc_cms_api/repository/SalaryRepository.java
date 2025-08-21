package carevn.luv2code.cms.tevc_cms_api.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Salary;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, UUID> {
    Page<Salary> findAll(Pageable pageable);
}
