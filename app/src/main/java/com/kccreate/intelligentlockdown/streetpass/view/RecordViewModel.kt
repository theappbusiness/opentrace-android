package com.kccreate.intelligentlockdown.streetpass.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.kccreate.intelligentlockdown.streetpass.persistence.StreetPassRecord
import com.kccreate.intelligentlockdown.streetpass.persistence.StreetPassRecordDatabase
import com.kccreate.intelligentlockdown.streetpass.persistence.StreetPassRecordRepository

class RecordViewModel(app: Application) : AndroidViewModel(app) {

    private var repo: StreetPassRecordRepository

    var allRecords: LiveData<List<StreetPassRecord>>

    init {
        val recordDao = StreetPassRecordDatabase.getDatabase(app).recordDao()
        repo = StreetPassRecordRepository(recordDao)
        allRecords = repo.allRecords
    }


}
