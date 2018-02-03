package com.toyins.fbsave

import android.app.DownloadManager
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.Toast
import com.downloader.*
import com.esafirm.rxdownloader.RxDownloader
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.internal.zzahn
import com.toyins.fbsave.Silo.Downloads
import com.toyins.fbsave.Silo.Facebook
import com.toyins.fbsave.Silo.Instagram
import com.toyins.fbsave.Tabs.ViewPagerAdapter
import com.toyins.fbsave.Utils.Alariwo
import com.toyins.fbsave.Utils.snackUp
import hotchemi.android.rate.AppRate
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.download_progress.*
import java.io.File

class MainActivity : AppCompatActivity() {
    var observable: Observable<String>? = null
    var ddownloadUrl = ""
    var mAdView: AdView? = null
    var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewPager(viewpager)
        tabs.setupWithViewPager(viewpager)


        Starters(applicationContext)


        AppRate.with(this@MainActivity)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(3) // default 10
                .setRemindInterval(2) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false)
                .monitor()

        AppRate.showRateDialogIfMeetsConditions(this);


    }







    //Ads or Lectures

    fun Starters(context: Context){

        val t = Thread(Runnable {
            //  Initialize SharedPreferences
            val getPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context)

            //  Create a new boolean and preference and set it to true
            val isFirstStart = getPrefs.getBoolean("firstStart", true)

            //  If the activity has never started before...
            if (isFirstStart) {

                //  Launch app intro
                val i = Intent(context, Lecturer::class.java)

                zzahn.runOnUiThread { startActivity(i) }

                //  Make a new preferences editor
                val e = getPrefs.edit()

                //  Edit preference to make it false because we don't want this to run again
                e.putBoolean("firstStart", false)

                //  Apply changes
                e.apply()
            }else{

                runOnUiThread(Runnable {

                    mAdView = findViewById<AdView>(R.id.adView)
                    val adRequest = AdRequest.Builder()
                            .build()
                    mAdView!!.loadAd(adRequest)

                })



            }
        })

        // Start the thread
        t.start()

    }








    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(Facebook(), "Facebook")
        adapter.addFragment(Instagram(), "Instagram")
        adapter.addFragment(Downloads(), getString(R.string.downloads))
        viewPager.adapter = adapter
    }








    // Process Video Download















    fun mrSave(urld: String,variety:String){

        val rxDownloader = RxDownloader(this@MainActivity)
        var extension = ""
        var desc = ""

        if (variety.contains("photo")){

            val bean = urld.substring(urld.lastIndexOf(".") + 1)

            if(bean.contains("jpg")){
                extension = "jpg"
            }

            if(bean.contains("png")){
                extension = "png"
            }


            if(bean.contains("gif")){
                extension = "gif"
            }
            desc = getString(R.string.downloadPhoto)


        }

        if (variety.contains("video")){

            extension = "mp4"
            desc = getString(R.string.downloadVideo)


        }



        val timeStamp =  System.currentTimeMillis()
        val filename = "fb_$variety"+"_"+timeStamp
        val name = filename + "." + extension
        val dex = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "fbsave")
        if (!dex.exists())
            dex.mkdirs()

        val Download_Uri = Uri.parse(urld)
        val downloadManager =  getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request =  DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false)
        request.setTitle(name)
        request.setDescription(desc)
        request.setVisibleInDownloadsUi(true)
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/fbsave/" + name)

        rxDownloader.download(request).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Observer<String> {
                    override fun onComplete() {


                    }

                    override fun onError(e: Throwable) {


                    }

                    override fun onNext(t: String) {


                    }

                    override fun onSubscribe(d: Disposable) {


                    }


                });

    }



}
