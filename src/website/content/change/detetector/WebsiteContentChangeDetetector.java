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

/**
 *
 * @author koczka.peter
 */
public class WebsiteContentChangeDetetector {

    private static String websitesActualContent = "";
    private static String websitesOldContent = "";
    private static File lastResultFile;

    public static void main(String[] args) {
        websitesActualContent = downloadWebsiteToLocalVariable();
        System.out.println(websitesActualContent);
        checkIfFileExistsAndCreateIfNot();
        websitesOldContent = getTheOldContentToString(lastResultFile);
        System.out.println("- - - - -");
        System.out.println(websitesOldContent);
        if (!websitesActualContent.equals(websitesOldContent)) {
            System.out.println("Nem egyezik");
            writeActualContentToOldContentFile(lastResultFile, websitesActualContent);
        } else {
            System.out.println("Egyezik");
        }

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
                result += line;
                result += "\n";
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
        }catch(Exception e){
            System.out.println("Hiba történt az aktuális tartalom elmentésekor");
        }
    }

}
