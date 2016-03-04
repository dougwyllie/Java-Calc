package com.calc;

// An valid Expression will be either:
//    1. A number
//    2. An Arithmetic Operator with 2 Arguments.
//    3. A Let Operator with 3 Arguments.
public class ExpressionComponents {

	private int number = 0;
	
	private String operator = "";
	private String argument1 = "";
	private String argument2 = "";
	private String argument3 = "";

	public int getNumber() { return number; }
	public void setNumber(int number) { this.number = number; }
	
	public String getOperator() { return operator; }
	public void setOperator(String operator) { this.operator = operator; }
	
	public String getArgument1() { return argument1; }
	public void setArgument1(String argument) { this.argument1 = argument; }
	
	public String getArgument2() { return argument2; }
	public void setArgument2(String argument) { this.argument2 = argument; }
	
	public String getArgument3() { return argument3; }
	public void setArgument3(String argument) { this.argument3 = argument; }
	
	public String toString() {
		if ( !operator.isEmpty() ) {
			if ( argument3.isEmpty() ) {
				return ( operator + "(" + argument1 + "," + argument2 + ")" );
			}
			else {
				return ( operator + "(" + argument1 + "," + argument2 + "," + argument3 + ")" );
			}
		}
		else {
			return ( String.valueOf( number ) );
		}
	}
}
