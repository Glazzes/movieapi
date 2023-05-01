package com.glaze.movieapi.mappers;

import com.glaze.movieapi.dto.in.CreateReviewRequest;
import com.glaze.movieapi.dto.out.ReviewResponse;
import com.glaze.movieapi.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "reviewAt", ignore = true)
    @Mapping(target = "lastModified", ignore = true)
    @Mapping(source = "content", target = "content")
    @Mapping(source = "rating", target = "rating")
    Review mapCreateReviewRequestToReviewEntity(CreateReviewRequest request);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "rating", target = "rating")
    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "lastModified", target = "lastModified")
    @Mapping(source = "reviewAt", target = "reviewAt")
    ReviewResponse mapReviewEntityToReviewResponse(Review review);

}
