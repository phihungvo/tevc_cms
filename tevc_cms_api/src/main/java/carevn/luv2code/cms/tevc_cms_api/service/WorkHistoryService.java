package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import carevn.luv2code.cms.tevc_cms_api.dto.WorkHistoryDTO;

public interface WorkHistoryService {
    WorkHistoryDTO createWorkHistory(WorkHistoryDTO workHistoryDTO);

    WorkHistoryDTO updateWorkHistory(Integer id, WorkHistoryDTO workHistoryDTO);

    WorkHistoryDTO getWorkHistoryById(Integer id);

    List<WorkHistoryDTO> getWorkHistoriesByEmployeeId(Integer employeeId);

    void deleteWorkHistory(Integer id);

    Page<WorkHistoryDTO> getAllWorkHistories(Pageable pageable);

    Page<WorkHistoryDTO> getWorkHistoriesByEmployeeIdPaged(Integer employeeId, Pageable pageable);
}
