package live.sahiwl.pocketlybe.controller;

import live.sahiwl.pocketlybe.dto.LinkResponseDTO;
import live.sahiwl.pocketlybe.dto.PocketDTO;
import live.sahiwl.pocketlybe.service.ContentService;
import live.sahiwl.pocketlybe.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;
    private final ContentService contentService;

    @GetMapping("/pocket/{hash}")
    public PocketDTO getPocket(@PathVariable String hash){
       return linkService.getUserPocketBySharedHash(hash);
    }

    // Create or disable share link (TODO: secure with auth in Phase 8)
    @PostMapping("/pocket/share")
    public Map<String, Object> createShareLink(
            @RequestParam Long userId, // TODO: replace with userId from token after auth
            @RequestParam boolean share
    ) {
        LinkResponseDTO dto = linkService.createShareLink(userId, share);

        Map<String, Object> response = new HashMap<>();
        if (dto == null) {
            response.put("message", "Sharing disabled");
        } else {
            response.put("link", "http://localhost:8080/api/pocket/" + dto.getHash());
        }
        return response;
    }
}
