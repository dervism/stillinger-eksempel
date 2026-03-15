package stilling_api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val took: Long?          = null,
    @SerialName("timed_out") val timedOut: Boolean? = null,
    val hits: HitsWrapper?   = null,
)

@Serializable
data class HitsWrapper(
    val total: HitsTotal?    = null,
    @SerialName("max_score") val maxScore: Double? = null,
    val hits: List<Hit>?     = null,
)

@Serializable
data class HitsTotal(
    val value: Int?          = null,
    val relation: String?    = null,
)

@Serializable
data class Hit(
    @SerialName("_index") val index: String?   = null,
    @SerialName("_id")    val id: String?      = null,
    @SerialName("_score") val score: Double?   = null,
    @SerialName("_source") val source: AdSource? = null,
)

@Serializable
data class AdSource(
    val uuid: String?                          = null,
    val title: String?                         = null,
    val businessName: String?                  = null,
    val published: String?                     = null,
    val expires: String?                       = null,
    val status: String?                        = null,
    val medium: String?                        = null,
    val source: String?                        = null,
    val reference: String?                     = null,
    val locationList: List<Location>?          = null,
    val categoryList: List<Category>?          = null,
    val employer: Employer?                    = null,
    val occupationList: List<Occupation>?      = null,
    val properties: Properties?                = null,
    val generatedSearchMetadata: SearchMetadata? = null,
)

@Serializable
data class Location(
    val country: String?     = null,
    val address: String?     = null,
    val city: String?        = null,
    val postalCode: String?  = null,
    val county: String?      = null,
    val municipal: String?   = null,
)

@Serializable
data class Category(
    val categoryType: String? = null,
    val name: String?         = null,
)

@Serializable
data class Employer(
    val name: String?         = null,
)

@Serializable
data class Occupation(
    val level1: String?       = null,
    val level2: String?       = null,
)

@Serializable
data class Properties(
    val workLanguage: List<String>?    = null,
    val education: List<String>?       = null,
    val applicationdue: String?        = null,
    val keywords: String?              = null,
    val jobtitle: String?              = null,
    val remote: String?                = null,
    val experience: List<String>?      = null,
    val needDriversLicense: List<String>? = null,
)

@Serializable
data class SearchMetadata(
    val shortSummary: String?      = null,
    val isUnder18: Boolean?        = null,
    val isUnder18Reason: String?   = null,
)
