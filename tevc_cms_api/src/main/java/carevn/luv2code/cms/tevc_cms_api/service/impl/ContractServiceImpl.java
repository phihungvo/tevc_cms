package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.dto.ContractDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Contract;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.ContractMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.ContractRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.service.ContractService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;
    private final EmployeeRepository employeeRepository;
    private final ContractMapper contractMapper;

    @Override
    @Transactional
    public ContractDTO createContract(ContractDTO contractDTO) {
        // Check if employee has active contract
        if (contractRepository
                .findByEmployeeIdAndStatus(contractDTO.getEmployeeId(), "ACTIVE")
                .isPresent()) {
            throw new AppException(ErrorCode.CONTRACT_ALREADY_EXISTS);
        }

        Contract contract = contractMapper.toEntity(contractDTO);
        Employee employee = employeeRepository
                .findById(contractDTO.getEmployeeId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        contract.setEmployee(employee);
        contract.setStatus("ACTIVE");
        contract.setSignedDate(new Date());

        return contractMapper.toDTO(contractRepository.save(contract));
    }

    @Override
    public ContractDTO updateContract(UUID id, ContractDTO contractDTO) {
        return null;
    }

    @Override
    public void deleteContract(UUID id) {}

    @Override
    public ContractDTO getContract(UUID id) {
        return null;
    }

    @Override
    public Page<ContractDTO> getAllContracts(int page, int size) {
        return null;
    }

    @Override
    @Transactional
    public ContractDTO terminateContract(UUID id, String reason) {
        Contract contract =
                contractRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));

        if (!"ACTIVE".equals(contract.getStatus())) {
            throw new AppException(ErrorCode.CONTRACT_NOT_ACTIVE);
        }

        contract.setStatus("TERMINATED");
        contract.setTerminationReason(reason);
        contract.setTerminationDate(new Date());

        return contractMapper.toDTO(contractRepository.save(contract));
    }

    @Override
    public List<ContractDTO> getEmployeeContracts(UUID employeeId) {
        return contractRepository.findByEmployeeId(employeeId).stream()
                .map(contractMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ... other standard CRUD methods implementation ...
}
