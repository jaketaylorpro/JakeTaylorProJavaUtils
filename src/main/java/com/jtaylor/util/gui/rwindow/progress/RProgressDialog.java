package com.jtaylor.util.gui.rwindow.progress;

import com.jtaylor.util.Logging;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import javax.swing.*;

public class RProgressDialog extends JDialog implements ProgressParent
{
	protected static int visibleProgressBarCount=0;
	protected RProgressPanel myPanel;
	protected Exception myError=null;
	protected boolean wasCancelled=false;
	public void done()
	{
		dispose();
	}
	public boolean wasCancelled()
	{
		return wasCancelled;
	}
	public void cancelled()
	{
		wasCancelled=true;
		dispose();
	}

	public void error(Exception e)
	{
		myError=e;
		dispose();
	}
	public Exception getError()
	{
		return myError;
	}
	public void display()
	{
		setVisible(true);
	}

	//public static String n;
	
	enum testEnum {a,b,c,d,e};
	public static void main(String[] args)
	{
		final Logger log=Logger.getLogger(RProgressDialog.class);
		log.addAppender(new ConsoleAppender(new PatternLayout("%p [%t] (%M) {%d{ISO8601}}: %m%n ")));
		Logger log2=Logger.getLogger(RProgressPanel.class);
		log2.addAppender(new ConsoleAppender(new PatternLayout("%p [%t] (%M) {%d{ISO8601}}: %m%n ")));
		RProgressDialog progressDialog=new RProgressDialog("tester",new SimpleProgressRunner(5)
		{
			public void run()
			{
				for(int i=0;i<5;i++)
				{
					try{Thread.sleep(1000);}catch(Exception e){};
					log.debug("i: "+i);
					increment();
				}
			}
		});
//		RProgressDialog progressDialog=new RProgressDialog("tester",new StringListProgressRunner<String>(Arrays.asList((String)"a","b","c","d","f"))
//		{
//			public void run()
//			{
//				for(int i:Arrays.asList(1,2,3,4,5))
//				{
//					new RProgressDialog("tester: "+i,new SimpleProgressRunner(5)
//					{
//						@Override
//						public void run()
//						{
//							log.debug("runinng!!");
//							for(int i=0;i<5;i++)
//							{
//								try{Thread.sleep(1000);}catch(Exception e){};
//								log.debug("i: "+i);
//								increment();
////								new RProgressDialog("tester: "+i,new SimpleProgressRunner(5)
////								{
////									@Override
////									public void run()
////									{
////										for(int i=0;i<5;i++)
////										{
////											try{Thread.sleep(1000);}catch(Exception e){};
////											increment();
////										}
////									}
////								});
//							}
//						}
//					});
//				}
//			}
//		});
		progressDialog.start();
		System.out.println("done");
//		()
//		{
//			public void run()
//			{
//				try{Thread.sleep(3000);}catch (InterruptedException e){e.printStackTrace();}
//				increment();
//			}
//		});
		/*
		RProgressDialog progressDialog=new RProgressDialog("tester",new EnumProgressRunner(testEnum.class)
		{
            public void run()
            {
            	for(int i=0;i<4;i++)
            	{
            		try{Thread.sleep(1000);}catch (InterruptedException e){e.printStackTrace();}
            		increment();
            	}
            	/*
            	setValue(testEnum.a);
            	System.out.println(testEnum.a);
            	try{Thread.sleep(1000);}catch (InterruptedException e){e.printStackTrace();}
            	setValue(testEnum.b);
            	System.out.println(testEnum.b);
				try{Thread.sleep(1000);}catch (InterruptedException e){e.printStackTrace();}
            	setValue(testEnum.c);
            	System.out.println(testEnum.c);
				try{Thread.sleep(1000);}catch (InterruptedException e){e.printStackTrace();}
            	setValue(testEnum.d);
            	System.out.println(testEnum.d);
				try{Thread.sleep(1000);}catch (InterruptedException e){e.printStackTrace();}
            	setValue(testEnum.e);
            	System.out.println(testEnum.e);
				try{Thread.sleep(1000);}catch (InterruptedException e){e.printStackTrace();}
            	*//*
            }
		}
		);
		/*
		RSimpleProgressDialog progressDialog=new RSimpleProgressDialog("tester",new SimpleProgressRunner(5)
		{
			public void run()
			{
				for(int i=0;i<5;i++)
				{
					try
	                {
	                    Thread.sleep(1000);
	                }
	                catch (InterruptedException e)
	                {
	                    e.printStackTrace();
	                }
					increment();
					System.out.println(i);
				}
			}
		});
		*/
	}

	protected Logger log;
	public RProgressDialog(String title,ProgressRunner run)
	{
		super();
		log=Logging.createServerLogger(RProgressDialog.class);
		myPanel=new RProgressPanel(title,run);
		myPanel.setParent(this);
		setContentPane(myPanel);
		setModal(true);
		setResizable(false);
		setUndecorated(true);
		setLocationRelativeTo(null);
		setLocation(getLocation().x,getLocation().y-100);
		setLocation(getLocation().x/*+(100*visibleProgressBarCount)*/,getLocation().y+(50*visibleProgressBarCount));
		setAlwaysOnTop(true);
		pack();
	}
	public void start()
	{
		myPanel.start();
	}
	public void setPanel(RProgressPanel panel)
	{
		myPanel=panel;
		myPanel.setParent(this);
		setContentPane(myPanel);
	}
	@Override
	public void setVisible(boolean b)
	{
		if(b)
		{
			if(!isVisible())
			{
				visibleProgressBarCount++;
				log.debug("showing, incrementing count to: "+visibleProgressBarCount);
			}
		}
		else
		{
			if(isVisible())
			{
				visibleProgressBarCount--;
				log.debug("hiding reducing count to: "+visibleProgressBarCount);
			}
		}
		super.setVisible(b);
	}
	@Override
	public void dispose()
	{
		if(isVisible())
		{
			visibleProgressBarCount--;
			log.debug("disposing reducing count to: "+visibleProgressBarCount);
		}
		super.dispose();
	}
	@Override
	public String toString()
	{
		return "RProgressDialog("+myPanel.getTitle()+")";
	}
}
