package com.students.data.gen;

import java.util.Random;

public class Randomus {
	private static final String email = "adbcdefghijklmnopqrstuvwxyz";
	private static final String[] topDomains = { "com", "ru", "moscow", "net", "ua", "uk", "po", "org" };
	private static final String[] domains = { "rambler", "yandex", "google", "gmail", "example", "site", "ia", "link", "mail" };
	private static Random random = new Random();
	
	private static int randomInt() {
		return random.nextInt(10);
	}
	
	private static char randomChar() {
		return email.charAt(random.nextInt(email.length()));
	}
	
	private static String randomDomain() {
		return domains[random.nextInt(domains.length)];
	}
	
	private static String randomTopDomain() {
		return topDomains[random.nextInt(topDomains.length)];
	}
	
	public static String phone() {
		var format = "+7 (%d%d%d) %d%d%d-%d%d-%d%d";
		
		Object[] args = new Object[10];
		for (int i = 0; i < 10; i++) {
			args[i] = randomInt();
		}
		
		return format.formatted(args);
	}
	
	public static String email(String login) {
		var format = "%s_%s_%d@%s.%s";
		
		var nick = new StringBuilder();
		int chars = random.nextInt(2) + 2;
		for (int i = 0; i < chars; i++) {
			nick.append(randomChar());
		}
		
		return format.formatted(login, nick, random.nextInt(20) + 10, randomDomain(), randomTopDomain());
	}
	
	private static String latin(String message) {
		String[] cyr = {" ","а","б","в","г","д","е","ж" ,"з","и","й","к","л","м","н","о","п","р","с","т","у","ф","х","ц","ч" ,"ш" ,"щ"};
	    String[] lat = {" ","a","b","v","g","d","e","zh","z","y","i","k","l","m","n","o","p","r","s","t","u","f","h","c","ch","sh","sch"};
	    
	    StringBuilder builder = new StringBuilder();
	    for (int i = 0; i < message.length(); i++) {
	    	var symbol = message.charAt(i);
	    	
	        for (int j = 0; j < cyr.length; j++ ) {
	            if (symbol == cyr[j].charAt(0)) {
	                builder.append(lat[j]);
	                break;
	            }
	        }
	    }
	    
	    return builder.toString();
	}
	
	public static String login(String firstName, String lastName) {
		return "%s_%s".formatted(latin(firstName.toLowerCase()), latin(lastName.toLowerCase()));
	}
}
