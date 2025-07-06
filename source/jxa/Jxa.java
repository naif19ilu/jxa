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

public final class Jxa
{
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
				JxaFatal.unsupportedStdin();
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
				JxaFatal.invalidId(flags[i]);
			}
			if (quickShortNames[key] != 0)
			{
				JxaFatal.duplicatedId(quickShortNames[key] - 1, i, flags);
			}
			
			final String longname = flags[i].getLongName();
			if (quickLongNames.containsKey(longname))
			{
				JxaFatal.duplicatedName(flags[i]);
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
		String rmDashes = longName.substring(2);
		String arg = "";
		
		if (rmDashes.contains("="))
		{
			final int eqIndex = rmDashes.indexOf('=');
			arg = (String) rmDashes.subSequence(eqIndex + 1, rmDashes.length());
			rmDashes = rmDashes.substring(0, eqIndex);	
		}
		
		final int at = quickLongNames.getOrDefault(rmDashes, -1);
		if (at == -1) { JxaFatal.undefinedFlag(rmDashes); }
		
		thisFlag = flags[at];
		thisFlag.setSeen(true);

		if (thisFlag.getNeeds() == JxaFlag.FlagArg.NON && arg.isEmpty() == false)
		{
			JxaFatal.noNeedOfArg(rmDashes);
		}
		thisFlag.setArgument(arg);
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
			
			if (locatedAt == 0) { JxaFatal.undefinedFlag(thisId); }
			JxaFlag flag = flags[locatedAt - 1];
			
			flag.setSeen(true);
			
			if (flag.getNeeds() != JxaFlag.FlagArg.NON && theresOneWhichTakesArgAlready)
			{	
				JxaFatal.groupedArgedFlags(arg, firstTakingArg, thisId);
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
	
	private static void handleFreeWord (String given)
	{
		if (thisFlag != null) { thisFlag.setArgument(given); thisFlag = null; }	
		else { posArguments.add(given); }
	}		
	
	private static void makeSureFlagHasItsArg ()
	{
		if (thisFlag != null && thisFlag.getNeeds() == JxaFlag.FlagArg.YES && thisFlag.getArgument().isEmpty())
		{
			JxaFatal.missingArgument(thisFlag);
		}
	}
}