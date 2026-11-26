# Resolver Diagnostics Design

This document specifies the resolver diagnostics contract before runtime code is changed.

## Feature runtime state
Use a sealed state model (or equivalent enum + data payload):
- Disabled: user preference is off.
- Resolving: resolver is working.
- Resolved: a candidate passed validation but hook installation is not yet confirmed.
- Installed: hook registration completed successfully.
- Unsupported: no validated candidate for this app build, or candidate count is ambiguous.
- Failed: an exception occurred. Store a sanitized failure category, not raw sensitive data.

Do not collapse preference state into runtime state. A preference can be enabled while a feature is unsupported or failed.

## Resolver result
Each resolution attempt should produce:
- feature identifier
- app package and version code (not account/user data)
- outcome
- candidate count
- elapsed milliseconds
- cache hit or miss
- sanitized failure category

Never record message contents, URLs shared in chats, account identifiers, authentication tokens, or full arbitrary stack traces in user-facing logs.

## Cache
Cache keys should include the feature identifier and Instagram version code. Clear or bypass cached entries on version change. Treat cache entries as hints, not proof: validate the descriptor and expected method metadata before installation. Invalidate on validation failure.

## Matching policy
- 0 candidates: unsupported for this build.
- 1 candidate: proceed to metadata validation.
- More than 1: ambiguous, unsupported. Do not select a random candidate.
- Exceptions: failed, feature isolated, app should not crash.

## Diagnostics UI
Status sheet should display a summary, supported app version, and feature-level status. Provide copy and clear actions. Logs must be redacted and bounded in size.

## Test cases
- No candidate.
- Single candidate with valid metadata.
- Single candidate with invalid metadata.
- Multiple candidates.
- Cache hit, valid cached metadata.
- Cache hit, invalid cached metadata and invalidation.
- Version change.
- Resolver exception.
- Hook installation exception.
- Feature disabled during initialization.
- Two feature resolvers fail without affecting other features.

## Acceptance
Resolver tests must be deterministic and run without Instagram. Device validation remains separately documented and required before declaring a feature Working.
