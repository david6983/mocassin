#include <stdio.h>
#include <stdlib.h>

#include "test3_structures.h"
#include "test3_Slist.h"

salami newSalami(position pos, tree date, compareSalamiCallback compare)
{
    salami s;

	s.pos = pos;
	s.date = date;
        
    s.display = displaysalami;
    s.compare = compare;

    return s;
}


void displaySalami(struct salami s)
{
    //TODO implement your own function
    printf("\t\t[VALUE] nothing to declare \n");
}


