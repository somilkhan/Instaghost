# Compatibility and Release Process

## Compatibility records
For each test run record:
- Instagram version name and version code
- Android version/API level
- device class (no unique device identifiers)
- InstaGhost commit
- feature capability outcomes
- build/test outcome

## Status model
- **Supported**: validated against a recorded test matrix.
- **Partial**: some features or device combinations are unvalidated.
- **Unsupported**: known incompatibility or no safe resolver outcome.
- **Unknown**: no evidence yet. This is the default.

## Update workflow
1. Detect host version and compare with known profiles.
2. Run compatibility checks and regression tests against the new APK.
3. Review discovery results and ambiguity; do not promote speculative matches.
4. Update profile metadata and test evidence.
5. Publish release notes and known limitations.

## Diagnostic privacy
Logs should be bounded, local, and sanitized. Never log account identifiers, message content, tokens, URLs with tracking/user data, or host payloads.