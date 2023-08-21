package vn.lotusviet.hotelmgmt.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class FileUtil {

  private static final String DEFAULT_TMP_DIR_PREFIX = "lotuswstmp";

  private FileUtil() {}

  public static String getFileExtension(MultipartFile file) {
    String originFileName = file.getOriginalFilename();
    Objects.requireNonNull(originFileName, "Original file name should not be null");

    int extensionStartIndex = originFileName.lastIndexOf(".");
    return originFileName.substring(extensionStartIndex);
  }

  public static void doTaskWithTemporaryFileFromMultipart(
      final MultipartFile multipartFile, final Consumer<File> task) throws IOException {
    Objects.requireNonNull(multipartFile.getOriginalFilename());
    Objects.requireNonNull(task);

    File convertedFile = creatFileFromMultipart(multipartFile);

    try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
      fos.write(multipartFile.getBytes());

      task.accept(convertedFile);
    }

    Files.delete(convertedFile.toPath());
  }

  @NotNull
  private static File creatFileFromMultipart(MultipartFile multipartFile) throws IOException {
    String dir = Files.createTempDirectory(DEFAULT_TMP_DIR_PREFIX).toFile().getAbsolutePath();

    return new File(dir + File.separator + randomMultipartFilename(multipartFile));
  }

  public static <R> R doTaskWithTemporaryFileFromMultipart(
      final MultipartFile multipartFile, final Function<File, R> task) throws IOException {
    Objects.requireNonNull(multipartFile.getOriginalFilename());
    Objects.requireNonNull(task);

    File convertedFile = creatFileFromMultipart(multipartFile);

    R result;

    try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
      fos.write(multipartFile.getBytes());

      result = task.apply(convertedFile);
    }

    Files.delete(convertedFile.toPath());

    return result;
  }

  public static String randomMultipartFilename(MultipartFile multipartFile) {
    return multipartFile.getName() + SecurityUtil.Random.generateRandomAlphanumericString(5);
  }
}