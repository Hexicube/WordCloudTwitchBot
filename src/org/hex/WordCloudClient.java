package org.hex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class WordCloudClient implements Runnable
{
	private Socket s;
	
	public WordCloudClient(Socket s)
	{
		this.s = s;
	}
	
	@Override
	public void run()
	{
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			while(true)
			{
				String inLine = in.readLine();
				if(inLine == null || inLine.equals("")) break;
			}
			PrintWriter out = new PrintWriter(s.getOutputStream());
			out.println("HTTP/1.1 200 OK");
			out.println("Connection: close");
			out.println("");
			out.println("<!DOCTYPE html>");
			out.println("<html><head><title>Twitch WordCount Test</title></head><body>");
			ArrayList<Word> words = IRCBot.getWordIntensities();
			for(Word w : words)
			{
				if(w.count > 1) out.println("<div><font size="+w.count+"px>"+w.word+"</font></div>");
			}
			out.println("<script>setTimeout(function(){location.reload(true)}, 3000)</script></body></html>");
			out.println("");
			out.flush();
			s.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}