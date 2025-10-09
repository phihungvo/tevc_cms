package carevn.luv2code.cms.tevc_cms_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {

    @Query("SELECT c.id FROM Candidate c JOIN c.jobPostings jp WHERE jp.id = :jobPostingId")
    List<Integer> findIdsByJobPostingId(@Param("jobPostingId") Integer jobPostingId);
}
