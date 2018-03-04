/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apt_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author Brenda

// Bassem edited this
=======
 * Bassem edited this - again -- again again
 * *****   jhjkhk kjhjk k
*/
public class test {
//dummy class
    public static void main (String [] args) throws URISyntaxException {

        URI uri = new URI("https://stackoverflow.com/questions/10159186/how-to-get-parent-url-in-java");
        URI parent = uri.getPath().endsWith("/") ? uri.resolve("..") : uri.resolve(".");
        System.out.println(parent);

        /*
        try(BufferedReader in = new BufferedReader(
                new InputStreamReader(new URL("https://google.com/robots.txt").openStream()))) {
            String line = null;
            while((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        //Spider toto = new Spider("http://annaxiin.tumblr.com/");
        //if (toto.success) toto.printLinks();
        //Spider stack = new Spider("http://stackoverflow.com/");
        //if (stack.success) stack.printLinks();

        //if (toto.success) toto.hashPage();
    }
    
}
