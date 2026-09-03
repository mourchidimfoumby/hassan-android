package com.mfoumby.hassan.common.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import com.mfoumby.hassan.common.ui.PhonePreviews
import com.mfoumby.hassan.common.ui.Previews
import com.mfoumby.hassan.common.ui.theme.padding
import com.mfoumby.hassan.common.ui.theme.topAppBarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTopBar(
    title: String,
    actions: @Composable (RowScope.() -> Unit) = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1
            )
        },
        colors = MaterialTheme.colorScheme.topAppBarColor,
        actions = actions,
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    title: String,
    actions: @Composable (RowScope.() -> Unit) = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            BackButton(onClick = onBackClick)
        },
        colors = MaterialTheme.colorScheme.topAppBarColor,
        actions = actions,
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    query: String,
    placeholder: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onClearClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val inputField =
        @Composable {
            BasicTextField(
                modifier = Modifier.focusRequester(focusRequester),
                value = query,
                onValueChange = onQueryChange,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions { onSearchClick() },
                cursorBrush = SolidColor(TextFieldDefaults.colors().cursorColor),
                singleLine = true
            ) { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    innerTextField = innerTextField,
                    value = query,
                    placeholder = { Text(text = placeholder) },
                    enabled = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        cursorColor =  TextFieldDefaults.colors().cursorColor
                    ),
                    contentPadding = PaddingValues(horizontal = MaterialTheme.padding.small),
                    singleLine = true,
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(
                                onClick = onClearClick,
                                colors = IconButtonDefaults.iconButtonColors().copy(
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Icon(
                                    painter = painterResource(com.mfoumby.hassan.common.R.drawable.ic_outline_close),
                                    contentDescription = stringResource(com.mfoumby.hassan.common.R.string.clear)
                                )
                            }
                        }
                    },
                    visualTransformation = VisualTransformation.None,
                    interactionSource = remember { MutableInteractionSource() }
                )
            }
        }

    Column {
        CenterAlignedTopAppBar(
            title = {
                SearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    state = rememberSearchBarState(),
                    inputField = inputField,
                    colors = SearchBarDefaults.colors(
                        containerColor = Color.Transparent
                    )
                )
            },
            navigationIcon = {
                BackButton(
                    onClick = onBackClick,
                    color = IconButtonDefaults.iconButtonColors().copy(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        )

        HorizontalDivider()
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@OptIn(ExperimentalMaterial3Api::class)
@PhonePreviews
@Composable
private fun TitleTopBarPreview() {
    Previews.Preview {
        Surface {
            TitleTopBar(title = "Title")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PhonePreviews
@Composable
private fun BackTopBarPreview() {
    Previews.Preview {
        Surface {
            BackTopBar(
                onBackClick = {},
                title = "Title"
            )
        }
    }
}

@PhonePreviews
@Composable
private fun SearchTopBarPreview() {
    Previews.Preview {
        Surface {
            SearchTopBar(
                query = "",
                placeholder = "Search",
                onQueryChange = {},
                onSearchClick = {},
                onBackClick = {},
                onClearClick = {}
            )
        }
    }
}