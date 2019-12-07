package com.david.mocassin.utils

/**
 * match only alphanumeric string
 *
 * Regex description :
 *     ^ : start of string
 *     [ : beginning of character group
 *     a-z : any lowercase letter
 *     A-Z : any uppercase letter
 *     0-9 : any digit
 *     _ : underscore
 *     ] : end of character group
 *     * : zero or more of the given characters
 *     $ : end of string
 */
const val C_VARIABLE_SYNTAX_REGEX = "^[a-zA-Z0-9_]*\$"

val RESERVED_C_WORDS = listOf(
    "auto",
    "break",
    "case",
    "char",
    "const",
    "continue",
    "default",
    "do",
    "int",
    "long",
    "register",
    "return",
    "short",
    "signed",
    "sizeof",
    "static",
    "struct",
    "switch",
    "typedef",
    "union",
    "unsigned",
    "void",
    "volatile",
    "while",
    "double",
    "else",
    "enum",
    "extern",
    "float",
    "for",
    "goto",
    "if"
)

/**
 * The given string should be an alphanumeric string to follow the C syntax
 *
 * We use a regular expression to check the string
 *
 * @param name name of the attribute to verify
 * @return
 */
fun isNameSyntaxFollowCstandard(name: String): Boolean {
    return name.contains(regex = Regex(C_VARIABLE_SYNTAX_REGEX))
}

/**
 * Check if the given name is a reserved word in c programming
 *
 * list of words : https://beginnersbook.com/2014/01/c-keywords-reserved-words/
 *
 * @param name name to check
 * @return 'true' if reserved, 'false' if not reserved
 */
fun isNameReservedWords(name: String): Boolean {
    return RESERVED_C_WORDS.contains(name)
}