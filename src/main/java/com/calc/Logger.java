/**
 * Logger.java
 *
 * Logs messages based on a set logging verbosity level.
 * The Logger class uses the Singleton design pattern.
 *
 * @author Doug Wyllie
 * @version 1.0 Mar 4/16
 */

package com.calc;

public class Logger {
	private static Logger instance = null;
	
	private Logger() {}
	
	public static Logger getLogger() {
		if(instance == null) {
			instance = new Logger();
		}
		return instance;
	}

	
	public enum Level { DEBUG, INFO, ERROR, NONE }
	
	// Default to the ERROR logging level (only Errors will be logged).
	private Level LoggingLevel = Level.ERROR;
	
	public void setLoggingLevel( Level level ) { LoggingLevel = level; }

	
	// Keep track of the number of errors found in the expression.
	private int errorCount = 0;
	public int getErrorCount() { return errorCount; }
	public void setErrorCount( int count ) { errorCount = count; }
	
	
	/**
	 * Logs a message if the given level has the same verbosity or is less verbose than the 
	 * current set logging level. 
	 *  
	 * @param  message  The message string.  
	 * @param  level    The logging level of the message string.
	 */
	public void log( String message, Level level ) {
		if ( level.compareTo(LoggingLevel) >= 0 ) {

			if ( level == Level.ERROR ) {
				message = "**** ERROR: " + message;
			}
			
			System.out.println( message );
		}

		if ( level == Level.ERROR ) errorCount++;
	}
}