package com.kumar.weather

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.kumar.weather.ui.theme.WeatherTheme
import com.kumar.weather.ui.theme.abelFontFamily
import com.kumar.weather.ui.theme.lightBG
import com.kumar.weather.ui.theme.lightPrimary
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Weather(viewModel)
                }
            }
        }

    }
}


@SuppressLint("RememberReturnType", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Weather(viewModel: WeatherViewModel) {

    var inputCity by remember { mutableStateOf("") }
    var showCity by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("") }
    val mintemp by remember { mutableStateOf("") }
    val maxtemp by remember { mutableStateOf("") }
    val weatherCond by viewModel.weatherCond.collectAsState(WeatherCondition.CLEAR)


    // Get the current date
    val currentDate = LocalDate.now()

    // Format the date as needed (e.g., "Monday, March 9, 2024")
    val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("EEEE"))

    WeatherTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 180.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            //Search Box
            Box(
                modifier = Modifier
                    .padding(15.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(50.dp)

                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val keyboardController = LocalSoftwareKeyboardController.current
                    val inputCity by viewModel.inputCity.collectAsState("")

                    OutlinedTextField(value = inputCity,
                        onValueChange = { viewModel.onInputCityChanged(it) },
                        label = {
                            Text(
                                "Enter city:",
                                fontFamily = abelFontFamily
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedTextColor = Color.Black, // Set text color to white
                            cursorColor = Color.Black, // Set cursor color to white
                            focusedLabelColor = Color.Transparent, // Set focused label color to white
                            unfocusedLabelColor = Color.Transparent.copy(alpha = 0.7f),
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),// Set unfocused label color to white with alpha
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            viewModel.onSearchClicked()
                            keyboardController?.hide()
                        }),
                        modifier = Modifier
                            .background(color = Color.Transparent)
                            .padding(end = 2.dp)
                            .fillMaxWidth(1f),
                        leadingIcon = {
                            FloatingActionButton(
                                onClick = { /*TODO*/ },
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.primary,
                                elevation = FloatingActionButtonDefaults.elevation(0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.LocationOn,
                                    contentDescription = "Search by location",
                                    modifier = Modifier
                                        .background(color = Color.Transparent)
                                        .size(40.dp)
                                        .padding(start = 10.dp)
                                )
                            }
                        },
                        trailingIcon = {
                            FloatingActionButton(
                                onClick = { viewModel.onSearchClicked() },
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.primary,
                                elevation = FloatingActionButtonDefaults.elevation(0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = "search_city_fab",
                                    modifier = Modifier
                                        .background(color = Color.Transparent)
                                        .size(40.dp)
                                        .padding(end = 10.dp)
                                )
                            }
                        })
                }

            }


            //City name
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val showCity by viewModel.showCity.collectAsState("")
                Text(
                    text = showCity,
                    textAlign = TextAlign.Center,
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontStyle = FontStyle.Normal,
                    fontFamily = abelFontFamily
                )

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val updateTime by viewModel.updateTime.collectAsState("")
                Text(
                    text = formattedDate,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    fontStyle = FontStyle.Normal,
                    fontFamily = abelFontFamily
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = updateTime,
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontStyle = FontStyle.Normal,
                    fontFamily = abelFontFamily
                )

            }

            //Weather Condition Image
            val iconUrl by viewModel.iconUrl.collectAsState("")
            Row {
                if (iconUrl.isNotEmpty()) {
                    val painter = rememberImagePainter(iconUrl)
                    Image(
                        painter = painter,
                        contentDescription = "Weather icon",
                        modifier = Modifier.size(150.dp)
                    )
                }
            }

            //Temperature
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                val temperature by viewModel.temperature.collectAsState()
                Row {
                    Text(
                        text = "$temperature°C",
                        textAlign = TextAlign.Center,
                        fontSize = 40.sp,
                        fontFamily = abelFontFamily,
                        color = Color.Black
                    )

                }

                //Weather condition
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    val weatherCond by viewModel.weatherCond.collectAsState("")
                    Text(
                        text = weatherCond.toString(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily
                    )

                }
            }

            //others
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(start = 30.dp, end = 25.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sunrise),
                        contentDescription = "sunrise",
                        modifier = Modifier.size(50.dp),
                        colorFilter = ColorFilter.tint(lightPrimary)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.wind_icon),
                        contentDescription = "sunrise",
                        modifier = Modifier.size(50.dp),
                        colorFilter = ColorFilter.tint(lightPrimary)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.sunset),
                        contentDescription = "sunrise",
                        modifier = Modifier.size(50.dp),
                        colorFilter = ColorFilter.tint(lightPrimary)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(start = 30.dp, end = 30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "SUNRISE",
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily
                    )
                    Text(
                        text = "WIND",
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily
                    )
                    Text(
                        text = "SUNSET",
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily
                    )

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(start = 35.dp, end = 30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val sunriseTime by viewModel.sunriseTime.collectAsState("")
                    Text(
                        text = sunriseTime,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily
                    )
                    val windSpeed by viewModel.windSpeed.collectAsState()
                    Text(
                        text = "$windSpeed" + "m/s",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily
                    )
                    val sunsetTime by viewModel.sunsetTime.collectAsState("")
                    Text(
                        text = sunsetTime,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily
                    )

                }
            }


        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun WeatherAppPreview() {
    Weather(viewModel = WeatherViewModel())
}

