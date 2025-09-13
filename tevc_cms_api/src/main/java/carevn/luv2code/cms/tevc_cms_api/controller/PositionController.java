package carevn.luv2code.cms.tevc_cms_api.controller;

import java.util.List;

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
    public ResponseEntity<PositionDTO> createPosition(@RequestBody PositionDTO positionDTO) {
        return ResponseEntity.ok(positionService.createPosition(positionDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PositionDTO> updatePosition(@PathVariable Integer id, @RequestBody PositionDTO positionDTO) {
        return ResponseEntity.ok(positionService.updatePosition(id, positionDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PositionDTO> getPosition(@PathVariable Integer id) {
        return ResponseEntity.ok(positionService.getPosition(id));
    }

    @GetMapping
    public ResponseEntity<Page<PositionDTO>> getAllPositions(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(positionService.getAllPositions(page, size));
    }

    @GetMapping("/no-paging")
    public ResponseEntity<List<PositionDTO>> getAllNoPaging() {
        return ResponseEntity.ok(positionService.getAllNoPaging());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable Integer id) {
        positionService.deletePosition(id);
        return ResponseEntity.ok().build();
    }

    //    @GetMapping("/getPositionsByTitle")
    //    public ResponseEntity<List<PositionDTO>> getPositionByTitle(@RequestParam("title") String title) {
    //        return ResponseEntity.ok(positionService.getPositionByTitle(title));
    //    }
}
