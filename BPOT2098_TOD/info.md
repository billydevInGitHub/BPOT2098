Manually stuff: 
   (i) manually create "BackupTargetTest"  (for test) or "Backup" folder in target disk
   (ii) setup application.properties
   (iii) for source folders with unicode stuff put into custom.properties file
        this is workaround as application.properties not support utf-8 
   
How to setup application.properties:    
# For a certain backup, need input or check the following factors:
# 1.backupSourceFolders
#      -- there might be more than one source folders, use comma to separate folders
#      -- example: D:\Backup\PM1000\billydev081226,D:\Temp\11060_Exp001
# 2.backupDestinationFolder
#      -- decide the destination folder in attached USB disk. If there is no such backupDestinationFolder
#         then throw an exception
#      -- example: Backup\\PM1000, it does not have Driver name like E,F,G
#                  driver name will be decide automatically
#                  PM1000 must match "backupSourceRootLocation" (DOUBLE CHECK!!)   
