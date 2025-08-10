package carevn.luv2code.cms.tevc_cms_api.service;

import carevn.luv2code.cms.tevc_cms_api.dto.PositionDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Position;
import carevn.luv2code.cms.tevc_cms_api.enums.PositionType;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface PositionService {

    PositionDTO createPosition(PositionDTO positionDTO);

    PositionDTO updatePosition(UUID id, PositionDTO positionDTO);

    void deletePosition(UUID id);

    PositionDTO getPosition(UUID id);

    Page<PositionDTO> getAllPositions(int page, int size);

    List<PositionDTO> getAllNoPaging();

    List<PositionDTO> getPositionsByType(PositionType type);
}
