package com.pokemonreview.api.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.pokemonreview.api.TestData;
import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.service.impl.PokemonServiceImpl;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class) // Bean'lerin gelmesi için lazım
public class PokemonServiceTest {

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks // service'i getirirken gerekli dependency injection'ları da yapıyor
    private PokemonServiceImpl pokemonService;

    @Test
    public void PokemonService_CreatePokemon_IsReturnsPokemonDto(){
        Pokemon pokemon =TestData.testPokemon();
        PokemonDto pokemonDto = TestData.testPokemonDto();

        when(pokemonRepository.save(Mockito.any(Pokemon.class))).thenReturn(pokemon);

        
        PokemonDto savedPokemon = pokemonService.createPokemon(pokemonDto);

        Assertions.assertThat(savedPokemon).isNotNull();
        Assertions.assertThat(savedPokemon.getId()).isEqualTo(pokemon.getId());
    }

    @Test
    public void PokemonService_GetAllPokemon_IsReturnsAllSavedPokemons(){
        Page<Pokemon> pokemons = Mockito.mock(Page.class); // Sahte bir Page<Pokemon> oluşturduk bunu aşağıda return edeceğiz
        //Buradan anlamamız gereken eğer biz bu test içerisinde bir classla işimiz varsa bunu mock etmeliyiz

        when(pokemonRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pokemons);
        PokemonResponse savedPokemons = pokemonService.getAllPokemon(1, 1); //pageNo ve pageSize rastgele 2 sayı olabilir

        Assertions.assertThat(savedPokemons).isNotNull();
    }

    @Test
    public void PokemonService_GetPokemonById_IsReturnsPokemon(){
        Pokemon pokemon = TestData.testPokemon();

        
        when(pokemonRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(pokemon));

        pokemonRepository.save(pokemon);
        
        PokemonDto pokemonDto = pokemonService.getPokemonById(1);

        Assertions.assertThat(pokemonDto.getId()).isGreaterThan(-1);
        Assertions.assertThat(pokemonDto).isNotNull();
        Assertions.assertThat(pokemonDto.getName()).isEqualTo(pokemon.getName());
    }

    @Test
    public void PokemonService_UpdatePokemon_IsSuccessAndReturnsPokemonDto(){
        Pokemon pokemon = TestData.testPokemon();
        PokemonDto pokemonDto = TestData.testPokemonDto();


        when(pokemonRepository.save(Mockito.any(Pokemon.class))).thenReturn(pokemon);
        when(pokemonRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(pokemon));

        pokemonService.createPokemon(pokemonDto);

        PokemonDto updatedPokemon = pokemonService.updatePokemon(pokemonDto, pokemonDto.getId());

        Assertions.assertThat(updatedPokemon.getId()).isGreaterThanOrEqualTo(0);
        Assertions.assertThat(updatedPokemon).isNotNull();

    }

    @Test
    public void PokemonService_DeletePokemonId_IsDeletesPokemon(){
        Pokemon pokemon = TestData.testPokemon();

        
        //eğer yapacağımız işlem sonuç olarak void ise yani geriye döndürme söz konusu değilse assertAll() kullanacağız
        when(pokemonRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(pokemon)); // bunu yapma sebebimiz Service içindeki delete fonksiyonunda bunu kullanıyoruz

        assertAll(()->pokemonService.deletePokemonId(pokemon.getId()));
    }
    
}
