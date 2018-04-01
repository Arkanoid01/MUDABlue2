package it.univaq.disim.mudablue.matrix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MudaBlueRun {

	public static void main(String[] args) throws IOException {
		
		/*
		 * qui si contano le occorrenze
		 */
		ArrayList<String> path_list = new ArrayList<String>();
		
		MatrixManager manager = new MatrixManager();
		
		String path= "C:/repos";
		
		File folder_path = new File(path);
		File[] listOfRepos = folder_path.listFiles();
		
		/*
		 * controllo per gli utenti che hanno multi repository
		 */
		for(File elem:listOfRepos)
		{
			if(elem.listFiles().length<=1)
			{
				String[] subRepo = elem.list();
				path_list.add(elem+"\\"+subRepo[0]);
			}
			else
			{
				File[] subListOfRepos = elem.listFiles();
				for(File subelem:subListOfRepos)
				{
					path_list.add(subelem.toString());
				}
			}
		}
		manager.createFiles(path_list);

	}

}
