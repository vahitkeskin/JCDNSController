package com.vahitkeskin.jcdnscontroller

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PrivateDnsScreen() {
    val context = LocalContext.current
    var status by remember { mutableStateOf(PrivateDnsStatus(false, null, emptyList())) }

    // Anlık dinleme
    LaunchedEffect(Unit) {
        observePrivateDns(context).collectLatest { status = it }
    }

    // Basit UI
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Özel DNS (Private DNS) Durumu", style = MaterialTheme.typography.titleLarge)
        AssistChipRow(status)

        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Özel DNS açık mı?: ${if (status.isPrivateDnsActive) "Evet" else "Hayır"}")
                Text(
                    "Sunucu adı: ${status.privateDnsServerName ?: "—"}",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
                Text("Etkin ağın DNS sunucuları:")
                if (status.dnsServers.isEmpty()) {
                    Text("—")
                } else {
                    status.dnsServers.forEach { ip -> Text("• $ip") }
                }
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Not: Private DNS API'leri Android 9 (API 28) ve sonrası için mevcuttur.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun AssistChipRow(status: PrivateDnsStatus) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AssistChip(
            onClick = {},
            label = { Text(if (status.isPrivateDnsActive) "Özel DNS: AÇIK" else "Özel DNS: KAPALI") }
        )
        AssistChip(
            onClick = {},
            label = { Text(status.privateDnsServerName ?: "Sunucu adı: —") }
        )
    }
}