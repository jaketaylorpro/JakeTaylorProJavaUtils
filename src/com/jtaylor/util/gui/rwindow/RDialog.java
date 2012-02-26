package com.jtaylor.util.gui.rwindow;

import com.jtaylor.util.Logging;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;


public class RDialog extends JDialog implements RWindow//, KeyEventDispatcher
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
		RDialog window = new RDialog(panel);
		window.prompt();
		System.out.println(window.pressedOk);
	}

	public RDialog(Component view)
	{
		this(view.getName(), view, new RBar());
	}

	public RDialog(String title, Component view, RBar bar)
	{
		super();
		log = Logging.createServerLogger(RDialog.class);
		setTitle(title);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		myView = view;
		if (myView instanceof RView)
		{
			((RView) myView).setParentWindow(this);
		}
		myScrollPane = new JScrollPane(myView);
		myScrollPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, BORDER));
		myBar = bar;
		myBar.setBorder(BorderFactory.createMatteBorder(0, 2, 2, 2, BORDER));
		myBar.setParentWindow(this);
//		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(myScrollPane, BorderLayout.CENTER);
		contentPane.add(myBar, BorderLayout.SOUTH);
		setContentPane(contentPane);
		pack();
		setLocationRelativeTo(null);
	}


	public void setView(Component view)
	{
		myView = view;
		myScrollPane.setViewportView(myView);
		repaint();
	}

	public Component getView()
	{
		return myScrollPane.getViewport().getView();
	}

	public RBar getBar()
	{
		return myBar;
	}

	/*public void setBar(RBar bar)
	{
		getContentPane().remove(myBar);
		myBar = bar;
		myBar.setParentWindow(this);
		getContentPane().add(myBar, BorderLayout.PAGE_END);
	}*/

	public void setBar(RBar bar)//changing to what i had for rdialog
	{
      if(bar!=null)
      {
         final RBar oldBar = myBar;
         myBar = bar;
         myBar.setParentWindow(this);
         getContentPane().remove(oldBar);
         getContentPane().add(myBar, BorderLayout.SOUTH);
//         SwingUtilities.invokeLater(new Runnable()
//         {
//            public void run()
//            {
//               getContentPane().remove(oldBar);
//               getContentPane().add(myBar, BorderLayout.SOUTH);
//               getContentPane().repaint();
//               setSize(new Dimension(getSize().width + 1, getSize().height + 1));
//               setSize(new Dimension(getSize().width - 1, getSize().height - 1));
//            }
//         });
      }
	}

	public void display()
	{
		setModal(false);
		setAlwaysOnTop(false);
		setVisible(true);
		myBar.repaint();
		myBar.requestFocus();
	}

	public void displayOnTop()
	{
		setModal(false);
		setAlwaysOnTop(true);
		setVisible(true);
		myBar.repaint();
		myBar.requestFocus();
	}

	public void prompt()
	{
		setVisible(false);
		setModal(false);
		setAlwaysOnTop(false);
		setVisible(true);
		myBar.repaint();
		setModal(true);
		setVisible(false);
		setVisible(true);
		myBar.repaint();
		myBar.requestFocus();
	}

	public void promptOnTop()
	{
		setVisible(false);
		setModal(false);
		setAlwaysOnTop(true);
		setVisible(true);
		myBar.repaint();
		setModal(true);
		setVisible(false);
		setVisible(true);
		myBar.repaint();
		myBar.requestFocus();
	}

	public void dispose()
	{
//		KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
		super.dispose();
		System.gc();
	}

	public void close()
	{
//		KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
		setVisible(false);
		System.gc();
	}

	public boolean pressedOk()
	{
		return pressedOk;
	}

	public void setPressedOk(boolean ok)
	{
		pressedOk = ok;
	}
	//this doesnt work at the moment (might work in 8.1)
//	public boolean dispatchKeyEvent(KeyEvent e)
//    {
//		System.out.println("keyevent detected: "+e.getKeyChar()+","+e.getModifiers());
//		if(e.getKeyChar()=='w'&&e.getModifiers()==4)
//		{
//			this.dispose();
//			return true;
//		}
//		return false;
//    }
}
