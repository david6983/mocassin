package com.david.mocassin.model.c_components

import com.david.mocassin.model.c_components.c_enum.Cenum
import com.david.mocassin.model.c_components.c_union.Cunion
import com.david.mocassin.model.c_components.c_variable.Cvariable
import tornadofx.JsonBuilder
import javax.json.JsonArray
import javax.json.JsonArrayBuilder
import kotlin.test.*

class CunionTest {
    private var union = Cunion("")
    @BeforeTest
    fun createEnum() {
        union = Cunion("FOO")
    }
    
    @Test
    fun shouldCreateEmptyCunion() {
        assertNotNull(union)
    }

    @Test
    fun shouldNotCreateCunionThatNameAlreadyTakenInUserModel() {

    }

    @Test
    fun shouldGetNameByProperty() {
        assertEquals("FOO", union.nameProperty.value)
        assertEquals(union.nameProperty.value, union.name)
    }

    @Test
    fun shouldGetNameByValue() {
        assertEquals("FOO", union.name)
    }

    @Test
    fun shouldAddVariableInUpperCase() {
        //assertTrue(union.add("ATTRIBUTE"))
    }

    @Test
    fun shouldAddVariableInLowerCase() {

    }

    @Test
    fun shouldAddVariableWithUnderscoreSeparatedWords() {

    }

    @Test
    fun shouldNotAddVariableWithWhiteSpaces() {

    }

    @Test
    fun shouldNotAddVariableDoesAlreadyExistInThisUnion() {

    }

    @Test
    fun shouldHaveCtypeEnumAsTypeOrExistingTypeInUserModel() {

    }

    @Test
    fun shouldNotHaveVariableOfTheCunionType() {
        assertFalse(union.add(
            Cvariable(
                union.name,
                union
            )
        ))
    }

    @Test
    fun shouldRemoveGivenVariable() {
        union.add(Cvariable("foo", CtypeEnum.CHAR))
        assertTrue(union.remove("foo"))
        assertNull(union.attributes.find { it.name == "foo" })
    }

    @Test
    fun shouldRemoveAllVariables() {
        union.add(Cvariable("", CtypeEnum.INT))
        union.add(Cvariable("", CtypeEnum.INT))
        union.add(Cvariable("", CtypeEnum.INT))
        union.clear()
        assertEquals(0, union.attributes.count())
    }

    @Test
    fun shouldReturnAttributesAsString() {

    }

    @Test
    fun shouldVariableAsUniqueName() {

    }

    @Test
    fun shouldReturnVariablesThatFollowCsyntax() {

    }

    @Test
    fun shouldReturnVariablesAsJson() {
        val e = Cenum("pos")
        e.add("x")
        e.add("Y")

        val v1 = Cvariable("foo", CtypeEnum.INT)
        val v2 = Cvariable("BAR", e)

        val expectedJson = "[{\"name\":\"foo\",\"type\":\"int\",\"isPointer\":false,\"isComparable\":false}," +
                "{\"name\":\"BAR\",\"type\":\"pos (enum)\",\"isPointer\":false,\"isComparable\":false}]"

        union.add(v1)
        union.add(v2)

        assertEquals(expectedJson, union.variablesToJSON().build().toString())
    }

    @Test
    fun shouldReturnCunionAsJson() {
        val e = Cenum("pos")
        e.add("x")
        e.add("Y")

        val v1 = Cvariable("foo", CtypeEnum.INT)
        val v2 = Cvariable("BAR", e)

        val variables = JsonBuilder()
        with(variables) {
            add("foo", v1.toJSON())
            add("BAR", v2.toJSON())
        }
        val expectedJson = "{\"name\":\"FOO\",\"variables\":[{\"name\":\"foo\",\"type\":\"int\"," +
                "\"isPointer\":false,\"isComparable\":false},{\"name\":\"BAR\",\"type\":\"pos (enum)\"" +
                ",\"isPointer\":false,\"isComparable\":false}]}"

        union.add(v1)
        union.add(v2)

        assertEquals(expectedJson, union.toJSON().toString())
    }
}