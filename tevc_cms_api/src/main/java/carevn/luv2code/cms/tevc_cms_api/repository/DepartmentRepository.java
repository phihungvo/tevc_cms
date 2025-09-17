package carevn.luv2code.cms.tevc_cms_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    boolean existsByName(String name);

    Page<Department> findAll(Pageable pageable);

    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.manager")
    Page<Department> findAllWithManager(Pageable pageable);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.department.id = :deptId")
    int countEmployeesByDepartment(@Param("deptId") Integer deptId);
}
