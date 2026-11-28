# Contribution QA

## Local checks
Run from the repository root:

```sh
gradle :app:testDebugUnitTest --no-daemon
gradle :app:lintDebug --no-daemon
gradle :app:assembleDebug --no-daemon
```

## Before submitting
- [ ] Change is scoped and described clearly.
- [ ] Tests added or updated for behavior changes.
- [ ] Unit tests, lint and APK assembly succeed.
- [ ] UI checked for contrast, 48dp touch targets, TalkBack labels and font scaling.
- [ ] No private content, identifiers, credentials or tokens in logs, tests or screenshots.
- [ ] Feature claims distinguish static/CI verification from device validation.
- [ ] Manual device results include device, Android, Instagram and LSPosed versions.
- [ ] Diagnostics remain bounded and sanitized.

## CI limitations
CI checks compile/test/lint/assemble only. It does not establish runtime hook behavior or compatibility with any Instagram build.
