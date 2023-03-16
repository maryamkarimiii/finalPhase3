package ir.maktab.finalprojectphase3.controller;

import ir.maktab.finalprojectphase3.data.dto.*;
import ir.maktab.finalprojectphase3.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("add/customers")
    public void addNewCustomer(@Valid @RequestBody UserCreationDTO userCreationDTO) {
        customerService.save(userCreationDTO);
    }

    @GetMapping("customers_log_in")
    public ResponseEntity<UserDTO> logIn(@RequestParam String username, @RequestParam String password) {
        UserDTO userDTO = customerService.login(username, password);
        return ResponseEntity.ok().body(userDTO);
    }

    @GetMapping("get_customers/{username}")
    public ResponseEntity<UserDTO> getCustomerByUsername(@PathVariable String username) {
        UserDTO userDTO = customerService.findByUsername(username);
        return ResponseEntity.ok().body(userDTO);
    }

    @GetMapping("get_all_customers")
    public ResponseEntity<Page<UserDTO>> getAllCustomer(@PageableDefault Pageable pageable) {
        Page<UserDTO> allCustomer = customerService.findAllCustomer(pageable);
        return ResponseEntity.ok().body(allCustomer);
    }

    @GetMapping("get_all_customers_criteria_search")
    public ResponseEntity<Page<UserDTO>> getAllCustomersUsingCriteriaSearch
            (@Valid @RequestBody CriteriaSearchDTO searchDTO, @PageableDefault Pageable pageable) {
        Page<UserDTO> allCustomer = customerService.findCustomersUsingCriteriaSearch(searchDTO, pageable);
        return ResponseEntity.ok().body(allCustomer);
    }

    @PutMapping("change_password/{username}")
    public void changeCustomerPassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO, @PathVariable String username) {
        customerService.changePassword(changePasswordDTO, username);
    }

    @PutMapping("confirm_offers")
    public void confirmOfferByCustomer(@Valid @RequestBody OfferIdDTO offerIdDTO) {
        customerService.confirmOffer(offerIdDTO);
    }
}
