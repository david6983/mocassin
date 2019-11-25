package com.david.mocassin.cli

import com.david.mocassin.model.UserModel

fun main(args: Array<String>) {
    val version = 0.1
    val model = UserModel("")

   //TODO create a general cli parser before with "by" from kotlin
    println("Mocassin - linked list generator (cli version $version)")
    println("type 'help' to display the list of command")
    do {
        val input = readLine()!!

        when (input){
            "init" -> {
                println("Enter the name of the project (or package)")
                do {
                    val name = readLine()!!
                    if (name.contains(" ")) {
                        println("[error] the package name should not contains any whitespaces. \u001B[0m")
                    } else {
                        model.packageName = name
                    }
                } while(name.contains(" "))
                println("[LOG] name of the project changed to ${model.packageName}")
            }
            "version" -> println("The current version is $version")
            "us" -> {
                if (model.getAllUserStructureNames().count() != 0) {
                    println("list of all user structures :")
                    for ((index, name) in model.getAllUserStructureNames().withIndex()) {
                        println("$index -> $name")
                    }
                } else {
                    println("[error] no user structures found")
                    println("Type 'create struct' to start creating a user structure")
                }
            }
            "ue" -> {

            }
        }
    } while (input != "quit")
    println("Are you sure you want to quit without saving ?")
}