# Feature development contract

## Status is explicit
A feature must not become available merely because its anchor resolves. Use this progression:

1. **Planned**: design and acceptance criteria exist.
2. **Implemented**: code exists, but has not passed device validation.
3. **Verified**: test evidence exists for a named Instagram build and Android/device environment.
4. **Unsupported**: no reliable target or validation for this build.

The scaffold's `implemented` flag remains false until its implementation is safe, reviewed, and testable. Never make the UI infer success from a preference value.

## Resolver discipline
- Use stable, narrowly scoped discovery criteria and a deterministic candidate policy.
- Require exactly one candidate. Zero and multiple matches fail closed.
- Treat cache entries as hints. Re-resolve when descriptors cannot be loaded.
- Do not retain raw method bodies, arbitrary exception messages, or user data in diagnostics.
- Keep expensive scanning out of repeated lifecycle callbacks.
- Release DexKit resources after the scan batch.
- Do not rely on class names, IDs, or strings from an APK without validation against the exact build.

## Test layers
- Unit tests: descriptor parsing, fingerprinting, cache invalidation rules, bounded logs, failure categorization.
- Static checks: lint, dependency checks, and APK build.
- Device smoke test: launch, module scope, companion UI, menu entry, crash-free behavior.
- Feature-specific validation: separate acceptance criteria and evidence. Never claim functionality based solely on successful compilation.

## Pull request checklist
- [ ] Scope is isolated and documented.
- [ ] Failure behavior is safe and app-crash risk is minimized.
- [ ] Unit tests cover positive, negative, malformed, and ambiguous cases.
- [ ] Diagnostics are bounded and redact sensitive data.
- [ ] UI status accurately represents validation.
- [ ] Compatibility record is updated with evidence, not assumptions.
- [ ] Rollback is straightforward.
