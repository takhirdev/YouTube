package you_tube_own.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import you_tube_own.dto.comment.CommentLikeCreateDto;
import you_tube_own.dto.comment.CommentLikeDto;
import you_tube_own.entity.CommentLikeEntity;
import you_tube_own.repository.CommentLikeRepository;
import you_tube_own.util.SecurityUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;

    public void reaction(CommentLikeCreateDto dto) {
        Long profileId = SecurityUtil.getProfileId();
        Optional<CommentLikeEntity> optional = commentLikeRepository.findByCommentIdAndProfileId(dto.getCommentId(), profileId);

        if (optional.isEmpty()) {
            var entity = CommentLikeEntity.builder()
                    .commentId(dto.getCommentId())
                    .profileId(profileId)
                    .reaction(dto.getReaction())
                    .build();
            commentLikeRepository.save(entity);
            return;
        }

        CommentLikeEntity entity = optional.get();
        if (entity.getReaction().equals(dto.getReaction())) {
            commentLikeRepository.delete(entity);
            return;
        }

        entity.setReaction(dto.getReaction());
        commentLikeRepository.save(entity);
    }

    public Page<CommentLikeDto> getByUserId(Long profileId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdDate"));
        Page<CommentLikeEntity> pageEntity = commentLikeRepository.findAllByProfileId(profileId,pageable);
        List<CommentLikeDto> list = pageEntity.getContent()
                .stream()
                .map(entity ->{
                    return CommentLikeDto.builder()
                            .id(entity.getId())
                            .commentId(entity.getCommentId())
                            .reaction(entity.getReaction())
                            .userId(entity.getProfileId())
                            .createdDate(entity.getCreatedDate())
                            .build();
                })
                .toList();

        long totalElements = pageEntity.getTotalElements();
        return new PageImpl<>(list, pageable, totalElements);
    }
}
