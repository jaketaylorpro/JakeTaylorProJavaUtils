package com.jtaylor.util;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/3/11
 * Time: 11:14 PM
 * To change this template use File | Settings | File Templates.
 */
public enum Direction{NORTH,NORTH_EAST,EAST,SOUTH_EAST,SOUTH,SOUTH_WEST,WEST,NORTH_WEST,CENTER;

   public boolean isCardinal()
   {
      return this==NORTH||this==SOUTH||this==WEST||this==EAST;
   }
}
