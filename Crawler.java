
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
private Model_DB db;
private boolean first;
private String seed;


    Crawler(String name,PageVisitedByCrawler pageVisited,int stopCreatria,Model_DB db,int crawlingType)
    {
       //urlNotVisited = new LinkedList<String>();
       stopCreatriaNumber=stopCreatria;
      // urlNotVisited.add(name);
       seed=name;
       this.pageVisited=pageVisited;
       this.db=db;
       if(crawlingType==-1)
       {
           first=true;
           //first time , and it's not interrupted
       }
       else 
       {
           first=false;
       }
       
    }
    Crawler(PageVisitedByCrawler pageVisited,int stopCreatria,Model_DB db,int crawlingType)
    {
       stopCreatriaNumber=stopCreatria;
       this.pageVisited=pageVisited;
       this.db=db;
       if(crawlingType==-1)
       {
           first=true;
           //first time , and it's not interrupted
       }
       else 
       {
           first=false;
       }
    }
    

    

    @Override
    public void run() {
        System.out.println("hello from Crawler Thread");
        
        while(stopCreatriaNumber>0)
        {
             String url;
            //it's supposed to be while we didn't reach the stopping creatria
            if(first==false)
            {
                url=nextUrl();
            }
            else
            {
                url=seed;
                first=false;
            }
             
             createSpider(url);
             stopCreatriaNumber--;
             System.out.println("***total unvisited links*** " + (urlNotVisited.size()));
             
        }
       
        
    }
    
    private String nextUrl()
    {
        String nextUrl=db.getUnvisitedUrl();
        
        while(nextUrl!=null )
        {
             //nextUrl = urlNotVisited.remove(0);
             if(pageVisited.isNotVisited(nextUrl))
             {
                 //if it's not already visited     
                return nextUrl;
             }
            nextUrl=db.getUnvisitedUrl(); 
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

		if (spider.success){
			Bundle data =spider.getData();
			db.saveUnvisitedUrl(data.getChild());
			/* for(int i=0; i<data.getChildCount();i++)
			{
				urlNotVisited.add(data.getChild(i));
            
            
			}*/
			pageVisited.makeVisited(url);
			db.saveBundle(data);
			db.incrementCounterOfStoppingCreatria();
			notifyIndexer();
			//HENA MFROUD NSAVE F DATA BASE KMAAN 
			// AND NOTIFY INDEXER THAT ONE ROW IS READY
		} else {
			//TODO
            //lw el spider failed le ai sbab network aw ai 7aaga . n3ml a
		}
    }

    private void notifyIndexer() {
       
    }
          
    
}


