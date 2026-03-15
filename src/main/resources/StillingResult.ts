import { Hit } from "./SearchModels";
import { FeedEntryContent } from "./FeedModels";

export interface StillingResult {
    hit: Hit;
    details: () => Promise<FeedEntryContent | null>;
}
