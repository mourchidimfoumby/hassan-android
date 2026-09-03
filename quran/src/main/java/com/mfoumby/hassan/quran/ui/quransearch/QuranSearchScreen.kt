package com.mfoumby.hassan.quran.ui.quransearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mfoumby.hassan.common.extension.mediumSpacing
import com.mfoumby.hassan.common.extension.smallSpacing
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.SearchTopBar
import com.mfoumby.hassan.common.ui.components.SectionTitle
import com.mfoumby.hassan.common.ui.components.SimpleLazyColumn
import com.mfoumby.hassan.common.ui.theme.padding
import com.mfoumby.hassan.quran.R
import com.mfoumby.hassan.quran.domain.SurahNumber
import com.mfoumby.hassan.quran.domain.entity.QuranSearchResult
import com.mfoumby.hassan.quran.domain.entity.QuranSearchResultType
import com.mfoumby.hassan.quran.domain.quranSearchResultFixtures
import com.mfoumby.hassan.quran.extension.labelResId
import com.mfoumby.hassan.quran.ui.SurahListItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun QuranSearchDestination(
    onBackClick: () -> Unit,
    onSurahClick: (SurahNumber) -> Unit,
    viewModel: QuranSearchViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    QuranSearchScreen(
        query = uiState.query,
        searchResults = uiState.searchResults,
        activeFilter = uiState.activeFilter,
        onQueryChange = viewModel::onQueryChange,
        onClearQueryClick = viewModel::onClearQuery,
        onSearchClick = viewModel::onSearch,
        onFilterClick = viewModel::onFilterClick,
        onSurahClick = onSurahClick,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuranSearchScreen(
    query: String,
    searchResults: List<QuranSearchResult>?,
    activeFilter: QuranSearchResultType?,
    onQueryChange: (String) -> Unit,
    onClearQueryClick: () -> Unit,
    onSearchClick: () -> Unit,
    onFilterClick: (QuranSearchResultType) -> Unit,
    onSurahClick: (SurahNumber) -> Unit,
    onBackClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            SearchTopBar(
                query = query,
                placeholder = stringResource(com.mfoumby.hassan.common.R.string.search),
                onQueryChange = onQueryChange,
                onSearchClick = {
                    focusManager.clearFocus()
                    onSearchClick()
                },
                onClearClick = onClearQueryClick,
                onBackClick = {
                    keyboardController?.hide()
                    onBackClick()
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (searchResults != null) {
                if (searchResults.any { activeFilter == null || it.type == activeFilter }) {
                    SearchResultContent(
                        searchResults = searchResults,
                        activeFilter = activeFilter,
                        onFilterClick = onFilterClick,
                        onSurahListItemClick = {
                            keyboardController?.hide()
                            onSurahClick(it)
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.padding.medium),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(com.mfoumby.hassan.common.R.string.no_result),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultContent(
    searchResults: List<QuranSearchResult>,
    activeFilter: QuranSearchResultType?,
    onFilterClick: (QuranSearchResultType) -> Unit,
    onSurahListItemClick: (SurahNumber) -> Unit
) {
    val itemCount = searchResults.sumOf {
        when (it) {
            is QuranSearchResult.SurahResult -> it.surahs.size
        }
    }

    Column(
        modifier = Modifier.padding(vertical = MaterialTheme.padding.medium),
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
            Row(
                modifier = Modifier.padding(horizontal = MaterialTheme.padding.medium),
                horizontalArrangement = Arrangement.smallSpacing()
            ) {
                QuranSearchResultType.entries.forEach { resultType ->
                    FilterChip(
                        selected = resultType == activeFilter,
                        onClick = { onFilterClick(resultType) },
                        label = { Text(text = stringResource(resultType.labelResId)) }
                    )
                }
            }
        }

        SimpleLazyColumn(
            modifier = Modifier.weight(1f),
            itemCount = itemCount
        ) {
            searchResults.forEach { result ->
                item {
                    when (result) {
                        is QuranSearchResult.SurahResult -> SectionTitle(
                            modifier = Modifier.padding(horizontal = MaterialTheme.padding.medium),
                            text = stringResource(R.string.surah)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.padding.medium))
                }

                when (result) {
                    is QuranSearchResult.SurahResult -> {
                        itemsIndexed(result.surahs) { index, surah ->
                            if (index > 0) {
                                Spacer(modifier = Modifier.height(MaterialTheme.padding.smallMedium))
                            }
                            SurahListItem(
                                modifier = Modifier
                                    .padding(horizontal = MaterialTheme.padding.medium)
                                    .clickable(onClick = { onSurahListItemClick(surah.number) })
                                    .clip(ShapeDefaults.Medium),
                                surah = surah,
                                colors = ListItemDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@PhonePreviews
@Composable
private fun QuranSearchScreenPreview() {
    Previews.Preview {
        QuranSearchScreen(
            query = "",
            searchResults = quranSearchResultFixtures,
            activeFilter = null,
            onQueryChange = {},
            onClearQueryClick = {},
            onSearchClick = {},
            onFilterClick = {},
            onSurahClick = {},
            onBackClick = {}
        )
    }
}