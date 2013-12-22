package com.progwml6.ircbot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config {
        private static PropertiesConfiguration conf;
        public static PropertiesConfiguration customcmd;
        public static PropertiesConfiguration add;
        public static String CURRENT_DIR = "conf/";
        public static File cnf = new File(CURRENT_DIR + "/config.yml");
        public static File cmd = new File(CURRENT_DIR + "/custom-commands.yml");
        public static File toadd = new File (CURRENT_DIR + "/To-Add.yml");
        public static boolean IDENTIFY_WITH_NICKSERV;
        public static boolean VERIFY_ADMIN_NICKS;
        public static boolean DEBUG_MODE;
        public static boolean NOTICE;
        public static boolean ACCEPT_INVITES;
        public static boolean ALLOW_FSERV;
        public static boolean AWAY;
        public static boolean AWAY_TNTUP;
        public static String PERMISSIONS_DENIED;
        public static String SERVER;
        public static String NOT_ADMIN;
        public static String PASSWORD;
        public static String NICK;
        public static String IDENT;
        public static String REALNAME;
        public static String NOTICE_IDENTIFIER;
        public static String PUBLIC_IDENTIFIER;
        public static String[] CHANS;
        public static List<String> ADMINS;
        public static List<String> EXEC_ADMINS;
        public static List<String> MCSERVER;
        
        public static void loadConfig() throws IOException, ConfigurationException{
                conf = new PropertiesConfiguration(cnf);
                customcmd = new PropertiesConfiguration(cmd);
                add = new PropertiesConfiguration(toadd);
                
                if(cmd.exists()){
                        customcmd.load(cmd);
                }else{
                        cmd.getParentFile().mkdirs();
                        cmd.createNewFile();
                }
                
                if(toadd.exists()){
                        add.load(toadd);
                }else{
                        toadd.getParentFile().mkdirs();
                        toadd.createNewFile();
                }
                
                System.out.println(String.valueOf(cnf.exists()));
                if (cnf.exists()) {        
                        System.out.println(cnf.getAbsolutePath());
                        conf.load(cnf);
                }else{
                        cnf.getParentFile().mkdirs();
                        cnf.createNewFile();
                        System.out.println(cnf.getAbsolutePath());
                        conf.setFile(cnf);
                        conf.setProperty("SERVER", "irc.esper.net");
                        conf.setProperty("BOT-NICKNAME", "Alfred");
                        conf.setProperty("BOT-IDENT", "Alphabot|batman");
                        conf.setProperty("BOT-REALNAME", "Alphabot|batman");
                        conf.setProperty("IDENTIFY-WITH-NICKSERV", true);
                        conf.setProperty("NICKSERV-PASS", "Change This!");
                        conf.setProperty("CHANNELS","#batman");
                        conf.setProperty("VERIFY-BOT-ADMINS", true);
                        conf.setProperty("BOT-ADMINS","batman");
                        conf.setProperty("EXEC-ADMINS","batman");
                        conf.setProperty("ALLOW-FILE-TRANSFER", false);
                        conf.setProperty("PUBLIC-IDENTIFIER", "`");
                        conf.setProperty("NOTICE-IDENTIFIER", "|");
                        conf.setProperty("PERMISSIONS-DENIED", "AWW HELL NAW! You ain't gonna tell me what to do!");
                        conf.setProperty("NOT-ADMIN", "AWW HELL NAW! You ain't gonna tell me what to do!");
                        conf.setProperty("DEBUG-MODE", true);
                        conf.setProperty("ACCEPT-INVITIATIONS", true);
                        conf.setProperty("NOTICE", false);
                        conf.save();
                }
                ADMINS = new ArrayList<String>();
                EXEC_ADMINS = new ArrayList<String>();
                IDENTIFY_WITH_NICKSERV = conf.getBoolean("IDENTIFY-WITH-NICKSERV");
                VERIFY_ADMIN_NICKS = conf.getBoolean("VERIFY-BOT-ADMINS");
                DEBUG_MODE = conf.getBoolean("DEBUG-MODE");
                PERMISSIONS_DENIED = conf.getString("PERMISSIONS-DENIED");
                NOT_ADMIN = conf.getString("NOT-ADMIN");
                PASSWORD = conf.getString("NICKSERV-PASS");
                NICK = conf.getString("BOT-NICKNAME");
                IDENT = conf.getString("BOT-IDENT");
                REALNAME = conf.getString("BOT-REALNAME");
                ALLOW_FSERV = conf.getBoolean("ALLOW-FILE-TRANSFER");
                NOTICE_IDENTIFIER = conf.getString("NOTICE-IDENTIFIER");
                PUBLIC_IDENTIFIER = conf.getString("PUBLIC-IDENTIFIER");
                CHANS = conf.getString("CHANNELS").split(" ");
                for(String user : conf.getString("BOT-ADMINS").split(" ")){
                        ADMINS.add(user);
                }
                for(String user : conf.getString("EXEC-ADMINS").split(" ")){
                        EXEC_ADMINS.add(user);
                }
                SERVER = conf.getString("SERVER");
                ACCEPT_INVITES = conf.getBoolean("ACCEPT-INVITIATIONS");
            }
        
        public static PropertiesConfiguration getConfig(){
                return conf;
        }
        public static void reload(){
                try{
                        System.out.println("Reloading config!");
                        getConfig().clear();
                        System.out.println("Cleared properies");
                        System.out.println("Setting properties");
                        conf.setProperty("SERVER", Config.SERVER);
                        conf.setProperty("AWAY", Config.AWAY);
                        conf.setProperty("AWAY-TNTUP", Config.AWAY_TNTUP);
                        conf.setProperty("NOTICE", Config.NOTICE);
                        conf.setProperty("BOT-NICKNAME", Config.NICK);
                        conf.setProperty("BOT-IDENT", Config.IDENT);
                        conf.setProperty("BOT-REALNAME", Config.REALNAME);
                        conf.setProperty("IDENTIFY-WITH-NICKSERV", Config.IDENTIFY_WITH_NICKSERV);
                        conf.setProperty("NICKSERV-PASS", Config.PASSWORD);
                        String tmp = "";
                        for(int i = 0; i < CHANS.length; i++){
                                tmp += CHANS[i] + " ";
                        }
                        conf.setProperty("CHANNELS", tmp);
                        conf.setProperty("VERIFY-BOT-ADMINS", Config.VERIFY_ADMIN_NICKS);
                        String temp = "";
                        for(String s : ADMINS){
                                temp += s + " ";
                        }
                        conf.setProperty("BOT-ADMINS", temp.trim());
                        temp = "";
                        for(String s : EXEC_ADMINS){
                                temp += s + " ";
                        }
                        conf.setProperty("EXEC-ADMINS", temp.trim());
                        conf.setProperty("EXEC-ADMINS", temp.trim());
                        conf.setProperty("ALLOW-FILE-TRANSFER", Config.ALLOW_FSERV);
                        conf.setProperty("PUBLIC-IDENTIFIER", Config.PUBLIC_IDENTIFIER);
                        conf.setProperty("NOTICE-IDENTIFIER", Config.NOTICE_IDENTIFIER);
                        conf.setProperty("PERMISSIONS-DENIED",Config.PERMISSIONS_DENIED);
                        conf.setProperty("NOT-ADMIN", Config.NOT_ADMIN);
                        conf.setProperty("DEBUG-MODE", Config.DEBUG_MODE);
                        conf.setProperty("ACCEPT-INVITIATIONS", Config.ACCEPT_INVITES);
                        System.out.println("Done Setting propertis!\nSaving!");
                        conf.save(cnf);
                        System.out.println("Saved!");
                }catch(Exception e){
                        e.printStackTrace();
                }
        }
}