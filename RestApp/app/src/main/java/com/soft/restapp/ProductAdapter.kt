package com.soft.restapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.soft.restapp.model.Product
import com.soft.restapp.model.StatusResponseEntity
import com.soft.restapp.service.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductAdapter(private val products: ArrayList<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvProductTitle = itemView.findViewById<TextView>(R.id.productTitle)
        //        private val tvProductDesc = itemView.findViewById<TextView>(R.id.desc)
        private val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)

        fun bind(product: Product) {
            tvProductTitle.text = product.name
            checkBox.isChecked = product.isAvailable
            checkBox.text = if (product.isAvailable) "Sold Out" else "In Stock"

            checkBox.setOnCheckedChangeListener { _, checked ->
                RetrofitService.factoryService().productAvailable(checked, product.id)
                    .enqueue(object : Callback<StatusResponseEntity<Boolean>?> {
                        override fun onFailure(
                            call: Call<StatusResponseEntity<Boolean>?>,
                            t: Throwable
                        ) {
                            Snackbar.make(
                                itemView,
                                "Failed to update product",
                                Snackbar.LENGTH_LONG
                            ).show()
                            checkBox.isChecked = !checked
                        }

                        override fun onResponse(
                            call: Call<StatusResponseEntity<Boolean>?>,
                            response: Response<StatusResponseEntity<Boolean>?>
                        ) {
                            if (response.body() != null) {
                                Snackbar.make(
                                    itemView,
                                    response.body()?.message.toString(), Snackbar.LENGTH_LONG
                                ).show()
                            } else {
                                Snackbar.make(
                                    itemView,
                                    "Failed to update item", Snackbar.LENGTH_LONG
                                ).show()
                                checkBox.isChecked = !checked
                            }
                        }
                    })
            }
        }
    }

    fun addAll(products: List<Product>) {
        this.products.addAll(products)
        notifyDataSetChanged()
    }

    fun add(product: Product) {
        this.products.add(product)
        notifyDataSetChanged()
    }

    fun clear() {
        this.products.clear()
        notifyDataSetChanged()
    }

}