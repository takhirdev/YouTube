package you_tube_own.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import you_tube_own.dto.AttachDto;
import you_tube_own.entity.AttachEntity;
import you_tube_own.exception.AppBadException;
import you_tube_own.repository.AttachRepository;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachService {
    private final AttachRepository attachRepository;

    @Value("${server.url}")
    private String serverUrl;
    @Value("${upload.path}")
    private String uploadPath;

    public AttachDto upload(MultipartFile file) {
        try {
            String key = UUID.randomUUID().toString();                     // aaaa-bbbb-ccc-dddd
            String extension = getExtension(file.getOriginalFilename());   // png/mp3/doc
            String pathFolder = getYMDString();                            // 2024/6/11

            File folder = new File(uploadPath + pathFolder);      //"C:/Users/takhi/OneDrive/Desktop/uploadFolder"/2024/6/20
            if (!folder.exists()) {
                folder.mkdirs();
            }
            Path path = Paths.get(folder.getAbsolutePath() + "/" + key + "." + extension);
            Files.copy(file.getInputStream(), path);

            AttachEntity entity = new AttachEntity();
            entity.setId(key + "." + extension);
            entity.setOriginName(file.getOriginalFilename());
            entity.setSize(file.getSize());
            entity.setPath(pathFolder);
            entity.setType(extension);
            attachRepository.save(entity);
            return toDTO(entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getById(String attachId) {
        byte[] data;
        try {
            AttachEntity entity = get(attachId);
            String path = entity.getPath() + "/" + attachId;         // 2024/06/11/aaaa-bbbb-cccc.png
            Path file = Paths.get(uploadPath + "/" + path);     // C:/Users/takhi/OneDrive/Desktop/uploadFolder// 2024/06/11/aaaa-bbbb-cccc.png
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public Resource download(String attachId) {
        try {
            AttachEntity entity = get(attachId);
            String path = entity.getPath() + "/" + attachId;
            Path file = Paths.get(uploadPath + "/" + path);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public Page<AttachDto> getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<AttachEntity> pageEntity = attachRepository.findAllBy(pageable);
        List<AttachDto> list = pageEntity
                .stream()
                .map(entity -> {
                    AttachDto dto = new AttachDto();
                    dto.setId(entity.getId());
                    dto.setOriginalName(entity.getOriginName());
                    dto.setSize(entity.getSize());
                    dto.setUrl(serverUrl + "/" + uploadPath + entity.getPath() + "/" + entity.getId());
                    return dto;
                })
                .toList();
        return new PageImpl<>(list, pageable, pageEntity.getTotalElements());
    }

    private String getExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return null;
        }
        return fileName.substring(index + 1);
    }

    private String getYMDString() {
        LocalDate date = LocalDate.now();
        return date.getYear() + "/" + date.getMonthValue() + "/" + date.getDayOfMonth();
    }

    public AttachEntity get(String attachId) {
        return attachRepository.findById(attachId)
                .orElseThrow(() -> new AppBadException("file not found"));
    }

    private AttachDto toDTO(AttachEntity entity) {
        AttachDto dto = new AttachDto();
        dto.setId(entity.getId());
        dto.setCreatedData(entity.getCreatedDate());
        dto.setType(entity.getType());
        dto.setSize(entity.getSize());
        dto.setOriginalName(entity.getOriginName());
        dto.setPath(entity.getPath());
        dto.setUrl(serverUrl + "/" + uploadPath + entity.getPath() + "/" + entity.getId());
        return dto;
    }

    public AttachDto getDTOWithURL(String attachId) {
        AttachEntity attach = attachRepository.findById(attachId)
                .orElseThrow(() -> new AppBadException("Attach not found"));
        AttachDto dto = new AttachDto();
        dto.setId(attachId);
        dto.setUrl(serverUrl + "/" + uploadPath + attach.getPath() + "/" + attachId);
        return dto;
    }

    public void delete(String attachId) {
        AttachEntity entity = get(attachId);
        File file = new File(uploadPath + entity.getPath() + "/" + attachId);
        if (!file.delete()) {
           throw new AppBadException("file not deleted");
        }
        attachRepository.delete(entity);
    }
}
