package ajmas74.daylighttracker;

import java.util.*;
import java.awt.*;

/**
 * @author <a href="mailto:ajmas@newtradetech.com">Andr&eacute;-John Mas</a>
 *
 */
public class MultiCompBorderLayout extends BorderLayout {
    Vector componentList = null;
    
    public MultiCompBorderLayout () {
      componentList = new Vector();
    }
  
    public void layoutContainer(Container target) {
      Insets insets = target.getInsets();
      int top = insets.top;
      int bottom = target.getSize().height - insets.bottom;
      int left = insets.left;
      int right = target.getSize().width - insets.right;
      
      for ( int i = 0; i < componentList.size(); i++ ) {
        Component theComponent = (Component) ( componentList.elementAt(i) );
        theComponent.setBounds(left, top, right, bottom);
      }
      
    }
    
    public void addLayoutComponent(String name, Component comp) {
      if (comp != null)
        componentList.addElement(comp);
    }
    
    public void removeLayoutComponent(Component comp) {
      System.out.println("removeLayoutComponent - not implemented");
    }
  }