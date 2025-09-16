package com.consulting.comcastkmp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Raw DTOs based on API Ninjas animals response.
@Serializable
data class AnimalDto(
    val name: String,
    @SerialName("taxonomy") val taxonomy: TaxonomyDto? = null,
    @SerialName("characteristics") val characteristics: CharacteristicsDto? = null
)

@Serializable
data class TaxonomyDto(
    val phylum: String? = null,
    val scientific_name: String? = null
)

@Serializable
data class CharacteristicsDto(
    val slogan: String? = null,
    val lifespan: String? = null,
    val wingspan: String? = null,
    val habitat: String? = null,
    val prey: String? = null,
    val predators: String? = null,
    @SerialName("common_name") val commonName: String? = null
)

// Domain model used by UI
enum class AnimalKind { DOG, BIRD, BUG, OTHER }

data class Animal(
    val id: String,
    val kind: AnimalKind,
    val name: String,
    val commonName: String?,
    val phylum: String?,
    val scientificName: String?,
    val slogan: String?,
    val lifespan: String?,
    val wingspan: String?,
    val habitat: String?,
    val prey: String?,
    val predators: String?
)

fun AnimalDto.toDomain(kind: AnimalKind): Animal = Animal(
    id = "$kind-$name-${taxonomy?.scientific_name ?: characteristics?.commonName ?: ""}",
    kind = kind,
    name = name,
    commonName = characteristics?.commonName,
    phylum = taxonomy?.phylum,
    scientificName = taxonomy?.scientific_name,
    slogan = characteristics?.slogan,
    lifespan = characteristics?.lifespan,
    wingspan = characteristics?.wingspan,
    habitat = characteristics?.habitat,
    prey = characteristics?.prey,
    predators = characteristics?.predators
)



