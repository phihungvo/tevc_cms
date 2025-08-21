package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.AttendanceDTO;

public interface AttendanceService {
    AttendanceDTO createAttendance(AttendanceDTO attendanceDTO);

    List<AttendanceDTO> getAllAttendances();

    AttendanceDTO getAttendance(UUID id);

    void deleteAttendance(UUID id);

    AttendanceDTO updateAttendance(UUID id, AttendanceDTO attendanceDTO);

    Page<AttendanceDTO> getAllAttendancesWithPagination(int page, int size);
}
