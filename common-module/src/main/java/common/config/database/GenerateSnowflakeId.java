package common.config.database;

import cn.hutool.core.lang.Snowflake;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

public class GenerateSnowflakeId implements IdentifierGenerator {

    @Autowired
    Snowflake snowflake;
//
//    @Override
//    public <T> T generateIdentifierOfType(TypeInformation<T> type) {
//        return (T) snowflake.nextIdStr();
//    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return snowflake.nextIdStr();
    }
}
