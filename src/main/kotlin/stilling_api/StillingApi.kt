package stilling_api

import kotlinx.serialization.json.Json
import stilling_api.model.*
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private const val SEARCH_URL = "https://arbeidsplassen.nav.no/stillinger/api/search"
private const val FEED_URL   = "https://pam-stilling-feed.nav.no/api/v1/feedentry"

private val http = HttpClient.newHttpClient()
private val json = Json { ignoreUnknownKeys = true }

/**
 * Fetches a single search page from the Stilling API.
 */
private fun fetchSearchPage(
    query: String,
    size: Int,
    sort: String,
    published: String,
    from: Int,
): SearchResponse {
    val url = "$SEARCH_URL?q=$query&size=$size&sort=$sort&published=$published&from=$from"
    val request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Accept", "application/json")
        .GET()
        .build()

    val response = http.send(request, HttpResponse.BodyHandlers.ofString())

    return when (response.statusCode()) {
        200  -> json.decodeFromString<SearchResponse>(response.body())
        else -> error("HTTP ${response.statusCode()} from $url")
    }
}

private const val TOKEN =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJuYXYudGVhbS5hcmJlaWRzcGxhc3NlbkBuYXYubm8iLCJraWQiOiI5YTY2OTc2MS1hMmFhLTQ2YjQtOWZkNi0yYTQ5YmNjZjJmNjUiLCJpc3MiOiJuYXYtbm8iLCJhdWQiOiJmZWVkLWFwaS12MiIsImlhdCI6MTc3MzE1NjAxMSwiZXhwIjoxNzc2MTgwMDExfQ.3gNy0H-uLgfrkLbzKY3mp2sWt2kqgjJGZ-0rCP4UQlE"

/**
 * Fetches the full ad details from the Feed API for a given UUID.
 * Returns null on 404 or error.
 */
private fun fetchFeedEntry(uuid: String, token: String): FeedEntryContent? {
    val request = HttpRequest.newBuilder()
        .uri(URI.create("$FEED_URL/$uuid"))
        .header("Accept", "application/json")
        .header("Authorization", "Bearer $token")
        .GET()
        .build()

    val response = http.send(request, HttpResponse.BodyHandlers.ofString())

    return when (response.statusCode()) {
        200  -> json.decodeFromString<FeedEntryContent>(response.body())
        404  -> null
        else -> null
    }
}

/**
 * Uses [generateSequence] to gather all job ads from the Nav Stilling API.
 */
fun searchAllAds(
    query: String,
    sort: String = "published",
    published: String,
    pageSize: Int = 25,
): List<Hit> {
    val startTime = System.currentTimeMillis()

    val firstPage = fetchSearchPage(query, pageSize, sort, published, 0)
    val total = firstPage.hits?.total?.value ?: 0

    return generateSequence(0 to firstPage) { (from, _) ->
        val nextFrom = from + pageSize
        if (nextFrom >= total) null
        else nextFrom to fetchSearchPage(query, pageSize, sort, published, nextFrom)
    }
    .onEachIndexed { pageIndex, _ ->
        val fetched = minOf((pageIndex + 1) * pageSize, total)
        val elapsed = (System.currentTimeMillis() - startTime) / 1000.0
        System.err.printf("\rPage %d | %,d / %,d hits | %.1fs", pageIndex + 1, fetched, total, elapsed)
    }
    .flatMap { (_, page) -> page.hits?.hits.orEmpty().asSequence() }
    .toList()
    .also {
        val elapsed = (System.currentTimeMillis() - startTime) / 1000.0
        System.err.println()
        System.err.printf("Done: %,d hits in %.1fs%n", it.size, elapsed)
    }
}

// ---------------------------------------------------------------------------
// Main
// ---------------------------------------------------------------------------

fun main() {
    val hits = searchAllAds(
        query = "nav",
        published = "2026-03-13",
    )

    println("=== ${hits.size} treff for 'nav' publisert 2026-03-13 ===")
    println()

    hits.forEach { hit ->
        val src = hit.source ?: return@forEach

        val loc = src.locationList?.firstOrNull()
        println("${src.title}")
        println("  ${src.businessName} | ${loc?.municipal ?: ""} | ${src.published?.take(10)}")

        val detail = hit.id?.let { fetchFeedEntry(it, TOKEN) }
        if (detail?.adContent != null) {
            val ad = detail.adContent
            println("  Søknadsfrist: ${ad.applicationDue ?: "Ikke oppgitt"}")
            println("  Type: ${ad.engagementtype ?: "?"} / ${ad.extent ?: "?"}")
        } else {
            println("  (ingen detaljer tilgjengelig fra Feed API)")
        }
        println()
    }
}
