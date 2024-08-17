package io.nekohasekai.sfa.utils

import go.Seq
import io.nekohasekai.libbox.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

open class CommandClient(
    private val scope: CoroutineScope,
    private val connectionType: ConnectionType,
    private val handler: Handler
) {

    enum class ConnectionType {
        Status, Groups, Log, ClashMode, Connections
    }

    interface Handler : CommandClientHandler {
        // This interface now directly extends CommandClientHandler
    }

    private var commandClient: CommandClient? = null
    private val clientHandler = ClientHandler()

    fun connect() {
        disconnect()
        val options = CommandClientOptions()
        options.command = when (connectionType) {
            ConnectionType.Status -> Libbox.CommandStatus
            ConnectionType.Groups -> Libbox.CommandGroup
            ConnectionType.Log -> Libbox.CommandLog
            ConnectionType.ClashMode -> Libbox.CommandClashMode
            ConnectionType.Connections -> Libbox.CommandConnections
        }
        options.statusInterval = 2 * 1000 * 1000 * 1000
        val commandClient = CommandClient(clientHandler, options)
        scope.launch(Dispatchers.IO) {
            for (i in 1..10) {
                delay(100 + i.toLong() * 50)
                try {
                    commandClient.connect()
                } catch (ignored: Exception) {
                    continue
                }
                if (!isActive) {
                    runCatching {
                        commandClient.disconnect()
                    }
                    return@launch
                }
                this@CommandClient.commandClient = commandClient
                return@launch
            }
            runCatching {
                commandClient.disconnect()
            }
        }
    }

    fun disconnect() {
        commandClient?.apply {
            runCatching {
                disconnect()
            }
            Seq.destroyRef(refnum)
        }
        commandClient = null
    }

    private inner class ClientHandler : CommandClientHandler {
        override fun connected() {
            handler.connected()
        }

        override fun disconnected(message: String) {
            handler.disconnected(message)
        }

        override fun clearLogs() {
            handler.clearLogs()
        }

        override fun writeLogs(messageList: StringIterator) {
            handler.writeLogs(messageList)
        }

        override fun writeStatus(message: StatusMessage) {
            handler.writeStatus(message)
        }

        override fun writeGroups(message: OutboundGroupIterator) {
            handler.writeGroups(message)
        }

        override fun initializeClashMode(modeList: StringIterator, currentMode: String) {
            handler.initializeClashMode(modeList, currentMode)
        }

        override fun updateClashMode(newMode: String) {
            handler.updateClashMode(newMode)
        }

        override fun writeConnections(message: Connections) {
            handler.writeConnections(message)
        }
    }
}
