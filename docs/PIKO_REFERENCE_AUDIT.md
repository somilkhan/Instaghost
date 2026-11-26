# Piko/Morphe Reference Audit

Date: 2026-11-26
Reference: https://github.com/crimera/piko (GPL-3.0)

## Architectural observations
Piko is a Morphe patch collection/build-time patching project, not an LSPosed runtime module. InstaGhost uses runtime hooking and DexKit resolution. Reuse therefore requires adapting the behavior/target-discovery concept to InstaGhost's lifecycle, resolver, and fail-closed model rather than transplanting patch framework abstractions.

## Candidate areas for further review
- Feed controls and suggested-content filtering: inspect for user-controlled and non-sensitive behavior.
- Link handling: review URI handling and external-opening behavior for privacy, transparency, and compatibility.
- Media UI/download affordances: assess only for content the user is authorized to save, with Android storage/share semantics and safety checks.
- UI utilities: dialog, action-sheet, button and accessibility patterns may be useful as architectural references.
- Resolver fingerprints and entity models: compare approaches, but keep InstaGhost's version-scoped DexKit discovery and ambiguity rejection.

## Exclusions
Do not implement covert suppression or falsification of read receipts, typing/presence, story-view signals, screenshot alerts, or other privacy/security signals. Avoid credentials, session-token extraction, private-message logging, or exporting private account data.

## GPL-3.0 reuse tracking
Before copying or adapting Piko code:
1. Identify the exact source file, upstream commit, and license.
2. Preserve copyright and license notices in reused files.
3. Mark modifications and provide the required corresponding source when distributing derivative GPL-covered work.
4. Add the applicable license and attribution to the project distribution.
5. Track reused/adapted files in a source attribution register.

This document records an initial source-tree inventory, not a claim that individual patch files have been validated against Instagram 456 or adapted into InstaGhost.

## Initial source paths (inventory only)
- `patches/src/main/kotlin/app/crimera/patches/instagram/ads/DisableAdsPatch.kt`
- `patches/src/main/kotlin/app/crimera/patches/instagram/ads/HideSuggestedContentPatch.kt`
- `patches/src/main/kotlin/app/crimera/patches/instagram/links/OpenLinksExternallyPatch.kt`
- `patches/src/main/kotlin/app/crimera/patches/instagram/links/InterceptUriPatch.kt`
- `patches/src/main/kotlin/app/crimera/patches/instagram/links/CustomSharingDomainPatch.kt`

These paths are discovery targets only. No implementation or validation is implied.
