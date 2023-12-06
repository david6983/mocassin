#ifndef test12_STRUCTURES_H
#define test12_STRUCTURES_H

/* ### USER SPECIFIC STRUCTURES ### */

typedef struct rectangle rectangle;

/* ### USER SPECIFIC ENUMERATIONS ### */

typedef enum position position;

/* ### position enum ### */

enum position {
	e = 0
};

/* ### USER SPECIFIC UNIONS ### */



/* ### USER SPECIFIC STRUCTURES ### */

/* ### rectangle structure ### */

typedef int (*compareRectangleCallback)(struct rectangle rectangle1, struct rectangle rectangle2);

struct rectangle {
	int x;
	int y;
	position pos;

    int (*compare)(struct rectangle rectangle1, struct rectangle rectangle2);
    void (*display)(struct rectangle);
};

rectangle newRectangle(int x, int y, position pos, compareRectangleCallback compare);
void displayRectangle(struct rectangle p);



#endif //STRUCTURES_H
