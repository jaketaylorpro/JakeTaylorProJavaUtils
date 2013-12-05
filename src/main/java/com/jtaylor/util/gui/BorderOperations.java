package com.jtaylor.util.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 5/18/11
 * Time: 10:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class BorderOperations
{
   public static Border getTitledEtchedBorder(String title)
   {
      return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),title, TitledBorder.LEFT,TitledBorder.TOP);
   }
   public static Border getTitledTopOnlyBorder(String title)
   {
      return BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK),title, TitledBorder.LEFT,TitledBorder.TOP);
   }
   public static Border getTopOnlyBorder()
   {
      return BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK);
   }
}
