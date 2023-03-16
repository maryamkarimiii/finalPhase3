package ir.maktab.finalprojectphase3.mapper;

import ir.maktab.finalprojectphase3.data.dto.OfferDTO;
import ir.maktab.finalprojectphase3.data.model.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OfferMapper {

    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);


    Offer dtoToModel(OfferDTO offerDTO);

    @Mapping(target = "orderTrackingNumber", expression = "java(offer.getOfferId().getOrder().getTrackingNumber())")
    @Mapping(target = "expertUserName", expression = "java(offer.getOfferId().getExpert().getUsername())")
    OfferDTO modelToDto(Offer offer);

    List<OfferDTO> orderListsToDto(List<Offer> offerList);
}