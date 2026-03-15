export interface SearchResponse {
    took?: number;
    timed_out?: boolean;
    hits?: HitsWrapper;
}

export interface HitsWrapper {
    total?: HitsTotal;
    max_score?: number;
    hits?: Hit[];
}

export interface HitsTotal {
    value?: number;
    relation?: string;
}

export interface Hit {
    _index?: string;
    _id?: string;
    _score?: number;
    _source?: AdSource;
}

export interface AdSource {
    uuid?: string;
    title?: string;
    businessName?: string;
    published?: string;
    expires?: string;
    status?: string;
    medium?: string;
    source?: string;
    reference?: string;
    locationList?: Location[];
    categoryList?: Category[];
    employer?: Employer;
    occupationList?: Occupation[];
    properties?: Properties;
    generatedSearchMetadata?: SearchMetadata;
}

export interface Location {
    country?: string;
    address?: string;
    city?: string;
    postalCode?: string;
    county?: string;
    municipal?: string;
}

export interface Category {
    categoryType?: string;
    name?: string;
}

export interface Employer {
    name?: string;
}

export interface Occupation {
    level1?: string;
    level2?: string;
}

export interface Properties {
    workLanguage?: string[];
    education?: string[];
    applicationdue?: string;
    keywords?: string;
    jobtitle?: string;
    remote?: string;
    experience?: string[];
    needDriversLicense?: string[];
}

export interface SearchMetadata {
    shortSummary?: string;
    isUnder18?: boolean;
    isUnder18Reason?: string;
}
