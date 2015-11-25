package infStrike.utils;

import java.io.File;

public class arenaXmlFilter extends SuffixAwareFilter {
    public boolean accept(File f) {
        boolean accept = super.accept(f);

        if( ! accept) {
            String suffix = getSuffix(f);
            if(suffix != null)
                accept = super.accept(f) || suffix.equals("arena");
        }
        return accept;
    }
    public String getDescription() {
        return "arena Files(*.arena)";
    }
}