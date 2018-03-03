/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apt_project;

/**
 *
 * @author Brenda
 */
public class APT_Project {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Thread crawlerController = new Thread(new CrawlerController());
        crawlerController.start();
        Thread indexer = new Thread(new Indexer());
        indexer.start();
        
    }
    
}
