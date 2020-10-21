package billydev;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;
import java.util.List;

@PropertySource(value = "classpath:custom.properties", encoding = "UTF-8")
@Configuration
@ConfigurationProperties(prefix = "tod")
public class TODProperties {

    @Value("${tod.backupSourceFoldersUTF8}")
    String backupSourceFoldersUTF8;
    /*
    * manually input in source folders need to be backup
    * */
    List<String> backupSourceFolders;
    /*
    * default backupDestinationFolder is:
    * example: D:\Backup\PM1000 PM1000 is physical machine name
    *          so Backup folder  can have more than one machine backup content
    * */
    String backupDestinationFolder;
    /*
    * examples of attachedUsbDiskCheckList are : D, E, F, G ...
    *   as USB disk got random driver name when it is attached to computer*/
    List<String> attachedUsbDiskCheckList;



    public List<String> getBackupSourceFolders() {
        return Arrays.asList(backupSourceFoldersUTF8.split(","));
    }

    public void setBackupSourceFolders(List<String> backupSourceFolders) {
        this.backupSourceFolders = backupSourceFolders;
    }

    public String getBackupDestinationFolder() {
        return backupDestinationFolder;
    }

    public void setBackupDestinationFolder(String backupDestinationFolder) {
        this.backupDestinationFolder = backupDestinationFolder;
    }

    public List<String> getAttachedUsbDiskCheckList() {
        return attachedUsbDiskCheckList;
    }

    public void setAttachedUsbDiskCheckList(List<String> attachedUsbDiskCheckList) {
        this.attachedUsbDiskCheckList = attachedUsbDiskCheckList;
    }
}
