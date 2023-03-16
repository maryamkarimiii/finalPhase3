package ir.maktab.finalprojectphase3.service;


import ir.maktab.finalprojectphase3.data.dto.AdminDTO;
import ir.maktab.finalprojectphase3.data.dto.CriteriaSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService extends BaseService<AdminDTO> {

    boolean isExistByUsername(String username);

    AdminDTO getAdminByUsername(String username);

    void addExpertToSubService(String expertUsername, String subServiceName);

    void deleteExpertFromSubService(String expertUsername, String subServiceName);

    Page<?> findUsersUsingCriteriaSearch(CriteriaSearchDTO searchDTO, Pageable pageable);
}
