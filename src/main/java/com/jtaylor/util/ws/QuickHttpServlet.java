package com.jtaylor.util.ws;

import com.jtaylor.util.enums.HttpOperation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumSet;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 12/14/13
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class QuickHttpServlet extends HttpServlet
{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		_doHttpOperation(req, resp, HttpOperation.Get);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		_doHttpOperation(req, resp, HttpOperation.Post);
	}
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		_doHttpOperation(req, resp, HttpOperation.Put);
	}
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		_doHttpOperation(req, resp, HttpOperation.Delete);
	}
	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		_doHttpOperation(req, resp, HttpOperation.Options);
	}
	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		_doHttpOperation(req, resp, HttpOperation.Head);
	}
	@Override
	protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		_doHttpOperation(req, resp, HttpOperation.Trace);
	}

	/**
	 * this should be overridden if the servlet wants to handle more than get and post
	 * @return and enum set containing the HttpOperations they want to be passed to doOperation
	 */
	protected EnumSet<HttpOperation> doesOperations()
	{
		return EnumSet.of(HttpOperation.Get,HttpOperation.Post);
	}
	protected void _doHttpOperation(HttpServletRequest req, HttpServletResponse resp, HttpOperation op) throws ServletException, IOException
	{
		if(doesOperations().contains(op))
		{
			doHttpOperation(req, resp, op);
		}
		else
		{
			resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
		}
	}
	public abstract void doHttpOperation(HttpServletRequest req, HttpServletResponse resp, HttpOperation op) throws ServletException, IOException;
}
