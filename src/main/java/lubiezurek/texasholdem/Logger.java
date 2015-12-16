package main.java.lubiezurek.texasholdem;

public class Logger {
	public static void status(String str) {
		System.out.println(str);
	}
	
	public static void error(String str) {
		System.out.println("ERROR!: " + str);
	}
	
	public static void exception(Exception ex) {
		System.out.println("EXCEPTION!: " + ex.toString());
		//System.out.println("EXCEPTION!: " + ex.getLocalizedMessage());
	}
}
