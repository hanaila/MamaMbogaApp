package com.example.mamambogaapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var groceryAdapter: GroceryAdapter

    private val groceryList = mutableListOf(
        GroceryItem(1, "Sukuma Wiki", 30.0, "bunch", "🥬"),
        GroceryItem(2, "Tomatoes", 50.0, "kg", "🍅"),
        GroceryItem(3, "Onions", 40.0, "kg", "🧅"),
        GroceryItem(4, "Dhania", 20.0, "bunch", "🌿"),
        GroceryItem(5, "Potatoes", 70.0, "kg", "🥔"),
        GroceryItem(6, "Cabbage", 50.0, "pc", "🥬"),
        GroceryItem(7, "Avocado", 20.0, "pc", "🥑"),
        GroceryItem(8, "Bananas", 50.0, "dozen", "🍌")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupListeners()
        updateCartSummary()
    }

    private fun setupRecyclerView() {
        val rvItems = findViewById<RecyclerView>(R.id.rvItems)
        rvItems.layoutManager = GridLayoutManager(this, 2)
        groceryAdapter = GroceryAdapter(groceryList) { _ ->
            updateCartSummary()
        }
        rvItems.adapter = groceryAdapter
    }

    private fun updateCartSummary() {
        val selectedItems = groceryList.filter { it.isSelected }
        val tvCartSummary = findViewById<TextView>(R.id.tvCartSummary)
        val tvTotalPrice = findViewById<TextView>(R.id.tvTotalPrice)

        if (selectedItems.isEmpty()) {
            tvCartSummary.text = "No items selected yet. Tap items above to add to your order."
            tvTotalPrice.text = "KES 0"
        } else {
            val sb = StringBuilder()
            var total = 0.0
            for (item in selectedItems) {
                val itemTotal = item.price * item.quantity
                total += itemTotal
                sb.append("• ${item.emoji} ${item.name} (${item.quantity} ${item.unit}) - KES ${itemTotal.toInt()}\n")
            }
            tvCartSummary.text = sb.toString().trim()
            tvTotalPrice.text = "KES ${total.toInt()}"
        }
    }

    private fun setupListeners() {
        findViewById<MaterialButton>(R.id.btnPurchase).setOnClickListener {
            val selectedItems = groceryList.filter { it.isSelected }
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Please select at least one item to purchase!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val total = selectedItems.sumOf { it.price * it.quantity }
            val summary = selectedItems.joinToString("\n") { "• ${it.name} (${it.quantity} ${it.unit}) - KES ${(it.price * it.quantity).toInt()}" }

            MaterialAlertDialogBuilder(this)
                .setTitle("Confirm Order 🛒")
                .setMessage("Mama Mboga has received your order:\n\n$summary\n\nTotal: KES ${total.toInt()}\n\nProceed to place order?")
                .setPositiveButton("Confirm & Pay") { _, _ ->
                    Toast.makeText(this, "Order placed successfully! Mama Mboga is preparing your fresh produce. 🎉", Toast.LENGTH_LONG).show()
                    // Reset selection
                    groceryList.forEach { it.isSelected = false }
                    groceryAdapter.notifyDataSetChanged()
                    updateCartSummary()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        // Social Media buttons
        findViewById<MaterialButton>(R.id.btnInstagram).setOnClickListener {
            openUrl("https://instagram.com")
        }
        findViewById<MaterialButton>(R.id.btnFacebook).setOnClickListener {
            openUrl("https://facebook.com")
        }
        findViewById<MaterialButton>(R.id.btnTwitter).setOnClickListener {
            openUrl("https://twitter.com")
        }
        findViewById<MaterialButton>(R.id.btnWhatsapp).setOnClickListener {
            openWhatsapp("+254700000000", "Hello Mama Mboga, I would like to order fresh produce from your app!")
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Could not open link", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openWhatsapp(phone: String, message: String) {
        try {
            val url = "https://api.whatsapp.com/send?phone=$phone&text=${Uri.encode(message)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
        }
    }
}
