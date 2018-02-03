package com.toyins.fbsave.Silo

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.widget.Toast
import com.downloader.*
import com.toyins.fbsave.MainActivity
import com.toyins.fbsave.R
import com.toyins.fbsave.Utils.*
import com.toyins.fbsave.dBstore.Store
import im.delight.android.webview.AdvancedWebView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate
import io.reactivex.internal.operators.observable.ObservableAny
import io.reactivex.internal.operators.observable.ObservableSubscribeOn
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Timed
import kotlinx.android.synthetic.main.facebook.*
import kotlinx.android.synthetic.main.facebook.view.*
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class Facebook : Fragment(), SwipeRefreshLayout.OnRefreshListener, AdvancedWebView.Listener {


   var observable :Disposable? = null

    var handler:Handler? = null
    val run:Runnable? = null
    var showing = false
    var webby:AdvancedWebView? = null
    var swipes:SwipeRefreshLayout? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater!!.inflate(R.layout.facebook, container, false)

        webby = v.mWebView
        swipes = v.swipeView

        val address = "http://facebook.com"
        initBrowser(address,webby!!)
        webby!!.setListener(activity,this@Facebook)
        return v
    }




    override fun onPageStarted(url: String?, favicon: Bitmap?) {
        swipes!!.isRefreshing = true
        webby!!.visibility = View.GONE


    }

    override fun onExternalPageRequest(url: String?) {


    }

    override fun onDownloadRequested(url: String?, suggestedFilename: String?, mimeType: String?, contentLength: Long, contentDisposition: String?, userAgent: String?) {


    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {


    }

    override fun onPageFinished(url: String?) {
        swipes!!.isRefreshing = false
        injectScriptFile(activity, "jquery.js", webby!!)
        injectScriptFile(activity, "fb/witty.js", webby!!)
        injectCSS(activity, "fb/style.css", webby!!)
        webby!!.visibility = View.VISIBLE

        lucas()

    }

    override fun onRefresh() {
        val address = "http://facebook.com"
        webby!!.loadUrl(address)

    }







    fun initBrowser( address:String, mWebView: AdvancedWebView){

        val jsInterface = JavaScriptInterface(activity)
        val webSettings = webby!!.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowUniversalAccessFromFileURLs = true
        webSettings.domStorageEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webby!!.webChromeClient = WebChromeClient()
        webby!!.loadUrl(address)
        webby!!.addJavascriptInterface(jsInterface, "JSInterface")
        webby!!.webViewClient = KustomClient()
        webby!!.setOnKeyListener(object:View.OnKeyListener{
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {

                if (event!!.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webby!!.canGoBack()) {
                        webby!!.goBack()
                    } else {

                        (activity as MainActivity).onBackPressed()
                    }
                    return true
                }
                return false

            }


        })

    }


    fun canGoBack(): Boolean {
        return webby!!.canGoBack()
    }

    fun goBack() {
        webby!!.goBack()
        webby!!!!.evaluateJavascript("test()") { }
    }

    fun reScript(){
        val video = context.getString(R.string.saveVideo)
        val photo = context.getString(R.string.savePhoto)
        webby!!.evaluateJavascript("test('$video','$photo')") {}
    }



 fun lucas(){

    observable = Observable.interval(3, TimeUnit.SECONDS,AndroidSchedulers.mainThread())

            .subscribe(object :Consumer<Long>{
                override fun accept(t: Long?) {

                    reScript()
                }




            })




 }

    override fun onResume() {
        super.onResume()
    }



    inner class JavaScriptInterface(private val activity: Activity) {

        @JavascriptInterface
        fun startVideo(videoAddress: String) {


            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {




                if (showing){



                }else{


                    if (videoAddress.contains("background-image")){

                        val active = getActivity() as MainActivity

                        val rebook = videoAddress.substring(0, videoAddress.indexOf("background-repeat"))
                        val tube = rebook.substring(rebook.indexOf("(")+1).replace(")","").replace(";","").replace("'","").replace("\\26","&").replace("\\3d","=").replace("\\3a",":").replace(" ","")

                        showing = photoDialog(context,tube,activity)



                    }

                    if (videoAddress.contains("\"videoID\"")){

                        val playresult = videoAddress.substring(0, videoAddress.indexOf("\"width\""))
                        val playtunes = playresult.substring(playresult.indexOf("\"src\"")+1).replace("\"", "").replace("src","").replace(",","").replace(":","").replace("/","").replace("https","https:")


                        val result = videoAddress.substring(0, videoAddress.indexOf(","))
                        val pattern = "\\D+".toRegex()
                        val tunes = result.replace(pattern,"")

                        showing = videoDialog(activity,tunes,playtunes,activity)


                    }

                }





            }else{

                AskPermission(activity, context)
            }


        }




        @JavascriptInterface
        fun reloader(vinky: String){

        }


    }


    override fun onDestroy() {
        super.onDestroy()
        if (observable != null && !observable!!.isDisposed) {
            observable!!.dispose()

        }

    }

    override fun onPause() {
        super.onPause()

        if (observable != null && !observable!!.isDisposed) {
            observable!!.dispose()

        }

    }








    // Downloader






}

