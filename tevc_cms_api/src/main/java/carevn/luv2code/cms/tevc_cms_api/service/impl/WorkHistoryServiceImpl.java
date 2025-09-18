package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import carevn.luv2code.cms.tevc_cms_api.dto.WorkHistoryDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.WorkHistory;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.WorkHistoryMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.WorkHistoryRepository;
import carevn.luv2code.cms.tevc_cms_api.service.WorkHistoryService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkHistoryServiceImpl implements WorkHistoryService {

    private final WorkHistoryRepository workHistoryRepository;
    private final EmployeeRepository employeeRepository;
    private final WorkHistoryMapper workHistoryMapper;

    @Override
    public WorkHistoryDTO createWorkHistory(WorkHistoryDTO workHistoryDTO) {
        WorkHistory workHistory = workHistoryMapper.toEntity(workHistoryDTO);

        workHistory.setEmployee(employeeRepository
                .findById(workHistoryDTO.getEmployeeId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND)));

        return workHistoryMapper.toDTO(workHistoryRepository.save(workHistory));
    }

    @Override
    public WorkHistoryDTO updateWorkHistory(Integer id, WorkHistoryDTO workHistoryDTO) {
        WorkHistory existingWorkHistory = workHistoryRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.WORK_HISTORY_NOT_FOUND));

        workHistoryMapper.updateEntityFromDTO(workHistoryDTO, existingWorkHistory);

        if (workHistoryDTO.getEmployeeId() != null) {
            existingWorkHistory.setEmployee(employeeRepository
                    .findById(workHistoryDTO.getEmployeeId())
                    .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND)));
        }

        return workHistoryMapper.toDTO(workHistoryRepository.save(existingWorkHistory));
    }

    @Override
    public WorkHistoryDTO getWorkHistoryById(Integer id) {
        WorkHistory workHistory = workHistoryRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.WORK_HISTORY_NOT_FOUND));
        return workHistoryMapper.toDTO(workHistory);
    }

    @Override
    public List<WorkHistoryDTO> getWorkHistoriesByEmployeeId(Integer employeeId) {
        return workHistoryRepository.findByEmployeeId(employeeId).stream()
                .map(workHistoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteWorkHistory(Integer id) {
        if (!workHistoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.WORK_HISTORY_NOT_FOUND);
        }
        workHistoryRepository.deleteById(id);
    }
}
