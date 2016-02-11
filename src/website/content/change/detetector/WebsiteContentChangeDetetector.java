package website.content.change.detetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

/**
 *
 * @author koczka.peter
 */
public class WebsiteContentChangeDetetector {

    private static final int TIMEINTERVALTOCHECK=1;
    private static final String website="http://index.hu/";
    
    private static String websitesActualContent = "";
    private static String websitesOldContent = "";
    private static File lastResultFile;
    
    

    public static void main(String[] args) {
        Runnable checkerRunnable = new Runnable() {
            public void run() {
                check();
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(checkerRunnable, 0, TIMEINTERVALTOCHECK, TimeUnit.SECONDS);
    }

    private static void check() {
        websitesOldContent=websitesActualContent;
        websitesActualContent = downloadWebsiteToLocalVariable();
        /*checkIfFileExistsAndCreateIfNot();
        websitesOldContent = getTheOldContentToString(lastResultFile);
        websitesActualContent = removeEmptyLines(websitesActualContent);
        websitesOldContent = removeEmptyLines(websitesOldContent);*/
        if (!websitesActualContent.equals(websitesOldContent) && !websitesOldContent.isEmpty()) {
            showWarningAboutNotEqualContent(); 
            System.out.println("Nem egyezik");
            //writeActualContentToOldContentFile(lastResultFile, websitesActualContent);
        } else {
            System.out.println("Egyezik");
        }
    }
    private static void showWarningAboutNotEqualContent() {
        JOptionPane.showMessageDialog(null, "Változás történt a(z) " + website +" oldalon!");
    }

    private static String downloadWebsiteToLocalVariable() {
        String result = "";
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;

        try {
            url = new URL(website);
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                if (!line.contains("googleLogin")) {
                    result += line;
                    result += "\n";
                }
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

    private static String getTheOldContentToString(File lastResultFile) {

        BufferedReader reader;
        try {

            reader = new BufferedReader(new FileReader(lastResultFile.getName()));
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            reader.close();
            //System.out.println(stringBuilder.toString());
            return stringBuilder.toString();
        } catch (Exception e) {
            System.out.println("Hiba");
        }
        System.out.println("Hiba");
        return "";
    }

    private static void checkIfFileExistsAndCreateIfNot() {
        lastResultFile = new File("lastResult.txt");
        try {
            if (!lastResultFile.exists()) {
                lastResultFile.createNewFile();
                System.out.println("A fájl nem létezett, ezért létrehoztuk");
            }

        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    private static void writeActualContentToOldContentFile(File lastResultFile, String websitesActualContent) {
        try (PrintWriter out = new PrintWriter(lastResultFile)) {
            out.println(websitesActualContent);
        } catch (Exception e) {
            System.out.println("Hiba történt az aktuális tartalom elmentésekor");
        }
    }

    private static String removeEmptyLines(String content) {
        return content.replaceAll("[\\\r\\\n]+", "");
    }

}
