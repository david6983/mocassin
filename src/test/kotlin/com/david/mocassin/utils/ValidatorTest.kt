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

    @Test
    fun givenNamesShouldBeReserved() {
        assertTrue(isNameReservedWords("auto"))
        assertTrue(isNameReservedWords("break"))
        assertTrue(isNameReservedWords("case"))
        assertTrue(isNameReservedWords("char"))
        assertTrue(isNameReservedWords("const"))
        assertTrue(isNameReservedWords("continue"))
        assertTrue(isNameReservedWords("default"))
        assertTrue(isNameReservedWords("do"))
        assertTrue(isNameReservedWords("int"))
        assertTrue(isNameReservedWords("long"))
        assertTrue(isNameReservedWords("register"))
        assertTrue(isNameReservedWords("return"))
        assertTrue(isNameReservedWords("short"))
        assertTrue(isNameReservedWords("signed"))
        assertTrue(isNameReservedWords("sizeof"))
        assertTrue(isNameReservedWords("static"))
        assertTrue(isNameReservedWords("struct"))
        assertTrue(isNameReservedWords("switch"))
        assertTrue(isNameReservedWords("typedef"))
        assertTrue(isNameReservedWords("union"))
        assertTrue(isNameReservedWords("unsigned"))
        assertTrue(isNameReservedWords("void"))
        assertTrue(isNameReservedWords("volatile"))
        assertTrue(isNameReservedWords("while"))
        assertTrue(isNameReservedWords("double"))
        assertTrue(isNameReservedWords("else"))
        assertTrue(isNameReservedWords("enum"))
        assertTrue(isNameReservedWords("extern"))
        assertTrue(isNameReservedWords("float"))
        assertTrue(isNameReservedWords("for"))
        assertTrue(isNameReservedWords("goto"))
        assertTrue(isNameReservedWords("if"))
    }
}