package com.benyq.guochat.media.video

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.abs

/**
 * @author benyqYe
 * date 2021/2/8
 * e-mail 1520063035@qq.com
 * description 加速度变化监听器，利用CaptureController感知生命周期
 */

class LifeCycleSensorManager(context: Context) : SensorEventListener {

    private val sensorManager: SensorManager = (context.getSystemService(Context.SENSOR_SERVICE) as SensorManager)
    private val sensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    fun onCreate() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun onDestroy() {
        sensorManager.unregisterListener(this)
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            if (abs(x) > 3 || abs(y) > 3) {
                if (abs(x) > abs(y)) {
                    VideoPictureCatcher.orientation = if (x > 0) 0 else 180
                } else {
                    VideoPictureCatcher.orientation = if (x > 0) 90 else 270
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}