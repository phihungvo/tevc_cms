package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.List;

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
    public OfferDTO createOffer(Integer candidateId, String position, double salary) {
        return null;
    }

    @Override
    public OfferDTO updateOffer(Integer offerId, String newPosition, double newSalary) {
        return null;
    }

    @Override
    public void withdrawOffer(Integer offerId) {}

    @Override
    public OfferDTO getOffer(Integer offerId) {
        return null;
    }

    @Override
    public List<OfferDTO> getAllOffers() {
        return List.of();
    }

    @Override
    public List<OfferDTO> getOffersByCandidate(Integer candidateId) {
        return List.of();
    }

    @Override
    public List<OfferDTO> getOffersByStatus(String status) {
        return List.of();
    }

    @Override
    public void acceptOffer(Integer offerId) {}

    @Override
    public void declineOffer(Integer offerId) {}
}
