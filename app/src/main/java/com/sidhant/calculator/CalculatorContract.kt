package com.sidhant.calculator

// Represents the state of the calculator display
data class CalculatorState(
    val number1: String = "",
    val number2: String = "",
    val operation: CalculatorOperation? = null
) {
    // Computed property to generate the text for the top (smaller) display
    val calculationDisplay: String
        get() = if (number2.isNotBlank() && operation != null) {
            "$number1 ${operation.symbol}"
        } else if (operation != null) {
            "$number1 ${operation.symbol}"
        } else {
            ""
        }

    // Computed property to generate the text for the bottom (main) display
    val resultDisplay: String
        get() = if (number2.isNotBlank()) {
            number2
        } else if (number1.isNotBlank()) {
            number1
        } else {
            "0"
        }
}

// Defines all possible user actions
sealed class CalculatorAction {
    data class Number(val number: Int) : CalculatorAction()
    object Clear : CalculatorAction()
    object Delete : CalculatorAction()
    object Calculate : CalculatorAction()
    object Decimal : CalculatorAction()
    object ToggleSign : CalculatorAction()
    object Percentage : CalculatorAction()
    data class Operation(val operation: CalculatorOperation) : CalculatorAction()
}

// Defines the mathematical operations
sealed class CalculatorOperation(val symbol: String) {
    object Add : CalculatorOperation("+")
    object Subtract : CalculatorOperation("-")
    object Multiply : CalculatorOperation("×")
    object Divide : CalculatorOperation("÷")
}