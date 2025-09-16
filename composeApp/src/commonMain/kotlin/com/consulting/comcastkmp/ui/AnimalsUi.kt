package com.consulting.comcastkmp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.consulting.comcastkmp.data.Animal
import com.consulting.comcastkmp.data.AnimalKind
import com.consulting.comcastkmp.data.AnimalsDataSource
import androidx.compose.ui.platform.testTag
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AnimalsScreen(
    repository: AnimalsDataSource,
    isLandscape: Boolean,
) {
    var query by remember { mutableStateOf("") }
    var animals by remember { mutableStateOf<List<Animal>>(emptyList()) }
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        animals = repository.getAnimals()
    }
    LaunchedEffect(query) {
        animals = repository.searchAnimals(query)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().testTag("searchField"),
            label = { Text("Search by name or common name") }
        )
        Spacer(Modifier.height(12.dp))

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    animals = repository.getAnimals(forceRefresh = true)
                    animals = repository.searchAnimals(query)
                    isRefreshing = false
                }
            }
        ) {
            if (isLandscape) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.testTag("animalsList")) {
                    items(animals) { animal -> AnimalCard(animal) }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.testTag("animalsList")) {
                    items(animals) { animal -> AnimalCard(animal) }
                }
            }
        }
    }
}

@Composable
private fun AnimalCard(animal: Animal) {
    Card(modifier = Modifier.fillMaxWidth().testTag("animal-${'$'}{animal.id}")) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(animal.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            val phylumText = animal.phylum ?: "Unknown phylum"
            val sciText = animal.scientificName ?: "Unknown scientific name"
            Text("Phylum: $phylumText")
            Text("Scientific: $sciText")

            when (animal.kind) {
                AnimalKind.DOG -> {
                    if (!animal.slogan.isNullOrBlank()) Text("Slogan: ${animal.slogan}")
                    if (!animal.lifespan.isNullOrBlank()) Text("Lifespan: ${animal.lifespan}")
                }
                AnimalKind.BIRD -> {
                    if (!animal.wingspan.isNullOrBlank()) Text("Wingspan: ${animal.wingspan}")
                    if (!animal.habitat.isNullOrBlank()) Text("Habitat: ${animal.habitat}")
                }
                AnimalKind.BUG -> {
                    if (!animal.prey.isNullOrBlank()) Text("Prey: ${animal.prey}")
                    if (!animal.predators.isNullOrBlank()) Text("Predators: ${animal.predators}")
                }
                else -> {}
            }
        }
    }
}


