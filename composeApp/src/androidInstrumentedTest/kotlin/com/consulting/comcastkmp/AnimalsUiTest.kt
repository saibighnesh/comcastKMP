package com.consulting.comcastkmp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.consulting.comcastkmp.data.Animal
import com.consulting.comcastkmp.data.AnimalKind
import com.consulting.comcastkmp.data.AnimalsDataSource
import com.consulting.comcastkmp.ui.AnimalsScreen
import org.junit.Rule
import org.junit.Test

class AnimalsUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleAnimals = listOf(
        Animal(
            id = "dog-1",
            kind = AnimalKind.DOG,
            name = "Doggo",
            commonName = "Good Boy",
            phylum = "Chordata",
            scientificName = "Canis familiaris",
            slogan = "Man's best friend",
            lifespan = "10-13 years",
            wingspan = null,
            habitat = null,
            prey = null,
            predators = null
        ),
        Animal(
            id = "bird-1",
            kind = AnimalKind.BIRD,
            name = "Robin",
            commonName = "Robin",
            phylum = "Chordata",
            scientificName = "Erithacus rubecula",
            slogan = null,
            lifespan = null,
            wingspan = "20-22 cm",
            habitat = "Woodlands",
            prey = null,
            predators = null
        ),
        Animal(
            id = "bug-1",
            kind = AnimalKind.BUG,
            name = "Ladybug",
            commonName = "Ladybird",
            phylum = "Arthropoda",
            scientificName = "Coccinellidae",
            slogan = null,
            lifespan = null,
            wingspan = null,
            habitat = null,
            prey = "Aphids",
            predators = "Birds"
        )
    )

    private val fakeDataSource = object : AnimalsDataSource {
        override suspend fun getAnimals(forceRefresh: Boolean): List<Animal> = sampleAnimals
        override suspend fun searchAnimals(query: String): List<Animal> =
            sampleAnimals.filter { it.name.contains(query, ignoreCase = true) || (it.commonName?.contains(query, true) == true) }
    }

    @Test
    fun renders_and_filters_by_search() {
        composeTestRule.setContent {
            AnimalsScreen(repository = fakeDataSource, isLandscape = false)
        }

        // Verify list appears
        composeTestRule.onNodeWithTag("animalsList").assertIsDisplayed()
        // Enter search and check result
        composeTestRule.onNodeWithTag("searchField").performTextInput("lady")
        composeTestRule.onNodeWithText("Ladybug").assertIsDisplayed()
    }

    @Test
    fun renders_required_fields_per_type() {
        composeTestRule.setContent {
            AnimalsScreen(repository = fakeDataSource, isLandscape = false)
        }

        // Dog specifics
        composeTestRule.onNodeWithText("Slogan: Man's best friend").assertIsDisplayed()
        composeTestRule.onNodeWithText("Lifespan: 10-13 years").assertIsDisplayed()

        // Bird specifics
        composeTestRule.onNodeWithText("Wingspan: 20-22 cm").assertIsDisplayed()
        composeTestRule.onNodeWithText("Habitat: Woodlands").assertIsDisplayed()

        // Bug specifics
        composeTestRule.onNodeWithText("Prey: Aphids").assertIsDisplayed()
        composeTestRule.onNodeWithText("Predators: Birds").assertIsDisplayed()
    }
}



