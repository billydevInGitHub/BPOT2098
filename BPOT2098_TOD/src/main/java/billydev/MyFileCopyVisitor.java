package billydev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyFileCopyVisitor extends SimpleFileVisitor<Path> {

	private static Logger logger = LoggerFactory.getLogger(MyFileCopyVisitor.class);

	private Path source, destination;

	public MyFileCopyVisitor(Path s, Path d) {
		source = s;
		destination = d;
	}

	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes fileAttributes) {

		Path newDestinationPath = destination.resolve(source.relativize(path));
		try {
			if(Tools.sourceIsNewer(path, newDestinationPath)
					&&Tools.sourceFileNotTmpFile(path)
					&&Tools.sourceFileNotClassFile(path)
					){
				Files.copy(path, newDestinationPath, StandardCopyOption.REPLACE_EXISTING);
				String message="Copy from "+path +"to "+newDestinationPath;
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



	@Override
	public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes fileAttributes) {
		Path newDestinationPath = destination.resolve(source.relativize(path));

		if(Tools.sourceDirIsCVS(path)){
			logger.debug("ignoring CVS folder: "+path);
			return FileVisitResult.SKIP_SUBTREE;
		}
		if(Tools.sourceDirIsNodeModule(path)){
			logger.debug("ignoring Node Module folder: " + path);
			return FileVisitResult.SKIP_SUBTREE;  
		}

		if(Tools.sourceDirIsMetaData(path)){
			logger.debug("ignoring metadata folder: "+path);
			return FileVisitResult.SKIP_SUBTREE;  
		}
		if(Tools.sourceDirIsMetaDataOnPM1000(path)){
			logger.debug("ignoring metadata folder: "+path);
			return FileVisitResult.SKIP_SUBTREE;  
		}
		//we do not need to copy the directory if it exists
		if(Files.exists(newDestinationPath)){
			return FileVisitResult.CONTINUE;
		}
		try {
			Files.copy(path, newDestinationPath, StandardCopyOption.REPLACE_EXISTING);
			String message="check source folder:  "+path
					+" created new folder: "+newDestinationPath;
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
