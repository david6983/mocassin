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
