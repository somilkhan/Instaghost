# Logging and Diagnostics

## Implemented
- Persist a bounded journal (250 events) in module preferences.
- Newest-first viewer accessible from the feature menu.
- Clear journal action.
- Event detail sanitization and capped component/detail lengths.
- Record module initialization and per-feature install routine outcomes.
- Show host package/version and Android/device snapshot in the viewer.

## Event schema
- timestamp: epoch milliseconds
- outcome: APPLIED, FAILED, SKIPPED, INFO
- component: bounded component label
- detail: bounded sanitized text

## Privacy and limitations
- Do not capture message content, account credentials, private media or unrelated app data.
- Sanitization is best-effort, not a guarantee against arbitrary sensitive strings. Prefer fixed diagnostic messages.
- Install routine completion does not prove a feature hook is active or compatible.
- The journal is local preference storage and is not exported.
- Storage is bounded by count (250 events).

## Planned
- Severity/outcome filtering, export with explicit user action, and compatibility status checks.
- Distinguish installation attempt from verified runtime behavior.
