package billydev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TODCommandLineRunner implements CommandLineRunner {
    public static final String TARGET_USBDISK_MARK_FOLDER1 = "WD Apps for Windows";
    public static final String TARGET_USBDISK_MARK_FOLDER2 = "User Manuals";
    public static final String TARGET_USBDISK_MARK_FOLDER3 = "Backup";
    public static final String SOURCE_USBDISK_MARK_FOLDER4 = "BackupSource";

    @Autowired
    TODProperties todProperties;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Test");


        /*
         * Part 1:  Prepare the parameters to run backup
         */

         //decide target USB disk drive name
        String backupTargetUSBDriverName = "";
        for (String attachedUsbDiskName : todProperties.getAttachedUsbDiskCheckList()) {
            //check if specific folder exists in targetFolder
            if (confirmTargetFolder(attachedUsbDiskName)) {
                backupTargetUSBDriverName = attachedUsbDiskName;
                System.out.println("Attached backup target usb driver is:" + backupTargetUSBDriverName);
                break;
            }
        }
        if (backupTargetUSBDriverName.equals("")) {
            throw new RuntimeException("No USD disk attached!!");
        }
        ;
        String destinationFolder = backupTargetUSBDriverName + ":\\" + todProperties.getBackupDestinationFolder();

        //TODO: log stuff         logLocation=resBundle.getString("PM1001_logfolder");

        // d for day, M for month, y for year
        String pattern = "yy-MM-dd-HH-mm-ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String backupMessage = "Backup Begin Time: " + formatter.format(new Date());
        System.out.println(backupMessage);

//        try {
//            TODLogger.getPrintWriter().println(backupMessage);
//        } catch (IOException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }

/*
 * Part 2: backup
 * */


        Path pathSource = null;
        Path pathDestination = null;
        for (String backupSourceFolder : todProperties.getBackupSourceFolders()) {
            System.out.println("backupSourceFolder is: " + backupSourceFolder);

            pathSource = Paths.get(backupSourceFolder);
            pathDestination = Paths.get(destinationFolder+ "\\" + pathSource.getFileName());
            System.out.println("destination folder is: " + pathDestination.toString());
            backup(pathSource, pathDestination, "logLocationHARDCODED");
        }
    }

    private boolean confirmTargetFolder(String usbFolder) {

        return Files.exists(Paths.get(usbFolder + ":\\" + TARGET_USBDISK_MARK_FOLDER1))
                && Files.exists(Paths.get(usbFolder + ":\\" + TARGET_USBDISK_MARK_FOLDER2))
                && Files.exists(Paths.get(usbFolder + ":\\" + TARGET_USBDISK_MARK_FOLDER3))
                && (!Files.exists(Paths.get(usbFolder + ":\\" + SOURCE_USBDISK_MARK_FOLDER4)));
    }

    private void backup(Path pathSource, Path pathDestination, String logLocation) {
        try {
            Files.walkFileTree(pathSource, new MyFileCopyVisitor(pathSource, pathDestination));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
