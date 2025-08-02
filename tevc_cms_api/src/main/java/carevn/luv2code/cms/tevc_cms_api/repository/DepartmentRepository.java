package carevn.luv2code.cms.tevc_cms_api.repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    boolean existsByName(String name);
}
