# InstaGhost Engineering Plan

## Working principles
- Build small, independently verifiable increments.
- Fail closed: unresolved or ambiguous targets remain unavailable, never guessed.
- Report feature status honestly. Distinguish enabled preference, installed hook, and validated behavior.
- Scope runtime work to Instagram and isolate hook failures.
- Keep diagnostics local, minimal, and free of message content, account identifiers, or tokens.
- Follow the monochrome, liquid-glass UI specification. This file is implementation planning, not a replacement for it.

## Phase 0: Stabilize the scaffold
- [ ] Inspect project tree and reconcile source paths with repository layout.
- [ ] Confirm Gradle, Kotlin, Xposed API, and DexKit versions against current primary docs.
- [ ] Verify package scoping and module entry point.
- [ ] Make hook registration idempotent and isolate failures per feature.
- [ ] Define explicit feature states: Disabled, Resolving, Resolved, Installed, Unsupported, Failed.
- [ ] Ensure cached data is version-bound and invalidated on package/version changes.
- [ ] Avoid trusting cached reflection descriptors without validation.

## Phase 1: Resolver diagnostics
- [ ] Define a result type for resolution: candidate count, elapsed time, failure category, and validated target metadata.
- [ ] Keep discovery and hook installation separate.
- [ ] Make ambiguous matches fail safely.
- [ ] Add cache hit/miss/invalidation counters.
- [ ] Add a debug-only diagnostics screen and exportable redacted log.
- [ ] Unit-test resolver state transitions and cache-key logic using controlled fixtures.

## Phase 2: Safe feature slice
- [ ] Implement and test non-sensitive UI/feed improvements first, with user-controlled preferences.
- [x] Prevent placeholder features from appearing operational: planned switches are disabled and hook registration skips unimplemented features.
- [ ] Do not publish reverse-engineered target strings for covertly suppressing read, presence, story-view, or screenshot-notification signals.
- [ ] Require an APK/device validation record before marking any feature as Working.
- [ ] Maintain feature-level kill switches and rollback paths.

## Phase 3: UI
- [ ] Implement S1 companion screen and setup guidance.
- [ ] Implement S2 menu, groups, switches, footer, and detail entry.
- [ ] Implement S3 feature details with truthful limitations and status.
- [ ] Implement S4 confirmation only where spec calls for it.
- [ ] Implement S5 status/log view with redaction and clear-log action.
- [ ] Implement S6 About and disclaimer.
- [ ] Follow spec tokens, motion, layouts, accessibility, and copy.
- [ ] Test TalkBack, 130% font scaling, reduced motion, narrow phones, tablet/fold, and landscape.

## Phase 4: CI and compatibility
- [ ] CI: assembleDebug, unit tests, lint, dependency/license report.
- [ ] Add a version manifest for tested Instagram APK versions (version name/code, date, device/Android, test result).
- [ ] Never label an untested Instagram version as supported.
- [ ] Use a reproducible, manually approved APK acquisition workflow. Do not automate redistribution of third-party APKs.
- [ ] Run smoke tests after every dependency or matcher change.
- [ ] Add release checklist, changelog, compatibility matrix, and rollback notes.

## Definition of done for a feature
1. It has a concise description and known limitations.
2. Its preference state and runtime state are distinct.
3. Resolution is deterministic or fails closed.
4. It has tests for expected, missing, and ambiguous resolution.
5. It has device validation on a documented Instagram version.
6. It produces privacy-safe diagnostics.
7. UI and accessibility checks pass.
