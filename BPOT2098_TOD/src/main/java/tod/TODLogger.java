package tod;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TODLogger {
	

	private static PrintWriter pw=null;
	public static  PrintWriter getPrintWriter() throws IOException{	
		if(pw==null){
			String pattern = "yy-MM-dd-HH-mm-ss"; /* d for day, M for month, y for year */  
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);	
			
			
			
			
			pw=new PrintWriter(new BufferedWriter(new FileWriter(TOD.getLogLocation()+"\\"
					+formatter.format(new Date())+".bklog")));
			return pw; 
		}
		return pw; 
	}
}
