package live.sahiwl.pocketlybe.service;

import live.sahiwl.pocketlybe.dto.ContentRequestDTO;
import live.sahiwl.pocketlybe.dto.ContentResponseDTO;
import live.sahiwl.pocketlybe.model.Content;
import live.sahiwl.pocketlybe.model.Tag;
import live.sahiwl.pocketlybe.model.User;
import live.sahiwl.pocketlybe.repository.ContentRepository;
import live.sahiwl.pocketlybe.repository.TagRepository;
import live.sahiwl.pocketlybe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @Transactional //since these (or files inside it) access lazyloading - if not used, will throw LazyInitException
    public ContentResponseDTO createContent(ContentRequestDTO request){
        User user = userRepository.findById(request.getUserId()).orElseThrow(()-> new RuntimeException("User not found"));

        List<Tag> tags = request.getTagIds() == null ? List.of():tagRepository.findAllById(request.getTagIds());
        Content content = Content.builder()
                .title(request.getTitle())
                .type(request.getType())
                .link(request.getLink())
                .user(user)
                .tags(tags)
                .build();

        Content saved = contentRepository.save(content);

        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ContentResponseDTO> getContentByUser(Long userId) {
        return contentRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    private ContentResponseDTO toResponseDTO(Content content) {
        return ContentResponseDTO.builder()
                .id(content.getId())
                .title(content.getTitle())
                .type(content.getType())
                .link(content.getLink())
                .userId(content.getUser().getId())
                .username(content.getUser().getUsername())
                .tags(content.getTags()
                        .stream()
                        .map(Tag::getTitle)
                        .collect(Collectors.toList()))
                .build();
    }
}
