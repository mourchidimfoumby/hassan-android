package com.mfoumby.hassan.quran.ui.surahverse.reciters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.components.BackTopBar
import com.mfoumby.hassan.common.ui.components.SimpleAsyncImage
import com.mfoumby.hassan.common.ui.components.SimpleLazyColumn
import com.mfoumby.hassan.quran.R
import com.mfoumby.hassan.quran.domain.entity.Reciter
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.reciterFixtures
import com.mfoumby.hassan.quran.domain.surahVersePreferencesFixture
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecitersDestination(
    onBackClick: () -> Unit,
    viewModel: RecitersViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (!uiState.isLoading) {
        RecitersScreen(
            surahVersePreferences = uiState.preferences!!,
            reciters = uiState.reciters!!,
            onBackClick = onBackClick,
            onReciterClick = viewModel::onReciterClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecitersScreen(
    surahVersePreferences: SurahVersePreferences,
    reciters: List<Reciter>,
    onBackClick: () -> Unit,
    onReciterClick: (Reciter) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(R.string.reciters)
            )
        }
    ) { innerPadding ->
        SimpleLazyColumn(
            modifier = Modifier.padding(innerPadding),
            itemCount = reciters.size
        ) {
            items(reciters) { reciter ->
                ReciterItem(
                    reciter = reciter,
                    selected = reciter.id == surahVersePreferences.reciter?.id,
                    onClick = { onReciterClick(reciter) }
                )
            }
        }
    }
}

@Composable
private fun ReciterItem(
    reciter: Reciter,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent

    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        colors = ListItemDefaults.colors(containerColor = containerColor),
        headlineContent = {
            Text(text = reciter.name)
        },
        leadingContent = {
            SimpleAsyncImage(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                model = reciter.imageUrl,
                contentDescription = "Reciter image"
            )
        },
        trailingContent = {
            if (selected) {
                Icon(
                    painter = painterResource(com.mfoumby.hassan.common.R.drawable.ic_fill_check_circle),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
            } else {
                Icon(
                    painter = painterResource(com.mfoumby.hassan.common.R.drawable.ic_outline_circle),
                    tint = MaterialTheme.colorScheme.outlineVariant,
                    contentDescription = null
                )
            }
        }
    )
}

@PhonePreviews
@Composable private fun RecitersScreenPreview() {
    Previews.Preview {
        RecitersScreen(
            surahVersePreferences = surahVersePreferencesFixture,
            reciters = reciterFixtures,
            onBackClick = {},
            onReciterClick = {}
        )
    }
}