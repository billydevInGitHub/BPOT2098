/*
 * Copyright (c) 2020.  Billydev
 */

package billydev;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;
import java.util.List;

/**
 * {@link ConfigurationProperties} for Tod application
 * Use @PropertySource for properties need unicode support
 */

@PropertySource(value = "classpath:custom.properties", encoding = "UTF-8")
@Configuration
@ConfigurationProperties(prefix = "tod")
public class TodProperties {

    /**
     * The source folder names which could have UTF8 characters
     */
    @Value("${tod.backupSourceFoldersUTF8}")
    String backupSourceFoldersUTF8;

    /**
     * This field is not used in getter method.
     */
    List<String> backupSourceFolders;

    /**
     * Target parent folder of backup.
     */
    String backupDestinationFolder;

    /**
     * Usb disk driver name check list as the target backup usb disk might
     * randomly mapped to a driver name like E, F, G etc.
     */
    List<String> attachedUsbDiskCheckList;

    public List<String> getBackupSourceFolders() {
        /*
          This is work around when application.properties not support UTF8
         */
        //
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
