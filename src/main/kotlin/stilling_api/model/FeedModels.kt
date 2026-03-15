package stilling_api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedCategory(
    val categoryType: String? = null,
    val code: String?         = null,
    val name: String?         = null,
    val description: String?  = null,
    val score: Double?        = null,
)

@Serializable
data class FeedEmployer(
    val name: String?        = null,
    val orgnr: String?       = null,
    val description: String? = null,
    val homepage: String?    = null,
)

@Serializable
data class FeedLocation(
    val country: String?    = null,
    val address: String?    = null,
    val city: String?       = null,
    val postalCode: String? = null,
    val county: String?     = null,
    val municipal: String?  = null,
) {
    fun format(): String = when {
        city != null && county != null -> "$city, $county"
        city != null                   -> city
        county != null                 -> county
        else                           -> "Ikke oppgitt"
    }
}

@Serializable
data class FeedContact(
    val name: String?  = null,
    val email: String? = null,
    val phone: String? = null,
    val role: String?  = null,
    val title: String? = null,
)

@Serializable
data class FeedOccupation(
    val level1: String? = null,
    val level2: String? = null,
)

@Serializable
data class FeedAd(
    val uuid: String?                              = null,
    val published: String?                         = null,
    val expires: String?                           = null,
    val updated: String?                           = null,
    val title: String?                             = null,
    val description: String?                       = null,
    val jobtitle: String?                          = null,
    val link: String?                              = null,
    val sourceurl: String?                         = null,
    val source: String?                            = null,
    val applicationUrl: String?                    = null,
    val applicationDue: String?                    = null,
    val engagementtype: String?                    = null,
    val extent: String?                            = null,
    val starttime: String?                         = null,
    val positioncount: String?                     = null,
    val sector: String?                            = null,
    val employer: FeedEmployer?                    = null,
    val workLocations: List<FeedLocation>?         = null,
    val contactList: List<FeedContact>?            = null,
    val occupationCategories: List<FeedOccupation>? = null,
    val categoryList: List<FeedCategory>?          = null,
) {
    fun format(): String {
        val employerName = employer?.name ?: "Ukjent"
        val location     = workLocations?.firstOrNull()?.format() ?: "Ikke oppgitt"
        val due          = applicationDue ?: "Ikke oppgitt"
        val url          = applicationUrl ?: link ?: "(ingen lenke)"
        val type         = engagementtype ?: "Ikke oppgitt"
        val scope        = extent ?: "Ikke oppgitt"
        val positions    = positioncount ?: "1"
        val cleanDesc    = description?.replace(Regex("<[^>]+>"), "")?.trim()
                         ?: "(ingen beskrivelse)"

        return """
            |Tittel       : $title
            |Arbeidsgiver : $employerName
            |Sted         : $location
            |Type         : $type / $scope
            |Stillinger   : $positions
            |Søknadsfrist : $due
            |Søk her      : $url
            |
            |Beskrivelse:
            |$cleanDesc
        """.trimMargin()
    }
}

@Serializable
data class FeedEntryContent(
    val uuid: String?                              = null,
    val sistEndret: String?                        = null,
    val status: String?                            = null,
    @SerialName("ad_content") val adContent: FeedAd? = null,
)
