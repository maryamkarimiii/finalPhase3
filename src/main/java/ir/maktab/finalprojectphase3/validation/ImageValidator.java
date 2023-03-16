package ir.maktab.finalprojectphase3.validation;

import ir.maktab.finalprojectphase3.exception.ValidationException;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

@UtilityClass
public class ImageValidator {
    private static final Integer MAXIMUM_FILE_SIZE_ALLOWED = 300;

    public void validateFileExistence(MultipartFile file) {
        if (file.isEmpty())
            throw new ValidationException("the file is empty");
    }

    public void validateExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!"jpeg".equals(extension) && !"jpg".equals(extension)) {
            throw new ValidationException("Only jpg/jpeg files are accepted");
        }
    }

    public void validateFileSize(MultipartFile file) {
        if (file.getSize() / 1024 >= MAXIMUM_FILE_SIZE_ALLOWED) {
            throw new ValidationException("File size cannot be greater than 300 kb");
        }
    }
}
