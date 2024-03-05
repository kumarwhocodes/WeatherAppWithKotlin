package com.kumar.weather

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.kumar.weather.network.WeatherApiService
import com.kumar.weather.ui.theme.WeatherTheme
import com.kumar.weather.ui.theme.newBG
import kotlinx.coroutines.flow.observeOn

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Weather(viewModel)
                }
            }
        }

    }
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Weather(viewModel: WeatherViewModel) {

    var inputCity by remember { mutableStateOf("") }
    var showCity by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("") }
    val mintemp by remember { mutableStateOf("") }
    val maxtemp by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = newBG)
            .padding(10.dp)
    ) {

        //TopBar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Weather",
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                fontSize = 32.sp,
                color = Color.White,
                fontFamily = FontFamily.Serif
            )
        }

        Spacer(modifier = Modifier.padding(bottom = 15.dp))

        //Search Box
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = newBG,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val keyboardController = LocalSoftwareKeyboardController.current
                val inputCity by viewModel.inputCity.collectAsState("")

                OutlinedTextField(value = inputCity,
                    onValueChange = { viewModel.onInputCityChanged(it) },
                    label = { Text("Enter city:") },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = Color.White, // Set text color to white
                        cursorColor = Color.White, // Set cursor color to white
                        focusedLabelColor = Color.White, // Set focused label color to white
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                    ),// Set unfocused label color to white with alpha
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        viewModel.onSearchClicked()
                        keyboardController?.hide()
                    }),
                    modifier = Modifier
                        .background(color = Color.Transparent)
                        .padding(end = 5.dp),
                    trailingIcon = {
                        FloatingActionButton(
                            onClick = { viewModel.onSearchClicked() },
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "search_city_fab",
                                Modifier.background(color = Color.Transparent)
                            )
                        }
                    })


            }

        }

        //City name
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val showCity by viewModel.showCity.collectAsState("")
            Text(
                text = showCity,
                textAlign = TextAlign.Center,
                fontSize = 32.sp,
                color = Color.White,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Monospace
            )

        }

        //Temperature
        Box(
            modifier = Modifier.border(2.dp, Color.Cyan, shape = RoundedCornerShape(8.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                val temperature by viewModel.temperature.collectAsState()
                Row {
                    Text(
                        text = "$temperature°C",
                        textAlign = TextAlign.Center,
                        fontSize = 32.sp,
                        fontFamily = FontFamily.Serif,
                        color = Color.White
                    )

                }


                Row {
                    //mintemp
                    Image(
                        painter = painterResource(id = R.drawable.temp_cold),
                        contentDescription = "mintemp",
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier
                            .padding(5.dp)
                            .width(30.dp)
                            .height(30.dp),
                        alignment = Alignment.TopStart
                    )
                    val mintemp by viewModel.mintemp.collectAsState()
                    Text(
                        text = "$mintemp°C",
                        fontSize = 26.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Serif,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.width(175.dp))

                    //maxtemp
                    Image(
                        painter = painterResource(id = R.drawable.temp_hot),
                        contentDescription = "maxtemp",
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier
                            .padding(5.dp)
                            .width(30.dp)
                            .height(30.dp),
                        alignment = Alignment.TopStart
                    )
                    val maxtemp by viewModel.maxtemp.collectAsState()
                    Text(
                        text = "$maxtemp°C",
                        fontSize = 26.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Serif,
                        color = Color.White
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
