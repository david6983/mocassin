<#assign types = slist_data_type?replace("\t","")?replace(",","")>
<#assign variables = slist_data_variables?replace("[","")?replace("]","")?replace(",","")>
<#assign user_structs = slist_data_user_structs?replace("[","")?replace("]","")?replace(",","")>
<#assign user_enums = slist_data_enums_name?replace("[","")?replace("]","")>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "${project_name}_structures.h"
#include "${project_name}_Slist.h"

SListEngine initSlistEngine(void)
{
    SListEngine e = malloc(sizeof *e);
    if (e == NULL)
        return NULL;

    /* AUTO GENERATED CODE */
    <#list types?split("\n") as type>
    SLIST_ENGINE_GENERATE_DISPLAY_NODE_METHODS(e, ${type?uncap_first})
    </#list>

    <#list types?split("\n") as type>
    SLIST_ENGINE_GENERATE_COMPARE_NODE_METHODS(e, ${type?uncap_first})
    </#list>

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
    <#list types?split("\n") as type>
        case ${type}:
            return "${type?uncap_first}";
    </#list>
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

<#list variables?split(" ") as var>
GENERATE_DISPLAY_NODE_METHOD_SIMPLE_TYPE(${var?keep_before(";")}, "\t\t[VALUE] ${var?keep_after(";")} \n")
</#list>

/* display enum */

<#list user_enums?split(" ") as enum>
char* ${enum?keep_before(";")?uncap_first}_toString_callback(${enum?keep_before(";")} e){
    switch(e) {
        <#list enum?keep_after(";")?split("|") as attr>
        case ${attr?replace(",", "")}:
            return "${attr?replace(",", "")}";
        </#list>
        default:
            return "NULL";
    }
}

GENERATE_DISPLAY_NODE_METHOD_ENUM(${enum?keep_before(";")?uncap_first})
</#list>

/* display struct */

<#list user_structs?split(" ") as struct>
GENERATE_DISPLAY_NODE_METHOD_USER_STRUCT(${struct?uncap_first})
</#list>

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

<#list variables?split(" ") as var>
    <#if var?keep_before(";") != "string">
GENERATE_COMPARE_SIMPLE_TYPE_METHOD(${var?keep_before(";")})
    </#if>
</#list>

<#list user_enums?split(" ") as enum>
GENERATE_COMPARE_SIMPLE_TYPE_METHOD(${enum?keep_before(";")?uncap_first})
</#list>

<#list user_structs?split(" ") as struct>
GENERATE_COMPARE_USER_STRUCTURE_COMPARE_METHOD_CALLBACK(${struct?uncap_first})
</#list>

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