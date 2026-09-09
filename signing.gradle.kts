import java.util.Properties
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.kotlin.dsl.withGroovyBuilder

check(pluginManager.hasPlugin("com.android.application")) {
    "Apply com.android.application before signing/signing.gradle.kts"
}

val signingDir = rootProject.file("signing")
val props = Properties().apply {
    signingDir.resolve("signing.properties")
        .inputStream().use { load(it) }
}

// Applied Kotlin scripts do not have the Android plugin types on their classpath.
extensions.getByName("android").withGroovyBuilder {
    val configs = getProperty("signingConfigs") as NamedDomainObjectContainer<*>
    configs.maybeCreate("platform").withGroovyBuilder {
        setProperty("storeFile", signingDir.resolve(props.getProperty("platform.storeFile")))
        setProperty("storePassword", props.getProperty("platform.storePassword"))
        setProperty("keyAlias", props.getProperty("platform.keyAlias"))
        setProperty("keyPassword", props.getProperty("platform.keyPassword"))
    }
}
