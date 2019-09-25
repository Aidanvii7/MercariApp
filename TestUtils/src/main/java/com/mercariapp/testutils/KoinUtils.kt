package com.mercariapp.testutils

import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier
import org.koin.test.check.CheckedComponent
import org.koin.test.check.ParametersBinding
import org.koin.test.check.ParametersCreator

fun ParametersBinding.createNamed(
    fullyQualifiedName: String,
    qualifier: Qualifier? = null,
    creator: ParametersCreator
): ParametersCreator? {
    val kClass = Class.forName(fullyQualifiedName).kotlin
    return creators.put(CheckedComponent(qualifier, kClass), creator)
}

fun ParametersBinding.parameterBindingsFor(fullyQualifiedName: String, vararg parameters: Any) {
    createNamed(fullyQualifiedName) { parametersOf(*parameters) }
}

class KoinExtension(vararg val koinModules: Module) : BeforeAndAfterEach {
    override fun beforeEach(context: ExtensionContext?) {
        startKoin { modules(*koinModules) }
    }
    override fun afterEach(context: ExtensionContext?) = stopKoin()
}