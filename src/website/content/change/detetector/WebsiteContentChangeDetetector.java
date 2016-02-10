package website.content.change.detetector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author koczka.peter
 */
public class WebsiteContentChangeDetetector {

    private static String websitesActualContent = "";

    public static void main(String[] args) {
        websitesActualContent = downloadWebsiteToLocalVariable();
        
        System.out.println(websitesActualContent);
    }

    private static String downloadWebsiteToLocalVariable() {
        String result = "";
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;

        try {
            url = new URL("https://holtankoljak.hu/");
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                result+=line;
                result+="\n";
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
        
        return result;
    }

}
