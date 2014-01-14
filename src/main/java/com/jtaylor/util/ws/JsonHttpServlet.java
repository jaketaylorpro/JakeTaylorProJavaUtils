package com.jtaylor.util.ws;

import com.jtaylor.util.Logging;
import com.jtaylor.util.enums.HttpOperation;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: jacob.taylor
 * Date: 12/14/13
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class JsonHttpServlet <I,O> extends QuickHttpServlet
{
	@Override
	public void doHttpOperation(HttpServletRequest req,
	                            HttpServletResponse resp,
	                            HttpOperation op) throws ServletException, IOException
	{
		Logger log= Logger.getLogger(JsonHttpServlet.class);
		log.addAppender(new ConsoleAppender(new PatternLayout(Logging.BASIC_PATTERN)));
		log.setLevel(Level.DEBUG);
		log.debug("getting mapper");
		ObjectMapper mapper=new ObjectMapper();
		resp.setHeader("Access-Control-Allow-Origin","*");
		resp.setContentType("application/json");
		BufferedReader in=req.getReader();
		I i = mapper.readValue(in,getInType());
		/*(Class<I>)this.getClass().
					getClassLoader().
					loadClass(StringOperations.extract(this.getClass().getGenericSuperclass().toString(), ".*<(.*)>.*"))*/
		PrintWriter out=resp.getWriter();
		mapper.writeValue(out,doHttpOperation(i,op));
		out.close();
		resp.setStatus(HttpServletResponse.SC_OK);
		log.debug("done");
	}
	public abstract O doHttpOperation(I i,HttpOperation op) throws ServletException;//TODO throw different exceptions to cause different responses, securiuty exception redirects (with url parameter)
	public abstract Class<I> getInType();
}
