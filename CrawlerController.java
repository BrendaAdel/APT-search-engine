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
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Brenda
 */
public class CrawlerController implements Runnable{
private List<String> myList;
private Crawler[] threadArray ;
private PageVisitedByCrawler pageVisited ;
private Model_DB db;
private int numOfThreads;
private  final int MAX_PAGES = 5000;
private boolean crawled=false;
private int firstTime;
//private boolean firstTime;// to indicate if he is returning from an interrupt or it's the first time 
    @Override
    public void run() {
        System.out.println("hello from controllerCrawler Thread");
        intialization();
        //if not craweled , create thread and go on the normal flow 
        if(!crawled)
        try 
        {
            createThreads();
        }
        catch (InterruptedException ex) 
        {
            System.out.println("can't create threads");
        }
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
        pageVisited= new PageVisitedByCrawler(db);
        try
        {
                FileReader fileReader = new FileReader("input.txt");

                BufferedReader bufferedReader =new BufferedReader(fileReader);


                line = bufferedReader.readLine();    
                if(line==null)
                {
                    firstTime=-1;
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
                   
                    if(line!=null)
                    {
                        crawled=true;
                        firstTime=1;
                    }
                        
                    else
                    {
                         firstTime=0;
                       
                        crawled=false;
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
    private void createThreads() throws InterruptedException
    {
        threadArray = new Crawler[numOfThreads]; //dynamic array of crawler
        Thread[] temp= new Thread [numOfThreads] ;
        int target=db.getStoppingCreatria();
        int stopCreatria=(5000-target)/numOfThreads;
        int stopCreatria2=((5000-target)/numOfThreads)+((5000-target)%numOfThreads);
        for(int i=0 ; i<numOfThreads;i++)
        {
            if(firstTime==-1)
            {
                if(i==0)
                {
                     threadArray[i] = new Crawler(myList.get(i),pageVisited,stopCreatria2,db,firstTime);
                }
                else
                {
                     threadArray[i] = new Crawler(myList.get(i),pageVisited,stopCreatria,db,firstTime);
                }
            }
            else if (firstTime==0)
            {
                //crawler got interrupted
                if(i==0)
                {
                     threadArray[i] = new Crawler(pageVisited,stopCreatria2,db,firstTime);
                }
                else
                {
                     threadArray[i] = new Crawler(pageVisited,stopCreatria,db,firstTime);
                }
            }
            
          

          temp[i]= new Thread(threadArray[i]);
            //temp = new Thread(threadArray[i]).start();
          temp[i].start();
          
          
        }
     
        for(int i=0 ; i<numOfThreads;i++)
        {
            temp[i].join();
        }
        try {
            FileWriter fileWriter =new FileWriter("input.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("crawled");
           
           
        }
        catch(IOException ex) {
            System.out.println( "Error writing to file '");

        }

        
        
    }
    private void reCrawel()
    {
        
    }
    
    
}
