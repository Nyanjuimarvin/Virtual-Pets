import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.sql2o.Connection;
import org.sql2o.Sql2o;



public class DatabaseRule implements AfterEachCallback, BeforeEachCallback {


    //OPEN CONNECTION BEFORE EACH TEST
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Db.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/virtualpets_test", "marvin", "nrvnqsr13");
    }

    //CLEAR DATABASE AFTER EACH TEST
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        try(Connection con = Db.sql2o.open()) {
            String deletePersonsQuery = "DELETE FROM persons *;";
            String deleteMonstersQuery = "DELETE FROM monsters *;";
            con.createQuery(deletePersonsQuery).executeUpdate();
            con.createQuery(deleteMonstersQuery).executeUpdate();
        }
    }
}
