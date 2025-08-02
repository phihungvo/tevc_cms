package carevn.luv2code.cms.tevc_cms_api.service;

import carevn.luv2code.cms.tevc_cms_api.dto.TrainingDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface TrainingService {

    TrainingDTO createTraining(TrainingDTO trainingDTO);

    TrainingDTO updateTraining(UUID id, TrainingDTO trainingDTO);

    void deleteTraining(UUID id);

    TrainingDTO getTraining(UUID id);

    Page<TrainingDTO> getAllTrainings(int page, int size);

    TrainingDTO addParticipants(UUID trainingId, List<UUID> employeeIds);

    TrainingDTO removeParticipant(UUID trainingId, UUID employeeId);
}
