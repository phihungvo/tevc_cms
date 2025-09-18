package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import carevn.luv2code.cms.tevc_cms_api.dto.EducationDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Education;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.EducationMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.EducationRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.service.EducationService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;
    private final EmployeeRepository employeeRepository;
    private final EducationMapper educationMapper;

    @Override
    public EducationDTO createEducation(EducationDTO educationDTO) {
        Education education = educationMapper.toEntity(educationDTO);

        education.setEmployee(employeeRepository
                .findById(educationDTO.getEmployeeId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND)));

        return educationMapper.toDTO(educationRepository.save(education));
    }

    @Override
    public EducationDTO updateEducation(Integer id, EducationDTO educationDTO) {
        Education existingEducation =
                educationRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EDUCATION_NOT_FOUND));

        educationMapper.updateEntityFromDTO(educationDTO, existingEducation);

        if (educationDTO.getEmployeeId() != null) {
            existingEducation.setEmployee(employeeRepository
                    .findById(educationDTO.getEmployeeId())
                    .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND)));
        }

        return educationMapper.toDTO(educationRepository.save(existingEducation));
    }

    @Override
    public EducationDTO getEducationById(Integer id) {
        Education education =
                educationRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.EDUCATION_NOT_FOUND));
        return educationMapper.toDTO(education);
    }

    @Override
    public List<EducationDTO> getEducationsByEmployeeId(Integer employeeId) {
        return educationRepository.findByEmployeeId(employeeId).stream()
                .map(educationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteEducation(Integer id) {
        if (!educationRepository.existsById(id)) {
            throw new AppException(ErrorCode.EDUCATION_NOT_FOUND);
        }
        educationRepository.deleteById(id);
    }
}
