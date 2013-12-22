package com.progwml6.ircbot;

import static com.progwml6.ircbot.Commands.perms;

import java.awt.BorderLayout;

import javax.swing.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.InviteEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.PartEvent;


@SuppressWarnings("rawtypes")
public class Bot extends ListenerAdapter{

    public static List<String> owners = new ArrayList<String>();
    public static PircBotX bot;
    public static String prefix = "!";
    private static List<String> allowed;
    public static List<String> ignored = new ArrayList<String>();
    public static List<String> users = new ArrayList<String>();
    public static HashMap<String,Integer> violation = new HashMap<String, Integer>();
    public static String curcmd;
    public static HashMap<Channel,Channel> relay = new HashMap<Channel, Channel>(0);
    public static void main(String[] args){    
        start();
    }

    
    public static void start() {
        try {
            Config.loadConfig();
            Configuration configuration = new Configuration.Builder()
            .setName(Config.NICK) //Set the nick of the bot. CHANGE IN YOUR CODE
            .setLogin(Config.IDENT) //login part of hostmask, eg name:login@host
            .setNickservPassword(Config.PASSWORD)
            .setAutoNickChange(true) //Automatically change nick when the current one is in use
            .setCapEnabled(true) //Enable CAP features
            .addListener(new Bot()) //This class is a listener, so add it to the bots known listeners
            .setServerHostname(Config.SERVER)
            .addAutoJoinChannel("#batbot") //Join the official #pircbotx channel
            .buildConfiguration();

            bot = new PircBotX(null);

            System.out.println(String.format("=======\nSETTINGS\n=======\nBOT-NICKNAME: %s\nBOT-IDENT: %s\nIDENTIFY-WITH-NICKSERV: %s\nVERIFY ADMIN NICKNAMES: %s\nAWAY: %s\nNOTICE: %s\nNOTICE IDENTIFIER: %s\nPUBLIC_IDENTIFIER: %s\n=======\nSETTINGS\n=======\n\n\n", Config.NICK, Config.IDENT, Config.IDENTIFY_WITH_NICKSERV, Config.VERIFY_ADMIN_NICKS,Config.AWAY, Config.NOTICE, Config.NOTICE_IDENTIFIER, Config.PUBLIC_IDENTIFIER));
           // bot.setVersion("Batbot V1.8 [Original by zack6849]");
            //bot.setFinger("oh god what are you doing");
           // bot.setVerbose(Config.DEBUG_MODE);
            bot.startBot();
           // if(Config.IDENTIFY_WITH_NICKSERV){
            //	 bot.identify(Config.PASSWORD);
           // }
            allowed = Config.ADMINS;
            for (String s : Config.CHANS) {
                bot.joinChannel(s);
                System.out.println("Joined channel " + s);
            }
            //bot.getListenerManager().addListener(new Bot());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onPart(PartEvent e){
        String nick = e.getUser().getNick();
        String Channel = e.getChannel().getName();
        String test = String.format("%s has left the channel #%s",
                nick, 
                Channel);
        System.out.println(test);
    }
    @Override
    public void onPrivateMessage(PrivateMessageEvent e) {
        if(Utils.isAdmin(e.getUser().getNick())) {
            String[] args = e.getMessage().split(" ");
            String commandname = args[0];
            if (args.length == 2) {
                
        		User x = e.getBot().getUser(args[0]);
        		if (commandname.equalsIgnoreCase("join")) {
        			 String join = args[1];
                                 e.getBot().joinChannel(join);
        		} else {
                            e.respond(perms);
                        }
            }
            if (commandname.equalsIgnoreCase("rejoin")){
                for (String s : Config.CHANS) {
                bot.joinChannel(s);
                System.out.println("Joined channel " + s);
            }
            }
        }
    }
    
    @Override
    public void onJoin(JoinEvent e) {
        String nick = e.getUser().getNick();
        String Channel = e.getChannel().getName();
        String test = String.format("%s has joined the channel %s",
                nick, 
                Channel);
        System.out.println(test);
    }
    
    @Override
    public void onMessage(MessageEvent e) throws IOException, ParseException, InterruptedException {
        String message = e.getMessage();
        String nick = e.getUser().getNick();
        String Channel = e.getChannel().getName();
        String test = String.format("[%s] %s: %s",
                Channel, 
                nick, 
                message);
        System.out.println(test);
        String command = CheckCommand(e);
        curcmd = command;
        String title = null;
        if (!e.getChannel().isOp(e.getUser()) && !e.getChannel().hasVoice(e.getUser())) {
            checkSpam(e);
        }
        if(ignored.contains(e.getUser().getHostmask())){
    		return;
    	}
        String[] words = e.getMessage().split(" ");
        for (int i = 0; i < words.length; i++) {
            if (Utils.isUrl(words[i]) && !command.contains("shorten") && !words[i].contains("youtube") && !curcmd.contains("setcmd")) {
//                try {
//                    title = Utils.getWebpageTitle(words[i]);
//                    String msg = String.format("%s's url title: %s", e.getUser().getNick(), title);
//                    e.getBot().sendMessage(e.getChannel(), msg);
//                } catch (Exception ex1) {
//                    ex1.printStackTrace();
//                }

            }
            if (Utils.isUrl(words[i]) && !command.contains("shorten") && words[i].toLowerCase().contains("youtube") && !command.equalsIgnoreCase("ping")) {
//                try {
//                    e.getBot().sendMessage(e.getChannel(), "[YouTube] " + Utils.getYoutubeInfo(words[i]));
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
            }
        }
        if(relay.containsKey(e.getChannel())){
        	bot.sendMessage(relay.get(e.getChannel()), "[" + e.getChannel().getName() + "] <" + e.getUser().getNick() + "> " + e.getMessage());
        }
        if (e.getMessage().startsWith(Config.PUBLIC_IDENTIFIER) || e.getMessage().startsWith(Config.NOTICE_IDENTIFIER)) {
            if (ignored.contains(e.getUser().getHostmask())) {
                Commands.sendNotice(e.getUser(), "Sorry, you've been ignored by the bot.");
                return;
            }
            
            String test1 = String.format("%s issued command %s in channel %s",
                            nick, 
                            message, 
                            Channel);
            System.out.println(test1);
            parseCommands(e);
            //System.out.println("Parsing commands!");
            Commands.getCommand(e);
        }
        
        
        if (e.getMessage().startsWith("prefix")){
        	Config.PUBLIC_IDENTIFIER = "!";
        }
        if (e.getMessage().startsWith("am i admin?") || e.getMessage().startsWith("am i admin") || e.getMessage().startsWith("Am i admin?") || e.getMessage().startsWith("Am i admin")){
        	Commands.checkAdmin(e);
        }
    }
    
    public void parseCommands(MessageEvent e) throws ParseException, IOException, InterruptedException {
        String command = curcmd;

        if (command.equalsIgnoreCase("cycle")) {
            Commands.cycle(e);
        }
        if (command.equalsIgnoreCase("help")) {
            Commands.help(e);
        }
        if (command.equalsIgnoreCase("insult")) {
            Commands.insult(e);
        }
        
        if (command.equalsIgnoreCase("cinsult")) {
            try {
                Commands.cinsult(e);
            } catch (InterruptedException ex) {
            }
        }
        
        if (command.equalsIgnoreCase("uptime")) {
            Commands.uptime(e);
        }
        
        if (command.equalsIgnoreCase("System")) {
            Commands.getSystemUptime(e);
        }
        if (command.equalsIgnoreCase("Notice")) {
            Commands.notice(e);
        }
        
        if (command.equalsIgnoreCase("wiki")) {
            Commands.wiki(e);
        }
        
        if (command.equalsIgnoreCase("irc")) {
        	bot.sendMessage(e.getChannel(), "http://webchat.esper.net/?nick=&channels=flakeynoobzone") ;
        }
        
        if (command.equalsIgnoreCase("kick")) {
            Commands.kick(e);
        }
        if (command.equalsIgnoreCase("skin")) {
        	Commands.skin(e);
        }
        if(command.equalsIgnoreCase("spam")) {
        	Commands.spam(e);
        }
        if (command.equalsIgnoreCase("raw")) {
            Commands.sendRaw(e);
        }
        if(command.equalsIgnoreCase("prefix")){
        	Commands.setPrefix(e);
        }
        if (command.equalsIgnoreCase("debug")) {
            Commands.setDebug(e);
        }
        if (command.equalsIgnoreCase("listops")) {
            Commands.listOperators(e);
        }
        if (command.equalsIgnoreCase("join")) {
            Commands.joinChannel(e);
        }
        if(command.equalsIgnoreCase("exec")){
        	Commands.execute(e);
        }
        if (command.equalsIgnoreCase("delay")) {
            Commands.setDelay(e);
        }
        if (command.equalsIgnoreCase("reqf")) {
            Commands.sendFile(e);
        }
        if (command.equalsIgnoreCase("gsay")) {
            Commands.globalSay(e);
        }
        if (command.equalsIgnoreCase("say")) {
            Commands.say(e);
        }
        if (command.equalsIgnoreCase("clense")) {
            Commands.clense(e);
        }
        if (command.equalsIgnoreCase("part")) {
            Commands.partChannel(e);
        }
        if (command.equalsIgnoreCase("eat")) {
            Commands.eat(e);
        }
        if (command.equalsIgnoreCase("nope")) {
            Commands.nope(e);
        }
        if (command.equalsIgnoreCase("nick")) {
            Commands.changeNickName(e);
        }
        if (command.equalsIgnoreCase("chans")) {
            Commands.listChannels(e);
        }
        if (command.equalsIgnoreCase("paid")) {
            Commands.checkAccount(e);
        }
        if (command.equalsIgnoreCase("mcstatus")) {
            Commands.checkMojangServers(e);
        }
        if(command.equalsIgnoreCase("spy")){
        	Commands.spy(e);
        }
        if (command.equalsIgnoreCase("google")) {
            Commands.google(e);
        }
        if (command.equalsIgnoreCase("query")) {
            Commands.checkServerStatus(e);
        }
        if (command.equalsIgnoreCase("murder")) {
            Commands.kill(e);
        }
        if (command.equalsIgnoreCase("op")) {
            Commands.op(e);
        }
        if (command.equalsIgnoreCase("reload")) {
            e.getBot().sendMessage(e.getChannel(), "Configuration reloaded!");
            try {
                Config.reload();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        if (command.equalsIgnoreCase("deop")) {
            Commands.deop(e);
        }
        if(command.equalsIgnoreCase("ping")){
        	Commands.ping(e);
        }
        if (command.equalsIgnoreCase("voice")) {
            Commands.voice(e);
        }
        if (command.equalsIgnoreCase("devoice")) {
            Commands.deVoice(e);
        }
        if (command.equalsIgnoreCase("quiet")) {
            Commands.quiet(e);
        }
        if (command.equalsIgnoreCase("unquiet")) {
            Commands.unquiet(e);
        }
        if (command.equalsIgnoreCase("stack")) {
            //
        }
        if (command.equalsIgnoreCase("login")) {
            Commands.login(e);
        }
        if (command.equalsIgnoreCase("ignore")) {
            Commands.ignore(e);
        }
        if (command.equalsIgnoreCase("unignore")) {
            Commands.unignore(e);
        }
        if (command.equalsIgnoreCase("shorten")) {
            Commands.shortenUrl(e);
        }
        if (command.equalsIgnoreCase("setcmd")) {
            Commands.setCommand(e);
        }
        if (command.equalsIgnoreCase("invalid")) {
            e.respond("Invalid command!");
        }
        if (command.equalsIgnoreCase("admin")) {
            try {
                Commands.addOwner(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        if (command.equalsIgnoreCase("listfiles")) {
            Commands.listFiles(e);
        }
        if (command.equalsIgnoreCase("sha")) {
            Commands.encrypt(e);
        }
        if (command.equalsIgnoreCase("delcmd")) {
            Commands.deleteCommand(e);
        }
        
        if (command.equalsIgnoreCase("me")) {
            Commands.sendAction(e);
        }
        if (command.equalsIgnoreCase("startwith")) {
            Commands.startwith(e);
        }
        if (command.equalsIgnoreCase("op?")) {
            Commands.checkAdmin(e);
        }
        if (command.equalsIgnoreCase("java")) {
            Commands.java(e);
        }
        if (command.equalsIgnoreCase("macjava")){
            Commands.MacJava(e);
        }
        if (command.equalsIgnoreCase("chstatus")) {
            Commands.CRstatus(e);
        }
        if (command.equalsIgnoreCase("request")){
            Commands.add(e);
        }
        if (command.equalsIgnoreCase("reqremove")) {
            Commands.remove(e);
        }
        if (command.equalsIgnoreCase("lmgtfy")) {
            Commands.imgtfy(e);
        }
        if (command.equalsIgnoreCase("url")) {
            Bot.findUrl(e);
        }
        if (command.equalsIgnoreCase("allsay")) {
            Commands.allsay(e);
        }
        if (command.equalsIgnoreCase("usage")) {
            Commands.usage(e);
        }
    }
    
    public String CheckCommand(MessageEvent e) {
        String[] args = e.getMessage().split(" ");
        if(args.length == 0){
        	return "invalid";
        }
        String cmd1 = args[0].substring(1);
        return cmd1;
    }
	public static String findUrl(MessageEvent e) throws MalformedURLException, IOException{
		String msg = null;
		String title = null;
		String[] words = e.getMessage().split(" ");
		for(int i = 0; i < words.length; i++){
			if(Utils.isUrl(words[i])){
				title = Utils.getWebpageTitle(words[i]);
			}
		}
		msg = String.format("%s's url title: %s", e.getUser().getNick(), title);
                e.respond(msg);
		return msg;
	}

    public static List<String> getAccessList() {
        return allowed;
    }
    public static void refreshAcessList() {
        try {
            allowed.clear();
            URL u;
            u = new URL("https://dl.dropbox.com/u/49928817/bot_users.txt");
            BufferedReader b = new BufferedReader(new InputStreamReader(u.openConnection().getInputStream()));
            String[] names = b.readLine().toLowerCase().split(" ");
            b.close();
            allowed.addAll(Arrays.asList(names));
        } catch (Exception e) {
            User u = bot.getUser("zack6849");
            bot.sendNotice(u, "Error whilst fetching acess list! " + e.getCause() + " " + e.getMessage());
        }
    }
    @Override
	public void onInvite(InviteEvent e){
    	if(Config.ACCEPT_INVITES){
    		e.getBot().joinChannel(e.getChannel());
    	}
    }
    private static void sendNotice(User user, String notice) {
        Bot.bot.sendNotice(user, notice);
    }
    
    public void checkSpam(final MessageEvent e){
    	new Thread(new Runnable(){
    		@Override
    		public void run(){
    			//System.out.println("entered run");
    			try{
    				if(!users.contains(e.getUser().getNick())){
    					if(violation.containsKey(e.getUser().getNick())){
    						violation.put(e.getUser().getNick(), (Integer) violation.get(e.getUser().getNick()) + 1);
    					}else{
    						violation.put(e.getUser().getNick(), 0);
    					}
    					users.add(e.getUser().getNick());
    					//System.out.println("Added to list.");
    					Thread.sleep(750);
    					//System.out.println("Removed from list.");
    					users.remove(e.getUser().getNick());
    				}else{
    					if((Integer) violation.get(e.getUser().getNick()) > 3){
    						e.getBot().kick(e.getChannel(), e.getUser(), "Calm your tits bro.");
    						violation.put(e.getUser().getNick(), 0);
    						return;
    					}
    					//System.out.println("User already in list. muting");	
    					e.getBot().setMode(e.getChannel(), "+q ", e.getUser().getHostmask());
    					users.remove(e.getUser().getNick());
    					//System.out.println("muted.");
    					Utils.sendNotice(e.getUser(), "You've been muted temporarily for spam.");
    					Thread.sleep(1000 * 10);
    					//System.out.println("unmuted");
    					e.getBot().setMode(e.getChannel(), "-q ", e.getUser().getHostmask());
    				}
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    		}
    	}).start();
    }

    
}
