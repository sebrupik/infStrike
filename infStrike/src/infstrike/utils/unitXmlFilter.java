package infStrike.utils;

import java.io.File;

public class unitXmlFilter extends SuffixAwareFilter {
    public boolean accept(File f) {
        boolean accept = super.accept(f);

        if( ! accept) {
            String suffix = getSuffix(f);
            if(suffix != null)
                accept = super.accept(f) || suffix.equals("unit");
        }
        return accept;
    }
    public String getDescription() {
        return "unit Files(*.unit)";
    }
}