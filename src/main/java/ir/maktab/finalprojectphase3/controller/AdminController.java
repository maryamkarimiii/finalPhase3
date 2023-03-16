package ir.maktab.finalprojectphase3.controller;

import ir.maktab.finalprojectphase3.data.dto.AdminDTO;
import ir.maktab.finalprojectphase3.data.dto.CriteriaSearchDTO;
import ir.maktab.finalprojectphase3.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("add/admins")
    public void addNewAdmin(@Valid @RequestBody AdminDTO adminDTO) {
        adminService.save(adminDTO);
    }

    @GetMapping("get_admins/{username}")
    public ResponseEntity<AdminDTO> getAdminByUsername(@PathVariable String username) {
        AdminDTO adminDTO = adminService.getAdminByUsername(username);
        return ResponseEntity.ok().body(adminDTO);
    }

    @PutMapping("add_experts_to_sub_services")
    public void addExpertToSubService(@RequestParam String expertUsername, @RequestParam String subServiceName) {
        adminService.addExpertToSubService(expertUsername, subServiceName);
    }

    @PutMapping("delete_experts_from_sub_services")
    public void deleteExpertFromSubService(@RequestParam String expertUsername, @RequestParam String subServiceName) {
        adminService.deleteExpertFromSubService(expertUsername, subServiceName);
    }

    @GetMapping("get_users_criteria_search")
    public ResponseEntity<Page<?>> getUsersUsingCriteriaSearch
            (@Valid @RequestBody CriteriaSearchDTO searchDTO, @PageableDefault Pageable pageable) {
        Page<?> criteriaSearch = adminService.findUsersUsingCriteriaSearch(searchDTO, pageable);
        return ResponseEntity.ok().body(criteriaSearch);
    }

}
