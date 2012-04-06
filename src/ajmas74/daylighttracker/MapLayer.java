package ajmas74.daylighttracker;
import java.util.Date;
import java.awt.*;

public interface MapLayer {
	
	
	//would implementing an applet type interface be useful
	//with init, stop, refresh, etc?
	
	/** Get the short name of the layer */
	public String getShortName();	 
	
	/** Get the long name of the layer */
	public String getLongName();
	
	/** Indicates whether this layer is time dependant */
	public boolean isTimeDependant();
	
	/** The date and time for time based data */
	public void setDateTime ( Date theDateTime );
	
	/** The map rectangle that matches the component Rectangle */
	public void setCoordRect ( MapCoordRect theRect );
	
	
/* Maybe create a configurable interface and place these here? */
	
	/** Returns whether this layer is configurable */
	public boolean isConfigurable();
	
	/** get the Panel need for configuring this element */
	public Component getConfigurationPanel();
	
}