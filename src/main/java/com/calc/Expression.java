package com.calc;

public class Expression {

	private static final String OPERATOR_ADD  = "add";
	private static final String OPERATOR_SUB  = "sub";
	private static final String OPERATOR_MULT = "mult";
	private static final String OPERATOR_DIV  = "div";
	private static final String OPERATOR_LET  = "let";
	
	public int solveExpression( String expression, int level ) {
		int result = 0;

		// Parse the expression into components. 
		ExpressionComponents expressionComponents = new ExpressionComponents();
		parseExpression(expression, expressionComponents, level);

		// Keep track of the nested expression level.
		if ( !expressionComponents.getOperator().equals("") ) level++;
		

		try {
			
			// Handle 5 operators: Add, Sub, Mult, Div, Let
			
			// Add Example: add(5,10)  Result: 5 + 10 = 15
			if ( expressionComponents.getOperator().equals(OPERATOR_ADD)) {
				result = integerOverflowUnderflowCheck( expressionComponents,
							(long)solveExpression(expressionComponents.getArgument1(), level) + 
						 	(long)solveExpression(expressionComponents.getArgument2(), level) );
			}
			
			// Sub Example: sub(10,5)  Result: 10 - 5 = 5
			else if ( expressionComponents.getOperator().equals(OPERATOR_SUB)) {
				result = integerOverflowUnderflowCheck( expressionComponents,
							(long)solveExpression(expressionComponents.getArgument1(), level) - 
							(long)solveExpression(expressionComponents.getArgument2(), level) );
			}
			
			// Mult Example: mult(2,5)  Result: 2 * 5 = 10
			else if ( expressionComponents.getOperator().equals(OPERATOR_MULT)) {
				result = integerOverflowUnderflowCheck( expressionComponents,
							(long)solveExpression(expressionComponents.getArgument1(), level) * 
							(long)solveExpression(expressionComponents.getArgument2(), level) );
			}
			
			// Div Example: div(10,2)  Result: 10 / 2 = 5
			else if ( expressionComponents.getOperator().equals(OPERATOR_DIV)) {
				int resultDenominator = solveExpression(expressionComponents.getArgument2(), level);
				if ( resultDenominator == 0 ) {
					// If the expression will result in a divide by 0, report an error and return 0.
					Logger.getLogger().log( "Divide by 0 in Expression: " + expressionComponents.toString() + "  ...Setting value to 0.", 
											Logger.Level.ERROR );
					result = 0;
				}
				else {
					result = integerOverflowUnderflowCheck( expressionComponents,
								(long)solveExpression(expressionComponents.getArgument1(), level) / 
								(long)resultDenominator );
				}
			}
			
			// Let Example: let(a,5,add(a,a))  Result: add(5,5) -> 5 + 5 = 10
			else if ( expressionComponents.getOperator().equals(OPERATOR_LET)) {
				String variable =  expressionComponents.getArgument1(); 
				String variableValue = String.valueOf(solveExpression(expressionComponents.getArgument2(), level));
				String expressionWithVariable =  expressionComponents.getArgument3();
				
				// If the expression containing the variable is syntactically correct and all spaces have been removed, 
				// the variable can be delimited in 2 ways: "(<var>," or ",<var>)"
				// In the expression we want to replace all occurrences of the variable with the variable value. 
				String replace1 = "\\(" + variable + ",";
				String replace2 = "," + variable + "\\)";
				String replacement1 = "\\(" + variableValue + ",";
				String replacement2 = "," + variableValue + "\\)";
				expressionWithVariable = expressionWithVariable.replaceAll( replace1, replacement1 );
				expressionWithVariable = expressionWithVariable.replaceAll( replace2, replacement2 );
				
				result = solveExpression(expressionWithVariable, level); 
			}	
	
			else if ( !expressionComponents.getOperator().isEmpty()) {
				Logger.getLogger().log( "Operator not recognized: " + expressionComponents.getOperator() + "  ...Operation ignored.",
										Logger.Level.ERROR );
				result = 0;
			}
			
			else {
				result = expressionComponents.getNumber();
			}
			
		}
		catch (ArithmeticException ex) {
			// Catch any type of Arithmetic Exception we haven't taken care of above.
			Logger.getLogger().log( "Arithmetic Exception in Expression: " + expressionComponents.toString() + "  ...Setting value to 0.", 
									Logger.Level.ERROR );
			result = 0;
		}
		
		return result;
	}
	
