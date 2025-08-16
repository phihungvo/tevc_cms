package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.dto.PerformanceDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.entity.Performance;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.PerformanceMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.PerformanceRepository;
import carevn.luv2code.cms.tevc_cms_api.service.PerformanceService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerformanceServiceImpl implements PerformanceService {
    private final PerformanceRepository performanceRepository;
    private final EmployeeRepository employeeRepository;
    private final PerformanceMapper performanceMapper;

    @Override
    @Transactional
    public PerformanceDTO createPerformance(PerformanceDTO performanceDTO) {
        Employee employee = employeeRepository
                .findById(performanceDTO.getEmployeeId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        Employee reviewer = employeeRepository
                .findById(performanceDTO.getReviewerId())
                .orElseThrow(() -> new AppException(ErrorCode.REVIEWER_NOT_FOUND));

        Performance performance = performanceMapper.toEntity(performanceDTO);
        performance.setEmployee(employee);
        performance.setReviewer(reviewer);
        performance.setReviewDate(new Date());

        return performanceMapper.toDTO(performanceRepository.save(performance));
    }

    @Override
    public PerformanceDTO updatePerformance(UUID id, PerformanceDTO performanceDTO) {
        return null;
    }

    @Override
    public void deletePerformance(UUID id) {}

    @Override
    public PerformanceDTO getPerformance(UUID id) {
        return performanceMapper.toDTO(performanceRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PERFORMANCE_NOT_FOUND)));
    }

    @Override
    public Page<PerformanceDTO> getAllPerformances(int page, int size) {
        return null;
    }

    @Override
    public List<PerformanceDTO> getEmployeePerformances(UUID employeeId) {
        return performanceRepository.findByEmployeeId(employeeId).stream()
                .map(performanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PerformanceDTO> getReviewerPerformances(UUID reviewerId) {
        return performanceRepository.findByReviewerId(reviewerId).stream()
                .map(performanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ... other standard CRUD methods implementation ...
}
