/*
 * Copyright (c) 2020.  Billydev
 */

package billydev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

/**
 * Tools used in project.
 * @author Billy Li
 * @since 1.0
 */
public class Tools {

	private static Logger logger = LoggerFactory.getLogger(Tools.class);

	/**
	 * Check if path is a compiled java class file.
	 * @param path the source file to be checked
	 * @return true if it is class file or false if not
	 */
	public static boolean sourceFileNotClassFile(Path path) {

		String pattern="glob:**.class";
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
		if(matcher.matches(path)){
			logger.debug("Found a class file will be ignored " + path);
			return false; 
		}
		return true;
	}

	/**
	 * Check if path is tmp file
	 * @param path the source file to be checked
	 * @return true if it is tmp file or false if not
	 */
	public static  boolean sourceFileNotTmpFile(Path path) {
		String pattern="glob:**.tmp";
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
		if(matcher.matches(path)){
			logger.debug("Found a tmp file will be ignored " + path);
			return false; 
		}
		return true;
	}

	/**
	 * Check source file is newer than target file.
	 * @param source  source file
	 * @param target   target file
	 * @return   true if source file is newer or false if not
	 * @throws IOException when IO operation fails
	 */
	public static boolean sourceIsNewer(Path source, Path target) throws IOException {

		if(Files.exists(target)){
			BasicFileAttributes fileAttributes = Files.readAttributes(source,	BasicFileAttributes.class);
			logger.debug("Source File last modified time: " +	fileAttributes.lastModifiedTime());
			FileTime ft1=fileAttributes.lastModifiedTime();
			fileAttributes = Files.readAttributes(target,	BasicFileAttributes.class);
			logger.debug("target File last modified time: " +	fileAttributes.lastModifiedTime());
			FileTime ft2=fileAttributes.lastModifiedTime();
			if (ft1.compareTo(ft2)<=0) {
				return false; 
			}
		}
		return true;
	}

	/**
	 * Check if source file is a CVS file.
	 * @param path source file
	 * @return  true if it is cvs file or false if not
	 */
	public static  boolean sourceDirIsCVS(Path path) {

		String pattern="glob:CVS";
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
		if(matcher.matches(path.getFileName())){
			logger.debug("Found a CVS folder will be ignored " + path);
			return true; 
		}
		return false;
	}

	/**
	 * Check if source folder is a NodeModule folder as the backup
	 * need ignore node module folders as they are too big.
	 * @param path  source folder to be checked
	 * @return  true if it is node module folder
	 */

	public static boolean sourceDirIsNodeModule(Path path) {
		String pattern="glob:**node_modules*";
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
		if(matcher.matches(path)){
			logger.debug("Found a node_modules folder will be ignored " + path);
			return true; 
		}
		return false;
	}

	/**
	 * Check if source folder is a meta data folder.
	 * @param path  source folder
	 * @return true if it meta data folder or false if not
	 */
	public static boolean sourceDirIsMetaData(Path path) {
		String pattern="glob:**wsCW7**metadata";
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
		
		if(matcher.matches(path)){
			/**
			 * Need to exclude DBConnectionMetaData
			 */
			String patternSpecial1="glob:**DBConnectionMetaData";
			PathMatcher matcherSpecial1 = FileSystems.getDefault().getPathMatcher(patternSpecial1);
			/**
			 * if it is this special case, we just say this is not a Meta Data folder we need to ignore
			 */
			if(matcherSpecial1.matches(path)){
				logger.debug("Found a special folder and make it not a meta folder: " + path);
				return false; 
			}
			System.out.println("    Found a metadata folder, will be ignored           Path is:  "+path);
			return true; 
		}
		return false;
	}

	/**
	 * Check if source folder is a project folder.
	 * @param path  source folder to be checked.
	 * @return  wheather the folder is a project folder
	 */
	public static boolean sourceDirIsProject(Path path) {

		String pattern="glob:**projects*";
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
		if(matcher.matches(path)){
			logger.debug("Found a project folder will be ignored " + path);
			return true; 
		}
		return false;
	}

	/**
	 * Check if source folder is meta data foler for PM1000 specific
	 * @param path source folder to be checked
	 * @return  wheather source folder is PM1000 specific meta data folder
	 */
	public static boolean sourceDirIsMetaDataOnPM1000(Path path) {
		String pattern="glob:**eclipseworkspaceE16**metadata";
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
		
		if(matcher.matches(path)){
			 //need to exclude DBConnectionMetaData
			String patternSpecial1="glob:**DBConnectionMetaData";
			PathMatcher matcherSpecial1 = FileSystems.getDefault().getPathMatcher(patternSpecial1);
			 //if it is this special case, log and ignore
			if(matcherSpecial1.matches(path)){
				logger.debug(" Not a meta folder path:"+path);
				return false; 
			}
			return true;
		}
		return false;
	}


}
