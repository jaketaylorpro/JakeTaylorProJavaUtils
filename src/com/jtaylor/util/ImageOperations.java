package com.jtaylor.util;

import com.jtaylor.util.gui.rwindow.RDialog;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: Mar 2, 2010
 * Time: 5:10:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageOperations
{
	public static ImageIcon scale(ImageIcon image,int size)
	{
		return new ImageIcon(scale(image.getImage(),size));
	}
	public static Image scale(Image image,int size)
	{
		int h=image.getHeight(null);
		int w=image.getWidth(null);
//		System.out.println("height: "+h);
//		System.out.println("width: "+w);
		float f;
		if(h>w)
		{//than its portrait
			if(h>size)
			{//than we shrink
				f=((float)size)/h;
			}
			else
			{//than we streatch
				f=((float)h)/size;
			}
		}
		else
		{//than its landscape
			if(w>size)
			{//than we shrink
				f=((float)size)/w;
			}
			else
			{//than we streatch
				f=((float)w)/size;
			}
		}
//		System.out.println("factor: "+f);
		return image.getScaledInstance((int)(w*f),(int)(h*f),Image.SCALE_SMOOTH);
	}
	public static void main(String[] args)
	{
		try
		{
			ImageIcon icon=new ImageIcon(FileOperations.readBytes(new FileInputStream(new File("/Users/jtaylor/testOUT.png"))));
			JLabel label=new JLabel(new ImageIcon(scale(icon.getImage(),25)));
//			System.out.println(Toolkit.getDefaultToolkit().prepareImage(icon.getImage(),25,25,label));
			new RDialog(label).prompt();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
