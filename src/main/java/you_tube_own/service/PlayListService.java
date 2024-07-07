package you_tube_own.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.web.access.channel.ChannelEntryPoint;
import org.springframework.stereotype.Service;
import you_tube_own.dto.AttachDto;
import you_tube_own.dto.chanel.ChanelDto;
import you_tube_own.dto.playList.PlayListCreateDto;
import you_tube_own.dto.playList.PlayListDto;
import you_tube_own.dto.playList.PlayListUpdateDto;
import you_tube_own.dto.profile.ProfileDto;
import you_tube_own.entity.PlayListEntity;
import you_tube_own.entity.ProfileEntity;
import you_tube_own.enums.PlayListStatus;
import you_tube_own.enums.ProfileRole;
import you_tube_own.exception.AppBadException;
import you_tube_own.repository.PlaylistRepository;
import you_tube_own.util.SecurityUtil;

import java.util.List;


@RequiredArgsConstructor
@Service
public class PlayListService {
    private final PlaylistRepository playlistRepository;
    private final AttachService attachService;

    public PlayListDto create(PlayListCreateDto dto) {
        PlayListEntity entity = toEntity(dto);
        PlayListEntity saved = playlistRepository.save(entity);
        return toDto(saved);
    }

    public PlayListDto update(Long playlistId, PlayListUpdateDto dto) {
        isOwner(playlistId);

        PlayListEntity playListEntity = getById(playlistId);
        if (dto.getName() != null) {
            playListEntity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            playListEntity.setDescription(dto.getDescription());
        }

        if (dto.getOrderNumber() != null) {
            playListEntity.setOrderNumber(dto.getOrderNumber());
        }
        PlayListEntity saved = playlistRepository.save(playListEntity);
        return toDto(saved);
    }

    public void updateStatus(Long playlistId, PlayListStatus status) {
        isAdminOrOwner(playlistId);
        playlistRepository.updateStatus(playlistId, status);
    }

    public void delete(Long playlistId) {
        isAdminOrOwner(playlistId);
        playlistRepository.deleteById(playlistId);
    }

    public Page<PlayListDto> getAll(int pageNumber, int pageSize) {
        //   id,name,description,status(private,public),order_num,
        //    channel(id,name,photo(id,url),
        //    profile(id,name,surname,photo(id,url)
        //    ))

        // 1-> 100 playlist , 2 - 200  -> 201
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PlayListEntity> entityPage = playlistRepository.findAllBy(pageable);
        List<PlayListDto> list = entityPage.getContent()
                .stream()
                .map( entity -> {

                })
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

    public List<PlayListDto> getByCurrentUserId() {
        Long userId = SecurityUtil.getProfileId();
        return playlistRepository.getByUserId(userId)
                .stream()
                .map(this::toShortInfo)
                .toList();
    }

    public List<PlayListDto> getByChanelId(String chanelId) {
        return playlistRepository.findAllByChanelId(chanelId)
                .stream()
                .map(this::toShortInfo)
                .toList();
    }

    public PlayListEntity getByPlaylistId(Long playlistId) {
        return getById(playlistId);
    }

    private PlayListDto toShortInfo(PlayListEntity entity) {
        PlayListDto dto = new PlayListDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setVideoCount(playlistRepository.videoCount());
        ///////////// video list

        // create channel
        ChanelDto chanel = new ChanelDto();
        chanel.setId(entity.getChanelId());
        chanel.setName(entity.getName());
        dto.setChanel(chanel);
        return dto;
    }

    private PlayListDto toFullInfo(PlayListEntity entity) {
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
        chanel.setName(entity.getName());
        // create chanel photo
        AttachDto chanelAttach = attachService.getDTOWithURL(entity.getChanel().getPhotoId());
        chanel.setPhoto(chanelAttach);

        dto.setChanel(chanel);

        // create profile
        ProfileDto profile = new ProfileDto();
        profile.setId(entity.getChanel().getProfileId());
        profile.setName(entity.getChanel().getProfile().getName());
        profile.setSurname(entity.getChanel().getProfile().getSurname());
        // create profile photo
        AttachDto profileAttach = attachService.getDTOWithURL(entity.getChanel().getProfile().getPhotoId());
        profile.setPhoto(profileAttach);

        dto.setProfile(profile);
        return dto;
    }

    private PlayListEntity toEntity(PlayListCreateDto dto) {
        PlayListEntity entity = new PlayListEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());
        entity.setChanelId(dto.getChanelId());
        entity.setOrderNumber(dto.getOrderNumber());
        return entity;
    }

    private PlayListDto toDto(PlayListEntity entity) {
        PlayListDto dto = new PlayListDto();
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setChanelId(entity.getChanelId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    private void isAdminOrOwner(Long playlistId) {
        ProfileEntity profile = SecurityUtil.getProfile();
        PlayListEntity playListEntity = getById(playlistId);
        if (playListEntity.getChanel().getProfileId() != profile.getId()
                || !profile.getRole().equals(ProfileRole.ROLE_ADMIN)) {
            throw new AppBadException("You dont have permission to delete this playlist");
        }
    }

    private void isOwner(Long playlistId) {
        Long profileId = SecurityUtil.getProfileId();
        PlayListEntity playListEntity = getById(playlistId);
        if (playListEntity.getChanel().getProfileId() != profileId) {
            throw new AppBadException("You dont have permission to update this playlist");
        }
    }

    public PlayListEntity getById(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new AppBadException("Playlist not found"));
    }
}
