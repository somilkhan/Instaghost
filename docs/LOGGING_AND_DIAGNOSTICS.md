# Logging and Diagnostics

## Event schema
- eventId: unique event identifier
- timestamp: epoch milliseconds
- level: DEBUG, INFO, WARN, ERROR
- outcome: APPLIED, FAILED, SKIPPED, INFO
- component: engine, resolver, compatibility, feature, configuration
- featureId: nullable stable registry ID
- targetPackage: target package name
- targetVersion: nullable version/build
- message: short sanitized explanation
- details: optional sanitized diagnostics

## Requirements
- Never capture message content, account credentials, private media, or unrelated app data.
- Keep persistence bounded by count and/or storage size; rotate deterministically.
- Filter by outcome, severity, feature and time.
- Provide clear-log confirmation and safe export.
- Failed entries should include a concise reason and, where useful, an exception class. Avoid raw stack traces containing sensitive data by default.
- Log feature configuration and actual runtime result separately.

## UI
Live Logs should have outcome chips (Applied, Failed, Skipped), newest-first ordering, expandable diagnostic detail, copy/export, and clear action. Include a useful empty state and a visible storage limit.
