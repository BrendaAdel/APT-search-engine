
package apt_project;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author Bassem
 * added the jsoup-1.11.2.jar
 * CTRL + SHIFT + ALT + S -> modules -> dependencies -> + jar
 */
public class Spider{

    private final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

    /**
     * the url that this spider object is supposed to visit and handle it's html files
     */
    private String url;

    /**
     * the HTML document file downloaded from the seed link
     */
    private Document htmlDocument;

    /**
     * list of links found on page
     */
    private Elements links;

    /**
     * checked by the class who owns the spider as a state flag
     */
    public boolean success;

    /**
     * number of links that can be returned by this spider
     */
    public int linksCount;

    {
        htmlDocument = null;
        success = false;
        linksCount = 0;
    }


    Spider(String url)
    {
        System.out.println("new Spider initialized, visiting " + url);
        this.url = url;
        crawl();
    }

    private void crawl ()
    {
        final Document document = downloadDocument();
        if (document != null) {
            findLinks();
            System.out.println("Received web page at " + url + " , found " + linksCount + " links.");
            this.success = true;
        } else {
            this.success = false;
        }
    }

    private Document downloadDocument ()
    {
        try {
            Connection connection = Jsoup.connect(this.url).userAgent(this.USER_AGENT);
            this.htmlDocument =connection.get();
            return this.htmlDocument;

        } catch (IOException ioe) {
            System.out.println("Error in out HTTP request" + ioe);
            this.htmlDocument = null;
            this.success = false;
            return null;
        }
    }

    //TODO : needs cleaning the links from the internal #links in page and empty links
    private void findLinks()
    {
        if (this.htmlDocument != null){
            this.links = this.htmlDocument.select("a[href]");
            System.out.println("Found (" + this.links.size() + ") links");
            this.linksCount = this.links.size();
        }
        else {
            this.linksCount = 0;
            System.out.println("this spider wasn't successful crawling it's page");
        }
    }

    public void printLinks()
    {
        if (this.success) {
            for (Element link : this.links) {
                System.out.println(link.toString());
            }
        }
    }

    //TODO
    public List<String> getLinks( List<String> li)
    {

        if (this.success){
            for (Element link : this.links) {
                //this.links.add(link.absUrl("href"));
                //TODO check ezay hraga3 el links di le brenda
            }
            return li;
        }
        else {
            System.out.println("this spider wasn't successful crawling it's page, returning an empty links list");
            return li;
        }
    }

    //TODO
    private void checkRobot(){
        /*
        // https://stackoverflow.com/questions/25731346/reading-robot-txt-with-jsoup-line-by-line
        try(BufferedReader in = new BufferedReader(
            new InputStreamReader(new URL("http://google.com/robots.txt").openStream()))) {
            String line = null;
            while((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    //TODO
    public String hashPage(){
        System.out.print(this.htmlDocument.toString());
        return "S";
    }
}
