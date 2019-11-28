package com.david.mocassin.model.c_components

import tornadofx.JsonBuilder
import kotlin.test.*

class CenumTest {
    private var enum = Cenum("")
    @BeforeTest
    fun createEnum() {
        enum = Cenum("FOO")
    }

    @Test
    fun shouldCreateEmptyCenum() {
        assertNotNull(enum)
    }

    @Test
    fun shouldGetNameByProperty() {
        assertEquals("FOO", enum.nameProperty.value)
        assertEquals(enum.nameProperty.value, enum.name)
    }

    @Test
    fun shouldGetNameByValue() {
        assertEquals("FOO", enum.name)
    }

    @Test
    fun shouldAddAttributeInUpperCase() {
        assertTrue(enum.add("ATTRIBUTE"))
    }

    @Test
    fun shouldAddAttributeInLowerCase() {
        assertTrue(enum.add("attribute"))
    }

    @Test
    fun shouldAddAttributeWithUnderscoreSeparatedWords() {
        assertTrue(enum.add("attribute_with_underscore"))
    }

    @Test
    fun shouldNotAddAttributeWithWhiteSpaces() {
        assertFalse(enum.add("attribute with white space"))
    }

    @Test
    fun shouldNotAddAttributeWithDash() {
        assertFalse(enum.add("attribute-with-dash"))
    }

    @Test
    fun shouldNotAddAttributeDoesAlreadyExistInThisEnum() {
        enum.add("foo2")
        assertFalse(enum.add("foo2"))
    }

    @Test
    fun shouldRemoveGivenAttributeFromName() {
        enum.add("foo3")
        assertTrue(enum.remove("foo3"))
        assertNull(enum.attributes.find { it.name == "foo3"})
    }

    @Test
    fun shouldRemoveGivenAttributeFromNameAndValue() {
        enum.add("foo4", 4)
        assertTrue(enum.remove("foo4"))
        assertNull(enum.attributes.find { it.name == "foo4" && it.value == 4 })
    }

    @Test
    fun shouldRemoveAllAttributes() {
        enum.add("a")
        enum.add("b")
        enum.add("c")
        enum.add("d")
        enum.clear()
        assertEquals(0, enum.attributes.count())
    }

    @Test
    fun shouldReplaceAttributeValueFromName() {
        enum.add("foo32", 4)
        enum.replaceValue("foo32", 5)
        assertEquals(5, enum.attributes.find { it.name == "foo32"}?.value)
    }

    @Test
    fun shouldReplaceAttributeNamefromValue() {
        enum.add("foo32", 4)
        enum.replaceName(4, "foo34")
        assertEquals("foo34", enum.attributes.find { it.value == 4}?.name)
    }

    @Test
    fun shouldAttributeAsUniqueNameInEnum() {
        enum.add("foo")
        enum.add("boo")
        assertTrue(enum.isAttributeUniqueInEnum("coo"))
        assertFalse(enum.isAttributeUniqueInEnum("foo"))
        assertFalse(enum.isAttributeUniqueInEnum("foo-ee3"))
    }

    @Test
    fun shouldReturnAttributesThatFollowCsyntax() {
        assertTrue(Cenum.isAttributeSyntaxFollowCstandard("foo"))
        assertTrue(Cenum.isAttributeSyntaxFollowCstandard("FOO"))
        assertTrue(Cenum.isAttributeSyntaxFollowCstandard("bar93"))
        assertTrue(Cenum.isAttributeSyntaxFollowCstandard("fo_ee99"))
        assertFalse(Cenum.isAttributeSyntaxFollowCstandard("foo-ee"))
        assertFalse(Cenum.isAttributeSyntaxFollowCstandard("ff ff"))
        assertFalse(Cenum.isAttributeSyntaxFollowCstandard("@e ee-éè"))
    }

    @Test
    fun shouldReturnAttributesAsJson() {
        val attributes = JsonBuilder()
        with(attributes) {
            add("foo", 0)
            add("bar", 1)
        }
        val expectedJson = JsonBuilder()
        with(expectedJson) {
            add("name", "FOO")
            add("attributes", attributes.build())
        }

        enum.add("foo")
        enum.add("bar")

        assertEquals(expectedJson.build().toString(), enum.toJSON().toString())
    }

    @Test
    fun shouldReturnCenumAsJson() {
        val expectedJson = JsonBuilder()
        with(expectedJson) {
            add("foo", 0)
            add("bar", 1)
        }

        enum.add("foo")
        enum.add("bar")

        assertEquals(expectedJson.build().toString(), enum.attributesToJSON().build().toString())
    }

    @Test
    fun shouldReturnCenumAsString() {
        //enum.add("foo")
        //enum.add("bar")

        //val e: JsonObject = JsonObject("")
    }
}