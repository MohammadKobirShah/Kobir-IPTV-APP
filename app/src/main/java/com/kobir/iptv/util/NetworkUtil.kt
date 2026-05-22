package com.kobir.iptv.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

enum class ConnectionType {
    ETHERNET,
    WIFI,
    CELLULAR,
    VPN,
    NONE
}

data class NetworkState(
    val isConnected: Boolean = false,
    val connectionType: ConnectionType = ConnectionType.NONE,
    val isMetered: Boolean = false,
    val speed: Int = 0
)

class NetworkUtil(private val context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val currentNetworkState: NetworkState
        get() {
            val network = connectivityManager.activeNetwork
                ?: return NetworkState()
            val caps = connectivityManager.getNetworkCapabilities(network)
                ?: return NetworkState(isConnected = true)

            return NetworkState(
                isConnected = true,
                connectionType = resolveConnectionType(caps),
                isMetered = !caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED),
                speed = caps.linkDownstreamBandwidthKbps
            )
        }

    val networkStateFlow: Flow<NetworkState> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(currentNetworkState)
            }

            override fun onLost(network: Network) {
                trySend(NetworkState())
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                trySend(
                    NetworkState(
                        isConnected = true,
                        connectionType = resolveConnectionType(networkCapabilities),
                        isMetered = !networkCapabilities.hasCapability(
                            NetworkCapabilities.NET_CAPABILITY_NOT_METERED
                        ),
                        speed = networkCapabilities.linkDownstreamBandwidthKbps
                    )
                )
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        trySend(currentNetworkState)

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()

    val isEthernet: Boolean
        get() = currentNetworkState.connectionType == ConnectionType.ETHERNET

    val isWifi: Boolean
        get() = currentNetworkState.connectionType == ConnectionType.WIFI

    val connectionSpeedMbps: Int
        get() = currentNetworkState.speed / 1000

    private fun resolveConnectionType(caps: NetworkCapabilities): ConnectionType {
        return when {
            caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> ConnectionType.ETHERNET
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectionType.WIFI
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectionType.CELLULAR
            caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> ConnectionType.VPN
            else -> ConnectionType.NONE
        }
    }

    companion object {
        fun getConnectionTypeName(type: ConnectionType): String {
            return when (type) {
                ConnectionType.ETHERNET -> "Ethernet"
                ConnectionType.WIFI -> "WiFi"
                ConnectionType.CELLULAR -> "Cellular"
                ConnectionType.VPN -> "VPN"
                ConnectionType.NONE -> "No Connection"
            }
        }
    }
}
