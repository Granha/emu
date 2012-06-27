package server;

import java.io.*;

public class FileManager {
	public File dir;
	
	public FileManager(String path) {
		this.dir = new File(path);

		if(!dir.exists()){
			dir.mkdir();
		}
	}

	public String[] getList(){
		
		String[] children = dir.list();
		
		if (children != null) {
		    for (int i=0; i<children.length; i++) {
		        String filename = children[i];
		        System.out.println(filename);
		    }
		}		
		
		return children;
	}
	
	public String getFile(){	
		return null;
	}
	
	public File addFile(String name){
		File myFile = new File(dir, name);
		
		try{
			myFile.createNewFile();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return myFile;		
	}
}
