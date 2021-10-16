package com.mycompany;

/**
 *
 * @author rbor
 */

import java.io.*;
import java.util.*;

/**
 *
 * @author rbor
 */
public class findWord {

    /**
     * Finds folders with name sSoughtFolder starting with folder fParentFolder
     * in all subfolders down to nesting level iLevelMax
     *
     * @return List of found folders
     */
    private ArrayList<String> listFilesForFolder(final File folder, String sourceFolder) {
        if (sourceFolder.isEmpty()){
            sourceFolder = folder.getPath();
        }
        ArrayList<String> listFiles = new ArrayList<String>();
        try {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    listFiles.addAll(listFilesForFolder(fileEntry, sourceFolder));
                } else if ((fileEntry.getName().endsWith(".inc")) || (fileEntry.getName().endsWith(".t"))){
                    listFiles.add(fileEntry.getPath());
//                    System.out.println(fileEntry.getName());
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());	
        }
        return listFiles;
    }
    
    private boolean checkFile(String fileName, String word){     
        boolean retVal = false;
        String line = null;
        try {            
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) { 
                if (line.contains(word)) {
//                    System.out.println(line);
                    retVal = true;
                    break;
                }
            }   
            bufferedReader.close();         
        } catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        } catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");          
        }
        return retVal;
    }

    	private void saveTestPlan(String out, String word) {
		try {
			String sTestPlan = "findWord_" + word + ".pln";
			File fTestPlan = new File(sTestPlan);
			if(fTestPlan.exists()) fTestPlan.delete();
			RandomAccessFile rafTestPlan = new RandomAccessFile(fTestPlan, "rw");
			rafTestPlan.writeBytes(out);
			rafTestPlan.close();
		}
		catch (Exception e) {
                    System.out.println(e.toString());}
	}
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.out.println("Hello! I can find words or phrases in files with extension .inc and .t");
        final File folder = new File(System.getProperty("user.dir"));
        System.out.println("I will check files in folder '" + folder.getPath() + "'");
        int i = 0;
        String word = "";
        StringBuffer strBuffer = new StringBuffer("");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Let's start! Enter the word: ");
        try {
            word = br.readLine();
        }catch(Exception e) {
            System.out.println(e);
        }
        
        findWord findWord = new findWord();
        for (String file : findWord.listFilesForFolder(folder, "")){
            if (findWord.checkFile(file, word)){
                System.out.println(file.replace(folder.getPath(), ""));
                strBuffer.append("script: " + file.replace((folder.getPath() + "\\"), "") + "\n");
                i++ ;
            }
        }
        System.out.println(i + " files were found.");
        findWord.saveTestPlan(strBuffer.toString(), word);
    }
}

