package application.layer.utils;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LoggerUtility {

	/******************************* Data-Members ****************************/
	// Log control attributes
	private static boolean enableFileLogs    = true;
	private static boolean enableConsoleLogs = true;

	// Enable or disable each log type
	private static boolean warningEnabled = true;
	private static boolean errorEnabled   = true;
	private static boolean debugEnabled   = true;
	private static boolean infoEnabled    = true;

	// Log file path
	private static String LOG_FILE = "logs.txt";

	// ANSI color codes for console
	private static final String RESET_COLOR  = "\u001B[0m";
	private static final String YELLOW_COLOR = "\u001B[33m";
	private static final String RED_COLOR    = "\u001B[31m";
	private static final String CYAN_COLOR   = "\u001B[36m";
	private static final String BLACK_COLOR  = "\u001B[0m";
	
	/********************************** Method ******************************/
	private static void log(String message, String color, String logType) {
		// Get the current time in this format and in English
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());   
		String logEntry = "[" + timestamp + "] [" + logType + "] " + message;

		// Log to console
		if (enableConsoleLogs) {
			System.out.println(color + logEntry + RESET_COLOR);
		}

		// Log to file
		if (enableFileLogs) {
			writeLogToFile(logEntry);
		}
	}

	private static void writeLogToFile(String message) {
		// true >> Enable append mode  
		try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
			writer.write(message + "\n");
		} catch (IOException e) {
			System.err.println("Error writing to log file: " + e.getMessage());
		}
	}
	
	
	/******************************* Shortcut Methods ****************************/
	public static void warning(String message) {
		if (warningEnabled) {
			log(message, YELLOW_COLOR, "WARNING");
		}
	}

	public static void error(String message) {
		if (errorEnabled) {
			log(message, RED_COLOR, "ERROR");
		}
	}

	public static void debug(String message) {
		if (debugEnabled) {
			log(message, CYAN_COLOR, "DEBUG");
		}
	}

	public static void info(String message) {
		if (infoEnabled) {
			log(message, BLACK_COLOR, "INFO");
		}
	}

	/******************************** Setters ********************************/
	public static void setEnableFileLogs(boolean enable) {
		enableFileLogs = enable;
	}

	public static void setEnableConsoleLogs(boolean enable) {
		enableConsoleLogs = enable;
	}

	public static void setWarningEnabled(boolean enable) {
		warningEnabled = enable;
	}

	public static void setErrorEnabled(boolean enable) {
		errorEnabled = enable;
	}

	public static void setDebugEnabled(boolean enable) {
		debugEnabled = enable;
	}

	public static void setInfoEnabled(boolean enable) {
		infoEnabled = enable;
	}

	public static void setLogFile(String filePath) {
		LOG_FILE = filePath;
	}
}