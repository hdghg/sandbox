## java-kt-interop

This sandbox is just a reminder that

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }

doesn't affect any java code with null-safety annotations,
including jsr305: The snippet is only for spring modules,
rest of the known libs [always have strict mode](https://kotlinlang.org/docs/java-interop.html#nullability-annotations).

