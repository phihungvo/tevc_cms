package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.AttendanceDTO;

public interface AttendanceService {
    AttendanceDTO createAttendance(AttendanceDTO attendanceDTO);

    List<AttendanceDTO> getAllAttendances();

    AttendanceDTO getAttendance(Integer id);

    void deleteAttendance(Integer id);

    AttendanceDTO updateAttendance(Integer id, AttendanceDTO attendanceDTO);

    Page<AttendanceDTO> getAllAttendancesWithPagination(int page, int size);
}
