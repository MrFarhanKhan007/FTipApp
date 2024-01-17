package com.example.ftipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ftipapp.ui.components.TextInput
import com.example.ftipapp.ui.theme.FTipAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FTipAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MyApp()
                }
            }
        }
    }
}


@Composable
fun MyApp(modifier: Modifier = Modifier) {

    val numberOfPerson = remember { mutableIntStateOf(1) }
    val bill = remember { mutableStateOf("") }
    val validState = remember(bill.value) {
        bill.value.trim().isEmpty()
    }
    val tipAmountState = remember {
        mutableDoubleStateOf(0.0)
    }
    val sliderPositionState = remember {
        mutableFloatStateOf(0f)
    }

    val tipPercentage = (sliderPositionState.floatValue * 100).toInt()

    val checkEnabled = remember {
        mutableStateOf(false)
    }
    val totalPerPerson = remember {
        mutableDoubleStateOf(0.00)
    }
    Surface(modifier.fillMaxSize(), color = Color(0xffFFFFFF)) {
        Column(modifier.padding(25.dp)) {
            TopCard(totalPerPerson = totalPerPerson.doubleValue)
            Spacer(modifier.height(20.dp))
            SplitTipCard(
                modifier = Modifier,
                numberOfPerson = numberOfPerson,
                bill = bill,
                validState = validState,
                tipAmountState = tipAmountState,
                tipPercentage = tipPercentage,
                checkEnabled = checkEnabled,
                sliderPositionState = sliderPositionState,
                totalPerPerson = totalPerPerson
            )
        }
    }
}

