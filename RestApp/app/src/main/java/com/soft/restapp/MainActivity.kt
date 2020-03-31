package com.soft.restapp

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.zxing.integration.android.IntentIntegrator
import com.soft.restapp.model.Product
import com.soft.restapp.model.StatusResponseEntity
import com.soft.restapp.service.RetrofitService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_alert_dialog_create_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var mAdView: AdView
    lateinit var mRewardedAd: RewardedAd
    lateinit var currentPhotoFilePath: String

    val productAdapter = ProductAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val dialog = setUpNewProductListItemDialog()

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        floatingActionButton.setOnClickListener {
            mRewardedAd = RewardedAd(this, "ca-app-pub-3940256099942544/5224354917")

            val adLoadCallback = object : RewardedAdLoadCallback() {
                override fun onRewardedAdLoaded() {
                    // Ad successfully loaded.
                    Toast.makeText(this@MainActivity, "successfully ad view", Toast.LENGTH_LONG)
                        .show()
                }

                override fun onRewardedAdFailedToLoad(errorCode: Int) {
                    // Ad failed to load.
                    Toast.makeText(this@MainActivity, "Failed ad view", Toast.LENGTH_LONG)
                        .show()
                }
            }
            mRewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)
            dialog.show()
        }

        swiperefresh.isEnabled = true

        recyclerView.apply {
            setHasFixedSize(true)
            adapter = productAdapter
        }

        setUpNewProductListItemDialog()
        loadProductFromServer()

        swiperefresh.setOnRefreshListener {
            loadProductFromServer()
            swiperefresh.isRefreshing = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intent = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            try {
                val bitmap = writeImage(this, uri, createImage())
                btnUpload.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun setUpNewProductListItemDialog(): AlertDialog {
        val view = LayoutInflater.from(this).inflate(R.layout.layout_alert_dialog_create_item, null)
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setTitle("Add new Product Item")
            .setPositiveButton("OK") { dialog, _ -> postNewProductItem(dialog, view) }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        //ad rewarded test for download btn
        val btnDownloaded: AppCompatImageButton = view.findViewById(R.id.btnDownload)

        btnDownloaded.setOnClickListener {
            if (mRewardedAd.isLoaded) {
//                mRewardedAd = RewardedAd(this, "ca-app-pub-3940256099942544/5224354917")
                val activity = this@MainActivity
                val adCallback = object : RewardedAdCallback() {

                    override fun onRewardedAdOpened() {
                        super.onRewardedAdOpened()
                        Toast.makeText(this@MainActivity, "ad open", Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onRewardedAdClosed() {
                        super.onRewardedAdClosed()
                        Toast.makeText(this@MainActivity, "ad close", Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onUserEarnedReward(p0: RewardItem) {
                        Toast.makeText(this@MainActivity, "successfully ad view", Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onRewardedAdFailedToShow(p0: Int) {
                        super.onRewardedAdFailedToShow(p0)
                        Toast.makeText(this@MainActivity, "ad failed", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                mRewardedAd.show(activity, adCallback)
            } else {
                Toast.makeText(this@MainActivity, "ad not load", Toast.LENGTH_LONG)
                    .show()
            }

        }
        val dialog = alertDialogBuilder.setView(view).create()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return dialog
    }

    private fun postNewProductItem(dialog: DialogInterface, view: View) {

        val edTitle = view.findViewById<EditText>(R.id.editText)
        val edDesc = view.findViewById<EditText>(R.id.edDesc)

        val btnUpload = view.findViewById<ImageButton>(R.id.btnUpload)

        val edAvailable = view.findViewById<CheckBox>(R.id.checkBox2)
        val product =
            Product(edTitle.text.toString(), edDesc.text.toString(), "", edAvailable.isChecked)

        swiperefresh.isRefreshing = true

        btnUpload.setOnClickListener {
            choosePhotoIntent()
        }

        RetrofitService.factoryService().addProductItem(product)
            .enqueue(object : Callback<StatusResponseEntity<Product>?> {
                override fun onFailure(call: Call<StatusResponseEntity<Product>?>, t: Throwable) {
                    swiperefresh.isRefreshing = false
                    Toast.makeText(
                        this@MainActivity,
                        "Check your internet connection. ",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<StatusResponseEntity<Product>?>,
                    response: Response<StatusResponseEntity<Product>?>
                ) {
                    swiperefresh.isRefreshing = false
                    if (response.body() != null && response.body()?.status == true) {
                        productAdapter.add(response.body()!!.entity!!)
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Something went terribly wrong ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
        dialog.dismiss()

    }

    private fun choosePhotoIntent() {
        val contentIntent = Intent(Intent.ACTION_GET_CONTENT)
        contentIntent.type = "image/*"
        val pickPhotoIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val chooseIntent = Intent.createChooser(contentIntent, "Choose Photo")
        chooseIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickPhotoIntent))
        startActivityForResult(chooseIntent, PICK_IMAGE)
    }

    private fun createImage(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmm", Locale.ENGLISH).format(Date())
        val imageFileName = "JPEG" + "_" + timestamp + "_";
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
        currentPhotoFilePath = image.absolutePath
        return image
    }

    private fun loadProductFromServer() {
        swiperefresh.isRefreshing = true
        RetrofitService.factoryService().getProductList()
            .enqueue(object : Callback<List<Product>?> {
                override fun onFailure(call: Call<List<Product>?>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "Something went wrong please check your network!",
                        Toast.LENGTH_LONG
                    ).show()
                    swiperefresh.isRefreshing = false
                }

                override fun onResponse(
                    call: Call<List<Product>?>,
                    response: Response<List<Product>?>
                ) {
                    swiperefresh.isRefreshing = false
                    val items = response.body()
                    Log.d("TAG", items?.size.toString())
                    if (items != null) {
                        productAdapter.addAll(items)
                    }
                }
            })
    }

    companion object {
        private const val PICK_IMAGE = 3

        private fun writeImage(context: Context, uri: Uri, imageFile: File): Bitmap {

            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true

            BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(uri),
                null,
                options
            )

            val REQUIRED_SIZE = 240
            var width_tmp = options.outWidth
            var height_tmp = options.outHeight

            var scale = 1
            while (width_tmp / 2 >= REQUIRED_SIZE && height_tmp / 2 >= REQUIRED_SIZE) {
                width_tmp /= 2
                height_tmp /= 2
                scale *= 2
            }

            val scaledOpts = BitmapFactory.Options()
            scaledOpts.inSampleSize = scale

            val bitmap = BitmapFactory.decodeStream(
                context.contentResolver.openInputStream(uri),
                null,
                scaledOpts
            )
            val fos = FileOutputStream(imageFile)
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, fos)
            return bitmap
        }
    }
}
