package com.sterling.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sterling.Connection.DBConnection;
import com.sterling.Interfaces.AssignedExerciseDAOInterface;
import com.sterling.Models.AssignedExercise;

public class AssignedExerciseDAO implements AssignedExerciseDAOInterface {

    @Override
    public void addAssignedExercise(AssignedExercise assignedExercise){
        String sql = "INSERT INTO assigned_exercises (assignment_id, exercise_name, weight, sets, reps) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, assignedExercise.getAssignmentId());
            ps.setString(2, assignedExercise.getExerciseName());
            ps.setFloat(3, assignedExercise.getWeight());
            ps.setInt(4, assignedExercise.getSets());
            ps.setInt(5, assignedExercise.getReps());

            ps.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<AssignedExercise> getAssignedExercisesByAssignmentId(int assignmentId){
        String sql = "SELECT * FROM assigned_exercises WHERE assignment_id = ?";
        List<AssignedExercise> exercises = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, assignmentId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                exercises.add(
                    new AssignedExercise(
                        rs.getInt("assigned_exercise_id"),
                        rs.getInt("assignment_id"),
                        rs.getString("exercise_name"),
                        rs.getFloat("weight"),
                        rs.getInt("sets"),
                        rs.getInt("reps"),
                        rs.getTimestamp("created_on"),
                        rs.getBoolean("is_completed")
                    )
                );
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return exercises;
    }

    @Override
    public boolean updateAssignedExercise(AssignedExercise assignedExercise){
        String sql = "UPDATE assigned_exercises SET exercise_name = ?, weight = ?, sets = ?, reps = ? WHERE assigned_exercise_id = ?";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, assignedExercise.getExerciseName());
            ps.setFloat(2, assignedExercise.getWeight());
            ps.setInt(3, assignedExercise.getSets());
            ps.setInt(4, assignedExercise.getReps());
            ps.setInt(5, assignedExercise.getAssignedExerciseId());

            return ps.executeUpdate() > 0;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteAssignedExercise(int assignmentExerciseId) {
        String sql = "DELETE FROM assigned_exercises WHERE assigned_exercise_id = ?";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, assignmentExerciseId);

            return ps.executeUpdate() > 0;
        } catch( SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public AssignedExercise getAssignedExerciseById(int assignedExerciseId){
        String sql = "SELECT * FROM assigned_exercises WHERE assigned_exercise_id = ?";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, assignedExerciseId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new AssignedExercise(
                    rs.getInt("assigned_exercise_id"),
                    rs.getInt("assignment_id"),
                    rs.getString("exercise_name"),
                    rs.getFloat("weight"),
                    rs.getInt("sets"),
                    rs.getInt("reps"),
                    rs.getTimestamp("created_on"),
                    rs.getBoolean("is_completed")
                    );
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean markExerciseCompleted(int assignedExerciseId){
        String sql = "UPDATE assigned_exercises SET is_completed = TRUE WHERE assigned_exercise_id = ?";
        
        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, assignedExerciseId);
            int affectedRows = ps.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
