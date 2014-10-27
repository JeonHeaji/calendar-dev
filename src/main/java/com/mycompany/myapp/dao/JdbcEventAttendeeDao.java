package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.domain.EventAttendee;
import com.mycompany.myapp.domain.CalendarUser;
import com.mycompany.myapp.domain.Event;

@Repository
public class JdbcEventAttendeeDao implements EventAttendeeDao {
	private JdbcTemplate jdbcTemplate;

	private RowMapper<EventAttendee> rowMapper;

	@Autowired
	private CalendarUserDao eventDao;
	
	@Autowired
	private CalendarUserDao calendarUserDao;
	
	// --- constructors ---
	public JdbcEventAttendeeDao() {
		rowMapper = new RowMapper<EventAttendee>() {
			public EventAttendee mapRow(ResultSet rs, int rowNum) throws SQLException {
				EventAttendee eventAttendeeList = new EventAttendee();

				/* TODO Assignment 3 */
				eventAttendeeList.setId(rs.getInt("id"));
				eventAttendeeList.setAttendee((CalendarUser) rs.getObject("attendee"));
				eventAttendeeList.setEvent((Event) rs.getObject("event"));
			
				return eventAttendeeList;
			}
		};
	}
	
	@Autowired
	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<EventAttendee> findEventAttendeeByEventId(int eventId) {
		// TODO Assignment 3
		String sql_query = "select * from event_attendees where eventId = ?";
		return this.jdbcTemplate.query(sql_query, new Object[] {eventId}, rowMapper);
	}

	@Override
	public List<EventAttendee> findEventAttendeeByAttendeeId(int attendeeId) {
		// TODO Assignment 3
		String sql_query = "select * from event_attendees where attendeeId = ?";
		return this.jdbcTemplate.query(sql_query, new Object[] {attendeeId}, rowMapper);
	}

	@Override
	public int createEventAttendee(EventAttendee eventAttendee) {
		// TODO Assignment 3
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement("insert into event_attendees(id, event_id, attendee) values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, eventAttendee.getId());
				ps.setObject(2, eventAttendee.getEvent());
				ps.setObject(3, eventAttendee.getAttendee());

				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public void deleteEventAttendee(int id) {
		// TODO Assignment 3
		String sql_query = "delete from event_attendees where id = ?";
		this.jdbcTemplate.query(sql_query, new Object[] {id}, rowMapper);
	}

	@Override
	public void deleteAll() {
		// TODO Assignment 3
		String sql = "delete from event_attendees";
		this.jdbcTemplate.update(sql);
	}
}