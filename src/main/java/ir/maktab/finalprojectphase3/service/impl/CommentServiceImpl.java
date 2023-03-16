package ir.maktab.finalprojectphase3.service.impl;

import ir.maktab.finalprojectphase3.data.dao.CommentDao;
import ir.maktab.finalprojectphase3.data.dao.CustomerDao;
import ir.maktab.finalprojectphase3.data.dao.ExpertDao;
import ir.maktab.finalprojectphase3.data.dto.CommentDTO;
import ir.maktab.finalprojectphase3.data.model.Comment;
import ir.maktab.finalprojectphase3.data.model.Expert;
import ir.maktab.finalprojectphase3.exception.NotFoundException;
import ir.maktab.finalprojectphase3.mapper.CommentMapper;
import ir.maktab.finalprojectphase3.service.CommentService;
import ir.maktab.finalprojectphase3.service.ExpertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentDao commentDao;
    private final CustomerDao customerDao;
    private final ExpertDao expertDao;
    private final ExpertService expertService;

    @Transactional
    @Override
    public void save(CommentDTO commentDTO) {
        Comment comment = convertCommentDtoToModel(commentDTO);
        commentDao.save(comment);
        expertService.calculateAndUpdateExpertScore(comment.getExpert());
    }

    @Override
    public Double averageOfExpertScore(Expert expert) {
        return commentDao.averageOfExpertScore(expert);
    }

    @Override
    public CommentDTO findById(Long id) {
        Comment comment = commentDao.findById(id).orElseThrow(() -> new NotFoundException("dose not exist"));
        return CommentMapper.INSTANCE.modelToDto(comment);
    }

    private Comment convertCommentDtoToModel(CommentDTO commentDTO) {
        Comment comment = CommentMapper.INSTANCE.dtoToModel(commentDTO);
        comment.setCustomer(customerDao.findByUsername(commentDTO.getCustomerUserName()).orElseThrow());
        comment.setExpert(expertDao.findByUsernameAndDisable(commentDTO.getExpertUserName(), false).orElseThrow());
        return comment;
    }
}
