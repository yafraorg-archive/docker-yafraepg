/*
 * Show.java
 *
 * This code is released to the public domain.
 *
 * Created by Mattias Larsson, ml@techno.org
 *
 * Created on April 22, 2003, 5:31 PM
 */
/* Modified by Lester Jacobs, April 28th, 2003
 * Code changed to detect rerun indicator
 * (<previously-shown> tag) in xmltv data
 * Search on: lester1
 */

package xmltv;

import java.util.zip.CRC32;
import java.text.SimpleDateFormat;

/**
 *
 * @author  mta
 */
public class Show {
    public String title;
    public String episodeName;
    public String desc;
    public int duration;
    public String category;
    public String subCategory;
    public String[] people;
    public byte[] roles;
    public String rated;
    public String[] expandedRatings;
    public String year;
    public String parentalRating;
    public String[] bonus;
    public String extId;
    public String language;
    public long originalAirDate;
    public boolean rerun;
    
    public String startString;
    public String endString;
    
    public long start;
    public long length;
    
    public int chanId;
    
    static public SimpleDateFormat formatterSeconds;
    static public SimpleDateFormat formatterNoSeconds;
    
    static {
        formatterSeconds = new SimpleDateFormat("yyyyMMddHHmmss Z");
        formatterNoSeconds = new SimpleDateFormat("yyyyMMddHHmm Z");
    }
    
    /** Creates a new instance of Show */
    public Show() {
        this.duration = 0;
        this.rerun = false; // assume shows are firstrun
        this.people = new String[0];
        this.roles = new byte[0];
        this.expandedRatings = new String[0];
        this.bonus = new String[0];
        this.originalAirDate = 0;
        this.length = 0;
        this.chanId = -1;
    }
    
    public void setChannel(Channel chan) {
        if (chan == null) {
            this.chanId = -1;
            return;
        }
        //        if (s.indexOf(" ") > -1) {
        //            String id = s.substring(0, s.indexOf(" ")).trim();
        //            try {
        //            this.chanId = Integer.parseInt(id);
        //            } catch (java.lang.NumberFormatException e) {
        //                System.out.println("fmt: "+e.toString()+" '"+id+"'");
        //                this.chanId = -1;
        //            }
        //        } else {
        //            this.chanId = -1;
        //        }
        this.chanId = chan.getChannelId();
    }
    
    public void setStart(String s) {
        if (s == null)
            return;
        this.startString = s;
        try {
            if (s.indexOf(" ") == 12) {
                this.start = this.formatterNoSeconds.parse(s).getTime();
            } else {
                this.start = this.formatterSeconds.parse(s).getTime();
            }
        } catch (java.text.ParseException e) {
            System.out.println("err: "+e.toString());
            e.printStackTrace(System.out);
        }
    }
    
    public void setEnd(String s) {
        if (s == null)
            return;
        this.endString = s;
        try {
            if (s.indexOf(" ") == 12) {
                this.length = this.formatterNoSeconds.parse(s).getTime()-this.start;
            } else {
                this.length = this.formatterSeconds.parse(s).getTime()-this.start;
            }
        } catch (java.text.ParseException e) {
            System.out.println("err: "+e.toString());
            e.printStackTrace(System.out);
        }
    }
    
    public void setCategory(String s) {
        if (this.category == null) {
            this.category = s;
        } else {
            this.subCategory = s;
        }
    }
    
    public void setTitle(String s) {
        this.title = s;
    }
    
    public void setDesc(String s) {
        this.desc = s;
    }

    public void setRerun(boolean b) {    // lester1.sn
        this.rerun = b;
        // System.out.println("Rerun: "+this.rerun);
    }						     // lester1.en
    
    public void setEpisodeName(String s) {
        this.episodeName = s;
    }
    
    public void EPGInsert(sage.EPGDBPublic epg) {
        // create extId
        CRC32 crc = new CRC32();
        if (this.title != null)
            crc.update(this.title.getBytes());
        if (this.episodeName != null) {
            crc.update(this.episodeName.getBytes());
        } else {
            if (this.startString != null) {
                crc.update(this.startString.getBytes());
            }
        }
        if (this.desc != null)
            crc.update(this.desc.getBytes());
        
        // apply logic here.. if we cant determine if this is a unique show (ep name?)
        // then include showing time in the EP UID, so make it unique
        this.extId = "EP" + Long.toHexString(crc.getValue());
        
        if (!rerun) { // firstrun
            if (!epg.addShowPublic(null, this.title, this.episodeName, this.desc, this.duration,
            this.category, this.subCategory, this.people, this.roles, this.rated,
            this.expandedRatings, this.year, this.parentalRating, this.bonus,
            this.extId, this.language, this.originalAirDate
            )) {
                System.out.println("add show failed 2");
            }
        } else { // rerun
            if (!epg.addShowPublic(this.title, null, this.episodeName, this.desc, this.duration,
            this.category, this.subCategory, this.people, this.roles, this.rated,
            this.expandedRatings, this.year, this.parentalRating, this.bonus,
            this.extId, this.language, this.originalAirDate
            )) {
                System.out.println("add show failed");
            }
        }
        // code to add airing as well, later
        if (this.chanId > -1 && this.length > 0) {
          //  System.out.println("Airing: "+this.chanId+" "+this.extId+" "+new java.util.Date(this.start).toString());
            if (!epg.addAiringPublic(this.extId, this.chanId, this.start, this.length)) {
                System.out.println("airing add failed");
            }
        }
    }
    
}
