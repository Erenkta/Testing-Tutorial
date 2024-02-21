package com.pokemonreview.api.controllers;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemonreview.api.TestData;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.service.PokemonService;

@WebMvcTest(controllers = PokemonController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PokemonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokemonService pokemonService;

    @Autowired
    private ObjectMapper objectMapper;
    private Pokemon pokemon;
    private Review review;
    private ReviewDto reviewDto;
    private PokemonDto pokemonDto;

    @BeforeEach
    public void init() {
        pokemon = TestData.testPokemon();
        pokemonDto = TestData.testPokemonDto();
        review = TestData.testReview();
        reviewDto = TestData.testReviewDto();

        review.setPokemon(pokemon);
        pokemon.setReviews(List.of(review));

    }

    @Test
    public void PokemonController_CreatePokemon_ReturnCreated() throws Exception {
        given(pokemonService.createPokemon(ArgumentMatchers.any()))
                .willAnswer((invocation -> invocation.getArgument(0))); //createPokemon'a verilen argümanı geri dönsün diye yazdığımız kod

        ResultActions response = mockMvc.perform(post("/api/pokemon/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pokemonDto))); //Serialize işlemleriyle uğraşmamak için

        response.andExpect(MockMvcResultMatchers.status().isCreated()) //Dönen cevabın status'ü 201 olmalı
            .andExpect(MockMvcResultMatchers.jsonPath("$.name",CoreMatchers.is(pokemonDto.getName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.type",CoreMatchers.is(pokemonDto.getType())));
            //.andDo(MockMvcResultHandlers.print()); bu respone nesnesini console'a print etmek içindi
    }
    @Test
    public void PokemonController_GetPokemons_IsReturnsAllSavedPokemons() throws Exception{
             PokemonResponse responseDto = PokemonResponse.builder().pageSize(10).last(true).pageNo(1).content(List.of(pokemonDto)).build();
        when(pokemonService.getAllPokemon(1,10)).thenReturn(responseDto);

        ResultActions response = mockMvc.perform(get("/api/pokemon")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo","1")
                .param("pageSize", "10"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()", CoreMatchers.is(responseDto.getContent().size())));
    }
    @Test
    public void PokemonController_PokemonDetails_IsReturnsPokemonDto() throws  Exception{
        when(pokemonService.getPokemonById(Mockito.anyInt())).thenReturn(pokemonDto);

        ResultActions response = mockMvc.perform(get("/api/pokemon/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(pokemonDto)));
        
        response.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name",CoreMatchers.is(pokemonDto.getName())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.type",CoreMatchers.is(pokemonDto.getType())));

    }
    @Test
    public void PokemonController_UpdatePokemon_IsReturnsPokemonDto() throws  Exception{
        when(pokemonService.updatePokemon(pokemonDto,1)).thenReturn(pokemonDto); //given().willAnswer() can be replaced with when().thenAnswer()
        
        ResultActions response =mockMvc.perform(put("/api/pokemon/1/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pokemonDto))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.name",CoreMatchers.is(pokemonDto.getName())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.type",CoreMatchers.is(pokemonDto.getType())));
    }
    @Test
    public void PokemonController_DeletePokemon_IsReturnsString() throws  Exception{
        doNothing().when(pokemonService).deletePokemonId(1); //delete geriye null döndüğü için

        ResultActions response = mockMvc.perform(delete("/api/pokemon/1/delete")
        .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
    

}


/*
    Örnek 1: thenReturn ile sabit değer döndürme
    Mockito.when(mockObj.getSayi()).thenReturn(10); // Her zaman 10 dönder

    Örnek 2: thenAnswer ile dinamik değer döndürme
    Mockito.when(mockObj.getAd(arg0)).thenAnswer(invocation -> {
        String arg = (String) invocation.getArgument(0);
        return "Merhaba, " + arg;
    });  Parametreye göre merhaba mesajı oluştur
 */