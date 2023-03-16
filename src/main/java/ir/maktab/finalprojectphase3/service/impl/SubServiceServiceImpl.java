package ir.maktab.finalprojectphase3.service.impl;

import ir.maktab.finalprojectphase3.data.dao.ServiceDao;
import ir.maktab.finalprojectphase3.data.dao.SubServiceDao;
import ir.maktab.finalprojectphase3.data.dto.ExpertDTO;
import ir.maktab.finalprojectphase3.data.dto.SubServiceDTO;
import ir.maktab.finalprojectphase3.data.model.SubService;
import ir.maktab.finalprojectphase3.exception.NotFoundException;
import ir.maktab.finalprojectphase3.mapper.ExpertMapper;
import ir.maktab.finalprojectphase3.mapper.SubServiceMapper;
import ir.maktab.finalprojectphase3.service.SubServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class SubServiceServiceImpl implements SubServiceService {
    private final SubServiceDao subServiceDao;
    private final ServiceDao serviceDao;
    private static final String ERROR_TEXT = "the %s subService dose not exist";


    @Override
    public void save(SubServiceDTO subServiceDTO) {
        SubService subService = convertSubServiceDtoToModel(subServiceDTO);
        subServiceDao.save(subService);
    }

    @Override
    public SubServiceDTO findEnableSubServiceByName(String subServiceName) {
        SubService enableSubService = findEnableSubServicesByName(subServiceName);
        return SubServiceMapper.INSTANCE.modelToDto(enableSubService);
    }

    @Override
    public SubServiceDTO findDisableSubServiceByName(String subServiceName) {
        SubService disableSubService = findDisableSubServicesByName(subServiceName);
        return SubServiceMapper.INSTANCE.modelToDto(disableSubService);
    }

    @Override
    public List<SubServiceDTO> findAllByServiceName(String serviceName) {
        return SubServiceMapper.INSTANCE.subServiceListToDto(subServiceDao.findAllByServiceNameAndDisableFalse(serviceName));
    }

    @Override
    public Map<String, List<SubServiceDTO>> findAllEnableSubService() {
        return SubServiceMapper.INSTANCE.subServiceListToDto(subServiceDao.findAllByDisable(false)).stream()
                .collect(Collectors.groupingBy(SubServiceDTO::getServiceName));
    }

    @Override
    public Map<String, List<SubServiceDTO>> findAllDisableSubService() {
        return SubServiceMapper.INSTANCE.subServiceListToDto(subServiceDao.findAllByDisable(true)).stream()
                .collect(Collectors.groupingBy(SubServiceDTO::getServiceName));
    }

    @Override
    public boolean isExist(String subServiceName) {
        return subServiceDao.existsByName(subServiceName);
    }

    @Override
    public void updateSubServicePrice(String subServiceName, double newPrice) {
        SubService subService = findEnableSubServicesByName(subServiceName);
        subService.setBaseAmount(newPrice);
        subServiceDao.save(subService);
    }

    @Override
    public void updateSubServiceDescription(String subServiceName, String newDescription) {
        SubService subService = findEnableSubServicesByName(subServiceName);
        subService.setDescription(newDescription);
        subServiceDao.save(subService);
    }

    @Override
    public List<ExpertDTO> findSubServiceExpertsBySubServiceName(String subServiceName) {
        return findEnableSubServicesByName(subServiceName).getExpertSet().stream()
                .map(ExpertMapper.INSTANCE::modelToDto)
                .toList();
    }

    @Override
    public void softDelete(String subServiceName) {
        SubService subService = findEnableSubServicesByName(subServiceName);
        subService.setDisable(true);
        subServiceDao.save(subService);
    }

    @Override
    public void activeDisableSubService(String subServiceName) {
        SubService subService = findDisableSubServicesByName(subServiceName);
        subService.setDisable(false);
        subServiceDao.save(subService);
    }

    protected SubService findEnableSubServicesByName(String subServiceName) {
        return subServiceDao.findByNameAndDisable(subServiceName, false)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_TEXT, subServiceName)));
    }

    private SubService findDisableSubServicesByName(String subServiceName) {
        return subServiceDao.findByNameAndDisable(subServiceName, true)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_TEXT, subServiceName)));
    }

    private SubService convertSubServiceDtoToModel(SubServiceDTO subServiceDTO) {
        SubService subService = SubServiceMapper.INSTANCE.dtoToModel(subServiceDTO);
        subService.setService(serviceDao.findByNameAndDisable(subServiceDTO.getServiceName(), false).orElseThrow());
        return subService;
    }


}
