package live.sahiwl.pocketlybe.controller;

import live.sahiwl.pocketlybe.dto.TagRequestDTO;
import live.sahiwl.pocketlybe.dto.TagResponseDTO;
import live.sahiwl.pocketlybe.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor

public class TagController {
    private final TagService tagService;

@PostMapping
    public Map<String, Object> createTag(@RequestBody TagRequestDTO req){
        TagResponseDTO tag = tagService.createTag(req);
        return Map.of(
                "message", "Tag created sucessfully",
                "data", tag
        );
    }

    @GetMapping
    public List<TagResponseDTO> getAllTags(){
        return tagService.getAllTags();
    }

}
