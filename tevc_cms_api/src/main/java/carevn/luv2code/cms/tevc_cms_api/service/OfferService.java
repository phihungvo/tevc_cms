package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;
import java.util.UUID;

import carevn.luv2code.cms.tevc_cms_api.dto.OfferDTO;

public interface OfferService {

    OfferDTO createOffer(UUID candidateId, String position, double salary);

    OfferDTO updateOffer(UUID offerId, String newPosition, double newSalary);

    void withdrawOffer(UUID offerId);

    OfferDTO getOffer(UUID offerId);

    List<OfferDTO> getAllOffers();

    List<OfferDTO> getOffersByCandidate(UUID candidateId);

    List<OfferDTO> getOffersByStatus(String status);

    void acceptOffer(UUID offerId);

    void declineOffer(UUID offerId);
}
