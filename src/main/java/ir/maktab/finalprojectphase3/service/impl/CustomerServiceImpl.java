package ir.maktab.finalprojectphase3.service.impl;

import ir.maktab.finalprojectphase3.data.dao.CustomerDao;
import ir.maktab.finalprojectphase3.data.dao.ExpertDao;
import ir.maktab.finalprojectphase3.data.dao.OfferDao;
import ir.maktab.finalprojectphase3.data.dao.OrderDao;
import ir.maktab.finalprojectphase3.data.dto.*;
import ir.maktab.finalprojectphase3.data.enums.Role;
import ir.maktab.finalprojectphase3.data.model.Customer;
import ir.maktab.finalprojectphase3.data.model.Offer;
import ir.maktab.finalprojectphase3.data.model.OfferId;
import ir.maktab.finalprojectphase3.data.model.Wallet;
import ir.maktab.finalprojectphase3.exception.NotCorrectInputException;
import ir.maktab.finalprojectphase3.exception.NotFoundException;
import ir.maktab.finalprojectphase3.mapper.CustomerMapper;
import ir.maktab.finalprojectphase3.service.CustomerService;
import ir.maktab.finalprojectphase3.service.OfferService;
import ir.maktab.finalprojectphase3.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerDao customerDao;
    private final OrderDao orderDao;
    private final ExpertDao expertDao;
    private final OfferDao offerDao;
    private final OrderService orderService;
    private final OfferService offerService;


    @Override
    public void save(UserCreationDTO userCreationDTO) {
        Customer customer = CustomerMapper.INSTANCE.dtoToModel(userCreationDTO);
        customer.setUsername(customer.getEmail());
        customer.setRole(Role.CUSTOMER);
        customer.setWallet(new Wallet(100d));
        customerDao.save(customer);
    }

    @Override
    public UserDTO login(String username, String password) {
        Customer customer = customerDao.findByUsername(username)
                .filter(customer1 -> customer1.getPassword().equals(password))
                .orElseThrow(() -> new NotCorrectInputException
                        (String.format("the %s username or %s password is incorrect", username, password)));
        return CustomerMapper.INSTANCE.modelToDto(customer);
    }

    @Override
    public UserDTO findByUsername(String username) {
        Customer customer = customerDao.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("the customer %s dose not exist", username)));
        return CustomerMapper.INSTANCE.modelToDto(customer);
    }

    @Override
    public Page<UserDTO> findAllCustomer(Pageable pageable) {
        Page<Customer> customers = customerDao.findAll(pageable);
        return customers.map(CustomerMapper.INSTANCE::modelToDto);
    }

    @Override
    public Page<UserDTO> findCustomersUsingCriteriaSearch(CriteriaSearchDTO searchDTO, Pageable pageable) {
        Specification<Customer> specification = CustomerDao.selectCustomersByCriteria(searchDTO);
        Page<Customer> customers = customerDao.findAll(specification, pageable);
        return customers.map(CustomerMapper.INSTANCE::modelToDto);
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO, String customerUsername) {
        Customer customer = findCustomerByUsername(customerUsername);
        if (!customer.getPassword().equals(changePasswordDTO.getOldPassword()))
            throw new NotCorrectInputException("the password is not correct");
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword()))
            throw new NotCorrectInputException("the new password and confirm password are not same");
        customer.setPassword(changePasswordDTO.getNewPassword());
        customerDao.save(customer);
    }

    @Override
    public void confirmOffer(OfferIdDTO offerIdDTO) {
        OfferId offerId = convertOfferIdDtoToModel(offerIdDTO);
        Offer offer = offerDao.findById(offerId).orElseThrow();
        offerService.updateOfferAfterConfirmed(offer);
        orderService.updateOrderAfterOfferConfirmed(offer.getOfferId().getOrder(), offer.getOfferId().getExpert());
    }

    @Override
    public List<OrderDTO> findAllCustomerOrder(String userName) {
        Customer customer = findCustomerByUsername(userName);
        return orderService.findAllByCustomer(customer);
    }


    private Customer findCustomerByUsername(String username) {
        return customerDao.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("the customer %s dose not exist", username)));
    }

    private OfferId convertOfferIdDtoToModel(OfferIdDTO offerIdDTO) {
        return OfferId.builder()
                .order(orderDao.findByTrackingNumberAndDisable
                        (offerIdDTO.getOrderTrackingNumber(), false).orElseThrow())
                .expert(expertDao.findByUsernameAndDisable
                        (offerIdDTO.getExpertUserName(), false).orElseThrow())
                .build();
    }
}
