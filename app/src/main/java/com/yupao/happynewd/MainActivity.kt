package com.yupao.happynewd

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.yupao.happynewd.adapter.CoverGenerateHelper
import com.yupao.happynewd.model.ResumeDetailBaseParamsEntity
import com.yupao.happynewd.model.ResumePosterEntity
import com.yupao.happynewd.ui.TestCardViewActivity
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import com.yupao.happynewd.util.IMAvatarUtils
import com.yupao.happynewd.util.OSSImageUtil
import com.yupao.happynewd.view.ResizedImageView


class MainActivity : AppCompatActivity() {

    var bobName: String? = null
    val simple = SimpleSync()
    var startTime: Long = 0
    var skill: TextView? = null
    private val takeCode = 1001
    private var avatar: ImageView? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_coin_demo)
        avatar = findViewById<ImageView>(R.id.ivAvatar)
//        setupCoinDemo()
        buildPosterBitmap()
    }

    private fun setupCoinDemo() {
        val leftCoinText = findViewById<TextView>(R.id.leftCoinText)
        val rightCoinText = findViewById<TextView>(R.id.rightCoinText)
        val leftContainer = findViewById<FrameLayout>(R.id.leftCoinContainer)
        val rightContainer = findViewById<FrameLayout>(R.id.rightCoinContainer)
        val btnUpdateValues = findViewById<Button>(R.id.btnUpdateValues)
        val btnPlayAnimation = findViewById<Button>(R.id.btnPlayAnimation)

        // 更新数值按钮
        btnUpdateValues.setOnClickListener {
            val leftValue = (200..500).random()
            val rightValue = (400..800).random()
            leftCoinText.text = leftValue.toString()
            rightCoinText.text = rightValue.toString()
            Toast.makeText(this, "数值已更新: $leftValue / $rightValue", Toast.LENGTH_SHORT).show()
        }

        // 播放动画按钮
        btnPlayAnimation.setOnClickListener {
            // 左边金币动画
            leftContainer.apply {
                translationX = -300f
                alpha = 0f
                animate()
                    .translationX(0f)
                    .alpha(1f)
                    .setDuration(600)
                    .start()
            }

            // 右边金币动画
            rightContainer.apply {
                translationX = 300f
                alpha = 0f
                animate()
                    .translationX(0f)
                    .alpha(1f)
                    .setDuration(600)
                    .setStartDelay(200)
                    .start()
            }
        }

        // 金币点击事件
        leftContainer.setOnClickListener {
            Toast.makeText(this, "领取了 ${leftCoinText.text} 金币!", Toast.LENGTH_SHORT).show()
            it.animate().scaleX(1.2f).scaleY(1.2f).setDuration(150).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
            }.start()
        }

        rightContainer.setOnClickListener {
            Toast.makeText(this, "观看视频领取了 ${rightCoinText.text} 金币!", Toast.LENGTH_SHORT)
                .show()
            it.animate().scaleX(1.2f).scaleY(1.2f).setDuration(150).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
            }.start()
        }
    }

    var target = 500f

    @RequiresApi(Build.VERSION_CODES.N)
    fun displayAnimate(view: View) {
//        val image = findViewById<ImageView>(R.id.imgView)
//        val animate = image.animate()
//        animate.translationX(target)
//        animate.duration = 1000L
//        animate.interpolator = AccelerateInterpolator()
//        animate.setListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(animation: Animator?) {
//
//            }
//
//            override fun onAnimationEnd(animation: Animator?) {
//                target = if (target == 0f) 500f else 0f
//            }
//
//            override fun onAnimationCancel(animation: Animator?) {
//
//            }
//
//            override fun onAnimationRepeat(animation: Animator?) {
//
//            }
//
//        })
//        animate.start()

//        val f1 = 1.0f - 0.9f
//        val f2 = 0.9 - 0.8f
//        val df = 1e-6f
//        Log.e("MainActivity", "f1 $f1")
//        Log.e("MainActivity", "f2 $f2")
//        Log.e("MainActivity", "df $df")
//        Log.e("MainActivity", "ca1 ${abs(f1 - f2)}")
//        val ha = abs(f1 - f2) > df
//        Log.e("MainActivity", "has $ha")
//        val string = "a,b,c,d,"
//        val list = string.split(",")
//        Log.e("MainActivity", "split: $list")
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date())
//        Log.e("MainActivity", "date: $dateFormat")

//        val code = ""
//        println("code: " + code.toDouble().toString())

//        sortTypeName(bobName)

//        whenTest(5,"20")

//        for (i in 2..10 step 2) {
//            println(i)
//        }
//
//        val mapping = bobName?.let {
//            "$it not Null"
//        } ?: "bobIsNull"
//        println(mapping)
//        bobName = " 二"
//        println(bobName.toIntOrZero())
//
//        val list = List(3) {
//            println("index = $it")
//            it
//        }
//        println(list)
//        bobName = ""
//        val nume = try {
//            bobName?.toInt()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        println("nume = $nume")
//        findViewById<TextView>(R.id.tvShow)?.let { tv ->
//            startActivity(Intent(this@MainActivity, TestCardViewActivity::class.java))
//        }
//        val dy = (24 * 60 * 60 * 1000)
//        val start = getSharedPreferences(file, 0).getLong(TIME_KEY, System.currentTimeMillis())
//        val newT = System.currentTimeMillis()
//        val cha = newT - start
//        val yu: Double = (cha / dy).toDouble()
//        val real = (newT - start) >= (dy * 2)
//        Log.e("currentTime-hour-ms", "get: $start")
//        Log.e("currentTime-hour-ms", "dy: $dy")
//        Log.e("currentTime-hour-ms", "newT: $newT")
//        Log.e("currentTime-hour-ms", "cha: $cha")
//        Log.e("currentTime-hour-ms", "yu: $yu")
//        Log.e("currentTime-hour-ms", "real: $real")

