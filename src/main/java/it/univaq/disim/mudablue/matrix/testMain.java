package it.univaq.disim.mudablue.matrix;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.linear.RealMatrix;

public class testMain {

	public static void main(String[] args) throws IOException {

		long startTime = System.currentTimeMillis(); //elapsed time
		
		ArrayList<String> path_list = new ArrayList<String>();
		
		MatrixManager manager = new MatrixManager();
		LSA lsa = new LSA();
		
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
		
		
		
		ArrayList<ArrayList<Double>> occurrencies_list = new ArrayList<ArrayList<Double>>();
		
		occurrencies_list = manager.createFiles(path_list);
		
		
		
		RealMatrix m = manager.createMatrix(occurrencies_list);
		
		
		/*
		 * formato dati in ingresso: repository X termini
		 * 
		 * 				  __Repos
		 * 			Terms|
		 * 
		 * 	matrix={{repo1,repo2,repo3,repo4},{repo1,repo2,repo3,repo4}.... per n termini}
		 */
		
		//debug
		//double[][] matrixData = {{1,1,0,0,0,0,0},{2,1,1,0,0,0,0},{0,1,3,0,0,0,0},{0,1,1,0,0,0,0},{0,1,0,0,0,1,0},{1,0,0,0,1,0,0},{0,0,0,2,1,1,0},{0,0,0,0,2,1,0},{0,0,0,1,0,0,1},{1,0,0,1,1,1,1}};
		//double[][] matrixData = {{1,0,0,1,0,0,0,0,0},{1,0,1,0,0,0,0,0,0},{1,1,0,0,0,0,0,0,0},{0,1,1,0,1,0,0,0,0},{0,1,1,2,0,0,0,0,0},{0,1,0,0,1,0,0,0,0},{0,1,0,0,1,0,0,0,0},{0,0,1,1,0,0,0,0,0},{0,1,0,0,0,0,0,0,1},{0,0,0,0,0,1,1,1,0},{0,0,0,0,0,0,1,1,1},{0,0,0,0,0,0,0,1,1}};
		//RealMatrix m = MatrixUtils.createRealMatrix(matrixData);
		
		System.out.println("Numero di Termini: "+m.getRowDimension());
		
		m = manager.cleanMatrix(m);

		System.out.println("Numero di Termini dopo pulizia: "+m.getRowDimension());
		
		m = lsa.algorithm(m);
		
		for(int i=0; i<m.getRowDimension(); i++)
		{
			//System.out.println(m.getRowMatrix(i));
		}
		
		/*
		 * Similarity
		 */
		CosineSimilarity csm = new CosineSimilarity();
		m = csm.CS(m);
		
		/*
		 * scrittura su file
		 */
		File file = new File("results.txt");
		FileWriter fileWriter = new FileWriter(file);
		for(int i=0; i<m.getRowDimension(); i++)
		{
			fileWriter.write(path_list.get(i)+" "+m.getRowMatrix(i).toString()+"\n");
		}
		fileWriter.flush();
		fileWriter.close();
		
		DataRefinement dr = new DataRefinement();
		folder_path = new File("results");
		dr.refine(m,folder_path);		
		long estimatedTime = System.currentTimeMillis() - startTime;

		System.out.println(		String.format("%d min, %d sec", 
			    TimeUnit.MILLISECONDS.toMinutes(estimatedTime),
			    TimeUnit.MILLISECONDS.toSeconds(estimatedTime) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(estimatedTime))
			));
		
	}

}
