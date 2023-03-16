package ir.maktab.finalprojectphase3.service;


import ir.maktab.finalprojectphase3.data.dto.*;
import ir.maktab.finalprojectphase3.data.model.Expert;
import ir.maktab.finalprojectphase3.data.model.Order;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public interface ExpertService extends BaseService<UserCreationDTO> {
    ExpertDTO login(String username, String password);

    boolean isExist(String username);

    ExpertDTO findActiveExpertByUsername(String username);

    ExpertDTO findDeActiveExpertByUsername(String username);

    void setExpertImage(String expertUsername, MultipartFile file) throws IOException;

    ByteArrayResource getExpertImage(String username);

    void changePassword(ChangePasswordDTO changePasswordDTO, String username);

    void calculateAndUpdateExpertScore(Expert expert);

    void confirmExpert(String expertUsername);

    void softDelete(String expertUserName);

    List<ExpertDTO> findAllWithNewExpertRegistrationStatus();

    Page<ExpertDTO> findExpertsUsingCriteriaSearch(CriteriaSearchDTO searchDTO, Pageable pageable);

    List<OrderDTO> findExpertsRelatedOrders(String expertUsername);

    void checkExpertOperation(Order order, LocalTime offerWorkTime);
}
