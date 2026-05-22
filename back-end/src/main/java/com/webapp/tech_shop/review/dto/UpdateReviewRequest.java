package com.webapp.tech_shop.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateReviewRequest {
    @Size(min = 1, max = 2000)
    private String content;

    @Min(1)
    @Max(5)
    private Integer rating;

    private String reviewReply;
}
