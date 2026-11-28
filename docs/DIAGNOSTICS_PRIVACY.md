# Diagnostics privacy

## Collection model
Diagnostics should be local, bounded, and opt-in for any export. No network telemetry is required for resolver or feature status.

## Allowed fields
- Module version and commit.
- Target app version code/name, where known.
- Stable feature key.
- State/outcome enums.
- Candidate count, elapsed time, cache hit/miss, and sanitized failure category.

## Never include
- Instagram handles, account IDs, message contents, conversation names, media, or shared URLs.
- Authentication/session tokens, cookies, headers, or credentials.
- Arbitrary exception text or stack traces in user-facing exports.
- Device identifiers or precise location.

## Export review
Before adding an export path:
- Apply an allowlist of fields rather than serializing objects wholesale.
- Bound the output size and number of events.
- Make export user-initiated.
- Clearly state what is included.
- Provide a clear/reset action for local logs.
