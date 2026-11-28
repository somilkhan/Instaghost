# Companion dashboard UI contract

The companion app is a setup and status surface, not a control plane for unverified hooks.

## Information hierarchy
1. Current build status and active feature count.
2. Setup instructions for LSPosed and Instagram scope.
3. Explicit compatibility caveat for the experimental Instagram menu entry.
4. Planned feature availability and honest disabled state.
5. Diagnostics and privacy summary.

## UX principles
- Respect system light/dark mode and contrast.
- Use readable typography, clear section hierarchy, and touch-friendly spacing.
- Keep status labels plain and descriptive; avoid implying features are active.
- Avoid collecting or displaying account data.
- Disabled controls must not appear interactive.
- Scroll content rather than clipping smaller screens.
- Use short setup steps and disclose the unverified resource ID/entry point.

## Validation checklist
- [ ] Compile and lint on CI.
- [ ] Check dark and light mode.
- [ ] Check small and large Android screens.
- [ ] Check font scaling and accessibility services.
- [ ] Check card spacing, scroll behavior and navigation-bar/status-bar contrast.
- [ ] Install on a device and verify onboarding text, without assuming the Instagram entry point works.
- [ ] Test LSPosed setup with a supported target build.
- [ ] Confirm disabled feature rows remain disabled.
