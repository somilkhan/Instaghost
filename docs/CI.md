# CI verification

Pull requests and branch pushes run the following Gradle tasks on Java 17:

- `:app:testDebugUnitTest`
- `:app:lintDebug`
- `:app:assembleDebug`

The debug APK is uploaded only when the preceding verification steps succeed. A green CI build is not a substitute for validating Xposed integration and feature behavior on a supported Android device and Instagram build.

## Latest verified run

- Commit: `9ccffe3746f1ea1834aea448994dac2d38de86cb`
- Workflow run: [45836351642](https://github.com/somilkhan/Instaghost/actions/runs/45836351642)
- Result: successful unit tests, Android lint, debug APK assembly, checksum generation, and artifact uploads.
- This confirms the scaffold's CI checks at that commit only. It does not confirm hook behavior on a device or Instagram-version compatibility.
