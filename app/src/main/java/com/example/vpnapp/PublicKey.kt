package com.example.vpnapp

data class PublicKeyItem(
    val key: String, // Public key string
    var signatures: MutableList<Signature> = mutableListOf(),
    var isInUse: Boolean = false // Track if this key is currently "in use"
)