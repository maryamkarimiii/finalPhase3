package ir.maktab.finalprojectphase3.service.impl;

import ir.maktab.finalprojectphase3.data.dao.ExpertDao;
import ir.maktab.finalprojectphase3.data.dao.OfferDao;
import ir.maktab.finalprojectphase3.data.dao.OrderDao;
import ir.maktab.finalprojectphase3.data.dto.OfferDTO;
import ir.maktab.finalprojectphase3.data.dto.OfferIdDTO;
import ir.maktab.finalprojectphase3.data.model.Offer;
import ir.maktab.finalprojectphase3.data.model.OfferId;
import ir.maktab.finalprojectphase3.data.model.Order;
import ir.maktab.finalprojectphase3.exception.NotFoundException;
import ir.maktab.finalprojectphase3.exception.ValidationException;
import ir.maktab.finalprojectphase3.mapper.OfferMapper;
import ir.maktab.finalprojectphase3.service.OfferService;
import ir.maktab.finalprojectphase3.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final OfferDao offerDao;
    private final OrderService orderService;
    private final OrderDao orderDao;
    private final ExpertDao expertDao;

    @Transactional
    @Override
    public void save(OfferDTO offerDTO) {
        Offer offer = convertOfferDtoToModel(offerDTO);
        validateOfferExistenceByOfferId(offer.getOfferId());
        validateOfferWorkDate(offer);
        validateOfferPrice(offer);
        if (isExistByOrder(offer.getOfferId().getOrder()))
            orderService.changeStatusToWaitingForCustomerChose(offer.getOfferId().getOrder());
        offerDao.save(offer);
    }

    @Override
    public List<OfferDTO> findAllByOrder(String orderTrackingNumber, String propertyToSort, String direction) {
        validatePropertyBeforeSort(propertyToSort);
        validateDirectionBeforeSort(direction);
        Order order = orderDao.findByTrackingNumberAndDisable(orderTrackingNumber, false).orElseThrow();
        List<Offer> offerList = offerDao
                .findAllByOfferId_OrderAndDisableFalse(order, Sort.by(Sort.Direction.valueOf(direction), propertyToSort));
        return OfferMapper.INSTANCE.orderListsToDto(offerList);
    }

    @Override
    public OfferDTO findByOrderAndConfirmed(String orderTrackingNumber) {
        Order order = orderDao.findByTrackingNumberAndDisable(orderTrackingNumber, false).orElseThrow();
        Offer offer = offerDao.findByOfferId_OrderAndConfirmedByCustomerTrue(order)
                .orElseThrow(() -> new NotFoundException("the offer by this order dose not exist"));
        return OfferMapper.INSTANCE.modelToDto(offer);
    }

    @Override
    public OfferDTO findById(OfferIdDTO offerIdDTO) {
        OfferId offerId = createOfferId(offerIdDTO.getOrderTrackingNumber(), offerIdDTO.getExpertUserName());
        Offer offer = offerDao.findById(offerId).orElseThrow(() -> new NotFoundException("the offer dose not exist"));
        return OfferMapper.INSTANCE.modelToDto(offer);
    }

    @Override
    public boolean isExistByOrder(Order order) {
        return offerDao.existsByOfferId_Order(order);
    }

    @Override
    public boolean isExistByOfferId(OfferId offerId) {
        return offerDao.existsById(offerId);
    }

    @Override
    public void updateOfferAfterConfirmed(Offer offer) {
        offer.setConfirmedByCustomer(true);
        offerDao.save(offer);
    }

    private OfferId createOfferId(String orderTrackingNumber, String expertUserName) {
        return OfferId.builder()
                .order(orderDao.findByTrackingNumberAndDisable(orderTrackingNumber, false).orElseThrow())
                .expert(expertDao.findByUsernameAndDisable(expertUserName, false).orElseThrow())
                .build();
    }

    private Offer convertOfferDtoToModel(OfferDTO offerDTO) {
        Offer offer = OfferMapper.INSTANCE.dtoToModel(offerDTO);
        offer.setOfferId(createOfferId(offerDTO.getOrderTrackingNumber(),
                offerDTO.getExpertUserName()));
        return offer;
    }

    private void validateOfferPrice(Offer offer) {
        Double basePrice = offer.getOfferId().getOrder().getSubService().getBaseAmount();
        if (offer.getPrice() < basePrice)
            throw new ValidationException("the offered price cant be less than: " + basePrice);
    }

    private void validateOfferExistenceByOfferId(OfferId offerId) {
        if (isExistByOfferId(offerId))
            throw new ValidationException
                    (String.format("you can put offer for this order %s just once", offerId.getOrder().getTrackingNumber()));
    }

    private void validateOfferWorkDate(Offer offer) {
        if (offer.getWorkDate().isBefore(LocalDate.now()))
            throw new ValidationException("the offered date cant be before: " + new Date());
    }

    private void validatePropertyBeforeSort(String property) {
        if (!property.equals("offerId.expert.totalScore") && !property.equals("price"))
            throw new ValidationException("you can sort result just by expert.score or price");
    }

    private void validateDirectionBeforeSort(String direction) {
        if (!direction.equals("ASC") && !direction.equals("DESC"))
            throw new ValidationException("you can sort result just by ASC or DESC direction");
    }
}
