package com.example.practise.presentation.screens.Home.util


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PrimaryBlue = Color(0xFF087FF5)
private val DarkButton = Color(0xFF092A45)

@Composable
fun HomeHeader(
    balance: String = "₹ 24,224.90",
    onWithdrawClick: () -> Unit = {},
    onDepositClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .background(PrimaryBlue)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 20.dp
                )
        ) {


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Profile image
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👤",
                        fontSize = 22.sp
                    )
                }

                Spacer(
                    modifier = Modifier.size(9.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Good Morning 👋",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Text(
                        text = "Alex Walker",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Notification
                HeaderIconButton(
                    onClick = onNotificationClick
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.size(8.dp)
                )

                // Settings
                HeaderIconButton(
                    onClick = onSettingsClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )


            Text(
                text = "Available Balance",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(
                modifier = Modifier.height(3.dp)
            )

            Text(
                text = balance,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                HeaderActionButton(
                    text = "WITHDRAW",
                    backgroundColor = Color.White,
                    textColor = DarkButton,
                    modifier = Modifier.weight(1f),
                    onClick = onWithdrawClick
                )

                HeaderActionButton(
                    text = "DEPOSIT",
                    backgroundColor = DarkButton,
                    textColor = Color.White,
                    modifier = Modifier.weight(1f),
                    onClick = onDepositClick
                )
            }
        }
    }
}


@Composable
private fun HeaderIconButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(
                Color.White.copy(alpha = 0.12f)
            ),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(34.dp)
        ) {
            content()
        }
    }
}


@Composable
private fun HeaderActionButton(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier.height(39.dp),
        shape = RoundedCornerShape(11.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        elevation = null
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}