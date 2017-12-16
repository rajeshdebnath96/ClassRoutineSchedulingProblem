import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Random;

import static java.lang.Integer.max;

public class Scheduler {
    ArrayList<ArrayList<ArrayList<Integer>>> routine,routineCopy;
    int noOfClass,noOfRoom,noOfTeacher,noOfDays,noOfPeriods;
    boolean steepest_ascent = false;
    boolean first_choice = false;
    boolean stochastic = true;
    boolean ramdom_restart = true;
    int[] w = {1,1,1};
    Scheduler(ArrayList<ArrayList<ArrayList<Integer>>> routine){
        this.routine = routine;
        this.routineCopy = routine;
    }
    void setData(int noOfClass,int noOfRoom,int noOfTeacher,int noOfDays,int noOfPeriods){
        this.noOfClass = noOfClass;
        this.noOfRoom = noOfRoom;
        this.noOfTeacher = noOfTeacher;
        this.noOfDays = noOfDays;
        this.noOfPeriods = noOfPeriods;
    }
    ArrayList<ArrayList<ArrayList<Integer>>> arrCopy(ArrayList<ArrayList<ArrayList<Integer>>> src,ArrayList<ArrayList<ArrayList<Integer>>> des){
        for(int i=0;i<src.size();i++){
            des.add(i,new ArrayList<ArrayList<Integer>>());
            for(int j=0;j<src.get(i).size();j++){
                des.get(i).add(j,new ArrayList<Integer>());
                for(int k=0;k<src.get(i).get(j).size();k++){
                    des.get(i).get(j).add(k,src.get(i).get(j).get(k));
                }
            }
        }
        return des;
    }
    int getDuplicates(ArrayList<Integer> element,int n){
      //  System.out.println("getDuplicate func");
        int dup = 0;
        for(int i=1;i<=n;i++){
            dup+=max(0,Collections.frequency(element,i)-1);
        }
        return dup;
    }
    int getCost(ArrayList<ArrayList<ArrayList<Integer>>> routine){
     //   System.out.println("getCost func");
        int cost = 0;
        for(int i=0;i<noOfDays*noOfPeriods;i++){
            ArrayList<ArrayList<Integer>> period = routine.get(i);
            ArrayList<Integer> teacher = new ArrayList<>();
            ArrayList<Integer> room = new ArrayList<>();
            ArrayList<Integer> classes = new ArrayList<>();
            for(int j=0;j<period.size();j++){
                ArrayList<Integer> element = period.get(j);
                classes.add(element.get(0));
                room.add(element.get(1));
                teacher.add(element.get(2));
            }
            cost += w[0]*getDuplicates(teacher,noOfTeacher);
            cost += w[1]*getDuplicates(room,noOfRoom);
            cost += w[2]*getDuplicates(classes,noOfClass);
        }
        return cost;
    }
    ArrayList<RoutineState> getNeighbours(ArrayList<ArrayList<ArrayList<Integer>>> routine){
     //   System.out.println("getNeighbour func");
        ArrayList<RoutineState> SuccessorList = new ArrayList<RoutineState>();
        int itr=0;
        for(int i=0;i<noOfPeriods*noOfDays;i++){
            for(int j=0;j<routine.get(i).size();j++){
                for(int k=0;k<noOfPeriods*noOfDays;k++){
                    itr++;
                  //  System.out.println("Iteration: "+itr+" i="+i+ ", j="+j+", k="+k);
                    ArrayList<ArrayList<ArrayList<Integer>>> routineSuccessor = new ArrayList<ArrayList<ArrayList<Integer>>>();
                    if(i!=k){
                        routineSuccessor = arrCopy(routine,routineSuccessor);
                        routineSuccessor.get(i).remove(j);
                    //    System.out.println("Period size "+routine.get(i).size());
                        routineSuccessor.get(k).add(routine.get(i).get(j));
                        int cost = getCost(routineSuccessor);
                        SuccessorList.add(new RoutineState(routineSuccessor,cost));
                    }
                    //else break;
                }
            }
        }
        return SuccessorList;
    }
    RoutineState random_Neighbour(ArrayList<ArrayList<ArrayList<Integer>>> routine){
        RoutineState res;
        while (true){
            ArrayList<ArrayList<ArrayList<Integer>>> routineSuccessor = new ArrayList<ArrayList<ArrayList<Integer>>>();
            Random rand = new Random();
            int i;
            while (true) {
                i = rand.nextInt(noOfPeriods * noOfDays);
                if(routine.get(i).size() > 0) break;
            }
            int k = rand.nextInt(noOfPeriods*noOfDays);
            if(i!=k){
                int j = rand.nextInt(routine.get(i).size());
                routineSuccessor = arrCopy(routine,routineSuccessor);
                routineSuccessor.get(i).remove(j);
              //  System.out.println("Period size "+routine.get(i).size());
                routineSuccessor.get(k).add(routine.get(i).get(j));
                int cost = getCost(routineSuccessor);
                res = new RoutineState(routineSuccessor,cost);
                break;
            }
        }
        return res;
    }
    RoutineState random_best_Neighbour(ArrayList<ArrayList<ArrayList<Integer>>> routine){
      //  System.out.println("random_best_Neighbour func");
        ArrayList<RoutineState> SuccessorList = new ArrayList<RoutineState>();
        int itr=0;
        int given_cost = getCost(routine);
        for(int i=0;i<noOfPeriods*noOfDays;i++){
            for(int j=0;j<routine.get(i).size();j++){
                for(int k=0;k<noOfPeriods*noOfDays;k++){
                    itr++;
                  //  System.out.println("Iteration: "+itr+" i="+i+ ", j="+j+", k="+k);
                    ArrayList<ArrayList<ArrayList<Integer>>> routineSuccessor = new ArrayList<ArrayList<ArrayList<Integer>>>();
                    if(i!=k){
                        routineSuccessor = arrCopy(routine,routineSuccessor);
                        routineSuccessor.get(i).remove(j);
                      //  System.out.println("Period size "+routine.get(i).size());
                        routineSuccessor.get(k).add(routine.get(i).get(j));
                        int cost = getCost(routineSuccessor);
                        if(cost < given_cost) {
                      //      System.out.println("Cost: "+cost+ " given_cost: "+given_cost);
                            SuccessorList.add(new RoutineState(routineSuccessor,cost));
                        }
                    }
                }
            }
        }
        Random rand = new Random();
        int i =0;
      //  System.out.println("Successorlist size: "+SuccessorList.size());
        if(SuccessorList.size() > 1) {
            i = rand.nextInt(SuccessorList.size());
        }
       // System.out.println("value of i = "+i);
        if(SuccessorList.size() == 0) return new RoutineState(null,100000);
        return SuccessorList.get(i);
    }
    RoutineState random_state(ArrayList<ArrayList<ArrayList<Integer>>> routine){
        System.out.println("random_state func");
        int itr=0,k;
        ArrayList<ArrayList<ArrayList<Integer>>> routineSuccessor = new ArrayList<ArrayList<ArrayList<Integer>>>();
        for(int i=0;i<noOfPeriods*noOfDays;i++) {
            routineSuccessor.add(new ArrayList<ArrayList<Integer>>());
        }
        for(int i=0;i<noOfPeriods*noOfDays;i++){
            for(int j=0;j<routine.get(i).size();j++){
                Random rand = new Random();
                while (true){
                    k = rand.nextInt(noOfPeriods*noOfDays);
                    if(k != i) break;
                };
                itr++;
             //   System.out.println("Iteration: "+itr+" i="+i+ ", j="+j+", k="+k);

              //  routineSuccessor = arrCopy(routine,routineSuccessor);
              //  routineSuccessor.get(i).remove(j);
             //   System.out.println("Period size "+routine.get(i).size());
                routineSuccessor.get(k).add(routine.get(i).get(j));
               // int cost = getCost(routineSuccessor);
                }


        }
        int cost = getCost(routineSuccessor);
        RoutineState res = new RoutineState(routineSuccessor,cost);
        return res;
    }
    RoutineState steepest_ascent_hill_climbing(ArrayList<ArrayList<ArrayList<Integer>>> r){
        RoutineState CurrentState = new RoutineState(r,getCost(r));
      //  printRoutine(CurrentState);
        System.out.println("Routine size: "+r.get(0).size() + " cost: "+getCost(r));
        RoutineState best;
        System.out.println("steepest_ascent_hill_climbing func");
        int itr=0;
        while (true){
            PriorityQueue<RoutineState> Successor= new PriorityQueue<>(getNeighbours(CurrentState.routine));
            best = Successor.remove();
            itr++;
           // System.out.println("Iteration in schedule : "+itr +" best cost: "+best.cost);
            if(best.cost >= CurrentState.cost){
                break;
               // return CurrentState;
            }
            CurrentState = best;
        }
        System.out.println("best in steepest_ascent_hill_climbing loop count: "+itr +" best cost: "+CurrentState.cost);
        printRoutine(CurrentState);
        return CurrentState;
    }
    RoutineState first_choice_hill_climbing(ArrayList<ArrayList<ArrayList<Integer>>> r){
        RoutineState currentState = new RoutineState(r,getCost(r));
        RoutineState best;
        System.out.println("first_choice_hill_climbing func");
        int itr=0,count =0;
        while(true){
            best = random_Neighbour(currentState.routine);
            itr++;
         //   System.out.println("Iteration in first_choice_hill_climbing : "+itr +" best cost: "+best.cost);
            if(best.cost < currentState.cost){
                count++;
                currentState = best;
             //   System.out.println("best in first_choice_hill_climbing : "+itr +" best cost: "+best.cost);
            }
            else {
              //  count++;
             //   System.out.println((itr - count) +" itr=" + itr +" count="+count);
                if((itr - count) > 5000) break;
            }
            if(best.cost <= 0){
                currentState = best;
                break;
            }
        }
        System.out.println("best in first_choice_hill_climbing loop count: "+itr +" best cost: "+currentState.cost);
        printRoutine(currentState);
        return currentState;
    }
    RoutineState stochastic_hill_climbing(ArrayList<ArrayList<ArrayList<Integer>>> r){
        RoutineState currentState = new RoutineState(r,getCost(r));
        RoutineState best;
        System.out.println("stochastic_hill_climbing func");
        int itr=0,count=0;
        while(true){
            best = random_best_Neighbour(currentState.routine);
            itr++;
          //  System.out.println("Iteration in stochastic_hill_climbing : "+itr +" best cost: "+best.cost);
            if(best.cost == 100000) break;
            if(best.cost < currentState.cost){
                count++;
                currentState = best;
            }
            else {
           //     System.out.println((itr - count) +" itr=" + itr +" count="+count);
                if((itr - count) > 5000) break;
            }
            if(best.cost <= 0){
                currentState = best;
                break;
            }
        }
        System.out.println("best in stochastic_hill_climbing : loop count "+itr +" best cost: "+currentState.cost);
        printRoutine(currentState);
        return currentState;
    }
    void random_restart_hill_climbing(ArrayList<ArrayList<ArrayList<Integer>>> r){
        RoutineState currentState = new RoutineState(r,getCost(r));
        RoutineState best;
        System.out.println("random_restart_hill_climbing func");
        while (true){
           // if(steepest_ascent)
            best = null;
            if(steepest_ascent) best = steepest_ascent_hill_climbing(currentState.routine);
            else if(first_choice) best = first_choice_hill_climbing(currentState.routine);
            else if(stochastic) best = stochastic_hill_climbing(currentState.routine);
            System.out.println("Best Routine cost: "+best.cost);
            if(best.cost > 0){
                currentState = random_state(routine);
            }
            else {
                currentState = best;
                break;
            }
        }
        printRoutine(currentState);
    }
    void schedule(){
        if(ramdom_restart) random_restart_hill_climbing(routine);
        else if(steepest_ascent) steepest_ascent_hill_climbing(routine);
        else if(first_choice) first_choice_hill_climbing(routine);
        else if(stochastic) stochastic_hill_climbing(routine);

    }
    void printRoutine(RoutineState r){
        ArrayList<ArrayList<ArrayList<Integer>>> routine = r.routine;
        System.out.println("---------------------------------- Printing Routine -----------------------------");
        for(int i=0;i<noOfDays*noOfPeriods;i++){
            ArrayList<ArrayList<Integer>> classes = routine.get(i);
            System.out.print("Day: "+((i/noOfPeriods)+1)+" Period: "+((i%noOfPeriods)+1) + "   ");
            for (int j=0;j<classes.size();j++){
                ArrayList<Integer> element = classes.get(j);
                System.out.print(" <C"+element.get(0)+", R"+element.get(1)+", T"+element.get(2)+">,");
            }
            System.out.println();
        }
    }
}
