package carevn.luv2code.cms.tevc_cms_api.service;

import carevn.luv2code.cms.tevc_cms_api.dto.PerformanceDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface PerformanceService {

    PerformanceDTO createPerformance(PerformanceDTO performanceDTO);

    PerformanceDTO updatePerformance(UUID id, PerformanceDTO performanceDTO);

    void deletePerformance(UUID id);

    PerformanceDTO getPerformance(UUID id);

    Page<PerformanceDTO> getAllPerformances(int page, int size);

    List<PerformanceDTO> getEmployeePerformances(UUID employeeId);

    List<PerformanceDTO> getReviewerPerformances(UUID reviewerId);
}
