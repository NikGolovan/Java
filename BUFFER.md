## Buffer

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
