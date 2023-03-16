package ir.maktab.finalprojectphase3.service;

import ir.maktab.finalprojectphase3.data.dto.OfferDTO;
import ir.maktab.finalprojectphase3.data.dto.OfferIdDTO;
import ir.maktab.finalprojectphase3.data.model.Offer;
import ir.maktab.finalprojectphase3.data.model.OfferId;
import ir.maktab.finalprojectphase3.data.model.Order;

import java.util.List;

public interface OfferService extends BaseService<OfferDTO> {

    List<OfferDTO> findAllByOrder(String orderTrackingNumber, String propertyToSort, String direction);

    OfferDTO findByOrderAndConfirmed(String orderTrackingNumber);

    OfferDTO findById(OfferIdDTO offerIdDTO);

    boolean isExistByOrder(Order order);

    boolean isExistByOfferId(OfferId offerId);


    void updateOfferAfterConfirmed(Offer offer);
}
