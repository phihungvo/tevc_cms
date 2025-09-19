package carevn.luv2code.cms.tevc_cms_api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import carevn.luv2code.cms.tevc_cms_api.entity.WorkHistory;

public interface WorkHistoryRepository extends JpaRepository<WorkHistory, Integer> {
    List<WorkHistory> findByEmployeeId(Integer employeeId);

    Page<WorkHistory> findAll(Pageable pageable);

    Page<WorkHistory> findByEmployeeId(Integer employeeId, Pageable pageable);
}
