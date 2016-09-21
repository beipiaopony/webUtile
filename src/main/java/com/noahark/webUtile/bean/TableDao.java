package com.noahark.webUtile.bean;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public class TableDao {

	private static Logger logger = Logger.getLogger(TableDao.class);
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

		
	public void save(final Table table) {
		String insertSql = table.getInsertSql();

		logger.debug(insertSql);
		final List<Row> rows = table.getRows();

		logger.debug("save data to db...");
		jdbcTemplate.batchUpdate(insertSql,
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						int j = 1;
						for (String column : table.getColumns()) {
							ps.setString(j, rows.get(i).getValue(column));
							j++;
						}

						ps.setString(j, table.getProjectName());
						//ps.setString(j+1,  UUID.randomUUID().toString());
					}

					public int getBatchSize() {
						return table.getRowCount();
					}
				});
		
		//logger.debug("db-result:" + getResult(updateCounts));
		
		
	}

}
