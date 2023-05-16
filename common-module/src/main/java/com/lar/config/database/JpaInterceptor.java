package com.lar.config.database;


import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.Query;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class JpaInterceptor implements StatementInspector {


    @Override
    public String inspect(String sql) {
        sql = sql.toLowerCase();
        StringBuilder sb = new StringBuilder();
        if (sql.contains("select" )) {

            String[] froms = sql.split("from" );
            String from = froms[0];
            String other = froms[1];
            String data = from.trim().split(" " )[1];
            String[] allField = data.split("," );

            sb.append("select " );
            for (int i = 0; i < allField.length; i++) {
                sb.append(allField[i].split("\\." )[1]);
                if(i<allField.length-1){
                    sb.append(",");
                }
            }
            sb.append(" from " ).append(other);
        }
        log.info("\n<======");
        System.out.println(sb+"\n======>");
        return sb.toString();
    }


}
