package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.PerformanceDTO;

public interface PerformanceService {

    PerformanceDTO createPerformance(PerformanceDTO performanceDTO);

    PerformanceDTO updatePerformance(Integer id, PerformanceDTO performanceDTO);

    void deletePerformance(Integer id);

    PerformanceDTO getPerformance(Integer id);

    Page<PerformanceDTO> getAllPerformances(int page, int size);

    List<PerformanceDTO> getEmployeePerformances(Integer employeeId);

    List<PerformanceDTO> getReviewerPerformances(Integer reviewerId);
}
