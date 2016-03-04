package com.calc;

// Logger class uses the Singleton design pattern.
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
	
	
	public void log( String sMessage, Level level ) {
		// We have a message with a certain verbosity level.
		// Only log the message if the level is greater or equal to the current Logging level.
		if ( level.compareTo(LoggingLevel) >= 0 ) {

			if ( level == Level.ERROR ) {
				sMessage = "**** ERROR: " + sMessage;
			}
			
			System.out.println( sMessage );
		}

		if ( level == Level.ERROR ) errorCount++;
	}
}