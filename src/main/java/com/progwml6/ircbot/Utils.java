/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progwml6.ircbot;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.events.WhoisEvent;


import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author zack6849(zcraig29@gmail.com)
 */
public class Utils {

    public static String GOOGLE_API_KEY = "AIzaSyC-tr8bq9mzLn9i11ioxCrXgPaAH3Wi2pA";

    /**
     * @param user the user object to send the notice too
     * @param notice the string to notice the user with
     */
    public static void sendNotice(String user, String notice) {
        Bot.bot.sendIRC().notice(user, notice);
    }

    public static boolean isAdmin(String s) {
      if(Config.ADMINS.contains(s) && Bot.bot.getUser(s).isVerified()){
    	  return true;
      }else{
    	  return false;
      }
    }
    
    public static boolean isMCServer(String s) {
      if(Config.MCSERVER.contains(s)&& Bot.bot.getUser(s).isVerified()) {
    		return true;
      }else{
    	return false;
      }
    }
    
    public static String htmlFormat(String s) {
        System.out.println(String.valueOf(new File("c:/").exists()));
        return s.replaceAll("<b>", "").replace("</b>", "").replace("&#39;", "'").replaceAll("&quot;", "'").replaceAll("   ", " ").replaceAll("&amp;", "&");
    }

    public static String removeBrackets(String s) {
        return s.replaceAll("[\\['']|['\\]'']", "");
    }

