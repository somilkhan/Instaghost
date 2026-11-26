# Release checklist

## Before release
- [ ] CI passes: unit tests, lint, debug assembly.
- [ ] Compatibility matrix has actual device records.
- [ ] Review module scope and permissions.
- [ ] Verify preferences and runtime status are not conflated.
- [ ] Confirm logs contain no personal content or raw exception traces.
- [ ] Verify unsupported or ambiguous cases fail safely.
- [ ] Inspect APK contents and signing configuration.
- [ ] Review dependency updates and license obligations.
- [ ] Update changelog and known limitations.

## Release integrity
- [ ] Build from a tagged commit.
- [ ] Record the commit SHA and artifact hash.
- [ ] Sign release artifacts with a protected signing key.
- [ ] Do not publish an unvalidated build as stable.
- [ ] Preserve rollback instructions.
