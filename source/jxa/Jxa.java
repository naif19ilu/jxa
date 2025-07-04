/*
 * jxa - Java executable arguments parser
 * Jul 4, 2025
 * Actual parser
 */

package jxa;

final class Fatal
{
	public static void duplicatedId (int first, int second, JxaFlag[] flags)
	{
		final String msg = String.format(
			"jxa::error: duplicated id!\n" +
			"  both '%s' and '%s' flags share same id '%c'\n",
			flags[first].getLongName(),
			flags[second].getLongName(),
			flags[second].getShortName()
		);
		System.err.print(msg);
		System.exit(1);
	}
}

public final class Jxa
{
	private static final int[] ids = new int[26 + 26 + 10];
	
	public static void parse (String[] args, JxaFlag[] flags)
	{
		checkShortNames(flags);
	}
	
	private static void checkShortNames (JxaFlag[] flags)
	{
		for (int i = 0; i < flags.length; i++)
		{
			final char id = flags[i].getShortName();
			final int key = getIdKey(id);
			
			if (ids[key] != 0)
			{
				Fatal.duplicatedId(ids[key] - 1, i, flags);
			}
			
			ids[key] = i + 1;
			System.out.println(key + " " + ids[key]);

		}
	}
	
	private static int getIdKey (char shortName)
	{
		if (Character.isDigit(shortName)) { return shortName - '0'; }
		if (Character.isLowerCase(shortName)) { return shortName - 'a' + 10; }
		return shortName - 'A' + 36;
	}
}

// -d --document