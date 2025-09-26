package live.sahiwl.pocketlybe.controller;

import live.sahiwl.pocketlybe.dto.LinkRequestDTO;
import live.sahiwl.pocketlybe.dto.LinkResponseDTO;
import live.sahiwl.pocketlybe.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @PostMapping
    public LinkResponseDTO createLink(@RequestBody LinkRequestDTO req){
        return linkService.createLink(req.getUserId(), req.getOriginalURL());
    }

    @GetMapping("/user/{userId}")
    public List<LinkResponseDTO> getLinksByUser(@PathVariable Long userId){
        return linkService.getLinksByUser(userId);
    }

    //TODO: remove userId from params, use JWT or session instead.
    @DeleteMapping("/{linkId}")
    public String deleteLink(@PathVariable Long linkId, @RequestParam Long userId){
        return linkService.deleteLink(linkId, userId);
    }
}
