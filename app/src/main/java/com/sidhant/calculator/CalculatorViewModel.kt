package com.sidhant.calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.DecimalFormat

class CalculatorViewModel : ViewModel() {

    private val _state = MutableStateFlow(CalculatorState())
    val state: StateFlow<CalculatorState> = _state.asStateFlow()

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Clear -> _state.value = CalculatorState()
            is CalculatorAction.Delete -> performDelete()
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Calculate -> performCalculation()
            is CalculatorAction.ToggleSign -> performToggleSign()
            is CalculatorAction.Percentage -> performPercentage()
        }
    }

    private fun enterNumber(number: Int) {
        _state.update { currentState ->
            if (currentState.operation == null) {
                // Entering number1
                if (currentState.number1.length >= MAX_NUM_LENGTH) return@update currentState
                currentState.copy(number1 = currentState.number1 + number)
            } else {
                // Entering number2
                if (currentState.number2.length >= MAX_NUM_LENGTH) return@update currentState
                currentState.copy(number2 = currentState.number2 + number)
            }
        }
    }

    private fun enterDecimal() {
        _state.update { currentState ->
            if (currentState.operation == null) {
                // Decimal for number1
                if (currentState.number1.contains(".") || currentState.number1.isBlank()) return@update currentState
                currentState.copy(number1 = currentState.number1 + ".")
            } else {
                // Decimal for number2
                if (currentState.number2.contains(".") || currentState.number2.isBlank()) return@update currentState
                currentState.copy(number2 = currentState.number2 + ".")
            }
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        _state.update { currentState ->
            if (currentState.number1.isNotBlank() && currentState.number2.isNotBlank()) {
                // If we have a full equation, calculate it first
                val result = performCalculationLogic(currentState)
                currentState.copy(
                    number1 = result,
                    number2 = "",
                    operation = operation
                )
            } else if (currentState.number1.isNotBlank()) {
                // Start a new operation
                currentState.copy(operation = operation)
            } else {
                currentState
            }
        }
    }

    private fun performCalculation() {
        _state.update { currentState ->
            val result = performCalculationLogic(currentState)
            currentState.copy(
                number1 = result,
                number2 = "",
                operation = null
            )
        }
    }

    private fun performToggleSign() {
        _state.update { currentState ->
            if (currentState.number2.isNotBlank()) {
                currentState.copy(number2 = toggleSign(currentState.number2))
            } else if (currentState.number1.isNotBlank()) {
                currentState.copy(number1 = toggleSign(currentState.number1))
            } else {
                currentState
            }
        }
    }

    private fun performPercentage() {
        _state.update { currentState ->
            if (currentState.number2.isNotBlank()) {
                val num2 = currentState.number2.toDoubleOrNull() ?: return@update currentState
                currentState.copy(number2 = formatResult(num2 / 100.0))
            } else if (currentState.number1.isNotBlank()) {
                val num1 = currentState.number1.toDoubleOrNull() ?: return@update currentState
                currentState.copy(number1 = formatResult(num1 / 100.0))
            } else {
                currentState
            }
        }
    }

    private fun performDelete() {
        _state.update { currentState ->
            if (currentState.number2.isNotBlank()) {
                currentState.copy(number2 = currentState.number2.dropLast(1))
            } else if (currentState.operation != null) {
                currentState.copy(operation = null)
            } else if (currentState.number1.isNotBlank()) {
                currentState.copy(number1 = currentState.number1.dropLast(1))
            } else {
                currentState
            }
        }
    }

    // --- Helper Functions ---

    private fun performCalculationLogic(currentState: CalculatorState): String {
        val num1 = currentState.number1.toDoubleOrNull()
        val num2 = currentState.number2.toDoubleOrNull()

        if (num1 != null && num2 != null && currentState.operation != null) {
            val result = when (currentState.operation) {
                CalculatorOperation.Add -> num1 + num2
                CalculatorOperation.Subtract -> num1 - num2
                CalculatorOperation.Multiply -> num1 * num2
                CalculatorOperation.Divide -> if (num2 == 0.0) return "Error" else num1 / num2
            }
            return formatResult(result)
        }
        // If calculation isn't possible, return the primary number
        return currentState.number1
    }

    private fun toggleSign(number: String): String {
        return if (number.startsWith("-")) {
            number.substring(1)
        } else if (number.isNotBlank() && number != "0") {
            "-$number"
        } else {
            number
        }
    }

    private fun formatResult(result: Double): String {
        // Check if result is a whole number
        if (result % 1.0 == 0.0) {
            val longResult = result.toLong().toString()
            // Limit to max display length
            return if (longResult.length > MAX_DISPLAY_LENGTH) {
                formatScientific(result)
            } else {
                longResult
            }
        }
        
        // Format decimal numbers
        val formatter = DecimalFormat("0.########")
        val formatted = formatter.format(result)
        
        // If too long, use scientific notation or truncate
        return if (formatted.length > MAX_DISPLAY_LENGTH) {
            formatScientific(result)
        } else {
            formatted
        }
    }
    
    private fun formatScientific(value: Double): String {
        return String.format("%.4e", value)
    }

    companion object {
        private const val MAX_NUM_LENGTH = 10
        private const val MAX_DISPLAY_LENGTH = 15
    }
}