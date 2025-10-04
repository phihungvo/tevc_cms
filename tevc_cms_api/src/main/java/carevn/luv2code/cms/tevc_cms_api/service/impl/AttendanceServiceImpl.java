package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public AttendanceDTO getAttendanceById(Integer id) {
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

    @Override
    public List<AttendanceDTO> getAttendancesByEmployee(Integer employeeId) {
        return attendanceRepository.findByEmployeeId(employeeId).stream()
                .map(attendanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AttendanceDTO> filterAttendances(
            String startDate, String endDate, String status, String employeeName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Attendance> spec = Specification.where(null);

        if (startDate != null && endDate != null) {
            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
            LocalDateTime endDt = LocalDate.parse(endDate).atTime(23, 59, 59);
            spec = spec.and((root, query, cb) -> cb.between(root.get("attendanceDate"), start, endDt));
        }

        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        if (employeeName != null && !employeeName.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.join("employee").get("firstName")), "%" + employeeName.toLowerCase() + "%"));
        }

        Page<Attendance> pageResult = attendanceRepository.findAll(spec, pageable);
        return pageResult.map(attendanceMapper::toDTO);
    }
}
