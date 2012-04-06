package ajmas74.daylighttracker;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

//import javax.swing.*;

/**
 * @author <a href="mailto:ajmas@newtradetech.com">Andr&eacute;-John Mas</a>
 *
 */
public class DaylightTrackerApplet extends Applet implements Runnable {

  boolean  _loop;
  NightDay _nightDayPanel;
  Panel    _basePanel; 
	/**
	 * @see java.applet.Applet#init()
	 */
	public void init() {
		super.init();
    
    String mapURL = getParameter("map.url");
    String leftmostLongitudeStr = getParameter("left.longitude");
    
    
    setLayout(new BorderLayout());
    
    _basePanel = this; //new Panel(); //Component();
    _nightDayPanel = new NightDay();
    _basePanel.setLayout( new MultiCompBorderLayout() );

		if (leftmostLongitudeStr != null ) {
			int lml = Integer.parseInt(leftmostLongitudeStr);
			_nightDayPanel.setLeftmostLongitude(lml);
		}
    _basePanel.add(_nightDayPanel); 
    
    
    if ( mapURL != null ) {
    	try {
        _basePanel.add(new ImagePanel(new URL(mapURL)));
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
    } else {
			_basePanel.add(new GeographicalMap());
    }


    //add(_basePanel);
	}

	/**
	 * @see java.awt.Component#resize(int, int)
	 * @deprecated
	 */
	public void resize(int width, int height) {
		super.resize(width, height);
	}

  public void run() {
    Rectangle rect = this.getBounds();
    repaint(rect.x,rect.y,rect.width,rect.height);
    while ( _loop ) {
      try {
        Thread.sleep(60000);
  			_nightDayPanel.setDateTime( new Date() );  			            
      } catch ( InterruptedException ex ) {
      }
      repaint(0);
    }
  }
  
	/**
	 * @see java.applet.Applet#start()
	 */
	public void start() {
		super.start();
    _loop = true;
    (new Thread(this)).start();
	}

	/**
	 * @see java.applet.Applet#stop()
	 */
	public void stop() {
		super.stop();
    _loop = false;
	}

}
