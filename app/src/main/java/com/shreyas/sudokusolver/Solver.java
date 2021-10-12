package com.shreyas.sudokusolver;

import java.util.ArrayList;

public class Solver {
    int[][] board;
    ArrayList<ArrayList<Object>> emptyBoxIndex;

    int selected_row;
    int selected_column;

    Solver(){
        selected_row=-1;
        selected_column=-1;

        board= new int[9][9];

        for(int r=0;r<9;r++){
            for(int c=0;c<9;c++){
                board[r][c]=0;
                //loop through all our box in board and assign it to 0.
            }
        }
        emptyBoxIndex = new ArrayList<>();
    }

    //to extract rows and columns of a empty box
    public void getEmptyBoxIndexs(){
        for(int r=0;r<9;r++){
            for(int c=0;c<9;c++){
                if(this.board[r][c]==0){
                    this.emptyBoxIndex.add(new ArrayList<>());
                    //to get the most recently added arraylist from emptybox and assign new r,c value.
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size()-1).add(r);
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size()-1).add(c);
                }
            }
        }
    }

    //to update game board - set new number or clear.
    public void setNumberPos(int num){
        if(this.selected_row!=-1 && this.selected_column!=-1){
            //if same number is entered clear selection | set to 0.
            if(this.board[this.selected_row-1][this.selected_column-1]==num){
                this.board[this.selected_row-1][this.selected_column-1]=0;
            }
            else{
                //if valid then put the selected button value to selected box.
                this.board[this.selected_row-1][this.selected_column-1]=num;
            }
        }
    }

    public String checkValidrow(){
        String str="";
        for(int r=0;r<9;r++){
            boolean flag=true;
            for(int c=0;c<9;c++){
                if(this.board[r][c]!=0){
                    for(int ck=c+1;ck<9;ck++){
                        if(board[r][c]==board[r][ck] && flag){
                            if(str==""){
                                str="Identical numbers in row ";
                            }
                            str+=""+ (r+1)+",";
                            flag=false;
                        }
                    }
                }
            }
        }
        return str;
    }

    public String checkValidcolumn(){
        String str="";
        for(int c=0;c<9;c++){
            boolean flag=true;
            for(int r=0;r<9;r++){
                if(this.board[r][c]!=0){
                    for(int rk=r+1;rk<9;rk++){
                        if(board[r][c]==board[rk][c] && flag){
                            if(str==""){
                                str="Identical numbers in column ";
                            }
                            str+=""+ (c+1)+",";
                            flag=false;
                        }
                    }
                }
            }
        }
        return str;
    }

    public String checkValidbox(){
        String str="";
        int i=0,j=0;
        int count=0;
        boolean y=true;
        int rval=0,cval=0;
        while(y) {
            count++;
            boolean flag=true;
            for (int r = i; r < i + 3; r++) {
                rval = r;
                for (int c = j; c < j + 3; c++) {
                    cval = c;
                    //here 0,0
                    if(board[r][c]!=0){
                        int m=r;
                        int n=c;
                        boolean end=true;
                        while(end){
                            if((n+1)%3==0 && (m+1)%3==0){
                                end=false;
                            }
                            else if((n+1)%3==0){
                                n=n-2;
                                m=m+1;
                            }
                            else{
                                n=n+1;
                            }
                            if(board[r][c]==board[m][n] && flag && end){
                                if(str==""){
                                    str+="Identical numbers in box ";
                                }
                                str+=count+",";
                                flag=false;
                                end=false;
                                //end=false;
                            }
                        }
                    }
                }
            }

            if(rval+1==9 && cval+1==9){
                y=false;
            }
            else if(cval+1==9){
                j=0;
                i=i+3;
            }
            else{
                j=j+3;
            }
        }
        return str;
    }

    private boolean check(int row, int col){
        if(this.board[row][col]!=0){
            for(int i=0;i<9;i++){
                //to check each row
                if(this.board[i][col]==this.board[row][col] && row !=i){
                    return false;
                }
                //to check each column
                if(this.board[row][i]==this.board[row][col] && col !=i){
                    return false;
                }
            }

            //to check each 3*3 box
            int br= ((int)row/3)*3;
            int cr=((int)col/3)*3;
            for(int k=br;k<(br+3);k++){
                for(int h=cr;h<(cr+3);h++){
                    if(this.board[k][h]==this.board[row][col] && (k!=row && h!=col)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean solve(SudokuBoard sudokuBoard){
        int row=-1;
        int col=-1;
        for(int r=0;r<9;r++){
            for(int c=0;c<9;c++){
                if(this.board[r][c]==0){
                    row=r;
                    col=c;
                    break;
                }
            }
        }
        if(row==-1 || col==-1){
            return true;
        }

        for(int i=1;i<10;i++){
            this.board[row][col]=i;
            sudokuBoard.invalidate();
            //check if that i value is valid in board
            if(check(row,col)){
                //call recursively solve method till all cells are filled.
                if(solve(sudokuBoard)) {
                    return true;
                }
            }
            //backtracking
            this.board[row][col]=0;
        }
        return false;
    }

    public void reset(){
        //to reset our board
        for(int r=0;r<9;r++){
            for(int c=0;c<9;c++){
                board[r][c]=0;
            }
        }
        //to reset our array
        this.emptyBoxIndex=new ArrayList<>();
    }


    public int[][] getBoard(){
        return this.board;
    }

    public ArrayList<ArrayList<Object>> getEmptyBoxIndex()
    {
        return this.emptyBoxIndex;
    }

    public int getSelectedRow(){
        return selected_row;
    }
    public int getSelectedColumn(){
        return selected_column;
    }

    public void setSelectedRow(int r){
        selected_row=r;
    }

    public void setSelectedColumn(int c){
        selected_column=c;
    }
}
