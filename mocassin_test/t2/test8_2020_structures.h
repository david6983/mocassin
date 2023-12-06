#ifndef test8_2020_STRUCTURES_H
#define test8_2020_STRUCTURES_H

/* ### USER SPECIFIC STRUCTURES ### */

typedef struct system system;

/* ### USER SPECIFIC ENUMERATIONS ### */

typedef enum direction direction;

/* ### direction enum ### */

enum direction {
	north = 0,
	east = 1
};

/* ### USER SPECIFIC UNIONS ### */

typedef union position position;

/* ### position union ### */

union position {
	direction* cartesian;
	int normal;
};


/* ### USER SPECIFIC STRUCTURES ### */

/* ### system structure ### */

typedef int (*compareSystemCallback)(struct system system1, struct system system2);

struct system {
	direction* positionDirection;
	unsigned int power;

    int (*compare)(struct system system1, struct system system2);
    void (*display)(struct system);
};

system newSystem(direction* positionDirection, unsigned int power, compareSystemCallback compare);
void displaySystem(struct system p);

int compareSystemOnPositionDirection(struct system system1, struct system system2);


#endif //STRUCTURES_H
