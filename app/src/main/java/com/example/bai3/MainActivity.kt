package com.example.bai3

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private lateinit var editTextFrom: EditText
    private lateinit var editTextTo: EditText

    private var isUpdating = false

    // Tỷ giá cố định
    private val rates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.93,
        "VND" to 24500.0,
        "JPY" to 152.0,
        "GBP" to 0.80,
        "AUD" to 1.55,
        "CAD" to 1.39,
        "SGD" to 1.35,
        "CHF" to 0.89,
        "CNY" to 7.13
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
        editTextFrom = findViewById(R.id.editTextFrom)
        editTextTo = findViewById(R.id.editTextTo)

        val currencies = rates.keys.toList()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, currencies)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        // Mặc định: USD → VND
        spinnerFrom.setSelection(currencies.indexOf("USD"))
        spinnerTo.setSelection(currencies.indexOf("VND"))

        // Lắng nghe thay đổi nội dung
        editTextFrom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isUpdating) convertCurrency(fromTop = true)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        editTextTo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isUpdating) convertCurrency(fromTop = false)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        spinnerFrom.onItemSelectedListener = SimpleSpinnerListener { convertCurrency(true) }
        spinnerTo.onItemSelectedListener = SimpleSpinnerListener { convertCurrency(true) }
    }

    private fun convertCurrency(fromTop: Boolean) {
        isUpdating = true
        try {
            val from = spinnerFrom.selectedItem.toString()
            val to = spinnerTo.selectedItem.toString()

            val rateFrom = rates[from] ?: 1.0
            val rateTo = rates[to] ?: 1.0

            if (fromTop) {
                val input = editTextFrom.text.toString()
                if (input.isNotEmpty()) {
                    val amount = input.toDouble()
                    val result = amount * (rateTo / rateFrom)
                    editTextTo.setText(String.format("%.2f", result))
                } else editTextTo.text.clear()
            } else {
                val input = editTextTo.text.toString()
                if (input.isNotEmpty()) {
                    val amount = input.toDouble()
                    val result = amount * (rateFrom / rateTo)
                    editTextFrom.setText(String.format("%.2f", result))
                } else editTextFrom.text.clear()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isUpdating = false
    }
}

