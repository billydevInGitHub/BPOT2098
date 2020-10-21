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

    /**
     * The folder names which could have UTF8 characters
     */
    @Value("${tod.backupSourceFoldersUTF8}")
    String backupSourceFoldersUTF8;

    /**
     * This is actually not used until application.properties support
     * UTF8 loading
     */
    List<String> backupSourceFolders;

    String backupDestinationFolder;

    List<String> attachedUsbDiskCheckList;

    public List<String> getBackupSourceFolders() {
        /**
         * This is temp work around when application.properties not support UTF8
         */
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
