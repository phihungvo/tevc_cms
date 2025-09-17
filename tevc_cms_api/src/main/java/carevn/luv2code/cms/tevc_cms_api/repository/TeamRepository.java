package carevn.luv2code.cms.tevc_cms_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import carevn.luv2code.cms.tevc_cms_api.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    Page<Team> findAll(Pageable pageable);
}
