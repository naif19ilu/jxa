/*
 * jxa - Java executable arguments parser
 * Jul 4, 2025
 * Actual parser
 */

package jxa;

public final class Jxa
{
	private JxaFlag[] ids = new JxaFlag[26 + 26 + 10];
	
	public static void parse (String[] args, JxaFlag[] flags)
	{
		checkShortNames(flags);
		
	}
	
	private static void checkShortNames (JxaFlag[] flags)
	{
		for (int i = 0; i < flags.length; i++)
		{
			System.out.println(getPosInMap(flags[i].getShortName()));
		}
	}
	
	private static int getPosInMap (char shortName)
	{
		if (Character.isDigit(shortName)) { return shortName - '0'; }
		if (Character.isLowerCase(shortName)) { return shortName - 'a' + 10; }
		else { return shortName - 'A' + 36; }
	}
}

