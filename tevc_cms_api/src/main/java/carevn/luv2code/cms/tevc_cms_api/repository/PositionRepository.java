package carevn.luv2code.cms.tevc_cms_api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {
    boolean existsByTitle(String title);

    Page<Position> findAll(Pageable pageable);

    List<Position> findByTitleContainingIgnoreCase(String title);
}
