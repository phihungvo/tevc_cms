package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.TrainingDTO;
import carevn.luv2code.cms.tevc_cms_api.service.TrainingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/trainings")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;

    @PostMapping
    @PreAuthorize("hasAuthority('TRAINING:CREATE')")
    public ResponseEntity<TrainingDTO> createTraining(@RequestBody TrainingDTO trainingDTO) {
        return ResponseEntity.ok(trainingService.createTraining(trainingDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TRAINING:UPDATE')")
    public ResponseEntity<TrainingDTO> updateTraining(@PathVariable Integer id, @RequestBody TrainingDTO trainingDTO) {
        return ResponseEntity.ok(trainingService.updateTraining(id, trainingDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('TRAINING:READ')")
    public ResponseEntity<TrainingDTO> getTraining(@PathVariable Integer id) {
        return ResponseEntity.ok(trainingService.getTraining(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('TRAINING:READ')")
    public ResponseEntity<Page<TrainingDTO>> getAllTrainings(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(trainingService.getAllTrainings(page, size));
    }

    @PostMapping("/{id}/participants")
    @PreAuthorize("hasAuthority('TRAINING:UPDATE')")
    public ResponseEntity<TrainingDTO> addParticipants(
            @PathVariable Integer id, @RequestBody List<Integer> employeeIds) {
        return ResponseEntity.ok(trainingService.addParticipants(id, employeeIds));
    }

    @DeleteMapping("/{trainingId}/participants/{employeeId}")
    @PreAuthorize("hasAuthority('TRAINING:UPDATE')")
    public ResponseEntity<TrainingDTO> removeParticipant(
            @PathVariable Integer trainingId, @PathVariable Integer employeeId) {
        return ResponseEntity.ok(trainingService.removeParticipant(trainingId, employeeId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('TRAINING:DELETE')")
    public ResponseEntity<Void> deleteTraining(@PathVariable Integer id) {
        trainingService.deleteTraining(id);
        return ResponseEntity.ok().build();
    }
}
