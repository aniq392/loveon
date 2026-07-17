package xyz.fsg123.loveon.feature.profile

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView



@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LiveStreamWebView(
    modifier: Modifier = Modifier

) {
    val context = LocalContext.current

    val webView = remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            WebView.setWebContentsDebuggingEnabled(true)

            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    Log.d("LOVEON", "START : $url")
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    Log.d("LOVEON", "FINISH : $url")
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    Log.e("LOVEON", "ERROR : ${error?.description}")
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                    Log.d(
                        "JS",
                        "${consoleMessage.message()} (${consoleMessage.lineNumber()})"
                    )
                    return true
                }
            }

            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                mediaPlaybackRequiresUserGesture = false
                allowFileAccess = true
                allowContentAccess = true
                databaseEnabled = true

                // 화면에 꽉 차게 맞추기 위한 중요한 메타데이터 허용 설정들
                useWideViewPort = true      // HTML의 viewport 메타태그를 준수하도록 설정
                loadWithOverviewMode = true // 페이지가 화면에 맞춰 축소되어 보이도록 설정

                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            loadUrl("https://ai.modubet.net/plugin/live/loveon_stream.php")
        }
    }

    DisposableEffect(webView) {
        onDispose {
            try {
                webView.stopLoading()
                webView.clearHistory()
                webView.removeAllViews()
                webView.destroy()
                Log.d("LOVEON", "WebView가 안전하게 해제되었습니다.")
            } catch (e: Exception) {
                Log.e("LOVEON", "WebView 해제 중 에러 발생: ${e.message}")
            }
        }
    }

    // AndroidView 자체를 가로로 꽉 채운 후, 높이를 16:9 비율에 맞춰 강제 설정합니다.
    AndroidView(
        modifier = modifier
            .fillMaxWidth()              // 가로 화면을 꽉 채웁니다.
            .aspectRatio(16f / 9f),      // 높이를 가로 대비 16:9 비율로 자동 고정합니다.
        factory = { webView }
    )
}