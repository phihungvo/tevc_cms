package carevn.luv2code.cms.tevc_cms_api.repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Training;

@Repository
public interface TrainingRepository extends JpaRepository<Training, UUID> {
    List<Training> findByParticipantsId(UUID employeeId);

    List<Training> findByStartDateBetween(Date startDate, Date endDate);

    boolean existsByNameAndStartDate(String name, Date startDate);
}
