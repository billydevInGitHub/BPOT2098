package billydev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class TODCommandLineRunner  implements CommandLineRunner {
    public static final String TARGET_USBDISK_MARK_FOLDER1="WD Apps for Windows";
    public static final String TARGET_USBDISK_MARK_FOLDER2="User Manuals";
    public static final String TARGET_USBDISK_MARK_FOLDER3="Backup";
    public static final String SOURCE_USBDISK_MARK_FOLDER1="BackupSource";

    @Autowired
TODProperties todProperties;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Test");
        System.out.println("todProperties.rootLocation = " + todProperties.rootLocation);


        /**
         * Part 1:  Prepare the parameters to run backup
         *
         */
        String destinationFolder="";

        /*
        * decide target USB disk
        * */
        String backupTargetUSBDriverName="";
        for(String attachedUsbDiskName:todProperties.getAttachedUsbDiskCheckList()){
            //check if specific folder exists in targetFolder
            if( confirmTargetFolder(attachedUsbDiskName)){
                backupTargetUSBDriverName=attachedUsbDiskName;
                System.out.println("Attached backup target usb driver is:"+backupTargetUSBDriverName);
                break;
            }
        }
        if(backupTargetUSBDriverName.equals("") ){
            throw new RuntimeException("No USD disk attached!!");
        };

        destinationFolder=backupTargetUSBDriverName+":\\"+todProperties.getBackupDestinationFolder();

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
                break;
            default:
                System.out.println("Please input physical machine name, examples: PM1000 or PM1001");
                return;
        }

//TODO: log stuff         logLocation=resBundle.getString("PM1001_logfolder");


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





        Path pathSource=null;
        Path pathDestination=null;
        for (String inputSourceFolder : todProperties.inputSourceFolders) {
            System.out.println("inputSourceFolder = " + inputSourceFolder);

            pathSource= Paths.get(inputSourceFolder);
            pathDestination=Paths.get("destinationFolderHARDCODED"+"\\"+pathSource.getFileName());
            System.out.println("destination folder is:"+pathDestination.toString());
            backup(pathSource, pathDestination, "logLocationHARDCODED");
        }
    }

    private boolean confirmTargetFolder(String usbFolder) {
        return Files.exists(Paths.get(usbFolder + ":\\" + TARGET_USBDISK_MARK_FOLDER1))
                && Files.exists(Paths.get(usbFolder + ":\\" + TARGET_USBDISK_MARK_FOLDER2))
                && Files.exists(Paths.get(usbFolder + ":\\" + TARGET_USBDISK_MARK_FOLDER3))
                && (!Files.exists(Paths.get(usbFolder + ":\\" + TARGET_USBDISK_MARK_FOLDER3)));
    }

    private  void backup(Path pathSource, Path pathDestination, String logLocation) {
        try {
            Files.walkFileTree(pathSource, new MyFileCopyVisitor(pathSource, pathDestination));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
