package carevn.luv2code.cms.tevc_cms_api.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Interview;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Integer> {

    List<Interview> findByCandidateId(Integer candidateId);

    List<Interview> findByInterviewDateBetween(Timestamp startDate, Timestamp endDate);
}
