package com.example.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Projects {
	private String empID;
    private String projectID;
    private Date dateFrom;
    private Date dateTo;
   
   private static DateFormat FORMATTER;

   public Projects(String empId, String projectId, String dateFrom, String dateTo){
        this.empID = empId;        
        this.projectID = projectId;
        this.dateFrom =  this.parseDate(dateFrom);
		this.dateTo =    this.parseDate(dateTo);
    }


   public String getEmpId()
   {
	   return empID;
   }
   
   public String getProjId()
   {
	   return projectID;
   }
   
   public long daysInPewriod()
   {
	 long diff = dateTo.getTime() - dateFrom.getTime();
	 return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
   }
   
   public Date parseDate(String date) {
	    java.util.List<String> patternList = Arrays.asList("MM-dd-yyyy", "MM/dd/yyyy", "dd.MM.yyyy", "yyyy-MM-dd",
	    		"yyyy.MM.dd", "yyyyy.MMMMM.dd", "dd MMM yyyy", "yyyyMMdd", "dd/MM/yyyy");
	    
	    if(date != null && !date.trim().isEmpty())
	    {
		    for (String pattern : patternList)
		    {
		    	FORMATTER = new SimpleDateFormat(pattern);
		        try {
		            return FORMATTER.parse(date);
		        } 
		        catch (Exception e) 
		        {
		        }
		    }
	    }
	     
	    return new Date();
	}
}
