package carevn.luv2code.cms.tevc_cms_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.File;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
    Optional<File> findByFileName(String fileName);

    boolean existsByFileName(String fileName);
}
