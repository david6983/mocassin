<#ftl output_format="plainText">
<#import "macros.ftlh" as macros>
#include <stdio.h>
#include <stdlib.h>

#include "${project_name.name}_structures.h"
#include "${project_name.name}_Slist.h"

${object.name} new${object.name?cap_first}(${object.attributesToString(true)}, compare${object.name?cap_first}Callback compare)
{
    ${object.name} ${object.name[0]?uncap_first};

${object.attributesToDeclarationString()}
        
    ${object.name[0]?uncap_first}.display = display${object.name};
    ${object.name[0]?uncap_first}.compare = compare;

    return ${object.name[0]?uncap_first};
}

<#list object.getAllComparableAttribute() as attr>
int compare${object.name?cap_first}On${attr?cap_first}(struct ${object.name} ${object.name?uncap_first}1, struct ${object.name} ${object.name?uncap_first}2)
{
    //TODO implement this method
    /*
     * By default this function return ERROR (see SListCompare_type is SList.h)
     * Implement you own function by using the following syntax
     *
     * - return LOWER (or -1) to indicate that the first ${object.name} is lower than the second
     * - return HIGHER (or 1) to indicate that the first ${object.name} is higher than the second
     * - return EQUALS (or 0) to indicate that they are equals
     * - return ERROR (or 2) in the other case
     */
    return ERROR;
}
</#list>

void display${object.name?cap_first}(struct ${object.name} ${object.name[0]?uncap_first})
{
    //TODO implement your own function
    printf("\t\t[VALUE] nothing to declare \n");
}


