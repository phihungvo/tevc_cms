package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.dto.ContractDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Contract;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.entity.Position;
import carevn.luv2code.cms.tevc_cms_api.enums.ContractStatus;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.ContractMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.ContractRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.PositionRepository;
import carevn.luv2code.cms.tevc_cms_api.service.ContractService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;
    private final EmployeeRepository employeeRepository;
    private final ContractMapper contractMapper;
    private final PositionRepository positionRepository;

    @Override
    @Transactional
    public ContractDTO createContract(ContractDTO contractDTO) {
        if (contractRepository
                .findByEmployeeIdAndStatus(contractDTO.getEmployeeId(), ContractStatus.ACTIVE)
                .isPresent()) {
            throw new AppException(ErrorCode.CONTRACT_ALREADY_EXISTS);
        }
        Contract contract = contractMapper.toEntity(contractDTO);

        Employee employee = employeeRepository
                .findById(contractDTO.getEmployeeId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        contract.setEmployee(employee);

        Position position = positionRepository
                .findById(contractDTO.getPositionId()) // Giả sử bạn có PositionRepository
                .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND)); // Tạo ErrorCode nếu cần
        contract.setPosition(position);

        // Optional: Set default
        contract.setStatus(ContractStatus.ACTIVE);
        contract.setSignedDate(LocalDate.now());

        return contractMapper.toDTO(contractRepository.save(contract));
    }

    @Override
    @Transactional
    public ContractDTO updateContract(Integer id, ContractDTO contractDTO) {
        Contract contract =
                contractRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
        contractMapper.updateContractFromDto(contractDTO, contract);
        Contract updatedContract = contractRepository.save(contract);
        return contractMapper.toDTO(updatedContract);
    }

    @Override
    public ContractDTO getContract(Integer id) {
        Contract contract =
                contractRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
        return contractMapper.toDTO(contract);
    }

    @Override
    public Page<ContractDTO> getAllContracts(int page, int size) {
        return contractRepository.findAll(PageRequest.of(page, size)).map(contractMapper::toDTO);
    }

    @Override
    public Page<ContractDTO> getContractsByEmployeeIdPaged(Integer employeeId, Pageable pageable) {
        return contractRepository.findByEmployeeId(employeeId, pageable).map(contractMapper::toDTO);
    }

    @Override
    public List<ContractDTO> getEmployeeContracts(Integer employeeId) {
        return contractRepository.findAll().stream()
                .filter(contract -> contract.getEmployee().getId().equals(employeeId))
                .map(contractMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ContractDTO terminateContract(Integer id, String reason) {
        Contract contract =
                contractRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));

        if (!"ACTIVE".equals(contract.getStatus())) {
            throw new AppException(ErrorCode.CONTRACT_NOT_ACTIVE);
        }

        contract.setStatus(ContractStatus.TERMINATED);
        contract.setTerminationReason(reason);
        contract.setTerminationDate(java.time.LocalDate.now());
        return contractMapper.toDTO(contractRepository.save(contract));
    }

    @Override
    @Transactional
    public void deleteContract(Integer id) {
        Contract contract =
                contractRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_FOUND));
        contractRepository.delete(contract);
    }
}
