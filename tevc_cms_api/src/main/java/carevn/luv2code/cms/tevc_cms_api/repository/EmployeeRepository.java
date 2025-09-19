package carevn.luv2code.cms.tevc_cms_api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.enums.PositionType;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    interface EmployeeBasicProjection {
        Integer getId();

        String getFirstName();

        String getLastName();
    }

    boolean existsByEmail(String email);

    Employee findByEmail(String email);

    boolean existsByEmployeeCode(String employeeCode);

    @Query(value = "SELECT employee_id FROM employee_skills WHERE skill_id = :skillId", nativeQuery = true)
    List<Integer> findEmployeeIdsBySkillId(@Param("skillId") Integer skillId);

    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId")
    Page<Employee> findByDepartmentId(@Param("departmentId") Integer departmentId, Pageable pageable);

    //    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId")
    //    List<EmployeeDTO> findByDepartmentId(@Param("departmentId") Integer departmentId);

    @Query("SELECT e FROM Employee e WHERE e.position.id = :positionId")
    List<Employee> findByPositionId(@Param("positionId") Integer positionId);

    @Query("SELECT e FROM Employee e JOIN e.position p WHERE p.positionType = :type")
    List<Employee> findByPositionType(@Param("type") PositionType type);

    @Query("SELECT e FROM Employee e WHERE " + "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
            + "LOWER(e.employeeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Employee> searchEmployees(@Param("keyword") String keyword, Pageable pageable);

    long countByIsActive(boolean isActive);

    @Query("SELECT MAX(e.employeeCode) FROM Employee e")
    String findMaxEmployeeCode();

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.department LEFT JOIN FETCH e.position")
    Page<Employee> findAllWithDepartmentAndPosition(Pageable pageable);

    //    @Query("SELECT e.id AS id, e.firstName AS firstName, e.lastName AS lastName "
    //            + "FROM Employee e WHERE e.department.id = :departmentId")
    //    List<EmployeeBasicProjection> findBasicByDepartmentId(@Param("departmentId") Integer departmentId);

    List<EmployeeBasicProjection> findByDepartmentId(Integer departmentId);
}
