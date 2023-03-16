package ir.maktab.finalprojectphase3.service;

import ir.maktab.finalprojectphase3.data.dto.CommentDTO;
import ir.maktab.finalprojectphase3.data.model.Expert;

public interface CommentService extends BaseService<CommentDTO> {
    Double averageOfExpertScore(Expert expert);

    CommentDTO findById(Long id);
}
