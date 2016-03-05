/**
 * Calculator.java
 * 
 * The Calculator program evaluates an expression supplied in a simple integer expression 
 * language. The program takes input supplied on the command line, computes the result and 
 * prints it to the console. 
 *
 * @author Doug Wyllie
 * @version 1.0 Mar 4/16
 */

package com.calc;

public class Calculator {
	
	public static void main(String[] args) {

		// Default logging level to ERROR.
		Logger.getLogger().setLoggingLevel(Logger.Level.ERROR);

		// Get the expression string from the command line.
		String expressionString = "";
		expressionString = getExpressionString( args );
		
		if ( expressionString.isEmpty() ) {
			showHelp();
			return;
		}

		if ( !checkExpressionStringParentheses( expressionString ) ) {
			Logger.getLogger().log( "Expression: " + expressionString, Logger.Level.INFO );
			Logger.getLogger().log( "Expression string is not valid. Open and closing parentheses do not match.",
									Logger.Level.ERROR );
			return;
		}
		
		// To make it easier to parse the expression:
		//    - Remove all spaces from the expression.
		//    - Convert the expression to lowercase.
		// Note that converting the string to lowercase will mean that variables in the expression
		// will NOT be case sensitive.
		expressionString = expressionString.replaceAll(" ", "");
		expressionString = expressionString.toLowerCase();
		
		Logger.getLogger().log( "Expression: " + expressionString, Logger.Level.INFO );

		int result = 0;
		Expression expression = new Expression();
		
		// Solve the expression.
		result = expression.solveExpression(expressionString, 1);
		
		System.out.println( "Result: " + result );
    }

	// Get and return the expression string from the command line arguments.
	// If the command line arguments are invalid in any way, return an empty string.
	// There must be 1 or 2 command line arguments. They can be in any order. Valid forms are:
	//    add(1,2) -DEBUG
	//    -INFO "mult(3,4)"
	//    let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))
	private static String getExpressionString( String[] args ) {
		
		if ( args.length < 1 || args.length > 2 ) {
			return("");
		}
		
		String expressionString = "";
		if ( args.length == 2 ) {
			// Two arguments. Determine which one is the switch.
			String switchStr = "";
			if ( args[0].charAt(0) == '-' ) {
				switchStr = args[0];
				expressionString = args[1];
			}
			else if ( args[1].charAt(0) == '-' ) {
				switchStr = args[1];
				expressionString = args[0];
			}
			else {
				expressionString = "";
			}
			
			if      ( switchStr.equalsIgnoreCase("-DEBUG") ) Logger.getLogger().setLoggingLevel(Logger.Level.DEBUG);
			else if ( switchStr.equalsIgnoreCase("-INFO") )  Logger.getLogger().setLoggingLevel(Logger.Level.INFO);
			else if ( switchStr.equalsIgnoreCase("-ERROR") ) Logger.getLogger().setLoggingLevel(Logger.Level.ERROR);
			else 
				expressionString = "";
		}
		else {
			expressionString = args[0];
		}
		
		return( expressionString );
	}

	// A valid expression string will have an equal number of opening and closing parentheses
	// as well as a closing parenthesis must always close off an opening parenthesis.
	public static boolean checkExpressionStringParentheses( String expression ) {
		int parenCount = 0;
		char[] chars = expression.toCharArray();
		for( int i = 0; i < chars.length; i++ ) {
			// Keep a running count of open/close parentheses.
			if      ( chars[i] == '(' ) parenCount++;
			else if ( chars[i] == ')' ) parenCount--;
			if ( parenCount < 0 ) return false;
		}
		if ( parenCount != 0 ) return false;
		return true;
	}
		
	private static void showHelp() {
		System.out.println("**** ERROR: Invalid command line arguments.");
		System.out.println("Enter an expression on the command line in the form:");
		System.out.println("   java -jar Calculator.jar -INFO \"add(1,2)\"");
		System.out.println("where:");
		System.out.println("   The verbosity switch -INFO is optional. Valid switches are:");
		System.out.println("      -DEBUG  - displays detailed output, info and errors");
		System.out.println("      -INFO   - displays info and errors");
		System.out.println("      -ERROR  - displays just errors (default)");
		System.out.println("   Valid Operators are: add, sub, mult, div, let");
		System.out.println("   Sample Expressions:");
		System.out.println("      mult(add(1,2),div(9,sub(5,2)))                Result:  9");
		System.out.println("      let(a,5,add(a,a))                             Result: 10");
		System.out.println("      let(a,let(b,10,add(b,b)),let(b,20,add(a,b)))  Result: 40");
	}
}