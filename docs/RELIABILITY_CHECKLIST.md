# Runtime reliability checklist

Use this checklist for each runtime change. Build and CI validation do not replace device validation.

## Hook lifecycle
- [ ] Scope only to the expected target package.
- [ ] Registration is idempotent across repeated lifecycle callbacks.
- [ ] Every hook failure is isolated and uses a sanitized category.
- [ ] Cleanup runs in a finally block when resources are opened.
- [ ] Feature installation requires an explicit implemented state and opt-in.
- [ ] Unsupported or ambiguous discovery fails closed.

## Resolver/cache
- [ ] Cache keys include target app version and anchor fingerprint.
- [ ] Cached descriptors are validated before use.
- [ ] Descriptor input is bounded and malformed values are rejected.
- [ ] Discovery results are uniquely matched or rejected.
- [ ] Native bridge lifecycle is serialized and closed safely.
- [ ] Resolver tests cover primitive, object and array parameter types.

## Privacy
- [ ] No message, account, token, cookie, URL, or screenshot data is logged.
- [ ] Diagnostic payloads are bounded and allowlisted.
- [ ] Preference keys are allowlisted.
- [ ] UI accurately distinguishes planned, implemented, and validated.

## Validation record
- CI run and commit SHA:
- Android version:
- Instagram version:
- LSPosed version:
- Device:
- Result and limitations:
