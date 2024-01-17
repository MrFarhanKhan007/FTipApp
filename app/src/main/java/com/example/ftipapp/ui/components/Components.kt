package com.example.ftipapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ftipapp.calculateTotalTip
import com.example.ftipapp.calculateTotalTipPersonWise

@Composable
fun TextInput(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    enabled: Boolean,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Number,
    onAction: KeyboardActions = KeyboardActions.Default,
    tipPercentage: Int,
    tipAmountState: MutableState<Double>,
    bill: MutableState<String>,
    numberOfPerson: MutableState<Int>,
    totalPerPerson: MutableState<Double>
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = {
            valueState.value = it
            tipAmountState.value = calculateTotalTip(
                bill = bill.value.toDouble(),
                tipPercentage = tipPercentage,
            )

            totalPerPerson.value = calculateTotalTipPersonWise(
                bill = bill.value.toDouble(),
                tipPercentage = tipPercentage,
                numberOfPerson = numberOfPerson
            )
        },
        modifier.padding(start = 10.dp, bottom = 10.dp, end = 10.dp),
        singleLine = true,
        label = {
            Text(
                "Enter Bill",
                color = Color.White,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.AttachMoney,
                contentDescription = "Attach Money Icon"
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White
        ),
        textStyle = TextStyle(
            fontSize = 18.sp,
            color = Color.White,
            fontFamily = FontFamily.Serif,
        ),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
        keyboardActions = onAction
    )
}

