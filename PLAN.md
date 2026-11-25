# InstaGhost: build plan

Goal: an open-source LSPosed module for Instagram, in the spirit of InstaEclipse.
Design: monochrome, liquid glass. No accent colors.

## Decisions (made by the lead)
- Name: InstaGhost. Package: dev.instaghost.module.
- Framework: classic Xposed API (works with LSPosed). Add a modern-API build later.
- Language: Kotlin. UI: plain Views drawn in code (no module resources needed inside Instagram).
- Hook finding: DexKit string anchors, cached per Instagram version. No hardcoded class names.
- Settings live inside Instagram. Long-press the search tab to open the menu.
- piko: use it as a map of WHAT to hook (class and method targets), not as code to copy.
  piko patches bytecode at build time. InstaGhost hooks at runtime. Check piko's license before copying anything.

## Design system
- Colors: black, white, grays only. Dark and light follow the phone.
- Glass: translucent gradient card, 1dp light border, real blur on Android 12+, soft fallback below.
- Shapes: 28dp corners on sheets. Bottom sheet for the menu.
- Icons: thin 4px line icons (ghost, shield, download, feed). App icon: ring and dot on near-black.
- Type: system font. Bold titles, gray summaries.

## Feature list
Ghost: read receipts, typing, story views, screenshot alerts.
Feed: ads, suggested people, double tap like.
Privacy: clean tracking links.
Next: download media (photo, video, audio only), DM tools, backup and restore, translations, log viewer.

## Team (you are the CEO)
| Agent | Owns | First task |
|---|---|---|
| Lead (me) | Plan, reviews, release | Approve the architecture |
| Reverse engineer | jadx, anchors | Fill every TODO_anchor in Features.kt |
| Hook engineer | features | Make Ghost features pass on 2 Instagram versions |
| UI engineer | glass menu, icons | Polish blur, add haptics, test small screens |
| Downloader | media features | Download share target and audio only |
| QA | device tests | Test matrix: 2 Instagram versions x 2 Android versions |
| Release and docs | README, CI, translations | GitHub Actions build, README, DISCLAIMER |

## Hook discovery (the hard part)
1. Download Instagram from APKMirror. Open it in jadx.
2. Search the feature's behavior (for example "typing" or "mark seen") and find the method that sends it.
3. Pick a stable string inside that method. Put it in the feature's anchors.
4. Run. Check LSPosed logs for "InstaGhost: no target".
5. Repeat on a newer Instagram. If it still matches, the anchor is good.

## Milestones
1. Builds, loads, menu opens. (this scaffold)
2. Ghost features work on one Instagram version.
3. Feed features and link cleaning.
4. Downloads.
5. Second Instagram version, modern API build, first release.

## Known limits
- Anchors are placeholders until checked against a real Instagram APK.
- Not built or tested here. Open in Android Studio and sync.