@Composable
fun TopCard(modifier: Modifier = Modifier, totalPerPerson: Double) {

    Card(
        modifier
            .fillMaxWidth()
            .height(150.dp),

        elevation = CardDefaults.cardElevation(7.dp),
        colors = CardDefaults.cardColors(Color(0xFFBB1111))
    ) {
        Column(
            modifier
                .padding(12.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Total Per Person",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = "$%.2f".format(totalPerPerson),
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SplitTipCard(
    modifier: Modifier = Modifier,
    numberOfPerson: MutableIntState,
    bill: MutableState<String>,
    tipPercentage: Int,
    checkEnabled: MutableState<Boolean>,
    tipAmountState: MutableState<Double>,
    validState: Boolean,
    sliderPositionState: MutableFloatState,
    totalPerPerson: MutableState<Double>,
    onValChange: (String) -> Unit = {}
) {


    val keyboardController = LocalSoftwareKeyboardController.current
//    val enabledState = remember { mutableStateOf(false) }
    Card(
        modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        colors = CardDefaults.cardColors(Color(0xDADD0E0E)),
        border = BorderStroke(1.dp, Color.DarkGray)
    ) {

        Column(
            modifier
                .padding(6.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            TextInput(
                valueState = bill,
                enabled = !checkEnabled.value,
                onAction = KeyboardActions {
                    if (validState) return@KeyboardActions
                    onValChange(bill.value.trim())
                    keyboardController?.hide()
                },
                bill = bill,
                tipPercentage = tipPercentage,
                tipAmountState = tipAmountState,
                totalPerPerson = totalPerPerson,
                numberOfPerson = numberOfPerson
            )
            if (!validState) {
                FinalBox(
                    modifier,
                    numberOfPerson,
                    sliderPositionState = sliderPositionState,
                    bill = bill,
                    tipPercentage = tipPercentage,
                    tipAmountState = tipAmountState,
                    totalPerPerson = totalPerPerson,
                )
            } else {
                Box {
                    //empty
                }
            }
        }
    }

}

@Composable
private fun FinalBox(
    modifier: Modifier,
    numberOfPerson: MutableIntState,
    sliderPositionState: MutableFloatState,
    bill: MutableState<String>,
    tipPercentage: Int,
    tipAmountState: MutableState<Double>,
    totalPerPerson: MutableState<Double>,
) {
    Column {
        SplitTipCard2(
            modifier, numberOfSplits = numberOfPerson,
            bill = bill,
            tipPercentage = tipPercentage,
            totalPerPerson = totalPerPerson
        )
        Spacer(modifier.height(10.dp))
        TipRow(
            modifier = Modifier,
            sliderPositionState = sliderPositionState,
            bill = bill,
            tipPercentage = tipPercentage,
            tipAmountState = tipAmountState,
            totalPerPerson = totalPerPerson,
            numberOfPerson = numberOfPerson
        )
    }
}

@Composable
private fun SplitTipCard2(
    modifier: Modifier,
    numberOfSplits: MutableState<Int>,
    bill: MutableState<String>,
    tipPercentage: Int,
    totalPerPerson: MutableState<Double>
) {
    Row(
        modifier.padding(3.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Split",
            modifier.align(Alignment.CenterVertically),
            fontSize = 25.sp,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier.width(100.dp))
        Row(
            modifier.padding(horizontal = 3.dp),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                onClick = {
                    Log.d("Remove", "SUBTRACTING")
                    if (numberOfSplits.value > 1)
                        numberOfSplits.value--
                    totalPerPerson.value = calculateTotalTipPersonWise(
                        bill = bill.value.toDouble(),
                        tipPercentage = tipPercentage,
                        numberOfPerson = numberOfSplits
                    )
                }, modifier
                    .size(50.dp)
                    .padding(2.5.dp),
                shape = CircleShape,
                containerColor = Color.White,
                content = {
                    Icon(imageVector = Icons.Filled.Remove, contentDescription = "")
                }
            )
            Text(
                text = "${numberOfSplits.value}",
                modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 7.5.dp, end = 7.5.dp),
                color = Color.White,
                fontFamily = FontFamily.Serif,
                fontSize = 15.sp
            )
            FloatingActionButton(
                onClick = {
                    Log.d("Add", "ADDING")
                    if (numberOfSplits.value < 100)
                        numberOfSplits.value++
                    totalPerPerson.value = calculateTotalTipPersonWise(
                        bill = bill.value.toDouble(),
                        tipPercentage = tipPercentage,
                        numberOfPerson = numberOfSplits
                    )
                }, modifier
                    .size(50.dp)
                    .padding(2.5.dp),
                shape = CircleShape,
                containerColor = Color.White,
                content = {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                }
            )
            Spacer(modifier = modifier.width(15.dp))
        }

    }
}

@Composable
fun TipRow(
    modifier: Modifier = Modifier,
    sliderPositionState: MutableFloatState,
    bill: MutableState<String>,
    tipPercentage: Int,
    tipAmountState: MutableState<Double>,
    totalPerPerson: MutableState<Double>,
    numberOfPerson: MutableIntState
) {
    Column {
        Row(modifier.padding(3.dp)) {
            Text(
                text = "Tip", modifier.align(Alignment.CenterVertically),
                fontSize = 25.sp,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier.width(145.dp))
            Text(
                text = "$%.2f".format(tipAmountState.value),
                fontFamily = FontFamily.Serif,
                color = Color.White,
                fontSize = 25.sp,
            )
        }
        Spacer(modifier.height(10.dp))
        Text(
            text = "$tipPercentage%",
            modifier
                .align(Alignment.CenterHorizontally),
            color = Color.White,
            fontFamily = FontFamily.Serif,
            fontSize = 25.sp,
        )

        Spacer(modifier.height(10.dp))
        Slider(
            value = sliderPositionState.floatValue,
            onValueChange = { newVal ->
                sliderPositionState.floatValue = newVal

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
            modifier.padding(bottom = 10.dp),
            enabled = true,
            steps = 0,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(alpha = 0.5f),
            )
        )

    }
}

fun calculateTotalTip(
    bill: Double,
    tipPercentage: Int,
): Double {
    return if (
        bill > 1 &&
        bill.toString().isNotEmpty()
    ) {
        ((bill * tipPercentage) / 100)
    } else {
        return 0.0
    }
}

fun calculateTotalTipPersonWise(
    bill: Double,
    tipPercentage: Int,
    numberOfPerson: MutableState<Int>
): Double {
    return if (
        bill > 1 &&
        bill.toString().isNotEmpty()
    ) {
        ((bill * tipPercentage) / (numberOfPerson.value * 100))
    } else {
        return 0.0
    }
}
