package it.univaq.disim.mudablue.matrix;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.apache.commons.math3.linear.RealMatrix;

public class MudaBlueRun2 {

	public static void main(String[] args) throws Exception {
		
		/*
		 * si crea la matrice dalle occorrenze e si fanno tutte le operazioni
		 */
		
		String path= "C:/repos";
		File folder_path = new File(path);
		ArrayList<String> pathList = new ArrayList<String>();
		File[] listOfRepos = folder_path.listFiles();
		
		for(File elem:listOfRepos)
		{
			if(elem.listFiles().length<=1)
			{
				String[] subRepo = elem.list();
				pathList.add(elem+"\\"+subRepo[0]);
			}
			else
			{
				File[] subListOfRepos = elem.listFiles();
				for(File subelem:subListOfRepos)
				{
					pathList.add(subelem.toString());
				}
			}
		}
		
		MatrixManager manager = new MatrixManager();
		LSA lsa = new LSA();
		
		System.out.println("creating matrix");
		
		RealMatrix m = manager.createMatrix();
		
		System.out.println("Numero di Termini: "+m.getRowDimension());
		
		m = manager.cleanMatrix(m);

		System.out.println("Numero di Termini dopo pulizia: "+m.getRowDimension());
		
		m = lsa.algorithm(m);
		
		/*
		 * Similarity
		 */	
		System.out.println("cosine similarity");
		CosineSimilarity csm = new CosineSimilarity();
		m = csm.CS(m);
		
		/*
		 * scrittura su file
		 */
		File file = new File("results.txt");
		FileWriter fileWriter = new FileWriter(file);
		for(int i=0; i<m.getRowDimension(); i++)
		{
			fileWriter.write(pathList.get(i)+" "+m.getRowMatrix(i).toString()+"\n");
		}
		fileWriter.flush();
		fileWriter.close();
		
		DataRefinement dr = new DataRefinement();
		folder_path = new File("results");
		dr.refine(m,folder_path);		

	}

}
