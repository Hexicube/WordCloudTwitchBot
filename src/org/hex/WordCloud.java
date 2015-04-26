package org.hex;

import java.io.IOException;
import java.net.ServerSocket;

public class WordCloud implements Runnable
{
	@Override
	public void run()
	{
		try
		{
			@SuppressWarnings("resource")
			ServerSocket ss = new ServerSocket(80);
			while(true)
			{
				new Thread(new WordCloudClient(ss.accept())).start();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}