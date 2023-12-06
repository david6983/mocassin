#ifndef test3_STRUCTURES_H
#define test3_STRUCTURES_H

/* ### USER SPECIFIC STRUCTURES ### */

typedef struct salami salami;

/* ### USER SPECIFIC ENUMERATIONS ### */

typedef enum position position;

/* ### position enum ### */

enum position {
	nw = 0,
	ne = 1
};

/* ### USER SPECIFIC UNIONS ### */

typedef union tree tree;

/* ### tree union ### */

union tree {
	int quad;
	int simple;
};


/* ### USER SPECIFIC STRUCTURES ### */

/* ### salami structure ### */

typedef int (*compareSalamiCallback)(struct salami salami1, struct salami salami2);

struct salami {
	position pos;
	tree date;

    int (*compare)(struct salami salami1, struct salami salami2);
    void (*display)(struct salami);
};

salami newSalami(position pos, tree date, compareSalamiCallback compare);
void displaySalami(struct salami p);



#endif //STRUCTURES_H
