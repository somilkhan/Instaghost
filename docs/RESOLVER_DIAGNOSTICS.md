# Resolver instrumentation notes

## Current event model
The resolver emits bounded, sanitized events for cache hits, invalid cache entries, cache misses, unique matches, ambiguous/no matches and failures. Events are process-local and retained in memory only. Feature keys and exception categories are validated; raw exception messages, account data and content are not recorded.

## Resolver safety
- Anchor discovery is fail-closed: exactly one candidate is required.
- Cached descriptors are revalidated reflectively before use.
- Cache entries are version-scoped and fingerprinted by anchor set.
- Old versions are evicted; cache size is capped by ResolverCachePolicy.
- Resolution failures are not treated as a successful hook installation.

## Important limitation
A companion APK runs under a different Android UID from Instagram. The in-memory event buffer and Instagram SharedPreferences cannot be read directly from the companion. This event model is groundwork only; it does not make the companion Health page live.

## Next transport gate
Before designing IPC, decide whether a dedicated provider can authenticate callers with an Android permission that is actually enforceable across the separately installed module and target app. Do not use an exported provider protected only by a manifest permission the companion cannot legitimately hold. If reliable caller authentication cannot be established, use explicit, user-initiated sanitized export inside the target process or a carefully validated LSPosed/Xposed bridge, rather than insecure broadcasts or public files.

## Test requirements
- Bounded event storage and concurrent access.
- Sanitized failure categories and strict field bounds.
- cache-hit and invalid-cache paths.
- unique, ambiguous and empty discovery outcomes.
- APK split behavior, runtime class-loader behavior and Android version matrix on physical devices.


## Startup APK inventory

Resolver startup now emits a sanitized inventory summary (base present, split count, total APK paths, and missing-file count) without logging APK paths. This is inventory-only: split APK DEX scanning remains unsupported, and discovery currently uses the base APK only. A split count greater than zero must not be interpreted as split compatibility.
