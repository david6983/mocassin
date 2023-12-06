#ifndef test1_STRUCTURES_H
#define test1_STRUCTURES_H

/* ### USER SPECIFIC STRUCTURES ### */


/* ### USER SPECIFIC ENUMERATIONS ### */

typedef enum position position;

/* ### position enum ### */

enum position {
	n = 0,
	w = 1,
	e = 2,
	s = 3
};

/* ### USER SPECIFIC UNIONS ### */

typedef union data data;

/* ### data union ### */

union data {
	int d;
	position p;
};


/* ### USER SPECIFIC STRUCTURES ### */


#endif //STRUCTURES_H
