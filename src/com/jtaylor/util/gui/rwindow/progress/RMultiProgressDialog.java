package com.jtaylor.util.gui.rwindow.progress;

import com.jtaylor.util.Logging;
import com.jtaylor.util.gui.rwindow.GBCHelper;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.util.List;
import java.util.Vector;

public class RMultiProgressDialog extends RProgressDialog
{
	public static void main(String[] args)
	{
		Logger log= Logger.getLogger(RMultiProgressDialog.class);
		log.addAppender(new ConsoleAppender(new PatternLayout("%p [%t] (%M) {%d{ISO8601}}: %m%n ")));
		Logger log2=Logger.getLogger(RProgressPanel.class);
		log2.addAppender(new ConsoleAppender(new PatternLayout("%p [%t] (%M) {%d{ISO8601}}: %m%n ")));
		List<RProgressPanel> panel=new Vector<RProgressPanel>();
		panel.add(new RProgressPanel("1",new SimpleProgressRunner(2){
				@Override
				public void run()
				{
					for(int i=0;i<2;i++)
					{
						try
						{
							Thread.sleep(500);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
						}
						increment();
					}
				}
			}));
		panel.add(new RProgressPanel("2",new SimpleProgressRunner(2){
				@Override
				public void run()
				{
					for(int i=0;i<2;i++)
					{
						try
						{
							Thread.sleep(500);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
						}
						increment();
					}
				}
			}));
		RMultiProgressDialog dialog=new RMultiProgressDialog("multi",panel);
		dialog.start();
	}
	protected Logger log;
	public RMultiProgressDialog(String title,final List<RProgressPanel> processes)
	{
		super(title,new SimpleProgressRunner(0){public void run(){}});
		log= Logging.createServerLogger(RMultiProgressDialog.class);
		setPanel(new RProgressPanel(title,new SimpleProgressRunner(processes.size()){
			@Override
			public void run()
			{
				for(RProgressPanel panel:processes)
				{
					log.debug("adding: "+panel);
					getContentPane().add(panel,GBCHelper.getFullGBC(0, 2,2,3));
					log.debug("starting: "+panel);
					panel.start();
					log.debug("removing: "+panel);
					getContentPane().remove(panel);
				}
			}
		}));
	}

//	@Override
//	public void done()
//	{
//		dispose();
//	}
//
//	@Override
//	public void cancelled()
//	{
//		dispose();
//	}
//
//	@Override
//	public void error(Exception e)
//	{
//		dispose();
//	}

	@Override
	public void display()
	{
		setVisible(true);
	}

	private static class MultiProgressRunner extends SimpleProgressRunner
	{
		List<RProgressPanel> myProcesses;
		public MultiProgressRunner(List<RProgressPanel> processes)
		{
			super(processes.size());
			myProcesses=processes;
		}

		@Override
		public void run()
		{

		}
	}
}
