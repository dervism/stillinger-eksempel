# stilling_api (TypeScript)

Denne mappen inneholder TypeScript-versjonen som bruker to endepunkter for å hente stillingsannonser fra NAV.

## 1. Search API (åpent)

**URL:** `https://arbeidsplassen.nav.no/stillinger/api/search`

Søker etter stillinger med fritekstsøk og filtrering på publiseringsdato.
Krever ingen autentisering. Returnerer en liste med treff som inneholder
tittel, arbeidsgiver, lokasjon og andre grunnleggende opplysninger.

Støtter paginering via `from` og `size`-parameterne.

## 2. Feed API (krever token)

**URL:** `https://pam-stilling-feed.nav.no/api/v1/feedentry/{uuid}`

Henter fullstendige detaljer for en enkelt stillingsannonse basert på UUID-en
fra Search API-et. Krever et Bearer-token i `Authorization`-headeren.

Returnerer utvidet informasjon som søknadsfrist, stillingstype, omfang og
full stillingstekst.

## Flyt

1. Kall Search API for å finne relevante stillinger
2. For hver stilling, bruk UUID-en til å hente detaljer fra Feed API

## Eksempel (main)

```bash
node StillingApi.ts
```

`main`-funksjonen i `StillingApi.ts` viser et konkret brukseksempel. Den søker
etter stillinger med søkeordet "nav" publisert en gitt dato, skriver ut tittel,
arbeidsgiver og lokasjon for hvert treff, og henter deretter detaljer fra
Feed API-et (søknadsfrist og stillingstype). Til slutt skrives de fullstendige
detaljene for det første treffet ut som JSON.

Du trenger Node versjon 22 eller nyere for å kjøre TypeScript filer med Node.