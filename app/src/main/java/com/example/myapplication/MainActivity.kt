package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import android.view.inputmethod.InputMethodManager;
import android.view.MotionEvent


class MainActivity : AppCompatActivity() {
    private lateinit var firstNumberEditText: EditText
    private lateinit var secondNumberEditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var addBtn: Button
    private lateinit var subtractBtn: Button
    private lateinit var multiplyBtn: Button
    private lateinit var divideBtn: Button
    private lateinit var operationTypeTextView: TextView
    private lateinit var operationOrderRadioButton: RadioButton
    private lateinit var numberOrderRadioButton: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstNumberEditText = findViewById(R.id.firstNumberEditText)
        secondNumberEditText = findViewById(R.id.secondNumberEditText)
        resultTextView = findViewById(R.id.resultEditText);
        addBtn = findViewById(R.id.addBtn)
        subtractBtn = findViewById(R.id.subtractBtn)
        multiplyBtn = findViewById(R.id.multiplyBtn)
        divideBtn = findViewById(R.id.divideBtn)
        operationTypeTextView = findViewById(R.id.operationTypeTextView)
        operationOrderRadioButton = findViewById(R.id.radioButton1)
        numberOrderRadioButton = findViewById(R.id.radioButton2)

        firstNumberEditText.setOnClickListener {
            firstNumberEditText.selectAll()
        }

        secondNumberEditText.setOnClickListener {
            secondNumberEditText.selectAll()
        }

        addBtn.setOnClickListener { performOperation(Operation.ADD) }
        subtractBtn.setOnClickListener { performOperation(Operation.SUBTRACT) }
        multiplyBtn.setOnClickListener { performOperation(Operation.MULTIPLY) }
        divideBtn.setOnClickListener { performOperation(Operation.DIVIDE) }

        findViewById<Button>(R.id.clearBtn).setOnClickListener {
            firstNumberEditText.text.clear()
            secondNumberEditText.text.clear()
            resultTextView.text = ""
            firstNumberEditText.requestFocus()
        }
    }

    private fun getSelectedOperation(): List<Operation> {
        val selectedOperations = mutableListOf<Operation>()

        if (operationOrderRadioButton.isChecked) {
            selectedOperations.add(Operation.SUBTRACT)
        } else if (numberOrderRadioButton.isChecked) {
            selectedOperations.add(Operation.ADD)
        }

        selectedOperations.addAll(listOf(Operation.MULTIPLY, Operation.DIVIDE))

        return selectedOperations
    }

    private fun performOperation(operation: Operation) {
        val firstNumberString = firstNumberEditText.text.toString()
        val secondNumberString = secondNumberEditText.text.toString()

        if (TextUtils.isEmpty(firstNumberString) || TextUtils.isEmpty(secondNumberString)) {
            Toast.makeText(this, "Lütfen tüm sayıları girin.", Toast.LENGTH_SHORT).show()

            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            return
        }

        val firstNumber: Double
        val secondNumber: Double
        try {
            firstNumber = firstNumberString.toDouble()
            secondNumber = secondNumberString.toDouble()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Geçersiz Sayı Girişi.", Toast.LENGTH_SHORT).show()

            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            return
        }

        val result = when (operation) {
            Operation.ADD -> {
                if (operationOrderRadioButton.isChecked) {
                    firstNumber + secondNumber
                } else {
                    secondNumber + firstNumber
                }
            }

            Operation.SUBTRACT -> {
                if (operationOrderRadioButton.isChecked) {
                    firstNumber - secondNumber
                } else {
                    secondNumber - firstNumber
                }
            }

            Operation.MULTIPLY -> {
                if (operationOrderRadioButton.isChecked) {
                    firstNumber * secondNumber
                } else {
                    secondNumber * firstNumber
                }
            }

            Operation.DIVIDE -> {
                if (secondNumber == 0.0) {
                    if (firstNumber == 0.0) {
                        Toast.makeText(this, "Tanımsız işlem: 0/0", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "0'a bölme tanımsızdır", Toast.LENGTH_SHORT).show()
                    }

                    val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    return
                }

                if (operationOrderRadioButton.isChecked) {
                    firstNumber / secondNumber
                } else {
                    secondNumber / firstNumber
                }
            }
        }
    resultTextView.setText(result.toString())
    }

    enum class Operation {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }
}
