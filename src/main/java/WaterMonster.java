import org.sql2o.Connection;

import java.util.List;

public class WaterMonster extends Monster{

    public WaterMonster(String name, int personId) {
        super(name, personId);
    }

    public static List<WaterMonster> all(){
        try(Connection conn = Db.sql2o.open()){
            return conn.createQuery("SELECT * FROM monsters")
                    .executeAndFetch(WaterMonster.class);
        }
    }

    public static WaterMonster find(int id){
        try(Connection conn = Db.sql2o.open()) {
            return conn.createQuery("SELECT * FROM monsters WHERE id = :id")
                    .addParameter("id",id)
                    .executeAndFetchFirst(WaterMonster.class);
        }
    }


}
