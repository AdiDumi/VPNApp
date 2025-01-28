package com.example.vpnapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class KeyListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PublicKeyAdapter
    private val keys = mutableListOf(
        PublicKeyItem("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu1H7EJHNEV7FGlM3l7+g\n" +
                "Z7+E4D5ol6VZyQXeQHZHIu5jJfiR1jJYPwz7zXxh5N5YQ1HYI9gRj2fY7EeZf7vW\n" +
                "J6k/5Z9FYZ3iI5gXcLQH2B4U4BvJ6yzsQy1T9U6CB9vKF9Ybc7F4F+UB9U7QHT2R\n" +
                "XOQ/7Pi8qUoM5bD9HVzFtD2v5fO2j/EtgZL2cS0ZXVksQxY2GQ5Qk8m8HV2BQIDAQAB"),
        PublicKeyItem("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArU9cL9k3K2vI9bLnE+qG\n" +
                "DNJ1dL5rZYRkY4U+ZQk5XJh5c13K8g9D1XJRrZ8kFh5z9m1IbcKx5tB5Ikm3yM8k\n" +
                "A5RIQ2+Z7sH7KI5yCfzX7vK8aJ+Y9Z5tN2pTLR5CJ3X8V4U8bKyQp9Ih9jX4R1sO\n" +
                "J+7X7fK9VZK7Y8I9X9yRW5N2CZQk8IbG4Y5R8N1X8bV9J2k5R5Y4F9X+K1K5DWwIDAQAB"),
        PublicKeyItem("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxL+YI5R5X8K9J2m5V9F8\n" +
                "K3L5dJ8R4N3O9V1I8Yk8dJ5X5K2M9W8C9V7F5Y9X8K3N8R7C5V5I8Yk8F2N5R6V3\n" +
                "Y9X8K3L5N8C9V7F5Y9X8K3N8Yk8F2N5R7C9V1O8L5R6V7Y8X5K3M9V2R4F5X3C9V\n" +
                "7F5Y9L5X8M2K3V9R8X5O1N5C2X8R4V7Y5K3L8X9W5R7V3C5X2O7V9L8F5X7C9V0IDAQAB")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_key_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewKeys)
        setupRecyclerView()

        return view
    }

    private fun setupRecyclerView() {
        adapter = PublicKeyAdapter(keys) { keyItem ->
            showKeyOptionsDialog(keyItem)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun showKeyOptionsDialog(keyItem: PublicKeyItem) {
        var options = arrayOf("Use", "Update", "Delete")
        if(keyItem.isInUse) {
            options = arrayOf("Deselect", "Update", "Delete")
        }
        AlertDialog.Builder(requireContext())
            .setTitle("Select an Option")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> { // Use
                        // Mark this key as in use and update the RecyclerView
                        if (!keyItem.isInUse) {
                            keys.forEach { it.isInUse = false } // Clear previous selection
                            keyItem.isInUse = true
                        } else {
                            keys.forEach { it.isInUse = false }
                        }
                        adapter.notifyDataSetChanged()
                    }
                    1 -> { // Update
                        // Perform update logic here
                        showSignaturesDialog(keyItem)
                    }
                    2 -> { // Delete
                        // Remove the selected key from the list
                        val position = keys.indexOf(keyItem)
                        if (position != -1) {
                            keys.removeAt(position) // Remove the item from the list
                            adapter.notifyItemRemoved(position) // Notify the adapter about item removal
                        }
                        Toast.makeText(requireContext(), "Deleted ${keyItem.key}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .show()
    }

    private fun showSignaturesDialog(keyItem: PublicKeyItem) {
        if (keyItem.signatures.isEmpty()) {
            keyItem.signatures = mutableListOf(
                Signature(id = "1", signatureText = "Signature 1", appIdentifier = "App A"),
                Signature(id = "2", signatureText = "Signature 2", appIdentifier = "App B"),
                Signature(id = "3", signatureText = "Signature 3", appIdentifier = "App C"),
                Signature(id = "4", signatureText = "Signature 4", appIdentifier = "App D")
            )
        }

        // Create a custom layout for the dialog
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.sigantures_list, null)
        val recyclerView: RecyclerView = dialogView.findViewById(R.id.recycler_view_signatures)

        // Set up the adapter for signatures
        val adapter = SignatureAdapter(keyItem.signatures) { signature ->
            // Remove the selected signature from the list
            keyItem.signatures.remove(signature)
            adapter.notifyDataSetChanged()
            Toast.makeText(
                requireContext(),
                "Deleted signature: ${signature.signatureText}",
                Toast.LENGTH_SHORT
            ).show()
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Show the dialog
        AlertDialog.Builder(requireContext())
            .setTitle("Signatures for Key")
            .setView(dialogView)
            .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}