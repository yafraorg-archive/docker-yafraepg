/*
 * XMLTVImportPlugin.java
 *
 * This code is released to the public domain.
 *
 * Created by Mattias Larsson, ml@techno.org
 *
 * Created on April 21, 2003, 5:52 PM
 */
/* Modified by Lester Jacobs, April 28th, 2003
 * Code modified to extract the correct tuner channel
 * from the xmltv data.  Also reruns are now determined
 * from the <previously-shown> tag.
 * Search on: lester1
 */

package xmltv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import org.xml.sax.*;
import org.apache.xerces.parsers.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Iterator;

/**
 *
 * More information at:
 *
 * http://mta.techno.org/epg/
 *
 * @author  mta
 */
public class XMLTVImportPlugin implements sage.EPGImportPlugin, ContentHandler, ErrorHandler {
    public String[][] prov = new String[][]{new String[]{ "867507149", "XMLTV Lineup" }};

    private sage.EPGDBPublic epg;
    
    // XML parsing scratch
    private Channel channel;
    private StringBuffer text;
    private HashMap channels;
    private Show show;
    
    /** Creates a new instance of XMLTVImportPlugin */
    public XMLTVImportPlugin() {
    }
    
    public String[][] getLocalMarkets() {
        return prov;
    }
    
    public String[][] getProviders(String str) {
        return prov;
    }
    
