package carevn.luv2code.cms.tevc_cms_api.service.impl;

import carevn.luv2code.cms.tevc_cms_api.dto.TrainingDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.*;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.mapper.TrainingMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.*;
import carevn.luv2code.cms.tevc_cms_api.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final EmployeeRepository employeeRepository;
    private final TrainingMapper trainingMapper;

    @Override
    @Transactional
    public TrainingDTO createTraining(TrainingDTO trainingDTO) {
        if (trainingRepository.existsByNameAndStartDate(
                trainingDTO.getName(), trainingDTO.getStartDate())) {
            throw new AppException("Training already exists for this date");
        }

        Training training = trainingMapper.toEntity(trainingDTO);
        Training savedTraining = trainingRepository.save(training);
        return trainingMapper.toDTO(savedTraining);
    }

    @Override
    @Transactional
    public TrainingDTO updateTraining(UUID id, TrainingDTO trainingDTO) {
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new AppException("Training not found"));
        
        trainingMapper.updateTrainingFromDto(trainingDTO, training);
        Training updatedTraining = trainingRepository.save(training);
        return trainingMapper.toDTO(updatedTraining);
    }

    @Override
    public TrainingDTO getTraining(UUID id) {
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new AppException("Training not found"));
        return trainingMapper.toDTO(training);
    }

    @Override
    public Page<TrainingDTO> getAllTrainings(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return trainingRepository.findAll(pageRequest)
                .map(trainingMapper::toDTO);
    }

    @Override
    @Transactional
    public TrainingDTO addParticipants(UUID trainingId, List<UUID> employeeIds) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new AppException("Training not found"));

        List<Employee> employees = employeeRepository.findAllById(employeeIds);
        training.getParticipants().addAll(employees);
        
        Training updatedTraining = trainingRepository.save(training);
        return trainingMapper.toDTO(updatedTraining);
    }

    @Override
    @Transactional
    public TrainingDTO removeParticipant(UUID trainingId, UUID employeeId) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new AppException("Training not found"));
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new AppException("Employee not found"));
                
        training.getParticipants().remove(employee);
        Training updatedTraining = trainingRepository.save(training);
        return trainingMapper.toDTO(updatedTraining);
    }

    @Override
    @Transactional
    public void deleteTraining(UUID id) {
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new AppException("Training not found"));
        trainingRepository.delete(training);
    }
}
