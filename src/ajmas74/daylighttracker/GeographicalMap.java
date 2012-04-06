package ajmas74.daylighttracker;

import java.awt.*;

public class GeographicalMap
  extends ImagePanel {

  Image theGraphic = null;
  MapCoordRect mapCoordRect = null;

  int _graphicWidth;
  int _graphicHeight;
  boolean _graphicLoaded = false;

  public GeographicalMap() {
    super(GeographicalMap.class.getResource("images/Earth001.jpg"));
//    super(Toolkit.getDefaultToolkit().getImage(
//      GeographicalMap.class.getResource("images/Earth001.jpg")));
  }

  /** Get the short name of the layer */
  public String getShortName() {
    return new String("Geographical");
  }

  /** Get the long name of the layer */
  public String getLongName() {
    return new String("Geographical Earth map");
  }


}