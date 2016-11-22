# LexicalAnalyzer
This is a lexical analyzer of java code!

- INPUT -
if a>12.3 then if somma >= 30 then else if somma>nicola else ugo<1.2

- OUTPUT -
Token (if)
Token (ID,a)
Token (RELOP,GT)
Token (NUMBER,12.3)
Token (then)
Token (if)
Token (ID,somma)
Token (RELOP,GE)
Token (NUMBER,30)
Token (then)
Token (else)
Token (if)
Token (somma)
Token (RELOP,GT)
Token (ID,nicola)
Token (else)
Token (ID,ugo)
Token (RELOP,LT)
Token (NUMBER,1.2)
