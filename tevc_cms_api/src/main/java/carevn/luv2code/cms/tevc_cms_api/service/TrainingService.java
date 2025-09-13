package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.TrainingDTO;

public interface TrainingService {

    TrainingDTO createTraining(TrainingDTO trainingDTO);

    TrainingDTO updateTraining(Integer id, TrainingDTO trainingDTO);

    void deleteTraining(Integer id);

    TrainingDTO getTraining(Integer id);

    Page<TrainingDTO> getAllTrainings(int page, int size);

    TrainingDTO addParticipants(Integer trainingId, List<Integer> employeeIds);

    TrainingDTO removeParticipant(Integer trainingId, Integer employeeId);
}
