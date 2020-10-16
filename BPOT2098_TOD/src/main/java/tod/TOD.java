package tod;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.ResourceBundle;



public class TOD {
	public static final String TARGET_USBDISK_MARK_FOLDER1="WD Apps for Windows";
	public static final String TARGET_USBDISK_MARK_FOLDER2="User Manuals"; 
	public static final String TARGET_USBDISK_MARK_FOLDER3="Backup"; 
	private static String logLocation; 
	
	public static String getLogLocation() {
		return logLocation;
	}

	public static void setLogLocation(String logLocation) {
		TOD.logLocation = logLocation;
	}

	public static void main(String[] args) {
		
		
		long startTime = System.currentTimeMillis();	
		
		/**
		 * Part 0: Check parameter number 
		 */
		String backupName=null;
		if(args.length>0){
			 backupName=args[0];
		}
		
		if(backupName==null||backupName.isEmpty()){
			System.out.println("Please input backup name, examples: \"PM1000\", \"PM1001\"");
			return; 
		}
		
		
		
		
		/**
		 * Part 1:  Prepare the parameters to run backup
		 * 
		 */
		ResourceBundle resBundle =ResourceBundle.getBundle("TOD"); 
		ArrayList<String> usbFolderList=new ArrayList<>(); 
		ArrayList<String> inputFolderList=new ArrayList<>(); 
		String destinationFolder=""; 
//		ArrayList<String> keyFileList=new ArrayList<>(); 
		String enumKey="";
		Enumeration<String> enumKeys;
		
		//decide usb folder 
		enumKeys=resBundle.getKeys();
		while(enumKeys.hasMoreElements()){
			enumKey=enumKeys.nextElement();
			if(enumKey.startsWith("usbFolder")){
				usbFolderList.add(resBundle.getString(enumKey));
			}
		}
		String targetUSBDisk=""; 
		for(String usbFolder:usbFolderList){
			if(
					Files.exists(Paths.get(usbFolder+":\\"+TARGET_USBDISK_MARK_FOLDER1))
				&&Files.exists(Paths.get(usbFolder+":\\"+TARGET_USBDISK_MARK_FOLDER2))
				&&Files.exists(Paths.get(usbFolder+":\\"+TARGET_USBDISK_MARK_FOLDER3))){
				targetUSBDisk=usbFolder;
				System.out.println("target usb folder is:"+targetUSBDisk); 
				break;
			}
		}
		if(targetUSBDisk.equals("") ){
			throw new RuntimeException("No USD disk attached!!");
			}; 			
		
		switch(backupName){
		//per different backupName decide different parameters from resource bundle 
		case "PM1000":  ; 
			enumKeys=resBundle.getKeys();
			while(enumKeys.hasMoreElements()){
				enumKey=enumKeys.nextElement();
				if(enumKey.startsWith("PM1000_inputSource")){
					inputFolderList.add(resBundle.getString(enumKey));
				}
				if(enumKey.equals("destination_folder_name")){
					destinationFolder=targetUSBDisk+":\\"+resBundle.getString(enumKey)+"\\PM1000";
					//keyFileDestination=targetUSBDisk+":\\"+resBundle.getString(enumKey); 
				}
//				if(enumKey.startsWith("PM1000_keyFile")){
//					keyFileList.add(resBundle.getString(enumKey));
//				}
			}		
			logLocation=resBundle.getString("PM1000_logfolder");
		break;
		case "PM1001":
			enumKeys=resBundle.getKeys();
			while(enumKeys.hasMoreElements()){
				enumKey=enumKeys.nextElement();
				if(enumKey.startsWith("PM1001_inputSource")){
					inputFolderList.add(resBundle.getString(enumKey));
				}
				if(enumKey.equals("destination_folder_name")){
					destinationFolder=targetUSBDisk+":\\"+resBundle.getString(enumKey)+"\\PM1001";
				}
//				if(enumKey.startsWith("PM1001_keyFile")){
//					keyFileList.add(resBundle.getString(enumKey));
//				}
			}
			logLocation=resBundle.getString("PM1001_logfolder");
    	    break;
		default: 
			System.out.println("Please input physical machine name, examples: PM1000 or PM1001");
		    return; 
		}

		

		String pattern = "yy-MM-dd-HH-mm-ss"; /* d for day, M for month, y for year */  
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);	
		
