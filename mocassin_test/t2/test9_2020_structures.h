#ifndef test9_2020_STRUCTURES_H
#define test9_2020_STRUCTURES_H

/* ### USER SPECIFIC STRUCTURES ### */


/* ### USER SPECIFIC ENUMERATIONS ### */

typedef enum position position;

/* ### position enum ### */

enum position {
	left = 0,
	right = 1
};

/* ### USER SPECIFIC UNIONS ### */

typedef union angle angle;

/* ### angle union ### */

union angle {
	int* max;
	position pos;
};


/* ### USER SPECIFIC STRUCTURES ### */


#endif //STRUCTURES_H
