# Compatibility Policy

## Compatibility metadata
Track module version, target package, Instagram version name/code, resolver diagnostic result, known status, and test date. Avoid claiming support based solely on successful package loading.

## Status
- VERIFIED: tested against the stated target build and environment
- PARTIAL: module loads, but feature coverage is incomplete
- UNKNOWN: not validated
- INCOMPATIBLE: known breakage

## Update workflow
1. Detect and record target version/build.
2. Run non-invasive diagnostics and integrity checks.
3. Mark features unsupported when required prerequisites are absent.
4. Never crash the host app due to missing optional structures.
5. Verify a release against a concrete target build and record evidence.
6. Maintain a changelog and regression checklist.

## Regression checklist
- Module loads and unloads/disabled behavior is safe.
- Preferences persist and are read consistently.
- Missing optional components fail gracefully.
- Log storage is bounded and sanitization is effective.
- UI remains responsive and handles empty/error states.
- Confirm test target version/build and device details in the test report.
