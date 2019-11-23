/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package materialcard;

/**
 *
 * @author J.Weidig
 * TODO: Sprachenwechsel - Meldungen übersetzen
 *       Eingabecheck - was muss unbedingt ausgefüllt sein
 * 
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;
import java.lang.*;
import javax.swing.DropMode;
import javax.swing.*;
import java.net.*;
import java.util.regex.Pattern;
import javax.swing.text.*;

public class MainJFrame extends javax.swing.JFrame {
    
    private String strFenstertitel = java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("MATERIALKARTE");
    private String strVerzeichnis = new String();
    private File[] fileDateien = null;
    private String strDateiExt = "xml";
    private String strDateiCheckSum = "checksums.md5";
    private String strTrennZeichenCheckSum = " ";
    private File fileDateiCheckSum = null;
    private String strXMLDateiname = java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("INHALT");
    
    private Color defaultWarningColor = Color.BLUE;
    private Color defaultNormalColor = Color.BLACK;
    private Color defaultErrorColor = Color.RED;
    private Color defaultBackgroundColor = null;
    
    private File checkSumFile = null;
    
    // zu überprüfende Einträge / Objekte
    // Position 0 enthält TabContainer
    
    private ArrayList checkObjAllgemein = new ArrayList();
    private ArrayList checkObjVideo = new ArrayList();
    private ArrayList checkObjVideoUT = new ArrayList();
    private ArrayList checkObjAudio = new ArrayList();
    
    // md5 liste
    private ArrayList genLabel = new ArrayList();
    private ArrayList genTXT = new ArrayList();
    
    public final static String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    
    // Feldvorgaben /////////////////////////////////////////////////////////////////////////
    private String[] strFramerate = {"", "23,976", "24" , "25" , "29,97"};
    private String[] strColorType = {"", "Color", "B&W"};
    private String[] strChromaSampling = {"", "4:4:4", "4:2:2" , "4:2:0" , "4:1:1", "3:1:1"}; //NOI18N
    private String[] strTexted = {"", "texted", "textless"}; //NOI18N

    private String[] strAspectRatio = {"", "1,33:1", "1,55:1" , "1,66:1" , "1,78:1", //NOI18N
                                       "1,85:1", "2,05:1", "2,21:1", "2,35:1", "2,40:1", //NOI18N
                                       "1,66:1 anamorph", "1,78:1 anamorph", "1,85:1 anamorph", //NOI18N
                                       "2,05:1 anamorph", "2,21:1 anamorph", "2,35:1 anamorph", //NOI18N
                                       "2,40:1 anamorph"}; //NOI18N
    private String[] strAudioType = {"", "Comment", "Nar", "MX", "FX", "DX", "M&E",  //NOI18N
                                    "DX/FX", "DX/Nar", "MX/Nar", "MX/DX", "FX/Nar", //NOI18N
                                    "DX/FX/Nar", "Mix minus", "Full mix", "Pre mix", //NOI18N
                                    "O-Ton", "TV-Mix", "DXGD", "OPT"};  //NOI18N
    private String[] strSpeakerLayout = {"", "1/0", "2/0" , "3/0" , "3/1", "3/2",  //NOI18N
                                         "3/2 LFE", "3/3", "3/3 LFE", "7.1"}; //NOI18N
    private String[] strContentType = {"", "Behind the Scenes", "Bonus Material", "Deleted Scenes", //NOI18N
                                       "Documentary", "Dummyinhalt für EC", "Effects", //NOI18N
                                       "EPK", "Featurette", "Film Clip", "Graphic", //NOI18N
                                       "In Rehearsal", "International Profiles",  //NOI18N
                                       "Interview", "Intro", "Logo", "Main Program", //NOI18N
                                       "Making of", "Music", "Music Clip", "Outtakes", //NOI18N
                                       "Photo", "Press Conference", "Production Material", //NOI18N
                                       "Promo Material", "Radio Clip", "Sound Bite", //NOI18N
                                       "Soundtrack", "Storyboards", "Teaser", "Trailer", //NOI18N
                                       "TV Spot", "Main Movie + Textless Element"}; //NOI18N
    private String[] strContentTypeEx = {"", "Credits", "Alternative Credits", "Start Credits",  //NOI18N
                                         "Closing Credits", "Texted Element", "Textless Element", //NOI18N
                                         "Inserts", "Repair Element", "Copyright", //NOI18N
                                         "Subtitles", "Graphics", "Logo", "Effects", //NOI18N
                                         "Splitted Content"}; //NOI18N
    // Sprachabkürzungen nach ISO 639-1 http://www.sil.org/iso639-3/codes.asp
    private String[] strSprachen = {"","none","multilingual","aa | Afar","ab | Abkhazian","af | Afrikaans", //NOI18N
                                    "ak | Akan","am | Amharic","ar | Arabic","ar/fr | Arabic / French","an | Aragonese", //NOI18N
                                    "as | Assamese","av | Avaric","ae | Avestan", //NOI18N
                                    "ay | Aymara","az | Azerbaijani","ba | Bashkir", //NOI18N
                                    "bm | Bambara","be | Belarusian","bn | Bengali", //NOI18N
                                    "bi | Bislama","bo | Tibetan","bs | Bosnian", //NOI18N
                                    "br | Breton","bg | Bulgarian","ca | Catalan", //NOI18N
                                    "cs | Czech","ch | Chamorro","ce | Chechen", //NOI18N
                                    "cu | Church Slavic","cv | Chuvash","kw | Cornish", //NOI18N
                                    "co | Corsican","cr | Cree","cy | Welsh","da | Danish", //NOI18N
                                    "de | German", "de / da | German / Danish","de / en | German / English", //NOI18N
                                    "de / fr | German / French","de / it | German / Italian", //NOI18N
                                    "de / es | German / Spanish", "de / no | German / Norwegian", //NOI18N
                                    "de / en / fr | German / English / French", //NOI18N
                                    "de / en / ru | German / English / Russian","dv | Dhivehi","dz | Dzongkha", //NOI18N
                                    "el | Modern Greek","en | English", //NOI18N
                                    "en / es | English / Spanish","en / fr | English / French","en / nl | English / Dutch", //NOI18N
                                    "en / zh | English / Chinese","en / th | English / Thai","en / sh | English / Serbo-Croatian", //NOI18N
                                    "eo | Esperanto", //NOI18N
                                    "et | Estonian","eu | Basque","ee | Ewe","fo | Faroese", //NOI18N
                                    "fa | Persian","fj | Fijian","fi | Finnish", //NOI18N
                                    "fr | French","fy | Western Frisian","ff | Fulah", //NOI18N
                                    "gd | Scottish Gaelic","ga | Irish","gl | Galician", //NOI18N
                                    "gv | Manx","gn | Guarani","gu | Gujarati", //NOI18N
                                    "ht | Haitian","ha | Hausa","sh | Serbo-Croatian", //NOI18N
                                    "he | Hebrew","hz | Herero","hi | Hindi","ho | Hiri Motu", //NOI18N
                                    "hr | Croatian","hu | Hungarian","hy | Armenian", //NOI18N
                                    "ig | Igbo","io | Ido","ii | Sichuan Yi","iu | Inuktitut", //NOI18N
                                    "ie | Interlingue","ia | Interlingua","id | Indonesian", //NOI18N
                                    "ik | Inupiaq","is | Icelandic","it | Italian", //NOI18N
                                    "jv | Javanese","ja | Japanese","kl | Kalaallisut", //NOI18N
                                    "kn | Kannada","ks | Kashmiri","ka | Georgian", //NOI18N
                                    "kr | Kanuri","kk | Kazakh","km | Central Khmer", //NOI18N
                                    "ki | Kikuyu","rw | Kinyarwanda","ky | Kirghiz", //NOI18N
                                    "kv | Komi","kg | Kongo","ko | Korean","kj | Kuanyama", //NOI18N
                                    "ku | Kurdish","lo | Lao","la | Latin","lv | Latvian", //NOI18N
                                    "li | Limburgan","ln | Lingala","lt | Lithuanian", //NOI18N
                                    "lb | Luxembourgish","lu | Luba-Katanga","lg | Ganda", //NOI18N
                                    "mh | Marshallese","ml | Malayalam","mr | Marathi", //NOI18N
                                    "mk | Macedonian","mg | Malagasy","mt | Maltese", //NOI18N
                                    "mn | Mongolian","mi | Maori","ms | Malay","my | Burmese", //NOI18N
                                    "na | Nauru","nv | Navajo","nr | South Ndebele", //NOI18N
                                    "nd | North Ndebele","ng | Ndonga","ne | Nepali", //NOI18N
                                    "nl | Dutch","nn | Norwegian Nynorsk","nb | Norwegian Bokm†l", //NOI18N
                                    "no | Norwegian","ny | Nyanja","oc | Occitan (post 1500)", //NOI18N
                                    "oj | Ojibwa","or | Oriya","om | Oromo","os | Ossetian", //NOI18N
                                    "pa | Panjabi","pi | Pali","pl | Polish","pt | Portuguese", //NOI18N
                                    "ps | Pushto","qu | Quechua","rm | Romansh","ro | Romanian", //NOI18N
                                    "rn | Rundi","ru | Russian", //NOI18N
                                    "ru / fr | Russian / French","sh / en | Serbo-Croatian / English", //NOI18N
                                    "sg | Sango","sa | Sanskrit", //NOI18N
                                    "si | Sinhala","sk | Slovak","sl | Slovenian","se | Northern Sami", //NOI18N
                                    "sm | Samoan","sn | Shona","sd | Sindhi","so | Somali","st | Southern Sotho", //NOI18N
                                    "es | Spanish","sq | Albanian","sc | Sardinian", //NOI18N
                                    "sr | Serbian","ss | Swati","su | Sundanese","sw | Swahili", //NOI18N
                                    "sv | Swedish","ty | Tahitian","ta | Tamil","tt | Tatar", //NOI18N
                                    "te | Telugu","tg | Tajik","tl | Tagalog","th | Thai", //NOI18N
                                    "ti | Tigrinya","to | Tonga","tn | Tswana","ts | Tsonga", //NOI18N
                                    "tk | Turkmen","tr | Turkish","tw | Twi","ug | Uighur", //NOI18N
                                    "uk | Ukrainian","ur | Urdu","uz | Uzbek","ve | Venda", //NOI18N
                                    "vi | Vietnamese","vo | Volapk","wa | Walloon","wo | Wolof", //NOI18N
                                    "xh | Xhosa","yi | Yiddish","yo | Yoruba","za | Zhuang", //NOI18N
                                    "zh | Chinese","zu | Zulu"}; //NOI18N
    private String[] strPresetResolution = {"","1920x1080","1280x720","720x576","720x480"}; //NOI18N
    private String[] strScanType = {"","interlaced","progressive","progressive segmented frames"};
    // Feldvorgaben Ende ///////////////////////////////////////////////////////////////////
    
    public MainJFrame() {
        initComponents();
        initMyComponents();
        
        Meldung("Programm ist bereit.",0);
        Meldung("Erkannte Programmsprache: " + Locale.getDefault(),0);
        // preset Videoauflösung
        jComboBox15.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox15ItemStateChanged(evt);
            }
        });
        
        jComboBox10.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox10ItemStateChanged(evt);
            }
        });
        
        checkObjAllgemein.clear();
        checkObjAllgemein.add(new Integer(0));
        checkObjAllgemein.add("Allgemein");
        checkObjAllgemein.add(jComboBox1);
        //checkObjAllgemein.add(jComboBox2);
        
        // Audioseite
        checkObjAudio.clear();
        checkObjAudio.add(new Integer(1));
        checkObjAudio.add("Audio");
        checkObjAudio.add(jComboBox3);
        checkObjAudio.add(jComboBox4);
        checkObjAudio.add(jComboBox5);
        
        //Videoseite
        checkObjVideo.clear();
        checkObjVideo.add(new Integer(2));
        checkObjVideo.add("Video");
        checkObjVideo.add(jComboBox6);
        checkObjVideo.add(jComboBox7);
        checkObjVideo.add(jComboBox8);
        checkObjVideo.add(jComboBox9);
        checkObjVideo.add(jComboBox16);
        checkObjVideo.add(jComboBox10);
        checkObjVideo.add(jTextField4);
        checkObjVideo.add(jTextField5);
        
        //Untertitelsprachen Index 11-14
        checkObjVideoUT.clear();
        checkObjVideoUT.add(new Integer(3));
        checkObjVideoUT.add("Video");
        checkObjVideoUT.add(jComboBox11);
        checkObjVideoUT.add(jComboBox12);
        checkObjVideoUT.add(jComboBox13);
        checkObjVideoUT.add(jComboBox14);
        
        defaultBackgroundColor = this.jPanel2.getBackground();
        
    }

    private void initMyComponents()
    {
        this.jTextPane3.setEditable(false);
        this.jLabel1.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("VERZEICHNIS:"));
        this.jLabel3.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("KUNDEN-KOTRS:"));
        this.jLabel4.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("DETAILS:"));
        this.jLabel5.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("INHALTSTYP:"));
        this.jLabel6.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("INHALTSTYPERWEITERUNG:"));
        
        //Audioseite 
        this.jTabbedPane1.setTitleAt(2,java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("AUDIOTAB"));        
        this.jLabel2.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("TYPE:"));
        this.jLabel7.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("SPRACHE:"));
        this.jLabel8.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("SPEAKER LAYOUT:"));
        this.jLabel9.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("AUDIO"));
        
        //Videoseite
        this.jTabbedPane1.setTitleAt(1,java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("VIDEOTAB"));
        this.jLabel10.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("VIDEO"));
        this.jLabel11.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("BREITE X HÖHE:"));
        this.jLabel12.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("X"));
        this.jLabel13.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("FRAMERATE:"));
        this.jLabel14.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("ASPECT RATIO:"));
        this.jLabel15.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("CHROMA SAMPLING:"));
        this.jLabel16.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("COLOR TYP:"));
        this.jLabel17.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("TEXTED / TEXTLESS:"));
        this.jLabel18.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("CREDITS:"));
        this.jLabel19.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("PROGRAMM:"));
        this.jLabel20.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("UNTERTITEL:"));
        this.jLabel21.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("INSERTS:"));
        this.jLabel22.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("SCANTYPE"));
        this.jTextField4.setText("");
        this.jTextField5.setText("");
        
        //Allgemeine Seite
        this.jTabbedPane1.setTitleAt(0,java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("ALLGEMEINTAB"));
        this.jButton1.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("SPEICHERN"));
        this.jButton2.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("BEENDEN"));               
        this.jButton4.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("ÖFFNEN"));
        this.jButton3.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("NEU"));
        this.jMenu1.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("DATEI"));
        this.jCheckBox1.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("VIDEO"));
        this.jCheckBox2.setText(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("AUDIO"));
        this.jTabbedPane1.setEnabledAt(1, false);
        this.jTabbedPane1.setEnabledAt(2, false);
        this.jTabbedPane1.setEnabledAt(3, false);
        this.jTabbedPane1.setEnabledAt(4, false);

        this.jTabbedPane1.setTitleAt(3,"MD5"); 
        this.jTabbedPane1.setTitleAt(4,java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("XMLTAB"));

        
        Action exitAction = new AbstractAction( java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("BEENDEN") ) {
            @Override
            public void actionPerformed( ActionEvent e ) {
            System.exit( 0 ); }};
        this.jMenu1.add(exitAction);

        this.jTextField1.setText("");
        this.jTextField2.setText("");
        this.jTextField3.setText("");
        this.setTitle(strFenstertitel);       
        
        // Fenstericon setzen
        this.setDefaultLookAndFeelDecorated(false);
        URL url = this.getClass().getResource("logo.jpg");
        this.setIconImage(new ImageIcon(url).getImage());
     
        ComboboxFüllen(this.jComboBox1, strContentType);
        ComboboxFüllen(this.jComboBox2, strContentTypeEx);
        ComboboxFüllen(this.jComboBox3, strAudioType);
        ComboboxFüllen(this.jComboBox4, strSprachen);
        ComboboxFüllen(this.jComboBox5, strSpeakerLayout);
        ComboboxFüllen(this.jComboBox6, strFramerate);
        ComboboxFüllen(this.jComboBox7, strAspectRatio);
        ComboboxFüllen(this.jComboBox8, strChromaSampling);
        ComboboxFüllen(this.jComboBox9, strColorType);
        ComboboxFüllen(this.jComboBox10, strTexted);
        ComboboxFüllen(this.jComboBox11, strSprachen);
        ComboboxFüllen(this.jComboBox12, strSprachen);
        ComboboxFüllen(this.jComboBox13, strSprachen);
        ComboboxFüllen(this.jComboBox14, strSprachen);
        ComboboxFüllen(this.jComboBox15, strPresetResolution);
        ComboboxFüllen(this.jComboBox16, strScanType);        
        
        //START // Drag-and-Drop mit jTextField1 verwenden
        DropTarget dt = new DropTarget();
        dt.setComponent(jTextField1);
        try
        {
            dt.addDropTargetListener(new DropTargetListener() {
                public void dragEnter(DropTargetDragEvent dEvent) {
                }
                public void dragOver(DropTargetDragEvent dEvent) {
                }
                public void dragExit(DropTargetEvent dEvent) {
                }
                public void dropActionChanged(DropTargetDragEvent dEvent) {
                }
                public void drop(DropTargetDropEvent dEvent) {
                    if ((dEvent.getSourceActions() & DnDConstants.ACTION_COPY) == 0)
                    {
                        dEvent.rejectDrop();
                    }
                    else
                    {
                        dEvent.acceptDrop(DnDConstants.ACTION_COPY);
                        Transferable trans = dEvent.getTransferable();
                        DataFlavor [] currentFlavors = trans.getTransferDataFlavors();
                        DataFlavor selectedFlavor = null;
                        int i =0;
                        //for (int i = 0; i < currentFlavors.length; i++)
                        //{
                            if (DataFlavor.javaFileListFlavor.equals(currentFlavors[i]))
                            {
                                selectedFlavor = currentFlavors[i];
                                //break;
                            }
                        //}
                        if (selectedFlavor != null)
                        {
                            try
                            {
                                resetAllInput();
                                Iterator dateien = ((java.util.List)trans.getTransferData(selectedFlavor)).iterator();
                                if (dateien.hasNext())
                                {
                                    File f = (File)dateien.next();
                                    if (f.isDirectory()){
                                        jTextField1.setText(f.getPath());
                                        strVerzeichnis = f.getPath();
                                    } else
                                    { // für den Fall das eine Datei ausgewählt wurde
                                        jTextField1.setText(getFoldername(f.getPath()));
                                        strVerzeichnis = getFoldername(f.getPath());
                                        f = new File(getFoldername(f.getPath()));
                                    }
                                    // Nur Dateien in Liste aufnehmen
                                    fileDateien = f.listFiles(new FileFilter() {
                                    @Override public boolean accept( File d ) {
                                    return d.isFile();
                                    } } );
                                }
                                // strDateiCheckSum herausfiltern
                                Vector tempList = new Vector();
                                tempList.clear();
                                for(int x = 1; x<fileDateien.length;x++)
                                {
                                    if (!fileDateien[x].getName().equals(strDateiCheckSum))
                                    { tempList.add(fileDateien[x]);}
                                }
                                tempList.trimToSize();
                                fileDateien = (File[]) tempList.toArray(new File[tempList.size()]);
                                //Meldung(">>>> "+ auswahl.getPath()+ File.separator + strDateiCheckSum,1);
                                fileDateiCheckSum = new File(strVerzeichnis+ File.separator + strDateiCheckSum);
                                if (fileDateiCheckSum.exists()) 
                                {
                                    Meldung("Datei " + strDateiCheckSum + " wurde gefunden.",0);
                                } //fileDateiCheckSum
                                
                                showDateinamen(fileDateiCheckSum);
                            }
                            catch (Exception ex)
                            {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
                }
            });
        }
        catch (TooManyListenersException ex)
        {
            System.out.println(ex.getMessage());
        }
        //ENDE // Drag-and-Drop mit jTextField1 verwenden
        
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jComboBox15 = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        jComboBox7 = new javax.swing.JComboBox();
        jComboBox8 = new javax.swing.JComboBox();
        jComboBox9 = new javax.swing.JComboBox();
        jComboBox10 = new javax.swing.JComboBox();
        jPanel9 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jComboBox11 = new javax.swing.JComboBox();
        jComboBox12 = new javax.swing.JComboBox();
        jComboBox13 = new javax.swing.JComboBox();
        jComboBox14 = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        jComboBox16 = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel13 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane3 = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 603, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jTextPane1);

        jScrollPane5.setViewportView(jTextPane2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(632, 662));
        setMinimumSize(new java.awt.Dimension(632, 662));

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton2)
                .addGap(162, 162, 162)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 181, Short.MAX_VALUE)
                .addComponent(jButton1))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3)))
        );

        jTabbedPane1.setMaximumSize(new java.awt.Dimension(631, 467));

        jPanel3.setMaximumSize(new java.awt.Dimension(626, 437));
        jPanel3.setMinimumSize(new java.awt.Dimension(626, 437));

        jButton4.setText("jButton4");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Verzeichnisinhalt:"));

        jTextArea2.setColumns(20);
        jTextArea2.setEditable(false);
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(5);
        jTextArea2.setMaximumSize(new java.awt.Dimension(164, 94));
        jTextArea2.setMinimumSize(new java.awt.Dimension(164, 94));
        jScrollPane2.setViewportView(jTextArea2);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        jLabel1.setText("jLabel1");

        jTextField1.setEditable(false);
        jTextField1.setText("jTextField1");

        jLabel3.setText("jLabel3");

        jLabel4.setText("jLabel4");

        jLabel5.setText("jLabel5");

        jLabel6.setText("jLabel6");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Material enthält:"));
        jPanel2.setMinimumSize(new java.awt.Dimension(593, 59));

        jCheckBox1.setText("jCheckBox1");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("jCheckBox2");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCheckBox1)
                .addGap(117, 117, 117)
                .addComponent(jCheckBox2)
                .addGap(112, 112, 112))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jCheckBox1)
                .addComponent(jCheckBox2))
        );

        jTextField2.setText("jTextField2");

        jTextField3.setText("jTextField3");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addGap(39, 39, 39))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
                                .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, 420, Short.MAX_VALUE)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Allgemein", jPanel3);

        jLabel10.setFont(new java.awt.Font("Tahoma", 3, 36)); // NOI18N
        jLabel10.setText("jLabel10");

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Auflösung:"));

        jLabel11.setText("jLabel11");

        jTextField4.setText("jTextField4");

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("jLabel12");

        jTextField5.setText("jTextField5");

        jComboBox15.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jComboBox15, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jLabel13.setText("jLabel13");

        jLabel14.setText("jLabel14");

        jLabel15.setText("jLabel15");

        jLabel16.setText("jLabel16");

        jLabel17.setText("jLabel17");

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Sprachen:"));

        jLabel18.setText("jLabel18");

        jLabel19.setText("jLabel19");

        jLabel20.setText("jLabel20");

        jLabel21.setText("jLabel21");

        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox13.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox14.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(0, 22, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(68, 68, 68))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel20)
                    .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel21)
                    .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel22.setText("jLabel22");

        jComboBox16.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel22)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox16, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel13)
                                        .addComponent(jLabel14)
                                        .addComponent(jLabel15)
                                        .addComponent(jLabel16)
                                        .addComponent(jLabel17))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 19, Short.MAX_VALUE))))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jComboBox16, jComboBox9});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jComboBox16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Video", jPanel5);

        jLabel2.setText("jLabel2");

        jLabel7.setText("jLabel7");

        jLabel8.setText("jLabel8");

        jLabel9.setFont(new java.awt.Font("Tahoma", 3, 36)); // NOI18N
        jLabel9.setText("jLabel9");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.Alignment.TRAILING, 0, 465, Short.MAX_VALUE)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(182, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Audio", jPanel7);

        jScrollPane7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanel13.setPreferredSize(new java.awt.Dimension(560, 600));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        jScrollPane7.setViewportView(jPanel13);

        jTabbedPane1.addTab("tab5", jScrollPane7);

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane4.setViewportView(jTextArea3);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab4", jPanel10);

        jTextPane3.setAlignmentX(0.1F);
        jTextPane3.setAlignmentY(0.1F);
        jScrollPane3.setViewportView(jTextPane3);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(8, 8, 8))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        System.exit(0);       
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (!strVerzeichnis.isEmpty()){
                if(checkFillStatusOK()) {
                    writeXML(strVerzeichnis + File.separator + getVerzeichnisName(strVerzeichnis) + "." + strDateiExt);
                }
                else {
                    Meldung("Die Rot unterlegten Felder müssen ausgefüllt sein.",1);
                }
            }
            else 
            Meldung("Es wurden keine Dateien ausgewählt.",1);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if(jCheckBox1.isSelected()) this.jTabbedPane1.setEnabledAt(1, true);
        else this.jTabbedPane1.setEnabledAt(1, false);
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        if(jCheckBox2.isSelected()) this.jTabbedPane1.setEnabledAt(2, true);
        else this.jTabbedPane1.setEnabledAt(2, false);
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        resetAllInput();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        resetAllInput();
        Vector tempList = new Vector();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(java.util.ResourceBundle.getBundle("materialcard/MyResource").getString("DIALOGTITLE.0"));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if ( fileChooser.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION )
        {
            File auswahl = fileChooser.getSelectedFile();
            if (auswahl.isDirectory()){
                jTextField1.setText(auswahl.getPath());
                strVerzeichnis = auswahl.getPath();
            } else
            { // für den Fall das eine Datei ausgewählt wurde
                jTextField1.setText(getFoldername(auswahl.getPath()));
                strVerzeichnis = getFoldername(auswahl.getPath());
                auswahl = new File(getFoldername(auswahl.getPath()));
                }

            
            // Nur Dateien in Liste aufnehmen
            fileDateien = auswahl.listFiles(new FileFilter() {
            @Override public boolean accept( File d ) {
                return d.isFile();
                } } );
            
            // strDateiCheckSum herausfiltern
            tempList.clear();
            for(int i = 1; i<fileDateien.length;i++)
            {
                if (!fileDateien[i].getName().equals(strDateiCheckSum))
                { tempList.add(fileDateien[i]);}
            }
            tempList.trimToSize();
            fileDateien = (File[]) tempList.toArray(new File[tempList.size()]);
            //Meldung(">>>> "+ auswahl.getPath()+ File.separator + strDateiCheckSum,1);
            fileDateiCheckSum = new File(strVerzeichnis+ File.separator + strDateiCheckSum);
            if (fileDateiCheckSum.exists()) 
            {
                Meldung("Datei " + strDateiCheckSum + " wurde gefunden.",0);
            } //fileDateiCheckSum

            showDateinamen(fileDateiCheckSum);

        }

    }//GEN-LAST:event_jButton4ActionPerformed

    // preset Videoauflösung
    private void jComboBox15ItemStateChanged(java.awt.event.ItemEvent evt) {
        String[] temp = jComboBox15.getSelectedItem().toString().split("x");
        this.jTextField4.setText(temp[0]);
        this.jTextField5.setText(temp[1]);
    }
    
    // preset Texted / Textless
    private void jComboBox10ItemStateChanged(java.awt.event.ItemEvent evt) {
        if (jComboBox10.getSelectedItem().toString().equals(strTexted[strTexted.length-1])){
            this.jComboBox11.setSelectedItem("none");
            this.jComboBox12.setSelectedItem("none");
            this.jComboBox13.setSelectedItem("none");
            this.jComboBox14.setSelectedItem("none");
            this.jComboBox11.setEnabled(false);
            this.jComboBox12.setEnabled(false);
            this.jComboBox13.setEnabled(false);
            this.jComboBox14.setEnabled(false);
        } else {
            this.jComboBox11.setEnabled(true);
            this.jComboBox12.setEnabled(true);
            this.jComboBox13.setEnabled(true);
            this.jComboBox14.setEnabled(true);
            this.jComboBox11.setSelectedItem("");
            this.jComboBox12.setSelectedItem("");
            this.jComboBox13.setSelectedItem("");
            this.jComboBox14.setSelectedItem(""); 
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        final MainJFrame cf = new MainJFrame();
        cf.setResizable(false);
        
        // Schließen-Button des Frames beendet Programm
        cf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
            System.exit(0);
            }
        });
        
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    
    // Halbe Bildschirmabmessung ergibt Bildschirmmittelpunkt  
    int xCenter = dim.width/2;
    int yCenter = dim.height/2;
    // Die Platzierung von Fenstern orientiert sich an der linken
    // oberen Ecke. Zur Korrektur muss somit die halbe     
    // Komponentenabmessung mit eingerechnet werden.
    int xDiff = cf.getWidth()/2;
    int yDiff = cf.getHeight()/2;
    int xCalculated = xCenter-xDiff;
    int yCalculated = yCenter-yDiff;
    // setLocation() legt den Ort der Komponente fest  
    cf.setLocation(xCalculated,yCalculated);    
            java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                cf.setVisible(true);
            }
        });

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox10;
    private javax.swing.JComboBox jComboBox11;
    private javax.swing.JComboBox jComboBox12;
    private javax.swing.JComboBox jComboBox13;
    private javax.swing.JComboBox jComboBox14;
    private javax.swing.JComboBox jComboBox15;
    private javax.swing.JComboBox jComboBox16;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JComboBox jComboBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JTextPane jTextPane3;
    // End of variables declaration//GEN-END:variables

    // schneidet dateinamen ab
private String getFoldername(String inFolder)
{
    File file = new File(inFolder);
    String dateiname = file.getName();
    int pos = inFolder.indexOf(dateiname,0);
    if (pos > 0)
        return new String(inFolder.substring(0,pos-1));
    else
        return new String();
}

private void showDateinamen(File checksumFile)
{
    jTextArea2.setText("");
    if(fileDateien.length >0)
    {
        for (int i=0; i< fileDateien.length;i++)
        {
            this.jTextArea2.append( fileDateien[i].getName() + System.lineSeparator());
        }
    }
    Meldung("Es wurden " + fileDateien.length + " Datei(en) im Verzeichnis \"" + strVerzeichnis + "\" gefunden.",0);
    generateMD5Felder(checksumFile);
}

private void generateMD5Felder(File checksumFile)
{

    this.jTabbedPane1.setEnabledAt(3, true);
    int posY = 10;
    JLabel label = null;
    JTextField txtfeld = null;
    File f = null;
    genTXT.clear();
    genLabel.clear();
    
       if(fileDateien.length >0)
       {
        for (int i=0; i< fileDateien.length;i++)
        {
            f = (File) fileDateien[i];
            label = new JLabel();
           // label.setBounds(20,posY, 300, 300);
            label.setLocation(10, posY);
            label.setSize(300, 30);
           // label.setBackground(Color.red);
            label.setText(f.getName());
            label.setVisible(true);
            jPanel13.add(label);
            genLabel.add(label);
                                
            txtfeld = new JTextField();
            txtfeld.setSize(200,30);
            txtfeld.setLocation(330, posY);
            txtfeld.setVisible(true);
            txtfeld.setText("");
            //jScrollPane7.add(txtfeld);
            jPanel13.add(txtfeld);
            genTXT.add(txtfeld);
            
            posY = posY + 32;
            
        }
        // Größe des jPanel berechnen
        jPanel13.setSize(700,  posY +40);
        jPanel13.revalidate();
        jScrollPane7.revalidate();
        
       }
       if (checksumFile != null)
       {
           //Meldung("MD5 füllen",1);
           readCheckSumFile(checksumFile);
       }
}


// Problem: Leerzeilen darf die Checksum-Datei nicht enthalten
private void readCheckSumFile(File checksumFile)
{
    BufferedReader in = null;
    
    
    Vector zeile = new Vector();
    String[] spalte = new String[2];
    if (checksumFile.exists())
    {
        try {
        in = new BufferedReader(new InputStreamReader(new FileInputStream(checksumFile)));
            String line;
            zeile.clear();
            
            while((line = in.readLine()) != null) 
            {
                String[] t = SplitString(line);
                if (t == null) 
                {
                    Meldung( strDateiCheckSum + " hat nicht das korrekte Format.",1);
                    break;
                }
                
                if (genLabel.size()>0)
                {
                    for(int y=0;y<genLabel.size();y++)
                    {
                        JLabel temp = (JLabel) genLabel.get(y);
                        JTextField tempJTextField = (JTextField) genTXT.get(y);
                        if(temp.getText().equals(cutFirstChar(t[1])) ) 
                        {
                            tempJTextField.setText(t[0]);
                            //Meldung("Übereinstimmung  > " + temp.getText() + " mit "  + cutFirstChar(t[1]), 0);
                            break;
                        } else 
                        {
                           // Meldung("Keine Übereinstimmung  > " + temp.getText() + " mit "  + cutFirstChar(t[1]), 1);
                        }
                    }
                }
            }
            in.close();
        } 
        catch (IOException e)
        {
            Meldung(e.getMessage(),1);
        }
 
     } else {
        Meldung("Datei " + strDateiCheckSum + " nicht gefunden.",1);
    }
}

// Trennung von Checksumme und Dateinamen aus Datei  strDateiCheckSum
private String[] SplitString(String line)
{
    int lastPOS = line.indexOf(strTrennZeichenCheckSum);
    if (lastPOS == 0) return null;
    
    int laenge = line.length();
    String s = new String(line);
    String[] back = new String[2];
    
    back[0]= line.substring(0,lastPOS);
    back[1]=s.substring(lastPOS+1 ,laenge);
    return back;
}


// Schneidet Stern vom Dateinamen ab 
private String cutFirstChar(String in)
{
    String s = in.substring(1);
    return s;
}

// gibt nur Verzeichnisnamen zurück  c:\hallo\test\ -> test
private String getVerzeichnisName(String inPfad)
{
    String[] temp = null;
    if(inPfad.lastIndexOf(File.separator) > 0){
        temp = inPfad.split(Pattern.quote(File.separator));
        return temp[temp.length - 1];
    } else return null;
}

private void Meldung (String mldg, int Status)
{
        StyleContext sc = StyleContext.getDefaultStyleContext();
        Color c = null;
        String decString = null;
        switch(Status)
        {
        case 1:
                    c= defaultErrorColor; decString = "Error: ";
        break;
        case 2:
                    c= defaultWarningColor;decString = "Warning: ";
        break;
        default:
                    c = defaultNormalColor; decString = "Info: ";
        }
        try{
                AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,StyleConstants.Foreground, c);
              int lastPOS = jTextPane3.getDocument().getLength();
              this.jTextPane3.getStyledDocument().insertString(lastPOS, decString + mldg + System.lineSeparator() , aset);            
        } catch(BadLocationException exc) {} 
}

private void ComboboxFüllen(JComboBox o, String[] s)
{
    o.removeAllItems();
    for(int i=0; i< s.length ;i++)
    {
        o.addItem(s[i].toString());
    }
}

    private void writeXML(String filename){
        
        
        File fileName = new File(filename);
        JTextField t = null;
        if (fileName.exists()) {
            Meldung("Datei " + filename + " ist bereits vorhanden. Diese wird ersetzt.",2);
            if(!fileName.delete()) Meldung("Datei " + filename + " kann durch dieses Programm nicht ersetzt werden.",1);
        }
        writeTABXML();
        FileWriter writer = null;
        try{
                writer = new FileWriter(fileName ,true);
                writer.write(xmlDeclaration+ System.lineSeparator());
                writer.write("<digim_file xmlns:fmrs=\"http://www.filemaker.com/fmpdsoresult\">" + System.lineSeparator());
                writer.write("<editorial>" + System.lineSeparator());
                writer.write("<digim_cost_tracking_number/>" + System.lineSeparator());
                writer.write("<customer_cost_tracking_number>" + System.lineSeparator());
                    writer.write(this.jTextField2.getText());
                writer.write("</customer_cost_tracking_number>" + System.lineSeparator());
                writer.write("<program_number_digim>" + System.lineSeparator());
                writer.write("</program_number_digim>" + System.lineSeparator());
                writer.write("<title>" + System.lineSeparator());
                writer.write("</title>" + System.lineSeparator());
                writer.write("<title_aka>" + System.lineSeparator());
                writer.write("</title_aka>" + System.lineSeparator());
                writer.write("<content_type>" + System.lineSeparator());
                    writer.write(this.jComboBox1.getSelectedItem().toString());
                writer.write("</content_type>" + System.lineSeparator());
                writer.write("<content_type_extension>");
                    writer.write(this.jComboBox2.getSelectedItem().toString());
                writer.write("</content_type_extension>" + System.lineSeparator());
                writer.write("<details>" + System.lineSeparator());
                    writer.write(this.jTextField3.getText());
                writer.write("</details>" + System.lineSeparator());
                writer.write("<kversion/>" + System.lineSeparator());
                writer.write("<status>");
                writer.write("</status>" + System.lineSeparator());
                writer.write("<Lead-ID/>" + System.lineSeparator());
                writer.write("<owner/>" + System.lineSeparator());                
                writer.write("</editorial>" + System.lineSeparator());
                writer.write("<general>" + System.lineSeparator());
                writer.write("<dateien>"+ System.lineSeparator());
                if(fileDateien.length>0)
                {
                    for(int i=0; i< fileDateien.length; i++)
                    {
                        t = (JTextField) genTXT.get(i);
                        writer.write("  <datei>"+ System.lineSeparator());
                        writer.write("      <dateiname>" + fileDateien[i].getName() + "</dateiname>" + System.lineSeparator());
                        writer.write("      <dateiMD5>" + t.getText() + "</dateiMD5>" + System.lineSeparator());                        
                        writer.write("  </datei>"+ System.lineSeparator());

                    }
                }
                writer.write("</dateien>" + System.lineSeparator());
                writer.write("<extension>");
                writer.write("</extension>" + System.lineSeparator());
                writer.write("<container>");
                writer.write("</container>" + System.lineSeparator());
                writer.write("<duration>" + System.lineSeparator());
                writer.write("</duration>" + System.lineSeparator());
                writer.write("<size>");
                writer.write("</size>" + System.lineSeparator());
                writer.write("<folder>");
                writer.write("</folder>" + System.lineSeparator());
                writer.write("<md5>" + System.lineSeparator());
                writer.write("</md5>" + System.lineSeparator());
                writer.write("</general>" + System.lineSeparator());

                //Video Section
                writer.write("<video>" + System.lineSeparator());
                writer.write("<codec/>" + System.lineSeparator());
                writer.write("<resolution_width>");
                    writer.write(this.jTextField4.getText());
                writer.write("</resolution_width>" + System.lineSeparator());
                writer.write("<resolution_heigth>");
                    writer.write(this.jTextField5.getText());                
                writer.write("</resolution_heigth>" + System.lineSeparator());
                writer.write("<frame_rate>" + System.lineSeparator());
                    writer.write(this.jComboBox6.getSelectedItem().toString());
                writer.write("</frame_rate>" + System.lineSeparator());
                writer.write("<bit_rate_type/>" + System.lineSeparator());
                writer.write("<bit_rate_size_average/>" + System.lineSeparator());
                writer.write("<aspect_ratio>");
                    writer.write(this.jComboBox7.getSelectedItem().toString());
                writer.write("</aspect_ratio>" + System.lineSeparator());
                writer.write("<chroma_sampling>" + System.lineSeparator());                    
                    writer.write(this.jComboBox8.getSelectedItem().toString());                
                writer.write("</chroma_sampling>" + System.lineSeparator());
                writer.write("<color_type>");
                    writer.write(this.jComboBox9.getSelectedItem().toString());                
                writer.write("</color_type>" + System.lineSeparator());
                writer.write("<scan_type>");
                    writer.write(this.jComboBox16.getSelectedItem().toString());                
                writer.write("</scan_type>" + System.lineSeparator());                
                writer.write("<texted_textless>");
                    writer.write(this.jComboBox10.getSelectedItem().toString());                
                writer.write("</texted_textless>" + System.lineSeparator());
                writer.write("<credits_language>" + System.lineSeparator());
                    writer.write(extractLangCode(this.jComboBox11.getSelectedItem().toString()," | "));                                
                writer.write("</credits_language>" + System.lineSeparator());                
                writer.write("<title_language>" + System.lineSeparator());
                    writer.write(extractLangCode(this.jComboBox12.getSelectedItem().toString()," | "));
                writer.write("</title_language>" + System.lineSeparator());
                writer.write("<subtitles_language>" + System.lineSeparator());
                   writer.write(extractLangCode(this.jComboBox13.getSelectedItem().toString()," | "));                
                writer.write("</subtitles_language>" + System.lineSeparator());
                writer.write("<text_inserts_language>" + System.lineSeparator());
                   writer.write(extractLangCode(this.jComboBox14.getSelectedItem().toString()," | "));                
                writer.write("</text_inserts_language>" + System.lineSeparator());
                writer.write("<source_video/>" + System.lineSeparator());
                writer.write("</video>" + System.lineSeparator());

                //Audio section
                writer.write("<audio>" + System.lineSeparator());
                writer.write("<ContainerCodec_Audio/>" + System.lineSeparator());
                writer.write("<Samplerate/>" + System.lineSeparator());
                writer.write("<Bitrate/>" + System.lineSeparator());
                writer.write("<video_reference/>" + System.lineSeparator());
                writer.write("<summary>" + System.lineSeparator());

                writer.write("<audio1>" + System.lineSeparator());
                writer.write("<channels>");
                writer.write("</channels>" + System.lineSeparator());
                writer.write("<type>");
                   writer.write(this.jComboBox3.getSelectedItem().toString());                
                writer.write("</type>" + System.lineSeparator());
                writer.write("<language>");
                   writer.write(extractLangCode(this.jComboBox4.getSelectedItem().toString()," | "));                
                writer.write("</language>" + System.lineSeparator());
                writer.write("<speaker_layout>");
                   writer.write(this.jComboBox5.getSelectedItem().toString());                
                writer.write("</speaker_layout>" + System.lineSeparator());
                writer.write("<source/>" + System.lineSeparator());
                writer.write("</audio1>" + System.lineSeparator());
                
                writer.write("  <audio2>" + System.lineSeparator());
                writer.write("<channels>");
                writer.write("</channels>" + System.lineSeparator());
                writer.write("<type>");
                writer.write("</type>" + System.lineSeparator());
                writer.write("<language>");
                writer.write("</language>" + System.lineSeparator());
                writer.write("<speaker_layout>");
                writer.write("</speaker_layout>" + System.lineSeparator());
                writer.write("<source/>" + System.lineSeparator());
                writer.write("</audio2>" + System.lineSeparator());

                writer.write("<audio3>" + System.lineSeparator());
                writer.write("<channels>");
                writer.write("</channels>" + System.lineSeparator());
                writer.write("<type>");
                writer.write("</type>" + System.lineSeparator());
                writer.write("<language>");
                writer.write("</language>" + System.lineSeparator());
                writer.write("<speaker_layout>");
                writer.write("</speaker_layout>" + System.lineSeparator());
                writer.write("<source/>" + System.lineSeparator());
                writer.write("</audio3>" + System.lineSeparator());

                writer.write("<audio4>" + System.lineSeparator());
                writer.write("<channels>");
                writer.write("</channels>" + System.lineSeparator());
                writer.write("<type>");
                writer.write("</type>" + System.lineSeparator());
                writer.write("<language>");
                writer.write("</language>" + System.lineSeparator());
                writer.write("<speaker_layout>");
                writer.write("</speaker_layout>" + System.lineSeparator());
                writer.write("<source/>" + System.lineSeparator());
                writer.write("</audio4>" + System.lineSeparator());
                
                writer.write("</summary>" + System.lineSeparator());
                writer.write("</audio>" + System.lineSeparator());

                writer.write("<data_carrier>" + System.lineSeparator());
                writer.write("<archive_number_digim>");
                writer.write("</archive_number_digim>" + System.lineSeparator());
                writer.write("<data_carrier_type/>" + System.lineSeparator());
                writer.write("</data_carrier>" + System.lineSeparator());
                
                writer.write("</digim_file>");
                writer.flush();
                writer.close();
                Meldung("Die Datei " + fileName.getName() + " wurde erfolgreich geschrieben.",0);
            }
            catch ( IOException e ) { 
                Meldung("Es ist folgender Fehler aufgetreten: " + e.getMessage(),1);
            } 
            finally { 
            if ( writer != null ) 
                try { writer.close(); } catch ( IOException e ) {
                Meldung("Es ist folgender Fehler aufgetreten: " + e.getMessage(),1);
                } 
            }    
        }

    private void writeTABXML()
    {
         JTextField t = null;
       this.jTabbedPane1.setEnabledAt(4, true);
       this.jTextArea3.setText("");
                jTextArea3.append(xmlDeclaration+ System.lineSeparator());
                jTextArea3.append("<digim_file xmlns:fmrs=\"http://www.filemaker.com/fmpdsoresult\">" + System.lineSeparator());
                jTextArea3.append("<editorial>" + System.lineSeparator());
                jTextArea3.append("     <digim_cost_tracking_number/>" + System.lineSeparator());
                jTextArea3.append("     <customer_cost_tracking_number>");
                    jTextArea3.append(this.jTextField2.getText());
                jTextArea3.append("</customer_cost_tracking_number>" + System.lineSeparator());
                jTextArea3.append("     <program_number_digim/>"+ System.lineSeparator());
                jTextArea3.append("     <title>");
                jTextArea3.append("</title>" + System.lineSeparator());
                jTextArea3.append("     <title_aka>");
                jTextArea3.append("</title_aka>" + System.lineSeparator());
                jTextArea3.append("     <content_type>");
                    jTextArea3.append(this.jComboBox1.getSelectedItem().toString());
                jTextArea3.append("</content_type>" + System.lineSeparator());
                jTextArea3.append("     <content_type_extension>");
                    jTextArea3.append(this.jComboBox2.getSelectedItem().toString());
                jTextArea3.append("</content_type_extension>" + System.lineSeparator());
                jTextArea3.append("     <details>");
                    jTextArea3.append(this.jTextField3.getText());
                jTextArea3.append("</details>" + System.lineSeparator());
                jTextArea3.append("     <kversion/>" + System.lineSeparator());
                jTextArea3.append("     <status/>"+ System.lineSeparator());
                jTextArea3.append("     <Lead-ID/>" + System.lineSeparator());
                jTextArea3.append("     <owner/>" + System.lineSeparator());                
                jTextArea3.append("</editorial>" + System.lineSeparator());
                jTextArea3.append("<general>" + System.lineSeparator());
                jTextArea3.append(" <dateien>"+ System.lineSeparator());
                if(fileDateien.length>0)
                {
                    for(int i=0; i< fileDateien.length; i++)
                    {
                        t = (JTextField) genTXT.get(i);
                        jTextArea3.append("  <datei>"+ System.lineSeparator());
                        jTextArea3.append("      <dateiname>" + fileDateien[i].getName() + "</dateiname>" + System.lineSeparator());
                        jTextArea3.append("      <dateiMD5>" + t.getText() + "</dateiMD5>" + System.lineSeparator());                        
                        jTextArea3.append("  </datei>"+ System.lineSeparator());

                        // jTextArea3.append(fileDateien[i].getName() + System.lineSeparator());
                    }
                }
                jTextArea3.append(" </dateien>" + System.lineSeparator());
                jTextArea3.append(" <extension/>"+ System.lineSeparator());
                jTextArea3.append(" <container/>"+ System.lineSeparator());
                jTextArea3.append(" <duration/>" + System.lineSeparator());
                jTextArea3.append(" <size/>"+ System.lineSeparator());
                jTextArea3.append(" <folder>");
                jTextArea3.append("</folder>" + System.lineSeparator());
                jTextArea3.append(" <md5/>" + System.lineSeparator());
                jTextArea3.append("</general>" + System.lineSeparator());

                //Video Section
                jTextArea3.append("<video>" + System.lineSeparator());
                jTextArea3.append("<codec/>" + System.lineSeparator());
                jTextArea3.append(" <resolution_width>");
                    jTextArea3.append(this.jTextField4.getText());
                jTextArea3.append("</resolution_width>" + System.lineSeparator());
                jTextArea3.append(" <resolution_heigth>");
                    jTextArea3.append(this.jTextField5.getText());                
                jTextArea3.append("</resolution_heigth>" + System.lineSeparator());
                jTextArea3.append(" <frame_rate>");
                    jTextArea3.append(this.jComboBox6.getSelectedItem().toString());
                jTextArea3.append("</frame_rate>" + System.lineSeparator());
                jTextArea3.append(" <bit_rate_type/>" + System.lineSeparator());
                jTextArea3.append(" <bit_rate_size_average/>" + System.lineSeparator());
                jTextArea3.append(" <aspect_ratio>");
                    jTextArea3.append(this.jComboBox7.getSelectedItem().toString());
                jTextArea3.append("</aspect_ratio>" + System.lineSeparator());
                jTextArea3.append(" <chroma_sampling>");                    
                    jTextArea3.append(this.jComboBox8.getSelectedItem().toString());                
                jTextArea3.append("</chroma_sampling>" + System.lineSeparator());
                jTextArea3.append(" <color_type>");
                    jTextArea3.append(this.jComboBox9.getSelectedItem().toString());                
                jTextArea3.append("</color_type>" + System.lineSeparator());
                jTextArea3.append(" <scan_type>");
                    jTextArea3.append(this.jComboBox16.getSelectedItem().toString());                
                jTextArea3.append("</scan_type>" + System.lineSeparator());
                jTextArea3.append(" <texted_textless>");
                    jTextArea3.append(this.jComboBox10.getSelectedItem().toString());                
                jTextArea3.append("</texted_textless>" + System.lineSeparator());
                jTextArea3.append(" <credits_language>");
                    jTextArea3.append(extractLangCode(this.jComboBox11.getSelectedItem().toString()," | "));                                
                jTextArea3.append("</credits_language>" + System.lineSeparator());                
                jTextArea3.append(" <title_language>");
                    jTextArea3.append(extractLangCode(this.jComboBox12.getSelectedItem().toString()," | "));
                jTextArea3.append("</title_language>" + System.lineSeparator());
                jTextArea3.append(" <subtitles_language>");
                   jTextArea3.append(extractLangCode(this.jComboBox13.getSelectedItem().toString()," | "));                
                jTextArea3.append("</subtitles_language>" + System.lineSeparator());
                jTextArea3.append(" <text_inserts_language>");
                   jTextArea3.append(extractLangCode(this.jComboBox14.getSelectedItem().toString()," | "));                
                jTextArea3.append("</text_inserts_language>" + System.lineSeparator());
                jTextArea3.append(" <source_video/>" + System.lineSeparator());
                jTextArea3.append("</video>" + System.lineSeparator());

                //Audio section
                jTextArea3.append("<audio>" + System.lineSeparator());
                jTextArea3.append(" <ContainerCodec_Audio/>" + System.lineSeparator());
                jTextArea3.append(" <Samplerate/>" + System.lineSeparator());
                jTextArea3.append(" <Bitrate/>" + System.lineSeparator());
                jTextArea3.append(" <video_reference/>" + System.lineSeparator());
                jTextArea3.append(" <summary>" + System.lineSeparator());

                jTextArea3.append("     <audio1>" + System.lineSeparator());
                jTextArea3.append("         <channels>");
                jTextArea3.append("</channels>" + System.lineSeparator());
                jTextArea3.append("         <type>");
                   jTextArea3.append(this.jComboBox3.getSelectedItem().toString());                
                jTextArea3.append("</type>" + System.lineSeparator());
                jTextArea3.append("         <language>");
                   jTextArea3.append(extractLangCode(this.jComboBox4.getSelectedItem().toString()," | "));                
                jTextArea3.append("</language>" + System.lineSeparator());
                jTextArea3.append("         <speaker_layout>");
                   jTextArea3.append(this.jComboBox5.getSelectedItem().toString());                
                jTextArea3.append("</speaker_layout>" + System.lineSeparator());
                jTextArea3.append("         <source/>" + System.lineSeparator());
                jTextArea3.append("     </audio1>" + System.lineSeparator());
                
                jTextArea3.append("     <audio2>" + System.lineSeparator());
                jTextArea3.append("         <channels>");
                jTextArea3.append("</channels>" + System.lineSeparator());
                jTextArea3.append("         <type>");
                jTextArea3.append("</type>" + System.lineSeparator());
                jTextArea3.append("         <language>");
                jTextArea3.append("</language>" + System.lineSeparator());
                jTextArea3.append("         <speaker_layout>");
                jTextArea3.append("</speaker_layout>" + System.lineSeparator());
                jTextArea3.append("         <source/>" + System.lineSeparator());
                jTextArea3.append("     </audio2>" + System.lineSeparator());

                jTextArea3.append("     <audio3>" + System.lineSeparator());
                jTextArea3.append("         <channels>");
                jTextArea3.append("</channels>" + System.lineSeparator());
                jTextArea3.append("         <type>");
                jTextArea3.append("</type>" + System.lineSeparator());
                jTextArea3.append("         <language>");
                jTextArea3.append("</language>" + System.lineSeparator());
                jTextArea3.append("         <speaker_layout>");
                jTextArea3.append("</speaker_layout>" + System.lineSeparator());
                jTextArea3.append("         <source/>" + System.lineSeparator());
                jTextArea3.append("     </audio3>" + System.lineSeparator());

                jTextArea3.append("     <audio4>" + System.lineSeparator());
                jTextArea3.append("         <channels>");
                jTextArea3.append("</channels>" + System.lineSeparator());
                jTextArea3.append("         <type>");
                jTextArea3.append("</type>" + System.lineSeparator());
                jTextArea3.append("         <language>");
                jTextArea3.append("</language>" + System.lineSeparator());
                jTextArea3.append("         <speaker_layout>");
                jTextArea3.append("</speaker_layout>" + System.lineSeparator());
                jTextArea3.append("         <source/>" + System.lineSeparator());
                jTextArea3.append("     </audio4>" + System.lineSeparator());
                
                jTextArea3.append(" </summary>" + System.lineSeparator());
                jTextArea3.append("</audio>" + System.lineSeparator());

                jTextArea3.append("<data_carrier>" + System.lineSeparator());
                jTextArea3.append(" <archive_number_digim>");
                jTextArea3.append("</archive_number_digim>" + System.lineSeparator());
                jTextArea3.append(" <data_carrier_type/>" + System.lineSeparator());
                jTextArea3.append("</data_carrier>" + System.lineSeparator());
                
                jTextArea3.append("</digim_file>");
    }
    
    private void resetAllInput()
    {
        // Var zurück setzen
        fileDateien = null;
        strVerzeichnis = new String();
       
        this.jTextField1.setText("");
        this.jTextField2.setText("");
        
        this.jTextPane1.setText("");
        this.jTextPane2.setText("");
        
        this.jTextPane3.setText("");
        this.jTextArea2.setText("");
        this.jTextArea3.setText("");
        
        // Audioseite
        this.jComboBox3.setSelectedIndex(0);
        this.jComboBox4.setSelectedIndex(0);
        this.jComboBox5.setSelectedIndex(0);
        
        //Videoseite
        this.jComboBox6.setSelectedIndex(0);
        this.jComboBox7.setSelectedIndex(0);
        this.jComboBox8.setSelectedIndex(0);
        this.jComboBox9.setSelectedIndex(0);
        this.jComboBox10.setSelectedIndex(0);
        this.jComboBox11.setSelectedIndex(0);
        this.jComboBox12.setSelectedIndex(0);
        this.jComboBox13.setSelectedIndex(0);
        this.jComboBox14.setSelectedIndex(0);
        this.jComboBox15.setSelectedIndex(0);
        this.jComboBox16.setSelectedIndex(0);
        this.jTextField4.setText("");
        this.jTextField5.setText("");
        
        //Allgemein
        this.jComboBox1.setSelectedIndex(0);
        this.jComboBox2.setSelectedIndex(0);
        this.jCheckBox1.setSelected(false);
        this.jCheckBox2.setSelected(false);
        this.jTextField3.setText("");
        
        //xml
        this.jTextArea3.setText("");
        
        // alle Markierungen aufheben
        resetMarker(checkObjAllgemein);
        resetMarker(checkObjVideo);
        resetMarker(checkObjVideoUT);
        resetMarker(checkObjAudio);
        jPanel2.setBackground(defaultBackgroundColor);

        
        // Alle felder vom MD5.tab löschen
        
        JTextField tempTXT   = null;
        JLabel tempLbl = null;
        
        for(int i = 0; i < genTXT.size();i++)
        {
            tempTXT = (JTextField) genTXT.get(i);
            jPanel13.remove(tempTXT);
        }

        for(int i = 0; i < genLabel.size();i++)
        {
            tempLbl = (JLabel) genLabel.get(i);
            jPanel13.remove(tempLbl);
        }
        
        genTXT.clear();
        genLabel.clear();

        this.jTabbedPane1.setSelectedIndex(0);
        this.jTabbedPane1.setEnabledAt(1, false);
        this.jTabbedPane1.setEnabledAt(2, false);
        this.jTabbedPane1.setEnabledAt(3, false);
        this.jTabbedPane1.setEnabledAt(4, false);


    }
    
    private void resetMarker(ArrayList checkObj)
    {
    Object o = new Object();
    JComboBox c = null;
    JTextField t = null;

        for (int i =0 ; i< checkObj.size(); i++)
        {
                o = checkObj.get(i);

                if (o instanceof JComboBox)
                {
                    c = (JComboBox) o;
                    c.setBackground(defaultBackgroundColor);
                }
                if (o instanceof JTextField)
                {
                    t = (JTextField) o;
                    t.setBackground(defaultBackgroundColor);
                }

        }   
    }
    
    
    private boolean checkFillStatusOK()
    {
       // boolean ok = true;
        
        boolean okAllgemein = true;
        boolean okVideo = true;
        boolean okVideoUT = true;
        boolean okAudio = true;
        boolean okMD5 = true;
        
        resetMarker(checkObjAllgemein);
        resetMarker(checkObjVideo);
        resetMarker(checkObjVideoUT);
        resetMarker(checkObjAudio);
        this.jPanel2.setBackground(defaultBackgroundColor);
 
        // Allgemeine Seite
        okAllgemein = checkFillStatus(checkObjAllgemein);
        if (jCheckBox1.isSelected() || jCheckBox2.isSelected()) 
        {
            this.jPanel2.setBackground(defaultBackgroundColor);
            // Audioseite Checken
            if(jCheckBox2.isSelected())
            {
                //resetMarker(checkObjAudio);
                okAudio = checkFillStatus(checkObjAudio);
            }
            
            // Videoseite checken
            if (jCheckBox1.isSelected())
            {
                //resetMarker(checkObjVideo);
                okVideo = checkFillStatus(checkObjVideo);
                if(jComboBox10.getSelectedItem().toString().equals("texted"))
                {
                    okVideoUT = checkFillStatus(checkObjVideoUT);
                }
            }
        }   else
        {
            Meldung("Audio oder Video muss ausgewählt werden.",1);
            this.jPanel2.setBackground(defaultErrorColor);
        }
        //ok = 
        
        if (fileDateien.length > 0)
        {
            
            ArrayList temp = new ArrayList();
            temp.clear();
            for (int i = 0; i< genTXT.size();i++)
            {
                temp.add(genTXT.get(i));
            }
            temp.add("MD5");
            resetMarker(temp);
            okMD5 = checkFillStatus(temp);
        }
        if(okAllgemein && okAudio && okVideo && okVideoUT && okMD5) return true;
        else return false;
        
    }
    
    //checkt ob Index=0 oder Leer ist
    private boolean checkFillStatus(ArrayList checkObj)
    {
    boolean ok = true;
    Object o = new Object();
    JComboBox c = null;
    JTextField t = null;
    int tab = 10;
    String bez = null;

    for (int i =0 ; i< checkObj.size(); i++)
    {
            o = checkObj.get(i);
            
            if (o instanceof JComboBox)
            {
                c = (JComboBox) o;
                if (c.getSelectedIndex()<1) 
                {
                    ok= false;
                    c.setBackground(defaultErrorColor);
                }
            }
            if (o instanceof JTextField)
            {
                t = (JTextField) o;
                if(t.getText().isEmpty())
                {
                    ok = false;
                    t.setBackground(defaultErrorColor);
                }
            }
            if (o instanceof Integer)  tab = (Integer) o;
            if (o instanceof String) bez = (String) o;
            
    }
    if (!ok) Meldung("Im Tab [ " + bez + " ] fehlen Angaben.",1);
        //this.jTabbedPane1.setBackgroundAt(tab, defaultErrorColor);
//    this.jTabbedPane1.setBackgroundAt(1, Color.RED);
//    this.jTabbedPane1.update(jTabbedPane1.getGraphics() );
    return ok;
    }
    
    private String extractLangCode(String s, String trenner)
    {
        String[] sa = s.split(trenner);
        return sa[0];    
    }
    
    private void loadChecksumFile()
    {
        
    }
}
