# CI verification

Pull requests and branch pushes run the following Gradle tasks on Java 17:

- `:app:testDebugUnitTest`
- `:app:lintDebug`
- `:app:assembleDebug`

The debug APK is uploaded only when the preceding verification steps succeed. A green CI build is not a substitute for validating Xposed integration and feature behavior on a supported Android device and Instagram build.
