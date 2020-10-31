
/*
 * Copyright (c) 2020.  Billydev
 */

package billydev;

import billydev.customize.SourceFileAndFolderPathChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * A class that go through the source path tree and copy directory or files
 * when needed.
 *
 * @author Billy Li
 * Since 1.0
 */
public class MyFileCopyVisitor extends SimpleFileVisitor<Path> {

	private static Logger logger = LoggerFactory.getLogger(MyFileCopyVisitor.class);

	private Path source, destination;

	/**
	 * Constructor
	 * @param s source path
	 * @param d destination path
	 */
	public MyFileCopyVisitor(Path s, Path d) {
		source = s;
		destination = d;
	}

	/**
	 * Copy the file when the source version is newer than target version
	 * @param path the source file path under visiting
	 * @param fileAttributes  the source file attributes
	 * @return an instance of FileVisitResult enum.
	 */
	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes fileAttributes) {

		Path newDestinationPath = destination.resolve(source.relativize(path));
		try {
			if (Tools.sourceIsNewer(path, newDestinationPath)
					&& SourceFileAndFolderPathChecker.sourceFileNotTmpFile(path)
					&& SourceFileAndFolderPathChecker.sourceFileNotClassFile(path)
					){
				Files.copy(path, newDestinationPath, StandardCopyOption.REPLACE_EXISTING);
				String message="Copy from "+path +" to "+newDestinationPath;
				logger.info(message);
			}
		} catch (IOException e) {
			logger.warn(e.getMessage());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.debug(sw.toString());
		}
		return FileVisitResult.CONTINUE;
	}


	/**
	 * Create a new folder when needed.Skip not important folders.
	 * @param path  source folder path
	 * @param fileAttributes  source folder attributes
	 * @return  FileVisitResult Enum
	 */

	@Override
	public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes fileAttributes) {
		Path newDestinationPath = destination.resolve(source.relativize(path));

		FileVisitResult checkResult = SourceFileAndFolderPathChecker.checkSourceFolders(path, newDestinationPath);
		if (checkResult != null) {
			return checkResult;
		}
		try {
			Files.copy(path, newDestinationPath, StandardCopyOption.REPLACE_EXISTING);
			String message="check source folder:  " + path
					+ " created new folder: " + newDestinationPath;
			logger.info(message);
		} catch (IOException e) {
			logger.warn(e.getMessage());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.debug(sw.toString());
		}
		logger.debug("Directory visited: "+path);
		return FileVisitResult.CONTINUE;
	}


}
