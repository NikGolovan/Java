
## Buffer

``` C
int main(void) {
  int number;
  char answer;
  
  while (answer != 'N' || answer != 'n') {
    scanf("%d%*answer", &number);
    prinft("Continue? (y/n)");
    scanf("%answer, &answer);
  }
}
