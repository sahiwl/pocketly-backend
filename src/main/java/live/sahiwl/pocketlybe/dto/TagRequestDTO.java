package live.sahiwl.pocketlybe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagRequestDTO {
    @NotBlank(message = "Tag title is required")
    @Size(min = 2, max = 50, message = "Tag title must be between 2 and 50 characters")
    private String title;
}
