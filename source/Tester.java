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
			new JxaFlag("document", '9', JxaFlag.FlagArg.YES),
			new JxaFlag("usage",    'a', JxaFlag.FlagArg.MAY),
			new JxaFlag("verbose",  'Z', JxaFlag.FlagArg.NON)
		};
		Jxa.parse(args, flags);
	}
}

