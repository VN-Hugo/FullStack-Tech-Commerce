package com.webapp.tech_shop.review.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewDetailResponse {
    private UUID id;
    private UUID productId;
    private UUID reviewerId;
    private String reviewerName;
    private String content;
    private Integer rating;
    private String reviewReply;
    private LocalDateTime createdAt;
}
