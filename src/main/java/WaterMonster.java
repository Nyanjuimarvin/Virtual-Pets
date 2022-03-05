import org.sql2o.Connection;

import java.sql.Timestamp;
import java.util.List;

public class WaterMonster extends Monster{

    private int waterLevel;
    public Timestamp lastWater;
    public static final int MAX_WATER_LEVEL = 8;
    public static final String DATABASE_TYPE = "water";

    public WaterMonster(String name, int personId) {
        super(name, personId);
        waterLevel = MAX_WATER_LEVEL /2;
        type = DATABASE_TYPE;

    }

    public int getWaterLevel() {
        return waterLevel;
    }


    public Timestamp getLastWater() {
        return lastWater;
    }

    @Override
    public void depleteLevels(){
        if (isAlive()){
            playLevel--;
            foodLevel--;
            sleepLevel--;
            waterLevel--;
        }
    }

    @Override
    public boolean isAlive() {
        return  foodLevel >= MIN_ALL_LEVELS &&
                playLevel >= MIN_ALL_LEVELS &&
                waterLevel >= MIN_ALL_LEVELS &&
                sleepLevel >= MIN_ALL_LEVELS;
    }

    public void water(){
        if (waterLevel >= MAX_WATER_LEVEL){
            throw new UnsupportedOperationException("You cannot water your pet any more!");
        }
        try(Connection con = Db.sql2o.open()) {
            String sql = "UPDATE monsters SET lastwater = now() WHERE id = :id";
            con.createQuery(sql)
                    .addParameter("id", getId())
                    .executeUpdate();
        }
        waterLevel++;
    }
    public static List<WaterMonster> all(){
        try(Connection conn = Db.sql2o.open()){
            return conn.createQuery("SELECT * FROM monsters WHERE type = 'water';")
                    .throwOnMappingFailure(false)
                    .executeAndFetch(WaterMonster.class);
        }
    }

    public static WaterMonster find(int id){
        try(Connection conn = Db.sql2o.open()) {
            return conn.createQuery("SELECT * FROM monsters WHERE id = :id")
                    .addParameter("id",id)
                    .throwOnMappingFailure(false)
                    .executeAndFetchFirst(WaterMonster.class);
        }
    }


}
