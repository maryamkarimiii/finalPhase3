package ir.maktab.finalprojectphase3.service;


import ir.maktab.finalprojectphase3.data.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService extends BaseService<UserCreationDTO> {
    UserDTO login(String username, String password);

    UserDTO findByUsername(String username);

    void changePassword(ChangePasswordDTO changePasswordDTO, String customerUsername);

    Page<UserDTO> findAllCustomer(Pageable pageable);

    Page<UserDTO> findCustomersUsingCriteriaSearch(CriteriaSearchDTO searchDTO, Pageable pageable);

    void confirmOffer(OfferIdDTO offerIdDTO);

    List<OrderDTO> findAllCustomerOrder(String userName);
}
