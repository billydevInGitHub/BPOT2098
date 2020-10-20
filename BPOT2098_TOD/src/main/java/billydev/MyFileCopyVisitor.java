package billydev;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

	

	private Path source, destination;

	public MyFileCopyVisitor(Path s, Path d) {
		source = s;
		destination = d;
		
	}

	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes fileAttributes) {
		Path newDestinationPath = destination.resolve(source.relativize(path));
//		System.out.println("path is: "+path
//				+"  destination.resolve(source.relativize(path) is:  "
//				+newDestinationPath);
		System.out.println("=========================");
		try {
			if(Tools.sourceIsNewer(path, newDestinationPath)
					&&Tools.sourceFileNotTmpFile(path)
					&&Tools.sourceFileNotClassFile(path)
					){
				String message="   ...Copying from "+path +" \n        ...to "+newDestinationPath;
				//only display to console important files
				if(Tools.sourceNeedDisplayInConsoleDuringCopy(path)){
				    System.out.println(message);
				}
//				TODLogger.getPrintWriter().println(message);
				Files.copy(path, newDestinationPath, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return FileVisitResult.CONTINUE;
	}



	@Override
	public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes fileAttributes) {
		Path newDestinationPath = destination.resolve(source.relativize(path));

		if(Tools.sourceDirIsCVS(path)){
			return FileVisitResult.SKIP_SUBTREE;
		}

		if(Tools.sourceDirIsNodeModule(path)){
			return FileVisitResult.SKIP_SUBTREE;  
		}

		if(Tools.sourceDirIsMetaData(path)){
			System.out.println("ignoring metadata folder");
			return FileVisitResult.SKIP_SUBTREE;  
		}
		if(Tools.sourceDirIsMetaDataOnPM1000(path)){
			System.out.println("ignoring metadata folder");
			return FileVisitResult.SKIP_SUBTREE;  
		}
		//we do not need to copy the directory if it exists
		if(Files.exists(newDestinationPath)){
			return FileVisitResult.CONTINUE;
		}
		
		try {
			String message="   ...check source folder:  "+path
					+" \n        ...create new folder: "+newDestinationPath;
			//only display to console important files
			if(Tools.sourceNeedDisplayInConsoleDuringCopy(path)){
			    System.out.println(message);
			}
      //todo:TODLogger.getPrintWriter().println(message);
			Files.copy(path, newDestinationPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Directory visited "+path);
		return FileVisitResult.CONTINUE;
	}
}
