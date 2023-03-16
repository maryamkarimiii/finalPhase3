package ir.maktab.finalprojectphase3.service;


import ir.maktab.finalprojectphase3.data.dto.ServiceDTO;

import java.util.List;

public interface ServiceService extends BaseService<ServiceDTO> {

    ServiceDTO findEnableServiceByName(String serviceName);

    ServiceDTO findDisableServiceByName(String serviceName);

    List<ServiceDTO> findAllEnableService();

    List<ServiceDTO> findAllDisableService();

    boolean isExist(String serviceName);

    void activeDisableService(String serviceName);

    void softDelete(String serviceName);
}
