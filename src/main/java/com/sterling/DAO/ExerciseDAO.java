package com.sterling.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sterling.Connection.DBConnection;
import com.sterling.Interfaces.ExerciseDAOInterface;
import com.sterling.Models.Exercise;

public class ExerciseDAO implements ExerciseDAOInterface {
    @Override
    public void addExercise(Exercise exercise){
        String sql = "INSERT INTO exercises(exercise_name, weight, sets, reps, user_id) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, exercise.getName());
            ps.setFloat(2, exercise.getWeight());
            ps.setInt(3, exercise.getSets());
            ps.setInt(4, exercise.getReps());
            ps.setInt(5, exercise.getUserId());

            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Exercise getExerciseById(int id){
        String sql = "SELECT * FROM exercises WHERE exercise_id = ?";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new Exercise(
                    rs.getInt("exercise_id"),
                    rs.getString("exercise_name"),
                    rs.getFloat("weight"),
                    rs.getInt("sets"),
                    rs.getInt("reps"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("created_on"),
                    rs.getTimestamp("updated_on")
                );
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Exercise> getExercisesByUserId(int userId){
        String sql = "SELECT * FROM exercises WHERE user_id = ?";
        List<Exercise> exercises = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                exercises.add(
                    new Exercise(
                        rs.getInt("exercise_id"),
                        rs.getString("exercise_name"),
                        rs.getFloat("weight"),
                        rs.getInt("sets"),
                        rs.getInt("reps"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("created_on"),
                        rs.getTimestamp("updated_on")
                    )
                );
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return exercises;
    }

    @Override
    public List<Exercise> getAllExercises(){
        String sql = "SELECT * FROM exercises";
        List<Exercise> exercises = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Exercise exercise = new Exercise(
                    rs.getInt("exercise_id"),
                    rs.getString("exercise_name"),
                    rs.getFloat("weight"),
                    rs.getInt("sets"),
                    rs.getInt("reps"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("created_on"),
                    rs.getTimestamp("updated_on")
                );
                exercises.add(exercise);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return exercises;
    }
    
    @Override
    public boolean updateExercise(Exercise exercise){
        String sql = "UPDATE exercises SET exercise_name = ?, weight = ?, sets = ?, reps = ? WHERE exercise_id = ?";
        boolean updated = false;

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, exercise.getName());
            ps.setFloat(2, exercise.getWeight());
            ps.setInt(3, exercise.getSets());
            ps.setInt(4, exercise.getReps());
            ps.setInt(5, exercise.getId());

            int affectedRows = ps.executeUpdate();

            updated = affectedRows > 0;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return updated;
    }

    @Override
    public boolean deleteExercise(int id){
        String sql = "DELETE FROM exercises WHERE exercise_id = ?";
        boolean deleted = false;

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            deleted = affectedRows>0;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return deleted;
    }
}
