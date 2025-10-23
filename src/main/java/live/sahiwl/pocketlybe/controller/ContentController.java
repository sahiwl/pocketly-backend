package live.sahiwl.pocketlybe.controller;

import live.sahiwl.pocketlybe.dto.ContentRequestDTO;
import live.sahiwl.pocketlybe.dto.ContentResponseDTO;
import live.sahiwl.pocketlybe.model.User;
import live.sahiwl.pocketlybe.service.ContentService;
import live.sahiwl.pocketlybe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;
    private final UserService userService;

    @PostMapping
    public ContentResponseDTO createContent(@Valid @RequestBody ContentRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        // Override any userId sent by client with authenticated user's ID for security
        request.setUserId(user.getId());

        return contentService.createContent(request);
    }

    @GetMapping
    public List<ContentResponseDTO> getMyContent() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        return contentService.getContentByUser(user.getId());
    }

    @PutMapping("/{contentId}")
    public ContentResponseDTO updateContent(
            @PathVariable Long contentId,
            @Valid @RequestBody ContentRequestDTO request) {
        // Extract username from JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        return contentService.updateContent(contentId, user.getId(), request);
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<Map<String, String>> deleteContent(@PathVariable Long contentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        contentService.deleteContent(contentId, user.getId());

        return ResponseEntity.ok(Map.of("message", "Content deleted successfully"));
    }

}
