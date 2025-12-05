package live.sahiwl.pocketlybe.controller;

import java.util.HashMap;
import java.util.Map;
import live.sahiwl.pocketlybe.dto.LinkResponseDTO;
import live.sahiwl.pocketlybe.dto.PocketDTO;
import live.sahiwl.pocketlybe.model.User;
import live.sahiwl.pocketlybe.service.LinkService;
import live.sahiwl.pocketlybe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;
    private final UserService userService;

    @Value("${APP_BASE_URL}")
    private String baseUrl;

    @GetMapping("/pocket/share/status")
    public Map<String, Object> getShareStatus() {
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        String hash = linkService.getShareLinkHash(user.getId());

        Map<String, Object> response = new HashMap<>();
        if (hash != null) {
            response.put("isSharing", true);
            response.put("hash", hash);
        } else {
            response.put("isSharing", false);
        }
        return response;
    }

    @GetMapping("/pocket/{hash}")
    public PocketDTO getPocket(@PathVariable String hash) {
        return linkService.getUserPocketBySharedHash(hash);
    }

    @PostMapping("/pocket/share")
    public Map<String, Object> createShareLink(@RequestParam boolean share) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        LinkResponseDTO dto = linkService.createShareLink(user.getId(), share);

        Map<String, Object> response = new HashMap<>();
        if (dto == null) {
            response.put("message", "Sharing disabled");
        } else {
            response.put("hash", dto.getHash());
        }
        return response;
    }
}
