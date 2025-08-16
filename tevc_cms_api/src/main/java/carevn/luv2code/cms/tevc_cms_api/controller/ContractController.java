package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    //    @PreAuthorize("hasAuthority('CONTRACT:CREATE')")
    public ResponseEntity<ContractDTO> createContract(@RequestBody ContractDTO contractDTO) {
        return ResponseEntity.ok(contractService.createContract(contractDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CONTRACT:UPDATE')")
    public ResponseEntity<ContractDTO> updateContract(@PathVariable UUID id, @RequestBody ContractDTO contractDTO) {
        return ResponseEntity.ok(contractService.updateContract(id, contractDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CONTRACT:READ')")
    public ResponseEntity<ContractDTO> getContract(@PathVariable UUID id) {
        return ResponseEntity.ok(contractService.getContract(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CONTRACT:READ')")
    public ResponseEntity<Page<ContractDTO>> getAllContracts(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(contractService.getAllContracts(page, size));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAuthority('CONTRACT:READ')")
    public ResponseEntity<List<ContractDTO>> getEmployeeContracts(@PathVariable UUID employeeId) {
        return ResponseEntity.ok(contractService.getEmployeeContracts(employeeId));
    }

    @PatchMapping("/{id}/terminate")
    @PreAuthorize("hasAuthority('CONTRACT:UPDATE')")
    public ResponseEntity<ContractDTO> terminateContract(@PathVariable UUID id, @RequestParam String reason) {
        return ResponseEntity.ok(contractService.terminateContract(id, reason));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CONTRACT:DELETE')")
    public ResponseEntity<Void> deleteContract(@PathVariable UUID id) {
        contractService.deleteContract(id);
        return ResponseEntity.ok().build();
    }
}
