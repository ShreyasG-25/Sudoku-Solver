package com.shreyas.sudokusolver;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SudokuBoard gameBoard;
    private Solver gameBoardSolver;
    public TextView txtrow,txtcolumn,txtbox,txtvalid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtrow=findViewById(R.id.tvrow);
        txtcolumn=findViewById(R.id.tvcol);
        txtbox=findViewById(R.id.tvbox);
        txtvalid=findViewById(R.id.tvvalid);
        gameBoard=findViewById(R.id.SudokuBoard);
        gameBoardSolver = gameBoard.getSolver();
    }

    public void numberClick(View view){
        switch(view.getId()){
            case R.id.button1:gameBoardSolver.setNumberPos(1);
            break;
            case R.id.button2:gameBoardSolver.setNumberPos(2);
            break;
            case R.id.button3:gameBoardSolver.setNumberPos(3);
                break;
            case R.id.button4:gameBoardSolver.setNumberPos(4);
                break;
            case R.id.button5:gameBoardSolver.setNumberPos(5);
                break;
            case R.id.button6:gameBoardSolver.setNumberPos(6);
                break;
            case R.id.button7:gameBoardSolver.setNumberPos(7);
                break;
            case R.id.button8:gameBoardSolver.setNumberPos(8);
                break;
            case R.id.button9:gameBoardSolver.setNumberPos(9);
                break;
            default: break;
        }
        gameBoard.invalidate();
    }

    public void btnClear(View view){
        gameBoardSolver.reset();
        gameBoard.invalidate();
        txtrow.setText("");
        txtcolumn.setText("");
        txtbox.setText("");
        txtvalid.setText("");
    }

    public void btnCheck(View view){
        String row =gameBoardSolver.checkValidrow();
        String column=gameBoardSolver.checkValidcolumn();
        String box=gameBoardSolver.checkValidbox();
        txtrow.setText(row);
        txtcolumn.setText(column);
        txtbox.setText(box);
        txtvalid.setText("");
        if(row=="" && column=="" && box==""){
            txtvalid.setText("Valid Combination!!");
        }
    }

    public void btnSolve(View view){
        String row =gameBoardSolver.checkValidrow();
        String column=gameBoardSolver.checkValidcolumn();
        String box=gameBoardSolver.checkValidbox();
        if(row=="" && column=="" && box==""){
            gameBoardSolver.getEmptyBoxIndexs();
            Solverthread solverthread=new Solverthread();
            new Thread(solverthread).start();
            gameBoard.invalidate();
            txtrow.setText("");
            txtcolumn.setText("");
            txtbox.setText("");
            txtvalid.setText("");
        }
        else{
            txtrow.setText("Combinations are not valid!!");
            txtcolumn.setText("Use Check Button to view invalid entries");
            txtbox.setText("");
        }

    }
    class Solverthread implements Runnable{
        @Override
        public void run() {
            gameBoardSolver.solve(gameBoard);
        }
    }
}