/*
 
 USE UTF-8 ENCODING AND A MONOSPACED FONT TO VIEW CORRECTLY
  
 ===================================================================================
 THIS SOURCE IS IN THE PUBLIC DOMAIN
 ===================================================================================

  Web page ...... : n/a

 Basic File Information
 ----------------------

  Class ......... : JulianCalendar
  First Author .. : André-John Mas <ajmas@bigfoot.com>
  Purpose ....... : Representing Julian Date and Time
  Language ...... : Java
  Creation date . : 1999-05-07


 Modification History
 --------------------

  Date ......... : 1999-05-07
  Author ....... : André-John Mas <ajmas@bigfoot.com>
  What ......... : Porting from XsunClock, by John Walker



 Comments / Description
 ----------------------

   The two routines that caclulate the Julian date and time from the
   'standard' calendar were taken from XsunClock, written by John Walker,
   in C. The source for XsunClock can be found at http://www.fourmilab.ch/
   

 ===================================================================================

   This program is in the public domain: "Do what thou wilt shall be the whole
   of the law".

 ===================================================================================

*/


package ajmas74.daylighttracker;
import java.util.Date;

public class JulianCalendar {

    Date theDate = null;
    
    public JulianCalendar () {
        theDate = new Date();
    }
    
    public JulianCalendar ( Date date) {
    	theDate = date;
    }
    
    public JulianCalendar ( long theJulianDate, double theJulianTime ) {
        System.out.println(" Not Implemented ");
    }
    
    public long getJulianDate () {

		int date = theDate.getDate();
		int year = theDate.getYear() + 1900;
		int month = theDate.getMonth() + 1;
		
		//System.out.println("year = " + year);
		
		if (month > 2) {
			month = month - 3;
		} else {
			month = month + 9;
			year--;
		}
		
		int century = (int) (year / 100L);
		year -= 100 * century;

		return date + (century * 146097L) / 4 + (year * 1461L) / 4 +
		       (month * 153L + 2) / 5 + 1721119L;
		       
	}
    
    public double getJulianTime () {
	    return ( getJulianDate() - 0.5)
	            + (( (long) (theDate.getSeconds()) ) +
		        60L * (theDate.getMinutes() + 60L * theDate.getHours() ))
		        / 86400.0;
    }    
    
}