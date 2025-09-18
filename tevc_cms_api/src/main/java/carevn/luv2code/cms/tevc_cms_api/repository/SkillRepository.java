package carevn.luv2code.cms.tevc_cms_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import carevn.luv2code.cms.tevc_cms_api.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
    Page<Skill> findAll(Pageable pageable);

    Page<Skill> findByEmployeesId(Integer employeeId, Pageable pageable);
}
