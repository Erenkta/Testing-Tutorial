package com.pokemonreview.api.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import com.pokemonreview.api.TestData;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.repository.ReviewRepository;
import com.pokemonreview.api.service.impl.ReviewServiceImpl;

@ExtendWith(MockitoExtension.class) // Mockito için gereklidir.
@RunWith(SpringRunner.class) // Bean'lerin gelmesi için lazım
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks // yukarıdaki mocklar'ı buraya inject etmek için
    private ReviewServiceImpl reviewService;
    
    //Test fieldlarımızı init anında oluşturduk.
    private Pokemon pokemon;
    private PokemonDto pokemonDto;
    private Review review;
    private ReviewDto reviewDto;

    @BeforeEach //Before => constructor gibi çalışıyor. BeforeEach ise her biri için ayrı ayrı çalışıyor sanırım
    public void init(){
        pokemon = TestData.testPokemon();
        pokemonDto = TestData.testPokemonDto();
        review = TestData.testReview();
        reviewDto = TestData.testReviewDto();

        review.setPokemon(pokemon);
        pokemon.setReviews(List.of(review));

    }

    @Test
    public void ReviewService_CreateReview_IsCreatesReview(){

        when(pokemonRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(pokemon));
        when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);

        ReviewDto savedReview = reviewService.createReview(0, reviewDto);

        Assertions.assertThat(savedReview).isNotNull();
    }
    @Test
    public void ReviewService_GetReviewsByPokemonId_IsReturnsListOfReview(){

        when(reviewRepository.findByPokemonId(Mockito.anyInt())).thenReturn(List.of(review,review));

        List<ReviewDto> returnedValue = reviewService.getReviewsByPokemonId(1);

        Assertions.assertThat(returnedValue.size()).isGreaterThan(0);
    }
    @Test
    public void ReviewService_GetReviewsById_IsReturnsListOfReview(){

        when(pokemonRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(pokemon));
        when(reviewRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(review));

        ReviewDto returnedValue = reviewService.getReviewById(1, 1);
        Assertions.assertThat(returnedValue).isNotNull();
    }
    @Test
    public void ReviewService_UpdateReview_IsUpdatesReview(){
        
        when(pokemonRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(pokemon));
        when(reviewRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(review));
        when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);

        ReviewDto updatedReview = reviewService.updateReview(0, 0, reviewDto);

        Assertions.assertThat(updatedReview).isNotNull();
    }

    @Test
    public void ReviewService_DeleteReview_IsDeletesReview(){
        when(pokemonRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(pokemon));
        when(reviewRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(review));

        assertAll(()->reviewService.deleteReview(1, 1));
    }


}
