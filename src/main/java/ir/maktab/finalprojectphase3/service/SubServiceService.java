package ir.maktab.finalprojectphase3.service;


import ir.maktab.finalprojectphase3.data.dto.ExpertDTO;
import ir.maktab.finalprojectphase3.data.dto.SubServiceDTO;

import java.util.List;
import java.util.Map;

public interface SubServiceService extends BaseService<SubServiceDTO> {

    SubServiceDTO findEnableSubServiceByName(String subServiceName);
    SubServiceDTO findDisableSubServiceByName(String subServiceName);

    List<SubServiceDTO> findAllByServiceName(String serviceName);

    Map<String, List<SubServiceDTO>> findAllEnableSubService();

    Map<String, List<SubServiceDTO>> findAllDisableSubService();

    List<ExpertDTO> findSubServiceExpertsBySubServiceName(String subServiceName);

    boolean isExist(String subServiceName);

    void updateSubServicePrice(String subServiceName, double newPrice);

    void updateSubServiceDescription(String subServiceName, String newDescription);


    void softDelete(String subServiceName);

    void activeDisableSubService(String subServiceName);

}
