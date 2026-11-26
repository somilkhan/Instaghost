# Security design and threat model

This document describes engineering safeguards for the experimental module. It is not a guarantee of security.

## Assets and trust boundaries
- Instagram's process and its private runtime: treat as unstable and untrusted for compatibility purposes.
- Module preferences: minimize retained state; avoid secrets and account-derived data.
- Resolver cache: treat as stale, attacker-influenced or corrupted until validation succeeds.
- Diagnostics: treat as potentially sensitive even when intended to be sanitized.
- Build artifacts: verify provenance and checksums before distribution.

## Threats considered
- Resolver selecting an unintended overload or ambiguous method.
- Stale cache entries surviving app upgrades or runtime changes.
- Exception messages or debug output leaking private content.
- Feature activation without clear user intent.
- Malformed descriptors, oversized input, and unexpected runtime errors destabilizing Instagram.
- Supply-chain drift in Gradle dependencies or GitHub Actions.

## Mitigations
- Unique-match resolution and fail-closed behavior.
- Version-scoped cache keys, descriptor validation and bounded input.
- Allowlisted diagnostics fields, bounded local storage, and no required telemetry.
- Feature enablement must be explicit, visible, reversible, and documented.
- CI tests, lint, build verification, dependency update automation, and artifact checksums.
- Pin or review third-party workflow/dependency changes as part of maintenance.

## Out of scope
- Protection against a compromised Android device, root manager, LSPosed installation, or Instagram process.
- Guaranteed compatibility or stability across Instagram releases.
- Hidden monitoring, account-data extraction, credential/session collection, or covert manipulation of privacy/security signals.

## Release review
Before distributing an APK, review the diff and dependency changes, run CI, inspect diagnostics for sensitive fields, record validation limitations, and publish a checksum alongside the artifact. Device-specific testing must be recorded separately from automated CI.
