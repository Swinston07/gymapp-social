package com.sterling.DAO;

import com.sterling.Models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sterling.Connection.DBConnection;
import com.sterling.Interfaces.UserDAOInterface;

public class UserDAO implements UserDAOInterface {
    @Override
    public void addUser(User user){
        String sql = "INSERT INTO users (email, username, password_hash, first_name, last_name, age, start_weight, start_body_fat_percentage, feet, inches, home_gym, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            ps.setDouble(12, user.getLatitude());
            ps.setDouble(13, user.getLongitude());

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
                    rs.getDouble("longitude")
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
                    rs.getString("home_gmy"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude")
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
                    rs.getDouble("longitude")
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
                        rs.getDouble("longitude")
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
        String sql = "UPDATE users SET email = ?, username = ?, password_hash = ?, first_name = ?, last_name = ?, age = ?, start_weight = ?, start_body_fat_percentage = ?, feet = ?, inches = ?, current_weight = ?, current_body_fat_percentage = ?, home_gym = ?, latitude = ?, longitude = ? WHERE id = ?";
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
            ps.setInt(13, user.getId());
            ps.setString(14, user.getHomeGym());
            ps.setDouble(15, user.getLatitude());
            ps.setDouble(16, user.getLongitude());

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
                        rs.getDouble("longitude")
                    )
                );
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return clientList;
    }
}
