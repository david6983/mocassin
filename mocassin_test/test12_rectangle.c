#include <stdio.h>
#include <stdlib.h>

#include "test12_structures.h"
#include "test12_Slist.h"

rectangle newRectangle(int x, int y, position pos, compareRectangleCallback compare)
{
    rectangle r;

	r.x = x;
	r.y = y;
	r.pos = pos;
        
    r.display = displayrectangle;
    r.compare = compare;

    return r;
}


void displayRectangle(struct rectangle r)
{
    //TODO implement your own function
    printf("\t\t[VALUE] nothing to declare \n");
}


