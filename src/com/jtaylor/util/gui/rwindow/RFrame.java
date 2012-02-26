package com.jtaylor.util.gui.rwindow;

import com.jtaylor.util.Logging;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;


public class RFrame extends JFrame implements RWindow
{
	protected RBar myBar;
	protected Component myView;
	protected JScrollPane myScrollPane;
	protected boolean pressedOk;
	protected Logger log;

	
	public static void main(String args[])
	{
		JPanel panel = new JPanel();
		panel.add(new JLabel("label"));
		panel.setSize(new Dimension(600, 600));
		RFrame window = new RFrame(panel);
		window.display();
		System.out.println(window.pressedOk);
	}

	public RFrame(Component view)
	{
		this(view.getName(), view, new RBar());
	}

	public RFrame(String title, Component view, RBar bar)
	{
		super();
		log= Logging.createServerLogger(RFrame.class);
		setTitle(title);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		myView = view;
		if (myView instanceof RView)
		{
			((RView) myView).setParentWindow(this);
		}
		myScrollPane = new JScrollPane(myView);
      myScrollPane.getVerticalScrollBar().setUnitIncrement(10);
      myScrollPane.getHorizontalScrollBar().setUnitIncrement(10);
		myScrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, BORDER));
		myBar = bar;
		myBar.setBorder(BorderFactory.createMatteBorder(0, 2, 2, 2, BORDER));
		myBar.setParentWindow(this);
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(myScrollPane, BorderLayout.CENTER);
		contentPane.add(myBar, BorderLayout.SOUTH);
		setContentPane(contentPane);
		pack();
		setLocationRelativeTo(null);
	}
   public void refreshScrollPane()
   {
      setSize(new Dimension(getSize().width + 1, getSize().height + 1));
      setSize(new Dimension(getSize().width - 1, getSize().height - 1));
   }
   public JScrollPane getScrollPane()
   {
      return myScrollPane;
   }

	public void setView(Component view)
	{
		myView = view;
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				myScrollPane.setViewportView(myView);
				getContentPane().repaint();
				refreshScrollPane();
			}
		});
	}

	public Component getView()
	{
		return myScrollPane.getViewport().getView();
	}

	public void setScrollSpeed(int speed)
	{
		myScrollPane.getVerticalScrollBar().setUnitIncrement(16);
	}

	public RBar getBar()
	{
		return myBar;
	}

	public void setBar(RBar bar)
	{
		getContentPane().remove(myBar);
      RBar oldBar=myBar;
		myBar = bar;
		myBar.setParentWindow(this);
      getContentPane().remove(oldBar);
      getContentPane().add(myBar, BorderLayout.SOUTH);
	}
   public void setAlwaysScroll()
   {
      myScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      myScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
   }
	/*public void setBar(RBar bar)//changing to what i had for rdialog
	{
		final RBar oldBar = myBar;
		myBar = bar;
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				getContentPane().remove(oldBar);
				getContentPane().add(myBar, BorderLayout.SOUTH);
				getContentPane().repaint();
				setSize(new Dimension(getSize().width + 1, getSize().height + 1));
				setSize(new Dimension(getSize().width - 1, getSize().height - 1));
			}
		});
	}*/

	public void display()
	{
		setAlwaysOnTop(false);
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				setVisible(true);
				getContentPane().repaint();
			}
		});

	}

	public void displayOnTop()
	{
		setAlwaysOnTop(true);
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				setVisible(true);
				getContentPane().repaint();
			}
		});
	}

	public void dispose()
	{
		log.debug("invoking dispose later");
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				superDispose();
				System.gc();
			}
		});
	}

	public void superDispose()
	{
		log.debug("actually disposing window");
		super.dispose();
		log.debug("disposed window");
	}

	public void close()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				setVisible(false);
				System.gc();
			}
		});
	}

	public boolean pressedOk()
	{
		return pressedOk;
	}

	public void setPressedOk(boolean ok)
	{
		pressedOk = ok;
	}
}
