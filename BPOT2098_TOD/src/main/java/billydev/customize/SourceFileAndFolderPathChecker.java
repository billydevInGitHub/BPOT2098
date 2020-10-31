/*
 * Copyright (c) 2020.  Billydev
 */

package billydev.customize;

import billydev.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;

/**
 * Class holds specific source path check methods. When needed these specific
 * check methods could to take parameters from configuration file.
 *
 * @author Billy Li
 * @since 1.0
 */
public class SourceFileAndFolderPathChecker {

    private static Logger logger = LoggerFactory.getLogger(SourceFileAndFolderPathChecker.class);

    public static FileVisitResult checkSourceFolders(Path path, Path newDestinationPath) {
        if (sourceDirIsCVS(path)) {
            logger.debug("ignoring CVS folder: " + path);
            return FileVisitResult.SKIP_SUBTREE;
        }
        if (sourceDirIsNodeModule(path)) {
            logger.debug("ignoring Node Module folder: " + path);
            return FileVisitResult.SKIP_SUBTREE;
        }

        if (sourceDirIsMetaData(path)) {
            logger.debug("ignoring metadata folder: " + path);
            return FileVisitResult.SKIP_SUBTREE;
        }
        if (sourceDirIsMetaDataOnPM1000(path)) {
            logger.debug("ignoring metadata folder: " + path);
            return FileVisitResult.SKIP_SUBTREE;
        }

        // we do not need to copy the directory if it exists
        if (Files.exists(newDestinationPath)) {
            return FileVisitResult.CONTINUE;
        }
        return null;
    }


    /**
     * Check if path is a compiled java class file.
     *
     * @param path the source file to be checked
     * @return true if it is class file or false if not
     */
    public static boolean sourceFileNotClassFile(Path path) {

        String pattern = "glob:**.class";
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
        if (matcher.matches(path)) {
            logger.debug("Found a class file will be ignored " + path);
            return false;
        }
        return true;
    }

    /**
     * Check if path is tmp file
     *
     * @param path the source file to be checked
     * @return true if it is tmp file or false if not
     */
    public static boolean sourceFileNotTmpFile(Path path) {
        String pattern = "glob:**.tmp";
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
        if (matcher.matches(path)) {
            logger.debug("Found a tmp file will be ignored " + path);
            return false;
        }
        return true;
    }

    /**
     * Check if source file is a CVS file.
     *
     * @param path source file
     * @return true if it is cvs file or false if not
     */
    private static boolean sourceDirIsCVS(Path path) {

        String pattern = "glob:CVS";
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
        if (matcher.matches(path.getFileName())) {
            logger.debug("Found a CVS folder will be ignored " + path);
            return true;
        }
        return false;
    }

    /**
     * Check if source folder is a NodeModule folder as the backup
     * need ignore node module folders as they are too big.
     *
     * @param path source folder to be checked
     * @return true if it is node module folder
     */

    private static boolean sourceDirIsNodeModule(Path path) {
        String pattern = "glob:**node_modules*";
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
        if (matcher.matches(path)) {
            logger.debug("Found a node_modules folder will be ignored " + path);
            return true;
        }
        return false;
    }

    /**
     * Check if source folder is a meta data folder.
     *
     * @param path source folder
     * @return true if it meta data folder or false if not
     */
    private static boolean sourceDirIsMetaData(Path path) {
        String pattern = "glob:**wsCW7**metadata";
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);

        if (matcher.matches(path)) {
            // Need to exclude DBConnectionMetaData
            String patternSpecial1 = "glob:**DBConnectionMetaData";
            PathMatcher matcherSpecial1 = FileSystems.getDefault().getPathMatcher(patternSpecial1);
            // if it is this special case, we just say this is not a Meta Data folder we need to ignore
            if (matcherSpecial1.matches(path)) {
                logger.debug("Found a special folder and make it not a meta folder: " + path);
                return false;
            }
            logger.debug("Found a metadata folder, will be ignored           Path is:  " + path);
            return true;
        }
        return false;
    }

    /**
     * Check if source folder is a project folder.
     *
     * @param path source folder to be checked.
     * @return whether the folder is a project folder
     */
    private static boolean sourceDirIsProject(Path path) {

        String pattern = "glob:**projects*";
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
        if (matcher.matches(path)) {
            logger.debug("Found a project folder will be ignored " + path);
            return true;
        }
        return false;
    }

    /**
     * Check if source folder is meta data foler for PM1000 specific
     *
     * @param path source folder to be checked
     * @return whether source folder is PM1000 specific meta data folder
     */
    private static boolean sourceDirIsMetaDataOnPM1000(Path path) {
        String pattern = "glob:**eclipseworkspaceE16**metadata";
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);

        if (matcher.matches(path)) {
            //need to exclude DBConnectionMetaData
            String patternSpecial1 = "glob:**DBConnectionMetaData";
            PathMatcher matcherSpecial1 = FileSystems.getDefault().getPathMatcher(patternSpecial1);
            //if it is this special case, log and ignore
            if (matcherSpecial1.matches(path)) {
                logger.debug(" Not a meta folder path:" + path);
                return false;
            }
            return true;
        }
        return false;
    }
}
