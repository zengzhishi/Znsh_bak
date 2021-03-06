package cn.zlion.dao.daoImpl;

import cn.zlion.dao.ClusterDataDao;
import cn.zlion.dao.domainMapper.JobResultRowMapper;
import cn.zlion.dao.domainMapper.RuleResultRowMapper;
import cn.zlion.dao.domainMapper.TaskResultRowMapper;
import cn.zlion.domain.JobResult;
import cn.zlion.domain.RuleResult;
import cn.zlion.domain.TaskResult;
import cn.zlion.pagenationUtil.PageResult;
import cn.zlion.pagenationUtil.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by zzs on 10/25/16.
 */
@Repository
public class ClusterDataDaoImpl implements ClusterDataDao{


    @Autowired
    @Qualifier("postgresqlJdbcTemplate")
    private JdbcTemplate postgresqlJdbcTemplate;

    /**
     * 分页查询应用对应的表数据, 静态审核结果表和动态用户表
     * @param app_id
     * @param curPage
     * @param pageRows
     * @param tableName
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public PageResult findByPage(String app_id, int curPage, int pageRows, String tableName) {

        Pagination pagination = null;
        String sql = "SELECT * FROM \"" + app_id + "\".\"" + tableName + "\"";
        if (tableName.equals(JobResult.TABLE_NAME)){
            sql = sql + " ORDER BY pk";
            pagination = new Pagination(sql, postgresqlJdbcTemplate, pageRows, curPage, new JobResultRowMapper());
        }
        else if (tableName.equals(TaskResult.TABLE_NAME)){
            sql = sql + " ORDER BY pk";
            pagination = new Pagination(sql, postgresqlJdbcTemplate, pageRows, curPage, new TaskResultRowMapper());
        }
        else if (tableName.equals(RuleResult.TABLE_NAME)){
            sql = sql + " ORDER BY pk";
            pagination = new Pagination(sql, postgresqlJdbcTemplate, pageRows, curPage, new RuleResultRowMapper());
        }
        else{
            //动态表查询
            pagination = new Pagination(sql, postgresqlJdbcTemplate, pageRows, curPage);
        }

        return new PageResult(pagination.getTotalRows(), pagination.getTotalPages(), pagination.getResultList());
    }


    //lastUpdateTime对TaskResult有效, foreignPk 则对作业结果和规则结果有效
    @Override
    public PageResult findByTimeAndPage(String app_id, int curPage, int pageRows, String tableName, Date lastUpdateTime, String foreignPk) {

        Pagination pagination = null;

        String sql = "SELECT * FROM \"" + app_id + "\".\"" + tableName + "\" ";

        switch (tableName){
            case "T_RESULT_TASK":
                sql = sql + "WHERE time_arrival > \'" + new Timestamp(lastUpdateTime.getTime()) + "\' ORDER BY pk";
                pagination = new Pagination(sql, postgresqlJdbcTemplate, pageRows, curPage, new TaskResultRowMapper());
                break;
            case "T_RESULT_JOB":
                sql = sql + " WHERE pk_t=\'" + foreignPk + "\' ORDER BY pk";
                pagination = new Pagination(sql, postgresqlJdbcTemplate, pageRows, curPage, new JobResultRowMapper());
                break;
            case "T_RESULT_RULE":
                //联表查询
                StringBuilder sqlBuilder = new StringBuilder(sql);
                sqlBuilder.append("WHERE pk_j IN (");
                sqlBuilder.append("SELECT pk FROM \"" + app_id + "\".\"" + JobResult.TABLE_NAME + "\" ");
                sqlBuilder.append("WHERE pk_t=\'" + foreignPk + "\')");
                sqlBuilder.append(" ORDER BY pk");
                pagination = new Pagination(sqlBuilder.toString(), postgresqlJdbcTemplate, pageRows, curPage, new RuleResultRowMapper());
                break;
            default:
                pagination = new Pagination(sql, postgresqlJdbcTemplate, pageRows, curPage);
        }

        return new PageResult(pagination.getTotalRows(), pagination.getTotalPages(), pagination.getResultList());
    }


    //JobResult和RuleResult两个表, 联表查询
    @Override
    public PageResult findResultByPk(String app_id, int curPage, int pageRows, String tableName, String pk) {
        Pagination pagination = null;

        String sql = "SELECT ";
        return null;
    }

    /**
     * 检查表在对应schema中是否存在数据表
     * @param appId
     * @param tableName
     * @return
     */
    @Override
    public boolean checkTableExist(String appId, String tableName){
        String sql = "select count(*) from information_schema.tables where table_schema='" + appId +
                "' and table_type='BASE TABLE' and table_name='" + tableName + "'";
        int amount = postgresqlJdbcTemplate.queryForObject(sql, Integer.class);
        if (amount > 0)
            return true;
        else
            return false;
    }
}
