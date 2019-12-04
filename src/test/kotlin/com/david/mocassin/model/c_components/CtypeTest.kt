package com.david.mocassin.model.c_components

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CtypeTest {
    private val type = Ctype(CtypeEnum.CHAR, true)

    @Test
    fun shouldCreateCtypeFromCuserType() {
        assertNotNull(type)
    }

    @Test
    fun shouldHavePointerTypeFromInitialisation() {
        assertTrue(type.isPointer)
    }
}