package com.ban.audiopanel

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.ban.audiopanel.databinding.ActivityMainBinding
import com.iflytek.cloud.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var mIat: SpeechRecognizer
    private lateinit var mSpeechSynthesizer: SpeechSynthesizer

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.currentListen.observe(this) {
            if (it != 0) {
                startListen()
            }
        }

        binding.fab.setOnClickListener { startListen() }
        initIat()
    }

    private fun startListen() {
        if (mIat.isListening) {
            mIat.stopListening()
        }
        //开始识别，并设置监听器
        mIat.startListening(object : RecognizerListener {
            override fun onVolumeChanged(p0: Int, p1: ByteArray?) {
            }

            override fun onBeginOfSpeech() {
            }

            override fun onEndOfSpeech() {
            }

            override fun onResult(result: RecognizerResult?, isLast: Boolean) {
                if (null != result) {
                    val s = result.resultString
                    Log.i("ban", "识别结果为:${s}")
                    viewModel.send(s)
//                        mSpeechSynthesizer.speak(result.resultString)
                } else {
                    Log.i("ban", "recognizer result : null")
                }
            }

            override fun onError(p0: SpeechError?) {
            }

            override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {
            }
        })
    }

    private fun initIat() {
        //初始化识别无UI识别对象
//使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this) {
            println("iat初始化结果:$it")
        }
//设置语法ID和 SUBJECT 为空，以免因之前有语法调用而设置了此参数；或直接清空所有参数，具体可参考 DEMO 的示例。
        mIat.setParameter(SpeechConstant.CLOUD_GRAMMAR, null);
        mIat.setParameter(SpeechConstant.SUBJECT, null);
//设置返回结果格式，目前支持json,xml以及plain 三种格式，其中plain为纯听写文本内容
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "plain");
//此处engineType为“cloud”
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");
//设置语音输入语言，zh_cn为简体中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//设置结果返回语言
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
// 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
//取值范围{1000～10000}
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
//设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
//自动停止录音，范围{0~10000}
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
//设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
    }


    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}