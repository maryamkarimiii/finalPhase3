package ir.maktab.finalprojectphase3.service.impl;

import ir.maktab.finalprojectphase3.data.dao.ServiceDao;
import ir.maktab.finalprojectphase3.data.dto.ServiceDTO;
import ir.maktab.finalprojectphase3.data.model.Service;
import ir.maktab.finalprojectphase3.exception.NotFoundException;
import ir.maktab.finalprojectphase3.mapper.ServiceMapper;
import ir.maktab.finalprojectphase3.service.ServiceService;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServicesServiceImpl implements ServiceService {
    private final ServiceDao serviceDao;

    @Override
    public void save(ServiceDTO serviceDTO) {
        Service service = ServiceMapper.INSTANCE.dtoToModel(serviceDTO);
        serviceDao.save(service);
    }

    @Override
    public ServiceDTO findEnableServiceByName(String serviceName) {
        Service service = findEnableServicesByName(serviceName);
        return ServiceMapper.INSTANCE.modelToDto(service);
    }

    @Override
    public ServiceDTO findDisableServiceByName(String serviceName) {
        Service service = findDisableServicesByName(serviceName);
        return ServiceMapper.INSTANCE.modelToDto(service);
    }

    @Override
    public void activeDisableService(String serviceName) {
        Service service = findDisableServicesByName(serviceName);
        service.setDisable(false);
        serviceDao.save(service);
    }

    @Override
    public List<ServiceDTO> findAllEnableService() {
        return serviceDao.findAllByDisable(false).stream()
                .map(ServiceMapper.INSTANCE::modelToDto)
                .sorted(Comparator.comparing(ServiceDTO::getName))
                .toList();
    }

    @Override
    public List<ServiceDTO> findAllDisableService() {
        return serviceDao.findAllByDisable(true).stream()
                .map(ServiceMapper.INSTANCE::modelToDto)
                .sorted(Comparator.comparing(ServiceDTO::getName))
                .toList();
    }


    @Override
    public boolean isExist(String serviceName) {
        return serviceDao.existsByName(serviceName);
    }

    @Override
    public void softDelete(String serviceName) {
        Service service = findEnableServicesByName(serviceName);
        service.setDisable(true);
        serviceDao.save(service);
    }

    private Service findEnableServicesByName(String serviceName) {
        return serviceDao.findByNameAndDisable(serviceName, false)
                .orElseThrow(() -> new NotFoundException(String.format("the %s service dose not exist", serviceName)));
    }

    private Service findDisableServicesByName(String serviceName) {
        return serviceDao.findByNameAndDisable(serviceName, true)
                .orElseThrow(() -> new NotFoundException(String.format("the %s service dose not exist", serviceName)));
    }
}
