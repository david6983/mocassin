package com.david.mocassin.model.c_components

import javafx.collections.FXCollections
import javafx.collections.ObservableList

/**
 * Represent a simple type in C programming (already exist in the language)
 *
 * @property cType name with official C programming syntax
 * @property enumValue name equivalent in enum format
 * @property displaySymbol symbol representation for printf and scanf
 */
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
        /**
         * From string value (C syntax of the type), retrieve the object
         *
         * @param value
         * @return
         */
        fun find(value: String): CtypeEnum? {
            return values().find { it.cType == value }
        }

        /**
         * export the enumeration to an observable list of string
         *
         * used by combobox in the view
         *
         * @return list of types in enum
         */
        fun toObservableArrayList() : ObservableList<String> {
            val items: ObservableList<String> = FXCollections.observableArrayList()
            for(type in values()) {
                items.add(type.cType)
            }
            return items
        }
    }
}