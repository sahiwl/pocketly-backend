package live.sahiwl.pocketlybe.dto;

import lombok.*;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentRequestDTO {
    @NotBlank(message = "Title is required")
    @Size(max=255, message = "Title must be less than 255characters.")
    private String title;
    
    @NotBlank(message = "Type is required")
    @Pattern(regexp = "article|video|tweet|image|other", message = "Type must be: article, video, tweet, image, or other")
    private String type;
    
    @NotBlank(message = "Link is required")
    @Pattern(regexp = "^https?://.*", message = "Link must be a valid URL starting with http:// or https://")
    @Size(max = 2048, message = "Link must be less than 2048 characters")
    private String link;
    
    private Long userId; // Set automatically from JWT in controller
    private List<Long> tagIds; // optional
}
