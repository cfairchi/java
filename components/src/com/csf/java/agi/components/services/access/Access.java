package com.csf.java.agi.components.services.access;

import agi.foundation.access.*;
import agi.foundation.access.constraints.CentralBodyObstructionConstraint;
import agi.foundation.propagators.TwoLineElementSet;
import agi.foundation.time.JulianDate;
import com.csf.java.agi.components.models.platforms.FacilityPlatform;
import com.csf.java.agi.components.models.platforms.SatellitePlatform;

public class Access {
    public static AccessQueryResult calculateAccess(TwoLineElementSet tleSet, double latDeg, double lonDeg, double altMeters) {
        SatellitePlatform sat = new SatellitePlatform("tempSat", tleSet);
        FacilityPlatform facility = new FacilityPlatform("tempTarget", latDeg, lonDeg, altMeters);
        LinkInstantaneous link = new LinkInstantaneous(sat, facility);
        CentralBodyObstructionConstraint cbo = new CentralBodyObstructionConstraint();
        cbo.setConstrainedLink(link);
        cbo.setConstrainedLinkEnd(LinkRole.RECEIVER);
        AccessQuery query = new AccessQueryAnd(cbo);
        return query.getEvaluator().evaluate(JulianDate.getNow(), JulianDate.getNow().addDays(5));
    }
}
