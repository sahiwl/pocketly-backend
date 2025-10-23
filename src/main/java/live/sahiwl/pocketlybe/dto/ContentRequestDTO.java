package live.sahiwl.pocketlybe.dto;

import lombok.*;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
// @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentRequestDTO {
    @NotBlank(message = "Title is required")
    @Size(max=255, message = "Title must be less than 255characters.")
    private String title;
    private String type;
    private String link; // raw URL string
    private Long userId; 
    private List<Long> tagIds; // optional
}
