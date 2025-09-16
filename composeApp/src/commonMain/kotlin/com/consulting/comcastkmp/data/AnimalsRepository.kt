package com.consulting.comcastkmp.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.URLBuilder
import io.ktor.http.encodeURLParameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json

interface AnimalsDataSource {
    suspend fun getAnimals(forceRefresh: Boolean = false): List<Animal>
    suspend fun searchAnimals(query: String): List<Animal>
}

class AnimalsRepository(
    private val apiKey: String,
    private val client: HttpClient = defaultHttpClient()
): AnimalsDataSource {
    private val baseUrl = "https://api.api-ninjas.com/v1/animals"

    private val cacheMutex = Mutex()
    private var cacheInstant: Instant? = null
    private var cachedAnimals: List<Animal> = emptyList()
    private val cacheTtlMillis: Long = 10 * 60 * 1000 // 10 minutes

    override suspend fun getAnimals(forceRefresh: Boolean): List<Animal> {
        return cacheMutex.withLock {
            val now = Clock.System.now()
            val isStale = cacheInstant == null || now.toEpochMilliseconds() - (cacheInstant!!.toEpochMilliseconds()) > cacheTtlMillis
            if (forceRefresh || isStale || cachedAnimals.isEmpty()) {
                val dogs = fetchKind("dog", AnimalKind.DOG)
                val birds = fetchKind("bird", AnimalKind.BIRD)
                val bugs = fetchKind("bug", AnimalKind.BUG)
                val combined = (dogs + birds + bugs)
                cachedAnimals = combined
                cacheInstant = now
            }
            cachedAnimals
        }
    }

    override suspend fun searchAnimals(query: String): List<Animal> {
        val all = getAnimals()
        val term = query.trim().lowercase()
        if (term.isEmpty()) return all
        return all.filter { animal ->
            animal.name.lowercase().contains(term) ||
            (animal.commonName?.lowercase()?.contains(term) == true)
        }
    }

    private suspend fun fetchKind(name: String, kind: AnimalKind): List<Animal> {
        val url = URLBuilder(baseUrl).apply {
            parameters.append("name", name.encodeURLParameter())
        }.buildString()
        val response: List<AnimalDto> = client.get(url) {
            header("X-Api-Key", apiKey)
        }.body()
        return response.take(3).map { it.toDomain(kind) }
    }

    companion object {
        fun defaultHttpClient(): HttpClient = HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(Logging) {
                level = LogLevel.INFO
            }
        }
    }
}


