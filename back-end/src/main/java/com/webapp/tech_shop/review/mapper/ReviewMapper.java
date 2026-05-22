package com.webapp.tech_shop.review.mapper;

import com.webapp.tech_shop.review.dto.ReviewDetailResponse;
import com.webapp.tech_shop.review.model.Review;
import java.util.List;
import java.util.stream.Collectors;

public interface ReviewMapper {
    static ReviewDetailResponse toDto(Review r) {
        return new ReviewDetailResponse(
                r.getId(),
                r.getProduct() != null ? r.getProduct().getId() : null,
                r.getReviewer() != null ? r.getReviewer().getId() : null,
                r.getReviewer() != null ? r.getReviewer().getName() : null,
                r.getContent(),
                r.getRating(),
                r.getReviewReply(),
                r.getCreatedAt()
        );
    }

    static java.util.List<ReviewDetailResponse> toDtoList(List<Review> reviews) {
        return reviews.stream().map(ReviewMapper::toDto).collect(Collectors.toList());
    }
}
