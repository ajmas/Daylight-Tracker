//import com.sun.java.swing.*;
package ajmas74.daylighttracker;

import javax.swing.*;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.net.URL;
import java.util.Date;

public class ImagePanel extends Component implements MapLayer, ImageObserver {

  Image _panelImage = null;
  MapCoordRect mapCoordRect = null;

  int _imageIndex = 0;
  int _graphicWidth;
  int _graphicHeight;
  boolean _graphicLoaded = false;
  boolean _firstTime = true;
  int oldWidth = 0;
  int oldHeight = 0;  
  
  ImageObserver _observer;
  
  //References to objects used for double buffering
  Image offScreenImage;
  Graphics offScreenContext;
    
    
  public ImagePanel( URL imageURL ) {
    this();
    setImage(Toolkit.getDefaultToolkit().createImage(imageURL));
    //ImageIcon imgIcon = new ImageIcon(imageURL);
    //setImage(imgIcon.getImage());
  }
      
  public ImagePanel( Image img ) {
    this();
    setImage(img);

  }

  public ImagePanel( ) {
    _observer = this; //new MyImageObserver();
  }
  
  public void setImage( Image img ) {
    _panelImage = img; 

    if ( _panelImage == null ) {
      return;
    }

    try {
      int imageIndex = _imageIndex++;
      MediaTracker mt = new MediaTracker(this);
      mt.addImage(img,imageIndex);
      mt.waitForID(imageIndex);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
      
  }

  public Image getImage() {
    return _panelImage; //theGraphic;
  }
  
  public void update(Graphics g){
    paint(g);
  }
  
  public void paint(Graphics g) {

		if ( _panelImage == null ) {
			return;
		}
		
    _graphicWidth = _panelImage.getWidth(null);
    _graphicHeight = _panelImage.getHeight(null);
                       
    g.drawImage(
      _panelImage,
      0,
      0,
      this.getWidth(),
      this.getHeight(),
      0,
      0,
      _graphicWidth,
      _graphicHeight,
      null);                           
  }

  /** Get the short name of the layer */
  public String getShortName() {
    return new String("Geographical");
  }

  /** Get the long name of the layer */
  public String getLongName() {
    return new String("Geographical Earth map");
  }

  /** Indicates whether this layer is time dependant */
  public boolean isTimeDependant() {
    return false;
  }

  /** The date and time for time based data */
  public void setDateTime(Date theDateTime) {
    // Ignored as we don't care about the date or time
  }

  /** The map rectangle that matches the component Rectangle */
  public void setCoordRect(MapCoordRect theRect) {
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