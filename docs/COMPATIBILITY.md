# Compatibility validation

No Instagram release is considered supported until a test record is completed. APK inventory logging reports the base APK and installed split count, but DexKit scanning of split APK DEX content is not implemented or verified.

| Instagram version name | version code | Android version | device | module commit | smoke test | notes |
|---|---:|---|---|---|---|---|
| 456.0.0.53.78 | 386214534 | Not tested | Not tested | APK-analysis commit pending | Pending | APKM sample analyzed; split install and device validation pending |
| Not yet validated | | | | | Pending | |

## Test protocol
- Record the Instagram version name and version code.
- Record Android version and device model.
- Install module, scope only to Instagram, and restart Instagram.
- Confirm module initialization does not crash the app.
- Exercise only features that are documented as safe and supported.
- Capture diagnostics without account data, messages, URLs, tokens, or raw stack traces.
- Mark failures and unsupported builds honestly.
