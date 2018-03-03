/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apt_project;
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
    @Override
    public void run() {
        System.out.println("hello from controllerCrawler Thread");
        intialization();
        createThreads();
        //
        while(true)
        {
            reCrawel();
        }
    }
    private void intialization ()
    {
        System.out.println("enter number of threads");
        Scanner in = new Scanner(System.in);
        numOfThreads = in.nextInt();
        myList = new ArrayList<>();
        pageVisited= new PageVisitedByCrawler();
        // we will make the user enter seeds equivalent to number of threads
        for(int i=0 ; i<numOfThreads ;i++)
        {
            System.out.println("enter a seed");
            String input=in.next();
            myList.add(input);
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
