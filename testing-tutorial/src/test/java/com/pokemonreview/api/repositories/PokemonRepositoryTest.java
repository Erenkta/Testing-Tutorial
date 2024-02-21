package com.pokemonreview.api.repositories;

import static org.junit.Assert.assertThat;

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
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.repository.PokemonRepository;




@DataJpaTest //Jpa için lazım
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2) //  Test için in-memory db'de çalışıyoruz bunun için lazım
@RunWith(SpringRunner.class) // Bean'lerin gelmesi için lazım
public class PokemonRepositoryTest {
    
    @Autowired
    private  PokemonRepository pokemonRepository;

    @Test
    public void PokemonRepository_Save_IsReturnsSavedPokemons(){
        //Arrange
        Pokemon pokemon = Pokemon.builder()
        .name("pikachu")
        .type("electric").build(); //Fake bir data oluşturduk

        //Act --> nasıl davranılacağı kısım

        Pokemon savedPokemon = pokemonRepository.save(pokemon);

        //Assert

       Assertions.assertThat(savedPokemon).isNotNull();
       Assertions.assertThat(savedPokemon.getId()).isEqualTo(pokemon.getId()).isGreaterThan(0);
    }

    @Test
    public void PokemonRepository_FindAll_IsReturnsAllSavedPokemons(){
        Pokemon pokemon1 = TestData.testPokemon();
        Pokemon pokemon2 = TestData.testPokemon();

        pokemonRepository.save(pokemon1);
        pokemonRepository.save(pokemon2);

        List<Pokemon> allSavedPokemon= pokemonRepository.findAll();

        Assertions.assertThat(allSavedPokemon).isNotNull();
        Assertions.assertThat(allSavedPokemon).isEqualTo(List.of(pokemon1,pokemon2));
    }

    @Test
    public void PokemonRepository_FindById_IsReturnsCorrectPokemon(){
        Pokemon pokemon = TestData.testPokemon();
        
        pokemonRepository.save(pokemon);
        Assertions.assertThat(pokemonRepository.findById(pokemon.getId()).get()).isNotNull();//Boş dönmesin diye
        Assertions.assertThat(pokemonRepository.findById(pokemon.getId()).get().getId()).isEqualTo(pokemon.getId()); // Doğru olanı dönsün diye
    }
    @Test
    public void PokemonRepository_FindByType_IsReturnsPokemonAndNotNull(){
        Pokemon pokemon = TestData.testPokemon();

        pokemonRepository.save(pokemon);
        

        Assertions.assertThat(pokemonRepository.findByType(pokemon.getType()).get()).isNotNull();
        Assertions.assertThat(pokemonRepository.findByType(pokemon.getType()).get()).isEqualTo(pokemon);
    }
    @Test
    public void PokemonRepository_UpdatePokemon_IsSuccess(){
        Pokemon pokemon = TestData.testPokemon();

        pokemonRepository.save(pokemon);

        Pokemon savedPokemon = pokemonRepository.findById(pokemon.getId()).get();

        savedPokemon.setName("New Pikachu");

        Pokemon updatedPokemon = pokemonRepository.save(savedPokemon);
        Assertions.assertThat(updatedPokemon.getName()).isNotNull();
        
    }
    @Test
    public void PokemonRepository_DeletePokemon_IsSuccess(){
        Pokemon pokemon = TestData.testPokemon();

        pokemonRepository.save(pokemon);
        pokemonRepository.delete(pokemon);
        
        Assertions.assertThat(pokemonRepository.findById(pokemon.getId())).isEmpty();
    }
}
