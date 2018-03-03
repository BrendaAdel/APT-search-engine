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
    
    
    public synchronized boolean isNotVisited (String nextUrl)
    {
        if(pagesVisited.contains(nextUrl))
           return false;
        pagesVisited.add(nextUrl);
        return true;
    }
    
}
