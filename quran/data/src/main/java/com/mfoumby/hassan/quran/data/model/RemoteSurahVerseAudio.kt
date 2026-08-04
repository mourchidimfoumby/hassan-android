package com.mfoumby.hassan.quran.data.model

import com.google.firebase.firestore.PropertyName
import com.mfoumby.hassan.quran.data.field.SurahVerseAudioField

data class RemoteSurahVerseAudio(
    @get:PropertyName(SurahVerseAudioField.Remote.RECITER_ID)
    @set:PropertyName(SurahVerseAudioField.Remote.RECITER_ID)
    var reciterId: String = "",
    @get:PropertyName(SurahVerseAudioField.Remote.AUDIO_SUB_FOLDER)
    @set:PropertyName(SurahVerseAudioField.Remote.AUDIO_SUB_FOLDER)
    var audioSubFolder: String = ""
)
