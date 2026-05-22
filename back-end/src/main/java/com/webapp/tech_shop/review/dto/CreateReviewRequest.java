package com.webapp.tech_shop.review.dto;

import java.util.UUID;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateReviewRequest {
    @NotNull
    private UUID productId;

    @NotNull
    @Size(min = 1, max = 2000)
    private String content;

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
}
