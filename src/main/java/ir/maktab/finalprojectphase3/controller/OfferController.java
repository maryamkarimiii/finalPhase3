package ir.maktab.finalprojectphase3.controller;

import ir.maktab.finalprojectphase3.data.dto.OfferDTO;
import ir.maktab.finalprojectphase3.data.dto.OfferIdDTO;
import ir.maktab.finalprojectphase3.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/offer")
public class OfferController {
    private final OfferService offerService;

    @PostMapping("add/offers")
    public void addNewOrder(@RequestBody OfferDTO offerDTO) {
        offerService.save(offerDTO);
    }

    @GetMapping("get_all_offers_by_order")
    public ResponseEntity<List<OfferDTO>> getAllSubServicesByServiceName
            (@RequestParam String orderTrackingNumber, @RequestParam String property, @RequestParam String direction) {
        List<OfferDTO> offerDTOList = offerService.findAllByOrder(orderTrackingNumber, property, direction);
        return ResponseEntity.ok().body(offerDTOList);
    }

    @GetMapping("get_confirmed_offer_by_order_tracking_number/{orderTrackingNumber}")
    public ResponseEntity<OfferDTO> getConfirmedOfferByOrderTrackingNumber(@PathVariable String orderTrackingNumber) {
        OfferDTO offerDTO = offerService.findByOrderAndConfirmed(orderTrackingNumber);
        return ResponseEntity.ok().body(offerDTO);
    }

    @GetMapping("get_offer_by_id")
    public ResponseEntity<OfferDTO> getOfferByOfferId(@Valid @RequestBody OfferIdDTO offerIdDTO) {
        OfferDTO offerDTO = offerService.findById(offerIdDTO);
        return ResponseEntity.ok().body(offerDTO);
    }
}
