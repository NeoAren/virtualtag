package com.virtualtag.app.ui.screens

import android.nfc.NfcAdapter
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.virtualtag.app.R
import com.virtualtag.app.ui.components.CardContainer
import com.virtualtag.app.ui.components.Dialog
import com.virtualtag.app.ui.components.Logo
import com.virtualtag.app.ui.components.PrimaryButton
import com.virtualtag.app.ui.theme.BlackBG
import com.virtualtag.app.utils.stringToColor
import com.virtualtag.app.viewmodels.CardViewModel

@Composable
fun HomeScreen(model: CardViewModel, viewCard: (id: Int) -> Unit, scanCard: () -> Unit) {
    val context = LocalContext.current
    val cardList = model.getAllCards().observeAsState(listOf())
    var errorDialogOpen by remember { mutableStateOf(false) }

    // Set up NFC adapter
    val adapter = NfcAdapter.getDefaultAdapter(context)

    // Check if the phone has NFC sensor and show error if sensor is not available
    fun onScanClick() {
        if (adapter != null) return scanCard()
        errorDialogOpen = true
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onScanClick() },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            color = MaterialTheme.colors.secondaryVariant,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Logo(modifier = Modifier.padding(top = 16.dp))
                if (cardList.value.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            stringResource(R.string.list_placeholder_line1),
                            color = Color.LightGray,
                            fontSize = 32.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            stringResource(R.string.list_placeholder_line2),
                            color = Color.LightGray,
                            fontStyle = FontStyle.Italic,
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            stringResource(R.string.list_placeholder_line3),
                            color = Color.LightGray,
                            fontStyle = FontStyle.Italic,
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            stringResource(R.string.list_placeholder_line4),
                            color = Color.LightGray,
                            fontStyle = FontStyle.Italic,
                            fontSize = 24.sp
                        )
                    }
                }
                // List of all cards in the database
                LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                    items(cardList.value) { card ->
                        CardContainer(
                            onClick = { viewCard(card.id) },
                            enabled = true,
                            color = stringToColor(card.color)
                        ) {
                            Text(
                                card.name,
                                color = BlackBG,
                                modifier = Modifier.padding(vertical = 64.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (errorDialogOpen) {
        Dialog(
            closeDialog = { errorDialogOpen = false }, title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Outlined.ErrorOutline,
                        null, modifier = Modifier
                            .padding(top = 24.dp)
                            .size(72.dp),
                        tint = Color.Red
                    )
                    Text(
                        stringResource(R.string.nfc_sensor_error),
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            }, description = {
                Text(
                    stringResource(
                        R.string.nfc_sensor_error_description
                    ),
                    color = MaterialTheme.colors.secondary,
                    textAlign = TextAlign.Center
                )
            }, confirmButton = {}, dismissButton = {
                PrimaryButton(
                    text = "OK",
                    onClick = { errorDialogOpen = false },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        )
    }
}
