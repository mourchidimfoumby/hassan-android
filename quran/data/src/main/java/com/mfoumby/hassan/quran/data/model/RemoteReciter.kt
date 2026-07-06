package com.mfoumby.hassan.quran.data.model

import com.google.firebase.firestore.PropertyName
import com.mfoumby.hassan.quran.data.field.ReciterField

data class RemoteReciter(
    @get:PropertyName(ReciterField.Remote.RECITER_ID)
    @set:PropertyName(ReciterField.Remote.RECITER_ID)
    var reciterId: String = "",
    @get:PropertyName(ReciterField.Remote.NAME)
    @set:PropertyName(ReciterField.Remote.NAME)
    var name: String = "",
    @get:PropertyName(ReciterField.Remote.IMAGE_NAME)
    @set:PropertyName(ReciterField.Remote.IMAGE_NAME)
    var imageName: String = "",
    @get:PropertyName(ReciterField.Remote.AUDIO_SUB_FOLDER)
    @set:PropertyName(ReciterField.Remote.AUDIO_SUB_FOLDER)
    var audioSubFolder: String = ""
)
