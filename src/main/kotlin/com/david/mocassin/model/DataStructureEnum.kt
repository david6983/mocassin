package com.david.mocassin.model

enum class DataStructureEnum(val shortname: String) {
    SLIST("SList"),
    DLIST("DList"),
    BTREE("BTree"),
    BSTREE("BSTree"),
    TREE("Tree"),
    QUADTREE("QuadTree"),
    RTREE("R-tree"),
    GRAPH("Graph"),
    HASHTABLE("Hash table");

    override fun toString(): String {
        return shortname
    }

    companion object {
        /**
         * From shortname string value, retrieve the object
         *
         * @param shortname shortname
         * @return
         */
        fun find(shortname: String): DataStructureEnum? {
            return values().find { it.shortname == shortname }
        }
    }
}