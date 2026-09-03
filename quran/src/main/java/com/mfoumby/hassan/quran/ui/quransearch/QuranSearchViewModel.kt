package com.mfoumby.hassan.quran.ui.quransearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfoumby.hassan.quran.domain.entity.QuranSearchResult
import com.mfoumby.hassan.quran.domain.entity.QuranSearchResultType
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuranSearchViewModel(
    private val surahRepository: SurahRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(QuranSearchUiState())
    val uiState = _uiState.asStateFlow()

    fun onQueryChange(query: String) {
        _uiState.update {
            it.copy(query = query)
        }
    }

    fun onClearQuery() {
        _uiState.update {
            it.copy(query = "")
        }
    }

    fun onFilterClick(resultType: QuranSearchResultType) {
        _uiState.update { state ->
            state.copy(activeFilter = resultType.takeUnless { it == state.activeFilter })
        }
    }

    fun onSearch() {
        val stateValue = uiState.value
        if (stateValue.query.isEmpty()) return
        viewModelScope.launch {
            val surahs = surahRepository.searchSurah(stateValue.query)
            val results = mutableListOf<QuranSearchResult>().apply {
                surahs.takeUnless { it.isEmpty() }?.let { add(QuranSearchResult.SurahResult(it)) }
            }
            _uiState.update {
                it.copy(searchResults = results)
            }
        }
    }

    data class QuranSearchUiState(
        val query: String = "",
        val searchResults: List<QuranSearchResult>? = null,
        val activeFilter: QuranSearchResultType? = null
    )
}