package com.webapp.tech_shop.review;

import com.webapp.tech_shop.exception.BaseException;
import com.webapp.tech_shop.exception.ErrorCode;
import com.webapp.tech_shop.product.ProductRepository;
import com.webapp.tech_shop.product.model.Product;
import com.webapp.tech_shop.review.dto.CreateReviewRequest;
import com.webapp.tech_shop.review.dto.ReviewDetailResponse;
import com.webapp.tech_shop.review.dto.UpdateReviewRequest;
import com.webapp.tech_shop.review.mapper.ReviewMapper;
import com.webapp.tech_shop.review.model.Review;
import com.webapp.tech_shop.user.Role;
import com.webapp.tech_shop.user.User;
import com.webapp.tech_shop.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewDetailResponse createReview(User user, CreateReviewRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));

        Review review = new Review();
        review.setProduct(product);
        review.setReviewer(user);
        review.setContent(request.getContent());
        review.setRating(request.getRating());

        Review saved = reviewRepository.save(review);
        return ReviewMapper.toDto(saved);
    }

    @Transactional
    public ReviewDetailResponse updateReview(User user, UUID reviewId, UpdateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(ErrorCode.REVIEW_NOT_FOUND));

        boolean isOwner = review.getReviewer() != null && review.getReviewer().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Not authorized to update this review");
        }

        if (request.getContent() != null) review.setContent(request.getContent());
        if (request.getRating() != null) review.setRating(request.getRating());
        if (request.getReviewReply() != null) review.setReviewReply(request.getReviewReply());

        Review saved = reviewRepository.save(review);
        return ReviewMapper.toDto(saved);
    }

    @Transactional
    public void deleteReview(User user, UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(ErrorCode.REVIEW_NOT_FOUND));

        boolean isOwner = review.getReviewer() != null && review.getReviewer().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Not authorized to delete this review");
        }

        reviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewDetailResponse> getReviewsByProduct(UUID productId) {
        return ReviewMapper.toDtoList(reviewRepository.findByProductId(productId));
    }

    @Transactional(readOnly = true)
    public List<ReviewDetailResponse> getReviewsByUser(UUID userId) {
        return ReviewMapper.toDtoList(reviewRepository.findByReviewerId(userId));
    }
}
