package com.example.abdul.calculator;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.objecthunter.exp4j.Expression;

        import android.os.Bundle;
        import android.widget.Button;
        import android.widget.TextView;

        import net.objecthunter.exp4j.ExpressionBuilder;

        import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private int[] numericButtons = {R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine};
    private int[] operatorButtons = {R.id.plus, R.id.minus, R.id.times, R.id.obelus};
    private TextView txtScreen;
    private TextView resultScreen;

    private boolean lastNumeric;
    private boolean stateError;
    private boolean lastDot;
    private boolean out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.txtScreen =  findViewById(R.id.entry);
        this.resultScreen=findViewById(R.id.result);
        setNumericOnClickListener();
        setOperatorOnClickListener();
    }

    private void setNumericOnClickListener() {
        for (int id : numericButtons) {
            findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    if(out){
                        txtScreen.setText("");
                        resultScreen.setText("");
                        out=false;
                    }
                    if (stateError) {
                        txtScreen.setText(button.getText());
                        stateError = false;
                    } else {
                        txtScreen.append(button.getText());
                    }
                    lastNumeric = true;
                    resultScreen.setText("");
                }
            });
        }
    }

    private void setOperatorOnClickListener() {
        for (int id : operatorButtons) {
            findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    String op=button.getText().toString();
                    if(!stateError){
                        if(out){
                            txtScreen.setText(resultScreen.getText());
                            resultScreen.setText("");
                            out=false;
                        }
                        if(op.equals("-")){
                            if(txtScreen.getText().toString().isEmpty()){
                                txtScreen.setText("-");
                            }
                            else if(txtScreen.getText().charAt(txtScreen.getText().length()-1)=='-'){
                                txtScreen.setText(txtScreen.getText().subSequence(0,txtScreen.getText().length()-1)+"+");
                            }
                            else if(txtScreen.getText().charAt(txtScreen.getText().length()-1)=='+'){
                                txtScreen.setText(txtScreen.getText().subSequence(0,txtScreen.getText().length()-1)+"-");
                            }
                            else txtScreen.append(op);
                        }
                        else if (lastNumeric) {
                            txtScreen.append(op);
                        }
                        lastNumeric = false;
                        lastDot = false;
                    }
                }
            });
        }
        findViewById(R.id.dot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNumeric && !stateError && !lastDot) {
                    txtScreen.append(".");
                    lastNumeric = false;
                    lastDot = true;
                }
            }
        });
        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultScreen.setText("");
                txtScreen.setText("");
                lastNumeric = false;
                stateError = false;
                lastDot = false;
            }
        });
        findViewById(R.id.del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultScreen.setText("");
                String txt=txtScreen.getText().toString();
                char last='\0';
                if (txt.length()>1){
                    txt=(String)txt.subSequence(0,txt.length()-1);
                    last=txt.charAt(txt.length()-1);
                }
                else txt="";
                txtScreen.setText(txt);
                lastNumeric = false;
                stateError = false;
                lastDot = false;
                if(Character.isDigit(last)){
                    lastNumeric=true;
                }
                else if (last=='.'){
                    lastDot=true;
                }
            }
        });
        findViewById(R.id.equal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEqual();
            }
        });
    }

    private void onEqual() {
        if (lastNumeric && !stateError) {
            String txt = txtScreen.getText().toString();
            txt=txt.replace('÷','/');
            txt=txt.replace('×','*');
            Expression expression = new ExpressionBuilder(txt).build();
            try {
                double result = expression.evaluate();
                resultScreen.setText(new DecimalFormat("#.######").format(result));
            } catch (ArithmeticException ex) {
                resultScreen.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
            finally {
                out=true;
            }
        }
    }
}