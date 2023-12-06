#ifndef test6_2020_STRUCTURES_H
#define test6_2020_STRUCTURES_H

/* ### USER SPECIFIC STRUCTURES ### */


/* ### USER SPECIFIC ENUMERATIONS ### */

typedef enum position position;

/* ### position enum ### */

enum position {
	left = 0,
	right = 1
};

/* ### USER SPECIFIC UNIONS ### */

typedef union direction direction;

/* ### direction union ### */

union direction {
	int north;
	unsigned int north2;
};


/* ### USER SPECIFIC STRUCTURES ### */


#endif //STRUCTURES_H
