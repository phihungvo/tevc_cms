package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import carevn.luv2code.cms.tevc_cms_api.dto.AttendanceDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Attendance;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.AttendanceMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.AttendanceRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.service.AttendanceService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceMapper attendanceMapper;

    @Override
    public AttendanceDTO createAttendance(AttendanceDTO attendanceDTO) {
        Attendance attendance = attendanceMapper.toEntity(attendanceDTO);

        attendance.setEmployee(employeeRepository
                .findById(attendanceDTO.getEmployeeId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND)));
        Attendance savedAttendance = attendanceRepository.save(attendance);
        return attendanceMapper.toDTO(savedAttendance);
    }

    @Override
    public List<AttendanceDTO> getAllAttendances() {
        return attendanceRepository.findAll().stream()
                .map(attendanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceDTO getAttendance(Integer id) {
        Attendance attendance =
                attendanceRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ATTENDANCE_NOT_FOUND));
        return attendanceMapper.toDTO(attendance);
    }

    @Override
    public AttendanceDTO updateAttendance(Integer attendanceId, AttendanceDTO attendanceDTO) {
        Attendance existingAttendance = attendanceRepository
                .findById(attendanceId)
                .orElseThrow(() -> new AppException(ErrorCode.ATTENDANCE_NOT_FOUND));

        attendanceMapper.updateEntityFromDTO(attendanceDTO, existingAttendance);

        if (attendanceDTO.getEmployeeId() != null) {
            Employee employee = employeeRepository
                    .findById(attendanceDTO.getEmployeeId())
                    .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
            existingAttendance.setEmployee(employee);
        }

        Attendance savedAttendance = attendanceRepository.save(existingAttendance);

        return attendanceMapper.toDTO(savedAttendance);
    }

    @Override
    public void deleteAttendance(Integer id) {
        if (!attendanceRepository.existsById(id)) {
            throw new AppException(ErrorCode.ATTENDANCE_NOT_FOUND);
        }
        attendanceRepository.deleteById(id);
    }

    @Override
    public Page<AttendanceDTO> getAllAttendancesWithPagination(int page, int size) {
        return attendanceRepository.findAll(PageRequest.of(page, size)).map(attendanceMapper::toDTO);
    }
}
