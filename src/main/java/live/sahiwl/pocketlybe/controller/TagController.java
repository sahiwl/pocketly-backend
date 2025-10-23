package live.sahiwl.pocketlybe.controller;

import live.sahiwl.pocketlybe.dto.TagRequestDTO;
import live.sahiwl.pocketlybe.dto.TagResponseDTO;
import live.sahiwl.pocketlybe.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor

public class TagController {
    private final TagService tagService;

    @PostMapping
    public Map<String, Object> createTag(@RequestBody TagRequestDTO req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal()))
                ? auth.getName()
                : null;

        TagResponseDTO tag = tagService.createTag(req, username);
        return Map.of(
                "message", "Tag created sucessfully",
                "data", tag);
    }

    @GetMapping
    public List<TagResponseDTO> getAllTags() {
        return tagService.getAllTags();
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteTag(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        tagService.deleteTag(id, username);
        return Map.of("message", "Tag deleted successfully");
    }

}
