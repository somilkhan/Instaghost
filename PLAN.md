# InstaGhost: build plan

Goal: an experimental, transparent LSPosed companion/module for Instagram.
Design: monochrome, adaptive, accessible, and privacy-first.

## Decisions
- Package: `dev.instaghost.module`.
- Framework: classic Xposed API, with LSPosed as the target environment.
- Language: Kotlin. Module UI uses Android Views.
- Runtime target discovery: DexKit, guarded by strict ambiguity checks and version-scoped cache invalidation.
- User-facing settings should be clearly labelled as experimental; no feature should be advertised as active before implementation and device validation.
- Prefer documented APIs and observable, user-controlled behavior. Do not implement covert alteration or suppression of other users' activity/privacy signals or security protections.

## Design system
- Grayscale only, with system light/dark theme support.
- Glass effects must have a legible non-blur fallback.
- Minimum touch target 48dp; content must support font scaling and screen readers.
- Avoid animation as the only way to communicate state.
- Provide clear loading, disabled, unsupported, and failure states.

## Engineering priorities
1. Stabilize diagnostics, resolver safety, and user-visible module status.
2. Establish a reproducible compatibility and manual device-test process.
3. Improve companion UI accessibility and settings persistence.
4. Implement only reviewed, privacy-respecting functionality with focused tests.
5. Establish release signing, reproducible release notes, and artifact verification.

## Compatibility validation
A compatibility claim requires recording:
- Instagram version and package build identifier.
- Android version, device model, and LSPosed version.
- Feature-specific test steps and expected/actual results.
- Resolver outcome and relevant sanitized diagnostic category.
- Date tested and regression result.

CI success is not device compatibility evidence.

## Release gates
- CI unit tests, lint, and debug assembly pass.
- No unresolved or ambiguous resolver targets are accepted.
- Diagnostics are bounded and sanitized, and must not collect message contents, credentials, or personal identifiers.
- APK provenance and SHA-256 checksum are recorded.
- Release notes list known limitations and only verified compatibility claims.

## Explicitly out of scope
- Covert tampering with read receipts, typing/presence, story-view signals, screenshot notifications, or other privacy/security signals.
- Collection, export, or logging of private Instagram content or session credentials.
- Declaring features supported based only on static analysis or CI.
