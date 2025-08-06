package carevn.luv2code.cms.tevc_cms_api.repository;

import aj.org.objectweb.asm.commons.Remapper;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.entity.Position;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PositionRepository extends JpaRepository<Position, UUID> {
    boolean existsByTitle(String title);

    List<Position> findByTitleContainingIgnoreCase(String title);
}
