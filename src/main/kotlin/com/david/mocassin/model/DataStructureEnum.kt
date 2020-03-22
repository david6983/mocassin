package com.david.mocassin.model

enum class DataStructureEnum(val fullname: String) {
    SLIST("SList (Single-chained Linked List)"),
    DLIST("DList (Double-chained Linked List)"),
    BTREE("BTree (Binary Tree)"),
    BSTREE("BSTree (Binary Search Tree)"),
    TREE("Tree (Multi-node Tree)"),
    GRAPH("Graph"),
    HASHTABLE("Hash table");

    override fun toString(): String {
        return fullname
    }
}