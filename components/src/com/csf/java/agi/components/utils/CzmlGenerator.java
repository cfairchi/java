package com.csf.java.agi.components.utils;

import agi.foundation.IServiceProvider;
import agi.foundation.cesium.Clock;
import agi.foundation.cesium.CzmlDocument;
import agi.foundation.time.JulianDate;
import agi.foundation.time.TimeInterval;

import java.io.FileWriter;
import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class CzmlGenerator {

    public static CzmlDocument createCZMLDocument(final String theName, ZonedDateTime theStart, ZonedDateTime theStop,
                                                  List<IServiceProvider> theCesiumObjects) {
        final TimeInterval docInterval = new TimeInterval(new JulianDate(theStart), new JulianDate(theStop));
        final Clock clock = new Clock();
        clock.setInterval(docInterval);
        clock.setCurrentTime(docInterval.getStart());

        final CzmlDocument czmlDocument = new CzmlDocument();
        czmlDocument.setName(theName);
        czmlDocument.setDescription(theName);
        czmlDocument.setPrettyFormatting(true);
        czmlDocument.setRequestedInterval(docInterval);
        czmlDocument.setClock(clock);

        for (IServiceProvider obj : theCesiumObjects) {
            czmlDocument.getObjectsToWrite().add(obj);
        }
        return czmlDocument;

    }

    public static String writeCZMLString(CzmlDocument theDocument) {
        final StringWriter streamWriter = new StringWriter();
        try {
            theDocument.writeDocument(streamWriter);
            streamWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return streamWriter.toString();
    }

    public static void writeCZMLDoc(CzmlDocument theDocument) {
        try {
            final FileWriter streamWriter = new FileWriter(theDocument.getName() + ".czml");
            StringWriter sw = new StringWriter();
            theDocument.writeDocument(sw);
            theDocument.writeDocument(streamWriter);
            streamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeCZML(final String theName, ZonedDateTime theStart, ZonedDateTime theStop,
                                 ArrayList<IServiceProvider> theCesiumObjects) {
        final CzmlDocument czmlDocument = createCZMLDocument(theName, theStart, theStop, theCesiumObjects);
        writeCZMLDoc(czmlDocument);
    }

}
