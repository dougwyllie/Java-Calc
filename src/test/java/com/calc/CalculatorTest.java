/**
 * CalculatorTest.java
 *
 * Unit tests for the Calculator program.
 *
 * @author Doug Wyllie
 * @version 1.0 Mar 4/16
 */

package com.calc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CalculatorTest extends TestCase
{
    public CalculatorTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( CalculatorTest.class );
    }

    // add(1,2)
    // add(1,2
    // add)1,2(
    // add(add(4,5)),2)
    // let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))
    // let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))
    public void testCalc_Invalid_Parentheses()
    {
		boolean valid;
		
		valid = Calculator.checkExpressionStringParentheses( "add(1,2)" );
        assertTrue( valid );
		
		valid = Calculator.checkExpressionStringParentheses( "add(1,2" );
        assertTrue( !valid );
		
		valid = Calculator.checkExpressionStringParentheses( "add)1,2(" );
        assertTrue( !valid );
		
		valid = Calculator.checkExpressionStringParentheses( "add(add(4,5)),2)" );
        assertTrue( !valid );
		
		valid = Calculator.checkExpressionStringParentheses( "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b))" );
        assertTrue( !valid );
		
		valid = Calculator.checkExpressionStringParentheses( "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))" );
        assertTrue( valid );
    }

    // add(4,5)
    // add(add(add(1,2),add(3,4)),add(5,6))
    public void testCalc_Add()
    {
		int result = 0;
		Expression expression = new Expression();

		Logger.getLogger().setLoggingLevel(Logger.Level.NONE);
		
		result = expression.solveExpression("add(4,5)", 1);
        assertTrue( result == 9 );

        result = expression.solveExpression("add(add(add(1,2),add(3,4)),add(5,6))", 1);
        assertTrue( result == 21 );
    }

    // sub(105,97)
    // sub(sub(sub(22,4),sub(13,-4)),sub(9,4))
    public void testCalc_Sub()
    {
		int result = 0;
		Expression expression = new Expression();

		Logger.getLogger().setLoggingLevel(Logger.Level.NONE);
		
		result = expression.solveExpression("sub(105,97)", 1);
        assertTrue( result == 8 );

        result = expression.solveExpression("sub(sub(sub(22,4),sub(13,-4)),sub(9,4))", 1);
        assertTrue( result == -4 );
    }

    // mult(105,76)
    // mult(mult(mult(-7,42),mult(13,-4)),mult(-9,4))
    public void testCalc_Mult()
    {
		int result = 0;
		Expression expression = new Expression();

		Logger.getLogger().setLoggingLevel(Logger.Level.NONE);
		
		result = expression.solveExpression("mult(105,76)", 1);
        assertTrue( result == 7980 );

        result = expression.solveExpression("mult(mult(mult(-7,42),mult(13,-4)),mult(-9,4))", 1);
        assertTrue( result == -550368 );
    }

    // div(9,3)
    // div(9,4)
    // div(9,5)
    // div(105,26)
    // div(div(1058,43),div(-9,4))
    public void testCalc_Div()
    {
		int result = 0;
		Expression expression = new Expression();

		Logger.getLogger().setLoggingLevel(Logger.Level.NONE);
		
		result = expression.solveExpression("div(9,3)", 1);
        assertTrue( result == 3 );
		
		result = expression.solveExpression("div(9,4)", 1);
        assertTrue( result == 2 );
		
		result = expression.solveExpression("div(9,5)", 1);
        assertTrue( result == 1 );
		
		result = expression.solveExpression("div(105,26)", 1);
        assertTrue( result == 4 );
		
        result = expression.solveExpression("div(div(1058,43),div(-9,4))", 1);
        assertTrue( result == -12 );
    }

    // div(9,0)
    public void testCalc_Divide_By_Zero()
    {
		int result = 0;
		Expression expression = new Expression();
		
		Logger.getLogger().setLoggingLevel(Logger.Level.NONE);
		Logger.getLogger().setErrorCount(0);
		
		// Divide by 0. The result should be 0 with 1 error.
		result = expression.solveExpression("div(9,0)", 1);
        assertTrue( result == 0 );
        assertTrue( Logger.getLogger().getErrorCount() == 1 );
    }

    // let(aa,5,add(cc,aa))
    public void testCalc_Invalid_Variable()
    {
		int result = 0;
		Expression expression = new Expression();
		
		Logger.getLogger().setLoggingLevel(Logger.Level.NONE);
		Logger.getLogger().setErrorCount(0);
		
		// The variable cc is invalid so it will be set to 0. There should be 1 error.
		result = expression.solveExpression("let(aa,5,add(cc,aa))", 1);
        assertTrue( result == 5 );
        assertTrue( Logger.getLogger().getErrorCount() == 1 );
    }

    // add(,)
    public void testCalc_No_Arguments()
    {
		int result = 0;
		Expression expression = new Expression();
		
		Logger.getLogger().setLoggingLevel(Logger.Level.NONE);
		Logger.getLogger().setErrorCount(0);
		
		// 2 arguments are not found. The result should be 0 with 2 errors.
		result = expression.solveExpression("add(,)", 1);
        assertTrue( result == 0 );
        assertTrue( Logger.getLogger().getErrorCount() == 2 );
    }

    // let(a,2,add(a,a),3,4)
    public void testCalc_Too_Many_Arguments()
    {
		int result = 0;
		Expression expression = new Expression();
		
		Logger.getLogger().setLoggingLevel(Logger.Level.NONE);
		Logger.getLogger().setErrorCount(0);
		
		// 2 extra arguments are not found. The result should be 4 with 2 errors.
		result = expression.solveExpression("let(a,2,add(a,a),3,4)", 1);
        assertTrue( result == 4 );
        assertTrue( Logger.getLogger().getErrorCount() == 2 );
    }

    // add(5,add(1))
    public void testCalc_Only_One_Argument()
    {
		int result = 0;
		Expression expression = new Expression();
		
		Logger.getLogger().setLoggingLevel(Logger.Level.NONE);
		Logger.getLogger().setErrorCount(0);
		
		// Only 1 arguments. The result should be 5 with 1 error.
		result = expression.solveExpression("add(5,add(1))", 1);
        assertTrue( result == 5 );
        assertTrue( Logger.getLogger().getErrorCount() == 1 );
    }

    // add(let(bc,add(bc,bc)),6)
    public void testCalc_Let_Only_2_Arguments()
    {
		int result = 0;
		Expression expression = new Expression();
		
		Logger.getLogger().setLoggingLevel(Logger.Level.NONE);
		Logger.getLogger().setErrorCount(0);
		
		// Let only has 2 arguments. The result should be 6 with 1 error.
		result = expression.solveExpression("add(let(bc,add(bc,bc)),6)", 1);
        assertTrue( result == 6 );
        assertTrue( Logger.getLogger().getErrorCount() == 1 );
    }

    // div(9,3,1)
    // div(9,3,1,2)
    public void testCalc_Div_Too_Many_Arguments()
    {
		int result = 0;
		Expression expression = new Expression();
		
		Logger.getLogger().setLoggingLevel(Logger.Level.NONE);
		Logger.getLogger().setErrorCount(0);
		
		// Div has 3 arguments when it only needs 2. The result should be 3 with 1 error.
		result = expression.solveExpression("div(9,3,1)", 1);
        assertTrue( result == 3 );
        assertTrue( Logger.getLogger().getErrorCount() == 1 );
		
		Logger.getLogger().setErrorCount(0);
		
		// Div has 4 arguments when it only needs 2. The result should be 3 with 2 errors.
		result = expression.solveExpression("div(9,3,1,2)", 1);
        assertTrue( result == 3 );
        assertTrue( Logger.getLogger().getErrorCount() == 2 );
    }

    // let(b5,3,add(b5,b5))
    // let(,3,add(b5,b5))
    public void testCalc_Invalid_Let_Variable()
    {
		int result = 0;
		Expression expression = new Expression();
		
		Logger.getLogger().setLoggingLevel(Logger.Level.NONE);
		Logger.getLogger().setErrorCount(0);
		
		// b5 is an invalid variable name (only a-z,A-Z). The result should be 0 with 1 error.
		result = expression.solveExpression("let(b5,3,add(b5,b5))", 1);
        assertTrue( result == 0 );
        assertTrue( Logger.getLogger().getErrorCount() == 1 );

        Logger.getLogger().setErrorCount(0);
		
		// b5 is an invalid variable name (only a-z,A-Z). The result should be 0 with 1 error.
		result = expression.solveExpression("let(,3,add(b5,b5))", 1);
        assertTrue( result == 0 );
        assertTrue( Logger.getLogger().getErrorCount() == 1 );
    }

    // sub(2147483647,1)
    // sub(2147483648,1)
    // add(-2147483648,1)
    // add(-2147483649,1)
    public void testCalc_Invalid_Numbers()
    {
		int result = 0;
		Expression expression = new Expression();
		
		Logger.getLogger().setLoggingLevel(Logger.Level.NONE);
		Logger.getLogger().setErrorCount(0);
		
		// Integer.MAX_VALUE (2147483647) is the largest valid number.
		result = expression.solveExpression("sub(2147483647,1)", 1);
        assertTrue( result == 2147483646 );
        assertTrue( Logger.getLogger().getErrorCount() == 0 );

        Logger.getLogger().setErrorCount(0);
		
		// Integer.MAX_VALUE+1 (2147483648) is invalid.
		result = expression.solveExpression("sub(2147483648,1)", 1);
        assertTrue( result == -1 );
        assertTrue( Logger.getLogger().getErrorCount() == 1 );

        Logger.getLogger().setErrorCount(0);
		
		// Integer.MIN_VALUE (-2147483648) is the smallest valid number.
		result = expression.solveExpression("add(-2147483648,1)", 1);
        assertTrue( result == -2147483647 );
        assertTrue( Logger.getLogger().getErrorCount() == 0 );

        Logger.getLogger().setErrorCount(0);
		
		// Integer.MIN_VALUE-1 (-2147483649) is invalid.
		result = expression.solveExpression("add(-2147483649,1)", 1);
        assertTrue( result == 1 );
        assertTrue( Logger.getLogger().getErrorCount() == 1 );
    }
    
    // add(-2000000000,-1)
    // add(-2000000000,-2000000000)
    // sub(2000000000,1)
    // sub(2147483647,-1)
    // mult(2000000000,1)
    // mult(2000000000,2)
    // div(-2147483648,1)
    // div(-2147483648,-1)
    public void testCalc_Integer_Overflow_Underflow()
    {
		int result = 0;
		Expression expression = new Expression();
		
		Logger.getLogger().setLoggingLevel(Logger.Level.NONE);
		Logger.getLogger().setErrorCount(0);
		
		// Result of -2 billion > Integer.MIN_VALUE (-2147483648). No error.
		result = expression.solveExpression("add(-2000000000,-1)", 1);
        assertTrue( result == -2000000001 );
        assertTrue( Logger.getLogger().getErrorCount() == 0 );

        Logger.getLogger().setErrorCount(0);
		
		// Result of -4 billion < Integer.MIN_VALUE (-2147483648). Underflow error.
		result = expression.solveExpression("add(-2000000000,-2000000000)", 1);
        assertTrue( result == 0 );
        assertTrue( Logger.getLogger().getErrorCount() == 1 );

        Logger.getLogger().setErrorCount(0);
		
		// Result of 2 billion < Integer.MAX_VALUE (2147483647). No error.
		result = expression.solveExpression("sub(2000000000,1)", 1);
        assertTrue( result == 1999999999 );
        assertTrue( Logger.getLogger().getErrorCount() == 0 );

        Logger.getLogger().setErrorCount(0);
		
		// Result of Integer.MAX_VALUE + 1 > Integer.MAX_VALUE (2147483647). Overflow error.
		result = expression.solveExpression("sub(2147483647,-1)", 1);
        assertTrue( result == 0 );
        assertTrue( Logger.getLogger().getErrorCount() == 1 );

        Logger.getLogger().setErrorCount(0);
		
		// Result of 2 billion < Integer.MAX_VALUE (2147483647). No error.
		result = expression.solveExpression("mult(2000000000,1)", 1);
        assertTrue( result == 2000000000 );
        assertTrue( Logger.getLogger().getErrorCount() == 0 );

        Logger.getLogger().setErrorCount(0);
		
		// Result of 4 billion > Integer.MAX_VALUE (2147483647). Overflow error.
		result = expression.solveExpression("mult(2000000000,2)", 1);
        assertTrue( result == 0 );
        assertTrue( Logger.getLogger().getErrorCount() == 1 );

        Logger.getLogger().setErrorCount(0);
		
		// Result of Integer.MIN_VALUE >= Integer.MIN_VALUE (-2147483648). No error.
		result = expression.solveExpression("div(-2147483648,1)", 1);
        assertTrue( result == -2147483648 );
        assertTrue( Logger.getLogger().getErrorCount() == 0 );

        Logger.getLogger().setErrorCount(0);
		
		// Result of Integer.MAX_VALUE + 1 > Integer.MAX_VALUE (2147483647). Overflow error.
		result = expression.solveExpression("div(-2147483648,-1)", 1);
        assertTrue( result == 0 );
        assertTrue( Logger.getLogger().getErrorCount() == 1 );
    }

    // let(a,let(b,241,add(b,b)),let(c,let(d,-5,div(a,d)),mult(a,c)))
    // 9! = mult(2,mult(3,mult(4,mult(5,mult(6,mult(7,mult(8,9)))))))
    public void testCalc_Nested_Expressions()
    {
		int result = 0;
		Expression expression = new Expression();
		
		Logger.getLogger().setLoggingLevel(Logger.Level.NONE);
		Logger.getLogger().setErrorCount(0);
		
		// Nested let.
		result = expression.solveExpression("let(a,let(b,241,add(b,b)),let(c,let(d,-5,div(a,d)),mult(a,c)))", 1);
        assertTrue( result == -46272 );
        assertTrue( Logger.getLogger().getErrorCount() == 0 );

        Logger.getLogger().setErrorCount(0);
		
		// Nested mult.
		result = expression.solveExpression("mult(2,mult(3,mult(4,mult(5,mult(6,mult(7,mult(8,9)))))))", 1);
        assertTrue( result == 362880 );
        assertTrue( Logger.getLogger().getErrorCount() == 0 );
    }
}
