# InstaGhost Architecture (working baseline)

## Principles
- Keep the Xposed entry point thin: package gate, lifecycle hand-off, and guarded bootstrap only.
- Isolate host-specific discovery behind Resolver and a versioned compatibility layer.
- Features are declarative, individually guarded, and should not own UI or persistence implementation.
- Configuration and diagnostics must not depend on Instagram internals.
- Fail closed: unresolved targets disable the affected feature and report a diagnostic; never crash the host.
- Avoid collecting account identifiers, message contents, or other sensitive host data in diagnostics.

## Proposed modules / packages
| Package | Responsibility |
|---|---|
| core | Bootstrap, lifecycle, host context |
| compatibility | Host-version metadata, capability status, diagnostics |
| resolver | DexKit discovery and cache keyed by host version + resolver schema |
| features | Registry, feature metadata, safe feature implementations |
| config | Preference schema, migrations, import/export |
| logging | bounded, sanitized local diagnostic events |
| ui | settings, search, compatibility status, log viewer |

## Bootstrap flow
1. Verify package and initialize process context.
2. Capture host package/version and Android build metadata.
3. Load config schema and perform migration.
4. Resolve compatibility profile, without assuming a match.
5. Initialize feature registry and install supported features independently.
6. Emit sanitized status; close discovery resources.
7. UI is attached independently and must not block bootstrap.

## Resolver requirements
- Cache entries include host version, resolver schema version, and key.
- Cache hits must be validated, and stale descriptors removed.
- Ambiguous matches are reported as unsupported rather than guessed.
- Discovery exceptions are isolated and logged without sensitive data.
- Discovery bridge lifetime must be explicit and closeable.

## Quality gates
- Unit tests for config migrations, descriptor serialization, registry behavior, and diagnostics sanitization.
- Test host-version mismatch and resolver ambiguity paths.
- CI build, lint, and test tasks must be green before release.
- Compatibility claims require evidence from tested APK/device matrix.
