# Diagnostics Bridge: Design and Threat Model

## Current situation
DiagnosticsLog is process-local. Instagram's UID and InstaGhost companion's UID are distinct, and their SharedPreferences files are not shared. Treating preferences as a bridge is incorrect.

## Requirements
- Explicit opt-in toggle in the companion.
- No message contents, credentials, cookies, usernames, account identifiers, URLs or raw exceptions.
- Bounded event count and payload size; stable enum/category values only.
- Clear/reset and retention controls.
- Companion displays data only after authenticating the event source.
- Runtime should not block Instagram UI threads on persistence or IPC.
- Fail closed: if bridge authentication/transport fails, patch behavior must not change.

## Avoid
- Exported ContentProvider without strict caller authentication.
- Unprotected broadcasts or intents that accept arbitrary event payloads.
- Shared external-storage files or world-readable paths.
- Logcat scraping as the companion's primary transport.
- Trusting caller-supplied package names as authentication.

## Candidate transport
Evaluate a dedicated LSPosed-supported communication mechanism or explicit privileged bridge with strong caller validation. Before implementation, document the Android permission model and test it on supported OS versions. If no safe, reliable authentication primitive is available, keep diagnostics in the Instagram runtime and provide a deliberate, user-triggered sanitized export rather than insecure cross-process streaming.

## Test plan
1. Unit-test event schema validation and sanitization.
2. Verify payload and event-count caps.
3. Verify export/clear behavior.
4. Test transport rejection for unauthorized callers.
5. Confirm no sensitive data is emitted by instrumentation.
6. Test on real Android devices; emulator/unit tests alone do not prove runtime bridge safety.
