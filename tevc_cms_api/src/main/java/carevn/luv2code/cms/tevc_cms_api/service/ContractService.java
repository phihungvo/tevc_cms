package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import carevn.luv2code.cms.tevc_cms_api.dto.ContractDTO;

public interface ContractService {
    ContractDTO createContract(ContractDTO contractDTO);

    ContractDTO updateContract(Integer id, ContractDTO contractDTO);

    ContractDTO getContract(Integer id);

    Page<ContractDTO> getAllContracts(int page, int size);

    Page<ContractDTO> getContractsByEmployeeIdPaged(Integer employeeId, Pageable pageable);

    List<ContractDTO> getEmployeeContracts(Integer employeeId);

    ContractDTO terminateContract(Integer id, String reason);

    void deleteContract(Integer id);
}
