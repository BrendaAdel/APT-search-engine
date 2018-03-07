
package apt_project;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
            //"Chrome/13.0.782.112";
            "Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3 (FM Scene 4.6.1) ";
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


    StringBuilder ROBOTS;

    /**
     * the bundle that will be filled and returned
     */
    private Bundle data;

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
        data = new Bundle();
        ROBOTS = new StringBuilder();
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
        if (document != null && checkRobots()) {
            findLinks();
            fillBundleData();
            System.out.println("        * done crawling " + url + " , found " + linksCount + " links.");
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
                System.out.println("        * Downloaded document.");
                break;
            } catch (java.net.ConnectException | java.net.SocketTimeoutException ioe){
                System.out.println("        * Connection timed out, retrying ..");
            } catch (IOException ioe) {
                System.out.println("        * Error in HTTP request, returning no document to crawler");
                this.htmlDocument = null;
                this.success = false;
                break;
            }
        }
        return this.htmlDocument;
    }

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
            System.out.println("        * found " + linksCount + " links.");
        }
        else {
            this.linksCount = 0;
            System.out.println("        * this spider wasn't successful crawling it's page");
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

    /*
    cleans the links of the disallowed links, and returns the robots.txt as string
    */

    /*
    private String checkRobot()
    {
        String parentURL = null;
        StringBuilder ROBOTS = new StringBuilder();
        try {
            parentURL = getParentURL(this.url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        boolean allCrawlers = false;
        List<String> thisURLdisallowedSLashes = new ArrayList<>();

        // https://stackoverflow.com/questions/25731346/reading-robot-txt-with-jsoup-line-by-line
        while (true) {
            try {
                System.out.println("        * Link :Trying to open " + parentURL + "robots.txt");
                BufferedReader in = new BufferedReader(new InputStreamReader(new URL(parentURL + "robots.txt").openStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    //System.out.println(line);
                    ROBOTS.append(line);

                    //extract the disallowed slashes of the ROBOTS file

                    if (allCrawlers) {
                        if (line.contains("Disallow:")) {
                            if (line.contains("/"))
                                thisURLdisallowedSLashes.add(line.substring("Disallow:".length() + 1, line.length()));
                        }
                    }
                    if (line.contains("User-agent: *")) { //first line
                        allCrawlers = true; //direct the behaviour
                    } else if (line.contains("User-agent"))
                        allCrawlers = false; //the next time it sees User-agent redirect the behaviour
                }
                System.out.println("        * Downloaded robots.txt");
                break;

            } catch (MalformedURLException e) {
                System.out.println("        * MalformedURL to " + parentURL + ", no changes to links list.");
                //e.printStackTrace();
                break;
            } catch (java.net.ConnectException e){
                System.out.println("        * Connection timed out, trying to reconnect ...");
            } catch (java.io.FileNotFoundException e) {
                System.out.println("        * robots.txt was not found for " + parentURL + ", no changes to links list.");
                break;
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("        * Unhandled exception accessing " + parentURL + ", no changes to links list.");
                break;
            }
        }
        //compare each link with all the disallowed slashes
        int count = 0;
        for (Iterator<String> iter = httpLinks.listIterator(); iter.hasNext(); ) {
            count ++;
            String link = iter.next();
            //if (link.contains("tmblr"))
             //   System.out.println("sad0"); 3abhady was here

            try {

                if (getParentURL(link).contains("/localhost/")){
                    iter.remove();
                    count--;
                    continue;
                }

                if (getParentURL(link).equals(getParentURL(this.url))) {
                    for (String slash : thisURLdisallowedSLashes) {
                        if (link.indexOf(slash) != -1) {
                            iter.remove();
                            count--;
                            break;
                        }
                    }
                } else {
                    List<String> URLdisallowedSLashes = new ArrayList<>();
                    System.out.println("        * Link " + count + " needs outsider robots.txt");
                    URLdisallowedSLashes = getDisallowedSlashes(link);
                    for (String slash : URLdisallowedSLashes) {
                        if (link.indexOf(slash) != -1) {
                            count--;
                            iter.remove();
                            break;
                        }
                    }
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        return ROBOTS.toString();
    }

    //copying the function above for now TODO fix this redundancy
    private List<String> getDisallowedSlashes(String URL)
    {
        String parentURL = null;
        StringBuilder ROBOTS = new StringBuilder();
        try {
            parentURL = getParentURL(URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (robotsOfLinksMap.containsKey(parentURL))
            return robotsOfLinksMap.get(parentURL);

        boolean allCrawlers = false;
        List<String> thisURLdisallowedSLashes = new ArrayList<>();

        while (true) {
            try {
                System.out.println("        * Trying to open " + parentURL + "robots.txt");
                BufferedReader in = new BufferedReader(new InputStreamReader(new URL(parentURL + "robots.txt").openStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    //extract the disallowed slashes of the ROBOTS file
                    if (allCrawlers) {
                        if (line.contains("Disallow:")) {
                            if (line.contains("/"))
                                thisURLdisallowedSLashes.add(line.substring("Disallow:".length() + 1, line.length()));
                        }
                    }
                    if (line.contains("User-agent: *")) { //first line
                        allCrawlers = true; //direct the behaviour
                    } else if (line.contains("User-agent"))
                        allCrawlers = false; //the next time it sees User-agent redirect the behaviour
                }
                System.out.println("        * Downloaded robots.txt");
                break;

            } catch (MalformedURLException e) {
                System.out.println("        * MalformedURL to " + parentURL + ", no changes to links list.");
                //e.printStackTrace();
                break;
            } catch (java.net.ConnectException e){
                System.out.println("        * Connection timed out, trying to reconnect ...");
            } catch (java.io.FileNotFoundException e) {
                System.out.println("        * robots.txt was not found for " + parentURL + ", no changes to links list.");
                break;
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("        * Unhandled exception accessing " + parentURL + ", no changes to links list.");
                break;
            }
        }
        robotsOfLinksMap.put(parentURL, thisURLdisallowedSLashes);
        return thisURLdisallowedSLashes;
    }
    */

    private boolean checkRobots()
    {
        String parentURL = null;
        try {
            parentURL = getParentURL(this.url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        boolean allCrawlers = false;
        List<String> thisURLdisallowedSLashes = new ArrayList<>();

        // https://stackoverflow.com/questions/25731346/reading-robot-txt-with-jsoup-line-by-line
        while (true) {
            try {
                System.out.println("        * Link :Trying to open " + parentURL + "robots.txt");
                BufferedReader in = new BufferedReader(new InputStreamReader(new URL(parentURL + "robots.txt").openStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    //System.out.println(line);
                    ROBOTS.append(line);

                    //extract the disallowed slashes of the ROBOTS file

                    if (allCrawlers) {
                        if (line.contains("Disallow:")) {
                            if (line.contains("/"))
                                thisURLdisallowedSLashes.add(line.substring("Disallow:".length() + 1, line.length()));
                        }
                    }
                    if (line.contains("User-agent: *")) { //first line
                        allCrawlers = true; //direct the behaviour
                    } else if (line.contains("User-agent"))
                        allCrawlers = false; //the next time it sees User-agent redirect the behaviour
                }
                System.out.println("        * Downloaded robots.txt");
                break;

            } catch (MalformedURLException e) {
                System.out.println("        * MalformedURL to " + parentURL + ", no changes to links list.");
                //e.printStackTrace();
                break;
            } catch (java.net.ConnectException e){
                System.out.println("        * Connection timed out, trying to reconnect ...");
            } catch (java.io.FileNotFoundException e) {
                System.out.println("        * robots.txt was not found for " + parentURL + ", no changes to links list.");
                break;
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("        * Unhandled exception accessing " + parentURL + ", no changes to links list.");
                break;
            }
        }

        if (this.url.contains("/localhost/")){
            return false;
        }

        for (String slash : thisURLdisallowedSLashes) {
            if (url.contains(slash)){
                System.out.println("        * Disallowed by robots.txt, spider was unsuccessful.");
                return false;
            }
        }


        return true;
    }

    private void fillBundleData()
    {
        //ID
        //data.id  ??

        //URL
        data.setUrl(this.url);

        //ROBOTS & DESCRIPTION  https://stackoverflow.com/questions/37591685/parsing-the-html-meta-tag-with-jsoup-library
        data.setRobots(this.ROBOTS.toString());
        Elements metaTags = htmlDocument.getElementsByTag("meta");
        for (Element metaTag : metaTags){
            String content = metaTag.attr("content");
            String name = metaTag.attr("name");
            if ("robots".equals(name))
                data.setDescription(content);
        }

        //CHILDREN -- must be after ROBOTS as robots edits the httpLinks list
        data.setChild(httpLinks);

        //PARENTS
        //data.setParent("");

        //TITLE
        data.setTitle(htmlDocument.title());

        //HEADER
        data.setHeader(htmlDocument.head().text());

        //BODY
        data.setBody(htmlDocument.body().text());

        //HASH
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(htmlDocument.body().text().getBytes());
            byte[] digest = md.digest();
            String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
            System.out.println("        * Hash: " + myHash);
            //TODO fill the bundle with hash

        } catch (NoSuchAlgorithmException e) {
            System.out.println("        * Error hashing html body. hash set to empty string");
            e.printStackTrace();
        }


    }

    private String getParentURL(String url) throws URISyntaxException
    {
        int currLenght , prvLenght = 0;
        URI uri = new URI(url);
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

    public Bundle getData()
    {
        return this.data;

    }
}


//TODO timeout needs fixing, it leads to infinite waiting and retrying