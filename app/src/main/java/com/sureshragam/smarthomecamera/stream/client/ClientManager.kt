package com.sureshragam.smarthomecamera.stream.client

import java.util.concurrent.CopyOnWriteArrayList

object ClientManager {

    private val clients =
        CopyOnWriteArrayList<ClientConnection>()

    fun add(client: ClientConnection) {
        clients.add(client)
    }

    fun remove(client: ClientConnection) {
        clients.remove(client)
    }

    fun getClients(): List<ClientConnection> {
        return clients
    }

    fun clientCount(): Int {
        return clients.size
    }
}