		String backupMessage="Backup mode:"+backupName+"\n Begin Time: "+formatter.format(new Date()); 
		System.out.println(backupMessage);
		try {
			TODLogger.getPrintWriter().println(backupMessage);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		/**
		 * Part 2:  Backup all source folders to destination 
		 * 
		 */
		Path pathSource=null; 
		Path pathDestination=null; 

		for (String inputFolder:inputFolderList){
			System.out.println("source folder is:"+inputFolder);

			pathSource=Paths.get(inputFolder); 
			pathDestination=Paths.get(destinationFolder+"\\"+pathSource.getFileName());
			System.out.println("destination folder is:"+pathDestination.toString());
			backup(pathSource, pathDestination, logLocation);
		}


		/**
		 * Part 3: pick up  the key files into one folder for email backup
		 * 

		
		System.out.println("Key file back up begin =========== "); 
		String[] keyFileList={
				"\\PM1000\\李斌资料081226\\02-开发资料\\E18-Tools.xls",
				"\\PM1000\\李斌资料081226\\06-个人事务\\思路\\问题思考-机制.xls",
				"\\PM1000\\李斌资料081226\\06-个人事务\\父母家\\BeiK.xls",
				"\\PM1000\\李斌资料081226\\05-文曲星\\HomePage.htm",
				"\\PM1001\\Billydoc080107\\02Dev_Related\\00-EnvSetup\\01-科研思路18.doc",
				"\\PM1001\\Billydoc080107\\02Dev_Related\\00-EnvSetup\\E18-MaintInformation.xlsx",
				"\\PM1001\\Billydoc080107\\02Dev_Related\\00-EnvSetup\\问题思考-FrontMachine.xls",
				"\\PM1001\\Billydoc080107\\02Dev_Related\\00-EnvSetup\\WorkingSpreadSheet.xlsx",
				"\\PM1001\\Billydoc080107\\02Dev_Related\\00-EnvSetup\\PSS_usage_analysis.xlsx",
				"\\PM1001\\Billydoc080107\\02Dev_Related\\00-EnvSetup\\PSSLinks.htm",
				"\\PM1001\\Billydoc080107\\02Dev_Related\\00-EnvSetup\\Production Server's Details.xlsx"
				};
        for (String keyFile: keyFileList){

					pathSource=Paths.get(targetUSBDisk+":\\BackupTest"+keyFile);
					pathDestination=Paths.get(destinationFolder+"\\keyFiles\\"+keyFile.split("\\")[keyFile.split("\\").length-1]);
	
					try {
						if(Tools.sourceIsNewer(pathSource, pathDestination)
								){
							String message="   ...Copying key file "+pathSource +" \n        to "
								+pathDestination;
							System.out.println(message);
							TODLogger.getPrintWriter().println(message);
							Files.copy(pathSource, pathDestination, StandardCopyOption.REPLACE_EXISTING);
						}
					} catch (IOException e) {
						e.printStackTrace();
						throw new RuntimeException(); 
					}
				}

		 */			



		long estimatedTime = System.currentTimeMillis() - startTime;
		

		String message="Files copied successfully!\n"+"Time elapse: "+estimatedTime+" milliseconds\n End time: "
		     +formatter.format(new Date()); ;
		
		System.out.println(message);
		try {
			PrintWriter pw=TODLogger.getPrintWriter();
			pw.println(message);
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(); 
		}
		
	}



	private static void backup(Path pathSource, Path pathDestination, String logLocation) {			
		try {
			Files.walkFileTree(pathSource, new MyFileCopyVisitor(pathSource, pathDestination));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
}
