package com.hooitis.hoo.eyenosemouse.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View
import com.hooitis.hoo.eyenosemouse.R
import com.hooitis.hoo.eyenosemouse.base.BaseActivity
import com.hooitis.hoo.eyenosemouse.databinding.ActivityStartupBinding
import com.hooitis.hoo.eyenosemouse.di.ViewModelFactory
import com.hooitis.hoo.eyenosemouse.model.SharedPreferenceHelper
import com.hooitis.hoo.eyenosemouse.utils.*
import com.hooitis.hoo.eyenosemouse.vm.MainVM
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.*
import javax.inject.Inject


class QuizStartActivity: BaseActivity(), OnMapReadyCallback{

    @Inject
    lateinit var sharedPreferenceHelper: SharedPreferenceHelper

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainVM
    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var markerList: MutableList<Marker> = mutableListOf()
    private lateinit var selectedMarker: Marker

    private lateinit var binding: ActivityStartupBinding
    private val backButtonSubject: Subject<Long> = BehaviorSubject.createDefault(0L)
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    private lateinit var locationSource: FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_startup)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainVM::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        setContentView(binding.root)

        var naverMapFragment: MapFragment? = supportFragmentManager.findFragmentById(R.id.map) as MapFragment
        if(naverMapFragment == null){
            naverMapFragment = MapFragment.newInstance(NaverMapOptions()
                .camera(CameraPosition(NaverMap.DEFAULT_CAMERA_POSITION.target, 16.0, 40.0, 0.0))
            )
            UiUtils.addMapFragment(this, naverMapFragment, R.id.map)
        }

        naverMapFragment!!.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        sheetBehavior = BottomSheetBehavior.from<ConstraintLayout>(binding.bottomSheet.bottomSheet)
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        sheetBehavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }
                    BottomSheetBehavior.STATE_SETTLING-> {

                    }
                }
            }

            override fun onSlide(p0: View, p1: Float) {
            }

        })

        when(sharedPreferenceHelper.getInt(SharedPreferenceHelper.KEY.SEX)){
            0 -> binding.male.isChecked = true
            1 -> binding.female.isChecked = true
        }


        binding.apply {
            male.setOnClickListener {
                sharedPreferenceHelper.setInt(SharedPreferenceHelper.KEY.SEX, MALE)
                expandCloseSheet()
            }

            female.setOnClickListener {
                sharedPreferenceHelper.setInt(SharedPreferenceHelper.KEY.SEX, FEMALE)
            }

        }

        binding.imageDrag.apply {
            val manager = GridLayoutManager(this@QuizStartActivity, 3)
            val dividerItemDecoration = DividerItemDecoration(this@QuizStartActivity, manager.orientation)
            val callback = DragManageAdapter(viewModel.imageListAdapter, this@QuizStartActivity,
                ItemTouchHelper.UP.or(ItemTouchHelper.DOWN).or(ItemTouchHelper.LEFT).or(ItemTouchHelper.RIGHT), -1)
            val helper = ItemTouchHelper(callback)
            layoutManager = manager
            helper.attachToRecyclerView(this)
        }

        binding.startQuiz.setOnClickListener {
//            MediaPlayer.create(this, resources.getIdentifier("zing", "raw", packageName)).start()
            val intent = Intent(applicationContext, BeforeQuizActivity::class.java).apply {
                putExtra("SEX", sharedPreferenceHelper.getInt(SharedPreferenceHelper.KEY.SEX))
            }

            startActivity(intent)
        }


        viewModel.imageListAdapter.updateResultList(mutableListOf("abc", "abcd", "ad", "b", "c", "d", "e"))


        initBackPress()
    }

    private fun initBackPress(){
        backButtonSubject.toFlowable(BackpressureStrategy.BUFFER)
                .observeOn(AndroidSchedulers.mainThread())
                .buffer(2, 1)
                .map {
                    Pair<Long, Long>(it[0], it[1])
                }
                .doOnNext { t->
                    if (t != null && t.second - t.first < 1000) {
                        super.onBackPressed()
                    } else {
                        UiUtils.makeToast(binding.title, R.string.push_again_back_pressed)
                    }
                }.subscribe()
    }

    private fun expandCloseSheet(){
        if(!::sheetBehavior.isInitialized)
            return

        Log.d("BottomSheet", "Click")
        if(sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED){
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            Log.d("BottomSheet", "expand")
        }else{
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            Log.d("BottomSheet", "collapse")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)){
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed() {
        backButtonSubject.onNext(Calendar.getInstance().timeInMillis)
    }

    override fun onMapReady(naverMap: NaverMap) {
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, false)
//        naverMap.isLiteModeEnabled = true
        naverMap.locationSource = locationSource
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 5.0
        naverMap.uiSettings.apply {
            isScrollGesturesEnabled = true
            isTiltGesturesEnabled = false
            isZoomControlEnabled = false
            isIndoorLevelPickerEnabled = false
            isRotateGesturesEnabled = false
            isLocationButtonEnabled = true
            isLogoClickEnabled = false
            isCompassEnabled = false
        }
        naverMap.mapType = NaverMap.MapType.Basic


        for(i in 1..5) {
            Log.d("NaverMap", "$i ${37.5670135 + (i / 1000)} ${126.9783740 + (i / 1000)}")
            markerList.add(Marker().apply {
                position = LatLng(37.5670135 + (i.toDouble() / 1000), 126.9783740 + (i.toDouble() / 1000))
                map = naverMap
                icon = OverlayImage.fromResource(R.drawable.navermap_default_marker_icon_blue)
                setCaptionText("1개")
                captionOffset = -30
                setOnClickListener {
                    expandCloseSheet()
                    scrollToMap(naverMap, position)
                    clickMarker(this)
                    return@setOnClickListener true
                }
            }
            )
        }
//        markerList.add(Marker().apply {
//            position = LatLng(37.5671255, 126.9783750)
//            map = naverMap
//            icon = OverlayImage.fromResource(R.drawable.navermap_default_marker_icon_blue)
//            setOnClickListener {
//                expandCloseSheet()
//                scrollToMap(naverMap, position)
//                clickMarker(this)
//                return@setOnClickListener true
//            }
//        })
    }

    private fun clickMarker(marker: Marker){
        if(!::selectedMarker.isInitialized) {
            selectedMarker = marker
            marker.icon = OverlayImage.fromResource(R.drawable.navermap_default_marker_icon_green)
        }else{
            selectedMarker.icon = OverlayImage.fromResource(R.drawable.navermap_default_marker_icon_blue)
            marker.icon = OverlayImage.fromResource(R.drawable.navermap_default_marker_icon_green)
            selectedMarker = marker
        }


    }

    private fun scrollToMap(naverMap: NaverMap, latLng: LatLng){

        naverMap.moveCamera(
            CameraUpdate.scrollAndZoomTo(latLng, 14.0)
                .animate(CameraAnimation.Easing, 2000)
                .finishCallback {
                }
        )

    }
}