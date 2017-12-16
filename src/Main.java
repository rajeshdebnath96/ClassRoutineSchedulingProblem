import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException{
        Scanner sc = new Scanner(System.in);
        Scanner scf = new Scanner(new File("./hdtt/hdtt4req.txt"));
        Scanner scf1 = new Scanner(new File("./hdtt/hdtt4note.txt"));
        PrintStream outStream = new PrintStream(new FileOutputStream("./output.txt"));
        System.setOut(outStream);
        int noOfClass=4,noOfRoom=4,noOfTeacher=4,noOfDays=5,noOfPeriods=8;
        int count =0;
        while(scf1.hasNext() ){
            String word = scf1.next();
            count++;
            if(count == 5) noOfTeacher = Integer.parseInt(word);
            if(count == 15) noOfClass = Integer.parseInt(word);
            if(count == 21) noOfRoom = Integer.parseInt(word);
        }
        int i,j,k;
        ArrayList<ArrayList<ArrayList<Integer>>> routine = new ArrayList<ArrayList<ArrayList<Integer>>>();
        for(i=0;i<noOfDays*noOfPeriods;i++){
            routine.add(new ArrayList<ArrayList<Integer>>());
        }
        for(i=0;i<noOfClass*noOfRoom;i++){
            for(j=1;j<=noOfTeacher;j++) {
              //  int period = sc.nextInt();
                int period = scf.nextInt();
                for(k=0;k<period;k++) {
                    ArrayList<Integer> element = new ArrayList<Integer>(Arrays.asList((i % noOfRoom) +1, (i / noOfRoom)+1, j)); //class,romm,teacher
                    routine.get(0).add(element);
                }
            }
        }
        System.out.println("Input completed");
        long startTime = System.currentTimeMillis();
        Scheduler sh = new Scheduler(routine);
        sh.setData(noOfClass,noOfRoom,noOfTeacher,noOfDays,noOfPeriods);
        sh.schedule();
        long endTime = System.currentTimeMillis();
        double totalTime = endTime-startTime;
        System.out.println("Execution Time: "+(totalTime/1000) +" seconds");
    }
}