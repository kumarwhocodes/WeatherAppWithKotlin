package com.kumar.weather

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

    var inputCity by remember { mutableStateOf("") }
    var showCity by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("") }
    val mintemp by remember { mutableStateOf("") }
    val maxtemp by remember { mutableStateOf("") }
    val weatherCond by viewModel.weatherCond.collectAsState(WeatherCondition.CLEAR)

    val currentDate = LocalDate.now()

    val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("EEEE"))

    WeatherTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp),
            verticalArrangement = Arrangement.SpaceBetween,
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
                                fontFamily = abelFontFamily,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedTextColor = MaterialTheme.colorScheme.secondary,
                            cursorColor = MaterialTheme.colorScheme.secondary,
                            focusedLabelColor = MaterialTheme.colorScheme.background,
                            unfocusedLabelColor = Color.Transparent,
                            focusedBorderColor = MaterialTheme.colorScheme.background,
                            unfocusedBorderColor = Color.Transparent
                        ),
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
                                onClick = {
                                    try {
                                        viewModel.onSearchClicked()
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Invalid City", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                },
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


            Column {
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
                        fontSize = 34.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily,
                        fontWeight = FontWeight.ExtraBold
                    )

                }
                //day and time
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
                        color = MaterialTheme.colorScheme.secondary,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily
                    )

                }
            }

            //Weather Condition Image
            val iconUrl by viewModel.iconUrl.collectAsState("")
            Row {
                if (iconUrl.isNotEmpty()) {
                    val painter = rememberImagePainter(iconUrl)
                    Image(
                        painter = painter,
                        contentDescription = "Weather icon",
                        modifier = Modifier.size(150.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
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
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold
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
                        color = MaterialTheme.colorScheme.secondary,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily
                    )

                }
            }

            Spacer(modifier = Modifier.height(40.dp))

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
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.wind_icon),
                        contentDescription = "sunrise",
                        modifier = Modifier.size(50.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.sunset),
                        contentDescription = "sunrise",
                        modifier = Modifier.size(50.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
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
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "WIND",
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "SUNSET",
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily,
                        fontWeight = FontWeight.SemiBold
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
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    val windSpeed by viewModel.windSpeed.collectAsState()
                    Text(
                        text = "$windSpeed" + "m/s",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    val sunsetTime by viewModel.sunsetTime.collectAsState("")
                    Text(
                        text = sunsetTime,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily,
                        fontWeight = FontWeight.Bold
                    )

                }
            }

            Spacer(modifier = Modifier.height(0.dp))

            //otherssssss
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(start = 25.dp, end = 30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.humidity_icon),
                        contentDescription = "sunrise",
                        modifier = Modifier.size(50.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.temp_hot),
                        contentDescription = "sunrise",
                        modifier = Modifier.size(50.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.visibility),
                        contentDescription = "sunrise",
                        modifier = Modifier.size(50.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(start = 25.dp, end = 25.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "HUMIDITY",
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "FEELS LIKE",
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "VISIBILITY",
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(start = 35.dp, end = 30.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val humidity by viewModel.humidity.collectAsState("")
                    Text(
                        text = "$humidity" + "%",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    val feelslike by viewModel.feels.collectAsState()
                    Text(
                        text = "  " + "$feelslike" + "°C",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                    val visibility by viewModel.visibility.collectAsState("")
                    Text(
                        text = "$visibility" + "m",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontStyle = FontStyle.Normal,
                        fontFamily = abelFontFamily,
                        fontWeight = FontWeight.Bold
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

