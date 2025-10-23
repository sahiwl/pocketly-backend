package live.sahiwl.pocketlybe.service;

import live.sahiwl.pocketlybe.dto.TagRequestDTO;
import live.sahiwl.pocketlybe.dto.TagResponseDTO;
import live.sahiwl.pocketlybe.model.Tag;
import live.sahiwl.pocketlybe.model.User;
import live.sahiwl.pocketlybe.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final ContentService contentService;
    private final TagRepository tagRepository;
    private final UserService userService;

    public TagResponseDTO createTag(TagRequestDTO req, String username) {
        Tag tag = tagRepository.findByTitle(req.getTitle())
                .orElseGet(() -> {
                    User user = username != null ? userService.findByUsername(username) : null;
                    return tagRepository.save(Tag.builder()
                            .title(req.getTitle())
                            .createdBy(user)
                            .build());
                });
        return new TagResponseDTO(tag.getId(), tag.getTitle());
    }

    public List<TagResponseDTO> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tag -> TagResponseDTO.builder().id(tag.getId()).title(tag.getTitle()).build())
                .collect(Collectors.toList());
    }

    public void deleteTag(Long tagId, String username) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        if (tag.getCreatedBy() == null || !tag.getCreatedBy().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to delete this tag");
        }

        tagRepository.deleteById(tagId);
    }
}