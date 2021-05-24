package com.csf.java.utils;

import com.csf.java.models.Tle;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.*;

public class TLEUtils {

    public static Optional<Pair<String, String>> readTLEFile(String tleFile, String desiredSsc) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(tleFile));
            String line1;

            while ((line1 = br.readLine()) != null) {

                String line2 = br.readLine();
                String ssc = line1.substring(2, 5);
                if (desiredSsc.equals(ssc)) {
                    return Optional.of(new Pair<>(line1, line2));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public static Map<String, Tle> downloadTles() {
        List<String> lines = new ArrayList<>();
        try {
            String baseURL = "https://www.celestrak.com/NORAD/elements/active.txt";
            URL url = new URL(baseURL);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                lines.add(output);
            }
            conn.disconnect();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Tle> tles = new HashMap<>();

        for (int i=0; i < lines.size(); i+=3) {
            if ((i+1) < lines.size() && i+2 < lines.size()) {
                Tle tle = new Tle(lines.get(i), lines.get(i+1), lines.get(i+2));
                tles.put(tle.getSscNum(), tle);
            }
        }
        return tles;
    }

    public static Map<String, Tle> downloadTleFile(String thePath) {
        try {
            FileUtils.renameFile(thePath, thePath + ".bak", true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Map<String, Tle> tles = downloadTles();
        try {
            FileWriter outFile = new FileWriter(thePath);
            BufferedWriter out = new BufferedWriter(outFile);
            for(Tle tle : tles.values()) {
                out.write(tle.getName());
                out.write(System.lineSeparator());
                out.write(tle.getLine1());
                out.write(System.lineSeparator());
                out.write(tle.getLine2());
                out.write(System.lineSeparator());
            }
            out.flush();
            out.close();
            outFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tles;
    }


}
