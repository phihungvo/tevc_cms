package carevn.luv2code.cms.tevc_cms_api.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Training;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {
    List<Training> findByParticipantsId(Integer employeeId);

    List<Training> findByStartDateBetween(Date startDate, Date endDate);

    boolean existsByNameAndStartDate(String name, Date startDate);
}
