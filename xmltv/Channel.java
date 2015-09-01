/*
 * Channel.java
 *
 * This code is released to the public domain.
 *
 * Created by Mattias Larsson, ml@techno.org
 *
 * Created on April 4, 2003, 4:42 PM
 */
/*
 * Modified by Lester Jacobs, April 28th, 2003
 * Changes made to extract the tuner channel
 * and station call signs from the xmltv data.
 * Search on: lester1
 *
 * Modified by Lester Jacobs, July 1st, 2003
 * Changes made to account for channel id changes
 * in new xmltv output.
 * Search on: lester2
 */

package xmltv;

import java.util.zip.CRC32;

/**
 *
 * @author  mta
 */
public class Channel {
    public String id; // xmltv channel id
    public String shortname;
    public String longname;
    public String network;
    private int chanId;
    
    /** Creates a new instance of Channel */
    public Channel() {
        this.network = ""; // for now
        this.chanId = -1;
    }
    
    public void setId(String str) {
        // this.id = str;   // lester1.o
        //this.id = str.substring(0, str.indexOf(" ")).trim(); // lester1.n //lester2.o
        //  this.shortname= str.substring(str.indexOf(" ")).trim();
        //  this.chanId = Integer.parseInt(this.id);
        //this.shortname = str.substring(str.indexOf(" ")).trim(); // lester1 //lester2.o
        //this.chanId = Integer.parseInt(this.id); // lester1 //lester2.o
    }
    
    public void setDisplayName(String s) {
        // logic here being that we want the longest name we can find for longname
        if (this.longname == null) {
            this.longname = s;
        } else if (this.longname.length() < s.length()) {
           // this.shortname = this.longname; // this works for UK listings where
            // two display names are available, one longer (ie Channel 4) and one
            // shorter (4).
            this.longname = s;
        } else if (this.longname.length() > s.length()) {
           // this.shortname = s;
        } // equal length display-names will just be ignored
        //this.id = s.substring(s.indexOf(" "), s.lastIndexOf(" ")).trim(); // lester2
        this.id = s.substring(0, s.indexOf(" ")).trim(); // ckim
        this.shortname = s.substring(s.lastIndexOf(" ")).trim(); // lester2
        
        if (!this.id.equals("")) {
            this.chanId = Integer.parseInt(this.id); // lester2
        }
        
    }
    
    public void EPGInsert(sage.EPGDBPublic epg) {
        if (this.shortname == null) {
            if (this.longname != null) {
                this.shortname = this.longname;
            } else {
                this.shortname = this.id;
            }
        }
        if (this.longname == null) {
            this.longname = this.shortname;
        }
        epg.addChannelPublic(this.shortname, this.longname, this.network, getChannelId());
    }
    
    public int getChannelId() {
        if (this.chanId == -1) { // calculate channel id
            CRC32 crc = new CRC32();
            crc.update(this.id.getBytes());
            this.chanId = (int)(crc.getValue()>>16&0xFFFFFFFF);
        }
        return this.chanId;
    }
    
    public boolean isChannelIdSet() {
        return (this.chanId != -1);
    }

    public int getTunerChannel() {
	  // return -1; // lester1.o
        return Integer.parseInt(this.id); // lester1.n
   }
    
}
