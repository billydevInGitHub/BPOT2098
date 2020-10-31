package billydev;

import billydev.customize.SourceDiskDriveSpecificInformationConstants;
import billydev.customize.TargetDiskDriveSpecificFolderNameConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that can be used to backup from source folders to target folder.
 * By default the class will do the following steps to do the backup:
 *
 * <ul>
 * <li>Inject to Spring Boot as a Component</li>
 * <li>Use {@link MyFileCopyVisitor} instance when visitor the source path tree
 * using {@link  Files} walkFileTree </li>
 * </ul>
 * @author Billy Li
 * @since 1.0
 */

@Component
public class TodCommandLineRunner implements CommandLineRunner {


    @Autowired
    TodProperties todProperties;

    private static  Logger logger = LoggerFactory.getLogger(TodCommandLineRunner.class);

    @Override
    public void run(String... args) throws Exception {

        long startTime = System.currentTimeMillis();
        String pattern = "yy-MM-dd HH-mm-ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        try {
            // Part 1:  Prepare the parameters to run backup.
            String backupTargetUSBDriverName = "";
            for (String attachedUsbDiskName : todProperties.getAttachedUsbDiskCheckList()) {
                // check if specific folder exists in target drive
                // usually there is only one target drive attached
                // the first drive has all marker folders will be target drive
                if (confirmTargetFolders(attachedUsbDiskName)) {
                    backupTargetUSBDriverName = attachedUsbDiskName;
                    logger.info("Attached backup target usb driver is decided: " + backupTargetUSBDriverName);
                    break;
                }
            }
            if (backupTargetUSBDriverName.equals("")) {
                throw new RuntimeException("No target USD disk attached!!");
            }
            if (!Files.exists(Paths.get(SourceDiskDriveSpecificInformationConstants.SOURCE_DISK_DRIVE_NAME
                    + SourceDiskDriveSpecificInformationConstants.SOURCE_DISK_MARK_FOLDER1))) {
                throw new RuntimeException("Source Disk drive not found or it does not have marker folder!");
            }
            String destinationFolder = backupTargetUSBDriverName + ":\\" + todProperties.getBackupDestinationFolder();
            String backupMessage = "Backup begin time: " + formatter.format(new Date());
            logger.info(backupMessage);

            // Part 2: do the backup.
            Path pathSource = null;
            Path pathDestination = null;
            for (String backupSourceFolder : todProperties.getBackupSourceFolders()) {
                logger.info("Back up source folder: " + backupSourceFolder);
                pathSource = Paths.get(backupSourceFolder);
                pathDestination = Paths.get(destinationFolder + "\\" + pathSource.getFileName());
                logger.info("Back up destination folder: " + pathDestination.toString());
                backup(pathSource, pathDestination);
            }
            long estimatedTime = System.currentTimeMillis() - startTime;
            String message="Files copied successfully.  Time elapsed: " + estimatedTime + " milliseconds  End time: "
                    + formatter.format(new Date());
            logger.info(message);
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            logger.debug(sw.toString());
            System.exit(2);
        }
    }

    private boolean confirmTargetFolders(String usbFolder) {
        return Files.exists(Paths.get(usbFolder + ":\\"
                    + TargetDiskDriveSpecificFolderNameConstants.TARGET_USBDISK_MARK_FOLDER1))
                && Files.exists(Paths.get(usbFolder
                    + ":\\" + TargetDiskDriveSpecificFolderNameConstants.TARGET_USBDISK_MARK_FOLDER2))
                && Files.exists(Paths.get(usbFolder + ":\\"
                    + TargetDiskDriveSpecificFolderNameConstants.TARGET_USBDISK_MARK_FOLDER3))
                && (!Files.exists(Paths.get(usbFolder + ":\\"
                    + SourceDiskDriveSpecificInformationConstants.SOURCE_DISK_MARK_FOLDER1)));
    }

    private void backup(Path pathSource, Path pathDestination) {
        try {
            Files.walkFileTree(pathSource, new MyFileCopyVisitor(pathSource, pathDestination));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
