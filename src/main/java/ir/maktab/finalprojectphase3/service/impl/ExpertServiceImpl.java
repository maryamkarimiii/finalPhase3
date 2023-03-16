package ir.maktab.finalprojectphase3.service.impl;

import ir.maktab.finalprojectphase3.data.dao.ExpertDao;
import ir.maktab.finalprojectphase3.data.dto.*;
import ir.maktab.finalprojectphase3.data.enums.ExpertRegistrationStatus;
import ir.maktab.finalprojectphase3.data.enums.Role;
import ir.maktab.finalprojectphase3.data.model.Expert;
import ir.maktab.finalprojectphase3.data.model.Order;
import ir.maktab.finalprojectphase3.data.model.SubService;
import ir.maktab.finalprojectphase3.data.model.Wallet;
import ir.maktab.finalprojectphase3.exception.NotCorrectInputException;
import ir.maktab.finalprojectphase3.exception.NotFoundException;
import ir.maktab.finalprojectphase3.mapper.ExpertMapper;
import ir.maktab.finalprojectphase3.service.CommentService;
import ir.maktab.finalprojectphase3.service.ExpertService;
import ir.maktab.finalprojectphase3.service.OrderService;
import ir.maktab.finalprojectphase3.validation.ImageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ExpertServiceImpl implements ExpertService {
    private final ExpertDao expertDao;
    private final OrderService orderService;
    @Lazy
    @Autowired()
    private CommentService commentService;

    @Override
    public void save(UserCreationDTO userCreationDTO) {
        Expert expert = ExpertMapper.INSTANCE.dtoToModel(userCreationDTO);
        expert.setUsername(expert.getEmail());
        expert.setRole(Role.EXPERT);
        expert.setExpertRegistrationStatus(ExpertRegistrationStatus.NEW);
        expert.setTotalScore(0.0);
        expert.setWallet(new Wallet(100d));
        expertDao.save(expert);
    }

    @Override
    public boolean isExist(String username) {
        return expertDao.existsByUsername(username);
    }

    @Override
    public ExpertDTO login(String username, String password) {
        Expert expert = expertDao.findByUsernameAndDisable(username, false)
                .filter(expert1 -> expert1.getPassword().equals(password))
                .filter(expert1 -> expert1.getExpertRegistrationStatus().equals(ExpertRegistrationStatus.CONFIRMED))
                .orElseThrow(() -> new NotCorrectInputException
                        (String.format("the %s username or %s password is incorrect,or you not confirm", username, password)));
        return convertExpertToExpertDto(expert);
    }

    @Override
    public ExpertDTO findActiveExpertByUsername(String username) {
        Expert expert = expertDao.findByUsernameAndDisable(username, false)
                .orElseThrow(() -> new NotFoundException(String.format("the username %s do not exist", username)));
        return convertExpertToExpertDto(expert);
    }

    @Override
    public ExpertDTO findDeActiveExpertByUsername(String username) {
        Expert expert = expertDao.findByUsernameAndDisable(username, true)
                .orElseThrow(() -> new NotFoundException(String.format("the username %s dose not exist", username)));
        return convertExpertToExpertDto(expert);
    }

    @Override
    public List<ExpertDTO> findAllWithNewExpertRegistrationStatus() {
        List<Expert> expertList = expertDao.findAllByExpertRegistrationStatus(ExpertRegistrationStatus.NEW);
        return ExpertMapper.INSTANCE.expertListsToDto(expertList);
    }

    @Override
    public Page<ExpertDTO> findExpertsUsingCriteriaSearch(CriteriaSearchDTO searchDTO, Pageable pageable) {
        Specification<Expert> specification = ExpertDao.selectExpertsByCriteria(searchDTO);
        Page<Expert> experts = expertDao.findAll(specification, pageable);
        return experts.map(ExpertMapper.INSTANCE::modelToDto);
    }

    @Override
    public void confirmExpert(String username) {
        Expert expert = findExpertByUserName(username);
        expert.setExpertRegistrationStatus(ExpertRegistrationStatus.CONFIRMED);
        expertDao.save(expert);
    }

    @Override
    public List<OrderDTO> findExpertsRelatedOrders(String username) {
        Set<SubService> subServiceSet = findExpertByUserName(username).getSubServiceSet();
        return orderService.findAllBySubServiceAndOrderStatus(subServiceSet.stream().toList());
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO, String username) {
        Expert expert = findExpertByUserName(username);
        if (!expert.getPassword().equals(changePasswordDTO.getOldPassword()))
            throw new NotCorrectInputException("the password is not correct");
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword()))
            throw new NotCorrectInputException("the new password and confirm password are not same");
        expert.setPassword(changePasswordDTO.getNewPassword());
        expertDao.save(expert);
    }

    @Override
    public void calculateAndUpdateExpertScore(Expert expert) {
        Double averageOfExpertScore = commentService.averageOfExpertScore(expert);
        expert.setTotalScore(averageOfExpertScore);
        expertDao.save(expert);
    }

    @Override
    public void softDelete(String userName) {
        Expert expert = findExpertByUserName(userName);
        expert.setDisable(true);
        expertDao.save(expert);
    }

    @Override
    public void setExpertImage(String expertUsername, MultipartFile file) throws IOException {
        validateImage(file);
        String imageToBase64 = encodeImageToBase64(file);
        Expert expert = findExpertByUserName(expertUsername);
        expert.setImage(imageToBase64);
        expertDao.save(expert);
    }

    @Override
    public ByteArrayResource getExpertImage(String username) {
        Expert expert = findExpertByUserName(username);
        String image = expert.getImage();
        byte[] bytes = decodeImageFromBase64ToByte(image);
        return new ByteArrayResource(bytes);
    }

    @Override
    public void checkExpertOperation(Order order, LocalTime offerWorkTime) {
        LocalTime durationOfTwoLocalTimes = getDurationOfTwoLocalTimes(order.getStartWorkingTime(), order.getFinishWorkingTime());
        LocalTime localTime = getDurationOfTwoLocalTimes(offerWorkTime, durationOfTwoLocalTimes);
        int hour = localTime.getHour();
        if (hour >= 1)
            getExpertMinusPoint(order.getExpert(), hour);
    }

    private void getExpertMinusPoint(Expert expert, int minusPoint) {
        Double totalScore = expert.getTotalScore();
        expert.setTotalScore(totalScore - minusPoint);
        if (expert.getTotalScore() < 0)
            expert.setDisable(true);
        expertDao.save(expert);
    }

    private LocalTime getDurationOfTwoLocalTimes(LocalTime localTime1, LocalTime localTime2) {
        Duration between = Duration.between(localTime1, localTime2);
        Duration duration = Duration.parse(between.toString());
        return LocalTime.MIDNIGHT.plus(duration);
    }

    private ExpertDTO convertExpertToExpertDto(Expert expert) {
        return ExpertMapper.INSTANCE.modelToDto(expert);
    }

    private Expert findExpertByUserName(String username) {
        return expertDao.findByUsernameAndDisable(username, false)
                .orElseThrow(() -> new NotFoundException(String.format("the username %s dose not exist", username)));
    }

    private String encodeImageToBase64(MultipartFile file) throws IOException {
        return Base64.getEncoder().encodeToString(file.getBytes());
    }

    private byte[] decodeImageFromBase64ToByte(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }

    private void validateImage(MultipartFile file) {
        ImageValidator.validateFileExistence(file);
        ImageValidator.validateExtension(file);
        ImageValidator.validateFileSize(file);
    }
}
