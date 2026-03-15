# stillinger

A Kotlin command-line application for fetching and filtering job listings from the [NAV Arbeidsplassen](https://arbeidsplassen.nav.no) job vacancy feed API.

## Prerequisites

- JDK 21 or newer
- Maven 3.8+
- Node 22 or newer

## Build and run Kotlin

```bash
mvn compile exec:java
```

## Build and run TypeScript

```bash
cd src/main/resources
node StillingApi.ts
```

## Project structure
    
```
src/main/kotlin/stilling_api/
├── StillingApi.kt              # API client and main entry point
└── model/
    ├── SearchModels.kt         # Search API response types
    ├── FeedModels.kt           # Feed API response types
    └── StillingResult.kt       # Result wrapper with lazy detail loading
src/main/resources/
├── StillingApi.ts              # TypeScript API client and main entry point
├── SearchModels.ts             # Search API response types (TypeScript)
├── FeedModels.ts               # Feed API response types (TypeScript)
├── StillingResult.ts           # Result wrapper (TypeScript)
```
