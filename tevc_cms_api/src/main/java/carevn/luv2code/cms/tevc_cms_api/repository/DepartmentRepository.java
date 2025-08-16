package carevn.luv2code.cms.tevc_cms_api.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    boolean existsByName(String name);

    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.manager")
    Page<Department> findAllWithManager(Pageable pageable);
}
