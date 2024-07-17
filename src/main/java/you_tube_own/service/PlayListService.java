package you_tube_own.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import you_tube_own.dto.chanel.ChanelDto;
import you_tube_own.dto.playList.PlayListCreateDto;
import you_tube_own.dto.playList.PlayListDto;
import you_tube_own.dto.playList.PlayListUpdateDto;
import you_tube_own.dto.profile.ProfileDto;
import you_tube_own.dto.video.VideoDto;
import you_tube_own.entity.PlayListEntity;
import you_tube_own.entity.ProfileEntity;
import you_tube_own.enums.PlayListStatus;
import you_tube_own.enums.ProfileRole;
import you_tube_own.exception.AppBadException;
import you_tube_own.mapper.PlayListMapper;
import you_tube_own.mapper.PlaylistFullInfoMapper;
import you_tube_own.repository.PlaylistRepository;
import you_tube_own.util.SecurityUtil;
import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Service
public class PlayListService {
    private final PlaylistRepository playlistRepository;
    private final AttachService attachService;

    public String create(PlayListCreateDto dto) {
        var entity = PlayListEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .chanelId(dto.getChanelId())
                .orderNumber(dto.getOrderNumber())
                .build();

        playlistRepository.save(entity);
        return entity.getId();
    }

    public PlayListDto update(Long playlistId, PlayListUpdateDto dto) {
        isOwner(playlistId);
        PlayListEntity entity = getById(playlistId);
        entity.setName(dto.getName() == null ? entity.getName() : dto.getName());
        entity.setDescription(dto.getDescription() == null ? entity.getDescription() : dto.getDescription());
        entity.setOrderNumber(dto.getOrderNumber() == null ? entity.getOrderNumber() : dto.getOrderNumber());
        playlistRepository.save(entity);

        return PlayListDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .orderNumber(entity.getOrderNumber())
                .status(entity.getStatus())
                .chanelId(entity.getChanelId())
                .createdDate(entity.getCreatedDate())
                .build();
    }

    public void updateStatus(Long playlistId, PlayListStatus status) {
        isOwner(playlistId);
        playlistRepository.updateStatus(playlistId, status);
    }

    public void delete(Long playlistId) {
        isAdminOrOwner(playlistId);
        playlistRepository.deleteById(playlistId);
    }

    public Page<PlayListDto> getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PlaylistFullInfoMapper> entityPage = playlistRepository.findAllBy(pageable);
        List<PlayListDto> list = entityPage.getContent()
                .stream()
                .map(this::toFullInfo)
                .toList();
        long totalElements = entityPage.getTotalElements();
        return new PageImpl<>(list, pageable, totalElements);
    }

    public List<PlayListDto> getByUserId(Long userId) {
        return playlistRepository.getByUserId(userId)
                .stream()
                .map(this::toFullInfo)
                .toList();
    }

    public List<PlayListDto> getByChanelId(String chanelId) {
        List<Object[]> resultList = playlistRepository.findAllByChanelId(chanelId);
//        JSONArray playlist = new JSONArray();
//        for (Object[] o : resultList) {
//            JSONObject obj = new JSONObject();
//            obj.put("playlistId", MapperUtil.getLongValue(o[0]));
//            obj.put("playlistName", MapperUtil.getStringValue(o[1]));
//            obj.put("playlistCreationDate", (o[2]));
//            obj.put("chanelId", MapperUtil.getStringValue(o[3]));
//            obj.put("chanelName", MapperUtil.getStringValue(o[4]));
//            obj.put("videoCount", MapperUtil.getLongValue(o[5]));
//            obj.put("videos", new JSONArray(MapperUtil.getStringValue(o[6])));
//            playlist.put(obj);
//        }
//        return playlist;
        return resultList
                .stream()
                .map(objects -> {
                    PlayListDto dto = new PlayListDto();
                    dto.setId((String) objects[0]);
                    dto.setName((String) objects[1]);
                    dto.setCreatedDate((LocalDateTime) objects[2]);
                    dto.setVideoCount((Long) objects[5]);

                    ObjectMapper objectMapper = new ObjectMapper();
                    List<VideoDto> videoList = null;
                    try {
                        videoList = objectMapper.readValue(objects[6].toString(), new TypeReference<List<VideoDto>>() {});
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    dto.setVideoList(videoList);

                    // create chanel
                    ChanelDto chanelDto = new ChanelDto();
                    chanelDto.setId((String) objects[3]);
                    chanelDto.setName((String) objects[4]);
                    dto.setChanel(chanelDto);

                    return dto;
                })
                .toList();
    }

    public PlayListDto getByPlaylistId(Long playlistId) {
        PlayListMapper mapper = playlistRepository.getById(playlistId);
        return PlayListDto.builder()
                .id(mapper.getId())
                .name(mapper.getName())
                .videoCount(mapper.getVideoCount())
                .totalViewCount(mapper.getTotalViewCount())
                .build();
    }

    private PlayListDto  toFullInfo(PlaylistFullInfoMapper entity) {
        // create playlist
        PlayListDto dto = new PlayListDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getCreatedDate());

        // create channel
        ChanelDto chanel = new ChanelDto();
        chanel.setId(entity.getChanelId());
        chanel.setName(entity.getChanelName());
        chanel.setPhotoId(entity.getChanelPhotoId());
        dto.setChanel(chanel);

        // create profile
        ProfileDto profile = new ProfileDto();
        profile.setId(entity.getProfileId());
        profile.setName(entity.getProfileName());
        profile.setSurname(entity.getProfileSurname());
        profile.setPhotoId(entity.getProfilePhotoId());
        dto.setProfile(profile);
        return dto;
    }

    private void isAdminOrOwner(Long playlistId) {
        ProfileEntity currentUserId = SecurityUtil.getProfile();
        Long ownerId = playlistRepository.findOwnerIdByPlaylistId(playlistId);
        if (!ownerId.equals(currentUserId.getId())
                && !currentUserId.getRole().equals(ProfileRole.ROLE_ADMIN)) {
            throw new AppBadException("You dont have permission to delete this playlist");
        }
    }

    private void isOwner(Long playlistId) {
        Long currentUserId = SecurityUtil.getProfileId();
        Long ownerId = playlistRepository.findOwnerIdByPlaylistId(playlistId);
        if (!ownerId.equals(currentUserId)) {
            throw new AppBadException("You dont have permission to update this playlist");
        }
    }

    public PlayListEntity getById(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new AppBadException("Playlist not found"));
    }
}
