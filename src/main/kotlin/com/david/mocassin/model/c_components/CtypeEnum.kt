package com.david.mocassin.model.c_components

enum class CtypeEnum(val cType: String, val enumValue: String, val displaySymbol: String) : CuserType {
    INT("int", "number", "%d"),
    UNSIGNED_INT("unsigned int", "unsigned_number", "%d"),
    SHORT_INT("short int", "short_number", "%hu"),
    LONG_INT("long int", "long_number", "%ld"),
    FLOAT("float", "real_number", "%f"),
    DOUBLE("double", "double_real_number", "%lf"),
    LONG_DOUBLE("long double", "long_double_real_number", "%Le"),
    CHAR("char", "character", "%c"),
    UNSIGNED_CHAR("unsigned char", "unsigned_character", "%u"),
    VOID("void", "void_value", "%p"),
    STRING("char*", "string", "%s"),
    PTR_INT("int*", "number_ptr", "%p"),
    PTR_UNSIGNED_INT("unsigned int*", "unsigned_number_ptr", "%p"),
    PTR_SHORT_INT("short int*", "short_number_ptr", "%p"),
    PTR_LONG_INT("long int*", "long_number_ptr", "%p"),
    PTR_FLOAT("float*", "real_number_ptr", "%p"),
    PTR_DOUBLE("double*", "double_real_number_ptr", "%p"),
    PTR_LONG_DOUBLE("long double*", "long_double_real_number_ptr", "%p"),
    PTR_CHAR("char*", "character_ptr", "%p"),
    PTR_UNSIGNED_CHAR("unsigned char*", "unsigned_character_ptr", "%p"),
    PTR_VOID("void*", "void_value_ptr", "%p"),
    PTR_STRING("char**", "string_ptr", "%p");

    companion object {
        fun find(value: String): CtypeEnum? {
            return CtypeEnum.values().find { it.cType == value }
        }
    }
}