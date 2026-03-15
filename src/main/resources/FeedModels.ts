export interface FeedCategory {
    categoryType?: string;
    code?: string;
    name?: string;
    description?: string;
    score?: number;
}

export interface FeedEmployer {
    name?: string;
    orgnr?: string;
    description?: string;
    homepage?: string;
}

export interface FeedLocation {
    country?: string;
    address?: string;
    city?: string;
    postalCode?: string;
    county?: string;
    municipal?: string;
}

export function formatFeedLocation(loc: FeedLocation): string {
    if (loc.city != null && loc.county != null) return `${loc.city}, ${loc.county}`;
    if (loc.city != null) return loc.city;
    if (loc.county != null) return loc.county;
    return "Ikke oppgitt";
}

export interface FeedContact {
    name?: string;
    email?: string;
    phone?: string;
    role?: string;
    title?: string;
}

export interface FeedOccupation {
    level1?: string;
    level2?: string;
}

export interface FeedAd {
    uuid?: string;
    published?: string;
    expires?: string;
    updated?: string;
    title?: string;
    description?: string;
    jobtitle?: string;
    link?: string;
    sourceurl?: string;
    source?: string;
    applicationUrl?: string;
    applicationDue?: string;
    engagementtype?: string;
    extent?: string;
    starttime?: string;
    positioncount?: string;
    sector?: string;
    employer?: FeedEmployer;
    workLocations?: FeedLocation[];
    contactList?: FeedContact[];
    occupationCategories?: FeedOccupation[];
    categoryList?: FeedCategory[];
}

export function formatFeedAd(ad: FeedAd): string {
    const employerName = ad.employer?.name ?? "Ukjent";
    const location = ad.workLocations?.[0] ? formatFeedLocation(ad.workLocations[0]) : "Ikke oppgitt";
    const due = ad.applicationDue ?? "Ikke oppgitt";
    const url = ad.applicationUrl ?? ad.link ?? "(ingen lenke)";
    const type = ad.engagementtype ?? "Ikke oppgitt";
    const scope = ad.extent ?? "Ikke oppgitt";
    const positions = ad.positioncount ?? "1";
    const cleanDesc = ad.description?.replace(/<[^>]+>/g, "").trim() ?? "(ingen beskrivelse)";

    return [
        `Tittel       : ${ad.title}`,
        `Arbeidsgiver : ${employerName}`,
        `Sted         : ${location}`,
        `Type         : ${type} / ${scope}`,
        `Stillinger   : ${positions}`,
        `Søknadsfrist : ${due}`,
        `Søk her      : ${url}`,
        ``,
        `Beskrivelse:`,
        cleanDesc,
    ].join("\n");
}

export interface FeedEntryContent {
    uuid?: string;
    sistEndret?: string;
    status?: string;
    ad_content?: FeedAd;
}
