/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apt_project;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Brenda
 */
public class PageVisitedByCrawler {
    private Set<String> pagesVisited = new HashSet<String>();
	private int dummy ;//this is to test github conflicts only 
    
    

    PageVisitedByCrawler(Model_DB db) {
        //TO GET FILLED FROM DATABASE 3SHAN IF INTERRUPTED 
        System.out.print("entered constructor");
       db.getVisitedUrl(pagesVisited);
        System.out.print("hello");
        
    }
    
    public synchronized boolean isNotVisited (String nextUrl)
    {
        if(pagesVisited.contains(nextUrl))
           return false;
        //pagesVisited.add(nextUrl);
        return true;
    }
    public synchronized void makeVisited (String nextUrl)
    {
        
        pagesVisited.add(nextUrl);
        
    }
    
}