	// Parse the given expression into components. 
	// An valid Expression will be either:
	//    1. A number
	//    2. An Arithmetic Operator with 2 Arguments.
	//    3. A Let Operator with 3 Arguments.
	// Examples:
	//    - 7                        Number: 7
	//    - add(5,7)                 Operator: add   Arg1: 5         Arg2: 7
	//    - let(a,1,add(a,a))        Operator: let   Arg1: a         Arg2: 1         Arg3: add(a,a)
	//    - mult(add(1,2),add(3,4))  Operator: mult  Arg1: add(1,2)  Arg2: add(3,4)
	private void parseExpression( String expression, ExpressionComponents expressionComponents, int level ) {
		
		// look for the first Operator in the expression.
		// For example, in the expression: "sub(add(4,5),3)" we look for "sub".  
		char[] chars = expression.toCharArray();
		int opEnd = -1;
		for( int i = 0; i < chars.length; i++ ) {
			if ( chars[i] == '(' ) {
				opEnd = i;
				break;
			}
		}
		
		// If we don't find an Operator, the Expression should be a Number.
		// Note that at this point all variables should be replaced by their proper numeric values.
		if (opEnd < 0) {
			try {
				// A Number must be between Integer.MIN_VALUE and Integer.MAX_VALUE.
				// Parse the number as a Long then check it against Integer.MIN_VALUE and Integer.MAX_VALUE.
				long number = Long.parseLong(expression);
				if ( number < Integer.MIN_VALUE ) {
					Logger.getLogger().log( "Number < Integer.MIN_VALUE (-2147483648): " + number + "  ...Setting value to 0.",
											Logger.Level.ERROR );
					expressionComponents.setNumber(0);
				}
				else if ( number > Integer.MAX_VALUE ) {
					Logger.getLogger().log( "Number > Integer.MAX_VALUE (2147483647): " + number + "  ...Setting value to 0.",
											Logger.Level.ERROR );
					expressionComponents.setNumber(0);
				}
				else {
					expressionComponents.setNumber((int)number);
				}
			} 
			catch (NumberFormatException e) {
				Logger.getLogger().log( "Expected a Numeric value: " + expression + "  ...Setting value to 0.",
										Logger.Level.ERROR );
				expressionComponents.setNumber(0);
			}
			return;
		}

		// Operator.
		expressionComponents.setOperator(expression.substring(0,opEnd));

		// Now look for 2 or 3 Arguments.
		int parenCount = 0;
		int argCount = 0;
		int argStart = opEnd + 1;
		int argEnd = -1;
		for( int i = opEnd+1; i < chars.length; i++ ) {

			// Keep a running count of open/close parentheses.
			if      ( chars[i] == '(' ) parenCount++;
			else if ( chars[i] == ')' ) parenCount--;
			
			if ( ( chars[i] == ',' ) && ( parenCount == 0 )) {
				argEnd = i;
				
				String argument = expression.substring(argStart,argEnd);
				
				if      (argCount == 0) expressionComponents.setArgument1(argument);
				else if (argCount == 1) expressionComponents.setArgument2(argument);
				else if (argCount == 2) expressionComponents.setArgument3(argument);
				else {
					Logger.getLogger().log( "An extra argument was found: " + argument + "  ...It will be ignored.",
											Logger.Level.ERROR );
				}
				
				argCount++;
				argStart = i + 1;
			}
		}
		
		String argument = expression.substring(argStart,chars.length-1);
		
		if      (argCount == 0) {
			Logger.getLogger().log( "Only 1 argument was found: " + argument + "  ...Operation ignored.",
									Logger.Level.ERROR );
			expressionComponents.setOperator("");
			expressionComponents.setNumber(0);
		}
		else if (argCount == 1) expressionComponents.setArgument2(argument);
		else if (argCount == 2) expressionComponents.setArgument3(argument);
		else {
			Logger.getLogger().log( "An extra argument was found: " + argument + "  ...It will be ignored.",
									Logger.Level.ERROR );
		}

		argCount++;
		
		// Check that LET has 3 arguments. Otherwise there should be 2 arguments.
		if ( expressionComponents.getOperator().equals(OPERATOR_LET) ) {
			if ( argCount < 3 ) {
				Logger.getLogger().log( "The Let Operator needs 3 arguments, it only has: " + argCount + "  ...Operattion ignored.",
										Logger.Level.ERROR );
				expressionComponents.setOperator("");
				expressionComponents.setNumber(0);
			}
		}
		else {
			if ( argCount > 2 ) {
				Logger.getLogger().log( "An extra argument was found: " + expressionComponents.getArgument3() + "  ...It will be ignored.",
										Logger.Level.ERROR );
			}
		}
		
		// Check that argument 1 of the LET operator is a valid variable.
		// It must be a string of characters, where each character if one of a-z (it has already been 
		// converted to lowercase).
		if ( expressionComponents.getOperator().equals(OPERATOR_LET) ) {
			String argument1 = expressionComponents.getArgument1();
			if ( !argument1.matches("[a-z]+") ) {
				Logger.getLogger().log( "The Let variable name is invalid (must only contain a-z,A-Z): " + argument1 + "  ...Operattion ignored.",
										Logger.Level.ERROR );
				expressionComponents.setOperator("");
				expressionComponents.setNumber(0);
			}
		}

		showParsedExpression( expressionComponents, level );
		
		return;
	}
	
	private void showParsedExpression( ExpressionComponents expressionComponents, int level ) {
		String sParsed = "";
		if ( !expressionComponents.getOperator().equals("") )  sParsed += "  Operator = " + expressionComponents.getOperator();
		if ( !expressionComponents.getArgument1().equals("") ) sParsed += "  Arg1 = "     + expressionComponents.getArgument1();
		if ( !expressionComponents.getArgument2().equals("") ) sParsed += "  Arg2 = "     + expressionComponents.getArgument2();
		if ( !expressionComponents.getArgument3().equals("") ) sParsed += "  Arg3 = "     + expressionComponents.getArgument3();

		sParsed = "Parsed Expression:" + sParsed;
		
		int padding = sParsed.length() + ( 2 * level );
		
		Logger.getLogger().log( String.format("%1$" + padding + "s", sParsed), Logger.Level.DEBUG );
	}
	
	// Integer operations silently wrap the results without indicating underflow/overflow. Yikes!
	// We have to do our own checking for underflow/overflow.
	private int integerOverflowUnderflowCheck( ExpressionComponents expressionComponents, long result ) {
		if ( result < Integer.MIN_VALUE ) {
			Logger.getLogger().log( "Expression value < Integer.MIN_VALUE (-2147483648): " + expressionComponents.toString() + "  ...Setting value to 0.",
									Logger.Level.ERROR );
			result = 0;
		}
		else if ( result > Integer.MAX_VALUE ) {
			Logger.getLogger().log( "Expression value > Integer.MAX_VALUE (2147483647): " + expressionComponents.toString() + "  ...Setting value to 0.",
									Logger.Level.ERROR );
			result = 0;
		}
		return (int)result;
	}
	
}