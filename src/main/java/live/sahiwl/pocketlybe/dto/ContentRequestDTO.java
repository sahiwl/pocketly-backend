package live.sahiwl.pocketlybe.dto;

import lombok.*;

import java.util.List;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentRequestDTO {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255characters.")
    private String title;

    @NotBlank(message = "Type is required")
    @Pattern(regexp = "youtube|linkedin|pinterest|tweet|facebook|instagram|other", message = "Type must be: youtube, linkedin, tweet, pinterest, facebook, instagram, or other")
    private String type;

    @NotBlank(message = "Link is required")
    @Pattern(regexp = "^https?://.*", message = "Link must be a valid URL starting with http:// or https://")
    @Size(max = 2048, message = "Link must be less than 2048 characters")
    private String link;

    @Size(max = 5000, message = "Description must be less than 5000 characters")
    private String description; 

    private Long userId; // Set automatically from JWT in controller
    private List<Long> tagIds; 
}
