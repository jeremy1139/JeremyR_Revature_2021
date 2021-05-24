package com.revature.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingClass 
{
	private static final Logger LOG = LogManager.getLogger(LoggingClass.class);
	
	public static void main(String[] args)
	{
		LOG.trace("Trace");
		LOG.debug("Debug");
		LOG.info("Info");
		LOG.warn("Warn");
		LOG.error("Error");
		LOG.fatal("Fatal");
	}
}
