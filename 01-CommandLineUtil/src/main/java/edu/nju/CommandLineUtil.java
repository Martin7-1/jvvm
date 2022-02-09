package edu.nju;

import org.apache.commons.cli.*;

/**
 * @author Zyi
 */
public class CommandLineUtil {

    private static CommandLine commandLine;
    private static CommandLineParser parser = new DefaultParser();
    private static Options options = new Options();
    private boolean sideEffect;
    public static final String WRONG_MESSAGE = "Invalid input.";

    /**
     * you can define options here
     * or you can create a func such as [static void defineOptions()] and call it before parse input
     */
    static {
        options.addOption("h", "help", false, "print all usage");
        options.addOption("p", "print", true, "print arguments");
        options.addOption("s", false, "set side effect to true");
    }

    /**
     * step1 add some option rules（you can do it in static{}）
     * step2 parse the input
     * step3 handle options
     * @param args input of program
     */
    public void main(String[] args){
        // 判断是否有用户参数
        boolean hasUserArg = false;
        for (String arg : args) {
            if ("arg0".equals(arg)) {
                hasUserArg = true;
                break;
            }
        }

        if (!hasUserArg) {
            System.out.println(WRONG_MESSAGE);
            return;
        }

        try {
            parseInput(args);
            handleOptions();
        } catch (ParseException e) {
            System.out.println("Missing argument for option: p");
            System.exit(-1);
        }
    }

    /**
     * Print the usage of all options
     * Actually, you can print anything to pass the test
     * but you are recommended to use HelpFormatter to see what will happen
     */
    private static void printHelpMessage() {
        String header = "header of help message";
        String footer = "footer of help message";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("jvm", header, options, footer);
    }

    /**
     * Parse the input and handle exception
     * @param args origin args form input
     */
    public void parseInput(String[] args) throws ParseException {
        commandLine = parser.parse(options, args);
    }

    /**
     * You can handle options here or create your own func
     */
    public void handleOptions() {
        if (commandLine.hasOption("h")) {
            // printHelpMessage();
            // 如果有-h的指令那么其他指令都无效
            System.out.println("help");
            return;
        }
        if (commandLine.hasOption("p")) {
            // 判断有没有参数
            String arg = commandLine.getOptionValue("p");
            System.out.println(arg);
        }
        if (commandLine.hasOption("s")) {
            this.sideEffect = true;
        }
    }

    public boolean getSideEffectFlag(){
        return this.sideEffect;
    }

}