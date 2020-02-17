package teammates.diy;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
public class Diy {
    String[] functionLists = new String[] {
            "getRecipientsOfQuestion.txt",
            "getRecipientsForQuestion.txt",
            "isResponseVisibleForUser.txt",
            "updateFeedbackSession.txt",
            "getQuestionWithExistingResponseSubmissionFormHtml.txt",
            "validateQuestionDetails2.txt",
            "prepareData.txt",
            "execute.txt",
            "equals.txt",
            "validateQuestionDetails1.txt"
    };

    private void runTests(){
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("./gradlew componentTests");
            proc.waitFor(4, TimeUnit.MINUTES);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initializeFile(String functionName, int numberOfBranches){
        try {
            File results = new File(functionName+ ".txt");
            if (!results.exists()) {
                PrintWriter writer = new PrintWriter(functionName + ".txt", "UTF-8");
                writer.println(numberOfBranches);
                writer.close();
            }
        } catch (IOException e){
            System.err.println("");
        }
    }

    //sets the value of the specific ID of a specific functionName to true
    public void setReachedId(String functionName, int id) {
        try {
            File results = new File(functionName + ".txt");
            if (results.exists()) {
                addId(results, id);
            }
        } catch (Exception e){
            System.err.println("Exception caught");
        }
    }

    public void writeResults() throws IOException {
        for(String f : functionLists){
            int percentage = 0;
            int numberOfBranches = 0;
            File file = new File(f);
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                String total = scanner.nextLine();
                while (scanner.hasNextLine()) {
                    numberOfBranches++;
                    scanner.nextLine();
                }
                scanner.close();
                percentage = (int) (100 * numberOfBranches / Double.parseDouble(total));
            }
            FileWriter fw = new FileWriter("branchResults.txt", true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(f + ": " + percentage + "%");
            pw.close();
        }
    }


    private void addId(File results, int id){
        try {
            String numberString;
            boolean print = true;
            Scanner scanner = new Scanner(results);
            scanner.nextLine();
            while(scanner.hasNext()){
                numberString = scanner.nextLine();
                if(id == Integer.parseInt(numberString)){
                    print = false;
                }
            }
            if(print){
                FileWriter s = new FileWriter(results.getPath(), true);
                PrintWriter pw = new PrintWriter(s);
                pw.println(id);
                pw.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Diy diy = new Diy();
        diy.runTests();
        //File test = new File("getRecipientsOfQuestion.txt");

        // do some stuff with the files that have all the IDs reached + total number of branches (at the top)
        diy.writeResults();
    }

}