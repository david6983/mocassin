package com.david.mocassin.model

import com.david.mocassin.model.c_components.Cunion
import com.david.mocassin.model.c_components.CuserStructure
import com.david.mocassin.model.c_components.c_enum.Cenum
import kotlin.test.Test
import kotlin.test.assertEquals

class UserModelTest {
    @Test
    fun shouldNotAddCenumThatNameAlreadyTakenInUserModel() {

    }

    @Test
    fun shouldFindEnumByName() {
        val u = UserModel("")
        u.add(Cenum("POS"))

        assertEquals(Cenum("POS").name, u.findEnumByName("POS").name)
    }

    @Test
    fun shouldFindUnionByName() {
        val u = UserModel("")
        u.add(Cunion("POS"))

        assertEquals(Cunion("POS").name, u.findUnionByName("POS").name)
    }

    @Test
    fun shouldFindStructByName() {
        val u = UserModel("")
        u.add(CuserStructure("POS"))

        assertEquals(CuserStructure("POS").name, u.findStructByName("POS").name)
    }
}