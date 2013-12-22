package com.progwml6.ircbot;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Arrays;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.ParseException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.WhoisEvent;

import bsh.Interpreter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import org.pircbotx.hooks.events.PrivateMessageEvent;


@SuppressWarnings("rawtypes")
/**
 *
 * @author zack6849(zcraig29@gmail.com)
 */
public class Commands {
	static Interpreter i = new Interpreter();
	static Interpreter interpreter = new Interpreter();
    static List<String> owners = Bot.owners;
    static String perms = Config.PERMISSIONS_DENIED;
    static String password;
    static Utils utils = new Utils();
    public static PircBotX bot;
    
    
    public static void shortenUrl(MessageEvent e) {
        String[] args = e.getMessage().split(" ");
        if (args.length == 2) {
            e.respond(Utils.shortenUrl(args[1]));
        } else {
            Utils.sendNotice(e.getUser().toString(), "Improper usage! correct usage: $shorten http://google.com/");
        }
    }
    
    public static void uptime(MessageEvent e) {
        RuntimeMXBean uptime1 = ManagementFactory.getRuntimeMXBean();
        Long time = uptime1.getUptime();
        int seconds = (int) (time / 1000) % 60;
        int minutes = (int) (time / (1000 * 60)) % 60;
        int hours = (int) (time / (1000 * 60 * 60)) % 24;
        int days = (int) (time / 86400000);
        String uptime = String.format("%d Days %d Hours %d Minutes and %d seconds", days, hours, minutes, seconds);
        e.getBot().sendIRC().message(e.getChannel().toString(), "I've been running for " + uptime);
    }
    
    public static void usage(MessageEvent e) {
    if (Utils.isAdmin(e.getUser().getNick()) || e.getChannel().hasVoice(e.getUser()) || e.getChannel().isOp(e.getUser())) {
        Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        String Free = ("Free memory: " + format.format((freeMemory / 1024) / 1024));
        String Allocated = ("Allocated memory: " + format.format((allocatedMemory / 1024) / 1024));
        String Max = ("Max memory: " + format.format((maxMemory / 1024) / 1024));
        String Total = ("Total free memory: " + format.format(((freeMemory + (maxMemory - allocatedMemory)) / 1024) / 1024  ));
        System.out.println(sb);
        e.getBot().sendIRC().message(e.getChannel().toString(), Free + "MB | " + Allocated + "MB | " + Max + "MB | " + Total + "MB");
    } else {
        sendNotice(e.getUser().toString(), perms);
    }
    }
    
