#ifndef SList_H
#define SList_H

/* GENERATE */
#define NB_DATA_TYPE 5 //except none
/* GENERATE_END */

#define SLIST_DATA_NOT_NULL(list) (list->data != NULL)
#define SLIST_DISPLAY_USER_STRUCT(list ,name) \
    if (list->data->data.name.display != NULL) { \
        list->data->data.name.display(list->data->data.name); \
    } \

#define SLIST_DISPLAY_TYPE(list, type, formatString) (printf(formatString, list->data->data.type))
#define SLIST_ENUM_TOSTRING(name)(name ## _toString_callback(head->data->data.name))
#define SLIST_ENGINE_GENERATE_DISPLAY_NODE_METHODS(engineName, type)\
    engineName->ptrDisplayNode[type] = &displayNode_ ## type;
#define SLIST_ENGINE_GENERATE_COMPARE_NODE_METHODS(engineName, type)\
    engineName->ptrCompareSList[type] = &compare_ ## type; \

#define SLIST_SINGLE_DATA(value, type) ((SListSingleData){.type=value})

#define SLIST_DATA(value, data_type) (&(dataSLIST){.data=(SListSingleData){.data_type=value}, .type=data_type})

#define GENERATE_DISPLAY_NODE_METHOD_SIMPLE_TYPE(type, message) \
    void displayNode_ ## type(SList head){\
        if (SLIST_DATA_NOT_NULL(head)) {\
            displayNode_type(head);\
            SLIST_DISPLAY_TYPE(head, type, message);\
        }\
    }\

#define GENERATE_DISPLAY_NODE_METHOD_ENUM(type)\
    void displayNode_ ## type(SList head){\
        if (SLIST_DATA_NOT_NULL(head)) {\
            displayNode_type(head);\
            printf("\t\t[VALUE] %s \n", SLIST_ENUM_TOSTRING(type));\
        }\
    }\

#define GENERATE_DISPLAY_NODE_METHOD_USER_STRUCT(type) \
    void displayNode_ ## type(SList head){\
        if (SLIST_DATA_NOT_NULL(head)) {\
            displayNode_type(head);\
            SLIST_DISPLAY_USER_STRUCT(head , type);\
        }\
    }\

#define GENERATE_COMPARE_USER_STRUCTURE_COMPARE_METHOD_CALLBACK(type) \
    SListCompare_type compare_ ## type(SListSingleData data1, SListSingleData data2){\
        if (data1.type.compare == NULL) {\
            if (data2.type.compare != NULL) {\
                data2.type.compare(data1.type, data2.type);\
            } else {\
                return ERROR;\
            }\
        } else {\
            return data1.type.compare(data1.type, data2.type);\
        }\
        return ERROR; \
    }\

#define GENERATE_COMPARE_SIMPLE_TYPE_METHOD(type) \
    SListCompare_type compare_ ## type(SListSingleData data1, SListSingleData data2) { \
        if (data1.type < data2.type) { \
            return LOWER; \
        } \
        else if (data1.type > data2.type) { \
            return HIGHER; \
        } \
        else if (data1.type == data2.type) { \
            return EQUALS; \
        } else { \
            return ERROR; \
        } \
    }

typedef enum SListCompare_type {
    LOWER = -1,
    HIGHER = 1,
    EQUALS = 0,
    ERROR = -2
} SListCompare_type;

typedef enum SListDataType {
	position,
	direction,
	number,
	unsigned_number,
	short_number,
    none
} SListDataType;

typedef union SListSingleData {
	position position;
	direction direction;
	int number;
	unsigned int unsigned_number;
	short int short_number;
} SListSingleData;

typedef struct SList_Node* SList;
typedef struct dataSLIST* SListData;
typedef struct SList_ptrFunc* SListEngine;

typedef void(*displayNode)(SList head);
typedef SListCompare_type (*compareNode)(SListSingleData data1, SListSingleData data2);

typedef struct SList_ptrFunc {
    compareNode ptrCompareSList[NB_DATA_TYPE];
    displayNode  ptrDisplayNode[NB_DATA_TYPE];
} SList_ptrFunc;

SListEngine initSlistEngine(void);
void freeSListEngine(SListEngine engine);

typedef struct dataSLIST {
    SListDataType type;
    SListSingleData data;
} dataSLIST;

struct SList_Node {
    SListData data;
    SList next;

    void (*displayNode)(SList head, SListEngine engine);
    void (*delete)(SList* head, SListData data, SListEngine engine);
    void (*pop)(SList* head, SListEngine engine);
    void (*remove_last)(SList* head, SListEngine engine);

    void (*push)(SList* head, SListData data, SListEngine engine);
    void (*append)(SList* head, SListData data, SListEngine engine);
    void (*search)(SList head, SList *searched, SListData data, SListEngine engine);

    SListCompare_type (*compare) (SListData data1, SListData data2, SListEngine engine);
};

/* # constructor */

SList createSList(void);

/* # destructor */

void deleteSList(SList* head);

/* # GENERAL METHODS # */

char* slistDataTypeToString(SListDataType type); 
unsigned int countSlistElement(SList head);

/* # PUBLIC METHODS # */

void displaySList(SList head, SListEngine engine);
void displaySListNode(SList head, SListEngine engine);

void deleteData(SList* head, SListData data, SListEngine engine);
void popNode(SList* head, SListEngine engine);
void removeLastNode(SList* head, SListEngine engine);

void pushNodeData(SList* head, SListData data, SListEngine engine);
void appendNodeData(SList* head, SListData data, SListEngine engine);

/* # PRIVATE # */

void freeNode(SList *head);
void displayNode_type(SList head);
void searchNode(SList head, SList *searched, SListData data, SListEngine engine);
SListCompare_type compare_callback (SListData data1, SListData data2, SListEngine engine);
char* compareTypeToString(SListCompare_type type);
void displayCompareType(SListCompare_type type);
void searchPreviousNode_callback(SList head, SList *searched, dataSLIST  insert, SListEngine engine);

/* GENERATE */

/* callbacks for displaying */

void displayNode_number(SList head);
void displayNode_unsigned_number(SList head);
void displayNode_short_number(SList head);

void displayNode_position(SList head);

char* position_toString_callback(position e);

/* callbacks for searching */
void searchNode_position(SList head, SList *searched, position position);
void searchNode_direction(SList head, SList *searched, direction direction);
void searchNode_number(SList head, SList *searched, int number);
void searchNode_unsigned_number(SList head, SList *searched, unsigned int unsigned_number);
void searchNode_short_number(SList head, SList *searched, short int short_number);

/* callbacks for comparing */
//generé avec des macro de free marker

SListCompare_type compare_position(SListSingleData data1, SListSingleData data2);
SListCompare_type compare_number(SListSingleData data1, SListSingleData data2);
SListCompare_type compare_unsigned_number(SListSingleData data1, SListSingleData data2);
SListCompare_type compare_short_number(SListSingleData data1, SListSingleData data2);

/* GENERATE_END */

#endif //SList_H
