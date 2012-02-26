package com.jtaylor.util.gui.rwindow.progress;

import com.jtaylor.util.Logging;
import com.jtaylor.util.gui.rwindow.GBCHelper;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: Jan 22, 2010
 * Time: 11:21:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class RProgressPanel extends JPanel implements ProgressListener
{
	protected ProgressParent myParent;
	protected JProgressBar myProgressBar;
	protected JLabel myTitle;

	protected JButton myButton;
	protected boolean myStarted;

	protected Thread myThread;
	protected ProgressRunner myRunner;


	protected static final Color BORDER = Color.darkGray;
	protected static final Color BACKGROUND = Color.white;


	protected static Icon X;

	static
	{
		BufferedImage image = new BufferedImage(15, 15, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.black);
		g.fillPolygon(new int[]{0, 0, 1, 12, 12, 11}, new int[]{1, 0, 0, 11, 12, 12}, 6);
		g.fillPolygon(new int[]{0, 0, 1, 12, 12, 11}, new int[]{11, 12, 12, 1, 0, 0}, 6);
		X = new ImageIcon(image);
	}
	protected Logger log;
	public RProgressPanel(String title, ProgressRunner run)
	{
		super(new GridBagLayout());
		log = Logging.createServerLogger(RProgressPanel.class);

		myProgressBar = new JProgressBar(0, 100);
		myProgressBar.setStringPainted(true);
		myTitle = new JLabel(title);
		myTitle.setHorizontalAlignment(JLabel.CENTER);
		Font oldFont = myTitle.getFont();
		Font newFont = new Font(oldFont.getName(), oldFont.getStyle(), oldFont.getSize() + 4);
		myTitle.setFont(newFont);
		myStarted = false;
		myRunner = run;
		myRunner.addProgressListener(this);
		myThread = new Thread(new Runnable()
		{
			public void run()
			{
				log.debug("starting!");
				try
				{
					myRunner.run();
				}
				catch (Exception e)
				{
					Logging.notifyUser("There was an error running the process: " + myTitle.getText(), e, log);
					if (myParent != null)
					{
						myParent.error(e);
					}
				}
				finally
				{
					System.gc();
				}
			}
		});
		myButton = new JButton(X);
		myButton.setHorizontalTextPosition(JButton.LEFT);
		myButton.setMinimumSize(new Dimension(10, 10));
		myButton.setMaximumSize(new Dimension(15, 15));
		myButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (myThread != null)
				{
					log.debug("stopping runner: " + myRunner);
					log.debug(myThread.getStackTrace());
					myThread.stop();
					if (myParent != null)
					{
						myParent.cancelled();
					}
				}
				else
				{
					log.warn("myThread is null");
				}
			}
		});
		add(myTitle, GBCHelper.getInlineGBC(0, 0));
		add(myButton, GBCHelper.getStaticEastGBC(1, 0));
		add(myProgressBar, GBCHelper.getFullGBC(0, 1, 2, 2));
		setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, BORDER));
		setBackground(BACKGROUND);
	}

	public void setParent(ProgressParent parent)
	{
		myParent = parent;
	}

	public void start()
	{
		if (!myStarted)
		{
			myStarted = true;
			try
			{
				log.debug("starting run: " + myRunner);
				myThread.start();
			}
			catch (Exception e)
			{
				log.error("there was an error starting progressRunner: " + myTitle.getText(), e);
				Logging.notifyUser(Level.ERROR, "There was an error starting the process: " + myTitle.getText());
			}
			try
			{
				SwingUtilities.invokeAndWait(new Runnable()
				{
					public void run()
					{
						if (myParent != null)
						{
							myParent.display();
						}
						log.debug("set visible actually called");
					}
				});
				log.debug("setVisible put in the event queue");
			}
			catch (Exception e)
			{
				log.error("there was an error putting 'setVisible in the event queue", e);
			}
		}
	}

	public void progress(int progress, String text)
	{
		if (progress < 0)
		{
			myProgressBar.setIndeterminate(true);
		}
		else
		{
			myProgressBar.setIndeterminate(false);
			myProgressBar.setValue(progress);
		}
		myProgressBar.setString(text);
		repaint();
		if (myProgressBar.getValue() >= myProgressBar.getMaximum())
		{
			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e)
			{
			}
			if (myParent != null)
			{
				myParent.done();
			}
		}
	}

	public String getTitle()
	{
		return myTitle.getText();
	}
}