    public boolean updateGuide(String str, sage.EPGDBPublic epg) {
        this.epg = epg;
        this.channels = new HashMap();
        
        try {
            XMLReader r = new SAXParser();
            r.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            r.setContentHandler(this);
            r.setErrorHandler(this);
            try {
                r.parse(new InputSource(new FileInputStream("epgdata.xml")));
            } catch (Exception e) {
                e.printStackTrace(System.out);
                return false;
            }
            
            TreeMap map = new TreeMap();
            int tunerchan = 2;
            for (Iterator i = this.channels.values().iterator(); i.hasNext(); ) {
                Channel c = (Channel)i.next();
                String ch;
                
                if (c.getTunerChannel() < 0) { // no real tuner channel available
                    ch = ""+tunerchan;
                    tunerchan++;
                } else { // real tuner available
                    ch = ""+c.getTunerChannel();
                }
                map.put(new Integer(c.getChannelId()), new String[]{ ch });
            }
            
           this.epg.setLineup(867507149L, map);
            // provider hardcoded since sagetv doesnt call get providers
            
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        
        
        
        return true;
    }
    
    /* **** XML Parsing **** */
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.text != null) {
            this.text.append(ch, start, length);
        }
    }
    
    public void endDocument() throws SAXException {
    }
    
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        String tag = qName.toLowerCase();
        boolean isShow = (this.show != null && this.text != null);
        if (tag.equals("channel")) {
            // this.channels.put(channel.id, channel); // lester1.o
            // System.out.println("channel: "+this.channel.longname); // lester1.o
            this.channel.EPGInsert(this.epg);
            this.channel = null;
        } else if (tag.equals("programme")) {
            // add show here...
            this.show.EPGInsert(this.epg);
            this.show = null;
        } else if (tag.equals("sub-title") && isShow) {
            this.show.setEpisodeName(this.text.toString());
        } else if (tag.equals("desc") && isShow) {
            this.show.setDesc(this.text.toString());
        } else if (tag.equals("title") && isShow) {
            this.show.setTitle(this.text.toString());
        } else if (tag.equals("category") && isShow) {
            this.show.setCategory(this.text.toString());
        } else if (tag.equals("previously-shown") && isShow) { // lester1.sn
            this.show.setRerun(true);  // lester1.en
        } else if (tag.equals("display-name") && this.channel != null) {
            // System.out.println("setting display name to '"+this.text+"'\n");  // CKIM
            if (this.channel.isChannelIdSet()) {
                // System.out.println("set was skipped. current channelId='"+(this.channel.getChannelId())+"'\n"); // CKIM
            } else {
                this.channel.setDisplayName(this.text.toString());
                // System.out.println("set was successful\n");  // CKIM
            }
        }
        this.text = null;
    }
    
    public void endPrefixMapping(String prefix) throws SAXException {
    }
    
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }
    
    public void setDocumentLocator(Locator locator) {
    }
    
    public void skippedEntity(String name) throws SAXException {
    }
    
    public void startDocument() throws SAXException {
    }
    
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        String tag = qName.toLowerCase();
        if (tag.equals("channel")) {
            this.channel = new Channel();
            this.channel.setId(atts.getValue("id"));
            this.channels.put(atts.getValue("id"), channel); // lester1
        } else if (tag.equals("programme")) {
            this.show = new Show();
            // System.out.println("start: "+atts.getValue("start"));
            this.show.setStart(atts.getValue("start"));
            this.show.setEnd(atts.getValue("stop"));
            //  System.out.println("chan: "+atts.getValue("channel"));
            String channelAttr = atts.getValue("channel");
            if (channelAttr != null) {
                Channel chan = (Channel)this.channels.get(channelAttr);
                if (chan != null) {
                    this.show.setChannel(chan);
                } else { // never seen before channel, create it
                    chan = new Channel();
                    chan.setId(channelAttr);
                    this.channels.put(channelAttr, chan);
                    this.show.setChannel(chan);
                    chan.EPGInsert(this.epg);
                }
            }
            
        } else if ((tag.equals("category") || tag.equals("desc") || tag.equals("title") ||
        tag.equals("sub-title") || tag.equals("previously-shown")
        )
        && this.show != null) {
            this.text = new StringBuffer();
        } else if (tag.equals("display-name")) {
            this.text = new StringBuffer();
        }
    }
    
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }
    
    public void processingInstruction(String target, String data) throws SAXException {
    }
    
    /** Receive notification of a recoverable error.
     *
     * <p>This corresponds to the definition of "error" in section 1.2
     * of the W3C XML 1.0 Recommendation.  For example, a validating
     * parser would use this callback to report the violation of a
     * validity constraint.  The default behaviour is to take no
     * action.</p>
     *
     * <p>The SAX parser must continue to provide normal parsing events
     * after invoking this method: it should still be possible for the
     * application to process the document through to the end.  If the
     * application cannot do so, then the parser should report a fatal
     * error even if the XML 1.0 recommendation does not require it to
     * do so.</p>
     *
     * <p>Filters may use this method to report other, non-XML errors
     * as well.</p>
     *
     * @param exception The error information encapsulated in a
     *                  SAX parse exception.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.SAXParseException
     *
     */
    public void error(SAXParseException exception) throws SAXException {
        System.out.println(exception.toString());
    }
    
    /** Receive notification of a non-recoverable error.
     *
     * <p>This corresponds to the definition of "fatal error" in
     * section 1.2 of the W3C XML 1.0 Recommendation.  For example, a
     * parser would use this callback to report the violation of a
     * well-formedness constraint.</p>
     *
     * <p>The application must assume that the document is unusable
     * after the parser has invoked this method, and should continue
     * (if at all) only for the sake of collecting addition error
     * messages: in fact, SAX parsers are free to stop reporting any
     * other events once this method has been invoked.</p>
     *
     * @param exception The error information encapsulated in a
     *                  SAX parse exception.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.SAXParseException
     *
     */
    public void fatalError(SAXParseException exception) throws SAXException {
        System.out.println(exception.toString());
    }
    
    /** Receive notification of a warning.
     *
     * <p>SAX parsers will use this method to report conditions that
     * are not errors or fatal errors as defined by the XML 1.0
     * recommendation.  The default behaviour is to take no action.</p>
     *
     * <p>The SAX parser must continue to provide normal parsing events
     * after invoking this method: it should still be possible for the
     * application to process the document through to the end.</p>
     *
     * <p>Filters may use this method to report other, non-XML warnings
     * as well.</p>
     *
     * @param exception The warning information encapsulated in a
     *                  SAX parse exception.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.SAXParseException
     *
     */
    public void warning(SAXParseException exception) throws SAXException {
        System.out.println(exception.toString());
    }
    
}
