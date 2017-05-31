package br.com.dasa.relatoriousuario.teste;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TesteGerarExcel {

	public static void main(String[] args) throws ParseException {
		
		DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");

		// Get the date today using Calendar object.
		Date today = Calendar.getInstance().getTime();        
		// Using DateFormat format method we can create a string 
		// representation of a date with the defined format.
		String reportDate = df.format(today);

		// Print what date is today!
		System.out.println("Report Date: " + reportDate);
	}
	
}
