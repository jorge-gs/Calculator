package com.example.universidad.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private enum State {
        INPUT, DISPLAY
    }

    private State state = State.DISPLAY;
    private CalculatorBrain brain;
    private TextView screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = this.findViewById(R.id.screen);
        this.screen = view instanceof TextView ? (TextView)view : null;
        if (this.screen != null) {
            this.screen.setText("0");
        }
    }

    public void onDigit(View view) {
        String newDigits = "";

        Button button = view instanceof Button ? (Button) view : null;
        if (button != null) {
            newDigits = button.getText().toString();
        }

        String text = this.screen.getText().toString();
        switch (this.state) {
            case INPUT:
                if (text.charAt(0) != '0') {
                    this.screen.setText(text + newDigits);
                    break;
                }
            case DISPLAY:
                this.state = State.INPUT;
                this.screen.setText("" + newDigits);
                break;
        }
    }

    public void onOperator(View view) {
        CalculatorBrain newBrain = null;
        switch (view.getId()) {
            case R.id.buttonAddition:
                newBrain = new CalculatorBrain() {
                    @Override
                    public int getResult() throws DivisionByZero {
                        return this.left + this.right;
                    }
                };
                break;
            case R.id.buttonSubtraction:
                newBrain = new CalculatorBrain() {
                    @Override
                    public int getResult() throws DivisionByZero {
                        return this.left - this.right;
                    }
                };
                break;
            case R.id.buttonMultiplication:
                newBrain = new CalculatorBrain() {
                    @Override
                    public int getResult() throws DivisionByZero {
                        return this.left * this.right;
                    }
                };
                break;
            case R.id.buttonDivision:
                newBrain = new CalculatorBrain() {
                    @Override
                    public int getResult() throws DivisionByZero {
                        if (this.right == 0) { throw new DivisionByZero(); }
                        return this.left / this.right;
                    }
                };
                break;
            default:
                return;
        }

        try {
            int number = Integer.parseInt(this.screen.getText().toString());

            if (this.brain == null) {
                this.brain = newBrain;
                this.brain.setLeft(number);
            } else {
                this.brain.setRight(number);

                int result = this.brain.getResult();
                this.screen.setText("" + result);

                this.brain = newBrain;
                this.brain.setLeft(result);
            }
        } catch (NumberFormatException exception) {
            this.brain = null;
            this.screen.setText("0");
        } catch (CalculatorBrain.DivisionByZero exception) {
            this.brain = null;
            this.screen.setText("NaN");
        } finally {
            this.state = State.DISPLAY;
        }
    }

    public void onAction(View view) {
        switch (view.getId()) {
            case R.id.buttonNegate:
                String text = this.screen.getText().toString();
                if (text.charAt(0) == '-') {
                    this.screen.setText(text.replace("-", ""));
                } else {
                    this.screen.setText("-" + text);
                }
                break;
            case R.id.buttonClear:
                this.brain = null;
                this.state = State.DISPLAY;
                this.screen.setText("0");
                break;
            case R.id.buttonEqual:
                if (this.brain != null) {
                    try {
                        int number = Integer.parseInt(this.screen.getText().toString());
                        this.brain.setRight(number);

                        int result = this.brain.getResult();
                        this.screen.setText("" + result);
                    } catch (CalculatorBrain.DivisionByZero exception) {
                        this.screen.setText("NaN");
                    } catch (NumberFormatException exception) {
                        this.screen.setText("0");
                    } finally {
                        this.brain = null;
                        this.state = State.DISPLAY;
                    }
                }
                break;
            default:
                return;
        }
    }
}
