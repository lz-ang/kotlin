/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.test.generators

import org.jetbrains.kotlin.generators.model.annotation
import org.jetbrains.kotlin.generators.util.TestGeneratorUtil
import org.jetbrains.kotlin.test.TargetBackend
import org.jetbrains.kotlin.test.runners.*
import org.jetbrains.kotlin.test.runners.codegen.AbstractBlackBoxCodegenTest
import org.jetbrains.kotlin.test.runners.codegen.AbstractFirBlackBoxCodegenTest
import org.jetbrains.kotlin.test.runners.codegen.AbstractIrBlackBoxCodegenTest
import org.jetbrains.kotlin.test.runners.ir.AbstractFir2IrTextTest
import org.jetbrains.kotlin.test.runners.ir.AbstractIrTextTest
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

fun generateJUnit5CompilerTests(args: Array<String>) {
    val excludedFirTestdataPattern = "^(.+)\\.fir\\.kts?\$"

    generateTestGroupSuiteWithJUnit5(args) {
        testGroup(testsRoot = "compiler/tests-common-new/tests-gen", testDataRoot = "compiler/testData") {
            testClass<AbstractDiagnosticTest> {
                model("diagnostics/tests", pattern = "^(.*)\\.kts?$", excludedPattern = excludedFirTestdataPattern)
                model("diagnostics/testsWithStdLib", excludedPattern = excludedFirTestdataPattern)
            }

            testClass<AbstractDiagnosticUsingJavacTest> {
                model("diagnostics/tests/javac", pattern = "^(.*)\\.kts?$", excludedPattern = excludedFirTestdataPattern)
            }

            testClass<AbstractDiagnosticsTestWithJsStdLib> {
                model("diagnostics/testsWithJsStdLib")
            }

            testClass<AbstractDiagnosticsTestWithOldJvmBackend> {
                model("diagnostics/testsWithJvmBackend", targetBackend = TargetBackend.JVM_OLD)
            }

            testClass<AbstractDiagnosticsTestWithJvmIrBackend> {
                model("diagnostics/testsWithJvmBackend", targetBackend = TargetBackend.JVM_IR)
            }

            testClass<AbstractDiagnosticsNativeTest> {
                model("diagnostics/nativeTests")
            }

            testClass<AbstractForeignAnnotationsTest> {
                model("foreignAnnotations/tests")
                model("foreignAnnotations/java8Tests", excludeDirs = listOf("jspecify", "typeEnhancementOnCompiledJava"))
            }

            testClass<AbstractForeignAnnotationsNoAnnotationInClasspathTest> {
                model("foreignAnnotations/tests")
                model("foreignAnnotations/java8Tests", excludeDirs = listOf("jspecify", "typeEnhancementOnCompiledJava"))
            }

            testClass<AbstractForeignAnnotationsNoAnnotationInClasspathWithPsiClassReadingTest> {
                model("foreignAnnotations/tests")
                model("foreignAnnotations/java8Tests", excludeDirs = listOf("jspecify", "typeEnhancementOnCompiledJava"))
            }

            testClass<AbstractBlackBoxCodegenTest> {
                model("codegen/box")
            }

            testClass<AbstractIrBlackBoxCodegenTest> {
                model("codegen/box", excludeDirs = listOf("oldLanguageVersions"))
            }

            testClass<AbstractIrTextTest> {
                model("ir/irText")
            }
        }

        // ---------------------------------------------- FIR tests ----------------------------------------------

        testGroup(testsRoot = "compiler/fir/analysis-tests/tests-gen", testDataRoot = "compiler/testData") {
            testClass<AbstractFirDiagnosticTest>(suiteTestClassName = "FirOldFrontendDiagnosticsTestGenerated") {
                model("diagnostics/tests", excludedPattern = excludedFirTestdataPattern)
                model("diagnostics/testsWithStdLib", excludedPattern = excludedFirTestdataPattern)
            }

            testClass<AbstractFirBlackBoxCodegenTest> {
                model("codegen/box", excludeDirs = listOf("oldLanguageVersions"))
            }
        }

        testGroup("compiler/fir/analysis-tests/tests-gen", "compiler/fir/analysis-tests/testData") {
            testClass<AbstractFirDiagnosticTest> {
                model("resolve", pattern = TestGeneratorUtil.KT_WITHOUT_DOTS_IN_NAME)
                model("resolveWithStdlib", pattern = TestGeneratorUtil.KT_WITHOUT_DOTS_IN_NAME)
            }

            testClass<AbstractFirDiagnosticsWithLightTreeTest>(
                annotations = listOf(annotation(Execution::class.java, ExecutionMode.SAME_THREAD))
            ) {
                model("resolve", pattern = TestGeneratorUtil.KT_WITHOUT_DOTS_IN_NAME)
            }
        }

        testGroup(testsRoot = "compiler/fir/analysis-tests/tests-gen", testDataRoot = "compiler/testData") {
            testClass<AbstractFir2IrTextTest> {
                model("ir/irText")
            }
        }
    }
}