    public static void cinsult(MessageEvent e) throws InterruptedException {
        if (Utils.isAdmin(e.getUser().getNick()) || e.getChannel().hasVoice(e.getUser()) || e.getChannel().isOp(e.getUser())) {
        String[] args = e.getMessage().split(" ");
        String join = args[1];
        Channel chan = e.getBot().getChannel(args[1]);
        String insult1 = null;
        do {
        try {
            URL insult;
            insult = new URL("http://www.pangloss.com/seidel/Shaker/index.html?");
                BufferedReader br = new BufferedReader(new InputStreamReader(insult.openStream()));
                for(int i = 0; i < 16; ++i)
                br.readLine();
                String line = br.readLine();
                String y = line.replaceAll("</font>", " ").replace("</form><hr>", "");
                insult1 = y;
                while (line != null) {
                    System.out.println(line);
                    line = br.readLine();
                }
                br.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } while (insult1.isEmpty());
        e.getBot().sendIRC().joinChannel(join);
        e.getBot().sendIRC().message(join, insult1);
        Thread.sleep(50);
        e.getBot().sendIRC().partChannel(chan);
    } else {
            sendNotice(e.getUser().toString(), perms);
        }
    }
    
    public static void insult(MessageEvent e) {
        if (Utils.isAdmin(e.getUser().getNick()) || e.getChannel().hasVoice(e.getUser()) || e.getChannel().isOp(e.getUser())) {
        String insult1 = null;
        do {
        try {
            URL insult;
            insult = new URL("http://www.pangloss.com/seidel/Shaker/index.html?");
                BufferedReader br = new BufferedReader(new InputStreamReader(insult.openStream()));
                for(int i = 0; i < 16; ++i)
                br.readLine();
                String line = br.readLine();
                String y = line.replaceAll("</font>", " ").replace("</form><hr>", "");
                insult1 = y;
                while (line != null) {
                    System.out.println(line);
                    line = br.readLine();
                }
                br.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } while (insult1.isEmpty());
        e.getBot().sendIRC().message(e.getChannel().toString(), insult1);
    } else {
            sendNotice(e.getUser().toString(), perms);
        }
    }
    
    public static void notice(MessageEvent e) {
        boolean Notice = Boolean.valueOf(e.getMessage().split(" ")[1]);
        if (e.getUser().getNick().equalsIgnoreCase("batman")) {
            Config.NOTICE = Notice;
            e.getBot().sendIRC().notice(e.getUser().toString(), "Notice was set to " + String.valueOf(Notice));
    }
    }
    
    public static void getSystemUptime(MessageEvent e) throws IOException, ParseException  {
    int unixTime = Integer.valueOf(new Scanner(new FileInputStream("/proc/uptime")).next().replaceAll("\\.[0-9]+", ""));
    int day = (int) TimeUnit.SECONDS.toDays(unixTime);
    long hours = TimeUnit.SECONDS.toHours(unixTime) - (day * 24);
    long minute = TimeUnit.SECONDS.toMinutes(unixTime) - (TimeUnit.SECONDS.toHours(unixTime) * 60);
    long seconds = TimeUnit.SECONDS.toSeconds(unixTime) - (TimeUnit.SECONDS.toMinutes(unixTime) * 60);
                String time = String.format("%d Days %d Hours %d Minutes and %d seconds", day, hours, minute, seconds);
                e.getBot().sendIRC().message(e.getChannel().toString(), Colors.GREEN + "System uptime" + Colors.NORMAL + ": " + time);
}
    
    public static void wiki(MessageEvent e) {
        String[] arg = e.getMessage().split(" ");
        String commandname = arg[1];
        if (commandname.equalsIgnoreCase("ftb")) {
            
            StringBuilder sb = new StringBuilder();
            String[] args = e.getMessage().split(" ");
            
            for(int i = 2; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            
            String message = sb.toString().trim();
            String y = "http://feed-the-beast.wikia.com/wiki/Special:Search?search=" + message;
            String x = y.replaceAll(" ", "%20");
            String finalurl = null;
            String nope = null;
            String no = "																				No results found.";
            try {
                URL wiki;
                wiki = new URL("http://is.gd/create.php?format=simple&url=" + x);
                BufferedReader br = new BufferedReader(new InputStreamReader(wiki.openStream()));
                finalurl = br.readLine();                      
                br.close();
                

                URL read;
                read = new URL(x);
                BufferedReader xx = new BufferedReader(new InputStreamReader(read.openStream()));
                for(int i = 0; i < 575; ++i)
                xx.readLine();
                String line = xx.readLine();
                String line2 = line.replaceAll("<p><i>", "").replaceAll("</i></p>", "");
                nope = line2;
//                e.getBot().sendIRC().message(e.getChannel().toString(), nope);
                while (line != null) {
                    System.out.println(line);
                    line = xx.readLine();
                 
                }
                xx.close();
                        
                
                } catch (Exception e2) {
                e2.printStackTrace();
            }
            if (nope.equals(no)) {
                e.getBot().sendIRC().message(e.getChannel().toString(), "No results found. :(");
            } else {
                e.getBot().sendIRC().message(e.getChannel().toString(), message + ": " + finalurl);
            }
        }
        
        if (commandname.equalsIgnoreCase("Wikip")) {
            // http://www.google.com/search?q=site%3Aen.wikipedia.org+%s&btnI=745
            StringBuilder sb = new StringBuilder();
            String[] args = e.getMessage().split(" ");
            
            for(int i = 2; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            
            String string = sb.toString().trim();
            String message = string.replaceAll(" ", "+");
            String y = "http://www.google.com/search?q=site%3Aen.wikipedia.org+" + message + "&btnI=745";
            String x = y.replaceAll(" ", "+");
            String finalurl = null;
            String link = null;
            try {
                URL shorten;
                shorten = new URL("http://tinyurl.com/create.php?source=indexpage&url=" + x);
                BufferedReader br = new BufferedReader(new InputStreamReader(shorten.openStream()));
                for(int i = 0; i < 216; ++i)
                br.readLine();
                String xx = br.readLine();
                br.close();
                String test = xx.replace("copy('", "").replace("')", "");
                link = test.replace(";", "");
        }   catch (Exception e2) {
                e2.printStackTrace();
            }
            e.getBot().sendIRC().message(e.getChannel().toString(), x);
 //           e.getBot().sendIRC().message(e.getChannel().toString(), message + ": " + finalurl);
        }
    }
    
    public static void google(MessageEvent event) {
        String[] args = event.getMessage().split(" ");
        String prefix = String.valueOf(args[0].charAt(0));
        String result = "";
        if (args.length >= 2) {
            for (int i = 1; i < args.length; i++) {
                result += args[i] + " ";
            }
            if (prefix.equalsIgnoreCase(Config.NOTICE_IDENTIFIER)) {
                event.getBot().sendIRC().notice(event.getUser().toString(), StringEscapeUtils.unescapeHtml(Utils.google(result.trim()).replaceAll("%3F", "?").replaceAll("%3D", "=")));
                return;
            }
            event.getBot().sendIRC().message(event.getChannel().toString(), StringEscapeUtils.unescapeHtml(Utils.google(result.trim()).replaceAll("%3F", "?").replaceAll("%3D", "=")));
        }
    }

    public static void listOperators(MessageEvent e) {
        String[] args = e.getMessage().split(" ");
        String s = String.valueOf(args[0].charAt(0));
        if (s.equalsIgnoreCase(Config.PUBLIC_IDENTIFIER)) {
            List<String> myList = new ArrayList<String>();
            for (User u : e.getChannel().getOps()) {
                myList.add(u.getNick());
            }
            String f1 = myList.toString().replaceAll("[\\['']|['\\]'']", "");
            e.respond("The current channel operators are " + f1);
        }
        if (s.equalsIgnoreCase(Config.NOTICE_IDENTIFIER)) {
            List<String> myList = new ArrayList<String>();
            for (User u : e.getChannel().getOps()) {
                myList.add(u.getNick());
            }
            String f1 = myList.toString().replaceAll("[\\['']|['\\]'']", "");
            sendNotice(e.getUser().toString(), "The current channel operators are " + f1);
        }
    }

    public static void joinChannel(MessageEvent e) {
        String[] args = e.getMessage().split(" ");
        if (args.length == 2) {
            if (e.getBot().getChannel(args[1]).isOp(e.getUser()) || Utils.isAdmin(e.getUser().getNick())) {
                e.respond("FINE!");
                if (args[1].startsWith("#")) {
                e.getBot().sendIRC().joinChannel(args[1]);
                e.getBot().getChannel(args[1]);
                } else {
                String y = "#" + args[1];
                e.getBot().sendIRC().joinChannel(y);
                e.getBot().getChannel(y);
            }
            } else {
                e.respond(perms);
            }
        }
    }
    
    public static void startwith(MessageEvent e){
        String x = ("#");
        String[] args = e.getMessage().split(" ");
        if (args.length == 2) {
            if (args[1].startsWith(x)) {
                e.respond(args[1]);
            } else {
                String y = "#" + args[1];
                e.respond(y);
            }
        }
    }

    public static void setDelay(MessageEvent e) {
        String[] args = e.getMessage().split(" ");
        if (args.length == 2) {
            if (StringUtils.isNumeric(args[1])) {
                try
                {
                    e.getBot().sendIRC().wait(Long.valueOf(args[1]));
                }
                catch (NumberFormatException | InterruptedException e1)
                {
                    e1.printStackTrace();
                }
                sendNotice(e.getUser().toString(), "Message delay set to " + Integer.valueOf(args[1]) + " milliseconds!");
            } else {
                sendNotice(e.getUser().toString(), "The argument " + args[1] + " is not a number!");
            }
        }
    }

    public static void authenticate(MessageEvent e) {
        e.getBot().identify(password);
    }

    public static void listFiles(MessageEvent e) {
        File f = new File(Config.CURRENT_DIR + "/dcc/");
        String filenames = "";
        boolean ispublic;
        if (String.valueOf(e.getMessage().charAt(0)).equalsIgnoreCase(Config.PUBLIC_IDENTIFIER)) {
            ispublic = true;
        } else {
            ispublic = false;
        }
        File[] dir = f.listFiles();
        if (dir.length == 0) {
            if (ispublic) {
                e.getBot().sendIRC().message(e.getChannel().toString(), "no files in that directory!");
            } else {
                e.getBot().sendIRC().notice(e.getUser().toString(), "no files in that directory.");
            }
            return;
        }
        for (int i = 0; i < dir.length; i++) {
            filenames += dir[i].getName() + " ";
        }
        if (ispublic) {
            e.getBot().sendIRC().message(e.getChannel().toString(), "available files: " + filenames);
        } else {
            e.getBot().sendIRC().notice(e.getUser().toString(), "available files: " + filenames);
        }
    }

    public static void sendFile(final MessageEvent e) {
        final String[] args = e.getMessage().split(" ");
        if (args.length == 2) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File requested = new File(Config.CURRENT_DIR + "/dcc/" + args[1]);
                        if (requested.exists()) {
                            e.getBot().dccSendChatRequest(e.getUser(), 120000);
                            Thread.sleep(2000);
                            e.getBot().dccSendFile(requested, e.getUser(), 120000);
                        } else {
                            e.getBot().sendIRC().notice(e.getUser().toString(), "Unknown file specified, run listfiles to see all files.");
                        }

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }).start();

        }
    }

    public static void globalSay(MessageEvent e) {
        String[] args = e.getMessage().split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String all = sb.toString().trim();
        if (args.length >= 3) {
            Channel t = e.getBot().getChannel(args[1]);
            e.getBot().sendIRC().message(t.toString(), all);
        } else {
            sendNotice(e.getUser().toString(), "Usage: " + Bot.prefix + "GSAY #CHANNEL MESSAGE");
        }
    }

    public static void clense(MessageEvent e) {
        String[] args = e.getMessage().split(" ");
        if (args.length == 3) {
            User t = e.getBot().getUser(args[1]);
            User x = e.getBot().getUser(args[2]);
            e.getBot().sendIRC().action(e.getChannel().toString(), "cleanses " + t.getNick() + " with the love of " + x.getNick());
        } else {
            sendNotice(e.getUser().toString(), "Usage: " + Bot.prefix + "Clense [victim] [rapist]");
        }
    }
    
    public static void clenseMC(MessageEvent e) {
    	String[] args = e.getMessage().split(" ");
        if (args.length == 4) {
            User t = e.getBot().getUser(args[2]);
            User x = e.getBot().getUser(args[3]);
            e.getBot().sendIRC().message(e.getChannel().toString(), "Clenses " + t.getNick() + " with the love of " + x.getNick());
        } else {
            sendNotice(e.getUser().toString(), "Usage: " + Bot.prefix + "Clense [victim] [rapist]");
        }
    }

    public static void partChannel(MessageEvent e) {
    	String[] args = e.getMessage().split(" ");
    	  if(args.length == 1){
    		if (e.getChannel().getOps().contains(e.getUser()) || Utils.isAdmin(e.getUser().getNick())) {
                e.getBot().partChannel(e.getChannel());
            } else {
                e.respond(perms);
            }
    	} else {
    		Channel chan = e.getBot().getChannel(args[1]);
    		e.getBot().partChannel(chan);
    	} 
    }
    
    public static void sendAction(MessageEvent e){
        if (Utils.isAdmin(e.getUser().getNick()) || e.getChannel().hasVoice(e.getUser()) || e.getChannel().isOp(e.getUser())) {
    	String[] args = e.getMessage().split(" ");
    	
    	StringBuilder sb = new StringBuilder();
    	for(int i = 1; i < args.length; i++){
    		sb.append(args[i]).append(" ");
    	}
        
    	String action = sb.toString().trim();
        e.getBot().sendIRC().message(e.getChannel().toString(), action);
    } else {
            sendNotice(e.getUser().toString(), perms);
        }
    }

    public static void nope(MessageEvent e) {
        if (e.getMessage().contains("@")) {
            String[] arg1 = e.getMessage().split("@");
            e.getBot().sendIRC().message(e.getChannel().toString(), arg1[1] + ": http://www.youtube.com/watch?v=gvdf5n-zI14");
        } else {
            e.getBot().sendIRC().message(e.getChannel().toString(), "nope: http://www.youtube.com/watch?v=gvdf5n-zI14");
        }
    }

    public static void changeNickName(MessageEvent e) {
        String[] arguments = e.getMessage().split(" ");
        if (Utils.isAdmin(e.getUser().getNick())) {
            if (arguments.length == 2) {
                e.getBot().changeNick(arguments[1]);
            } else {
                e.respond("Usage: nick <new nickname>");
            }
        } else {
            e.respond(perms);
        }
    }

    public static void setPrefix(MessageEvent e) {
        String[] args = e.getMessage().split(" ");
        if (e.getChannel().getVoices().contains(e.getUser()) || e.getChannel().getOps().contains(e.getUser()) || Utils.isAdmin(e.getUser().getNick())) {
            if (args.length == 2) {
                Config.PUBLIC_IDENTIFIER = args[1];
                sendNotice(e.getUser().toString(), e.getBot().getNick() + "' prefix was set to :" + Config.PUBLIC_IDENTIFIER);
            } else {
                sendNotice(e.getUser().toString(), "Usage: $Bot prefix <new Bot prefix>");
            }
        } else {
            e.respond(perms);
        }
    }

    public static void listChannels(MessageEvent e) {
    	String[] args = e.getMessage().split(" ");
    	String s = String.valueOf(args[0].charAt(0));
    	String channels = "";
    	for(Channel c : e.getBot().getChannels()){
    		if(!c.isSecret()){
    			channels += c.getName() + " ";
    		}
    	}
    	if(s.equalsIgnoreCase(Config.NOTICE_IDENTIFIER)){
			e.getBot().sendIRC().notice(e.getUser().toString(), "Current channels: " + channels);
		}else if(s.equalsIgnoreCase(Config.PUBLIC_IDENTIFIER)){
			e.getBot().sendIRC().message(e.getChannel().toString(), "Current channels: " + channels);
		}
    }

    public static void not_here(MessageEvent e) {
        boolean x = Config.AWAY ;
        if (x) {
        	e.getBot().sendIRC().message(e.getChannel().toString(), "Batman/Harry is not here, but your message will be logged for him to stalk later");
           }else{
            	
        }
    }
        public static void not_hereTNT(MessageEvent e) {
        boolean x = Config.AWAY_TNTUP ;
        if (x) {
        	e.getBot().sendIRC().message(e.getChannel().toString(), "TNTUP is not here, but your message will be logged for him to stalk later");
           }else{
            	
        }
    }
    
    public static void checkAccount(MessageEvent e) {
        String[] args = e.getMessage().split(" ");
        String s = String.valueOf(args[0].charAt(0));
        if (args.length == 2) {
            Boolean b = Utils.checkAccount(args[1]);
            if (s.equalsIgnoreCase(Config.PUBLIC_IDENTIFIER)) {
                if (b) {
                    e.respond(args[1] + Colors.GREEN + " has " + Colors.NORMAL + "paid for minecraft");
                } else {
                    e.respond(args[1] + Colors.RED + " has not " + Colors.NORMAL + "paid for minecraft");
                }
            }
            if (s.equalsIgnoreCase(Config.NOTICE_IDENTIFIER)) {
                if (b) {
                    Utils.sendNotice(e.getUser().toString(), args[1] + ":" + Colors.GREEN + String.valueOf(b));
                } else {
                    Utils.sendNotice(e.getUser().toString(), args[1] + ":" + Colors.RED + String.valueOf(b));
                }
            }
        } else {
            Utils.sendNotice(e.getUser().toString(), "You failed to specify a username! usage:  " + Bot.prefix + "paid <username>");
        }

    }
    public static void paidMC(MessageEvent e) {
        String[] args = e.getMessage().split(" ");
        Boolean b = Utils.checkAccount(args[2]);
                        if (b) {
                    e.getBot().sendIRC().message(e.getChannel().toString(), args[1] + Colors.GREEN + " has " + Colors.NORMAL + "paid for minecraft");
                } else {
                    e.getBot().sendIRC().message(e.getChannel().toString(), args[1] + Colors.RED + " has not " + Colors.NORMAL + "paid for minecraft");
                }
    }

    public static void checkMojangServers(MessageEvent e) {
        String[] args = e.getMessage().split(" ");
        String s = String.valueOf(args[0].charAt(0));
        if (s.equalsIgnoreCase(Config.PUBLIC_IDENTIFIER)) {
            try {
                URL url;
                url = new URL("http://status.mojang.com/check");
                BufferedReader re = new BufferedReader(new InputStreamReader(url.openStream()));
                String st;
                while ((st = re.readLine()) != null) {
                    String a = st.replace("red", Colors.RED + "✘" + Colors.NORMAL +" |" + Colors.NORMAL).replace("green", Colors.DARK_GREEN + "✔" + Colors.NORMAL +" |" + Colors.NORMAL).replace("[", "").replace("]", "");
                    String b = a.replace("{", "").replace("}", "").replace(":", " ").replace("\"", "").replace("minecraft.net", "Minecraft").replace("login.Minecraft", "Login").replace("session.Minecraft", "Session").replace("account.mojang.com", "Account").replace("auth.mojang.com", "Auth").replace("skins.Minecraft.net", "test").replace("authserver.mojang.com", "Auth").replace("sessionserver.mojang.com", "Session").replaceAll(",", " ");
                    String c = b.replace("skins.Minecraft", "Skins");
                    e.respond(c);
                }
            } catch (IOException E) {
                if (E.getMessage().contains("503")) {
                    e.respond("The minecraft status server is temporarily unavailable, please try again later");
                }
                if (E.getMessage().contains("404")) {
                    e.respond("Uhoh, it would appear as if the haspaid page has been removed or relocated >_>");
                }
            }
        }
        
        if (s.equalsIgnoreCase(Config.NOTICE_IDENTIFIER)) {
            try {
                URL url;
                url = new URL("http://status.mojang.com/check");
                BufferedReader re = new BufferedReader(new InputStreamReader(url.openStream()));
                String st;
                while ((st = re.readLine()) != null) {
                    String a = st.replace("red", Colors.RED + "✘" + Colors.NORMAL +" |" + Colors.NORMAL).replace("green", Colors.GREEN + "✔" + Colors.NORMAL +" |" + Colors.NORMAL).replace("[", "").replace("]", "");
                    String b = a.replace("{", "").replace("}", "").replace(":", " ").replace("\"", "").replace("minecraft.net", "Minecraft").replace("login.Minecraft", "Login").replace("session.Minecraft", "Session").replace("account.mojang.com", "Account").replace("auth.mojang.com", "Auth").replace("skins.Minecraft.net", "test").replace("authserver.mojang.com", "Auth").replace("sessionserver.mojang.com", "Session").replaceAll(",", " ");
                    String c = b.replace("skins.Minecraft", "Skins");
                    e.respond(c);
                }
            } catch (IOException E) {
                if (E.getMessage().contains("503")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "The Minecraft status server is temporarily unavailable, please try again later");
                }
                if (E.getMessage().contains("404")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "Uhoh, it would appear as if the haspaid page has been removed or relocated >_>");
                }
            }
        }
    }

    public static void say(MessageEvent e) {
        if (Utils.isAdmin(e.getUser().getNick())) {
            StringBuilder sb = new StringBuilder();
            String[] arguments = e.getMessage().split(" ");
            for (int i = 1; i < arguments.length; i++) {
                sb.append(arguments[i]).append(" ");
            }
            String allArgs = sb.toString().trim();
            e.getBot().sendIRC().message(e.getChannel().toString(), allArgs);
        } else {
            e.respond(perms);
        }
    }

    public static void checkServerStatus(MessageEvent e) {
        String[] args = e.getMessage().split(" ");
        String s1 = String.valueOf(e.getMessage().charAt(0));
        String result = null;
        if (args.length == 2) {
            try {
                result = Utils.checkServerStatus(InetAddress.getByName(args[1]), 25565);
            } catch (UnknownHostException ex) {
                Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (args.length == 3) {
            try {
                result = Utils.checkServerStatus(InetAddress.getByName(args[1]), Integer.valueOf(args[2]));
            } catch (UnknownHostException ex) {
                Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (s1.equalsIgnoreCase(Config.PUBLIC_IDENTIFIER)) {
            Utils.sendNotice(e.getUser().toString(), result);
        }
        if (s1.equalsIgnoreCase(Config.NOTICE_IDENTIFIER)) {
            e.getBot().sendIRC().message(e.getChannel().toString(), result);
        }
    }

    public static void killPM(PrivateMessageEvent e) {
      if (Utils.isAdmin(e.getUser().getNick())) {
        for (Channel ch : e.getUser().getChannels()) {
            e.getBot().sendRaw().rawLine("PART "+ ch.getName() + " : I HOPE YOU BURN IN HELL " + e.getUser().getNick() + ">:|");
        }
      }else{
    	  sendNotice(e.getUser().toString(), perms);
      }
    }
    public static void kill(MessageEvent e) {
        if (Utils.isAdmin(e.getUser().getNick())) {
        for (Channel ch : e.getUser().getChannels()) {
            e.getBot().sendRaw().rawLine("PART "+ ch.getName() + " : I HOPE YOU BURN IN HELL " + e.getUser().getNick() + ">:|");
        }
      }else{
    	  sendNotice(e.getUser().toString(), perms);
      }
    }

    public static void op(MessageEvent e) {
        String[] arguments = e.getMessage().split(" ");
        if (e.getChannel().getOps().contains(e.getUser()) || Utils.isAdmin(e.getUser().getNick())) {
            if (arguments.length == 2) {
                User u = e.getBot().getUser(arguments[1]);
                e.getBot().op(e.getChannel(), u);
            } else {
                e.respond("Usage: op <username>");
            }
        } else {
            e.respond(perms);
        }
    }

    public static void deop(MessageEvent e) {
        if (e.getChannel().getOps().contains(e.getUser()) || Utils.isAdmin(e.getUser().getNick())) {
            String[] arguments = e.getMessage().split(" ");
            if (arguments.length == 2) {
                User u = e.getBot().getUser(arguments[1]);
                e.getBot().sendIRC().message(e.getChannel().toString(), "Sorry " + u.getNick() + " </3");
                e.getChannel().send().deOp(u);
            } else {
                e.respond("Usage: deop <username>");
            }
        } else {
            e.respond(perms);
        }
    }

    public static void voice(MessageEvent e) {
        if (e.getChannel().getOps().contains(e.getUser()) || Utils.isAdmin(e.getUser().getNick())) {
            String[] arguments = e.getMessage().split(" ");
            if (arguments.length == 2) {
                User u = e.getBot().getUser(arguments[1]);
                e.getChannel().send().voice(u);
            } else {
                e.respond("Usage: voice <username>");
            }
        } else {
            e.respond(perms);
        }
    }

    public static void deVoice(MessageEvent e) {
        if (e.getChannel().getOps().contains(e.getUser()) || Utils.isAdmin(e.getUser().getNick())) {
            String[] arguments = e.getMessage().split(" ");
            if (arguments.length == 2) {
                User u = e.getBot().getUser(arguments[1]);
                e.getChannel().send().deVoice(u);
            } else {
                e.respond("Usage: devoice <username>");
            }
        } else {
            e.respond(perms);
        }
    }

    public static void quiet(MessageEvent e) {
        String[] arguments = e.getMessage().split(" ");
        if (e.getChannel().getOps().contains(e.getUser()) || Utils.isAdmin(e.getUser().getNick()) || e.getChannel().hasVoice(e.getUser())) {
            if (arguments.length == 2) {
                User u = e.getBot().getUser(arguments[1]);
                e.getChannel().send().setMode( "+1 " + u.getNick());
            } else {
                e.getBot().sendIRC().notice(e.getUser().toString(), "Usage: quiet <username>");
            }
        } else {
            e.getBot().sendIRC().notice(e.getUser().toString(), perms);
        }
    }

    public static void unquiet(MessageEvent e) {
        String[] arguments = e.getMessage().split(" ");
        if (e.getChannel().isOp(e.getUser()) || e.getChannel().hasVoice(e.getUser()) || Utils.isAdmin(e.getUser().getNick())) {
            if (arguments.length == 2) {
                User u = e.getBot().getUser(arguments[1]);
                e.getChannel().send().setMode( "-q " + u.getNick());
            } else {
                e.getBot().sendIRC().notice(e.getUser().toString(), "Usage: unquiet <username>");
            }
        } else {
            e.getBot().sendIRC().notice(e.getUser().toString(), perms);
        }
    }

    public static void addOwner(MessageEvent e) throws ConfigurationException, IOException {
        if (Utils.isAdmin(e.getUser().getNick())) {
            String[] arguments = e.getMessage().split(" ");
            if (arguments.length == 2) {
                if (!Utils.isAdmin(arguments[1])) {
                    Config.ADMINS.add(arguments[1]);
                    String admins = "";
                    for (String s : Config.ADMINS) {
                        admins += s + " ";
                    }
                    Config.reload();
                    Config.getConfig().refresh();
                    Utils.sendNotice(e.getUser().toString(), arguments[1] + " is now an administrator. reloaded the configuration.");
                } else {
                    Utils.sendNotice(e.getUser().toString(), arguments[1] + " is already an admin!");
                }
            } else {
                e.getBot().sendIRC().notice(e.getUser().toString(), "Usage: addowner <name>");
            }
        } else {
            sendNotice(e.getUser().toString(), perms);
        }
    }

    public static void delteOwner(MessageEvent e) throws ConfigurationException, IOException {
        if (Utils.isAdmin(e.getUser().getNick())) {
            String[] arguments = e.getMessage().split(" ");
            if (arguments.length == 2) {
                if (!Utils.isAdmin(arguments[1])) {
                    Config.ADMINS.remove(arguments[1]);
                    String admins = "";
                    for (String s : Config.ADMINS) {
                        admins += s + " ";
                    }
                    Config.reload();
                    Config.getConfig().refresh();
                    Utils.sendNotice(e.getUser().toString(), arguments[1] + " is now an administrator. reloaded the configuration.");
                } else {
                    Utils.sendNotice(e.getUser().toString(), arguments[1] + " is already an admin!");
                }
            } else {
                e.getBot().sendIRC().notice(e.getUser().toString(), "Usage: addowner <name>");
            }
        } else {
            sendNotice(e.getUser().toString(), perms);
        }
    }

    public static void kick(MessageEvent e) {
            String[] args = e.getMessage().split(" ");
            if (args.length <= 2) {
                User u = e.getBot().getUser(args[1]);
                if (e.getChannel().isOp(u) || Utils.isAdmin(e.getUser().getNick()) || e.getChannel().hasVoice(e.getUser())) {
                     e.getChannel().send().kick(u, "Get the fuck out of here " + u.getNick());
                }
            }
            if (args.length >= 3) {
                User u = e.getBot().getUser(args[1]);
                if (!e.getChannel().isOp(u) && !e.getChannel().hasVoice(u)) {
                    StringBuilder sb = new StringBuilder();
                    String[] arguments = e.getMessage().split(" ");
                    for (int i = 2; i < arguments.length; i++) {
                        sb.append(arguments[i]).append(" ");
                    }
                    String allArgs = sb.toString().trim();
                    e.getChannel().send().kick(u, allArgs);
                }
        }
    }
    public static void sendNotice(String user, String notice) {
        Bot.bot.sendIRC().notice(user, notice);
    }

    public static void ignore(MessageEvent e) {
        String[] args = e.getMessage().split(" ");
        if (Utils.isAdmin(e.getUser().getNick())  ||  e.getChannel().getOps().contains(e.getUser())) {
            if (args.length == 2) {
                User user = e.getBot().getUser(args[1]);
                if (!Bot.ignored.contains(user.getHostmask())) {
                    Bot.ignored.add(user.getHostmask());
                    e.respond(user.getNick() + " was added to the ignore list.");
                } else {
                    e.respond(user.getNick() + " is already in the ignore list");
                }
            } else {
                sendNotice(e.getUser().toString(), "usage: $ignore user");
            }
        } else {
            sendNotice(e.getUser().toString(), Commands.perms);
        }
    }

    public static void unignore(MessageEvent e) {
        if (Utils.isAdmin(e.getUser().getNick()) || e.getChannel().hasVoice(e.getUser()) || e.getChannel().isOp(e.getUser())) {
        String[] args = e.getMessage().split(" ");
        PropertiesConfiguration def = Config.customcmd;
        String word = args[1];
        StringBuilder builder = new StringBuilder();
        if (args.length >= 3) {
            try {
                for (int i = 2; i < args.length; i++) {
                    builder.append(args[i]).append(" ");
                    String s = "\\";
                }

                String allargs = builder.toString().trim().replaceAll(",", "\\\\,");

                if (!def.containsKey(word.toLowerCase())) {
                    def.setProperty(word.toLowerCase(), allargs);
                }
                if (def.containsKey(word.toLowerCase())) {
                    def.clearProperty(word.toLowerCase());
                    def.setProperty(word.toLowerCase(), allargs);
                }
                def.save();
                e.getBot().sendIRC().notice(e.getUser().toString(), "command " + word + " set to " + Utils.colorEncode(allargs.replaceAll("\\\\,", ",")));
            }
            catch (ConfigurationException ex) {
                Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            e.getBot().sendIRC().notice(e.getUser().toString(), "Not enough arguments!");
        }
    } else {
    sendNotice(e.getUser().toString(), perms);
        }
    }

    public static void eat(MessageEvent e) {
        String eaten = e.getMessage().split("eat")[1];
        e.getBot().sendIRC().message(e.getChannel().toString(), e.getUser().getNick() + " has eaten" + eaten);
    }

    public static void setDebug(MessageEvent e) {
        boolean debug = Boolean.valueOf(e.getMessage().split(" ")[1]);
        if (Utils.isAdmin(e.getUser().getNick())) {
            e.getBot().setVerbose(debug);
            e.getBot().sendIRC().notice(e.getUser().toString(), "debug set to " + String.valueOf(debug));
        }
    }
    
    public static void getCommand(MessageEvent e) {
        if (Config.customcmd.containsKey(Bot.curcmd)) {
            String commandpre = Config.customcmd.getString(e.getMessage().substring(1));
            String cmd = commandpre.replaceAll("color.red", Colors.RED).replaceAll("color.green", Colors.GREEN).replaceAll("colors.red", Colors.RED).replaceAll("colors.bold", Colors.BOLD).replaceAll("colors.normal", Colors.NORMAL);
            if (e.getMessage().startsWith(Config.PUBLIC_IDENTIFIER)) {
                e.getBot().sendIRC().message(e.getChannel().toString(), cmd);
            } else {
                e.getBot().sendIRC().notice(e.getUser().toString(), cmd);
            }
        }
    }

    public static void setCommand(MessageEvent e){
        String[] args = e.getMessage().split(" ");
        if (e.getChannel().getVoices().contains(e.getUser()) || e.getChannel().getOps().contains(e.getUser()) || Utils.isAdmin(e.getUser().getNick())) {
        PropertiesConfiguration def = Config.customcmd;
        String word = args[1];
        StringBuilder sb = new StringBuilder();
        if (args.length >= 3) {
            try {
                for (int i = 2; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                String allarg = sb.toString().trim();
                String allargs = allarg.replaceAll("", "");
                if (!def.containsKey(word)) {
                    def.setProperty(word.toLowerCase(), allargs);
                }
                if (def.containsKey(word)) {
                    def.clearProperty(word);
                    def.setProperty(word.toLowerCase(), allargs);
                }
                def.save();
                e.getBot().sendIRC().notice(e.getUser().toString(), "command " + word + " set to " + allargs.replaceAll("color.green", Colors.GREEN).replaceAll("color.red", Colors.RED).replaceAll("color.bold", Colors.BOLD).replaceAll("color.reset", Colors.NORMAL).replaceAll("color.darkgreen", Colors.DARK_GREEN));
            } catch (ConfigurationException ex) {
                Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            e.getBot().sendIRC().notice(e.getUser().toString(), "Not enough arguments!");
        }
        } else {
            e.respond(perms);
        }
    }

    public static void deleteCommand(MessageEvent e){
        if (Utils.isAdmin(e.getUser().getNick()) || e.getChannel().hasVoice(e.getUser()) || e.getChannel().isOp(e.getUser())) {
            String[] args = e.getMessage().split(" ");
            String word = args[1];
            if (args.length >= 2) {
                try {
                    Config.customcmd.clearProperty(word);
                    Config.customcmd.save();
                    Config.customcmd.refresh();
                    e.getBot().sendIRC().notice(e.getUser().toString(), "command " + word + " deleted");
                } catch (ConfigurationException ex) {
                    Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } else {
                e.getBot().sendIRC().notice(e.getUser().toString(), "Not enough arguments!");
            }
        } else {
            e.respond(perms);
        }
    }

    public static void cycle(MessageEvent e) {
        if (Utils.isAdmin(e.getUser().getNick()) || e.getChannel().hasVoice(e.getUser()) || e.getChannel().isOp(e.getUser())) {
        String[] args = e.getMessage().split(" ");
        if (args.length == 1) {
            String chan = e.getChannel().getName();
            e.getChannel().send().part();
            e.getBot().sendIRC().joinChannel(chan);
        } else {
            String chan = args[1];
            if (!e.getBot().getUserBot().getChannels().contains(chan)) {
                e.getBot().sendIRC().notice(e.getUser().toString(), "I'm not in that channel!");
            }
            e.getBot().partChannel(e.getBot().getChannel(chan));
            e.getBot().sendIRC().joinChannel(chan);
        }
    } else {
            sendNotice(e.getUser().toString(), perms);
        }
    }

    public static void sendRaw(MessageEvent e) {
        if (Utils.isAdmin(e.getUser().getNick())) {
            StringBuilder sb = new StringBuilder();
            String[] args = e.getMessage().split(" ");
            for (int i = 1; i < args.length; i++) {
                sb.append(args[i] + " ");
            }
            e.getBot().sendRaw().rawLineNow(sb.toString().trim());
        }
    }

    public static void login(MessageEvent e) {
        e.getBot().sendIRC().identify(Config.PASSWORD);
    }

    public static void encrypt(MessageEvent e) {
        StringBuilder sb = new StringBuilder();
        String[] args = e.getMessage().split(" ");
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i] + " ");
        }
        String s = Utils.encrypt(sb.toString().trim());
        if (e.getMessage().startsWith(Config.PUBLIC_IDENTIFIER)) {
            e.respond("ENCRYPTED STRING: " + s);
        } else {
            Utils.sendNotice(e.getUser().toString(), "ENCRYPTED STRING: " + s);
        }        
    }

    public  static void spam(MessageEvent e){
      if (Utils.isAdmin(e.getUser().getNick())) {
        String[] args = e.getMessage().split(" ");
    	  String target = args[1];
    	  int count = Integer.parseInt(args[2]);
    	  String msg = "";
    	  for(int i = 3; i < args.length; i++){
    		  msg += args[i]+" ";
    	  }
    	  for(int i =0; i < count; i++){
    		  e.getBot().sendIRC().message(target, msg.trim());
    	  }
      } else {      
          e.respond(perms);
      }    
    }
	public static void ping(MessageEvent e) {
		String returns = "";
		Long time = Long.valueOf("0");
		try{
		String[] args = e.getMessage().split(" ");
		if(!(args.length == 3)){
			e.respond("Invalid syntax!");
			return;
		}
		String host = args[1];
		int port = Integer.valueOf(args[2]);
		
		Long start = System.currentTimeMillis();
		Socket s = new Socket(InetAddress.getByName(host),port);
		s.close();
		time = System.currentTimeMillis() - start;
		returns = "Response time: " + time + " milliseconds";
		}catch(Exception ex){
			//ex.printStackTrace();
			returns = ex.toString();
		}
		e.getBot().sendIRC().message(e.getChannel().toString(), returns);
	}

	public static void spy(MessageEvent e) {
            if (Utils.isAdmin(e.getUser().getNick()) || e.getChannel().hasVoice(e.getUser()) || e.getChannel().isOp(e.getUser())) {
		String[] args = e.getMessage().split(" ");
		Channel spychan = e.getBot().getChannel(args[1]);
		Channel relayto = e.getChannel();
		if(Bot.relay.containsKey(spychan)){
			Bot.relay.remove(spychan);
			e.getBot().sendIRC().message(e.getChannel().toString(), "no longer spying on channel " + spychan.getName());
			return;
		}
		Bot.relay.put(spychan, relayto);
		e.getBot().sendIRC().message(e.getChannel().toString(), "now spying on channel " + spychan.getName());
            } else {
            sendNotice(e.getUser().toString(), perms);
        } 
}

    public static void execute(final MessageEvent event) {
        //please for the love of god don't touch this line.
        if (Config.EXEC_ADMINS.contains(Utils.getAccount(event.getUser()))) {
            final String[] args = event.getMessage().split(" ");
            new Thread(new Runnable() {
                public void run() {
                    try {
                        interpreter.set("event", event);
                        interpreter.set("bot", event.getBot());
                        interpreter.set("chan", event.getChannel());
                        interpreter.set("user", event.getUser());
                        final StringBuilder builder = new StringBuilder();
                        for (int c = 1; c < args.length; c++) {
                            builder.append(args[c]).append(" ");
                        }
                        interpreter.eval(builder.toString().trim());
                    }
                    catch (Exception e) {
                        event.respond(e.getMessage());
                    }
                }
            }).start();
        }
        else {
            event.respond(Config.PERMISSIONS_DENIED);
        }
    }
    public static void skin(MessageEvent e){
		String[] args = e.getMessage().split(" ");
		if (args.length == 2) {
			User a = e.getBot().getUser(args[1]);
			e.respond("https://tntup.me/player/" + a.getNick() + "/128");
		}
		
	}
	
    public static void MCcheckAccount(MessageEvent e) {
        String[] args = e.getMessage().split(" ");
        if (args.length == 3) {
            String x = args[0];
            String y = x.replaceAll("<", "");
            String z = y.replaceAll(">", "");
            Boolean b = Utils.checkAccount(args[2]);
                if (b) {
               	e.getBot().sendIRC().message(e.getChannel().toString(), z + ": " + args[2] + Colors.GREEN + " has " + Colors.NORMAL + "paid for minecraft");
                } else {
                	e.getBot().sendIRC().message(e.getChannel().toString(), z + ": " + args[2] + Colors.RED + " has not " + Colors.NORMAL + "paid for minecraft");
                }
        }

    }
        
    public static void help(MessageEvent e) {
            String[] args = e.getMessage().split(" ");
            if (args.length == 1) {
                e.getBot().sendIRC().notice(e.getUser().toString(), "Chans, Cinsult, CRstatus, Cycle, Debug[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "], Deop[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "], DelCmd[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "], DeVoice[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "], Google, Gsay, Ignore[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "], Insult, Join[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "], Mcstatus, Murder[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "], Nick[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "], Op[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "]");
                e.getBot().sendIRC().notice(e.getUser().toString(), "Paid, Part, Query, Quiet[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "], Raw[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "],Request[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "], Reload[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "], Say, SetCmd[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "], Shorten, Skin, UnIgnore[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "], UnQuiet[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "], URL, Voice[" + Colors.YELLOW + "Admin" + Colors.NORMAL + "], Wiki, YouTube");
                e.getBot().sendIRC().notice(e.getUser().toString(), "------------------NOTES------------------");
                e.getBot().sendIRC().notice(e.getUser().toString(), Colors.RED + "[Not requied as of 12/13/2013] " + Colors.NORMAL+ "A '#' is always required in the channel name!  |  " + Config.PUBLIC_IDENTIFIER + "join " + Colors.GREEN + "#batman" + Colors.NORMAL + " | Not " + Config.PUBLIC_IDENTIFIER + "join " + Colors.RED + "batman");
                sendNotice(e.getUser().toString(), "The " + Config.PUBLIC_IDENTIFIER + "wiki command is still being worked on at the moment to support more Wikies.");
                sendNotice(e.getUser().toString(), "Not all commands are listed here :P ");
                sendNotice(e.getUser().toString(), "To people with Voice: Even if you aren't Admin for the bot, you do have access to few commands. Test to see which ones :3 ");
                sendNotice(e.getUser().toString(), "To pople with OP: You have access to most of the commands for the! ");
                e.getBot().sendIRC().notice(e.getUser().toString(), "--------------------------------------------");
            }
            if (args.length == 2) {
                String commandname = args[1];
                if (commandname.equalsIgnoreCase("Chans")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "Lists the channels the bot is currently in.  |  " + Config.PUBLIC_IDENTIFIER + "chans" );
                }
                if (commandname.equalsIgnoreCase("Cycle")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "Cycles the specified [" + Colors.MAGENTA + "#channel" + Colors.NORMAL + "], if no channel name is specified it will cycle the channel the command was sent from.  |  " + Config.PUBLIC_IDENTIFIER + "cycle OR " + Config.PUBLIC_IDENTIFIER + "cycle [" + Colors.MAGENTA + "#channel" + Colors.NORMAL + "]" );
                }
                if (commandname.equalsIgnoreCase("Debug")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin" + Colors.NORMAL + "]Sets the bot to debug (verbose) mode in system.out.  |  " + Config.PUBLIC_IDENTIFIER + "debug true/false" );
                }
                if (commandname.equalsIgnoreCase("Deop")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin" + Colors.NORMAL + "]Removes the specified [" + Colors.GREEN + "user" + Colors.NORMAL + "]'s operator status in the channel (Note: This DOES NOT take the op flags, just op.)  |  " + Config.PUBLIC_IDENTIFIER + "deop [" + Colors.GREEN + "user" + Colors.NORMAL + "]" );
                }
                if (commandname.equalsIgnoreCase("DeVoice")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin" + Colors.NORMAL + "]Removes voice from the specified [" + Colors.GREEN + "user" + Colors.NORMAL + "] (Note: This DOES NOT take the voice flags, just voice.)  |  " + Config.PUBLIC_IDENTIFIER + "devoice [" + Colors.GREEN + "user" + Colors.NORMAL + "]" );
                }
                if (commandname.equalsIgnoreCase("Google")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "Googles the specified query and returns the first result.  |  " + Config.PUBLIC_IDENTIFIER + "google [" + Colors.GREEN + "search" + Colors.NORMAL + "]" );
                }
                if (commandname.equalsIgnoreCase("Gsay")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin" + Colors.NORMAL + "]Sends a message to the specified [" + Colors.GREEN + "user" + Colors.NORMAL + "] or [" + Colors.MAGENTA + "#channel" + Colors.NORMAL + "]. (Note: Bot must be in the channel)  |  " + Config.PUBLIC_IDENTIFIER + "gsay [" + Colors.GREEN + "user" + Colors.NORMAL + "/" + Colors.MAGENTA + "#channel" + Colors.NORMAL + "] [message]" );
                }
                if (commandname.equalsIgnoreCase("Ignore")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin" + Colors.NORMAL + "]Ignore all commands from [" + Colors.GREEN + "user" + Colors.NORMAL + "]'s hostmask.  |  " + Config.PUBLIC_IDENTIFIER + "ignore [" + Colors.GREEN + "user" + Colors.NORMAL + "]" );
                }
                if (commandname.equalsIgnoreCase("Join")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin" + Colors.NORMAL + "]Joins the specified [" + Colors.MAGENTA + "#channel" + Colors.NORMAL + "]. Must start with '#' |  " + Config.PUBLIC_IDENTIFIER + "Join [" + Colors.MAGENTA + "#channel" + Colors.NORMAL + "]" );
                }
                if (commandname.equalsIgnoreCase("Murder")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin" + Colors.NORMAL + "]Immediately stops the bot.  |  " + Config.PUBLIC_IDENTIFIER + "Murder" );
                }
                if (commandname.equalsIgnoreCase("MCStatus")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "Tells you the status of the minecraft internal servers (auth, login, session, etc)  |  " + Config.PUBLIC_IDENTIFIER + "mcstatus" );
                }
                if (commandname.equalsIgnoreCase("Nick")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "Changes the bot's nickname.  |  " + Config.PUBLIC_IDENTIFIER + "nick [name]" );
                }
                if (commandname.equalsIgnoreCase("Op")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin" + Colors.NORMAL + "]Gives the specified [" + Colors.GREEN + "user" + Colors.NORMAL + "] operator status in the channel (Note: This DOES NOT give them the op flags, just op.)  |  " + Config.PUBLIC_IDENTIFIER + "op [" + Colors.GREEN + "user" + Colors.NORMAL + "]" );
                }
                if (commandname.equalsIgnoreCase("Part")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin" + Colors.NORMAL + "]Parts the specified channel. If no channel is given, It will part the channel the command was sent from.  |  " + Config.PUBLIC_IDENTIFIER + "part [" + Colors.MAGENTA + "#channel" + Colors.NORMAL + "]" );
                }
                if (commandname.equalsIgnoreCase("Paid")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "Tells you if [" + Colors.GREEN + "user" + Colors.NORMAL + "] has a paid minecraft account.  |  " + Config.PUBLIC_IDENTIFIER + "paid [" + Colors.GREEN + "user" + Colors.NORMAL + "]" );
                }
                if (commandname.equalsIgnoreCase("Uptime")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "Show the amount of time the bot has been running for.  |  " + Config.PUBLIC_IDENTIFIER + "uptime" );
                }
                if (commandname.equalsIgnoreCase("Query")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "Queries the specified minecraft server and returns the player count and MOTD.  |  " + Config.PUBLIC_IDENTIFIER + "query [serverIP] OR " + Config.PUBLIC_IDENTIFIER + "query [serverIP] [port]" );
                }
                if (commandname.equalsIgnoreCase("Quiet")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin" + Colors.NORMAL + "]Mutes the specified user by setting the +q flag on them.  |  " + Config.PUBLIC_IDENTIFIER + "quiet [user]" );
                }
                if (commandname.equalsIgnoreCase("Raw")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin" + Colors.NORMAL + "] Sends a raw line to the irc server.  |  Example: " + Config.PUBLIC_IDENTIFIER + "raw privmsg #chan :boo! --- Send a message to Channel '#chan' which says 'boo' --- '#chan' can be replaced by with user and privmsg can be replaced by variety of things" );
                }
                if (commandname.equalsIgnoreCase("Reload")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin" + Colors.NORMAL + "]Reloads the bot's configuration.  |  " + Config.PUBLIC_IDENTIFIER + "reload" );
                }
                if (commandname.equalsIgnoreCase("Say")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "Sends a message to the [" + Colors.MAGENTA + "#channel" + Colors.NORMAL + "] you're in.  |  " + Config.PUBLIC_IDENTIFIER + "say [message]" );
                }
                if (commandname.equalsIgnoreCase("Setcmd")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin" + Colors.NORMAL + "]Sets the custom command to the specified text. You can run said command as you would any other.  |  " + Config.PUBLIC_IDENTIFIER + "setcmd [" + Colors.BLUE + "command" + Colors.NORMAL + "] [" + Colors.GREEN + "text" + Colors.NORMAL + "] --- typing " + Config.PUBLIC_IDENTIFIER + "" + Colors.BLUE + "command" + Colors.NORMAL + " will say '" + Colors.GREEN + "text" + Colors.NORMAL + "'" );
                }
                if (commandname.equalsIgnoreCase("Unignore")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin" + Colors.NORMAL + "]Removes [" + Colors.GREEN + "user" + Colors.NORMAL + "]'s hostmask from the ignore list.  |  " + Config.PUBLIC_IDENTIFIER + "unignore [" + Colors.GREEN + "user" + Colors.NORMAL + "]" );
                }
                if (commandname.equalsIgnoreCase("UnQuiet")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin/Voice" + Colors.NORMAL + "]Un-Mutes the specified [" + Colors.GREEN + "user" + Colors.NORMAL + "] by setting the -q flag on them.  |  " + Config.PUBLIC_IDENTIFIER + "UnQuiet [" + Colors.GREEN + "user" + Colors.NORMAL + "]" );
                }
                if (commandname.equalsIgnoreCase("Voice")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin" + Colors.NORMAL + "]Gives the specified [" + Colors.GREEN + "user" + Colors.NORMAL + "] voice (Note: This DOES NOT give them the voice flags, just voice.)  |  " + Config.PUBLIC_IDENTIFIER + "Voice [" + Colors.GREEN + "user" + Colors.NORMAL + "]" );
                }
                if (commandname.equalsIgnoreCase("Skin")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "Gets the Minecraft skin for the specified " + Colors.GREEN + "username" + Colors.NORMAL + " (Must be In-game Minecraft name).  |  " + Config.PUBLIC_IDENTIFIER + "skin [Minecraft " + Colors.GREEN + "username" + Colors.NORMAL + "]" );
                }
                if (commandname.equalsIgnoreCase("Insult")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "Says a random shakespearean insult.  |  " + Config.PUBLIC_IDENTIFIER + "insult" );
                }
                if (commandname.equalsIgnoreCase("cinsult")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "(Channel Insult) Bot joins the specified channel and uses '" + Config.PUBLIC_IDENTIFIER + "insult' command.  |  " + Config.PUBLIC_IDENTIFIER + "cinsult [" + Colors.MAGENTA + "#channel" + Colors.NORMAL + "]" );
                }
                if (commandname.equalsIgnoreCase("DelCmd")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "[" + Colors.RED + "Admin/Voice" + Colors.NORMAL + "]Delete a custom command that was created previously.  |  " + Config.PUBLIC_IDENTIFIER + "delcmd [" + Colors.GREEN + "Custom Command Name" + Colors.NORMAL + "]");
                }
                if (commandname.equalsIgnoreCase("shorten")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "Shortens the " + Colors.GREEN + "url" + Colors.NORMAL + ".  |  " + Config.PUBLIC_IDENTIFIER + "shorten [" + Colors.GREEN + "url" +Colors.NORMAL + "]" );
                }
                if (commandname.equalsIgnoreCase("wiki")) {
                    e.getBot().sendIRC().notice(e.getUser().toString(), "Search Feed The Beast Wiki.  |  "  + Config.PUBLIC_IDENTIFIER + "wiki [" + Colors.GREEN + "Item/Block/Mod name" +Colors.NORMAL + "]" );
                }
                if (commandname.equalsIgnoreCase("youtube")) {
                    sendNotice(e.getUser().toString(), "Search YouTube with the specified " + Colors.GREEN + "query" + Colors.NORMAL + ".  |  " + Config.PUBLIC_IDENTIFIER + "youtube [" + Colors.GREEN + "query" + Colors.NORMAL + "]");
                }
                if (commandname.equalsIgnoreCase("request")) {
                    sendNotice(e.getUser().toString(), "[" + Colors.RED + "Admin/Voice" + Colors.NORMAL + "]Send a request note to the bot owner.  |  " + Config.PUBLIC_IDENTIFIER + "request [" + Colors.GREEN + "Note to send" + Colors.NORMAL + "]");
                }
                if (commandname.equalsIgnoreCase("Chstatus")){
                    sendNotice(e.getUser().toString(), "Tells you the status of the CreeperHost servers status (England, Los Angeles, Atlanta, etc)  |  " + Config.PUBLIC_IDENTIFIER + Colors.GREEN + "CHstatus");
                }
                if (commandname.equalsIgnoreCase("url")) {
                    sendNotice(e.getUser().toString(), "Gets the title for a " + Colors.GREEN + "Webpage URL" + Colors.NORMAL + ".  |  " + Config.PUBLIC_IDENTIFIER + "url [" + Colors.GREEN + "webpage URL" + Colors.NORMAL + "]");
                }
            }
                
        }
    public static void youtube(MessageEvent e) {
        StringBuilder sb = new StringBuilder();
            String[] args = e.getMessage().split(" ");
            
            for(int i = 1; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            
            String message = sb.toString().trim();
            String y = "http://www.youtube.com/results?search_query=" + message;
                        String x = y.replaceAll(" ", "%20");
            String finalurl = null;
            String nope = null;
            String no = "																	No results found.";
            try {
                URL wiki;
                wiki = new URL("http://is.gd/create.php?format=simple&url=" + x);
                BufferedReader br = new BufferedReader(new InputStreamReader(wiki.openStream()));
                finalurl = br.readLine();                      
                br.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            e.getBot().sendIRC().message(e.getChannel().toString(), message + ": " + finalurl);
    }
    
    public static void checkAdmin (MessageEvent e) {
        if (Utils.isAdmin(e.getUser().getNick()) || e.getChannel().hasVoice(e.getUser()) || e.getChannel().isOp(e.getUser())) {
            if (Utils.isAdmin(e.getUser().getNick())) {
            e.getBot().sendIRC().message(e.getChannel().toString(), "You are an Admin :D");
            }
            if (e.getChannel().hasVoice(e.getUser())) {
                e.getBot().sendIRC().message(e.getChannel().toString(), "You are not an Admin but have limited access because of Voice");
            }
            if (e.getChannel().isOp(e.getUser())) {
                e.getBot().sendIRC().message(e.getChannel().toString(), "You are not an Admin but because you have OP, you have access.");
            }
        } else {
            e.getBot().sendIRC().message(e.getChannel().toString(), e.getUser().getNick() + ": NO NO NO! NOT TODAY!");
        }
            
    }
    
    public static void java(MessageEvent e) {
        String[] args = e.getMessage().split(" ");
        if (args.length == 2) {
            e.getBot().sendIRC().notice(args[1], "64Bit http://javadl.sun.com/webapps/download/AutoDL?BundleId=81821");
            e.getBot().sendIRC().notice(args[1], "32Bit http://javadl.sun.com/webapps/download/AutoDL?BundleId=81819");
        }
    }
    
    public static void MacJava(MessageEvent e){
        String[] args = e.getMessage().split(" ");
        if (args.length == 2) {
            e.getBot().sendIRC().notice(args[1], "http://is.gd/oQWJOY");
        }
    }

    public static void CRstatus(final MessageEvent e) throws IOException {
            try {
                String Maidenhead = "Maidenhead: ";
                String Nottingham = "Nottingham: ";
                String Grantham = "Grantham: ";
                String Atlanta = "Atlanta: ";
                String Atlanta2 = "Atlanta-2: ";
                String Chicago = "Chicago: ";
                String Chicago2 = "Chicago-2: ";
                String LA = "Los Angeles: ";
                Socket socket = null;
                String maidencheck = null;
                String nottingcheck = null;
                String grantcheck = null;
                String atlantacheck = null;
                String atlanta2check = null;
                String chicagocheck = null;
                String chicago2check = null;
                String LAcheck = null;
                String P = null;
                boolean Maiden = false;
                boolean Notting = false;
                boolean Grant = false;
                boolean Atlant = false;
                boolean Atlant2 = false;
                boolean Chicag = false;
                boolean Chicag2 = false;
                boolean Los = false;
        //----------------------
                try {
                      socket = new Socket("england1.creeperrepo.net", 80);
                      Maiden = true;
                    } finally {            
                if (socket != null) try { socket.close(); }
                catch(IOException M) {}
                }
        //----------------------
                try {
                      socket = new Socket("england2.creeperrepo.net", 80);
                      Notting = true;
                    } finally {            
                if (socket != null) try { socket.close(); }
                catch(IOException M) {}
                }
        //----------------------
                try {
                      socket = new Socket("england2.creeperrepo.net", 80);
                      Grant = true;
                    } finally {            
                if (socket != null) try { socket.close(); }
                catch(IOException M) {}
                }
        //----------------------
                        try {
                      socket = new Socket("atlanta1.creeperrepo.net", 80);
                      Atlant = true;
                    } finally {            
                if (socket != null) try { socket.close(); }
                catch(IOException M) {}
                }
        //----------------------
                                try {
                      socket = new Socket("atlanta2.creeperrepo.net", 80);
                      Atlant2 = true;
                    } finally {            
                if (socket != null) try { socket.close(); }
                catch(IOException M) {}
                }
        //----------------------
                                        try {
                      socket = new Socket("chicago1.creeperrepo.net", 80);
                      Chicag = true;
                    } finally {            
                if (socket != null) try { socket.close(); }
                catch(IOException M) {}
                }
        //----------------------
                try {
                      socket = new Socket("chicago2.creeperrepo.net", 80);
                      Chicag2 = true;
                    } finally {            
                if (socket != null) try { socket.close(); }
                catch(IOException C1) {}
                }
        //----------------------
                        try {
                      socket = new Socket("losangeles1.creeperrepo.net", 80);
                      Los = true;
                    } finally {            
                if (socket != null) try { socket.close(); }
                catch(IOException LS) {}
                }
        //----------------------
                   
               if (Maiden) {
                   maidencheck = Colors.DARK_GREEN + "✔" + Colors.NORMAL + " | ";
               } else {
                   maidencheck = Colors.RED + "✘ " + Colors.NORMAL + " | ";
               }
               if (Notting) {
                   nottingcheck = Colors.DARK_GREEN + "✔" + Colors.NORMAL + " | ";
               } else {
                   nottingcheck = Colors.RED + "✘ " + Colors.NORMAL + " | ";
               }
               if (Grant) {
                   grantcheck = Colors.DARK_GREEN + "✔" + Colors.NORMAL + " | ";
               } else {
                   grantcheck = Colors.RED + "✘ " + Colors.NORMAL + " | ";
               }
               if (Atlant) {
                   atlantacheck = Colors.DARK_GREEN + "✔" + Colors.NORMAL + " | ";
               } else {
                   atlantacheck = Colors.RED + "✘ " + Colors.NORMAL + " | ";
               }
               if (Atlant2) {
                   atlanta2check = Colors.DARK_GREEN + "✔" + Colors.NORMAL + " | ";
               } else {
                   atlanta2check = Colors.RED + "✘ " + Colors.NORMAL + " | ";
               }
               if (Chicag) {
                   chicagocheck = Colors.DARK_GREEN + "✔" + Colors.NORMAL + " | ";
               } else {
                   chicagocheck = Colors.RED + "✘ " + Colors.NORMAL + " | ";
               }
               if (Chicag2) {
                   chicago2check = Colors.DARK_GREEN + "✔" + Colors.NORMAL + " | ";
               } else {
                   chicago2check = Colors.RED + "✘ " + Colors.NORMAL + " | ";
               }
               if (Los) {
                   LAcheck = Colors.DARK_GREEN + "✔" + Colors.NORMAL + " | ";
               } else {
                   LAcheck = Colors.RED + "✘ " + Colors.NORMAL + " | ";
               }
                      P = Maidenhead 
                          + maidencheck 
                          + Nottingham 
                          + nottingcheck 
                          + Grantham 
                          + grantcheck 
                          + Atlanta 
                          + atlantacheck 
                          + Atlanta2 
                          + atlanta2check 
                          + Chicago
                          + chicagocheck 
                          + Chicago2 
                          + chicago2check 
                          + LA
                          + LAcheck
                              ;
                      e.getBot().sendIRC().message(e.getChannel().toString(), P);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
    }

    public static void add(MessageEvent e) throws ParseException {
        String[] args = e.getMessage().split(" ");
        if (e.getChannel().getVoices().contains(e.getUser()) || e.getChannel().getOps().contains(e.getUser()) || Utils.isAdmin(e.getUser().getNick())) {
        PropertiesConfiguration def = Config.add;
        String word = args[1];
        StringBuilder sb = new StringBuilder();
        String oldstring = "2011-01-18 00:00:00.0";
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(oldstring);
        String newstring = new SimpleDateFormat("yyyy-MM-dd").format(date);
        
        
        if (args.length >= 2) {
            try {
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                String allarg = sb.toString().trim();
                String allargs = allarg.replaceAll("", "");
                if (!def.containsKey(word)) {
                    def.setProperty(newstring+""+e.getUser().getNick(), allargs);
                    e.getBot().sendIRC().message(newstring + e.getUser().getNick(), allargs);
                    
                }
                if (def.containsKey(word)) {
                    def.clearProperty(word);
                    def.setProperty(newstring+""+e.getUser().getNick(), allargs);
                    e.getBot().sendIRC().message(newstring + e.getUser().getNick(), allargs);
                    
                }
                def.save();
                e.getBot().sendIRC().notice(e.getUser().toString(),"You request was added :D");
            } catch (ConfigurationException ex) {
                Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
      }
    }
    public static void remove(MessageEvent e) {
        if (Utils.isAdmin(e.getUser().getNick()) || e.getChannel().hasVoice(e.getUser()) || e.getChannel().isOp(e.getUser())) {
            String[] args = e.getMessage().split(" ");
            String date = args[1];
            String name = args[2];
            if (args.length >= 3) {
                try {
                    Config.add.clearProperty(date+name);
                    Config.add.save();
                    Config.add.refresh();
                    e.getBot().sendIRC().notice(e.getUser().toString(), "Request from  " + date + " was deleted.");
                } catch (ConfigurationException ex) {
                    Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } else {
                e.getBot().sendIRC().notice(e.getUser().toString(), "Not enough arguments!");
            }
    
        }
    }
    public static void imgtfy (MessageEvent e) {
        String[] args = e.getMessage().split(" ");
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            
            String message = sb.toString().trim();
            String y = "http://lmgtfy.com/?q=" + message;
            String x = y.replaceAll(" ", "+");
            e.respond(Utils.shortenUrl(x));
    }
    
    public static void allsay(MessageEvent e) {
    String t = e.getChannel().getName();
    Set<Channel> chan = e.getBot().getChannels();
    System.out.println(chan);
    System.out.println(t);
    }
}