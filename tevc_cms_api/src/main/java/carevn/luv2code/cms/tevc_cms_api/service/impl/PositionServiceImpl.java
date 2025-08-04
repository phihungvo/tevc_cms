package carevn.luv2code.cms.tevc_cms_api.service.impl;

import carevn.luv2code.cms.tevc_cms_api.dto.PositionDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Position;
import carevn.luv2code.cms.tevc_cms_api.enums.PositionType;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.PositionMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.PositionRepository;
import carevn.luv2code.cms.tevc_cms_api.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    @Override
    @Transactional
    public PositionDTO createPosition(PositionDTO positionDTO) {
        if (positionRepository.existsByTitle(positionDTO.getTitle())) {
            throw new AppException(ErrorCode.POSITION_TITLE_EXISTS);
        }
        Position position = positionMapper.toEntity(positionDTO);
        Position savedPosition = positionRepository.save(position);
        return positionMapper.toDTO(savedPosition);
    }

    @Override
    @Transactional
    public PositionDTO updatePosition(UUID id, PositionDTO positionDTO) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND));
        
        positionMapper.updatePositionFromDto(positionDTO, position);
        Position updatedPosition = positionRepository.save(position);
        return positionMapper.toDTO(updatedPosition);
    }

    @Override
    public PositionDTO getPosition(UUID id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND));
        return positionMapper.toDTO(position);
    }

    @Override
    public Page<PositionDTO> getAllPositions(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return positionRepository.findAll(pageRequest)
                .map(positionMapper::toDTO);
    }

    @Override
    public List<PositionDTO> getPositionsByType(PositionType type) {
        return List.of();
    }

    @Override
    @Transactional
    public void deletePosition(UUID id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POSITION_NOT_FOUND));
        if (!position.getEmployees().isEmpty()) {
            throw new AppException(ErrorCode.POSITION_HAS_EMPLOYEES);
        }
        positionRepository.delete(position);
    }
}
