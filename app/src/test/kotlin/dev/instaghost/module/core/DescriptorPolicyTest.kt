package dev.instaghost.module.core

import org.junit.Assert.*
import org.junit.Test

class DescriptorPolicyTest {
    @Test fun parsesNormalAndEmptyParameterDescriptors() {
        assertEquals(listOf("int", "java.lang.String"), DescriptorPolicy.parse("pkg.Type#method#int,java.lang.String")?.parameters)
        assertEquals(emptyList<String>(), DescriptorPolicy.parse("pkg.Type#method#")?.parameters)
    }

    @Test fun supportsArrayTypesAndNestedClassNames() {
        assertNotNull(DescriptorPolicy.parse("pkg.Type#method#[I,[Ljava.lang.String;,pkg.Outer$Inner"))
    }

    @Test fun rejectsInvalidClassAndMethodIdentifiers() {
        listOf("pkg..Type", ".Type", "Type.", "bad/name", "bad;type").forEach {
            assertNull(DescriptorPolicy.parse("$it#method#"))
        }
        listOf("method.name", "bad/name", "bad name", "").forEach {
            assertNull(DescriptorPolicy.parse("pkg.Type#$it#"))
        }
    }

    @Test fun rejectsMalformedDescriptorsAndExcessiveParameters() {
        listOf("", "a#b", "#b#", "a##", "a#bad name#", "a#b#unknown type",
            "a#b#[Q", "a#b#[[[[[[[[[[I", "a#b#Ljava.lang.String;").forEach {
            assertNull(DescriptorPolicy.parse(it))
        }
        assertNull(DescriptorPolicy.parse("pkg.Type#method#" + (1..129).joinToString(",") { "int" }))
    }

    @Test fun rejectsOversizedDescriptor() {
        assertNull(DescriptorPolicy.parse("pkg.Type#method#" + "x".repeat(4097)))
    }
}
