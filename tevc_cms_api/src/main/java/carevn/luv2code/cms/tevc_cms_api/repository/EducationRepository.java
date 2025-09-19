package carevn.luv2code.cms.tevc_cms_api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import carevn.luv2code.cms.tevc_cms_api.entity.Education;

public interface EducationRepository extends JpaRepository<Education, Integer> {
    List<Education> findByEmployeeId(Integer employeeId);

    Page<Education> findByEmployeeId(Integer employeeId, Pageable pageable);
}
