package com.sterling.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.sterling.Connection.DBConnection;
import com.sterling.Interfaces.AssignedWorkoutDAOInterface;
import com.sterling.Models.AssignedWorkout;

public class AssignedWorkoutDAO implements AssignedWorkoutDAOInterface {

    @Override
    public void assignWorkout(AssignedWorkout workout){
        String sql = "INSERT INTO assigned_workouts (trainer_id, client_id, workout_name, description, is_completed) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, workout.getTrainerId());
            ps.setInt(2, workout.getClientId());
            ps.setString(3, workout.getWorkoutName());
            ps.setString(4, workout.getDescription());
            ps.setBoolean(5, workout.getIsCompleted() != null ? workout.getIsCompleted() : false);

            ps.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<AssignedWorkout> getWorkoutsByClientId(int clientId){
        String sql = "SELECT * FROM assigned_workouts WHERE client_id = ?";
        List<AssignedWorkout> workoutList = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, clientId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                // Timestamp completedTS = rs.getTimestamp("completed_on");
                // LocalDateTime completedOn = (completedTS != null) ? completedTS.toLocalDateTime() : null;

                workoutList.add(
                    new AssignedWorkout(
                        rs.getInt("assignment_id"),
                        rs.getInt("trainer_id"),
                        rs.getInt("client_id"),
                        rs.getString("workout_name"),
                        rs.getString("description"),
                        rs.getTimestamp("date_assigned"),
                        rs.getBoolean("is_completed"),
                        rs.getTimestamp("completed_on")
                    )
                );
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return workoutList;
    }

    @Override
    public List<AssignedWorkout> getWorkoutsByTrainerId(int trainerId){
        String sql = "SELECT * FROM assigned_workouts WHERE trainer_id = ?";
        List<AssignedWorkout> workoutList = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, trainerId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                // Timestamp completedTS = rs.getTimestamp("completed_on");
                // LocalDateTime completedOn = (completedTS != null) ? completedTS.toLocalDateTime() : null;

                workoutList.add(
                    new AssignedWorkout(
                        rs.getInt("assignment_id"),
                        rs.getInt("trainer_id"),
                        rs.getInt("client_id"),
                        rs.getString("workout_name"),
                        rs.getString("description"),
                        rs.getTimestamp("date_assigned"),
                        rs.getBoolean("is_completed"),
                        rs.getTimestamp("completed_on")
                    )
                );
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return workoutList;
    }

    @Override
    public boolean markWorkoutCompleted(int assignmentId){
        String sql = "UPDATE assigned_workouts SET is_completed = TRUE, completed_on = CURRENT_TIMESTAMP WHERE assignment_id = ?";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setInt(1, assignmentId);

            return ps.executeUpdate()>0;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public AssignedWorkout getById(int assignmentId){
        String sql = "SELECT * FROM assigned_workouts WHERE assignment_id = ?";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, assignmentId);
            
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                // Timestamp completedTS = rs.getTimestamp("completed_on");
                // LocalDateTime completedOn = (completedTS != null) ? completedTS.toLocalDateTime() : null;

                return new AssignedWorkout(
                    rs.getInt("assignment_id"), 
                    rs.getInt("trainer_id"), 
                    rs.getInt("client_id"), 
                    rs.getString("workout_name"), 
                    rs.getString("description"), 
                    rs.getTimestamp("date_assigned"), 
                    rs.getBoolean("is_completed"),
                    rs.getTimestamp("completed_on")
                );
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteAssignment(int assignmentId){
        String sql = "DELETE FROM assigned_workouts WHERE assignment_id = ?";
        
        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, assignmentId);

            return ps.executeUpdate()>0;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public AssignedWorkout getWorkoutByClientIdAndDate(int clientId, Timestamp date){
        String sql = "SELET * FROM assigned_workouts WHERE client_id = ? AND date_assigned = ? LIMIT 1";
        AssignedWorkout assignedWorkout;

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, clientId);
            ps.setTimestamp(2, date);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                assignedWorkout = new AssignedWorkout();

                assignedWorkout.setAssignmentId(rs.getInt("assignment_id"));
                assignedWorkout.setTrainerId(rs.getInt("trainer_id"));
                assignedWorkout.setClientId(rs.getInt("client_id"));
                assignedWorkout.setWorkoutName(rs.getString("workout_name"));
                assignedWorkout.setDescription(rs.getString("description"));
                assignedWorkout.setDateAssigned(rs.getTimestamp("date_assigned"));
                assignedWorkout.setIsCompleted(rs.getBoolean("is_completed"));
                assignedWorkout.setCompletedOn(rs.getTimestamp("completed_on"));

                return assignedWorkout;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
