#include <stdio.h>
#include <stdlib.h>

#include "test8_2020_structures.h"
#include "test8_2020_Slist.h"

system newSystem(direction* positionDirection, unsigned int power, compareSystemCallback compare)
{
    system s;

	s.positionDirection = positionDirection;
	s.power = power;
        
    s.display = displaysystem;
    s.compare = compare;

    return s;
}

int compareSystemOnPositionDirection(struct system system1, struct system system2)
{
    //TODO implement this method
    /*
     * By default this function return ERROR (see SListCompare_type is SList.h)
     * Implement you own function by using the following syntax
     *
     * - return LOWER (or -1) to indicate that the first system is lower than the second
     * - return HIGHER (or 1) to indicate that the first system is higher than the second
     * - return EQUALS (or 0) to indicate that they are equals
     * - return ERROR (or 2) in the other case
     */
    return ERROR;
}

void displaySystem(struct system s)
{
    //TODO implement your own function
    printf("\t\t[VALUE] nothing to declare \n");
}


