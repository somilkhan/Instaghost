package dev.instaghost.module.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class AnchorFingerprintTest {
    @Test fun fingerprintIsStableForSameOrderedAnchors() {
        assertEquals(AnchorFingerprint.of("feed", "suggested"), AnchorFingerprint.of("feed", "suggested"))
    }

    @Test fun fingerprintPreservesAnchorOrder() {
        assertNotEquals(AnchorFingerprint.of("feed", "suggested"), AnchorFingerprint.of("suggested", "feed"))
    }

    @Test fun lengthFramingDistinguishesAmbiguousConcatenations() {
        assertNotEquals(AnchorFingerprint.of("a", "bc"), AnchorFingerprint.of("ab", "c"))
    }

    @Test fun utf8AnchorsAreHandledDeterministically() {
        assertEquals(AnchorFingerprint.of("café", "猫"), AnchorFingerprint.of("café", "猫"))
        assertNotEquals(AnchorFingerprint.of("café"), AnchorFingerprint.of("cafe"))
    }
}
