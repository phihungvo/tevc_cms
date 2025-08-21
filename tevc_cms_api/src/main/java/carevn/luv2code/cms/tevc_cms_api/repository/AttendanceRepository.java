package carevn.luv2code.cms.tevc_cms_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import carevn.luv2code.cms.tevc_cms_api.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {}
