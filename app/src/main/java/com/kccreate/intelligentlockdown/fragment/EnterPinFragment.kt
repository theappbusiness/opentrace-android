package com.kccreate.intelligentlockdown.fragment

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import com.kccreate.intelligentlockdown.BuildConfig
import com.kccreate.intelligentlockdown.R
import com.kccreate.intelligentlockdown.TracerApp
import com.kccreate.intelligentlockdown.Utils
import com.kccreate.intelligentlockdown.extensions.bindView
import com.kccreate.intelligentlockdown.logging.CentralLog
import com.kccreate.intelligentlockdown.status.persistence.StatusRecord
import com.kccreate.intelligentlockdown.status.persistence.StatusRecordStorage
import com.kccreate.intelligentlockdown.streetpass.persistence.StreetPassRecord
import com.kccreate.intelligentlockdown.streetpass.persistence.StreetPassRecordStorage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class EnterPinFragment : Fragment(R.layout.fragment_upload_enterpin) {

    private val firstPin by bindView<EditText>(R.id.enter_pin_first)
    private val secondPin by bindView<EditText>(R.id.enter_pin_second)
    private val thirdPin by bindView<EditText>(R.id.enter_pin_third)
    private val fourthPin by bindView<EditText>(R.id.enter_pin_fourth)
    private val fifthPin by bindView<EditText>(R.id.enter_pin_fifth)
    private val sixthPin by bindView<EditText>(R.id.enter_pin_sixth)
    private val enterPinErrorMessage by bindView<TextView>(R.id.enter_pin_error_message)
    private val enterPinButton by bindView<Button>(R.id.enter_pin_button)
    private val loadingContainer by bindView<View>(R.id.enter_pin_loading_container)
    private val errorContainer by bindView<View>(R.id.enter_pin_error_container)
    private val errorButton by bindView<View>(R.id.enter_pin_error_button)

    private var TAG = "EnterPinFragment"

    private var disposeObj: Disposable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstPin.requestFocus()
        Utils.showKeyboardFrom(firstPin.context, firstPin)

        firstPin.focusWhenChanged(next = secondPin)
        secondPin.focusWhenChanged(previous = firstPin, next = thirdPin)
        thirdPin.focusWhenChanged(previous = secondPin, next = fourthPin)
        fourthPin.focusWhenChanged(previous = thirdPin, next = fifthPin)
        fifthPin.focusWhenChanged(previous = fourthPin, next = sixthPin)
        sixthPin.focusWhenChanged(previous = fifthPin)

        sixthPin.afterNumberEntered {
            Utils.hideKeyboardFrom(sixthPin.context, sixthPin)
            enterPinButton.isEnabled = true
            sixthPin.clearFocus()
        }

        enterPinButton.setOnClickListener {
            val pin =
                "${firstPin.text}${secondPin.text}${thirdPin.text}${fourthPin.text}${fifthPin.text}${sixthPin.text}"
            uploadPin(pin)
        }

        errorButton.setOnClickListener {
            errorContainer.isVisible = false
        }

    }

    private fun EditText.focusWhenChanged(previous: EditText? = null, next: EditText? = null) {
        doAfterTextChanged {
            if (it?.length == 0) {
                previous?.requestFocus()
            } else {
                next?.requestFocus()
            }
        }
    }

    private fun EditText.afterNumberEntered(callback: () -> Unit) {
        doAfterTextChanged {
            if (it?.length == 1) {
                callback()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeObj?.dispose()
    }

    private fun uploadPin(pin: String) {
        loadingContainer.isVisible = true

        val observableStreetRecords = Observable.create<List<StreetPassRecord>> {
            val result = StreetPassRecordStorage(TracerApp.AppContext).getAllRecords()
            it.onNext(result)
        }
        val observableStatusRecords = Observable.create<List<StatusRecord>> {
            val result = StatusRecordStorage(TracerApp.AppContext).getAllRecords()
            it.onNext(result)
        }

        disposeObj = Observable.zip(observableStreetRecords, observableStatusRecords,

            BiFunction<List<StreetPassRecord>, List<StatusRecord>, ExportData> { records, status ->
                ExportData(
                    records,
                    status
                )
            }

        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { exportedData ->
                Log.d(TAG, "records: ${exportedData.recordList}")
                Log.d(TAG, "status: ${exportedData.statusList}")

                getUploadToken(pin).addOnSuccessListener {
                    val response = it.data as HashMap<String, String>
                    try {
                        val uploadToken = response["token"]
                        CentralLog.d(TAG, "uploadToken: $uploadToken")
                        val task = writeToInternalStorageAndUpload(
                            TracerApp.AppContext,
                            exportedData.recordList,
                            exportedData.statusList,
                            uploadToken
                        )
                        task.addOnFailureListener {
                            CentralLog.d(TAG, "failed to upload")
                            loadingContainer.isVisible = false
                            errorContainer.isVisible = true
                        }.addOnSuccessListener {
                            CentralLog.d(TAG, "uploaded successfully")
                            navigateToUploadSuccessScreen()
                        }
                    } catch (e: Throwable) {
                        CentralLog.d(TAG, "Failed to upload data: ${e.message}")
                        loadingContainer.isVisible = false
                        errorContainer.isVisible = true
                    }
                }.addOnFailureListener {
                    CentralLog.d(TAG, "Invalid code")
                    loadingContainer.isVisible = false
                    enterPinErrorMessage.isVisible = true
                }
            }
    }

    private fun navigateToUploadSuccessScreen() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.content, UploadSuccessFragment())
            .commit()
    }

    private fun getUploadToken(uploadCode: String): Task<HttpsCallableResult> {
        val functions = FirebaseFunctions.getInstance(BuildConfig.FIREBASE_REGION)
        return functions
            .getHttpsCallable("getUploadToken")
            .call(uploadCode)
    }

    private fun writeToInternalStorageAndUpload(
        context: Context,
        deviceDataList: List<StreetPassRecord>,
        statusList: List<StatusRecord>,
        uploadToken: String?
    ): UploadTask {
        val date = Utils.getDateFromUnix(System.currentTimeMillis())
        val gson = Gson()

        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL

        val updatedDeviceList = deviceDataList.map {
            it.timestamp = it.timestamp / 1000
            return@map it
        }

        val updatedStatusList = statusList.map {
            it.timestamp = it.timestamp / 1000
            return@map it
        }

        val map: MutableMap<String, Any> = HashMap()
        map["token"] = uploadToken as Any
        map["records"] = updatedDeviceList as Any
        map["events"] = updatedStatusList as Any

        val mapString = gson.toJson(map)

        val fileName = "StreetPassRecord_${manufacturer}_${model}_$date.json"
        val fileOutputStream: FileOutputStream

        val uploadDir = File(context.filesDir, "upload")

        if (uploadDir.exists()) {
            uploadDir.deleteRecursively()
        }

        uploadDir.mkdirs()
        val fileToUpload = File(uploadDir, fileName)
        fileOutputStream = FileOutputStream(fileToUpload)

        fileOutputStream.write(mapString.toByteArray())
        fileOutputStream.close()

        CentralLog.i(TAG, "File wrote: ${fileToUpload.absolutePath}")

        return uploadToCloudStorage(context, fileToUpload)
    }

    private fun uploadToCloudStorage(context: Context, fileToUpload: File): UploadTask {
        CentralLog.d(TAG, "Uploading to Cloud Storage")

        val bucketName = BuildConfig.FIREBASE_UPLOAD_BUCKET
        val storage = FirebaseStorage.getInstance("gs://${bucketName}")
        val storageRef = storage.getReferenceFromUrl("gs://${bucketName}")

        val streetPassRecordsRef =
            storageRef.child("streetPassRecords/${fileToUpload.name}")

        val fileUri: Uri =
            FileProvider.getUriForFile(
                context,
                "${BuildConfig.APPLICATION_ID}.fileprovider",
                fileToUpload
            )

        val uploadTask = streetPassRecordsRef.putFile(fileUri)
        uploadTask.addOnCompleteListener {
            try {
                fileToUpload.delete()
                CentralLog.i(TAG, "upload file deleted")
            } catch (e: Exception) {
                CentralLog.e(TAG, "Failed to delete upload file")
            }
        }
        return uploadTask
    }

}
