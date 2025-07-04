/*
 * jxa - Java executable arguments parser
 * Jul 4, 2025
 * Actual parser
 */

package jxa;

import java.util.List;
import java.util.ArrayList;

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
	
	public static void invalidId (JxaFlag flag)
	{
		final String msg = String.format(
			"jxa::error: invalid id provided\n" +
			"  the '%s' flag has an invalid id (%c)\n",
			flag.getLongName(),
			flag.getShortName()
		);
		System.err.print(msg);
		System.exit(1);
	}
	
	public static void doubleSingleDash ()
	{
		final String msg =
			"jxa::error: double single dash option provided\n" +
		     "  the '-' option can only be used once\n";
		System.err.print(msg);
		System.exit(1);
	}
	
	public static void undefinedFlag (String given)
	{
		final String msg = String.format(
			"jxa::error: undefined flag provided\n" +
			"  the program found '%s' as an argument ,':|\n",
			given
		);
		System.err.print(msg);
		System.exit(1);
	}
}

public final class Jxa
{
	public static String readFromStdin;
	public static List<String> posArguments = new ArrayList<>();
	
	/* Saves the position where a certain id
	 * can be found within 'flags' array
	 */
	private static final int[] ids = new int[26 + 26 + 10];
	
	public static void parse (String[] args, JxaFlag[] flags)
	{
		checkShortNames(flags);
		
		boolean endOfArgs = false;
		
		for (int i = 0; i < args.length; i++)
		{
			final String arg = args[i];
			
			if (arg.equals("--"))
			{
				if (endOfArgs == false) { endOfArgs = true; continue; }
				posArguments.add(arg);
			}
			else if (arg.equals("-") && readFromStdin.isEmpty())
			{
				readFromStdin();
			}
			else if (arg.startsWith("--"))
			{
				handleLong();
			}
			else if (arg.startsWith("-"))
			{
				handleShort(arg);
			}
			
		}
	}
	
	private static void checkShortNames (JxaFlag[] flags)
	{
		for (int i = 0; i < flags.length; i++)
		{
			final char id = flags[i].getShortName();
			final int key = getIdKey(id);
			
			if (key == -1)
			{
				Fatal.invalidId(flags[i]);
			}
			if (ids[key] != 0)
			{
				Fatal.duplicatedId(ids[key] - 1, i, flags);
			}
			
			ids[key] = i + 1;
		}
	}
	
	private static int getIdKey (char shortName)
	{
		if (Character.isDigit(shortName))     { return shortName - '0'; }
		if (Character.isLowerCase(shortName)) { return shortName - 'a' + 10; }
		if (Character.isUpperCase(shortName)) { return shortName - 'A' + 36; }
		
		return -1;
	}
	
	private static void handleLong ()
	{
		/* TODO */
	}
	
	private static void handleShort (String arg)
	{
		if (arg.length() == 1) { Fatal.doubleSingleDash(); }
		final char id = arg.charAt(1);
		
		final int key = getIdKey(id);
		if (ids[key] == 0) { Fatal.undefinedFlag(arg); }
		
	}
	
	private static void readFromStdin ()
	{
		/* TODO */
	}
}






























