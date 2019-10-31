#ifndef TEST_LINKED_LIST_CLION_STRUCTURES_H
#define TEST_LINKED_LIST_CLION_STRUCTURES_H

/* GENERATE */

/* # Project relative structures # */

typedef struct Product {
char* name;
int quantity;
double price;

// the returned type should be a SListCompare_type
// the condition would be defined in the user interface
// exemple : I want to compare product on quantity
int (*compare)(struct Product product1, struct Product product2);
//auto generated
void (*display)(struct Product);
} Product;

Product newProduct(char* name, int quantity, double price);
int compareProductOnName(struct Product product1, struct Product product2);
void displayProduct(struct Product p);

typedef enum ClientPosition {
SELLER,
CEO
} ClientPosition;

typedef struct Company {
char* name;
unsigned int numberOfClient;

int (*compare)(struct Company company1, struct Company company2);
void (*display)(struct Company);
} Company;

typedef struct Shop {
int stock;
char* name;

int (*compare)(struct Shop shop1, struct Shop shop2);
void (*display)(struct Shop);
} Shop;

typedef struct Client {
char* firstName, lastName;
int age;
ClientPosition position;
union {
Company c;
Shop s;
} bien;

// implements this methods to be part of a SList
int (*compare)(struct Client client1, struct Client client2);
void (*display)(struct Client);
} Client;

/* GENERATE_END */

#endif //TEST_LINKED_LIST_CLION_STRUCTURES_H
