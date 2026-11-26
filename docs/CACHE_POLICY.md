## Resolver cache key format

Cache keys are version-scoped as `version:anchor:fingerprint`. Pruning retains up to 128 keys for the current Instagram version and removes keys outside the current-version prefix. The prefix filter is intentionally conservative but does not parse or validate the key's internal fields. Malformed current-prefix keys may be retained until descriptor validation invalidates them.

