import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class Leaderboard {
    private TreeMap<Integer, String> leaderboard;

    public Leaderboard(){
        leaderboard = new TreeMap<Integer, String>(Collections.reverseOrder());
    }

    public void setLeaderboard(TreeMap<Integer, String> balanceTreeMap){
        int count = 0;

        for(Map.Entry<Integer, String> entry : balanceTreeMap.entrySet()){
            if (count >= 3){
                break;
            }

            leaderboard.put(entry.getKey(), entry.getValue());
            count++;
        }
    }

    public ArrayList<String> getLeaderboard(){
        ArrayList<String> readableLeaderboard = new ArrayList<String>();
        int place = 1;

        for(Map.Entry<Integer, String> entry : leaderboard.entrySet()){
            Integer balance = entry.getKey();
            String username = entry.getValue();

            String readableEntry = place + ".) Username: " + username + ", Balance: " + balance;

            readableLeaderboard.add(readableEntry);
            place++;
        }

        return readableLeaderboard;
    }
}
