package com.glaze.movieapi.mappers;

import com.glaze.movieapi.dto.in.CreateReviewRequest;
import com.glaze.movieapi.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "content", target = "content")
    @Mapping(source = "rating", target = "rating")
    Review mapCreateReviewRequestToReviewEntity(CreateReviewRequest request);

}
