package stilling_api.model

/**
 * Wraps a search [Hit] with a lazy function that fetches the full ad details
 * from the Feed API (`/feedentry/{uuid}`). The detail call is only made when
 * [details] is first accessed.
 */
data class StillingResult(
    val hit: Hit,
    val details: Lazy<FeedEntryContent?>,
)
