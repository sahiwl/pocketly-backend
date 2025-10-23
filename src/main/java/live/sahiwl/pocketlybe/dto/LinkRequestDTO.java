package live.sahiwl.pocketlybe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkRequestDTO {
    private Long userId; // Set automatically from JWT in controller
    
    @NotBlank(message = "URL is required")
    @Pattern(regexp = "^https?://.*", message = "URL must be a valid URL starting with http:// or https://")
    @Size(max = 2048, message = "URL must be less than 2048 characters")
    private String originalURL;
}
