/** This Program prompts the user for a list of URLs, an output document, and a list type. The program then reads
 * through the list of URLs and reports the number of Fully Qualified Domain names, and then the number of unique fully
 * qualified domain names, second level domain names, and top level domain names while writing them to the output
 * document. The output is designated by the list type which also changes the way the data is stored in the list. The
 * program also records the time needed to complete this task and reports, in milliseconds, the amount of time that
 * passed. Should the desired list be a front, transpose, or count list, it will also report the frequency of the
 * FQDN, TLDN, and 2LDN.
 *
 * @author Christos Kydonieus {@literal < kydoce20@wfu.edu>}
 * @version 0.1, October 11th, 2022
 */

import java.io.*;
import java.util.*;
import java.util.NoSuchElementException;
import java.sql.SQLOutput;
import java.util.Scanner;
import java.io.FileWriter;

public class ProcessFQDN2{
    //@param args String [] , -[-c|d|f|s|t] names . txt results . txt
    public static void main(String[] args) throws IOException{
        List<String> uniqueHost = null; // unique hostnames
        List<String> unique2LD = null; // unique 2 LD names
        List<String> uniqueTLD = null; // unique TLD names
        int numFQDN; // number of FQDNs


        if (!argsOK(args))
            System.exit(1);


        // use the list specified by the user ...
        if (args[0].equals("-d")) {
            uniqueHost = new DList<String>();
            unique2LD = new DList<String>();
            uniqueTLD = new DList<String>();
        } else if (args[0].equals("-s")) {
            uniqueHost = new SortedList<String>();
            unique2LD = new SortedList<String>();
            uniqueTLD = new SortedList<String>();
        } else if (args[0].equals("-c")) {
            uniqueHost = new CountList<String>();
            unique2LD = new CountList<String>();
            uniqueTLD = new CountList<String>();
        } else if (args[0].equals("-f")) {
            uniqueHost = new FrontList<String>();
            unique2LD = new FrontList<String>();
            uniqueTLD = new FrontList<String>();
        } else if (args[0].equals("-t")) {
            uniqueHost = new TransposeList<String>();
            unique2LD = new TransposeList<String>();
            uniqueTLD = new TransposeList<String>();
        } else{
            System.out.println("list type " + args[0] + " is incorrect ");
            return;
        }



        final long startTime = System.currentTimeMillis();

        numFQDN = readNameFile (args [1], uniqueHost , unique2LD , uniqueTLD );
        displayNameInfo (args [2] , numFQDN , uniqueHost , unique2LD , uniqueTLD );


        final long endTime = System.currentTimeMillis();

        // just subtract the two times
        long difference = endTime - startTime;
        System.out.println("Time to complete : " + difference + " msec ");
    }

    /**
     * This method reads in a file of domain names and sorts the unique values into the proper lists of domain names
     * and then reports the number of FQDNs.
     *
     * @param fileName designated input file
     * @param uniqueHost list of FQDNs
     * @param unique2LD list of 2LDs
     * @param uniqueTLD list of TLDs
     * @return number of FQDNs
     * @throws IOException in case the input file is not found
     */
    public static int readNameFile(String fileName, List uniqueHost, List unique2LD, List uniqueTLD) throws IOException{
        int numFQDN = 0;

        // reading the name file
        try {
            Scanner input = new Scanner(new File(fileName));
            while (input.hasNext()) {
                String line = input.nextLine();
                line = line.toLowerCase();
                System.out.println("Line: " + line);

                // split into array
                String[] temp = line.split("\\.");
                // check if FQDN
                if ((temp.length >= 2) && (temp[0].length() > 0)){
                    numFQDN++;
                    // create TLD and SecLD
                    String TLD = temp[temp.length -1];
                    String SecLD = temp[temp.length -2] + "." + temp[temp.length -1];

                    System.out.println("FQDN: " + line);
                    System.out.println("2LD: " + SecLD);
                    System.out.println("TLD: " + TLD);


                    // add to lists
                    if (!uniqueTLD.contains(TLD)) {
                        uniqueTLD.add(TLD);
                    }
                    if (!unique2LD.contains(SecLD)) {
                        unique2LD.add(SecLD);
                    }
                    if (!uniqueHost.contains(line)) {
                        uniqueHost.add(line);
                    }
                }
            }
            input.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error in opening " + fileName);
            System.exit(1);
        }

        return numFQDN;
    }

    /**
     * This method reports the number of FQDNs and the number of unique FQDNs, 2DNs, and TLDNs, while writing them
     * to the output file
     * @param fileName designated output file
     * @param numFQDN number of FQDNs
     * @param uniqueHost list of FQDNs
     * @param unique2LD list of 2LDs
     * @param uniqueTLD list of TLDs
     * @throws IOException in case the result file is not found
     */
    public static void displayNameInfo(String fileName, int numFQDN, List uniqueHost, List unique2LD, List uniqueTLD) throws IOException {
        // creating output file
        FileWriter output = new FileWriter(fileName);

        // setting up unique numbers
        int numUFQDN = uniqueHost.size();
        int numU2LD = unique2LD.size();
        int numUTLD = uniqueTLD.size();

        // reporting numbers
        System.out.println("Found " + numFQDN + " FQDNs, " + numUFQDN + " unique FQDNs, " + numU2LD + " unique 2LDs, and " + numUTLD + " unique TLDs");

        // writing to results
        output.write("Unique FQDNs: ");
        output.write(uniqueHost.toString());

        output.write("\nUnique 2DLs: ");
        output.write(unique2LD.toString());

        output.write("\nUnique TDLs: ");
        output.write(uniqueTLD.toString());

        output.close();
        System.out.println("Unique FQDNs, 2LDs, and TLDs written to: results.txt");
    }

    /**
     * This method checks if the arguments passed through the command line are accurate for the program to work. It
     * checks if the list designation is right, and if the specified inputs and outputs are correct.
     * @param args the arguements passed through the command line
     * @return true if yes, false if the arguments are not correct.
     */
    private static boolean argsOK(String[] args) {
        if(args.length < 3) {
            System.out.println("Usage: java program [-c|d|f|s|t] nameFile outputFile");
            System.out.println("  -c|d|f|s|t      list management (count, double, front, sorted, transpose)");
            System.out.println("  nameFile    file containing input names ");
            System.out.println("  outputFile  file containing output name info ");
            return false;
        }
        return true;
    }
}
