package com.jtaylor.util.gui.rwindow.progress;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: Jan 22, 2010
 * Time: 11:57:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProgressParent
{
	public void done();
	public void cancelled();
	public void error(Exception e);
	public void display();
	public Exception getError();
	public boolean wasCancelled();
}
