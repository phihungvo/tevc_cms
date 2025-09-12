package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import carevn.luv2code.cms.tevc_cms_api.dto.OfferDTO;

public interface OfferService {

    OfferDTO createOffer(Integer candidateId, String position, double salary);

    OfferDTO updateOffer(Integer offerId, String newPosition, double newSalary);

    void withdrawOffer(Integer offerId);

    OfferDTO getOffer(Integer offerId);

    List<OfferDTO> getAllOffers();

    List<OfferDTO> getOffersByCandidate(Integer candidateId);

    List<OfferDTO> getOffersByStatus(String status);

    void acceptOffer(Integer offerId);

    void declineOffer(Integer offerId);
}
