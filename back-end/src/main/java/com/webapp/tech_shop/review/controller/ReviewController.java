package com.webapp.tech_shop.review.controller;

import com.webapp.tech_shop.review.ReviewService;
import com.webapp.tech_shop.review.dto.CreateReviewRequest;
import com.webapp.tech_shop.review.dto.ReviewDetailResponse;
import com.webapp.tech_shop.review.dto.UpdateReviewRequest;
import com.webapp.tech_shop.user.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Management", description = "APIs for product reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDetailResponse> createReview(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateReviewRequest request) {
        return ResponseEntity.ok(reviewService.createReview(user, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDetailResponse> updateReview(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateReviewRequest request) {
        return ResponseEntity.ok(reviewService.updateReview(user, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id) {
        reviewService.deleteReview(user, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDetailResponse>> getReviewsByProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId));
    }

    @GetMapping("/me")
    public ResponseEntity<List<ReviewDetailResponse>> getMyReviews(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(reviewService.getReviewsByUser(user.getId()));
    }
}
