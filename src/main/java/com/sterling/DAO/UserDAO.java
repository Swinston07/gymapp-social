package com.sterling.DAO;

import com.sterling.Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.sterling.Connection.DBConnection;
import com.sterling.Interfaces.UserDAOInterface;

public class UserDAO implements UserDAOInterface {
    @Override
    public void addUser(User user){
        String sql = "INSERT INTO users (email, username, password_hash, first_name, last_name, age, start_weight, start_body_fat_percentage, feet, inches, home_gym, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getFirstName());
            ps.setString(5, user.getLastName());
            ps.setInt(6, user.getAge());
            ps.setFloat(7, user.getStartWeight());
            ps.setFloat(8, user.getStartBodyFatPercentage());
            ps.setInt(9, user.getHeightFeet());
            ps.setInt(10, user.getHeightInches());
            ps.setString(11,user.getHomeGym());

            if(user.getLatitude() != null)
                ps.setDouble(12, user.getLatitude());
            else
                ps.setNull(12, Types.DOUBLE);

            if(user.getLongitude() != null)
                ps.setDouble(13, user.getLongitude());
            else
                ps.setNull(13, Types.DOUBLE);

            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();;
        }
    }

    @Override
    public User getUserById(int id){
        String sql = "SELECT * FROM users WHERE id = ? ";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new User(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getFloat("start_weight"),
                    rs.getFloat("start_body_fat_percentage"),
                    rs.getInt("feet"),
                    rs.getInt("inches"),
                    rs.getFloat("current_weight"),
                    rs.getFloat("current_body_fat_percentage"),
                    rs.getTimestamp("created_on"),
                    rs.getString("role"),
                    rs.getInt("trainer_id"),
                    rs.getString("home_gym"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getBoolean("is_working_out"),
                    rs.getString("about_me"),
                    rs.getString("experience_level"),
                    rs.getString("lifestyle"),
                    rs.getString("consistency")
                );
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserByEmail(String email){
        String sql = "SELECT * FROM users WHERE email = ?";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new User(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getFloat("start_weight"),
                    rs.getFloat("start_body_fat_percentage"),
                    rs.getInt("feet"),
                    rs.getInt("inches"),
                    rs.getFloat("current_weight"),
                    rs.getFloat("current_body_fat_percentage"),
                    rs.getTimestamp("created_on"),
                    rs.getString("role"),
                    rs.getInt("trainer_id"),
                    rs.getString("home_gym"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getBoolean("is_working_out"),
                    rs.getString("about_me"),
                    rs.getString("experience_level"),
                    rs.getString("lifestyle"),
                    rs.getString("consistency")
                );
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username){
        String sql = "SELECT * FROM users WHERE username = ?";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new User(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getFloat("start_weight"),
                    rs.getFloat("start_body_fat_percentage"),
                    rs.getInt("feet"),
                    rs.getInt("inches"),
                    rs.getFloat("current_weight"),
                    rs.getFloat("current_body_fat_percentage"),
                    rs.getTimestamp("created_on"),
                    rs.getString("role"),
                    rs.getInt("trainer_id"),
                    rs.getString("home_gym"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getBoolean("is_working_out"),
                    rs.getString("about_me"),
                    rs.getString("experience_level"),
                    rs.getString("lifestyle"),
                    rs.getString("consistency")
                );
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAllUsers(){
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                users.add (
                        new User(
                        rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getFloat("start_weight"),
                    rs.getFloat("start_body_fat_percentage"),
                    rs.getInt("feet"),
                    rs.getInt("inches"),
                    rs.getFloat("current_weight"),
                    rs.getFloat("current_body_fat_percentage"),
                    rs.getTimestamp("created_on"),
                    rs.getString("role"),
                    rs.getInt("trainer_id"),
                    rs.getString("home_gym"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getBoolean("is_working_out"),
                    rs.getString("about_me"),
                    rs.getString("experience_level"),
                    rs.getString("lifestyle"),
                    rs.getString("consistency")
                    )
                );
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean updateUser(User user){
        String sql = "UPDATE users SET email = ?, username = ?, password_hash = ?, first_name = ?, last_name = ?, age = ?, start_weight = ?, start_body_fat_percentage = ?, feet = ?, inches = ?, current_weight = ?, current_body_fat_percentage = ?, home_gym = ?, latitude = ?, longitude = ?, " +
        "about_me = ?, experience_level = ?, lifestyle = ?, consistency = ?  WHERE id = ?";
        boolean updated = false;

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getFirstName());
            ps.setString(5, user.getLastName());
            ps.setInt(6, user.getAge());
            ps.setFloat(7, user.getStartWeight());
            ps.setFloat(8, user.getStartBodyFatPercentage());
            ps.setInt(9, user.getHeightFeet());
            ps.setInt(10, user.getHeightInches());
            ps.setFloat(11, user.getCurrentWeight());
            ps.setFloat(12, user.getCurrentBodyFatPercentage());
            ps.setString(13, user.getHomeGym());
            ps.setDouble(14, user.getLatitude());
            ps.setDouble(15, user.getLongitude());
            ps.setString(16, user.getAboutMe());
            ps.setString(17, user.getExperienceLevel());
            ps.setString(18, user.getLifestyle());
            ps.setString(19, user.getConsistency());
            ps.setInt(20, user.getId());

            int rowsAffected = ps.executeUpdate();
            updated = rowsAffected > 0;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return updated;
    }

    @Override
    public boolean deleteUser(int id){
        String sql = "DELETE FROM users WHERE id = ?";
        boolean deleted = false;

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            deleted = rowsAffected > 0;

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return deleted;
    }

    @Override
    public boolean updateUserRole(int userId, String role){
        String sql = "UPDATE users SET role = ? WHERE id = ?";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, role);
            ps.setInt(2, userId);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected>0;

        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean assignClientToTrainer(int clientId, int trainerId){
        String sql = "UPDATE users SET trainer_id = ? WHERE id = ?";
        boolean updated = false;

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, trainerId);
            ps.setInt(2, clientId);

            int rowsAffected = ps.executeUpdate();

            updated = rowsAffected > 0;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return updated;
    }

    @Override
    public List<User> getClientsByTrainerId(int trainerId){
        String sql = "SELECT * FROM users WHERE trainer_id = ?";
        List<User> clientList = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, trainerId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                clientList.add(
                    new User(
                        rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getFloat("start_weight"),
                    rs.getFloat("start_body_fat_percentage"),
                    rs.getInt("feet"),
                    rs.getInt("inches"),
                    rs.getFloat("current_weight"),
                    rs.getFloat("current_body_fat_percentage"),
                    rs.getTimestamp("created_on"),
                    rs.getString("role"),
                    rs.getInt("trainer_id"),
                    rs.getString("home_gym"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getBoolean("is_working_out"),
                    rs.getString("about_me"),
                    rs.getString("experience_level"),
                    rs.getString("lifestyle"),
                    rs.getString("consistency")
                    )
                );
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return clientList;
    }

    @Override
    public boolean updateHomeGym(int userId, String homeGym, Double latitude, Double longitude){
        String sql = "UPDATE users SET home_gym = ?, latitude = ?, longitude = ? WHERE id = ?";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, homeGym);
            ps.setDouble(2, latitude);
            ps.setDouble(3, longitude);
            ps.setInt(4, userId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean toggleWorkoutStatus(int userId){
        String sql = "UPDATE users SET is_working_out = NOT is_working_out WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> findMatchingUsers(String homeGym, String role, int userId){
        StringBuilder sql = new StringBuilder("SELECT * FROM users WHERE home_gym = ? AND id != ?");
        List<User> matches = new ArrayList<>();

        if(role != null){
            sql.append(" AND role = ?");
        }

        try (Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql.toString());

            ps.setString(1, homeGym);
            ps.setInt(2, userId);

            if(role != null){
                ps.setString(3, role);
            }

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                matches.add(
                    new User(
                        rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getFloat("start_weight"),
                    rs.getFloat("start_body_fat_percentage"),
                    rs.getInt("feet"),
                    rs.getInt("inches"),
                    rs.getFloat("current_weight"),
                    rs.getFloat("current_body_fat_percentage"),
                    rs.getTimestamp("created_on"),
                    rs.getString("role"),
                    rs.getInt("trainer_id"),
                    rs.getString("home_gym"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getBoolean("is_working_out"),
                    rs.getString("about_me"),
                    rs.getString("experience_level"),
                    rs.getString("lifestyle"),
                    rs.getString("consistency")
                    )
                );
            }
        } catch (SQLException e){
            e.printStackTrace();;
        }
        return matches;
    }

    @Override
    public List<User> findUsersByHomeGym(String homeGym, int userId){
        String sql = "SELECT * FROM users WHERE home_gym = ? AND id != ?";
        List<User> userList = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, homeGym);
            ps.setInt(2, userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                userList.add(
                    new User(
                        rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getFloat("start_weight"),
                    rs.getFloat("start_body_fat_percentage"),
                    rs.getInt("feet"),
                    rs.getInt("inches"),
                    rs.getFloat("current_weight"),
                    rs.getFloat("current_body_fat_percentage"),
                    rs.getTimestamp("created_on"),
                    rs.getString("role"),
                    rs.getInt("trainer_id"),
                    rs.getString("home_gym"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getBoolean("is_working_out"),
                    rs.getString("about_me"),
                    rs.getString("experience_level"),
                    rs.getString("lifestyle"),
                    rs.getString("consistency")
                    )
                );
            }

            return userList;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findUsersByHomeGymAndRole(String homeGym, String role, int userId){
        String sql = "SELECT * FROM users WHERE home_gym = ? AND role = ? AND id != ?";
        List<User> userList = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1, homeGym);
            ps.setString(2, role);
            ps.setInt(3, userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                userList.add(
                    new User(
                        rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getInt("age"),
                    rs.getFloat("start_weight"),
                    rs.getFloat("start_body_fat_percentage"),
                    rs.getInt("feet"),
                    rs.getInt("inches"),
                    rs.getFloat("current_weight"),
                    rs.getFloat("current_body_fat_percentage"),
                    rs.getTimestamp("created_on"),
                    rs.getString("role"),
                    rs.getInt("trainer_id"),
                    rs.getString("home_gym"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getBoolean("is_working_out"),
                    rs.getString("about_me"),
                    rs.getString("experience_level"),
                    rs.getString("lifestyle"),
                    rs.getString("consistency")
                    )
                );
            }

            return userList;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
