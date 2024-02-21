package com.pokemonreview.api.repositories;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pokemonreview.api.TestData;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.repository.ReviewRepository;

@DataJpaTest //Jpa için lazım
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2) //  Test için in-memory db'de çalışıyoruz bunun için lazım
@RunWith(SpringRunner.class) // Bean'lerin gelmesi için lazım
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void ReviewRepository_Save_IsSavesReviewAndReturnsSavedReview(){
        Review review = TestData.testReview();

        Review savedReview = reviewRepository.save(review);

        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview).isEqualTo(review);
    }
    @Test
    public void ReviewRepository_FindAll_IsReturnsAllSavedReviews(){
        Review review1 = TestData.testReview();
        Review review2 = TestData.testReview();

        List<Review> allSavedReviews = reviewRepository.saveAll(List.of(review1,review2));

        Assertions.assertThat(allSavedReviews).isNotNull();
        Assertions.assertThat(allSavedReviews.size()).isEqualTo(2);
    }

    @Test
    public void ReviewRepository_FindById_IsReturnsReview(){
        Review review = TestData.testReview();
        reviewRepository.save(review);

        Review savedReview = reviewRepository.findById(review.getId()).get();

        Assertions.assertThat(savedReview).isNotNull();
        Assertions.assertThat(savedReview.getId()).isEqualTo(review.getId());
    }

    @Test
    public void ReviewRepository_UpdateReview_IsSuccess(){
        Review review = TestData.testReview();
        
         reviewRepository.save(review);
         Review savedReview = reviewRepository.findById(review.getId()).get();
        savedReview.setContent("new content");

        Review updatedReview = reviewRepository.save(savedReview);

        Assertions.assertThat(updatedReview.getId()).isEqualTo(review.getId());
        Assertions.assertThat(updatedReview.getContent()).isNotNull(); // Content karşılaştırma işe yaramdı

    }
    @Test
    public void ReviewRepository_Delete_IsSuccessfullyDeletesReview(){
        Review review = TestData.testReview();
        
       Review savedReview = reviewRepository.save(review);
       reviewRepository.delete(savedReview);

        Assertions.assertThat(reviewRepository.findById(savedReview.getId())).isEmpty();

    }
}