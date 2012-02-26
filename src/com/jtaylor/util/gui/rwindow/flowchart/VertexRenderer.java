package com.jtaylor.util.gui.rwindow.flowchart;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/3/11
 * Time: 12:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface VertexRenderer <V>
{
   public void drawVertex(Vertex<V> vertex,Graphics2D g2,Point upperLeft,Point lowerRight,boolean selected);
}
