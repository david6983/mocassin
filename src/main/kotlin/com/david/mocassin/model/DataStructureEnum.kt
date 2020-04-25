package com.david.mocassin.model

enum class DataStructureEnum(val fullname: String, val shortname: String) {
    SLIST("SList (Single-chained Linked List)", "SList"),
    DLIST("DList (Double-chained Linked List)","DList"),
    BTREE("BTree (Binary Tree)","BTree"),
    BSTREE("BSTree (Binary Search Tree)","BSTree"),
    TREE("Tree (Multi-node Tree)","Tree"),
    QUADTREE("QuadTree","QuadTree"),
    RTREE("R-Tree","R-tree"),
    GRAPH("Graph", "Graph"),
    HASHTABLE("Hash table","Hash table");

    override fun toString(): String {
        return fullname
    }
}