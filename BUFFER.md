## Buffer

Lets imagine that we need to create a program that will tell us if the user input was an upper or a lower case by using the following code: 

``` C
#include <stdio.h>

#define ESC 27

int main()
{
	char caracter = 0;

	do {
		printf("Give a caracter:\n");
		scanf("%c", &caracter);

		if (caracter >= 65 && caracter <= 90) {
			printf("This is an upper case caracter\n");
		} else if (caracter >= 97 && caracter <= 122) {
			printf("This is a lower case caracter\n");
		} else {
			printf("Unknown caracter\n");
		}
	} while (caracter != ESC);
	
	return 0;
}
```

The break point of the loop is when user presses the ESC button, which defined as a ``` C #define ESC 27 ``` using it's ```ASCII``` code. 

``` C
printf("Give a caracter:\n");
scanf("%c%*c", &caracter);
```

``` C
int main()
{
	char caracter = 0;

	do {
		printf("Give a caracter:\n");
		scanf("%c%*c", &caracter);

		if (caracter >= 65 && caracter <= 90) {
			printf("This is an upper case caracter\n");
		} else if (caracter >= 97 && caracter <= 122) {
			printf("This is a lower case caracter\n");
		} else {
			printf("Unknown caracter\n");
		}
	} while (caracter != ESC);
	
	return 0;
}
```
