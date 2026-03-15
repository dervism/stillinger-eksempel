import type { SearchResponse } from "./SearchModels";
import type { FeedEntryContent } from "./FeedModels";
import type { StillingResult } from "./StillingResult";

const SEARCH_URL = "https://arbeidsplassen.nav.no/stillinger/api/search";
const FEED_URL   = "https://pam-stilling-feed.nav.no/api/v1/feedentry";

const TOKEN =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
    ".eyJzdWIiOiJuYXYudGVhbS5hcmJlaWRzcGxhc3NlbkBuYXYubm8iLCJraWQiOiI5YTY2OTc2" +
    "MS1hMmFhLTQ2YjQtOWZkNi0yYTQ5YmNjZjJmNjUiLCJpc3MiOiJuYXYtbm8iLCJhdWQiOiJm" +
    "ZWVkLWFwaS12MiIsImlhdCI6MTc3MzE1NjAxMSwiZXhwIjoxNzc2MTgwMDExfQ" +
    ".3gNy0H-uLgfrkLbzKY3mp2sWt2kqgjJGZ-0rCP4UQlE";

async function fetchSearchPage(
    query: string,
    size: number,
    sort: string,
    published: string,
    from: number,
): Promise<SearchResponse> {
    const url = `${SEARCH_URL}?q=${query}&size=${size}&sort=${sort}&published=${published}&from=${from}`;
    const response = await fetch(url, {
        headers: { Accept: "application/json" },
    });

    if (response.status !== 200) {
        throw new Error(`HTTP ${response.status} from ${url}`);
    }

    return (await response.json()) as SearchResponse;
}

async function fetchFeedEntry(uuid: string, token: string): Promise<FeedEntryContent | null> {
    const response = await fetch(`${FEED_URL}/${uuid}`, {
        headers: {
            Accept: "application/json",
            Authorization: `Bearer ${token}`,
        },
    });

    if (response.status === 200) {
        return (await response.json()) as FeedEntryContent;
    }
    return null;
}

async function searchAllAds(
    query: string,
    published: string,
    feedToken: string,
    sort: string = "published",
    pageSize: number = 25,
): Promise<StillingResult[]> {
    const startTime = Date.now();
    const results: StillingResult[] = [];

    const firstPage = await fetchSearchPage(query, pageSize, sort, published, 0);
    const total = firstPage.hits?.total?.value ?? 0;

    let from = 0;
    let page = firstPage;
    let pageCount = 0;

    while (true) {
        pageCount++;
        const hits = page.hits?.hits ?? [];

        for (const hit of hits) {
            results.push({
                hit,
                details: () => hit._id ? fetchFeedEntry(hit._id, feedToken) : Promise.resolve(null),
            });
        }

        const elapsed = ((Date.now() - startTime) / 1000).toFixed(1);
        process.stderr.write(`\rPage ${pageCount} | ${results.length} / ${total} hits | ${elapsed}s`);

        if (results.length >= total || hits.length === 0) break;

        from += pageSize;
        page = await fetchSearchPage(query, pageSize, sort, published, from);
    }

    const elapsed = ((Date.now() - startTime) / 1000).toFixed(1);
    process.stderr.write("\n");
    process.stderr.write(`Done: ${results.length} hits in ${elapsed}s\n`);

    return results;
}

async function main() {
    const results = await searchAllAds(
        "nav",
        "2026-03-13",
        TOKEN,
    );

    console.log(`=== ${results.length} treff for 'nav' publisert 2026-03-13 ===`);
    console.log();

    for (const result of results) {
        const src = result.hit._source;
        if (!src) continue;

        const loc = src.locationList?.[0];
        console.log(src.title);
        console.log(`  ${src.businessName} | ${loc?.municipal ?? ""} | ${src.published?.substring(0, 10)}`);

        const detail = await result.details();
        if (detail?.ad_content) {
            const ad = detail.ad_content;
            console.log(`  Søknadsfrist: ${ad.applicationDue ?? "Ikke oppgitt"}`);
            console.log(`  Type: ${ad.engagementtype ?? "?"} / ${ad.extent ?? "?"}`);
        } else {
            console.log("  (ingen detaljer tilgjengelig fra Feed API)");
        }
        console.log();
    }
}

main();
