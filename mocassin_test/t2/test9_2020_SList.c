#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "test9_2020_structures.h"
#include "test9_2020_Slist.h"

SListEngine initSlistEngine(void)
{
    SListEngine e = malloc(sizeof *e);
    if (e == NULL)
        return NULL;

    /* AUTO GENERATED CODE */
    SLIST_ENGINE_GENERATE_DISPLAY_NODE_METHODS(e, position)
    SLIST_ENGINE_GENERATE_DISPLAY_NODE_METHODS(e, angle)
    SLIST_ENGINE_GENERATE_DISPLAY_NODE_METHODS(e, number)
    SLIST_ENGINE_GENERATE_DISPLAY_NODE_METHODS(e, unsigned_number)
    SLIST_ENGINE_GENERATE_DISPLAY_NODE_METHODS(e, short_number)
    SLIST_ENGINE_GENERATE_DISPLAY_NODE_METHODS(e, long_number)

    SLIST_ENGINE_GENERATE_COMPARE_NODE_METHODS(e, position)
    SLIST_ENGINE_GENERATE_COMPARE_NODE_METHODS(e, angle)
    SLIST_ENGINE_GENERATE_COMPARE_NODE_METHODS(e, number)
    SLIST_ENGINE_GENERATE_COMPARE_NODE_METHODS(e, unsigned_number)
    SLIST_ENGINE_GENERATE_COMPARE_NODE_METHODS(e, short_number)
    SLIST_ENGINE_GENERATE_COMPARE_NODE_METHODS(e, long_number)

    return e;
}
void freeSListEngine(SListEngine engine)
{
    free(engine);
    engine = NULL;
}

SList createSList(void){
    SList l = malloc(sizeof *l);
    if (l == NULL) {
        return NULL;
    }
    l->next = NULL;
    l->data = NULL;
    l->displayNode = &displaySListNode;

    l->delete = &deleteData;
    l->pop = popNode;
    l->remove_last = removeLastNode;

    l->push = pushNodeData;
    l->append = appendNodeData;

    l->search = &searchNode;
    l->compare = &compare_callback;
    return l;
}
void displaySListNode(SList head, SListEngine engine){
    if (head->data->type != none) {
        (*engine->ptrDisplayNode[head->data->type])(head);
    }
}

void displaySList(SList head, SListEngine engine) {
    printf("[SList] number of element : %d \n", countSlistElement(head));
    SList tmp = head;
    while (tmp != NULL)
    {
        if (tmp->data->type != none) {
            (*(engine->ptrDisplayNode[tmp->data->type]))(tmp);
        }
        tmp = tmp->next;
    }

}

void deleteSList(SList* head){
    if ( head != NULL) {
        SList tmp = NULL;

        while( (*head) != NULL ) {
            tmp = (*head);
            (*head) = (*head)->next;
            freeNode(&tmp);
        }
        free( (*head) );
        (*head) = NULL;
    }

}

void pushNodeData(SList* head, SListData data, SListEngine engine){
    if ((*head)->data == NULL){
        (*head)->data = data;
        return;
    }

    SList tmp = createSList();
    tmp->data = data;

    tmp->next = (*head);
    (*head) = tmp;
}
void appendNodeData(SList* head, SListData data, SListEngine engine) {
    SList current = (*head);
    if (current->data == NULL) {
        current->data = data;
        return;
    }

    while (current->next != NULL) {
        current = current->next;
    }
    current->next = createSList();
    current->next->data = data;
}

void freeNode(SList *head){
    if (head != NULL) {
        if ((*head)->data != NULL) {
            (*head)->data = NULL;
        }
        free((*head));
        (*head) = NULL;
    }
}

char* slistDataTypeToString(SListDataType type){
    switch(type) {
        case position:
            return "position";
        case angle:
            return "angle";
        case number:
            return "number";
        case unsigned_number:
            return "unsigned_number";
        case short_number:
            return "short_number";
        case long_number:
            return "long_number";
        case none:
            return "none";
        default:
            return NULL;
    }
}

/* display generic types */

void displayNode_type(SList head) {
    if (head != NULL) {
        printf("\t[NODE] type -> %s \n", slistDataTypeToString(head->data->type));
    }
}

unsigned int countSlistElement(SList head) {
    SList tmp = head;
    unsigned int count = 0;
    if (head->data != NULL) {
        while (tmp != NULL) {
            count++;
            tmp = tmp->next;
        }
    }
    return count;
}

