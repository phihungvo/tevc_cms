package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import carevn.luv2code.cms.tevc_cms_api.dto.OfferDTO;
import carevn.luv2code.cms.tevc_cms_api.mapper.OfferMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.OfferRepository;
import carevn.luv2code.cms.tevc_cms_api.service.OfferService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;

    @Override
    public OfferDTO createOffer(UUID candidateId, String position, double salary) {
        return null;
    }

    @Override
    public OfferDTO updateOffer(UUID offerId, String newPosition, double newSalary) {
        return null;
    }

    @Override
    public void withdrawOffer(UUID offerId) {}

    @Override
    public OfferDTO getOffer(UUID offerId) {
        return null;
    }

    @Override
    public List<OfferDTO> getAllOffers() {
        return List.of();
    }

    @Override
    public List<OfferDTO> getOffersByCandidate(UUID candidateId) {
        return List.of();
    }

    @Override
    public List<OfferDTO> getOffersByStatus(String status) {
        return List.of();
    }

    @Override
    public void acceptOffer(UUID offerId) {}

    @Override
    public void declineOffer(UUID offerId) {}
}
