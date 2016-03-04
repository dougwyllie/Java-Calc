# Java-Calc
Command-line Calculator written in Java.

- To build using Maven from the Calculator folder:

    mvn package

- Sample command-line running from the Calculator\Target folder:

    java -jar Calculator.jar "mult(add(2,2),div(9,3))"  -debug

- Expression variables are not case sensitive. The variables "abc" and "ABC" used in an 
  expression are treated as the same variable.

- Integer Arithmetic:

    - Range of values:
        - The Calculator program uses Java Integer arithmetic (primitive data type "int") to do the calculations.
        - This allows a range of integers from Integer.MIN_VALUE (-2147483648) to Integer.MAX_VALUE (2147483647).
        - Integer operations in Java do not indicate Integer underflow and overflow conditions and will silently
          wrap the results (2147483647 + 100 will give a result of -2147483549).
        - Therefore we have to do out own checking for underflow/overflow.
            - The program uses a technique called "Upcasting". Integer values and all intermediary results are
              upcast to Long. The Long values are then checked against Integer.MIN_VALUE and Integer.MAX_VALUE.
              If a value is out of range, an error is logged, otherwise the result is safely downcast to int.
            - Note that Java 8 has new functions addExact(), multiplyExact() and subtractExact() that could also
              have been used. They return a mathematically correct value or throw an ArithmeticException.

    - Division:
        - Integer division rounds down to the nearest integer:
              9 / 2  = 4.5    Integer division result = 4     
              9 / 4  = 2.25   Integer division result = 2
              9 / 5  = 1.8    Integer division result = 1
              9 / 10 =  .9    Integer division result = 0
