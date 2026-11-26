# Privacy and runtime safety model

## Defaults
- Features are opt-in and inactive unless explicitly enabled.
- Unresolved or ambiguous targets fail closed. Never guess a method from a partial match.
- Resolver cache entries are version-scoped hints, not authority. Validate cached descriptors before use.
- Diagnostics are bounded, local, and use allowlisted fields and stable failure categories.
- Never collect message text, usernames, account identifiers, session tokens, cookies, private URLs, screenshots, or raw exception messages.

## Runtime lifecycle
1. Confirm the target package and supported runtime context.
2. Resolve only a uniquely matched target with documented anchors.
3. Validate the resolved descriptor and cache key.
4. Install a hook only for a feature that has a reviewed implementation and explicit enablement.
5. On errors, fail closed and record only a stable category.
6. Close resolver resources when lifecycle permits.

## Scope boundary
This project is not intended to covertly alter privacy/security signals, bypass access controls, harvest account data, or monitor other users. Targets must be reviewed for privacy, security, user consent, and Instagram compatibility.

## Review checklist
- [ ] No private content or identifiers in logs, cache, analytics, reports, or issue templates.
- [ ] Feature is opt-in, reversible, and explained in UI.
- [ ] Resolver uses unique matches and rejects ambiguity.
- [ ] Cache is invalidated across app versions and invalid descriptors.
- [ ] Failure behavior is fail-closed.
- [ ] Unit tests cover validation, failure, and boundary conditions.
- [ ] Device testing is documented separately from CI results.
