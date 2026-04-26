package cn.com.mfqh.common.db.config;

/**
 * 统一管理数据库连接配置名称
 */
public class DateSourceParameters {
    public DateSourceParameters() {
    }

    /**
     * ServiceUser持久化配置连接
     */
    public final class ServiceUserUpdate {
        public static final String SERVICE_USER_UPDATE_DATA_SOURCE = "serviceUserUpdateDataSource";
        public static final String SERVICE_USER_UPDATE_ENTITY_MANGER_FACTORY = "serviceUserUpdateEntityMangerFactory";
        public static final String SERVICE_USER_UPDATE_TRANSACTION_MANGER_FACTORY = "serviceUserUpdateTransactionMangerFactory";
        public static final String SERVICE_USER_UPDATE_DATA_SOURCE_PREFIX = "service.user.update.datasource";

        public ServiceUserUpdate() {
        }
    }

    /**
     * ServiceUser查询配置连接
     */
    public final class ServiceUserQuery {
        public static final String SERVICE_USER_QUERY_SQL_SESSION_FACTORY = "serviceUserQuerySqlSessionFactory";
        public static final String SERVICE_USER_QUERY_SQL_SESSION_TEMPLATE = "serviceUserQuerySqlSessionTemplate";
        public static final String SERVICE_USER_QUERY_DATA_SOURCE = "serviceUserQueryDataSource";
        public static final String SERVICE_USER_QUERY_TRANSACTION_MANGER_FACTORY = "serviceUserQueryTransactionMangerFactory";
        public static final String SERVICE_USER_QUERY_DATA_SOURCE_PREFIX = "service.user.query.datasource";

        public ServiceUserQuery() {
        }
    }
}
