package com.fhd.sys.business.param;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class AddedJob implements Job{
	@Autowired
	public void run()
	{
		System.out.println("*****addedJob:"+new Date(System.currentTimeMillis()));
	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("AddedJob.execute:....");
	}
}