//        makeList(false, hasBoolean = false)
//        val jiaJiaLe = JiaJiaLePaySystem()
//        val manager = Manager(jiaJiaLe)
//        manager.addStrategy(EightStrategy())
//        //
//        val cashier = CashierLi(jiaJiaLe)
//        cashier.startCheck("李白")
//        cashier.identityGood(
//            listOf(
//                Noodle(),
//                HuoTui(),
//                Tea(),
//                LuEgg()
//            )
//        )
//        val remain = cashier.settleAccounts(99.0)
//        Toast.makeText(this, "$remain", Toast.LENGTH_SHORT).show()

//        ActivityCompat.requestPermissions(this@MainActivity,
//            listOf<String>(Manifest.permission.CAMERA).toTypedArray(),1)
//        // 直接启动照相机，照相机照片将会存在默认的文件中
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent, takeCode)
//        startActivity(Intent(this@MainActivity, TestCardViewActivity::class.java))
        val parentView = findViewById<View>(R.id.clContainer)
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            showPreviewPopup(parentView)
        }
    }

    fun showCoinReward(view: View) {
        startActivity(Intent(this@MainActivity, CoinRewardActivity::class.java))
    }

    private fun showPreviewPopup(view: View) {
//        val popView =
//            LayoutInflater.from(this@MainActivity).inflate(R.layout.screen_shot_popup, null)
//        popView.measure(
//            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//        )
//        val popupWindow = PopupWindow(
//            popView,
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT, true
//        ).apply {
//            isOutsideTouchable = true
//            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        }
//        // /storage/emulated/0/DCIM/Screenshots/Screenshot_2025-04-10-19-47-44-020_io.dcloud.H576E6CC7.jpg
//        val preview = popView.findViewById<ImageView>(R.id.imgPreview)
//        preview.setImageURI(Uri.parse("file:///storage/emulated/0/DCIM/Screenshots/Screenshot_2025-04-10-19-47-44-020_io.dcloud.H576E6CC7.jpg"))
//        popView.findViewById<View>(R.id.imgClose).setOnClickListener {
//            popupWindow.dismiss()
//        }
//        val size = getPopupWindowPosition(popView)
//        popupWindow.showAsDropDown(view, size[0], size[1], Gravity.NO_GRAVITY)
//
        //
        val appInfo = applicationContext.packageManager.getApplicationInfo(
            applicationContext.packageName,
            PackageManager.GET_META_DATA
        )
        val label = applicationContext.packageManager.getApplicationLabel(appInfo).toString()
        println("label: $label")
    }

    private fun getPopupWindowPosition(popView: View): MutableList<Int> {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val position = mutableListOf<Int>()
        position.add(metrics.widthPixels - popView.measuredWidth - 10)
        position.add((metrics.heightPixels - popView.measuredHeight) / 2)
        return position
    }

    fun whenTest(x: Int, s: String) {
        val list = arrayOf(1, 10, 20, 100)
        when (x) {
            in 1..10 -> println("x in 1~10")
            in list -> println("x in list")
            !in 20..50 -> println("x !in 20~50")
            else -> println("else")
        }

        when (s) {
            is String -> println("s is String")
            else -> println("s is not String")
        }
    }

    private fun sortTypeName(work: String?) {
        work?.let {
            val print = if (work.isEmpty()) "work is Null" else work
            println(print)
        }
    }

    companion object {
        const val file = "config_file"
        const val TIME_KEY = "start_time"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun buildPosterBitmap() {
        val imageUrl =
            "https://static-test-public.cdqlkj.cn/r/8375/108/pb/p/20241011/db48869d7f684d3d98063219792425ef.jpeg"
        val entity = ResumePosterEntity(
            imgPath = "",
            resumeBaseEntity = ResumeDetailBaseParamsEntity(
                avatar = imageUrl,
                gender = 1,
                name = "阿里巴巴逾四十大盗",
                workAge = "10年工作经验",
                degree = "博士",
                isFuzzy = true,
                hopeCity = "北戴河",
                jobTitle = "工程院士",
                jobCompany = "中科院工程学研究所",
                introduction = "学术造诣高，有三层楼那么高！时间安排合理，按时完成工作，不拖延，不影响整体进度。",
            ),
        )
        avatar?.let {
            OSSImageUtil.loadResizedImage(
                this,
                imageView = it,
                imageUrl = entity.resumeBaseEntity?.avatar!!,
                placeHolder = this.getDrawable(R.drawable.ic_recruitment_head)!!,
                errorImg = this.getDrawable(R.drawable.ic_recruitment_head)!!,
                isFuzzy = true,
                width = it.width,
            )
        }
        findViewById<ResizedImageView>(R.id.ivRImageView)?.let {
            IMAvatarUtils.setAvatar(it, entity.resumeBaseEntity?.avatar)
        }
        if (entity.resumeBaseEntity != null) {
            lifecycleScope.launch {
                CoverGenerateHelper.generateResumeCover(
                    context = this@MainActivity,
                    this@MainActivity,
                    layoutInflater,
                    entity,
                    callBack = {
                        println("打印生成的图片 ====> , $it")
                    }
                )
            }
            return
        }
    }
}