package com.pokemonreview.api;

import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;

public final class TestData {

	private TestData() {

	}
	public static Pokemon testPokemon() {
		return Pokemon.builder() 
        .name("pikachu")
        .type("electric").build(); 
	}
    public static Review testReview(){
        return Review.builder()
        .content("content")
        .title("title")
        .stars(5)
        .build();
    }
    public static PokemonDto testPokemonDto() {
		return PokemonDto.builder() 
        .name("pikachu")
        .type("electric").build(); 
	}
    public static ReviewDto testReviewDto(){
        return ReviewDto.builder()
        .content("content")
        .title("title")
        .stars(5)
        .build();
    }

}