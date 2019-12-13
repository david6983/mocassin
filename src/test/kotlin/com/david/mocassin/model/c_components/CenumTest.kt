package com.david.mocassin.model.c_components

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.c_enum.Cenum
import org.apache.logging.log4j.kotlin.Logging
import tornadofx.JsonBuilder
import kotlin.test.*

class CenumTest {
    companion object: Logging

    private var enum = Cenum("")
    private val project = ProjectController()

    @BeforeTest
    fun createEnum() {
        enum = Cenum("FOO")
    }

    @Test
    fun shouldCreateEmptyCenum() {
        logger.debug("created empty enum")
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
    fun shouldNotAddAttributeWithReservedName() {
        assertFalse(enum.add("auto"))
        assertFalse(enum.add("break"))
        assertFalse(enum.add("case"))
        assertFalse(enum.add("char"))
        assertFalse(enum.add("const"))
        assertFalse(enum.add("continue"))
        assertFalse(enum.add("default"))
        assertFalse(enum.add("do"))
        assertFalse(enum.add("int"))
        assertFalse(enum.add("long"))
        assertFalse(enum.add("register"))
        assertFalse(enum.add("return"))
        assertFalse(enum.add("short"))
        assertFalse(enum.add("signed"))
        assertFalse(enum.add("sizeof"))
        assertFalse(enum.add("static"))
        assertFalse(enum.add("struct"))
        assertFalse(enum.add("switch"))
        assertFalse(enum.add("typedef"))
        assertFalse(enum.add("union"))
        assertFalse(enum.add("unsigned"))
        assertFalse(enum.add("void"))
        assertFalse(enum.add("volatile"))
        assertFalse(enum.add("while"))
        assertFalse(enum.add("double"))
        assertFalse(enum.add("else"))
        assertFalse(enum.add("enum"))
        assertFalse(enum.add("extern"))
        assertFalse(enum.add("float"))
        assertFalse(enum.add("for"))
        assertFalse(enum.add("goto"))
        assertFalse(enum.add("if"))
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
        project.userModel.add(enum)
        project.userModel.add(Cunion("onion"))

        enum.add("bar", 0)
        enum.add("foo32", 4)

        assertTrue(enum.replaceName(4, "foo34", project))
        assertEquals("foo34", enum.attributes.find { it.value == 4}?.name)
        assertFalse(enum.replaceName(4, "éé--\\", project))
        assertFalse(enum.replaceName(4, "bar", project))
        assertFalse(enum.replaceName(4, "onion", project))
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
    fun shouldReturnAttributesAsJson() {
        val expectedString = "{\"name\":\"FOO\",\"attributes\":[{\"name\":\"foo\",\"value\":0},{\"name\":\"bar\",\"value\":1}]}"

        enum.add("foo")
        enum.add("bar")

        assertEquals(expectedString, enum.toJSON().toString())
    }

    @Test
    fun shouldDisplayAttributesAsCformat() {
        val expected = "\tfoo = 0,\n" +
                       "\tbar = 1,\n" +
                       "\tbar2 = 2"
        enum.add("foo")
        enum.add("bar")
        enum.add("bar2")

        assertEquals(expected, enum.attributesToCformatString())
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
    fun shouldCreateCenumFromJson() {
        enum.add("foo")
        enum.add("bar", 2)

        val e2 = Cenum("")
        e2.updateModel(enum.toJSON())
        assertEquals(e2.toJSON(), enum.toJSON())
    }
}