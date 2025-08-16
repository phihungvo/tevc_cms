package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import carevn.luv2code.cms.tevc_cms_api.dto.PositionDTO;
import carevn.luv2code.cms.tevc_cms_api.service.PositionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
public class PositionController {
    private final PositionService positionService;

    @PostMapping
    //    @PreAuthorize("hasAuthority('POSITION:CREATE')")
    public ResponseEntity<PositionDTO> createPosition(@RequestBody PositionDTO positionDTO) {
        return ResponseEntity.ok(positionService.createPosition(positionDTO));
    }

    @PatchMapping("/{id}")
    //    @PreAuthorize("hasAuthority('POSITION:UPDATE')")
    public ResponseEntity<PositionDTO> updatePosition(@PathVariable UUID id, @RequestBody PositionDTO positionDTO) {
        return ResponseEntity.ok(positionService.updatePosition(id, positionDTO));
    }

    @GetMapping("/{id}")
    //    @PreAuthorize("hasAuthority('POSITION:READ')")
    public ResponseEntity<PositionDTO> getPosition(@PathVariable UUID id) {
        return ResponseEntity.ok(positionService.getPosition(id));
    }

    @GetMapping
    //    @PreAuthorize("hasAuthority('POSITION:READ')")
    public ResponseEntity<Page<PositionDTO>> getAllPositions(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(positionService.getAllPositions(page, size));
    }

    @GetMapping("/no-paging")
    public ResponseEntity<List<PositionDTO>> getAllNoPaging() {
        return ResponseEntity.ok(positionService.getAllNoPaging());
    }

    @DeleteMapping("/{id}")
    //    @PreAuthorize("hasAuthority('POSITION:DELETE')")
    public ResponseEntity<Void> deletePosition(@PathVariable UUID id) {
        positionService.deletePosition(id);
        return ResponseEntity.ok().build();
    }

    //    @GetMapping("/getPositionsByTitle")
    ////    @PreAuthorize("hasAuthority('POSITION:READ')")
    //    public ResponseEntity<List<PositionDTO>> getPositionByTitle(@RequestParam("title") String title) {
    //        return ResponseEntity.ok(positionService.getPositionByTitle(title));
    //    }
}
