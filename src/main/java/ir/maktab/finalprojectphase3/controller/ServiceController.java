package ir.maktab.finalprojectphase3.controller;


import ir.maktab.finalprojectphase3.data.dto.ServiceDTO;
import ir.maktab.finalprojectphase3.service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
@Validated
public class ServiceController {
    private final ServiceService serviceService;

    @PostMapping("add/services")
    public void addNewService(@Valid @RequestBody ServiceDTO serviceDTO) {
        serviceService.save(serviceDTO);
    }

    @GetMapping("get_enable_services/{serviceName}")
    public ResponseEntity<ServiceDTO> getEnableServiceByName(@PathVariable String serviceName) {
        ServiceDTO serviceDTO = serviceService.findEnableServiceByName(serviceName);
        return ResponseEntity.ok().body(serviceDTO);
    }

    @GetMapping("get_disable_services/{serviceName}")
    public ResponseEntity<ServiceDTO> getDisableServiceByName(@PathVariable String serviceName) {
        ServiceDTO serviceDTO = serviceService.findDisableServiceByName(serviceName);
        return ResponseEntity.ok().body(serviceDTO);
    }

    @GetMapping("get_all_enable_services")
    public ResponseEntity<List<ServiceDTO>> getAllEnableServices() {
        List<ServiceDTO> serviceList = serviceService.findAllEnableService();
        return ResponseEntity.ok().body(serviceList);
    }

    @GetMapping("get_all_disable_services")
    public ResponseEntity<List<ServiceDTO>> getAllDisableServices() {
        List<ServiceDTO> serviceList = serviceService.findAllDisableService();
        return ResponseEntity.ok().body(serviceList);
    }

    @PatchMapping("active_disable_services/{serviceName}")
    public void activeDisableService(@PathVariable String serviceName) {
        serviceService.activeDisableService(serviceName);
    }

    @PatchMapping("delete_services/{serviceName}")
    public void deleteService(@PathVariable String serviceName) {
        serviceService.softDelete(serviceName);
    }

}
