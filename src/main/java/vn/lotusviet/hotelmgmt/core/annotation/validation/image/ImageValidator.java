package vn.lotusviet.hotelmgmt.core.annotation.validation.image;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

  private static final List<String> SUPPORTED_TYPES =
      List.of("image/png", "image/jpg", "image/jpeg");

  @Override
  public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
    return SUPPORTED_TYPES.contains(value.getContentType());
  }
}