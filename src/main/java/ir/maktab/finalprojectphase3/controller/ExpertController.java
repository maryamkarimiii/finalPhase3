package ir.maktab.finalprojectphase3.controller;

import ir.maktab.finalprojectphase3.data.dto.*;
import ir.maktab.finalprojectphase3.exception.NotCorrectInputException;
import ir.maktab.finalprojectphase3.service.ExpertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/expert")
@RequiredArgsConstructor
@Validated
public class ExpertController {
    private final ExpertService expertService;

    @PostMapping("add/experts")
    public void addNewExperts(@Valid @RequestBody UserCreationDTO userCreationDTO) {
        expertService.save(userCreationDTO);
    }

    @PostMapping("upLode_image/{username}")
    public void upLodeImage(@PathVariable String username, @RequestParam MultipartFile file) {
        try {
            expertService.setExpertImage(username, file);
        } catch (IOException e) {
            throw new NotCorrectInputException("some problem happened in uploading");
        }
    }

    @GetMapping("get_image/{username}")
    public ResponseEntity<ByteArrayResource> getExpertImage(@PathVariable String username) {
        ByteArrayResource image = expertService.getExpertImage(username);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @GetMapping("experts_log_in")
    public ResponseEntity<ExpertDTO> logIn(@RequestParam String username, @RequestParam String password) {
        ExpertDTO expertDTO = expertService.login(username, password);
        return ResponseEntity.ok().body(expertDTO);
    }

    @GetMapping("get_active_experts/{username}")
    public ResponseEntity<ExpertDTO> getActiveExpertByUsername(@PathVariable String username) {
        ExpertDTO expertDTO = expertService.findActiveExpertByUsername(username);
        return ResponseEntity.ok().body(expertDTO);
    }

    @GetMapping("get_deActive_experts/{username}")
    public ResponseEntity<ExpertDTO> getDeActiveExpertByUsername(@PathVariable String username) {
        ExpertDTO expertDTO = expertService.findDeActiveExpertByUsername(username);
        return ResponseEntity.ok().body(expertDTO);
    }

    @GetMapping("get_all_experts_with_new_expert_registration_status")
    public ResponseEntity<List<ExpertDTO>> getExpertsWithNewExpertRegistrationStatus() {
        List<ExpertDTO> expertDTOList = expertService.findAllWithNewExpertRegistrationStatus();
        return ResponseEntity.ok().body(expertDTOList);
    }

    @GetMapping("get_all_experts_criteria_search")
    public ResponseEntity<Page<ExpertDTO>> getAllExpertsUsingCriteriaSearch
            (@Valid @RequestBody CriteriaSearchDTO searchDTO, @PageableDefault Pageable pageable) {
        Page<ExpertDTO> expertDTOPage = expertService.findExpertsUsingCriteriaSearch(searchDTO, pageable);
        return ResponseEntity.ok().body(expertDTOPage);
    }

    @PatchMapping("confirm_experts/{username}")
    public void confirmExpert(@PathVariable String username) {
        expertService.confirmExpert(username);
    }

    @GetMapping("get_all_experts_related_orders/{username}")
    public ResponseEntity<List<OrderDTO>> getExpertsRelatedOrders(@PathVariable String username) {
        List<OrderDTO> orderDTOList = expertService.findExpertsRelatedOrders(username);
        return ResponseEntity.ok().body(orderDTOList);
    }

    @PutMapping("change_password/{username}")
    public void changeExpertPassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO, @PathVariable String username) {
        expertService.changePassword(changePasswordDTO, username);
    }

    @PatchMapping("delete_experts/{username}")
    public void deleteExpert(@PathVariable String username) {
        expertService.softDelete(username);
    }
}
