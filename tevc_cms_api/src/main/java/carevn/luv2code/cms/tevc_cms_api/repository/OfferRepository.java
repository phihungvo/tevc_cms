package carevn.luv2code.cms.tevc_cms_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {}
