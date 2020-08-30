# Mocassin - generate data structures in C

## Table of contents
* [Introduction](#introduction)
* [Installation](#installation)
* [Manual](#manual)

## Introduction

- What is Mocassin ?

This software is graphical interface to generate data structures such as simple linked list in C programming.
The idea comes from my first classes of C when I learned how to create linked lists. I wanted to generalize the variables inside
linked lists without using ```void*```. My teacher told me that we can use ```union``` in C and add as many type as we want in a
linked list. The main issue I found  ```void*``` is, in my opinion, not type-safe to use.

- What are the advantages compared to other technologies ?

Nowadays I know that most of the implementation of linked list use ```void*``` 
such as [GLIB Singly-Linked-Lists ](https://developer.gnome.org/glib/stable/glib-Singly-Linked-Lists.html) or 
[GermainLib](https://github.com/ANTUNESREMI/GermainLib). However, Mocassin provides a safe-type way of generating data structures in C
which is working pretty well.

- Why did you choose kotlin ?

To be honest, I wanted to have a personal project during my kotlin class. 
It was a good chance to apply what I learned in a concret project.

- Why didn't you used swing as your GUI ?

I used the [tornadofx](https://tornadofx.io/) framework which is the translation of javafx in kotlin. 
For tornadofx user, this a great project exemple to fork because they are a lot of features concerning the framework such
as internationalization, wizards, packaging, modals, drawer and so on.

## Installation

### using jar package

The software can be run on any platform using java 11 or above by running
```
java -jar mocassin-VERSION.jar
```

Note: VERSION need to be replaced by the current version you want to use.

### Mac Os X (catalina 10.15 or above)

- download the .pkg file in the release section.

or 

- use the gradle task ```jar2pkg``` to build a .pkg on mac os x.

```bash
chmod +x gradlew
./gradlew jar2pkg
```

### windows and linux

work in progress...

## Manual

see wiki page... (wip)