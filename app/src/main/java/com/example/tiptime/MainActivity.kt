package com.example.tiptime
import androidx.compose.ui.tooling.preview.Preview
import androidx.annotation.StringRes
import androidx.annotation.DrawableRes
import androidx.annotation.VisibleForTesting
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Composable
fun TipTimeLayout() {
    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember { mutableStateOf("") }
    var roundUp by remember { mutableStateOf(false) }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount, tipPercent, roundUp)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TitleText()
        AmountInputField(amountInput) { amountInput = it }
        TipInputField(tipInput) { tipInput = it }
        RoundTipSwitch(roundUp) { roundUp = it }
        TipResultText(tip)
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun TitleText() {
    Text(
        text = stringResource(R.string.calculate_tip),
        modifier = Modifier
            .padding(bottom = 16.dp, top = 40.dp)
    )
}

@Composable
fun AmountInputField(value: String, onValueChanged: (String) -> Unit) {
    EditNumberField(
        label = R.string.bill_amount,
        leadingIcon = R.drawable.money,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        value = value,
        onValueChanged = onValueChanged,
        modifier = Modifier
            .padding(bottom = 32.dp)
            .fillMaxWidth()
    )
}

@Composable
fun TipInputField(value: String, onValueChanged: (String) -> Unit) {
    EditNumberField(
        label = R.string.how_was_the_service,
        leadingIcon = R.drawable.percent,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        value = value,
        onValueChanged = onValueChanged,
        modifier = Modifier
            .padding(bottom = 32.dp)
            .fillMaxWidth()
    )
}

@Composable
fun RoundTipSwitch(roundUp: Boolean, onRoundUpChanged: (Boolean) -> Unit) {
    RoundTheTipRow(
        roundUp = roundUp,
        onRoundUpChanged = onRoundUpChanged,
        modifier = Modifier.padding(bottom = 32.dp)
    )
}

@Composable
fun TipResultText(tip: String) {
    Text(
        text = stringResource(R.string.tip_amount, tip),
        style = MaterialTheme.typography.displaySmall
    )
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        singleLine = true,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) },
        modifier = modifier,
        onValueChange = onValueChanged,
        label = { Text(stringResource(label)) },
        keyboardOptions = keyboardOptions
    )
}

@Composable
fun RoundTheTipRow(
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.round_up_tip))
        Switch(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            checked = roundUp,
            onCheckedChange = onRoundUpChanged
        )
    }
}

@VisibleForTesting
internal fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean): String {
    var tip = tipPercent / 100 * amount
    if (roundUp) {
        tip = ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    TipTimeTheme {
        TipTimeLayout()
    }
}
