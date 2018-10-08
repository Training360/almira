package com.training360.locationsapp.repository;

import com.training360.locationsapp.controller.ListLocationsResponse;
import com.training360.locationsapp.entities.Location;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
public class LocationsRepository {

    private JdbcTemplate template;

    public LocationsRepository(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public Location createLocation(String name, double lat, double lon) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(new PreparedStatementCreator() {
                            @Override
                            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                                PreparedStatement ps = connection.prepareStatement("insert into locations(name, lat, lon) values (?, ?, ?)"
                                    , Statement.RETURN_GENERATED_KEYS);
                                ps.setString(1, name);
                                ps.setDouble(2, lat);
                                ps.setDouble(3, lon);
                                return ps;
                            }
                        }, keyHolder
                );

        long id = keyHolder.getKey().longValue();

        return getLocationById(id);
    }

    public Location updateLocation(long id, String name, double lat, double lon) {
        template.update("update locations set name = ?, lat = ?, lon = ? where id = ?",
                name, lat, lon, id);
        return getLocationById(id);
    }

    public int deleteLocation(long id) {
        return template.update("delete from locations where id = ?",
                id);
    }

    public int sumOfLocations() {
        return template.queryForObject("select count(id) from locations", Integer.class);
    }

    public List<Location> listLocations() {
        return template.query("select id, name, lat, lon from locations order by name",
                new LocationRowMapper());
    }

    private static class LocationRowMapper implements RowMapper<Location> {
        @Override
        public Location mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            double lat = rs.getDouble("lat");
            double lon = rs.getDouble("lon");
            return new Location(id, name, lat, lon);
        }
    }

    public List<Location> listLocations(int start, int size) {
        return template.query("select id, name, lat, lon from locations order by name limit ?, ?",
                new LocationRowMapper(), start, size);
    }

    public Location getLocationById(long id) {
        return template.queryForObject("select id, name, lat, lon from locations where id = ?",
                new LocationRowMapper(),
                id
                );
    }

}
