package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import carevn.luv2code.cms.tevc_cms_api.dto.PositionDTO;
import carevn.luv2code.cms.tevc_cms_api.enums.PositionType;

public interface PositionService {

    PositionDTO createPosition(PositionDTO positionDTO);

    PositionDTO updatePosition(Integer id, PositionDTO positionDTO);

    void deletePosition(Integer id);

    PositionDTO getPosition(Integer id);

    Page<PositionDTO> getAllPositions(int page, int size);

    List<PositionDTO> getAllNoPaging();

    Page<PositionDTO> getPositionsByEmployeeIdPaged(Integer employeeId, Pageable pageable);

    List<PositionDTO> getPositionsByType(PositionType type);
}
