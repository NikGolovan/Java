
## Buffer

``` C
int main(void) {
  int number;
  char c;
  
  while (answer != 'N' || answer != 'n') {
    scanf("%d%*c", &number);
    prinft("Continue? (y/n)");
    scanf("%c", &answer);
  }
}
