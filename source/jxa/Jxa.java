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
	
	// XXX: check
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
			"  the program found '--%s' as an argument ,':|\n",
			given
		);
		System.err.print(msg);
		System.exit(1);
	}

	public static void undefinedFlag (char given)
	{
		final String msg = String.format(
			"jxa::error: undefined flag provided\n" +
			"  the program found '-%c' as an argument ,':|\n",
			given
		);
		System.err.print(msg);
		System.exit(1);
	}
	
	// XXX: check
	public static void groupedArgedFlags (String given, char first, char second)
	{
		final String msg = String.format(
			"jxa::error: more than one flag needs argument in this group\n" +
			"  the program found '%s'; Both '-%c' and '-%c' takes argument\n" +
			"  better do: -%c <arg> -%c <arg> ...\n",
			given,
			first,
			second,
			first,
			second
		);
		System.err.print(msg);
		System.exit(1);
	}
}

public final class Jxa
{
	public static String stdin;
	public static List<String> posArguments = new ArrayList<>();
	
	/* Saves the position where a certain id
	 * can be found within 'flags' array
	 */
	private static final int[] ids = new int[26 + 26 + 10];
	
	private static JxaFlag lastVisited = null;
	
	public static void parse (String[] args, JxaFlag[] flags)
	{
		checkShortNames(flags);
		boolean endOfArgs = false;
		
		for (int i = 0; i < args.length; i++)
		{
			final String arg = args[i];
			
			/* Once '--' is found anything that comes after
			 * will be treated as a positional argument (UNIX standard)
			 */
			if (arg.equals("--"))
			{
				if (endOfArgs == false) { endOfArgs = true; continue; }
				posArguments.add(arg);
			}
			/* In UNIX when a single dash is found, the program
			 * will have to read from STDIN and treat it as an argument
			 */
			else if (arg.equals("-"))
			{
				if (stdin.isEmpty() == false) { Fatal.doubleSingleDash(); }
				handleStdin();
			}
			else if (arg.startsWith("--"))
			{
				handleLong();
			}
			else if (arg.startsWith("-"))
			{
				handleShort(arg, flags);
			}
			/* A 'free word' means anything that is not a flag
			 * for example: 54 "hello java" etc (constant values)
			 */
			else
			{
				handleFreeWord(arg);
			}
		}
	}
	
	/* Makes sure there are not repeated IDs
	 * TODO: Figure out a way to store long names as well for O(1) access
	 */
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
	
	private static void handleShort (String arg, JxaFlag[] flags)
	{
		boolean theresOneWhichTakesArgAlready = false;
		char firstTakingArg = 0;
		
		for (int i = 1; i < arg.length(); i++)
		{
			final char thisId =  arg.charAt(i);
			final int key = getIdKey(thisId);
			final int locatedAt = ids[key];
			
			if (locatedAt == 0) { Fatal.undefinedFlag(thisId); }
			JxaFlag flag = flags[locatedAt - 1];
			
			flag.setSeen(true);
			
			if (flag.getNeeds() != JxaFlag.FlagArg.NON && theresOneWhichTakesArgAlready)
			{	
				Fatal.groupedArgedFlags(arg, firstTakingArg, thisId);
			}
			
			if (flag.getNeeds() != JxaFlag.FlagArg.NON)
			{
				theresOneWhichTakesArgAlready = true;
				firstTakingArg = thisId;
			}
		}
	}
	
	private static void handleStdin ()
	{
		/* TODO */
	}
	
	private static void handleFreeWord (String given)
	{
		/* TODO */
	}
}






























































