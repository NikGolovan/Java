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

The break point of the loop is when user presses the ESC button, which defined as a ``` #define ESC 27 ``` using it's ```ASCII``` code. 
The problem is, if we execute current code, we will get the next output: 

``` 
A
This is an upper case caracter
Give a caracter:
Unknown caracter
Give a caracter:
```
So, as we can see, the ``` else ``` statement in our ``` if ``` loop was executed as well. Why did it happen? 
The problem hides behind the buffer. The thing is that, the buffer holds all actions that has been sent from the keyboard. Lets imagine that it's our buffer:

```
---+---+---+---+---+
   |   |   |   |   |
---+---+---+---+---+
```

Now it's empty but as soon as we type something on our keyboards, it's going to get filled with information. So, lets type our ``` caracter ``` right now and see what happens :

```
---+---+---+---+---+
%c |   |   |   |   |
---+---+---+---+---+
```

We got our caracter in the buffer. But besides the caracter itself, we also typed ENTER to sumbit our input. So, the buffer look now something like:

```
---+---+---+---+---+
%c | E |   |   |   |
---+---+---+---+---+
```

Where ``` E ``` is the ENTER key. So, what does see the program in the example above with an A caracter? We give the ``` caracter ``` and press ENTER. It fits our condition of ``` if (caracter >= 65 && caracter <= 90) ``` so it's an upper case caracter, we got it right but program will also see the enter caracter in the buffer ``` E ``` which will cause the algorithm to execute the ``` else ``` condition. 

The possible solution will be to clean the buffer using ``` fflush(name_of_buffer) ``` or we could use one technique. 

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
