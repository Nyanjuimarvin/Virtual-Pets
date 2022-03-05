import org.sql2o.Connection;

import java.sql.Timestamp;
import java.util.List;

public class FireMonster extends Monster{

    private int fireLevel;
    public Timestamp lastKindling;
    public static final int MAX_FIRE_LEVEL = 10;
    public static final String DATABASE_TYPE = "fire";

    public FireMonster(String name, int personId) {
        super(name, personId);
        fireLevel = MAX_FIRE_LEVEL / 2;
        type = DATABASE_TYPE;
    }

    public int getFireLevel() {
        return fireLevel;
    }

    public Timestamp getLastKindling() {
        return lastKindling;
    }

    //Increase fire-level
    public void kindling(){
        if (fireLevel >= MAX_PLAY_LEVEL){
            throw new UnsupportedOperationException("You cannot give any more kindling!");
        }
        try(Connection con = Db.sql2o.open()) {
            String sql = "UPDATE monsters SET lastkindling = now() WHERE id = :id";
            con.createQuery(sql)
                    .addParameter("id", getId())
                    .executeUpdate();
        }
        fireLevel++;
    }

    @Override
    public void depleteLevels(){
        if (isAlive()){
            playLevel--;
            foodLevel--;
            sleepLevel--;
            fireLevel--;
        }
    }

    @Override
    public boolean isAlive() {
        return  foodLevel >= MIN_ALL_LEVELS &&
                playLevel >= MIN_ALL_LEVELS &&
                fireLevel >= MIN_ALL_LEVELS &&
                sleepLevel >= MIN_ALL_LEVELS;
    }


    public static List<FireMonster> all(){
        try(Connection conn = Db.sql2o.open()){
            return conn.createQuery("SELECT * FROM monsters WHERE type = 'fire';")
                    .throwOnMappingFailure(false)
                    .executeAndFetch(FireMonster.class);
        }
    }

    public static FireMonster find(int id){
        try(Connection conn = Db.sql2o.open()) {
            return conn.createQuery("SELECT * FROM monsters WHERE id = :id")
                    .addParameter("id",id)
                    .throwOnMappingFailure(false)
                    .executeAndFetchFirst(FireMonster.class);
        }
    }


}
