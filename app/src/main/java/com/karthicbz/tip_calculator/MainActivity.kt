package com.karthicbz.tip_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karthicbz.tip_calculator.components.CustomElevatedButton
import com.karthicbz.tip_calculator.ui.theme.Tip_calculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tip_calculatorTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars)
                ) { innerPadding ->
                    Content(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Content(modifier: Modifier) {
    val tipTotal = remember { mutableIntStateOf(0) }
    val sliderPosition = remember { mutableFloatStateOf(50f) }
    val text = remember { mutableStateOf<String>("") }
    val numOfPeopleCount = remember { mutableIntStateOf(2) }

    Column(modifier = Modifier.padding(16.dp)) {
        CardHeader(tipTotal)
        Spacer(modifier = Modifier.height(16.dp))
        CardBody(text, sliderPosition, numOfPeopleCount, tipTotal)
    }
}

fun calculateTip(
    tipTotal: MutableIntState,
    sliderPosition: MutableFloatState,
    text: MutableState<String>,
    numOfPeopleCount: MutableIntState
) {
    if (text.value != "") {
        tipTotal.intValue =
            ((text.value.toFloat() + sliderPosition.floatValue) / numOfPeopleCount.intValue).toInt()
    } else {
        tipTotal.intValue = 0
    }
}

@Preview
@Composable
fun CardHeader(tipTotal: MutableIntState = mutableIntStateOf(0)) {
    // TODO: add text "total tip per person" above tiptotal
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        elevation = CardDefaults.cardElevation(draggedElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Total Tip Per Person", fontSize = 30.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "$${tipTotal.intValue}", fontSize = 50.sp)
        }
    }
}

@Preview
@Composable
fun CardBody(
    //text is the count value we are showing between buttons
    text: MutableState<String>,
    sliderPosition: MutableFloatState,
    numOfPeopleCount: MutableIntState,
    tipTotal: MutableIntState
) {
    Column {
        val focusRequester = remember { FocusRequester() }
        var focusManager = LocalFocusManager.current
        OutlinedTextField(
            //Todo:remove focus on keyboard hide
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
            value = text.value,
            onValueChange = {
                text.value = it; calculateTip(
                tipTotal,
                sliderPosition,
                text,
                numOfPeopleCount
            )
            },
            label = { Text(text = "Enter Bill Amount") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone={
                    focusManager.clearFocus()
                }
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (text.value != "") {
            Surface(
                modifier = Modifier
                    .wrapContentHeight()
                    .border(BorderStroke(1.5.dp, Color.Gray), RoundedCornerShape(8.dp))
            ) {
                Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)) {
                    Text("Number of People", fontSize = 16.sp)
                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .align(alignment = Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomElevatedButton(onClick = {
                            numOfPeopleCount.intValue += 1
                            calculateTip(tipTotal, sliderPosition, text, numOfPeopleCount)
                        }, icon = Icons.Default.Add, contentDescription = "Add")
                        Text("${numOfPeopleCount.intValue}")
                        CustomElevatedButton(onClick = {
                            if (numOfPeopleCount.intValue > 1) {
                                numOfPeopleCount.intValue -= 1
                                calculateTip(tipTotal, sliderPosition, text, numOfPeopleCount)
                            }
                        }, icon = Icons.Default.Remove, contentDescription = "Remove")

                    }
                    Text("Tip %", fontSize = 16.sp)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "${sliderPosition.floatValue.toInt()} %",
                            fontSize = 16.sp,
                        )
                        Slider(
                            modifier = Modifier.padding(top = 16.dp),
                            value = sliderPosition.floatValue,
                            onValueChange = {
                                sliderPosition.floatValue = it; calculateTip(
                                tipTotal,
                                sliderPosition,
                                text,
                                numOfPeopleCount
                            )
                            },
                            valueRange = 0f..100f,
                        )
                    }
                }
            }
        }
    }

}