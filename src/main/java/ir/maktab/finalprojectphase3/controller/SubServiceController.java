package ir.maktab.finalprojectphase3.controller;

import ir.maktab.finalprojectphase3.data.dto.ExpertDTO;
import ir.maktab.finalprojectphase3.data.dto.SubServiceDTO;
import ir.maktab.finalprojectphase3.service.SubServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sub_service")
public class SubServiceController {
    private final SubServiceService subServiceService;

    @PostMapping("add/sub_services")
    public void addNewSubService(@Valid @RequestBody SubServiceDTO subServiceDTO) {
        subServiceService.save(subServiceDTO);
    }

    @GetMapping("get_enable_sub_services/{subServiceName}")
    public ResponseEntity<SubServiceDTO> getEnableSubServiceByName(@PathVariable String subServiceName) {
        SubServiceDTO subServiceDTO = subServiceService.findEnableSubServiceByName(subServiceName);
        return ResponseEntity.ok().body(subServiceDTO);
    }

    @GetMapping("get_disable_sub_services/{subServiceName}")
    public ResponseEntity<SubServiceDTO> getDisableSubServiceByName(@PathVariable String subServiceName) {
        SubServiceDTO subServiceDTO = subServiceService.findDisableSubServiceByName(subServiceName);
        return ResponseEntity.ok().body(subServiceDTO);
    }

    @GetMapping("get_all_sub_services_by_service_name/{serviceName}")
    public ResponseEntity<List<SubServiceDTO>> getAllSubServicesByServiceName(@PathVariable String serviceName) {
        List<SubServiceDTO> subServiceDTOList = subServiceService.findAllByServiceName(serviceName);
        return ResponseEntity.ok().body(subServiceDTOList);
    }

    @GetMapping("get_all_enable_sub_services")
    public ResponseEntity<Map<String, List<SubServiceDTO>>> getAllEnableSubServices() {
        Map<String, List<SubServiceDTO>> dtoListMap = subServiceService.findAllEnableSubService();
        return ResponseEntity.ok().body(dtoListMap);
    }

    @GetMapping("get_all_disable_sub_services")
    public ResponseEntity<Map<String, List<SubServiceDTO>>> getAllDisableSubServices() {
        Map<String, List<SubServiceDTO>> dtoListMap = subServiceService.findAllDisableSubService();
        return ResponseEntity.ok().body(dtoListMap);
    }

    @GetMapping("get_experts_of_sub_services/{subServiceName}")
    public ResponseEntity<List<ExpertDTO>> getSubServiceExperts(@PathVariable String subServiceName) {
        List<ExpertDTO> expertDTOList = subServiceService.findSubServiceExpertsBySubServiceName(subServiceName);
        return ResponseEntity.ok().body(expertDTOList);
    }

    @PatchMapping("update_price_sub_services")
    public void updateSubServicePrice(@RequestParam String subServiceName, @RequestParam double newPrice) {
        subServiceService.updateSubServicePrice(subServiceName, newPrice);
    }

    @PatchMapping("update_description_sub_services")
    public void updateSubServiceDescription(@RequestParam String subServiceName, @RequestParam String newDescription) {
        subServiceService.updateSubServiceDescription(subServiceName, newDescription);
    }

    @PatchMapping("delete_sub_services/{subServiceName}")
    public void deleteSubService(@PathVariable String subServiceName) {
        subServiceService.softDelete(subServiceName);
    }

    @PatchMapping("active_disable_services/{subServiceName}")
    public void activeDisableSubService(@PathVariable String subServiceName) {
        subServiceService.activeDisableSubService(subServiceName);
    }
}
