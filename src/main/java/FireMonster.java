import org.sql2o.Connection;

import java.util.List;

public class FireMonster extends Monster{

    public FireMonster(String name, int personId) {
        super(name, personId);
    }


    public static List<FireMonster> all(){
        try(Connection conn = Db.sql2o.open()){
            return conn.createQuery("SELECT * FROM monsters")
                    .executeAndFetch(FireMonster.class);
        }
    }

    public static FireMonster find(int id){
        try(Connection conn = Db.sql2o.open()) {
            return conn.createQuery("SELECT * FROM monsters WHERE id = :id")
                    .addParameter("id",id)
                    .executeAndFetchFirst(FireMonster.class);
        }
    }


}
