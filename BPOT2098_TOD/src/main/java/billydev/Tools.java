/*
 * Copyright (c) 2020.  Billydev
 */

package billydev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
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



}
