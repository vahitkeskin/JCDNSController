package com.vahitkeskin.jcdnscontroller

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.net.InetAddress

data class PrivateDnsStatus(
    val isPrivateDnsActive: Boolean,
    val privateDnsServerName: String?,
    val dnsServers: List<String>
)

fun ConnectivityManager.currentPrivateDnsStatus(): PrivateDnsStatus {
    val lp = activeNetwork?.let { getLinkProperties(it) }
    return lp.toStatus()
}

private fun LinkProperties?.toStatus(): PrivateDnsStatus {
    if (this == null) return PrivateDnsStatus(false, null, emptyList())
    val isActive = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        this.isPrivateDnsActive
    } else false

    val name = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        this.privateDnsServerName
    } else null

    val servers = this.dnsServers.map(InetAddress::getHostAddress)
    return PrivateDnsStatus(isActive, name, servers)
}

/**
 * Akış (Flow) olarak güncellemeleri verir: ağ değişince/emniyet DNS değişince tetiklenir.
 */
fun observePrivateDns(context: Context): Flow<PrivateDnsStatus> = callbackFlow {
    val cm = context.getSystemService(ConnectivityManager::class.java)

    // İlk değer
    trySend(cm.currentPrivateDnsStatus())

    val request = NetworkRequest.Builder().build()
    val callback = object : NetworkCallback() {
        override fun onAvailable(network: Network) {
            trySend(cm.currentPrivateDnsStatus())
        }

        override fun onLost(network: Network) {
            trySend(cm.currentPrivateDnsStatus())
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            trySend(linkProperties.toStatus())
        }
    }

    cm.registerNetworkCallback(request, callback)
    awaitClose { cm.unregisterNetworkCallback(callback) }
}