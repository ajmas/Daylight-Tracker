//import com.sun.java.swing.*;
package ajmas74.daylighttracker;
import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class TopographicalMap extends ImagePanel {

	private final static String MAP_NAME = new String("./images/Earth-Topographical.jpg");
	private long LEFT_BASE_ANGLE = 180;
	private long TOP_BASE_ANGLE = 180;
	
	ImageIcon theGraphic = null;
	MapCoordRect mapCoordRect = null;

    public TopographicalMap () {
      super(Toolkit.getDefaultToolkit().getImage(
        GeographicalMap.class.getResource(MAP_NAME)));      

    	//theGraphic = new ImageIcon ( new String ("Earth-Topographical.jpg") );
//		if (theGraphic == null)
//			System.out.println(" Error while getting image ");
    }

//	public ImageIcon getImage() {
//		return theGraphic;
//	}
//
//	/** Paints this layer */
//    public void paint ( Graphics g ) {
//		if (theGraphic != null) {
//			Dimension theGraphicSize = this.getSize();
//	    	g.drawImage ( theGraphic.getImage(), 0, 0, getSize().width, getSize().height, null );
//		}
//	}


	/** Get the short name of the layer */
	public String getShortName() {
		return new String ("Topographical");
	}
	
	/** Get the long name of the layer */
	public String getLongName() {
		return new String ("Topographical Earth map");
	}
//	
//	/** Indicates whether this layer is time dependant */	
//	public boolean isTimeDependant() {
//		return false;
//	}
//	
//	/** The date and time for time based data */
//	public void setDateTime ( Date theDateTime ) {
//		// Ignored as we don't care about the date or time
//	}
//	
//	/** The map rectangle that matches the component Rectangle */
//	public void setCoordRect ( MapCoordRect theRect ) {
//		mapCoordRect = theRect;
//	}
//
//	/** Returns whether this layer is configurable */
//	public boolean isConfigurable() {
//		return false;
//	}
//	
//	/** get the Panel need for configuring this element */
//	public Component getConfigurationPanel() {
//		return null;
//	}
//	
}