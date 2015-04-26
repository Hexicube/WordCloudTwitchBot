package org.hex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;

public class IRCBot
{
	public static Socket sock;
	public static BufferedReader in;
	public static PrintWriter out;
	
	public static JEditorPane console;
	public static JTextField consoleInput;
	public static JFrame mainFrame;
	public static JScrollPane scrolltxt;
	
	public static File lastDraggedFile;
	
	public static String consoleText = "";
	
	public static boolean testRoom = false;
	
	public static Date time = new Date();
	
	public static String username = "hexicube"; //PUT USERNAME IN HERE
	public static String password = "oauth:xpbhml99l02nd1crp2mr3ecbqfr3rg"; //PUT OATH KEY IN HERE
	public static String channel = null;
	
	public static ArrayList<Message> messages = new ArrayList<Message>();
	
	public static void printText(String text)
	{
		/*time = new Date();
		@SuppressWarnings("deprecation")
		String hour = String.valueOf(time.getHours());
		if(hour.length() == 1) hour = "0" + hour;
		@SuppressWarnings("deprecation")
		String minute = String.valueOf(time.getMinutes());
		if(minute.length() == 1) minute = "0" + minute;
		String timeStamp = "["+hour+":"+minute+"] ";*/
		String timeStamp = "";
		
		text = text.replace("â€‹", "");
		System.out.println(timeStamp+text);
		text = text.replace("&", "&amp;");
		text = text.replace("<", "&lt;");
		text = text.replace(">", "&gt;");
		text = text.replace(" ", "&nbsp;");
		
		String thingsToTerminate = "";
		
		boolean isBold = false;
		boolean isUnderlined = false;
		int curColour = -1;
		for(int a = 0; a < text.length(); a++)
		{
			if(text.charAt(a) == 31)
			{
				if(isUnderlined)
				{
					isUnderlined = false;
					text = text.substring(0, a) + thingsToTerminate + text.substring(a + 1);
					a += thingsToTerminate.length();
					thingsToTerminate = "";
					if(isBold)
					{
						text = text.substring(0, a) + "<b>" + text.substring(a);
						a += 3;
						thingsToTerminate = "</b>" + thingsToTerminate;
					}
					if(curColour != -1)
					{
						String colourCode = "";
						if(curColour == 0) colourCode = "FFFFFF";
						else if(curColour == 1) colourCode = "000000";
						else if(curColour == 2) colourCode = "3535B3";
						else if(curColour == 3) colourCode = "2A8C2A";
						else if(curColour == 4) colourCode = "C33B3B";
						else if(curColour == 5) colourCode = "C73232";
						else if(curColour == 6) colourCode = "802680";
						else if(curColour == 7) colourCode = "66361F";
						else if(curColour == 8) colourCode = "D9A641";
						else if(curColour == 9) colourCode = "3DCC3D";
						else if(curColour == 10) colourCode = "195555";
						else if(curColour == 11) colourCode = "2E8C74";
						else if(curColour == 12) colourCode = "4545E6";
						else if(curColour == 13) colourCode = "B037B0";
						else if(curColour == 14) colourCode = "4C4C4C";
						else if(curColour == 15) colourCode = "959595";
						text = text.substring(0, a) + "<font color = #"+colourCode+">" + text.substring(a);
						thingsToTerminate += "</font>";
						a += 22;
					}
				}
				else
				{
					text = text.substring(0, a) + "<u>" + text.substring(a + 1);
					a += 2;
					thingsToTerminate = "</u>" + thingsToTerminate;
					isUnderlined = true;
				}
			}
			else if(text.charAt(a) == 2)
			{
				if(isBold)
				{
					isBold = false;
					text = text.substring(0, a) + thingsToTerminate + text.substring(a + 1);
					a += thingsToTerminate.length();
					thingsToTerminate = "";
					if(isUnderlined)
					{
						text = text.substring(0, a) + "<u>" + text.substring(a);
						a += 3;
						thingsToTerminate = "</u>" + thingsToTerminate;
					}
					if(curColour != -1)
					{
						String colourCode = "";
						if(curColour == 0) colourCode = "FFFFFF";
						else if(curColour == 1) colourCode = "000000";
						else if(curColour == 2) colourCode = "3535B3";
						else if(curColour == 3) colourCode = "2A8C2A";
						else if(curColour == 4) colourCode = "C33B3B";
						else if(curColour == 5) colourCode = "C73232";
						else if(curColour == 6) colourCode = "802680";
						else if(curColour == 7) colourCode = "66361F";
						else if(curColour == 8) colourCode = "D9A641";
						else if(curColour == 9) colourCode = "3DCC3D";
						else if(curColour == 10) colourCode = "195555";
						else if(curColour == 11) colourCode = "2E8C74";
						else if(curColour == 12) colourCode = "4545E6";
						else if(curColour == 13) colourCode = "B037B0";
						else if(curColour == 14) colourCode = "4C4C4C";
						else if(curColour == 15) colourCode = "959595";
						text = text.substring(0, a) + "<font color = #"+colourCode+">" + text.substring(a);
						thingsToTerminate += "</font>";
						a += 22;
					}
				}
				else
				{
					text = text.substring(0, a) + "<b>" + text.substring(a + 1);
					a += 2;
					thingsToTerminate = "</b>" + thingsToTerminate;
					isBold = true;
				}
			}
			else if(text.charAt(a) == 15)
			{
				text = text.substring(0, a) + thingsToTerminate + text.substring(a + 1);
				a += thingsToTerminate.length() - 1;
				thingsToTerminate = "";
				isBold = false;
				isUnderlined = false;
				curColour = -1;
			}
			else if(text.charAt(a) == 3)
			{
				//colour code
				int colour = 1;
				try
				{
					colour = Integer.parseInt(text.substring(a + 1, a + 3));
				}
				catch (NumberFormatException e) {}
				curColour = colour;
				String colourCode = "";
				if(colour == 0) colourCode = "FFFFFF";
				else if(colour == 1) colourCode = "000000";
				else if(colour == 2) colourCode = "3535B3";
				else if(colour == 3) colourCode = "2A8C2A";
				else if(colour == 4) colourCode = "C33B3B";
				else if(colour == 5) colourCode = "C73232";
				else if(colour == 6) colourCode = "802680";
				else if(colour == 7) colourCode = "66361F";
				else if(colour == 8) colourCode = "D9A641";
				else if(colour == 9) colourCode = "3DCC3D";
				else if(colour == 10) colourCode = "195555";
				else if(colour == 11) colourCode = "2E8C74";
				else if(colour == 12) colourCode = "4545E6";
				else if(colour == 13) colourCode = "B037B0";
				else if(colour == 14) colourCode = "4C4C4C";
				else if(colour == 15) colourCode = "959595";
				text = text.substring(0, a) + "<font color = #"+colourCode+">" + text.substring(a + 3);
				thingsToTerminate += "</font>";
				a += 17;
			}
		}
		text += thingsToTerminate;
		
		String[] temp = text.split("&nbsp;");
		text = "";
		for(int a = 0; a < temp.length; a++)
		{
			if(temp[a].indexOf("://") != -1)
				temp[a] = "<a href=" + temp[a] + ">"+temp[a]+"</a>";
			text += temp[a];
			if(a+1 != temp.length) text += " ";
		}
		
		consoleText += "<br>" + timeStamp + text;
		temp = consoleText.split("<br>");
		consoleText = "";
		int len = (temp.length > 50?50:temp.length);
		for(int a = 0; a < len; a++)
		{
			consoleText += temp[temp.length - len + a];
			if(a+1 != len) consoleText += "<br>";
		}
		console.setText("<html><body style=\"font-family: Consolas; font-size:20pt;\">"+consoleText+"</body></html>");
		Insets i = IRCBot.mainFrame.getInsets();
		Dimension d = IRCBot.mainFrame.getSize();
		console.setBounds(2, 2, d.width - i.left - i.right - 24, console.getText().split("\n").length * console.getFontMetrics(console.getFont()).getHeight());
	}
	
