/*
 * jxa - Java executable arguments parser
 * Jul 4, 2025
 * Actual parser
 */

package jxa;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
			"  the program cannot recognize '--%s' as a flag ,':|\n",
			given
		);
		System.err.print(msg);
		System.exit(1);
	}

	public static void undefinedFlag (char given)
	{
		final String msg = String.format(
			"jxa::error: undefined flag provided\n" +
			"  the program cannot recognize '-%c' as a flag ,':|\n",
			given
		);
		System.err.print(msg);
		System.exit(1);
	}
	
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
	
	public static void missingArgument (JxaFlag flag)
	{
		final String msg = String.format(
			"jxa::error: flag is missing its argument\n" +
			"  the program found that '--%s' has no argument, please provide it\n",
			flag.getLongName()
		);
		System.err.print(msg);
		System.exit(1);
	}
	
	public static void duplicatedName (JxaFlag flag)
	{	
		final String msg = String.format(
			"jxa::error: longname appears more than once\n" +
			"  the program found that '--%s' longname is repeated\n",
			flag.getLongName()
		);
		System.err.print(msg);
		System.exit(1);
	}
}

public final class Jxa
{
	public static String stdin = "";
	public static List<String> posArguments = new ArrayList<>();
	
	/* Saves the position where a certain short-name
	 * can be found within 'flags' array
	 */
	private static final int[] quickShortNames = new int[26 + 26 + 10];
	
	/* Saves the position where a certain long-name
	 * can be found within 'flags' array
	 */
	private static final Map<String, Integer> quickLongNames = new HashMap<>();
	
	/* This variable is only assigned to some actual flag
	 * when the flag needs is different from JxaFlag.FlagArg.NON
	 */
	private static JxaFlag thisFlag = null;
	
	public static void parse (String[] args, JxaFlag[] flags)
	{
		checkNames(flags);
		boolean endOfArgs = false;
		
		for (int i = 0; i < args.length; i++)
		{
			final String arg = args[i];
			
			if (endOfArgs)
			{
				posArguments.add(arg);
				continue;
			}
			/* Once '--' is found anything that comes after
			 * will be treated as a positional argument (UNIX standard)
			 */
			else if (arg.equals("--"))
			{
				makeSureFlagHasItsArg();
				if (endOfArgs == false) { endOfArgs = true; }
			}
			/* In UNIX when a single dash is found, the program
			 * will have to read from STDIN and treat it as an argument
			 */
			else if (arg.equals("-"))
			{
				if (stdin.isEmpty() == false) { Fatal.doubleSingleDash(); }
				makeSureFlagHasItsArg();
				handleStdin();
			}
			else if (arg.startsWith("--"))
			{
				makeSureFlagHasItsArg();
				handleLong(arg, flags);
			}
			else if (arg.startsWith("-"))
			{
				makeSureFlagHasItsArg();
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
		makeSureFlagHasItsArg();
	}
	
	/* Makes sure there are not repeated IDs nor
	 * duplicated long names
	 */
	private static void checkNames (JxaFlag[] flags)
	{
		for (int i = 0; i < flags.length; i++)
		{
			final char id = flags[i].getShortName();
			final int key = getIdKey(id);
			
			if (key == -1)
			{
				Fatal.invalidId(flags[i]);
			}
			if (quickShortNames[key] != 0)
			{
				Fatal.duplicatedId(quickShortNames[key] - 1, i, flags);
			}
			
			final String longname = flags[i].getLongName();
			if (quickLongNames.containsKey(longname))
			{
				Fatal.duplicatedName(flags[i]);
			}
			
			quickShortNames[key] = i + 1;	
			quickLongNames.put(longname, i);
		}
	}
	
	private static int getIdKey (char shortName)
	{
		if (Character.isDigit(shortName))     { return shortName - '0'; }
		if (Character.isLowerCase(shortName)) { return shortName - 'a' + 10; }
		if (Character.isUpperCase(shortName)) { return shortName - 'A' + 36; }
		
		return -1;
	}
	
	private static void handleLong (String longName, JxaFlag[] flags)
	{
		final String rmDashes = longName.substring(2);
		
		final int at = quickLongNames.getOrDefault(rmDashes, -1);
		if (at == -1) { Fatal.undefinedFlag(rmDashes); }
		
		thisFlag = flags[at];
		thisFlag.setSeen(true);
		thisFlag.setArgument("");
	}
	
	private static void handleShort (String arg, JxaFlag[] flags)
	{
		boolean theresOneWhichTakesArgAlready = false;
		char firstTakingArg = 0;
		
		for (int i = 1; i < arg.length(); i++)
		{
			final char thisId =  arg.charAt(i);
			final int key = getIdKey(thisId);
			final int locatedAt = quickShortNames[key];
			
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

				thisFlag = flag;
				/* Reset the argument of this flag since it could be
				 * repeated in the argument list
				 */
				thisFlag.setArgument("");
			}
		}
	}
	
	private static void handleStdin ()
	{
		/* TODO */
	}
	
	private static void handleFreeWord (String given)
	{
		if (thisFlag != null) { thisFlag.setArgument(given); thisFlag = null; }	
		else { posArguments.add(given); }
	}		
	
	private static void makeSureFlagHasItsArg ()
	{
		if (thisFlag != null && thisFlag.getNeeds() == JxaFlag.FlagArg.YES && thisFlag.getArgument().isEmpty())
		{
			Fatal.missingArgument(thisFlag);
		}
	}
}






























































