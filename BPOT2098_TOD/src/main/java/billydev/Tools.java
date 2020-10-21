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

public class Tools {

	private static Logger logger = LoggerFactory.getLogger(Tools.class);

	public static boolean sourceFileNotClassFile(Path path) {

		String pattern="glob:**.class";
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
		if(matcher.matches(path)){
			logger.debug("Found a class file will be ignored " + path);
			return false; 
		}
		return true;
	}

	public static  boolean sourceFileNotTmpFile(Path path) {
		String pattern="glob:**.tmp";
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
		if(matcher.matches(path)){
			logger.debug("Found a tmp file will be ignored " + path);
			return false; 
		}
		return true;
	}

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
	
	public static  boolean sourceDirIsCVS(Path path) {

		String pattern="glob:CVS";
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
		if(matcher.matches(path.getFileName())){
			logger.debug("Found a CVS folder will be ignored " + path);
			return true; 
		}
		return false;
	}		
	
	public static boolean sourceNeedDisplayInConsoleDuringCopy(Path path){
		String pattern="glob:*.{java,uxf,sql,sh,css,html,htm,js,doc, docx,xls,xlsx,bat,txt}";
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);		
		if(matcher.matches(path.getFileName())){
			return true; 
		}
		return false;
	}

	public static boolean sourceDirIsNodeModule(Path path) {
		String pattern="glob:**node_modules*";
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
		if(matcher.matches(path)){
			logger.debug("Found a node_modules folder will be ignored " + path);
			return true; 
		}
		return false;
	}

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

	public static boolean sourceDirIsProject(Path path) {

		String pattern="glob:**projects*";
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
		if(matcher.matches(path)){
			logger.debug("Found a project folder will be ignored " + path);
			return true; 
		}
		return false;
	}

	public static boolean sourceDirIsMetaDataOnPM1000(Path path) {
		String pattern="glob:**eclipseworkspaceE16**metadata";
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pattern);
		
		if(matcher.matches(path)){
			/**
			 * need to exclude DBConnectionMetaData
			 */
			String patternSpecial1="glob:**DBConnectionMetaData";
			PathMatcher matcherSpecial1 = FileSystems.getDefault().getPathMatcher(patternSpecial1);
			/**
			 * if it is this special case, we just say this is not a Meta Data folder we need to ignore
			 */
			if(matcherSpecial1.matches(path)){
				System.out.println("        Not a meta folder ....................path:"+path);
				return false; 
			}
			return true;
		}
		return false;
	}


}
