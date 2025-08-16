package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.ContractDTO;

public interface ContractService {
    ContractDTO createContract(ContractDTO contractDTO);

    ContractDTO updateContract(UUID id, ContractDTO contractDTO);

    void deleteContract(UUID id);

    ContractDTO getContract(UUID id);

    Page<ContractDTO> getAllContracts(int page, int size);

    List<ContractDTO> getEmployeeContracts(UUID employeeId);

    ContractDTO terminateContract(UUID id, String reason);
}
