<#ftl output_format="plainText">
<#import "macros.ftlh" as macros>
<#list user_project_name as project_name>
#ifndef ${project_name.name}_STRUCTURES_H
#define ${project_name.name}_STRUCTURES_H
</#list>

/* ### USER SPECIFIC STRUCTURES ### */

<#if user_structures??>
    <#list user_structures as struct>
typedef struct ${struct.name} ${struct.name};
    </#list>
<#else></#if>

/* ### USER SPECIFIC ENUMERATIONS ### */

<#if user_enums??>
    <#list user_enums as enum>
typedef enum ${enum.name} ${enum.name};
    </#list>
<#else></#if>

<#if user_enums??>
    <#list user_enums as enum>
        <@macros.enum
        name="${enum.name}"
        attrs="${enum.attributesToCformatString()}"
        />
    </#list>
<#else></#if>

/* ### USER SPECIFIC UNIONS ### */

<#if user_unions??>
    <#list user_unions as union>
typedef union ${union.name} ${union.name};
    </#list>
<#else></#if>

<#if user_unions??>
    <#list user_unions as union>
        <@macros.union
            name="${union.name}"
            attrs="${union.attributesToString()}"
        />
    </#list>
<#else></#if>

/* ### USER SPECIFIC STRUCTURES ### */

<#if user_structures??>
    <#list user_structures as struct>
        <@macros.userStructure
        name="${struct.name}"
        attrs="${struct.attributesToString(false)}"
        parameters="${struct.attributesToString(true)}"
        compareAttributes=struct.getAllComparableAttribute()
        />
    </#list>
<#else></#if>

#endif //STRUCTURES_H
