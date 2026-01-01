package vn.com.hieuduongmanhblog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Tag Request Payload")
public record TagDTO(
        @Schema(description = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Integer id,

        @Schema(description = "tag name", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Tag Name is required")
        String tagName
) {
        public TagDTO TagDTOWithDefaultId(Integer newId) {
                return new TagDTO(newId, tagName);
        }
}
