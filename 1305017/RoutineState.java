import java.util.ArrayList;

public class RoutineState implements Comparable<RoutineState>{
    ArrayList<ArrayList<ArrayList<Integer>>> routine;
    int cost;
    RoutineState(ArrayList<ArrayList<ArrayList<Integer>>> routine, int cost){
        this.routine = routine;
        this.cost = cost;
    }

    @Override
    public int compareTo(RoutineState r) {
        if(cost > r.cost) return 1;
        else if(cost < r.cost) return -1;
        else return 0;
    }
}
