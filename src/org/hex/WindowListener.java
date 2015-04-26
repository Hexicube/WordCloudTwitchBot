package org.hex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class Listener extends WindowAdapter implements ActionListener
{
	public void windowClosing(WindowEvent e)
	{
		if(IRCBot.out != null)
			IRCBot.out.println("PART "+IRCBot.channel+" :Closed!");
		try{Thread.sleep(100);}catch (InterruptedException e1){}
		System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		if(arg0.getSource().equals(IRCBot.consoleInput))
		{
			String command = arg0.getActionCommand();
			if(command.equals("")) return;
			if(command.startsWith("/me "))
			{
				IRCBot.out.println("PRIVMSG "+IRCBot.channel+" ACTION "+command.substring(4)+"");
				IRCBot.printText(IRCBot.username+" "+command.substring(4));
			}
			else if(command.startsWith("/join "))
			{
				if(IRCBot.channel != null)
				{
					IRCBot.out.println("PART "+IRCBot.channel);
				}
				IRCBot.channel = command.substring(6);
				IRCBot.out.println("JOIN "+IRCBot.channel);
			}
			else if(command.startsWith("/nick "))
			{
				IRCBot.out.println("NICK "+command.substring(6));
			}
			else if(command.startsWith("/notice "))
			{
				IRCBot.out.println("NOTICE "+command.substring(8));
			}
			else if(command.startsWith("/msg "))
			{
				IRCBot.out.println("PRIVMSG "+command.substring(4));
				command = command.substring(5);
				int index = command.indexOf(" ");
				IRCBot.printText("<PRIVATE> "+command.substring(0, index)+": "+command.substring(index+1));
			}
			else if(command.startsWith("/kick "))
			{
				IRCBot.out.println("KICK "+IRCBot.channel+" "+command.substring(6));
			}
			else if(command.startsWith("/voice "))
			{
				String name = command.substring(7);
				IRCBot.out.println("MODE "+IRCBot.channel+" +v "+name);
			}
			else if(command.startsWith("/devoice "))
			{
				String name = command.substring(9);
				IRCBot.out.println("MODE "+IRCBot.channel+" -v "+name);
			}
			else if(command.startsWith("/"))
			{
				IRCBot.printText("Bad command!");
			}
			else IRCBot.say(command);
			IRCBot.consoleInput.setText("");
		}
	}
}