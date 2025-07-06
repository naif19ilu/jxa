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
		JxaFlag[] flags = new JxaFlag[] {
			new JxaFlag("document",      'd', JxaFlag.FlagArg.YES,    "specifies the input document"),
			new JxaFlag("verbose",       'v', JxaFlag.FlagArg.NON,    "enables verbose output"),
			new JxaFlag("usage",         'u', JxaFlag.FlagArg.MAY,    "prints usage or flag-specific help"),
			new JxaFlag("interactive",   'i', JxaFlag.FlagArg.NON,    "runs program in interactive mode"),
			new JxaFlag("output",        'o', JxaFlag.FlagArg.YES,    "defines output file path"),
			new JxaFlag("force",         'f', JxaFlag.FlagArg.NON,    "forces overwrite of output file"),
			new JxaFlag("color",         'c', JxaFlag.FlagArg.MAY,    "auto", "sets output color mode (auto, always, never)"),
			new JxaFlag("log",           'l', JxaFlag.FlagArg.YES,    "log.txt", "writes log to specified file"),
			new JxaFlag("threads",       't', JxaFlag.FlagArg.YES,    "4", "sets number of worker threads"),
			new JxaFlag("dry-run",       'n', JxaFlag.FlagArg.NON,    "shows what would be done without doing it"),
			new JxaFlag("filter",        'x', JxaFlag.FlagArg.YES,    "applies a filter expression to input"),
			new JxaFlag("config",        'C', JxaFlag.FlagArg.YES,    "loads configuration from file"),
			new JxaFlag("quiet",         'q', JxaFlag.FlagArg.NON,    "suppresses all output"),
			new JxaFlag("summary",       's', JxaFlag.FlagArg.NON,    "shows a summary at the end"),
			new JxaFlag("max-lines",     'M', JxaFlag.FlagArg.YES,    "1000", "limits number of lines processed"),
			new JxaFlag("min-size",      'm', JxaFlag.FlagArg.MAY,    "10K", "minimum size filter for files"),
			new JxaFlag("retry",         'r', JxaFlag.FlagArg.YES,    "3", "sets retry count on failure"),
			new JxaFlag("timeout",       'T', JxaFlag.FlagArg.MAY,    "30", "sets timeout in seconds"),
			new JxaFlag("seed",          'S', JxaFlag.FlagArg.MAY,    "random", "random seed for reproducibility"),
			new JxaFlag("license",       'L', JxaFlag.FlagArg.NON,    "prints license info and exits")
		};

		Jxa.parse(args, flags);	
		JxaDoc.debugInfo(flags);
	}
}

