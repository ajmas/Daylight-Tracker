/* 
 USE UTF-8 ENCODING AND A MONOSPACED FONT TO VIEW CORRECTLY
 
 ===================================================================================
 THIS SOURCE IS IN THE PUBLIC DOMAIN
 ===================================================================================

  Web page ...... : n/a

 Basic File Information
 ----------------------

  Class ......... : NightDay
  First Author .. : André-John Mas <ajmas@bigfoot.com>
  Purpose ....... : Calculating night time zone
  Language ...... : Java
  Creation date . : 1999-05-07


 Modification History
 --------------------

  Date ......... : 1999-05-07
  Author ....... : André-John Mas <ajmas@bigfoot.com>
  What ......... : Porting from XsunClock, by John Walker
                   Adapting algorithm to draw night, instead of day



 Comments / Description
 ----------------------

   The routines in this class were taken from XsunClock, written by John Walker,
   in C. The source for XsunClock can be found at http://www.fourmilab.ch/
   

 ===================================================================================

   This program is in the public domain: "Do what thou wilt shall be the whole
   of the law".

 ===================================================================================

*/

package ajmas74.daylighttracker;

import java.awt.*;
import java.util.Date;

public class NightDay extends Component implements MapLayer {

	private Date    _userDate = null;
	private Date    _lastCalcDate = null;
	private int     _lastWidth = 0;
	private int     _lastHeight = 0;
	private float   _leftmostLongitude = 180.0f;
	private boolean _hasGraphics2D;	
	
	// not currently used
	private MapCoordRect _mapCoordRect = null;

	public NightDay() {
		this(new Date());
	}

	public NightDay(Date theDate) {
		_userDate = theDate;
		_hasGraphics2D = hasGraphics2D();
	}

	private boolean hasGraphics2D() {
		try {
			this.getClass().getClassLoader().loadClass("java.awt.Graphics2D");
			return true;
		} catch (Exception ex) {
		}
		return false;
	}

	/* This will not trigger a redraw, so it the parent component
	   should call a repaint */
	public void setDate(Date theDate) {
		_userDate = theDate;
	}

	public Date getDate() {
		return _userDate;
	}

	public void paint(Graphics g) {
		super.paint(g);

		int timeZoneOffset = 0;
		int xdots = this.getSize().width;
		int ydots = this.getSize().height;

		_lastCalcDate = _userDate;

		timeZoneOffset = _userDate.getTimezoneOffset();

		_userDate.setMinutes(_userDate.getMinutes() + timeZoneOffset);
		onoon = -1;

		wtabsize = this.getSize().height;
		wtab = new int[wtabsize];
		wtab1 = new int[wtabsize];

//		for (int i = 0; i < wtabsize; i++) {
//			wtab[i] = -1;
//			wtab1[i] = -1;
//		}

		JulianCalendar jc = new JulianCalendar(_userDate);
		System.out.println("A: julian date" + jc.getJulianDate());
		Calendar.getInstance();
		sunPos(jc.getJulianDate(), false);

		double gt = gmst(jc.getJulianTime());

		sunlong = fixangle(180.0 + (sunra - (gt * 15)));
		xl = (int) (sunlong * (xdots / 360.0));

		if (_lastCalcDate != null) {
			_lastCalcDate.setMinutes(_lastCalcDate.getMinutes() + timeZoneOffset);
		}
		/*	
			if (   ( lastCalcDate == null ) 
			    || ( lastCalcDate != userDate )
			    || ( lastWidth != this.getSize().width )
			    || ( lastHeight != this.getSize().height ) ) {	
		*/
		_lastCalcDate = _userDate;

		_lastWidth = this.getSize().width;
		_lastHeight = this.getSize().height;

		/* Below here Java 2 only code */
		// The following needs Java2D to work
		if (_hasGraphics2D) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setComposite(AlphaComposite.SrcOver);
			g2.setColor(Color.black);
			float alpha = 0.7f;
			AlphaComposite ac =
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			g2.setComposite(ac);
		}
		/* Above here Java 2 only code  */

		//System.out.println(sundec);
		//System.out.println(xl);
			
		wtab = projillum(wtab, xdots, ydots, sundec);

		int[] wtabs = wtab;
		wtab = wtab1;
		wtab1 = wtabs;
		
