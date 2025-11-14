package com.sidhant.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit

@Composable
fun AutoSizeText(
    text: String,
    maxFontSize: TextUnit,
    minFontSize: TextUnit,
    fontWeight: FontWeight? = null,
    color: androidx.compose.ui.graphics.Color,
    textAlign: TextAlign,
    modifier: Modifier = Modifier
) {
    var fontSize = maxFontSize
    val step = 4.sp
    
    // Calculate appropriate font size based on text length
    fontSize = when {
        text.length <= 8 -> maxFontSize
        text.length <= 10 -> 64.sp
        text.length <= 12 -> 52.sp
        text.length <= 14 -> 44.sp
        else -> minFontSize
    }
    
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = color,
        textAlign = textAlign,
        maxLines = 1,
        overflow = TextOverflow.Visible,
        softWrap = false,
        modifier = modifier
    )
}

@Composable
fun CalculatorScreen(
    state: CalculatorState,
    onAction: (CalculatorAction) -> Unit
) {
    val buttonSpacing = 12.dp
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                top = statusBarPadding.calculateTopPadding(),
                bottom = navBarPadding.calculateBottomPadding()
            )
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing),
        ) {
            // Display Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = state.calculationDisplay,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Visible,
                    softWrap = false
                )
                AutoSizeText(
                    text = state.resultDisplay,
                    maxFontSize = 80.sp,
                    minFontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Button Grid
            // Row 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                val functionColor = MaterialTheme.colorScheme.secondaryContainer
                CalculatorButton(
                    symbol = "C",
                    backgroundColor = functionColor,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Clear) }
                )
                CalculatorButton(
                    symbol = "+/-",
                    backgroundColor = functionColor,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.ToggleSign) }
                )
                CalculatorButton(
                    symbol = "%",
                    backgroundColor = functionColor,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Percentage) }
                )
                CalculatorButton(
                    symbol = "*",
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Multiply)) }
                )
            }

            // Row 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "7",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Number(7)) }
                )
                CalculatorButton(
                    symbol = "8",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Number(8)) }
                )
                CalculatorButton(
                    symbol = "9",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Number(9)) }
                )
                CalculatorButton(
                    symbol = "÷",
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Divide)) }
                )
            }

            // Row 3
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "4",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Number(4)) }
                )
                CalculatorButton(
                    symbol = "5",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Number(5)) }
                )
                CalculatorButton(
                    symbol = "6",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Number(6)) }
                )
                CalculatorButton(
                    symbol = "-",
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Subtract)) }
                )
            }

            // Row 4
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "1",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Number(1)) }
                )
                CalculatorButton(
                    symbol = "2",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Number(2)) }
                )
                CalculatorButton(
                    symbol = "3",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Number(3)) }
                )
                CalculatorButton(
                    symbol = "+",
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.Add)) }
                )
            }

            // Row 5
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "0",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Number(0)) }
                )
                CalculatorButton(
                    symbol = ".",
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Decimal) }
                )
                CalculatorButton(
                    symbol = "Del",
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Delete) },
                    isIcon = true
                )
                
                CalculatorButton(
                    symbol = "=",
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = { onAction(CalculatorAction.Calculate) }
                )
            }
        }
    }
}