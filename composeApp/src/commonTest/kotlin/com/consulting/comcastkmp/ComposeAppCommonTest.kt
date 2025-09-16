package com.consulting.comcastkmp

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.consulting.comcastkmp.data.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ComposeAppCommonTest {

    @Test
    fun example() {
        assertEquals(3, 1 + 2)
    }

    @Test
    fun repository_fetches_and_filters() = runTestBlocking {
        val payload = """
            [
              {"name":"Dog A","taxonomy":{"phylum":"Chordata","scientific_name":"Canis a"},"characteristics":{"common_name":"dog a","slogan":"good","lifespan":"10"}},
              {"name":"Dog B","taxonomy":{"phylum":"Chordata","scientific_name":"Canis b"},"characteristics":{"common_name":"dog b"}},
              {"name":"Dog C","taxonomy":{"phylum":"Chordata","scientific_name":"Canis c"},"characteristics":{"common_name":"dog c"}}
            ]
        """.trimIndent()
        val engine = MockEngine { _ ->
            respond(
                content = payload,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = HttpClient(engine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        }
        val repo = AnimalsRepository(apiKey = "test", client = client)
        val all = repo.getAnimals(forceRefresh = true)
        assertEquals(9, all.size)
        val filtered = repo.searchAnimals("dog b")
        assertTrue(filtered.any { it.name == "Dog B" })
    }
}

// Minimal runBlocking for KMP tests (define directly in commonTest)
fun runTestBlocking(block: suspend () -> Unit) = kotlinx.coroutines.runBlocking { block() }