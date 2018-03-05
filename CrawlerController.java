/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apt_project;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 *
 * @author Brenda
 */
public class CrawlerController implements Runnable{
private List<String> myList;
private Crawler[] threadArray ;
private PageVisitedByCrawler pageVisited ;
private int numOfThreads;
private  final int MAX_PAGES = 5000;
private boolean crawled=false;
private boolean firstTime;// to indicate if he is returning from an interrupt or it's the first time 
    @Override
    public void run() {
        System.out.println("hello from controllerCrawler Thread");
        intialization();
        //if not craweled , create thread and go on the normal flow 
        if(!crawled)
          createThreads();
        else
        {
           //if it's craweled before call recrawel()
        //reCrawel();
        }

        System.out.print(pageVisited);
      
    }
    private void intialization ()
    {
        String line ="";
        myList = new ArrayList<>();
        pageVisited= new PageVisitedByCrawler();
        try
        {
                FileReader fileReader = new FileReader("input.txt");

                BufferedReader bufferedReader =new BufferedReader(fileReader);


                line = bufferedReader.readLine();    
                if(line==null)
                {
                    System.out.println("enter number of threads");
                    Scanner in = new Scanner(System.in);
                    numOfThreads = in.nextInt();
                     // we will make the user enter seeds equivalent to number of threads
                    for(int i=0 ; i<numOfThreads ;i++)
                    {
                       System.out.println("enter a seed");
                       String input=in.next();
                       myList.add(input);
                    }
                    bufferedReader.close(); 

                    try {
                        FileWriter fileWriter =new FileWriter("input.txt");
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write("firsttime");
                        bufferedWriter.newLine();
                        bufferedWriter.write(numOfThreads);
                        bufferedWriter.close();
                    }
                    catch(IOException ex) {
                        System.out.println( "Error writing to file '");

                    }


                }
                else
                {
                    //the crawler/recrawler got interrupted
                    line = bufferedReader.readLine();
                    numOfThreads=Integer.parseInt(line);
                    line= bufferedReader.readLine();
                    if(line=="crawled")
                        crawled=true;
                    else
                        crawled=false;
                    
                    while((line = bufferedReader.readLine()) != null)
                    {
                        // he is reading the non visited url 
                        myList.add(line);

                    }  

                    bufferedReader.close();
                    


                }
               
                     
        }
        catch(FileNotFoundException ex) {
            System.out.println( "Unable to open file '");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '" );                  
          
        }
        
        
       
        
    }
    private void createThreads()
    {
        threadArray = new Crawler[numOfThreads]; //dynamic array of crawler
        int stopCreatria=5000/numOfThreads;
        int stopCreatria2=(5000/numOfThreads)+(5000%numOfThreads);
        for(int i=0 ; i<numOfThreads;i++)
        {
            if(i==0)
            {
                 threadArray[i] = new Crawler(myList.get(i),pageVisited,stopCreatria2);
            }
            else
            {
                 threadArray[i] = new Crawler(myList.get(i),pageVisited,stopCreatria);
            }
          
          new Thread(threadArray[i]).start(); 
        }        
    }
    private void reCrawel()
    {
        
    }
    
    
}
