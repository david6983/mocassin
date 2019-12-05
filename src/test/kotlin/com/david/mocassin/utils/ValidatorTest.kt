package com.david.mocassin.utils

import kotlin.test.*

class ValidatorTest {
    @Test
    fun givenNameShouldFollowCstandard() {
        assertTrue(isNameSyntaxFollowCstandard("foo"))
        assertTrue(isNameSyntaxFollowCstandard("FOO"))
        assertTrue(isNameSyntaxFollowCstandard("bar93"))
        assertTrue(isNameSyntaxFollowCstandard("fo_ee99"))
        assertFalse(isNameSyntaxFollowCstandard("foo-ee"))
        assertFalse(isNameSyntaxFollowCstandard("ff ff"))
        assertFalse(isNameSyntaxFollowCstandard("@e ee-éè"))
    }
}