# Instagram APK analysis: 456.0.0.53.78

## Sample identity
- Package: `com.instagram.android`
- Version: `456.0.0.53.78`
- Version code: `386214534`
- Variant: arm64-v8a, 160-480 dpi, Android 9+
- Bundle metadata release date: 2026-11-16
- Base APK SHA-256: `915e5d2c89f101cf9950af2e7ef9ea177f3d005b320681c12d59bb4cd4fa7767`

## Bundle layout
The supplied APKM bundle contains a base APK plus configuration and feature splits:
- `base.apk`
- `split_config.mdpi.apk`
- `split_config.xxhdpi.apk`
- `split_executorch.apk`
- `split_heliumcore.apk`
- `split_pytorch.apk`

This is a split installation package, not a standalone APK. Device installation/validation must use a compatible set of splits.

## Base APK inventory
- 32 DEX files, approximately 194.3 MB total uncompressed.
- 7,291 entries under `res/`.
- 3,222 assets, approximately 68.0 MB uncompressed.
- 15 native library files, all observed under `arm64-v8a`, approximately 3.13 MB total uncompressed.
- The bundle includes a Hermes bytecode asset and a compressed native-library payload asset.

## Engineering implications
1. Avoid relying on a single DEX or on simple class-name assumptions.
2. Resolution should be version-scoped and fingerprint-based, with explicit ambiguity handling and metadata validation.
3. Feature discovery and hook registration must be separate steps. Candidate discovery alone does not prove behavior.
4. APK inspection alone does not establish device compatibility or safe feature behavior. Device smoke tests remain pending.
5. The analysis identifies the sample and its broad structure. It does not publish or certify target anchors for privacy-signal interception.

## Status
This build is an analysis target only. It is not listed as supported or device-tested.
