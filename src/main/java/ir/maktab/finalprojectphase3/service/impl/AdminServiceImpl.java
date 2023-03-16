package ir.maktab.finalprojectphase3.service.impl;


import ir.maktab.finalprojectphase3.data.dao.AdminDao;
import ir.maktab.finalprojectphase3.data.dao.ExpertDao;
import ir.maktab.finalprojectphase3.data.dao.SubServiceDao;
import ir.maktab.finalprojectphase3.data.dto.AdminDTO;
import ir.maktab.finalprojectphase3.data.dto.CriteriaSearchDTO;
import ir.maktab.finalprojectphase3.data.enums.Role;
import ir.maktab.finalprojectphase3.data.model.Admin;
import ir.maktab.finalprojectphase3.data.model.Expert;
import ir.maktab.finalprojectphase3.data.model.SubService;
import ir.maktab.finalprojectphase3.exception.NotFoundException;
import ir.maktab.finalprojectphase3.exception.ValidationException;
import ir.maktab.finalprojectphase3.mapper.AdminMapper;
import ir.maktab.finalprojectphase3.service.AdminService;
import ir.maktab.finalprojectphase3.service.CustomerService;
import ir.maktab.finalprojectphase3.service.ExpertService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminDao adminDao;
    private final SubServiceDao subServiceDao;
    private final ExpertDao expertDao;
    private final ExpertService expertService;
    private final CustomerService customerService;

    @Override
    public void save(AdminDTO adminDTO) {
        Admin admin = AdminMapper.INSTANCE.dtoToModel(adminDTO);
        admin.setRole(Role.ADMIN);
        adminDao.save(admin);
    }

    @Override
    public boolean isExistByUsername(String username) {
        return adminDao.existsByUsername(username);
    }

    @Override
    public AdminDTO getAdminByUsername(String username) {
        Admin admin = adminDao.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("the customer %s dose not exist", username)));
        return AdminMapper.INSTANCE.modelToDto(admin);
    }

    @Override
    @Transactional
    public void addExpertToSubService(String expertUsername, String subServiceName) {
        if (!expertDao.existsByUsername(expertUsername))
            throw new NotFoundException("the expert dose not exist");
        if (!subServiceDao.existsByName(subServiceName))
            throw new NotFoundException("the subService dose not exist");
        Expert expert = expertDao.findByUsernameAndDisable(expertUsername, false).orElseThrow();
        SubService subService = subServiceDao.findByNameAndDisable(subServiceName, false).orElseThrow();
        subService.getExpertSet().add(expert);
        subServiceDao.save(subService);
    }

    @Override
    @Transactional
    public void deleteExpertFromSubService(String expertUsername, String subServiceName) {
        SubService subService = subServiceDao.findByNameAndDisable(subServiceName, false).orElseThrow();
        boolean result = subService.getExpertSet().removeIf(expert -> expert.getUsername().equals(expertUsername));
        if (!result)
            throw new NotFoundException
                    (String.format("the expert with this username %s dose not exist in subService", expertUsername));

        subServiceDao.save(subService);
    }

    @Override
    public Page<?> findUsersUsingCriteriaSearch(CriteriaSearchDTO searchDTO, Pageable pageable) {
        if (searchDTO.getRole() == null)
            throw new ValidationException("the role can not be null");
        if (searchDTO.getRole().equals(Role.EXPERT))
            return expertService.findExpertsUsingCriteriaSearch(searchDTO, pageable);
        return customerService.findCustomersUsingCriteriaSearch(searchDTO, pageable);
    }
}
