package live.sahiwl.pocketlybe.controller;

import live.sahiwl.pocketlybe.dto.ContentRequestDTO;
import live.sahiwl.pocketlybe.dto.ContentResponseDTO;
import live.sahiwl.pocketlybe.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    // Add content (TODO: secure with auth in Phase 8)
    @PostMapping
    public ContentResponseDTO createContent(@RequestBody ContentRequestDTO request){
        // TODO: get userId from token after auth
    return contentService.createContent(request);

    }

    // Get all content for a user
    @GetMapping
    public List<ContentResponseDTO> getContentByUser(
            @RequestParam Long userId // TODO: replace with userId from token after auth
    ) {
        return contentService.getContentByUser(userId);
    }
}
