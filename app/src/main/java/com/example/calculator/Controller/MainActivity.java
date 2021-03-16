package com.example.calculator.Controller;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculator.R;

public class MainActivity extends AppCompatActivity {

    private TextView result, current;
    private Button ac, back,  division, seven, eight, nine, multiple, four, five, six, minus, one, two, three, plus,
                   zero, dot, equal;
    private String curr, res;
    public static final String BUNDLE_STATE_CURRENT = "currentOperation";
    public static final String BUNDLE_STATE_RESULT = "currentResult";
    private String display;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        current = findViewById(R.id.activity_current_input);
        result = findViewById(R.id.layout_result_output);
        ac = findViewById(R.id.layout_ac_btn);
        back = findViewById(R.id.layout_back_btn);
        division = findViewById(R.id.layout_division_btn);
        seven = findViewById(R.id.layout_7_btn);
        eight = findViewById(R.id.layout_8_btn);
        nine = findViewById(R.id.layout_9_btn);
        multiple = findViewById(R.id.layout_multiplication_btn);
        four = findViewById(R.id.layout_4_btn);
        five = findViewById(R.id.layout_5_btn);
        six = findViewById(R.id.layout_6_btn);
        minus = findViewById(R.id.layout_subtraction_btn);
        one = findViewById(R.id.layout_1_btn);
        two = findViewById(R.id.layout_2_btn);
        three = findViewById(R.id.layout_3_btn);
        plus = findViewById(R.id.layout_sum_btn);
        zero = findViewById(R.id.layout_0_btn);
        dot = findViewById(R.id.layout_dot_btn);
        equal = findViewById(R.id.layout_equal_btn);

       zero.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               curr = "0";
               showOne();
           }
       });

       one.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               curr = "1";
               showOne();
           }
       });

       two.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               curr = "2";
               showOne();
           }
       });

       three.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               curr = "3";
               showOne();
           }
       });

       four.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               curr = "4";
               showOne();
           }
       });

       five.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               curr = "5";
               showOne();
           }
       });

       six.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               curr = "6";
               showOne();
           }
       });

       seven.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               curr = "7";
               showOne();
           }
       });

       eight.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               curr = "8";
               showOne();
           }
       });

       nine.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               curr = "9";
               showOne();
           }
       });

       ac.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               display = current.getText().toString();
               String tvMain = result.getText().toString();
               if (!TextUtils.isEmpty(display) || !TextUtils.isEmpty(tvMain)) {
                   current.setText("");
                   result.setText("");
               }
           }
       });

       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               display = current.getText().toString();
               if (!TextUtils.isEmpty(display)) {

                  String erase = display.substring ( 0, display.length() - 1 );
                  current.setText(erase);
               }
           }
       });


       


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display = current.getText().toString();
                String[] splited = display.split("\\s+");
                String lastOne = splited[splited.length-1];
               boolean lastChar = lastOne.endsWith("+") || lastOne.endsWith("-")|| lastOne.endsWith("×")
                       || lastOne.endsWith("÷") || lastOne.endsWith(".");

               if ( lastChar == true || TextUtils.isEmpty(display)) {
                   current.getText().toString();
               }
               else {
                   curr = " + ";
                   showOne();

               }

            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display = current.getText().toString();
                String[] splited = display.split("\\s+");
                String lastOne = splited[splited.length-1];
                boolean lastChar = lastOne.endsWith("+") || lastOne.endsWith("-")|| lastOne.endsWith("×")
                        || lastOne.endsWith("÷") || lastOne.endsWith(".");

                if ( lastChar == true || TextUtils.isEmpty(display)) {
                    current.getText().toString();
                }
                else {
                    curr = " - ";
                    showOne();
                }
            }
        });

        multiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display = current.getText().toString();
                String[] splited = display.split("\\s+");
                String lastOne = splited[splited.length-1];
                boolean lastChar = lastOne.endsWith("+") || lastOne.endsWith("-")|| lastOne.endsWith("×")
                        || lastOne.endsWith("÷") || lastOne.endsWith(".");

                if ( lastChar == true || TextUtils.isEmpty(display)) {
                    current.getText().toString();
                }
                else {
                    curr = " × ";
                    showOne();
                }
            }

        });

        division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display = current.getText().toString();
                String[] splited = display.split("\\s+");
                String lastOne = splited[splited.length-1];
                boolean lastChar = lastOne.endsWith("+") || lastOne.endsWith("-")|| lastOne.endsWith("×")
                        || lastOne.endsWith("÷") || lastOne.endsWith(".");

                if ( lastChar == true || TextUtils.isEmpty(display)) {
                    current.getText().toString();
                }
                else {
                    curr = " ÷ ";
                    showOne();
                }
            }

        });

        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display = current.getText().toString();
                String[] splited = display.split("\\s+");
                String lastOne = splited[splited.length-1];
                boolean numeric = true;

                numeric = lastOne.indexOf(".") >= 0;

                boolean dot_inserted1 = (lastOne.indexOf(".") >= 0) && lastOne.endsWith("-") || lastOne.endsWith("+")
                        || lastOne.endsWith("×") || lastOne.endsWith("÷");




                if (TextUtils.isEmpty(display) || dot_inserted1 == true) {
                    curr = "0.";
                    showOne();
                }
                else if (numeric == false) {
                    curr = ".";
                    showOne();
                }

            }
        });

        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display = current.getText().toString();
                String[] splited = display.split("\\s+");
                String lastOne = splited[splited.length - 1];
                boolean dot_inserted1 = lastOne.endsWith("-") || lastOne.endsWith("+")
                        || lastOne.endsWith("×") || lastOne.endsWith("÷");


                if (!TextUtils.isEmpty(display) || dot_inserted1 == false) {
                    switch (splited[1].charAt(0)) {
                        case '+':
                            res = Double.toString(Double.parseDouble(splited[0]) + Double.parseDouble(splited[2]));
                            showTwo();
                            break;
                        case '-':
                            res = Double.toString(Double.parseDouble(splited[0]) - Double.parseDouble(splited[2]));
                            showTwo();
                            break;
                        case '×':
                            res = Double.toString(Double.parseDouble(splited[0]) * Double.parseDouble(splited[2]));
                            showTwo();
                            break;
                        case '÷':
                            res = Double.toString(Double.parseDouble(splited[0]) / Double.parseDouble(splited[2]));
                            showTwo();
                            break;
                    }
                  }

                }


        });

        



}




    public void showOne() {
        current.append(curr);
    }

    public void showTwo() {
        result.setText(res);
    }

}