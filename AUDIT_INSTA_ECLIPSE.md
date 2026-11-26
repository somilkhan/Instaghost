# InstaEclipse Source Audit and InstaGhost Adaptation Plan

This audit uses the accessible public fork `Oliver-Johnson/InstaEclipse`, whose README identifies it as a fork of `LumixCode/InstaEclipse` and upstream `ReSo7200/InstaEclipse`. This is a feature-level audit, not a source-code transplant or compatibility claim.

## Reusable ideas to evaluate

- **Settings UX:** discoverable in-host settings, categorized options, searchable feature list, and clear immediate feedback.
- **Companion diagnostics:** runtime module status, readable logs and help links. Keep diagnostics privacy-minimal.
- **Focus controls:** user-configurable feed decluttering and content visibility, subject to safe implementation and transparent scope.
- **Download workflow:** evaluate legitimate media-save workflows without bypassing access controls or disappearing-content safeguards.
- **Customization:** theming, typography and navigation personalization as independent UI concerns.
- **Release engineering:** version metadata, reproducible build configuration, CI, signing separation and shrinker rules.

## Excluded

Do not transplant or implement code designed to defeat privacy signals, read/view tracking, disappearing-content protections, screenshot notifications, private access controls or equivalent safeguards.

## Adaptation order

1. UI presentation and settings discoverability.
2. Preferences persistence and migration.
3. Diagnostic logging and status reporting.
4. Compatibility metadata and graceful degradation.
5. CI, lint and release workflow.
6. Evaluate benign customization features independently, with tests.

## Source limitations

The accessible README and build files were inspected. A complete patch-by-patch code audit is not claimed here. Candidate changes must be reviewed against InstaGhost's architecture and tests before implementation.
