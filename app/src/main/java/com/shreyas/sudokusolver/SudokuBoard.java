package com.shreyas.sudokusolver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SudokuBoard extends View {
    private final int boardColor;
    private final int cellFillColor;
    private final int cellHighlightColor;
    private final int letterColor;
    private final int letterColorSolve;

    private final Paint boardColorPaint= new Paint();
    private final Paint cellFillColorPaint= new Paint();
    private final Paint cellHighlightColorPaint= new Paint();
    private final Paint letterPaint= new Paint();

    //to get height of the text for drawing
    private final Rect letterPaintBounds = new Rect();

    private final Solver solver=new Solver();

    private int cellsize;

    public SudokuBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a=context.getTheme().obtainStyledAttributes(attrs,R.styleable.SudokuBoard,0,0);
        //this array will have all the styles we have defined in attrs resource file.
        //to get each style value we use try finally.
        try{
            boardColor=a.getInteger(R.styleable.SudokuBoard_boardColor,0);
            //extracting board color int value from our typed array and setting it.
            cellFillColor=a.getInteger(R.styleable.SudokuBoard_cellFillColor,0);
            cellHighlightColor=a.getInteger(R.styleable.SudokuBoard_cellsHighlightColor,0);
            letterColor=a.getInteger(R.styleable.SudokuBoard_letterColor,0);
            letterColorSolve=a.getInteger(R.styleable.SudokuBoard_letterColorSolve,0);
        }finally {
            a.recycle();
            //will free up some memory for our app
        }
    }

    //for taking measurements of user's device
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int dimension = Math.min(width,height);
        //will get height and width of user display and set dimension accordingly.
        cellsize=dimension/9;
        //get each cell size
        setMeasuredDimension(dimension,dimension);
    }


    //for drawing rectangle, rows, cols with correct paints.
    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(16);
        //this is our brush
        boardColorPaint.setColor(boardColor);
        //color
        boardColorPaint.setAntiAlias(true);
        //sharp drawing

        cellFillColorPaint.setStyle(Paint.Style.FILL);
        cellFillColorPaint.setColor(cellFillColor);
        cellFillColorPaint.setAntiAlias(true);

        cellHighlightColorPaint.setStyle(Paint.Style.FILL);
        cellHighlightColorPaint.setColor(cellHighlightColor);
        cellHighlightColorPaint.setAntiAlias(true);

        letterPaint.setStyle(Paint.Style.FILL);
        letterPaint.setAntiAlias(true);
        letterPaint.setColor(letterColor);

        colorCell(canvas,solver.getSelectedRow(),solver.getSelectedColumn());
        canvas.drawRect(0,0,getWidth(),getHeight(),boardColorPaint);
        drawBoard(canvas);
        drawNumbers(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isValid;
        float x = event.getX();
        float y= event.getY();
        //extracting x , y cordinates from tap event from user screen.

        int action=event.getAction();
        //differentiate the dif types of actions
        if(action==MotionEvent.ACTION_DOWN){
            //action is click event
            solver.setSelectedRow((int) Math.ceil(y/cellsize));
            solver.setSelectedColumn((int) Math.ceil(x/cellsize));
            //to recieve an integer value of x,y rather than decimal. set it to solver instance.
            isValid=true;
        }else{
            isValid=false;
        }

        return isValid;
    }

    //to draw text in box
    private void drawNumbers(Canvas canvas){
        letterPaint.setTextSize(cellsize);
        for(int r=0;r<9;r++){
            for(int c=0;c<9;c++){
                //dont draw text if value=0. keep it empty
                if(solver.getBoard()[r][c]!=0){
                    //get the number user assigned in board
                    String text=Integer.toString(solver.getBoard()[r][c]);
                    float width, height;

                    letterPaint.getTextBounds(text,0,text.length(),letterPaintBounds);
                    width=letterPaint.measureText(text);
                    height=letterPaintBounds.height();

                    canvas.drawText(text,(c*cellsize)+((cellsize-width)/2),(r*cellsize+cellsize)-((cellsize - height)/2), letterPaint);
                }
            }
        }
        letterPaint.setColor(letterColorSolve);
        for(ArrayList<Object> letter: solver.getEmptyBoxIndex()){
            int r=(int)letter.get(0);
            int c=(int)letter.get(1);
            String text=Integer.toString(solver.getBoard()[r][c]);
            float width, height;

            letterPaint.getTextBounds(text,0,text.length(),letterPaintBounds);
            width=letterPaint.measureText(text);
            height=letterPaintBounds.height();

            canvas.drawText(text,(c*cellsize)+((cellsize-width)/2),(r*cellsize+cellsize)-((cellsize - height)/2), letterPaint);
        }
    }

    private void colorCell(Canvas canvas, int r, int c){
        if(solver.getSelectedColumn()!=-1 && solver.getSelectedRow()!=-1){
            canvas.drawRect((c-1)*cellsize,0,c*cellsize,cellsize*9,cellHighlightColorPaint);
            //to highlight selected column
            canvas.drawRect(0,(r-1)*cellsize,cellsize*9,r*cellsize,cellHighlightColorPaint);
            //to highlight selected row
            canvas.drawRect((c-1)*cellsize,(r-1)*cellsize,c*cellsize,r*cellsize,cellHighlightColorPaint);
            //to hight selected box.

            invalidate();
            //to refresh our sudoku board
        }
    }

    private void drawThickLine(){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(10);
        boardColorPaint.setColor(boardColor);
        //called for every 3rd row,column
    }
    private  void drawThinLine(){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(4);
        boardColorPaint.setColor(boardColor);
    }

    private void drawBoard(Canvas canvas){
        for(int c=0;c<10;c++){
            if(c%3==0){
                drawThickLine();
            }
            else{
                drawThinLine();
            }
            canvas.drawLine(cellsize*c,0,cellsize*c,getWidth(),boardColorPaint);
            //for columns

        }
        for(int r=0;r<10;r++) {
            if (r % 3 == 0) {
                drawThickLine();
            } else {
                drawThinLine();
            }
            canvas.drawLine(0, cellsize * r,getWidth(),cellsize*r, boardColorPaint);
            //for rows
        }
    }

    public Solver getSolver(){
        return this.solver;
    }


}
