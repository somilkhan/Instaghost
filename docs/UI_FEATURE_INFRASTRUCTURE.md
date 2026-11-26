# UI and Feature Infrastructure Plan

This branch starts the InstaGhost UI and feature-engineering cleanup.

## Navigation
- Home: module enabled state, target app version, engine health, enabled feature count.
- Features: searchable, category-based feature catalog with per-feature state and diagnostic status.
- Live Logs: chronological applied, failed, skipped, and diagnostic events.
- Settings: Appearance, Behavior, Logging, Compatibility, Backup/Restore, About.

## Feature catalog
Each feature entry should define a stable ID, display name, category, concise description, default state, availability/compatibility status, and implementation status. UI should consume registry metadata and must not depend directly on hook implementation classes.

Suggested groups: Feed, Media, Interface, Utilities. Feature toggles must distinguish requested configuration from actual runtime status.

## Live logging
Log records should include timestamp, severity, event outcome (APPLIED, FAILED, SKIPPED, INFO), feature ID when applicable, target app version, concise message, and safe diagnostic detail. Avoid recording private user content, credentials, or message data. Provide filtering and clear/export controls.

## Settings
Keep settings grouped and searchable. Persist preferences through the existing configuration layer rather than tying UI controls to hook lifecycle classes. Validate configuration and report errors.

## Compatibility
Record target package/version/build, module version, resolver/diagnostic outcome, and supported/unknown/incompatible status. Feature initialization must fail gracefully and emit actionable diagnostics rather than crashing the host.

## Implementation sequence
1. Inspect the existing source tree and preserve its architecture.
2. Refactor navigation and feature catalog presentation.
3. Add structured persistent runtime logs and filtering.
4. Add compatibility diagnostics and status surfaces.
5. Implement and validate independent, non-invasive features in small patches with tests.
6. Validate build, regression tests, and target-device behavior.

## Safety and scope
Do not implement targeted hooks that bypass read receipts, typing indicators, story-view/screenshot alerts, disappearing-content protections, or similar privacy safeguards. Prefer transparent, non-invasive customization and diagnostic features.
