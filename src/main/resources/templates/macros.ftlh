<#macro displayNode name>
void displayNode_${name?uncap_first}(SList head);
</#macro>

<#macro toStringCallback name>
char* ${name?uncap_first}_toString_callback(${name} e);
</#macro>

<#macro compare name>
SListCompare_type compare_${name?uncap_first}(SListSingleData data1, SListSingleData data2);
</#macro>

<#macro searchNode name type>
void searchNode_${name}(SList head, SList *searched, ${type});
</#macro>

<#macro  searchNodeObject name>
void searchNode_${name}(SList head, SList *searched, ${name?cap_first} ${name});
</#macro>

<#macro userStructure name attrs parameters compareAttributes>
/* ### ${name} structure ### */

typedef int (*compare${name?cap_first}Callback)(struct ${name} ${name?uncap_first}1, struct ${name} ${name?uncap_first}2);

struct ${name} {
${attrs}
    int (*compare)(struct ${name} ${name?uncap_first}1, struct ${name} ${name?uncap_first}2);
    void (*display)(struct ${name});
};

${name} new${name?cap_first}(${parameters}, compare${name?cap_first}Callback compare);
void display${name?cap_first}(struct ${name} p);

<#list compareAttributes as attr>
int compare${name?cap_first}On${attr?cap_first}(struct ${name} ${name?uncap_first}1, struct ${name} ${name?uncap_first}2);
</#list>

</#macro>
<#macro union name attrs>
/* ### ${name} union ### */

union ${name} {
${attrs}
};

</#macro>

<#macro enum name attrs>
/* ### ${name} enum ### */

enum ${name} {
${attrs}
};
</#macro>
