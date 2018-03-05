/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apt_project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Brenda
 */
public class Crawler implements Runnable {
    
private List<String> urlNotVisited;
//private Set<String> pagesVisited = new HashSet<String>();
private int stopCreatriaNumber;
private PageVisitedByCrawler pageVisited;
private boolean recrawled=false;


    Crawler(String name,PageVisitedByCrawler pageVisited,int stopCreatria)
    {
       urlNotVisited = new LinkedList<String>();
       stopCreatriaNumber=stopCreatria;
       urlNotVisited.add(name);
       this.pageVisited=pageVisited;
    }
    Crawler(List<String> urlNotVisited,PageVisitedByCrawler pageVisited,int stopCreatria)
    {
        //for re crawling i will intially give him a list and not a single seed
        this.urlNotVisited = new LinkedList<String>();
        this.urlNotVisited=urlNotVisited;
        stopCreatriaNumber=stopCreatria;
        this.pageVisited=pageVisited;
        recrawled=true;
        
    }

    

    @Override
    public void run() {
        System.out.println("hello from Crawler Thread");
        
        while(stopCreatriaNumber>0)
        {
            //it's supposed to be while we didn't reach the stopping creatria
             String url=nextUrl();
             createSpider(url);
             stopCreatriaNumber--;
             
        }
       
        
    }
    
    private String nextUrl()
    {
        String nextUrl;
       
        while(urlNotVisited.size()>0 )
        {
             nextUrl = urlNotVisited.remove(0);
             if(pageVisited.isNotVisited(nextUrl))
             {
                 //if it's not already visited     
                return nextUrl;
             }
        }
        return null;
    }    

    private void createSpider(String url)
    {
        Spider spider= new Spider(url);
        /*List<String> temp=new ArrayList<>();
        if (spider.success)
            temp = spider.getLinks();
        */
        Bundle data =spider.getData();
        for(int i=0; i<data.getChildCount();i++)
        {
            urlNotVisited.add(data.getChild(i));
        }
        pageVisited.makeVisited(url);
        
        //HENA MFROUD NSAVE F DATA BASE KMAAN 
        // AND NOTIFY INDEXER THAT ONE ROW IS READY 
        
    }
          
    
}