		// adjustment for maps who left most longitude is not +180 degrees
		// I am not too sure whether the adjustments are right
		xl -= (180 - _leftmostLongitude);
		
		//xl -= 180;
		//}	
		//g.setXORMode(new Color(255,255,255));
		moveterm(g, wtab1, xl, wtab, onoon, xdots, ydots);
		onoon = xl;

		_userDate.setMinutes(_userDate.getMinutes() - timeZoneOffset);
		_lastCalcDate.setMinutes(_lastCalcDate.getMinutes() - timeZoneOffset);
	}

	/*
	----------------------------------------------------------------------------
	    The important stuff happens below
	----------------------------------------------------------------------------
	*/

	int onoon = -1; /* Previous pixel longitude of noon */
	int[] wtab = null;
	int[] wtab1 = null;
	long cctime = 0; /* Time to display (real or simulated) */
	//long lctime = 0;          /* Last full calculation time */
	int wtabsize = 0; /* Width table size */
	int xl = 0;

	double sunra;
	double sundec;
	double sunrv;
	double sunlong;

	final int TERMINC = 100; /* Circle segments for terminator */

	/* Fix angle */
	private double fixangle(double a) {
		return ((a) - 360.0 * (Math.floor((a) / 360.0)));
	}

	/* Extract Sign */
	private double sgn(double x) {
		return (((x) < 0) ? -1 : ((x) > 0 ? 1 : 0));
	}

	private void assert(boolean assertion, String theTest) {
		if (!assertion) {
			System.err.println(
				"assertion error, the following was false : " + theTest);
		}
	}

	/*  KEPLER  --	Solve the equation of Kepler.  */
	private double kepler(double m, double ecc) {
		double e;
		double delta;
		double EPSILON = 1E-6;

		e = m = Math.toRadians(m);
		do {
			delta = e - ecc * Math.sin(e) - m;
			e -= delta / (1 - ecc * Math.cos(e));
		} while (Math.abs(delta) > EPSILON);
		return e;
	}

	private void sunPos(double jd, boolean apparent) {

		double t;
		double t2;
		double t3;
		double l;
		double m;
		double e;
		double ea;
		double v;
		double theta;
		double omega;
		double eps;

		/* Time, in Julian centuries of 36525 ephemeris days,
		   measured from the epoch 1900 January 0.5 ET. */

		t = (jd - 2415020.0) / 36525.0;
		t2 = t * t;
		t3 = t2 * t;

		/* Geometric mean longitude of the Sun, referred to the
		   mean equinox of the date. */

		l = fixangle(279.69668 + 36000.76892 * t + 0.0003025 * t2);

		/* Sun's mean anomaly. */

		m = fixangle(358.47583 + 35999.04975 * t - 0.000150 * t2 - 0.0000033 * t3);

		/* Eccentricity of the Earth's orbit. */

		e = 0.01675104 - 0.0000418 * t - 0.000000126 * t2;

		/* Eccentric anomaly. */

		ea = kepler(m, e);

		/* True anomaly */

		v =
			fixangle(
				2
					* Math.toDegrees(
						Math.atan(Math.sqrt((1 + e) / (1 - e)) * Math.tan(ea / 2))));

		/* Sun's true longitude. */

		theta = l + v - m;

		/* Obliquity of the ecliptic. */

		eps = 23.452294 - 0.0130125 * t - 0.00000164 * t2 + 0.000000503 * t3;

		/* Corrections for Sun's apparent longitude, if desired. */

		if (apparent) {
			omega = fixangle(259.18 - 1934.142 * t);
			theta = theta - 0.00569 - 0.00479 * Math.sin(Math.toRadians(omega));
			eps += 0.00256 * Math.cos(Math.toRadians(omega));
		}

		/* Return Sun's longitude and radius vector */

		sunlong = theta;
		sunrv = (1.0000002 * (1 - e * e)) / (1 + e * Math.cos(Math.toRadians(v)));

		/* Determine solar co-ordinates. */

		sunra =
			fixangle(
				Math.toDegrees(
					Math.atan2(
						Math.cos(Math.toRadians(eps)) * Math.sin(Math.toRadians(theta)),
						Math.cos(Math.toRadians(theta)))));
		sundec =
			Math.toDegrees(
				Math.asin(
					Math.sin(Math.toRadians(eps)) * Math.sin(Math.toRadians(theta))));
	}

	/*  GMST  --  Calculate Greenwich Mean Siderial Time for a given
	      instant expressed as a Julian date and fraction.	*/
	private double gmst(double jd) {

		double t;
		double theta0;

		/* Time, in Julian centuries of 36525 ephemeris days,
		   measured from the epoch 1900 January 0.5 ET. */

		t = ((Math.floor(jd + 0.5) - 0.5) - 2415020.0) / 36525.0;

		theta0 = 6.6460656 + 2400.051262 * t + 0.00002581 * t * t;

		t = (jd + 0.5) - (Math.floor(jd + 0.5));

		theta0 += (t * 24.0) * 1.002737908;

		theta0 = (theta0 - 24.0 * (Math.floor(theta0 / 24.0)));

		return theta0;
	}

	/*  PROJILLUM  --  Project illuminated area on the map.  */
	private int[] projillum(int[] lwtab, int xdots, int ydots, double dec) {
		//int i;
		boolean ftf = true;
		int ilon;
		int ilat;
		int lilon = 0;
		int lilat = 0;
		int xt;
		double m;
		double x;
		double y;
		double z;
		double th;
		double lon;
		double lat;
		double s;
		double c;

		/* Clear unoccupied cells in width table */

		for (int i = 0; i < ydots; i++) {
			lwtab[i] = -1;
		}

		/* Build transformation for declination */

		s = Math.sin(-Math.toRadians(dec));
		c = Math.cos(-Math.toRadians(dec));

		/* Increment over a semicircle of illumination */

		for (th = - (Math.PI / 2);
			th <= Math.PI / 2 + 0.001;
			th += Math.PI / TERMINC) {

			/* Transform the point through the declination rotation. */

			x = -s * Math.sin(th);
			y = Math.cos(th);
			z = c * Math.sin(th);

			/* Transform the resulting co-ordinate through the
			   map projection to obtain screen co-ordinates. */

			lon = (y == 0 && x == 0) ? 0.0 : Math.toDegrees(Math.atan2(y, x));
			lat = Math.toDegrees(Math.asin(z));

			ilat = (int) (ydots - (lat + 90) * (ydots / 180.0));
			ilon = (int) (lon * (xdots / 360.0));

			if (ftf) {
				/* First time.  Just save start co-ordinate. */
				lilon = ilon;
				lilat = ilat;
				ftf = false;
			} else {

				/* Trace out the line and set the width table. */

				if (lilat == ilat) {
					lwtab[(ydots - 1) - ilat] = 2 * (ilon == 0 ? 1 : ilon);
				} else {
					m = ((double) (ilon - lilon)) / (ilat - lilat);
					for (int i = lilat; i != ilat; i += sgn(ilat - lilat)) {
						xt = (int) (lilon + Math.floor((m * (i - lilat)) + 0.5));
						lwtab[(ydots - 1) - i] = 2 * (xt == 0 ? 1 : xt);
					}
				}
				lilon = ilon;
				lilat = ilat;
			}
		}

		/* Now tweak the widths to generate full illumination for
		   the correct pole. */

		if (dec < 0.0) {
			ilat = ydots - 1;
			lilat = -1;
		} else {
			ilat = 0;
			lilat = 1;
		}

		for (int i = ilat; i != ydots / 2; i += lilat) {
			if (lwtab[i] != -1) {
				while (true) {
					lwtab[i] = xdots;
					if (i == ilat) {
						break;
					}
					i -= lilat;
				}
				break;
			}
		}

		return lwtab;
	}

	/*  XSPAN  --  Complement a span of pixels.  Called with line in which
	       pixels are contained, leftmost pixel in the  line,  and
	       the   number   of   pixels   to	 complement.   Handles
	       wrap-around at the right edge of the screen.  */

	private void xspan(Graphics g, int pline, int leftp, int npix) {
		// Sort this out, as it is a little out of place
		int xdots = this.getSize().width;
		int ydots = this.getSize().height;

		assert(npix <= xdots, "npix <= xdots");
		assert(pline >= 0 && pline < ydots, "pline >= 0 && pline < ydots");
		leftp = leftp % xdots;

		if (g == null) {
			System.out.println("Graphics is null");
			return;
		}

		if ((leftp + npix) > xdots) {
			g.drawLine(leftp, pline, (xdots - 1), pline);
			g.drawLine(0, pline, ((leftp + npix) - (xdots + 1)), pline);
		} else {
			g.drawLine(leftp, pline, leftp + (npix - 1), pline);
		}

	}

	/*  MOVETERM  --  Update illuminated portion of the globe.  */

	private void moveterm(
		Graphics g,
		int[] wtab,
		int noon,
		int[] otab,
		int onoon,
		int xdots,
		int ydots) {

		int ol, oh, nl, nh;

		/* This is for handling the fact that we want to draw night
		   and not day. */
		for (int i = 0; i < ydots; i++) {
			wtab[i] = xdots - wtab[i];
			if (wtab[i] == 0)
				wtab[i] = -1;
			else if (wtab[i] > xdots)
				wtab[i] = xdots;
		}

		noon = (xdots / 2) + noon;
		if (noon > xdots) {
			noon = noon - xdots;
		}
		/* end midnight and night adjustments */

		for (int i = 0; i < ydots; i++) {

			//System.out.println(" wtab[i] = " + wtab[i] );
			/* If line is off in new width table but is set in
			   the old table, clear it. */

			if (wtab[i] < 0) {
				if (otab[i] >= 0) {
					xspan(g, i, ((onoon - (otab[i] / 2)) + xdots) % xdots, otab[i]);
				}
			} else {

				/* Line is on in new width table.  If it was off in
				   the old width table, just draw it. */

				if (otab[i] < 0) {
					xspan(g, i, ((noon - (wtab[i] / 2)) + xdots) % xdots, wtab[i]);
				} else {

					/* If both the old and new spans were the entire
					               screen, they're equivalent. */

					if ((otab[i] == wtab[i]) && (wtab[i] == xdots)) {
						continue;
					}

					/* The line was on in both the old and new width
					   tables.  We must adjust the difference in the
					   span.  */

					ol = ((onoon - (otab[i] / 2)) + xdots) % xdots;
					oh = (ol + otab[i]) - 1;
					nl = ((noon - (wtab[i] / 2)) + xdots) % xdots;
					nh = (nl + wtab[i]) - 1;

					/* If spans are disjoint, erase old span and set
					   new span. */

					if (oh < nl || nh < ol) {
						xspan(g, i, ol, (oh - ol) + 1);
						xspan(g, i, nl, (nh - nl) + 1);
					} else {
						/* Clear portion(s) of old span that extend
						   beyond end of new span. */
						if (ol < nl) {
							xspan(g, i, ol, nl - ol);
							ol = nl;
						}
						if (oh > nh) {
							xspan(g, i, nh + 1, oh - nh);
							oh = nh;
						}
						/* Extend existing (possibly trimmed) span to
						   correct new length. */
						if (nl < ol) {
							xspan(g, i, nl, ol - nl);
						}
						if (nh > oh) {
							xspan(g, i, oh + 1, nh - oh);
						}
					}
				}
			}
			otab[i] = wtab[i];
		}
	}

	/** Get the short name of the layer */
	public String getShortName() {
		return new String("Night-Day");
	}

	/** Get the long name of the layer */
	public String getLongName() {
		return new String("Night and Day Earth map");
	}

	/** Indicates whether this layer is time dependant */
	public boolean isTimeDependant() {
		return true;
	}

	/** The date and time for time based data */
	public void setDateTime(Date theDateTime) {
		_userDate = theDateTime;
		repaint(1);
	}

	/** The map rectangle that matches the component Rectangle */
	public void setCoordRect(MapCoordRect theRect) {
		_mapCoordRect = theRect;
	}

	/** Returns whether this layer is configurable */
	public boolean isConfigurable() {
		return false;
	}

	/** get the Panel need for configuring this element */
	public Component getConfigurationPanel() {
		return null;
	}
	
	public void setLeftmostLongitude ( float longitude ) {
		_leftmostLongitude = longitude;
	}
}

/* Changelog
   ---------
   
   [2002-12-01]
   - using Math.toRadians & Math.toDegrees for conversion
     of angle units.
     
*/