    /**
     * @param user the Minecraft username to check
     * @return returns a boolean depending upon if he username has paid or not
     * (note: accounts that do not exists return false too)
     */
    public static boolean checkAccount(String user) {
        boolean paid = false;
        try {
            URL url = new URL("https://minecraft.net/haspaid.jsp?user=" + user);
//            System.out.println("URL for user id is : " + url);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str = in.readLine();
            in.close();
//            System.out.println("str =" + str);
            if (str != null) {
                paid = Boolean.valueOf(str);
            }
        } catch (java.io.IOException e1) {
        }
        return paid;
    }
    public static String getAccount(User u){
    	String returns = "";
    	try {
    		Bot.bot.sendRawLine("WHOIS " + u.getNick() + " " + u.getNick());
    		WhoisEvent event = Bot.bot.waitFor(WhoisEvent.class);
    		String tmp = event.getRegisteredAs();
    		if(tmp != null){
    			returns = tmp;
    		}
    	} catch (InterruptedException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return returns;
    }
    	

    /*
     * @return a string with the status.
     */
    public static String checkMojangServers() {
        String returns = null;
        try {
            URL url;
            url = new URL("http://status.mojang.com/check");
            BufferedReader re = new BufferedReader(new InputStreamReader(url.openStream()));
            String st;
            while ((st = re.readLine()) != null) {
                String a = st.replace("red", Colors.RED + "Offline" + Colors.NORMAL).replace("green", Colors.GREEN + "Online" + Colors.NORMAL).replace("[", "").replace("]", "");
                String b = a.replace("{", "").replace("}", "").replace(":", " is currently ").replace("\"", "").replace("minecraft.net", "Minecraft").replace("login.Minecraft", "Login").replace("session.Minecraft", "Session").replace("account.mojang.com", "Account").replace("auth.mojang.com", "Auth").replace("skins.minecraft.net", "Skins").replace("authserver.mojang.com", "Auth").replace("sessionserver.mojang.com", "Session").replaceAll(",", " ");
                returns = b;
            }
            re.close();
        } catch (IOException E) {
            if (E.getMessage().contains("503")) {
                returns = "The minecraft status server is temporarily unavailable, please try again later";
            }
            if (E.getMessage().contains("404")) {
                returns = "Uhoh, it would appear as if the haspaid page has been removed or relocated >_>";
            }
            
        }
        return returns;
    }

    /**
     *
     * @param longUrl the URL to shorten
     * @return the shortened URL
     */
    public static String shortenUrl(String longUrl) {
        String shortened = null;
        try {
            URL u;
            u = new URL("http://is.gd/create.php?format=simple&url=" + longUrl);
            BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
            shortened = br.readLine();
            br.close();
        } catch (Exception e) {
        }
        return shortened;
    }

    /**
     * @param s the query to google
     * @return the first result from google
     */
        public static String google(String s) {
        try {
            String temp = String.format("https://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=%s", URLEncoder.encode(s));
            URL u = new URL(temp);
            URLConnection c = u.openConnection();
            System.out.println("url = " + u);
            c.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.57 Safari/537.17");
            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String json = "";
            String tmp = "";
            while ((tmp = in.readLine()) != null) {
                json += tmp + "\n";
                //System.out.println(tmp);
            }
            in.close();
            Gson gson = new Gson();
            JsonElement jelement = new JsonParser().parse(json);
            JsonObject output = jelement.getAsJsonObject();
            output = output.getAsJsonObject("responseData").getAsJsonArray("results").get(0).getAsJsonObject();
            String result = String.format("Google: %s | %s | (%s)", StringEscapeUtils.unescapeHtml(output.get("titleNoFormatting").toString().replaceAll("\"", "")), StringEscapeUtils.unescapeHtml(output.get("content").toString().replaceAll("\\s+", " ").replaceAll("\\<.*?>", "").replaceAll("\"", "")), output.get("url").toString().replaceAll("\"", ""));

            if (result != null) {
                return result;
            }
            else {
                return "No results found for query " + s;
            }
        }
        catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String checkServerStatus(InetAddress i, int port) {
        String returns = "Error.";
        try {
            Socket s = new Socket(i, 25565);
            DataInputStream SS_BF = new DataInputStream(s.getInputStream());
            DataOutputStream d = new DataOutputStream(s.getOutputStream());
            d.write(new byte[]{(byte) 0xFE, (byte) 0x01});
            SS_BF.readByte();
            short length = SS_BF.readShort();
            StringBuilder sb = new StringBuilder();
            for (int in = 0; in < length; in++) {
                char ch = SS_BF.readChar();
                sb.append(ch);
            }
            String all = sb.toString().trim();
            System.out.println(all);
            String[] args1 = all.split("\u0000");
            if (args1[3].contains("Ã‚Â§")) {
                returns = "MOTD: " + args1[3].replaceAll("Ã‚Â§[a-m]", "").replaceAll("Ã‚Â§[1234567890]", "") + "   players: [" + args1[4] + "/" + args1[5] + "]";
            } else {
                returns = "MOTD: " + args1[3] + "   players: [" + args1[4] + "/" + args1[5] + "]";
            }
        } catch (UnknownHostException e1) {
            returns = "the host you specified is unknown. check your settings.";
        } catch (IOException e1) {
            returns = "sorry, we couldn't reach this server, make sure that the server is up and has query enabled.";
        }
        return returns;
    }
    
    public static String colorEncode(String s) {
        return s.replaceAll("color.reset", Colors.NORMAL).replaceAll("color.bold", Colors.BOLD).replaceAll("color.underline", Colors.UNDERLINE).
                replaceAll("color.reverse", Colors.REVERSE).replaceAll("color.red", Colors.RED).replaceAll("color.green", Colors.GREEN).replaceAll("color.blue", Colors.BLUE).
                replaceAll("color.yellow", Colors.YELLOW).replaceAll("color.cyan", Colors.CYAN).replaceAll("color.gray", Colors.LIGHT_GRAY).replaceAll("color.darkgreen", Colors.DARK_GREEN).
                replaceAll("colors.teal", Colors.TEAL);
    }

    public static String encrypt(String s) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest alg = MessageDigest.getInstance("SHA-512");
            alg.reset();
            alg.update(s.getBytes(Charset.forName("UTF-8")));

            byte[] digest = alg.digest();
            for (byte b : digest) {
                sb.append(Integer.toHexString(0xFF & b));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getYoutubeInfo(String s) throws IOException {
        String info;
        String title = null;
        String likes = null;
        String dislikes = null;
        String user = null;
        String views = null;
        String publishdate = null;
        String duration = null;
        Document doc = Jsoup.connect(s).userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.57 Safari/537.17").get();
        for (Element e : doc.select("a")) {
            if (e.attr("class").equalsIgnoreCase("yt-uix-sessionlink yt-user-videos")) {
                user = e.attr("href").split("/user/")[1].split("/")[0];
            }
        }
        for (Element e : doc.select("span")) {
            if (e.attr("class").equalsIgnoreCase("watch-view-count ")) {
                views = e.text();
            }
            if (e.attr("class").equalsIgnoreCase("likes-count")) {
                likes = e.text();
            }
            if (e.attr("class").equalsIgnoreCase("dislikes-count")) {
                dislikes = e.text();
            }
            if (e.attr("class").equalsIgnoreCase("watch-title  yt-uix-expander-head") || e.attr("class").equalsIgnoreCase("watch-title long-title yt-uix-expander-head")) {
                title = e.text();
            }
            if (e.attr("class").equalsIgnoreCase("watch-video-date")) {
                publishdate = e.text();
            }
            if (e.attr("class").equalsIgnoreCase("duration")) {
                duration = e.text();
            }
        }
        info = title + "  [" + Colors.DARK_GREEN + views + Colors.NORMAL +"]  [" + Colors.GREEN + "+" + likes + Colors.NORMAL + "]  [" + Colors.RED + "-" + dislikes + Colors.NORMAL + "]  [" + Colors.BLUE + user + Colors.NORMAL + " - " + Colors.CYAN + publishdate + Colors.NORMAL +"]";
        //System.out.println(info);
        return info;
    }

    public static String getWebpageTitle(String s) {
        String title = "";
        String error = "none!";
        try {
            String content = new URL(s).openConnection().getContentType();
            if (!content.toLowerCase().contains("text/html")) {
                return "content type: " + content + " size: " + new URL(s).openConnection().getContentLength() / 1024 + "kb";
            }
            Document doc = Jsoup.connect(s).userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17").followRedirects(true).get();
            URLConnection c = new URL(s).openConnection();

            Elements links = doc.select("title");
            for (Element e1 : links) {
                title += e1.text().replaceAll("\n", "").replaceAll("\\s+", " ");
            }
        } catch (Exception e) {
            error = e.toString();
        }
        if (!error.equalsIgnoreCase("none")) {
            if (error.contains("404")) {
                return "404 file not found";
            }
            if (error.contains("502")) {
                return "502 bad gateway";
            }
            if (error.contains("401")) {
                return "401 unauthorized request";
            }
            if (error.contains("403")) {
                return "403 forbidden";
            }
            if (error.contains("500")) {
                return "500 internal server error";
            }
            if (error.contains("503")) {
                return "503 service unavailable (usually temporary, try again later)";
            }
        }
        return title;
    }

    public static boolean isUrl(String s) {
        String url_regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
//        String url_regex2 = "[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern p = Pattern.compile(url_regex);
//        Pattern q = Pattern.compile(url_regex2);
        Matcher m = p.matcher(s);
//        Matcher n = q.matcher(s);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    public static String munge(String s) {
        StringBuilder sb = new StringBuilder();
        String[] normal = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        String[] munge = {"ÃƒÂ¤", "Ã�â€˜", "Ã„â€¹", "Ã„â€˜", "ÃƒÂ«", "Ã†â€™", "Ã„Â¡", "Ã„Â§", "ÃƒÂ­", "Ã„Âµ", "Ã„Â·", "Ã„Âº", "Ã¡Â¹ï¿½", "ÃƒÂ±", "ÃƒÂ¶", "Ã�ï¿½", "ÃŠÂ ", "Ã…â€”", "Ã…Â¡", "Ã…Â£", "ÃƒÂ¼", "v", "Ã�â€°", "Ã�â€¡", "ÃƒÂ¿", "Ã…Âº", "Ãƒâ€¦", "ÃŽâ€™", "Ãƒâ€¡", "Ã„Å½", "Ã„â€™", "Ã¡Â¸Å¾", "Ã„Â ", "Ã„Â¦", "Ãƒï¿½", "Ã„Â´", "Ã„Â¶", "Ã„Â¹", "ÃŽÅ“", "ÃŽï¿½", "Ãƒâ€“", "Ã�Â ", "Q", "Ã…â€“", "Ã…Â ", "Ã…Â¢", "Ã…Â®", "Ã¡Â¹Â¾", "Ã…Â´", "ÃŽÂ§", "Ã¡Â»Â²", "Ã…Â»"};
        String[] replace = s.split("");
        for (String s1 : replace) {
            for (int i = 0; i < normal.length; i++) {
                if (s1.equalsIgnoreCase(normal[i])) {
                    s1 = munge[i];
                }
                sb.append(s1);
            }
        }
        return sb.toString();
    }
}
