package oauthlearn.jdbc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

public class MyJdbcConnectionTest {

    @Test
    public void jdbcTest() {
        Connection myJdbcConnection = MyJdbcConnection.getConnection();

        Assertions.assertThat(myJdbcConnection).isNotNull();
    }
}
