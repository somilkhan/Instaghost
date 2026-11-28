# Feature implementation gate

This document is the review gate for any feature that touches Instagram runtime behavior.

## Before implementation
- [ ] State the user-visible behavior and its limitations.
- [ ] Identify the smallest possible hook surface and the app-version range.
- [ ] Use DexKit anchors that describe behavior, not brittle obfuscated names.
- [ ] Confirm that the feature does not collect, export, or retain private Instagram content.
- [ ] Keep failure behavior inert: ambiguous resolution must not install a hook.

## During implementation
- [ ] Keep hook installation isolated in a feature class.
- [ ] Use a stable, allowlisted preference key.
- [ ] Avoid network access and remote configuration.
- [ ] Make the feature opt-in, with an obvious disable path.
- [ ] Log only sanitized diagnostic categories, never user content.

## Validation
- [ ] Unit-test resolver descriptor and anchor logic where practical.
- [ ] Run debug build and unit tests on CI.
- [ ] Test on a disposable account and device.
- [ ] Record Instagram version, Android version, device, and result in the compatibility matrix.
- [ ] Test disabled state, ambiguous anchors, and hook failure.
- [ ] Verify no crash or behavior change when the feature is unavailable.

## Release
A feature stays marked planned until code review and device validation are complete. CI success alone is not evidence that a hook works on Instagram.
