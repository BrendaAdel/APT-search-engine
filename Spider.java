
package apt_project;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Bassem
 * added the jsoup-1.11.2.jar
 * CTRL + SHIFT + ALT + S -> modules -> dependencies -> + jar
 */
public class Spider{

    private final String USER_AGENT =
            "Chrome/13.0.782.112";

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
    private List<String> httpLinks = new ArrayList<>();

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
        if (url != null)
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
        while (true){
            try {
                Connection connection = Jsoup.connect(this.url).userAgent(this.USER_AGENT);
                this.htmlDocument =connection.get();
                break;
            } catch (java.net.ConnectException | java.net.SocketTimeoutException ioe){
                System.out.println("Connection timed out, retrying ..");
            } catch (IOException ioe) {
                System.out.println("Error in HTTP request: " + ioe);
                this.htmlDocument = null;
                this.success = false;
                break;
            }
        }
        return this.htmlDocument;
    }

    //TODO : needs cleaning the links from the internal #links in page, empty links and disallowed links
    //TODO remember , el links mmkn tb2a links ktir le nafs el page asln (# 7agat) bs el text hyb2a mo5talef , bta3 el link nafso
    private void findLinks()
    {
        if (this.htmlDocument != null){
            this.links = this.htmlDocument.select("a[href]");

            for (Element link : this.links) {
                if (link.toString().contains("http:/") || link.toString().contains("https:/")){
                    ArrayList LINKS = new ArrayList();
                    String regex = "\\(?\\b(https?://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(link.toString());
                    while(m.find()) {
                        String urlStr = m.group();
                        if (urlStr.startsWith("(") && urlStr.endsWith(")"))
                        {
                            urlStr = urlStr.substring(1, urlStr.length() - 1);
                        }
                        LINKS.add(urlStr);
                    }
                    httpLinks.add(LINKS.get(0).toString());
                }
            }

            this.linksCount = this.httpLinks.size();
        }
        else {
            this.linksCount = 0;
            System.out.println("this spider wasn't successful crawling it's page");
        }
    }

    public void printLinks()
    {
        if (this.success) {
            for (String link : this.httpLinks) {
                System.out.println(link);
            }
        }
    }

    public List<String> getLinks( )
    {
        return httpLinks;
    }

    //TODO :
    // check if the current (child) link is allowed or not
    // save the disallowed tags to check them before returning the links
    public void checkRobot() throws URISyntaxException {
        //get the parent link
        String parentURL = getParentURL(this.url);

        //get the Robots.txt file (TODO check if it even exists)
        // https://stackoverflow.com/questions/25731346/reading-robot-txt-with-jsoup-line-by-line
        try(BufferedReader in = new BufferedReader(
            new InputStreamReader(new URL(parentURL + "robots.txt").openStream()))) {
            String line = null;
            while((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getParentURL(String url) throws URISyntaxException
    {
        int currLenght , prvLenght = 0;
        URI uri = new URI("https://stackoverflow.com");
        URI parent = uri.getPath().endsWith("/") ? uri.resolve("..") : uri.resolve(".");
        while (true){
            prvLenght = parent.toString().length();
            parent = parent.getPath().endsWith("/") ? parent.resolve("..") : parent.resolve(".");
            currLenght = parent.toString().length();
            String tmpText = parent.toString().substring(parent.toString().length() - 2, parent.toString().length() );
            if (Objects.equals(tmpText, ".."))
                return (parent.toString().substring(0,parent.toString().length() - 2));
            else if (currLenght == prvLenght)
                return (parent.toString() + "/");
        }
    }

    //TODO
    public String hashPage(){
        System.out.print(this.htmlDocument.toString());
        return "S";
    }
}
