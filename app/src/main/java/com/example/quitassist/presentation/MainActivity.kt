/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.quitassist.presentation

import android.os.Bundle
import android.view.Surface
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.quitassist.R
import com.example.quitassist.presentation.theme.QuitAssistTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.quitassist.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.TimeZone

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)
//        val db = Room.databaseBuilder(
//            this,
//            AppDatabase::class.java, "AppDatabase"
//        ).createFromAsset("quitassistdb.db").build()
//        val lifetimeDB = db.lifetimeDao()
//        val currentAmountLoaded = lifetimeDB.getCDU()
        setContent {
            WearApp("Android")
        }
    }
    
}

@Composable
fun WearApp(greetingName: String) {
    QuitAssistTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            Timer("test")
        }
    }
}


@Composable
fun Timer(name: String, modifier: Modifier = Modifier){
    // Create a CoroutineScope that follows this composable's lifecycle
    val composableScope = rememberCoroutineScope()

    var context = LocalContext.current
    val expanded = remember { mutableStateOf(false) }
    val count = remember { mutableIntStateOf(0) }

    val pagerState = rememberPagerState(pageCount = {
        2
    })
    var currentAmountVal: String = "15"

    var currentAmount = remember { mutableStateOf(currentAmountVal)}

    val goalAmount = remember { mutableStateOf("10")}
    val costBasis = remember { mutableStateOf("0.53")}
    val totalSaved = remember { mutableStateOf("0.00")}

    LaunchedEffect(currentAmount.value) {


            val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "AppDatabase"
            ).createFromAsset("quitassistdb.db").build()
            val lifetimeDB = db.lifetimeDao()
            val currentAmountLoaded = lifetimeDB.getCDU()
            if (currentAmountLoaded != 0) {
                print(currentAmountLoaded)

                currentAmountVal = currentAmountLoaded.toString()
            }
            if (currentAmountVal::class.simpleName == "Int"){
                print("currentAmountVal is Int")
                lifetimeDB.setCDU(currentAmount.value.toInt())
            }
            if (currentAmountVal != ""){
                print("currentAmountVal is empty")
                lifetimeDB.setCDU(currentAmount.value.toInt())

        }
    }
    var bool = rememberSaveable { true }

    LaunchedEffect(key1 = bool) {
        // do something
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "AppDatabase"
        ).build()
        val DailyTable = db.DailyDao()

        val theDate = LocalDate.now(ZoneId.systemDefault()).toString()
        DailyTable.createDailyEntry(theDate)
        count.value = DailyTable.getAU(theDate)
        println("dailyTable.getCDU: " + DailyTable.getAU(theDate))
println("count.value at launch" + count.value)
        bool = false
    }
    LaunchedEffect(count.value){
        val theDate = LocalDate.now(ZoneId.systemDefault()).toString()

//        composableScope.launch {
        println("count.value updated")
            val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "AppDatabase"
            ).build()
            val DailyTable = db.DailyDao()
        if (count.value > 0){
            // TODO: set savings - (daily goal - daily usage) * unit price. send to setAU -- completed
            var savings: Double = (currentAmount.value.toInt() - count.intValue) * costBasis.value.toDouble()
            DailyTable.setAU(savings, count.value, theDate)
            totalSaved.value = savings.toString()
        }
    }





    HorizontalPager(state = pagerState){
    page ->
        when (page){
             0 -> {
                Column(modifier = modifier.padding(24.dp).fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text ="Saved: $" + totalSaved.value,
                        textAlign = TextAlign.Center,
                        style = typography.labelLarge
                    )
                    Text(
                        text ="Daily Goal: " + goalAmount.value,
                        textAlign = TextAlign.Center,
                        style = typography.labelSmall
                    )
                    Row(){
                    ElevatedButton(
                        onClick = {
                            expanded.value = !expanded.value
                            count.value = count.value.inc()
                            val toast = Toast.makeText(context,"starting timer, good luck!", Toast.LENGTH_LONG)
                            toast.show()
                        },

                    ) {
                        Text(
                            text ="+",
                            style = TextStyle(color = Color.Black)
                        )

                    }
                    ElevatedButton(
                        onClick = { if (count.value > 0) count.value = count.value.dec() else count.value = 0 }
                    ){
                        Text(
                            text = "-",
                            style = TextStyle(color = Color.Black)

                        )
                    }
                    }
                    Text(count.value.toString() + " used")

                }
            }
            1 -> {
                Column(modifier = modifier.padding(24.dp).verticalScroll(rememberScrollState())){


                    OutlinedTextField(
                        value = currentAmount.value,
                        onValueChange = { currentAmount.value = it

                        },

                        label = { Text("Current Daily Usage")},
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                        textStyle = TextStyle(color = Color.White),
                        modifier = Modifier.padding(top = 50.dp).padding(bottom = 30.dp)
                    )
                    OutlinedTextField(

                        value = goalAmount.value,
                        onValueChange = { goalAmount.value = it},
                        label = { Text("Goal Daily Usage")},
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                        textStyle = TextStyle(color = Color.White),
                        modifier = Modifier.padding(0.dp).padding(bottom = 30.dp)
                    )
                    OutlinedTextField(
                        value = costBasis.value,
                        onValueChange = {costBasis.value = it},
                        label = { Text("cost per pouch") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                        textStyle = TextStyle(color = Color.White),
                        )
                }

            }
        }
    }
    Row(
        Modifier
            .height(175.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(2) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.LightGray else Color.DarkGray
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .background(color, CircleShape)
                    .size(10.dp)
            )
        }
    }

}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}