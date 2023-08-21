package vn.lotusviet.hotelmgmt.service.storage;

import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Objects;

public interface StorageService {

  /**
   * Handle upload single file asynchronously
   *
   * @return object url
   */
  String saveFile(MultipartFile multipartFile, FileEntry entry);

  void deleteFiles(Collection<String> keys);

  String getFileURL(@NonNull String key);

  enum FileBrand {
    UPLOAD_IMG
  }

  enum FileCategory {
    SUITE,
    USER,
    PROMOTION_PROGRAM
  }

  class FileEntry {
    private final String filename;
    private final FileBrand brand;
    private final FileCategory category;

    public FileEntry(String filename, FileBrand brand, FileCategory category) {
      this.filename = filename;
      this.brand = brand;
      this.category = category;
    }

    public String getFilename() {
      return filename;
    }

    public FileBrand getBrand() {
      return brand;
    }

    public FileCategory getCategory() {
      return category;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof FileEntry)) return false;
      FileEntry fileEntry = (FileEntry) o;
      return getFilename().equals(fileEntry.getFilename());
    }

    @Override
    public int hashCode() {
      return Objects.hash(getFilename());
    }

    @Override
    public String toString() {
      return "FileEntry{"
          + "filename='"
          + filename
          + '\''
          + ", brand="
          + brand
          + ", category="
          + category
          + '}';
    }
  }
}