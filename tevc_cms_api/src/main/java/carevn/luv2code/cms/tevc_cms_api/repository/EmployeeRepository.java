package carevn.luv2code.cms.tevc_cms_api.repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.enums.PositionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    boolean existsByEmail(String email);

    boolean existsByEmployeeCode(String employeeCode);

    @Query("SELECT e FROM Employee e WHERE e.department.id = ?1")
    List<Employee> findByDepartmentId(UUID departmentId);

    @Query("SELECT e FROM Employee e WHERE e.position.id = ?1")
    List<Employee> findByPositionId(UUID positionId);

//    @Query("SELECT e FROM Employee e WHERE e.position.positionType = :type")
//    List<Employee> findByPositionType(@Param("type") PositionType type);

    @Query("SELECT e FROM Employee e JOIN e.position p WHERE p.positionType = :type")
    List<Employee> findByPositionType(@Param("type") PositionType type);

    @Query("SELECT MAX(e.employeeCode) FROM Employee e")
    String findMaxEmployeeCode();

}
