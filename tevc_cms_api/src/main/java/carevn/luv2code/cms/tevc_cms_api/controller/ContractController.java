package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;

import carevn.luv2code.cms.tevc_cms_api.dto.ContractDTO;
import carevn.luv2code.cms.tevc_cms_api.service.ContractService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {
    private final ContractService contractService;

    @PostMapping
    public ResponseEntity<ContractDTO> createContract(@RequestBody ContractDTO contractDTO) {
        return ResponseEntity.ok(contractService.createContract(contractDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContractDTO> updateContract(@PathVariable Integer id, @RequestBody ContractDTO contractDTO) {
        return ResponseEntity.ok(contractService.updateContract(id, contractDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractDTO> getContract(@PathVariable Integer id) {
        return ResponseEntity.ok(contractService.getContract(id));
    }

    @GetMapping
    public ResponseEntity<Page<ContractDTO>> getAllContracts(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(contractService.getAllContracts(page, size));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ContractDTO>> getEmployeeContracts(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(contractService.getEmployeeContracts(employeeId));
    }

    @PatchMapping("/{id}/terminate")
    public ResponseEntity<ContractDTO> terminateContract(@PathVariable Integer id, @RequestParam String reason) {
        return ResponseEntity.ok(contractService.terminateContract(id, reason));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable Integer id) {
        contractService.deleteContract(id);
        return ResponseEntity.ok().build();
    }
}
