# aosp-signing

Shared signing assets and Gradle configuration for standalone Android apps.
AOSP builds use their own signing configuration.

## Setup

Add this repository at `signing/` in your application's repository root:

```sh
git submodule add git@github.com:OkieLe/aosp-signing.git signing
```

For existing checkouts, run `git submodule update --init signing`.

In the app module's `build.gradle.kts`, apply the script after `plugins` and before
`android`. The `com.android.application` plugin must already be applied:

```kotlin
apply(from = rootProject.file("signing/signing.gradle.kts"))

android {
    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("platform")
        }
    }
}
```

Merge this into the existing build file and remove the old `platform` signing
configuration and its property-loading code. The shared script creates the
configuration; each app chooses which build types use it.

The keystore path in `signing.properties` is relative to `signing/`. No per-machine
signing paths are required. Commit `.gitmodules` and the submodule pointer with
your app changes; publish signing-repository commits before app commits that
reference them.
