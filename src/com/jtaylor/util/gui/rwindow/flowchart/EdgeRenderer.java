package com.jtaylor.util.gui.rwindow.flowchart;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 2/3/11
 * Time: 8:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EdgeRenderer <V,E,L>
{
   public void drawEdges(FlowGraphView<V,E,L> graph,Graphics2D g2);
   public Edge<V,E> getFocusEdge();
}
