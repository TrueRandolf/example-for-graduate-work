package ru.skypro.homework.dto.comments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "Comments")
@Data
public class Comments {

    @Schema(description = "общее количество комментариев")
    private Integer count;

    @Schema(description = "")
    private List<Comment> results;
}
