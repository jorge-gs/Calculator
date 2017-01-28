package com.example.universidad.calculator;

/**
 * Created by universidad on 1/28/17.
 */

public abstract class CalculatorBrain {
    public class DivisionByZero extends Exception { }

    //Protected Properties
    protected int left;
    protected int right;

    //Getters
    public int getLeft() { return  this.left; }
    public int getRight() { return  this.right; }
    public abstract int getResult() throws DivisionByZero;

    //Setters
    public void setLeft(int value) { this.left = value; }
    public void setRight(int value) { this.right = value; }
}
