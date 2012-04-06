//import com.sun.java.swing.*;
package ajmas74.daylighttracker;
import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class OverlayedTimeLayer extends Component implements MapLayer {

	ImageIcon theGraphic = null;
	MapCoordRect mapCoordRect = null;
	Date theDate = new Date();

    public OverlayedTimeLayer () {
    }


	/** Paints this layer */
    public void paint ( Graphics g ) {
		//if (theGraphic != null) {
			Dimension theGraphicSize = this.getSize();
						
			Point basePoint = new Point(0,0);
			
	  /*  	g.setColor(Color.black);
	    	g.fillRect(basePoint.x,basePoint.y,50,25);
	    	g.setColor(Color.red);
	    	g.drawString( theDate.getHours() + " : " + theDate.getMinutes(), basePoint.x+5, basePoint.y+18);
	   */
 		g.setColor(Color.black);
	    g.fillRect(basePoint.x,basePoint.y,70,25);
	    g.setColor(Color.red);
	    g.drawString( theDate.getHours() + " : " + theDate.getMinutes() + " : " + theDate.getSeconds() , basePoint.x+5, basePoint.y+18);
	   
	   
		//}
	}


	/** Get the short name of the layer */
	public String getShortName() {
		return new String ("Time");
	}

	/** Get the long name of the layer */
	public String getLongName() {
		return new String ("Current Time");
	}

	/** Indicates whether this layer is time dependant */
	public boolean isTimeDependant() {
		return false;
	}

	/** The date and time for time based data */
	public void setDateTime ( Date theDateTime ) {
		theDate = theDateTime;
		repaint(1);
	}

	/** The map rectangle that matches the component Rectangle */
	public void setCoordRect ( MapCoordRect theRect ) {
		mapCoordRect = theRect;
	}

	/** Returns whether this layer is configurable */
	public boolean isConfigurable() {
		return false;
	}

	/** get the Panel need for configuring this element */
	public Component getConfigurationPanel() {
		return null;
	}

}