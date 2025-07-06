/*
 * jxa - Java executable arguments parser
 * Jul 4, 2025
 * Tester file
 */

import jxa.*;

public class Tester
{
	public static void main(String[] args)
	{
		final JxaFlag[] flags =
		{
			new JxaFlag("document", 'd', JxaFlag.FlagArg.YES, "specifies document"),
			new JxaFlag("usage",    'u', JxaFlag.FlagArg.MAY, "prints program's usage or flag's usage if indicated"),
			new JxaFlag("verbose",  'v', JxaFlag.FlagArg.NON, "displays operations as they happen")
		};
		Jxa.parse(args, flags);

		System.out.println("flags:");
		for (int i = 0; i < flags.length; i++)
		{
			System.out.println(flags[i].getLongName() + ":" + flags[i].getSeen() + " > " + flags[i].getArgument());
		}

		System.out.println("-*-\npos args:");
		for (int i  = 0; i < Jxa.posArguments.size(); i++)
		{	
			System.out.println(Jxa.posArguments.get(i));
		}
		
		JxaDoc.printUsage("jxa", flags);
	}
}

