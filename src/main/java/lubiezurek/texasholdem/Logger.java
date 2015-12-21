package lubiezurek.texasholdem;

public class Logger {
	public static void status(String str) {
		System.out.println(str);
	}
	
	public static void error(String str) {
		System.err.println("ERROR!: " + str);
	}
	
	public static void exception(Exception ex) {
		System.err.println("EXCEPTION!: " + ex.toString());
	}
}
