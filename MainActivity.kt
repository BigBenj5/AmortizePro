package com.example.loan_calculator

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlin.math.pow
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etLoanAmount = findViewById<EditText>(R.id.etLoanAmount)
        val etInterestRate = findViewById<EditText>(R.id.etInterestRate)
        val etLoanTerm = findViewById<EditText>(R.id.etLoanTerm)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        val btnDay = findViewById<Button>(R.id.btnDay)
        val btnNight = findViewById<Button>(R.id.btnNight)

        //Loan Calculation
        btnCalculate.setOnClickListener {
            val loanStr = etLoanAmount.text.toString()
            val rateStr = etInterestRate.text.toString()
            val termStr = etLoanTerm.text.toString()

            if (loanStr.isEmpty() || rateStr.isEmpty() || termStr.isEmpty()) {
                Toast.makeText(this, "Please enter all values", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loanAmount = loanStr.toDouble()
            val annualRate = rateStr.toDouble()
            val years = termStr.toInt()

            val monthlyRate = annualRate / 12 / 100
            val numberOfPayments = years * 12

            val monthlyPayment = if (monthlyRate == 0.0) {
                loanAmount / numberOfPayments
            } else {
                (loanAmount * monthlyRate * (1 + monthlyRate).pow(numberOfPayments)) /
                        ((1 + monthlyRate).pow(numberOfPayments) - 1)
            }

            val totalPayment = monthlyPayment * numberOfPayments
            val totalInterest = totalPayment - loanAmount

            // Evaluate the loan deal
            val interestRatio = totalInterest / loanAmount
            val loanVerdict = when {
                interestRatio <= 0.2 -> "✅ This looks like a good deal."
                interestRatio <= 0.5 -> "⚖️ This loan is moderate/okay. Consider comparing with other offers."
                else -> "⚠️ Warning: This loan is too costly. Interest is very high."
            }

            tvResult.text = """
                Monthly Payment: $%.2f
                Total Payment: $%.2f
                Total Interest: $%.2f
                
                This means you will spend an extra $%.2f on interest alone

                Verdict: %s
            """.trimIndent().format(monthlyPayment, totalPayment, totalInterest, totalInterest, loanVerdict)
        }

        // Theme switching
        btnDay.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        btnNight.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}