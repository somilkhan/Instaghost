# Instagram update workflow

Instagram APK updates can invalidate resolver anchors, cached descriptors, resource identifiers, and assumptions about lifecycle timing. Treat each upstream version as a compatibility event.

## 1. Capture the build
Record version name/code, APK source and acquisition date, Android version, device model, and module commit. Do not redistribute third-party APKs.

## 2. Review the delta
Compare the new build with the last validated build using local, lawful analysis tools. Inspect relevant class/method structure and resource changes. Keep notes limited to what is necessary for maintenance. Do not publish sensitive or covert-behavior anchors.

## 3. Revalidate discovery
- Run resolver tests and build/lint CI.
- Verify candidates against the new build, not just the cache.
- Confirm ambiguous or missing matches fail closed.
- Invalidate stale version-bound cache entries.
- Ensure scanning is bounded and resource cleanup occurs.

## 4. Smoke test
On a test device and test account:
- Confirm the app launches with the module scoped only to Instagram.
- Confirm initialization and menu entry do not crash.
- Record relevant feature outcomes separately.
- Capture sanitized diagnostics only.

## 5. Release decision
Do not label a build supported without a completed validation record. If evidence is incomplete, mark the version pending or unsupported. Keep a rollback build and document known regressions.

## 6. Monitoring and maintenance
Use user-submitted issue reports with Instagram version, Android/device details, module commit, reproduction steps, and sanitized logs. Avoid collecting account identifiers, message contents, tokens, or unnecessary screenshots. Prioritize crash fixes and compatibility regressions over adding features.