GENERATE_DISPLAY_NODE_METHOD_SIMPLE_TYPE(number, "\t\t[VALUE] %d \n")
GENERATE_DISPLAY_NODE_METHOD_SIMPLE_TYPE(unsigned_number, "\t\t[VALUE] %d \n")
GENERATE_DISPLAY_NODE_METHOD_SIMPLE_TYPE(short_number, "\t\t[VALUE] %hu \n")
GENERATE_DISPLAY_NODE_METHOD_SIMPLE_TYPE(long_number, "\t\t[VALUE] %ld \n")

/* display enum */

char* position_toString_callback(position e){
    switch(e) {
        case left:
            return "left";
        case right:
            return "right";
        default:
            return "NULL";
    }
}

GENERATE_DISPLAY_NODE_METHOD_ENUM(position)

/* display struct */

GENERATE_DISPLAY_NODE_METHOD_USER_STRUCT()

/* generate --comparison callback-- methods */

SListCompare_type compare_callback (SListData data1, SListData data2, SListEngine engine){
    if (data1 != NULL && data2 != NULL) {
        if (data1->type == data2->type) {
            return engine->ptrCompareSList[data1->type](data1->data, data2->data);
        } else {
            return ERROR;
        }
    } else {
        return ERROR;
    }
}

SListCompare_type compare_string(SListSingleData data1, SListSingleData data2) {
    if (strcmp(data1.string, data2.string) < 0)
        return LOWER;
    else if (strcmp(data1.string, data2.string) > 0)
        return HIGHER;
    else if (strcmp(data1.string, data2.string) == 0)
        return EQUALS;
    else
        return ERROR;
}

char* compareTypeToString(SListCompare_type type){
    switch(type) {
        case EQUALS:
            return "EQUALS";
        case LOWER:
            return "LOWER";
        case HIGHER:
            return "HIGHER";
        default:
            return "ERROR";
    }
}
void displayCompareType(SListCompare_type type){
    printf("%s \n", compareTypeToString(type));
}

GENERATE_COMPARE_SIMPLE_TYPE_METHOD(number)
GENERATE_COMPARE_SIMPLE_TYPE_METHOD(unsigned_number)
GENERATE_COMPARE_SIMPLE_TYPE_METHOD(short_number)
GENERATE_COMPARE_SIMPLE_TYPE_METHOD(long_number)

GENERATE_COMPARE_SIMPLE_TYPE_METHOD(position)

GENERATE_COMPARE_USER_STRUCTURE_COMPARE_METHOD_CALLBACK()

/* search previous node */

void searchPreviousNode_callback(SList head, SList *searched, dataSLIST insert, SListEngine engine){
    SList tmp = head;
    (*searched) = NULL;
    while( tmp != NULL){
        // si le prochain est pas nulle (fin de liste)
        if (tmp->next != NULL) {
            // on verifie que le type inserer est égale à celui du prochain
            if (tmp->next->data->type == insert.type) {
                // on verifie qu'ils sont egaux
                if (insert.type != none) {
                    if (engine->ptrCompareSList[insert.type](insert.data, tmp->next->data->data) == EQUALS) {
                        // par consequent on aura trouver le prochain est tmp deviendra le précédent
                        (*searched) = tmp;
                    }
                }
            }
        }
        tmp = tmp->next;
    }
}

void searchNode(SList head, SList *searched, SListData data, SListEngine engine){
    SList tmp = head;
    (*searched) = NULL;
    while( tmp != NULL)
    {
        if (data != NULL) {
            if (tmp->data->type == data->type
                && engine->ptrCompareSList[data->type](data->data, tmp->data->data) == EQUALS)
                (*searched) = tmp;
        }
        tmp = tmp->next;
    }
}
void deleteData(SList* head, SListData data, SListEngine engine) {

    SList prec = NULL;
    SList toDelete = NULL;
    searchPreviousNode_callback((*head), &prec, *data, engine);
    searchNode((*head), &toDelete, data, engine);

    if( prec == NULL) {
        (*head) = toDelete->next;
        toDelete->next = NULL;
    } else {
        prec->next = toDelete->next;
        toDelete->next = NULL;
    }
    freeNode(&toDelete);
}

void popNode(SList* head, SListEngine engine){
    if ((*head) != NULL) {
        SList toDelete = (*head);
        (*head) = (*head)->next;
        toDelete->next = NULL;
        freeNode(&toDelete);
    }
}
void removeLastNode(SList* head, SListEngine engine){
    if ((*head)->next == NULL) {
        freeNode(head);
        return;
    }

    SList tmp = (*head);
    while(tmp->next->next != NULL) {
        tmp = tmp->next;
    }
    freeNode(&tmp->next);
    tmp->next = NULL;

}