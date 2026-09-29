package com.example.practise.presentation.screens.Dashboard


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.practise.R

private val Navy = Color(0xFF092A45)
private val Blue = Color(0xFF0785F9)

@Composable
fun DashboardScreen(
    navHost: NavHostController,
    paddingValues: PaddingValues,
    onGetStarted: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



        Text(
            text = "Welcome to\nCurrency Convert!",
            modifier = Modifier.padding(top = 25.dp),
            color = Navy,
            fontSize = 23.sp,
            lineHeight = 29.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(104.dp))



        Image(
           painter = painterResource(
                R.drawable.dash
            ),
            contentDescription = "Currency conversion",
            modifier = Modifier
                .fillMaxWidth()
                .height(177.dp),
            contentScale = ContentScale.FillBounds
        )

        Spacer(modifier = Modifier.height(93.dp))


        Text(
            text = buildAnnotatedString {

                append("Instantly convert between over\n")

                withStyle(
                    SpanStyle(
                        color = Blue,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                ) {
                    append("150 currencies.")
                }

                append(" Currency Convert is your\n")
                append("one-stop solution for effortless currency\n")
                append("conversions.")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 3.dp),
            color = Color(0xFF111111),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )

        // Push button toward bottom
        Spacer(modifier = Modifier.weight(1f))



        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .height(49.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(17.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Get started",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(21.dp))
    }
}