package com.example.springemployee.service.storage;

import com.example.springemployee.service.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation = Paths.get("upload/");

    @Override
    public String storeAdd(MultipartFile file) {
        try {
//            if (file.isEmpty()) {
//                throw new StorageException("Failed to store empty file." + file.getOriginalFilename());
//            }
//            if (!isImageOrPdfFile(file)){
//                throw new StorageException("Only save image or pdf files.");
//            }
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-","");
            generatedFileName = generatedFileName + "." + fileExtension;
            Files.copy(file.getInputStream(),this.rootLocation.resolve(generatedFileName).normalize().toAbsolutePath(),
                    StandardCopyOption.REPLACE_EXISTING);
            return generatedFileName;
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public MultipartFile convertStringToFile(String nameFile) {
        File file =new File("upload/" + nameFile);
        try (FileInputStream inputStream = new FileInputStream(file)){
            if (!file.isFile()) {
                throw new StorageException("Failed to store empty file " + file.getAbsolutePath());
            }
            MultipartFile multipartFile = new MockMultipartFile("file",file.getName(),
                    "text/plain", IOUtils.toByteArray(inputStream));
            return multipartFile;
        }catch (Exception e){
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public boolean isImageOrPdfFile(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[]{"jpg","png","jpeg","pdf","bmp"}).contains(fileExtension.trim().toLowerCase(Locale.ROOT));
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
