package com.jtaylor.util.gui.rwindow.flowchart;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/5/11
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LaneRenderer <L>
{
   public void drawLane(Lane<L> lane,Graphics2D g2,Point upperLeft,Point lowerRight,int cellWidth,boolean selected);
}