	public static void main(String[] args)
	{
		Font f = new Font("Consolas", Font.PLAIN, 32);
		mainFrame = new JFrame("HexBot");
		mainFrame.setUndecorated(true);
		mainFrame.setBackground(new Color(0, 0, 0, 0));
		//mainFrame.setAlwaysOnTop(true);
		mainFrame.setVisible(true);
		mainFrame.setLayout(null);
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		Listener l = new Listener();
		mainFrame.addWindowListener(l);
		console = new JEditorPane();
		console.setContentType("text/html");
		console.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent arg0) {
				if(arg0.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
				{
					java.net.URI uri = null;
					try {
						String target = arg0.getDescription();
						uri = new java.net.URI(target);
					} catch (URISyntaxException e) {}
					java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
	                try {
						desktop.browse(uri);
					} catch (IOException e) {}
				}
			}
		  });
		
		consoleText = "--- HexBot V1 ---";
		console.setText("<html><body style=\"font-family: Consolas; font-size:8pt;\">"+consoleText+"</body></html>");
		console.setEditable(false);
		((DefaultCaret)console.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrolltxt = new JScrollPane(console);
		mainFrame.add(scrolltxt);
		consoleInput = new JTextField();
		consoleInput.addActionListener(l);
		consoleInput.setFont(f);
		mainFrame.add(consoleInput);
		try
		{
			Thread.sleep(100);
		}
		catch (InterruptedException e) {}
		mainFrame.setBounds(5, 5, 360, 758);
		scrolltxt.setBounds(0, 0, 360, 726);
		console.setBounds(0, 0, scrolltxt.getWidth() - 16, console.getText().split("\n").length * console.getFontMetrics(console.getFont()).getHeight());
		consoleInput.setBounds(0, 728, 360, 30);
		try {
			printText("Connecting...");
			while(true)
			{
				try
				{
					sock = new Socket("irc.twitch.tv", 6667);
					break;
				}
				catch (UnknownHostException e1)
				{
					printText("Connection failed (no host): "+e1.getLocalizedMessage());
					try{Thread.sleep(5000);}catch(InterruptedException e2){}
					printText("Retrying...");
				}
				catch (ConnectException e1)
				{
					printText("Connection failed (connect): "+e1.getLocalizedMessage());
					try{Thread.sleep(5000);}catch(InterruptedException e2){}
					printText("Retrying...");
				}
			}
			BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);
			out.println("PASS "+password);
			out.println("NICK "+username);
			boolean connected = false;
			String pingValue = "";
			new Thread(new WordCloud()).start();
			while(true)
			{
				while(!in.ready())
				{
					if(sock.isClosed() || !sock.isConnected()) return;
					else if(systemIn.ready())
					{
						say(systemIn.readLine());
					}
					else
					{
						Thread.sleep(100);
					}
				}
				String inLine = in.readLine();
				if(inLine.startsWith("PING :"))
				{
					pingValue = inLine.substring(6);
					out.println("PONG :"+pingValue);
					if(!connected) connected = true;
				}
				else if(inLine.startsWith("ERROR :Closing Link:"))
				{
					printText("LINK CLOSED!");
					connected = false;
					sock.close();
					break;
				}
				else
				{
					String from = inLine.substring(0, inLine.indexOf(" "));
					inLine = inLine.substring(inLine.indexOf(" ")+1);
					if(from.startsWith(":")) from = from.substring(1);
					String fromIP = "";
					if(from.contains("!"))
					{
						fromIP = from.substring(from.indexOf("!")+1);
						if(fromIP.contains("@")) fromIP = fromIP.substring(fromIP.indexOf("@")+1);
					}
					if(from.contains("!")) from = from.substring(0, from.indexOf("!"));
					
					if(!inLine.contains(" ")) inLine += " ";
					String commandName = inLine.substring(0, inLine.indexOf(" "));
					inLine = inLine.substring(inLine.indexOf(" ")+1);
					
					String commandString = inLine;
					if(commandString.startsWith(":")) commandString = commandString.substring(1);
					
					int num = -1;
					try
					{
						 num = Integer.parseInt(commandName);
					}
					catch (NumberFormatException e)
					{
						num = -1;
					}
					
					if(num != -1)
					{
						if(num == 353) {}
						else if(num == 366) {}
						else if(num == 372)
						{
							String[] commandString2 = commandString.split("Â");
							commandString = "";
							for(int a = 0; a < commandString2.length; a++) commandString += commandString2[a];
							printText("["+commandName+"] "+commandString.substring(commandString.indexOf(":")+1));
						}
						else printText("["+commandName+"] "+commandString.substring(commandString.indexOf(":")+1));
					}
					else if(commandName.equals("NOTICE"))
					{
						printText("[NOTICE] "+from+": "+commandString.substring(commandString.indexOf(":")+1));
					}
					else if(commandName.equals("ERROR"))
					{
						printText("[ERROR]"+from+": "+commandString);
					}
					else if(commandName.equals("JOIN"))
					{
						System.out.println("[JOIN] "+commandString);
					}
					else if(commandName.equals("KICK"))
					{
						String kicked = commandString.substring(commandString.indexOf(" ")+1);
						if(kicked.equals(username))
						{
							connected = false;
							sock.close();
							break;
						}
					}
					else if(commandName.equals("MODE"))
					{
						System.out.println("[MODE] "+commandString);
					}
					else if(commandName.equals("NICK"))
					{
						if(from.equals(username)) username = commandString;
					}
					else if(commandName.equals("PART"))
					{
						System.out.println("[PART] "+commandString);
					}
					else if(commandName.equals("PRIVMSG"))
					{
						String server = commandString.substring(0, commandString.indexOf(" "));
						String message = commandString.substring(commandString.indexOf(" ")+1);
						if(message.startsWith(":")) message = message.substring(1);
						if(message.startsWith("VERSION"))
						{
							say("VERSION Java IRCBot V1", from);
						}
						else
						{
							if(message.startsWith("ACTION "))
							{
								String command = message.substring(8, message.length() - 1);
								if(server.equalsIgnoreCase(username))
								{
									printText("[PRIVATE] "+from+" "+command);
								}
								else
								{
									printText(from+" "+command);
									addMessage(command);
								}
							}
							else
							{
								if(server.equalsIgnoreCase(username))
								{
									printText("[PRIVATE] "+from+": "+message);
								}
								else
								{
									printText(from+": "+message);
									addMessage(message);
								}
							}
						}
					}
					else if(commandName.equals("QUIT"))
					{
						System.out.println("[QUIT] "+commandString);
					}
					else if(commandName.equals("TOPIC"))
					{
						System.out.println("[TOPC] "+commandString);
					}
					else
					{
						printText("[UNKNOWN: "+commandName+"]"+from+": "+commandString);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void say(String message)
	{
		say(message, channel);
	}
	public static void say(String message, String to)
	{
		printText(username+": "+message);
		out.println("PRIVMSG "+to+" :"+message);
	}
	public static void addMessage(String message)
	{
		long time = System.nanoTime();
		while(messages.size() > 0)
		{
			if(time - messages.get(0).time > 30000000000L) messages.remove(0);
			else break;
		}
		Message m = new Message();
		//m.text = message.replaceAll("[^a-zA-Z0-9\\s]", "");
		m.text = message.replaceAll("[,.!?:] ", " ");
		m.time = System.nanoTime();
		messages.add(m);
	}
	
	public static ArrayList<Word> getWordIntensities()
	{
		ArrayList<Word> wordList = new ArrayList<Word>();
		long time = System.nanoTime();
		int pos = 0;
		while(pos < messages.size())
		{
			Message m = messages.get(pos);
			long timeDiff = time - m.time;
			if(timeDiff > 30000000000L) messages.remove(pos);
			else
			{
				String[] words = m.text.split(" ");
				for(String word : words)
				{
					boolean found = false;
					for(Word w : wordList)
					{
						if(w.word.equalsIgnoreCase(word))
						{
							w.count++;
							found = true;
							break;
						}
					}
					if(!found)
					{
						Word w = new Word();
						w.word = word;
						w.count = 1;
						wordList.add(w);
					}
				}
				pos++;
			}
		}
		return wordList;
	}
}